<template>
  <el-dialog 
    v-model="form_store.resource_visibility" 
    :title="form_store.mode+(form_store.resource_mode=='init' ? '' : ' a new version of ')+' the resource'" 
    width="600"
    @closed="() => { formRef?.resetFields(); uploader?.clear(); video_len=undefined; }">
    
    <el-form
      ref="formRef"
      :model="form_store.resource_form"
      :rules="resource_rules"
      label-width="auto"
    >
      <el-form-item v-if="form_store.resource_mode=='init'" label="Name" prop="resource_name">
        <el-input v-model="form_store.resource_form.resource_name" placeholder="Enter the name"/>
      </el-form-item>

      <el-form-item label="Type" prop="resource_type">
        <el-segmented v-model="form_store.resource_form.resource_type" :options="['description', 'courseware', 'video', 'attachment']" />
      </el-form-item>

      <el-form-item label="Downloadable" prop="student_can_download">
        <el-switch v-model="form_store.resource_form.student_can_download" />
      </el-form-item>

      <el-form-item label="Version name" prop="resource_version_name">
        <el-input v-model="form_store.resource_form.resource_version_name" placeholder="Enter a version name"/>
      </el-form-item>

      <el-form-item v-if="form_store.mode=='Add'" label="File" prop="file_name">
        <FileUploader ref="uploader" @change="change"/>
      </el-form-item>

      <el-form-item v-if="video_len!==undefined" label="Watch time require">
        <el-input-number
          v-model="time_require"
          :min="0"
          :max="video_len"
          :step="60"
          :value-on-clear="'min'"
          controls-position="right"
        /><span style="margin-left: 8px;">/ {{ video_len }}</span>
      </el-form-item>

    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="form_store.reset_form(); uploader?.clear()">
          Reset
        </el-button>
        <el-button type="primary" @click="submitForm(formRef)">
          Confirm
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { useFormStore } from '@/stores/form';
import { useCourseStore } from '@/stores/course';
import { computed, reactive, ref } from 'vue';
import { CourseStatus, Publication, type CourseEntity } from '@/api/course/CourseAPI'
import { ChapterType, type ChapterEntity } from '@/api/course/ChapterAPI'
import { ResourceType, type ResourceEntity } from '@/api/course/CourseResourceAPI'
import { type FormInstance, type FormRules, ElMessage} from 'element-plus'
import { genFileId } from 'element-plus'
import type { UploadInstance, UploadProps, UploadRawFile } from 'element-plus'
import type FileUploader from '../FileUploader.vue';
import { setMinRequiredTimeCall } from '@/api/course/CheatCheckAPI';

const uploader = ref<InstanceType<typeof FileUploader>>()

const form_store = useFormStore()
const course_store = useCourseStore()

const video_len = ref<number|undefined>(undefined)
const time_require = ref(0)
function change(file: File|undefined) {
  if(file===undefined) {
    video_len.value = undefined
    time_require.value = 0
    return
  }
  if(!file.type.startsWith('video')) {
    video_len.value = undefined
    time_require.value = 0
    return
  }
  const link = URL.createObjectURL(file)
  const video = document.createElement('video')
  video.addEventListener("loadedmetadata", () => {
    video_len.value = Math.floor(video.duration)
  });
  video.src = link
}

const resource_rules = reactive<FormRules<ResourceEntity>>({
  resource_name: [
    { required: true, message: 'Please enter the name', trigger: 'blur', },
  ],
  resource_type: [
    { required: true, message: 'Please select a type', trigger: 'blur', },
  ],
  student_can_download: [
    { required: true, trigger: 'blur' }
  ],
  file_name: [{ 
      required: true, 
      message: 'Please select a file', 
      validator: () => {
        return (uploader.value?.file_get !== undefined)||(form_store.mode=='Edit')
      }, 
      trigger: 'blur' 
    }
  ],
  resource_version_name: [
    { required: true, message: 'Please enter a version name', trigger: 'blur', },
  ],
})

const formRef = ref<FormInstance>()
const submitForm = async (formIn: FormInstance | undefined) => {
  if (!formIn) return
  await formIn.validate(async (valid) => {
    if(!valid || (uploader.value?.file_get===undefined && form_store.mode=='Add')) {
      console.log('error submit!')
      return
    }
    if(uploader.value?.file_get!==undefined)
      form_store.resource_form.suffix = uploader.value.file_get.type+':'+uploader.value.file_get.name
    const id = await form_store.modify_resource(uploader.value?.file_get)
    if(id==undefined) {
      ElMessage({
        message: 'Network error',
        type: 'error',
      })
      return
    }

    ElMessage({
      message: "Success",
      type: "success",
    })
    console.log('submit!')

    if(video_len.value && video_len.value!==0) {
      const msg = await setMinRequiredTimeCall(id, {required_seconds: time_require.value})
      if(msg.code!=200) {
        ElMessage({
          message: 'set require time error',
          type: 'error',
        })
        return
      }
    }

    form_store.resource_visibility = false
    await course_store.load_from_route(true)
  })
}

</script>
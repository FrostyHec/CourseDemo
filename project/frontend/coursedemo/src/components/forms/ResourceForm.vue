<template>
  <el-dialog 
    v-model="form_store.resource_visibility" 
    :title="form_store.mode+(form_store.resource_mode=='init' ? '' : ' a new version of ')+' the resource'" 
    width="600"
    @closed="() => { formRef?.resetFields(); uploader?.clear() }">
    
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
        <FileUploader ref="uploader"/>
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

const uploader = ref<InstanceType<typeof FileUploader>>()

const form_store = useFormStore()
const course_store = useCourseStore()

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
  ]
})

const formRef = ref<FormInstance>()
const submitForm = async (formIn: FormInstance | undefined) => {
  if (!formIn) return
  await formIn.validate(async (valid) => {
    if(!valid || uploader.value?.file_get===undefined) {
      console.log('error submit!')
      return
    }
    form_store.resource_form.suffix = uploader.value.file_get.type+'\\'+uploader.value.file_get.name
    console.log(form_store.resource_form.suffix)
    if(!await form_store.modify_resource(uploader.value.file_get)) {
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
    form_store.resource_visibility = false
    await course_store.load_from_route(true)
  })
}

</script>
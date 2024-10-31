<template>
  <el-dialog 
    v-model="form_store.course_visibility" 
    :title="form_store.mode+' the course'" 
    width="600"
    :before-close="(done) => { formRef?.resetFields(); done(); }">
    
    <el-form
      ref="formRef"
      :model="form_store.course_form"
      :rules="course_rules"
      label-width="auto"
    >
      <el-form-item label="Name" prop="course_name">
        <el-input v-model="form_store.course_form.course_name" placeholder="Enter the name"/>
      </el-form-item>

      <el-form-item label="Publication" prop="publication">
        <el-segmented v-model="form_store.course_form.publication" :options="['open', 'closed', 'semi open']" />
      </el-form-item>

      <el-form-item label="Description" prop="description">
        <el-input v-model="form_store.course_form.description" type="textarea" placeholder="Enter the description"/>
      </el-form-item>

    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="form_store.reset_form()">
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


const form_store = useFormStore()
const course_store = useCourseStore()

const course_rules = reactive<FormRules<CourseEntity>>({
  course_name: [
    { required: true, message: 'Please enter the name', trigger: 'blur', },
  ],
  publication: [
    { required: true, message: 'Please select a public type' , trigger: 'blur', },
  ],
})

const formRef = ref<FormInstance>()

const resetForm = (formIn: FormInstance | undefined) => {
  if (!formIn) return
  formIn.resetFields()
}

const submitForm = async (formIn: FormInstance | undefined) => {
  if (!formIn) return
  await formIn.validate(async (valid) => {
    if(!valid) {
      console.log('error submit!')
      return
    }
    if(!await form_store.modify_course()) {
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
    form_store.course_visibility = false
    await course_store.load_from_route(true)
  })
}

</script>
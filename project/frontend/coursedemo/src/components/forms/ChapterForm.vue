<template>
  <el-dialog 
    v-model="form_store.chapter_visibility" 
    :title="form_store.mode+' the chapter'" 
    width="600"
    @closed="() => { formRef?.resetFields(); }">
    
    <el-form
      ref="formRef"
      :model="form_store.chapter_form"
      :rules="chapter_rules"
      label-width="auto"
    >
      <el-form-item label="Title" prop="chapter_title">
        <el-input v-model="form_store.chapter_form.chapter_title" placeholder="Enter the name"/>
      </el-form-item>

      <el-form-item label="Type" prop="chapter_type">
        <el-segmented v-model="form_store.chapter_form.chapter_type" :options="['teaching', 'assignment', 'project']" />
      </el-form-item>

      <el-form-item label="Visibility" prop="visible">
        <el-switch v-model="form_store.chapter_form.visible" />
      </el-form-item>

      <el-form-item label="Publication" prop="publication">
        <el-switch v-model="form_store.chapter_form.publication" />
      </el-form-item>

      <el-form-item label="Description" prop="content">
        <el-input v-model="form_store.chapter_form.content" type="textarea" placeholder="Enter the description"/>
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

const form_store = useFormStore()
const course_store = useCourseStore()

const chapter_rules = reactive<FormRules<ChapterEntity>>({
  chapter_title: [
    { required: true, message: 'Please enter the title', trigger: 'blur', },
  ],
  chapter_type: [
    { required: true, message: 'Please select a type', trigger: 'blur', },
  ],
})

const formRef = ref<FormInstance>()

const submitForm = async (formIn: FormInstance | undefined) => {
  if (!formIn) return
  await formIn.validate(async (valid) => {
    if(!valid) {
      console.log('error submit!')
      return
    }
    if(!await form_store.modify_chapter()) {
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
    form_store.chapter_visibility = false
    await course_store.load_from_route(true)
  })
}

</script>
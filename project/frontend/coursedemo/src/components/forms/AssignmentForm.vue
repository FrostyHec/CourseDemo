<template>
  <el-dialog 
    v-model="form_store.assignment_visibility" 
    :title="form_store.mode+' the assignment'" 
    width="600"
    @closed="() => { formRef?.resetFields(); }">
    
    <el-form
      ref="formRef"
      :model="form_store.assignment_form"
      :rules="assignment_rules"
      label-width="auto"
    >
    <el-form-item label="Name" prop="assignment_name">
        <el-input v-model="form_store.assignment_form.assignment_name" type="textarea" placeholder="Enter the name"/>
      </el-form-item>

      <el-form-item label="Description" prop="description">
        <el-input v-model="form_store.assignment_form.description" type="textarea" placeholder="Enter the description"/>
      </el-form-item>

      <el-form-item label="View score allow" prop="allow_student_to_view_score">
        <el-switch v-model="form_store.assignment_form.allow_student_to_view_score" />
      </el-form-item>

      <el-form-item label="Resubmit allow" prop="allow_update_submission">
        <el-switch v-model="form_store.assignment_form.allow_update_submission" />
      </el-form-item>

      <el-form-item label="Deadline" prop="latest_submission_time">
        <el-date-picker
          v-model="form_store.assignment_form.latest_submission_time"
          type="datetime"
          placeholder="Select date and time"
          :default-time="new Date()"
        />
      </el-form-item>

      <el-form-item label="Score" prop="maximum_score">
        <el-input-number v-model="form_store.assignment_form.maximum_score" :min="0"/>
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
import type { AssignmentEntity } from '@/api/course/AssignmentAPI';


const form_store = useFormStore()

const assignment_rules = reactive<FormRules<AssignmentEntity>>({
  assignment_name: [
    { required: true, message: 'Please enter a name', trigger: 'blur', },
  ],
  maximum_score: [
    { required: true, message: 'Please select a maximum score', trigger: 'blur', }
  ]
})

const formRef = ref<FormInstance>()

const emit = defineEmits({
  afterSubmit: () => true
})

const submitForm = async (formIn: FormInstance | undefined) => {
  if (!formIn) return
  await formIn.validate(async (valid) => {
    if(!valid) {
      console.log('error submit!')
      return
    }
    if(!await form_store.modify_assignment()) {
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
    emit('afterSubmit')
    form_store.assignment_visibility = false
  })
}

</script>
<template>
  <div v-if="is_assignment_chapter">
    <div style="display: flex; align-items: baseline;">
      <h1 style="font-size: large; margin-bottom: 0;">
        {{ is_project ? "Project assignments" : "Assignment" }}
      </h1>
      <p style="font-size: medium; margin-left: 8px; color: var(--ep-color-primary); margin-bottom: 0;">
        {{ assignments.length }}
      </p>
    </div>

    <el-button 
      v-if="course_store.current_course_teacher()===auth_store.user.user_id" 
      style="margin-top: 20px;" type="primary" @click="open_form(undefined, 'Add')">
      Add new assignment
    </el-button>

    <div v-for="c in assignments" :key="c.data.assignment_id" style="width: 100%; margin-top: 20px; border-top: solid 1px var(--ep-border-color); padding-top: 18px; overflow: hidden;">

      <div style="float: left;">
        <div style="overflow: hidden; display: flex; align-items: center; gap: 8px;">
          <div i="ep-edit-pen"/>
          <span style="line-height: 1.5; white-space-collapse: break-spaces; font-weight: 600;">
            {{ c.data.assignment_name }}
          </span>
        </div>

        <div style="margin: 15px; margin-top: 5px; color: var(--ep-color-primary);">{{ c.data.description }}</div>

        <div style="overflow: hidden; display: flex; align-items: center; gap: 8px; margin-top: 6pt;">
          <div i="ep-clock"/>
          <span style="white-space-collapse: break-spaces;;">
            {{ 'Deadline:' }}
          </span>
          <span style="white-space-collapse: break-spaces; color: var(--ep-color-primary); font-weight: bold;">
            {{ (new Date(c.data.latest_submission_time)).toLocaleString() }}
          </span>
        </div>

        <div style="overflow: hidden; display: flex; align-items: center; gap: 8px; margin-top: 6pt;">
          <div i="ep-histogram"/>
          <span style="white-space-collapse: break-spaces;">
            {{ 'Total score:' }}
          </span>
          <span style="white-space-collapse: break-spaces; color: var(--ep-color-primary); font-weight: bold;">
            {{ c.data.maximum_score }}
          </span>
        </div>

        <div style="display: flex; gap: 10px; flex-direction: row; align-items: center; margin-top: 16px; font-size: small;">
          <div style="color: var(--ep-text-color-placeholder); width: 130px;">  
            {{ (new Date(c.data.updated_at)).toLocaleString() }}
          </div>
          <el-link
            v-if="course_store.current_course_teacher()===auth_store.user.user_id"
            type="primary" :underline="false" style="font-size: small;" @click="open_form(c.data, 'Edit')">
            Update
          </el-link>
          <el-link 
            v-if="course_store.current_course_teacher()===auth_store.user.user_id" 
            type="danger" :underline="false" style="font-size: small;" @click="del(c.data)">
            Delete
          </el-link>
        </div>
      </div>
      
      <el-button 
        v-if="course_store.current_course_teacher()===auth_store.user.user_id" 
        style="float: right; margin-right: 20px;"
        @click="router.push('/assignment/' + c.data.assignment_id)">
        View all submissions <span style="margin-left: 5px; margin-right: -12px;" i="ep-arrow-right"/>
      </el-button>
      <div v-else style="display: flex; float: right; flex-direction: column; align-items: flex-end; margin-right: 20px;">
        <div style="margin-bottom: 20px;">
          <span :style="c.submitted!==undefined ? 'color: var(--ep-color-success)' : 'color: var(--ep-color-danger)'">
            {{ c.submitted!==undefined ? "Submitted" : "No submission" }}
          </span>
        </div>
        <div>
          <el-button 
            v-if="c.submitted===undefined" 
            style="float: right; width: 100px; margin-left: 10px" type="primary" 
            @click="submit_visibility=true; current_assignment=c.data.assignment_id"
          >
            Submit
          </el-button>
          <el-button 
            v-if="c.submitted!==undefined && c.data.allow_update_submission" 
            style="float: right; width: 100px; margin-left: 10px" type="primary" 
            @click="submit_visibility=true; current_assignment=c.data.assignment_id"
          >
            Resubmit
          </el-button>
          <el-button
            v-if="c.submitted!==undefined" style="float: right;"
            @click="router.push('/assignment/submission/' + c.submitted)"
          >
            View your submission <span style="margin-left: 5px; margin-right: -12px;" i="ep-arrow-right"/>
          </el-button>
        </div>
      </div>
        
    </div>
    <div style="width: 100%; margin-top: 20px; border-top: solid 1px var(--ep-border-color); padding-top: 18px;"/>
  
    <AssignmentForm @after-submit="load_assignment"/>
    <el-dialog
      v-model="submit_visibility"
      title="Submit the assignment"
      width="600"
      @closed="uploader?.clear()"
    >
      <FileUploader ref="uploader"/>
      <template #footer>
      <div class="dialog-footer">
        <el-button @click="uploader?.clear()">
          Reset
        </el-button>
        <el-button type="primary" @click="submit">
          Confirm
        </el-button>
      </div>
    </template>
    </el-dialog>

  </div>
</template>

<script lang='ts' setup>
import { reactive, ref, watch } from 'vue'
import { useCourseStore } from '@/stores/course';
import { type CourseEntity } from '@/api/course/CourseAPI'
import { ChapterType, type ChapterEntity } from '@/api/course/ChapterAPI'
import type { ResourceEntityPlus } from '@/stores/course';
import { ElMessage } from 'element-plus';
import { useAuthStore } from '@/stores/auth';
import { deleteAssignmentCall, getAssignmentCall, getAssignmentsByChapterIdCall, type AssignmentEntity } from '@/api/course/AssignmentAPI';
import { useFormStore } from '@/stores/form';
import { getStudentSubmissionCall, submitFileCall, type FileSubmissionEntity } from '@/api/course/FileSubmissionAPI';
import type FileUploader from './FileUploader.vue';
import { useRoute, useRouter } from 'vue-router';

const course_store = useCourseStore()
const auth_store = useAuthStore()
const form_store = useFormStore()
const is_assignment_chapter = ref(false)
const is_project = ref(false)
let current_chapter_id = -1
const assignments = ref<{data: AssignmentEntity, submitted?: number}[]>([])

const open_form = (data: AssignmentEntity|undefined, mode: 'Add'|'Edit') => {
  if(mode=='Edit') {
    if(data!==undefined)
      form_store.open_form(data, mode)
  }
  else {
    if(!is_assignment_chapter.value)
      return
    let temp: AssignmentEntity = {...form_store.assignment_null}
    temp.chapter_id = current_chapter_id
    form_store.open_form(temp, mode)
  }
}

async function load_assignment() {
  const msg = await getAssignmentsByChapterIdCall(current_chapter_id)
  if(msg.code!=200) {
    ElMessage({
      message: 'Announcement network error',
      type: 'error',
    })
    return
  }
  console.log(msg.data)
  assignments.value = []
  for(const a of msg.data.content) {
    const sub_msg = await getStudentSubmissionCall(a.assignment_id)
    if(sub_msg.code==200)
      assignments.value.push({data: a, submitted: sub_msg.data.file_submission.file_submission_id})
    else
      assignments.value.push({data: a})
  }
}
const watch_current_data = watch(() => course_store.current_data?.data,
  async (new_data: CourseEntity|ChapterEntity|ResourceEntityPlus|undefined) => {
    is_assignment_chapter.value = false
    is_project.value = false
    if(!new_data || !('chapter_title' in new_data))
      return
    is_assignment_chapter.value = new_data.chapter_type==ChapterType.assignment || new_data.chapter_type==ChapterType.project
    is_project.value = new_data.chapter_type==ChapterType.project
    if(!is_assignment_chapter.value || new_data.chapter_id==current_chapter_id)
      return
    current_chapter_id = new_data.chapter_id
    await load_assignment()
  },
  { immediate: true }
)

async function del(data: AssignmentEntity) {
  const msg = await deleteAssignmentCall(data.assignment_id)
  if(msg && msg.code!=200) {
    ElMessage({
      message: 'delete assignment network error',
      type: 'error',
    })
    return
  }
  await load_assignment()
}

const submit_visibility = ref(false)
const uploader = ref<InstanceType<typeof FileUploader>>()
let current_assignment: number = -1

async function submit() {
  if(uploader.value?.file_get===undefined)
    return
  const file_submit: FileSubmissionEntity = {
    file_submission_id: 0,
    assignment_id: current_assignment,
    student_id: auth_store.user.user_id,
    suffix: uploader.value.file_get.type+':'+uploader.value.file_get.name,
    file_name: '',
    gained_score: 0,
    created_at: new Date(),
    updated_at: new Date(),
  }
  const msg = await submitFileCall(current_assignment, file_submit, uploader.value.file_get)
  if(msg.code!==200) {
    ElMessage({
      message: 'Assignment submit network error',
      type: 'error',
    })
    return
  }
  submit_visibility.value = false
  await load_assignment()
}

const router = useRouter()

</script>
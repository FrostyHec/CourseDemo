<template>
<div style="margin: 40px;">

  <AssignmentForm @after-submit="load"/>
  <div style="width: 100%; margin-top: 20px; overflow: hidden;">

    <div style="float: left;">
      <div style="overflow: hidden; display: flex; align-items: center; gap: 8px; font-size: larger;">
        <div i="ep-edit-pen"/>
        <span style="line-height: 1.5; white-space-collapse: break-spaces; font-weight: 600;">
          {{ assignment_data?.assignment_name }}
        </span>
      </div>

      <div style="margin: 15px; margin-top: 5px; color: var(--ep-color-primary);">{{ assignment_data?.description }}</div>

      <div style="display: flex; gap: 5px; flex-direction: row; align-items: baseline; margin-top: 12pt;">
        <div style="font-weight: bold; margin-top: 5px;">
          {{ student_data?.first_name+' '+student_data?.last_name }}
        </div>
        <div style="font-size: small; color: var(--ep-color-primary-light-3);">
          {{ student_data?.email }}
        </div>  
      </div>

      <div style="display: flex; gap: 5px; flex-direction: row; align-items: center; margin-top: 12px;">
        <div i="ep-histogram"/>
        <span> Score: </span>
        <el-input-number
          v-if="is_teacher"
          v-model="submission_data.file_submission.gained_score"
          :min="0"
          :max="assignment_data?.maximum_score"
          :step="1"
          :value-on-clear="'min'"
          controls-position="right"
          style="margin-left: 6px;"
          @change="update_score(submission_data.file_submission)"
        />
        <div v-else style="font-weight: bold; margin-left: 6px;">{{ submission_data.file_submission.gained_score }}</div>
        <div style="margin-left: 4px; margin-right: 4px;">/</div>
        <div style="font-weight: bold; color: var(--ep-color-primary);">{{ assignment_data?.maximum_score }}</div>
      </div>
      <div style="margin-top: 15px; font-size: small; color: var(--ep-text-color-placeholder);">
        {{ (new Date(submission_data.file_submission.updated_at)).toLocaleString() }}
      </div>
    </div>
    
    <div v-if="is_teacher" style="float: right; margin-right: 20px;">
      <el-button 
        :disabled="current_index===0" 
        type="primary" circle
        @click="router.push('/assignment/submission/' + submission_list[current_index-1].file_submission_id)"
      >
        <span i="ep-caret-left"/>
      </el-button>
      <el-button 
        :disabled="current_index===submission_list.length-1" 
        type="primary" circle style="margin-right: 20px;"
        @click="router.push('/assignment/submission/' + submission_list[current_index+1].file_submission_id)"
      >
        <span i="ep-caret-right"/>
      </el-button>
      <el-button
        @click="back">
        <span style="margin-left: -12px; margin-right: 5px;" i="ep-arrow-left"/>Back to list
      </el-button>
    </div>
    <el-button
      v-else
      style="float: right; margin-right: 20px;"
      @click="router.push('/course' + '/' + course_data?.course_id + '/' + chapter_data?.chapter_title.replace(/ /g, '-'))">
      <span style="margin-left: -12px; margin-right: 5px;" i="ep-arrow-left"/>Back to chapter
    </el-button>

  </div>
  <div style="width: 100%; margin-top: 20px; border-top: solid 1px var(--ep-border-color); padding-top: 18px;"/>
  <FileDisplay
    :suffix="submission_data.file_submission.suffix"
    :file_name="submission_data.file_submission.file_name"
    :access_key="submission_data.access_key"
  />
</div>
</template>
<script setup lang="ts">
import { deleteAssignmentCall, getAssignmentCall, type AssignmentEntity } from '@/api/course/AssignmentAPI';
import { getChapterCall, type ChapterEntity } from '@/api/course/ChapterAPI';
import { getCourseCall, type CourseEntity } from '@/api/course/CourseAPI';
import { type FileSubmissionWithAccessKey, getAllSubmissionCall, updateScoreCall, type FileSubmissionEntity, getStudentSubmissionCall, getFileSubmissionCall } from '@/api/course/FileSubmissionAPI';
import { getUserPublicInfoCall, type UserEntity, type UserPublicInfoEntity } from '@/api/user/UserAPI';
import { useAuthStore } from '@/stores/auth';
import { useCourseStore } from '@/stores/course';
import { useFormStore } from '@/stores/form';
import { ElMessage } from 'element-plus';
import { reactive, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const auth_store = useAuthStore()
const form_store = useFormStore()

const route = useRoute()
let submission_id = Number(route.params.submission_id)
const assignment_data = ref<AssignmentEntity>()
const chapter_data = ref<ChapterEntity>()
const course_data = ref<CourseEntity>()
const submission_data = ref<FileSubmissionWithAccessKey>({
  file_submission: {
    file_submission_id: 0,
    assignment_id: 0,
    student_id: 0,
    suffix: '',
    file_name: '',
    gained_score: 0,
    created_at: new Date(),
    updated_at: new Date(),
  },
  access_key: ''
})
const student_data = ref<UserPublicInfoEntity>()
const submission_list = ref<FileSubmissionEntity[]>([])
const is_teacher = ref(false)
const current_index = ref(0)

async function load() {
  submission_data.value = (await getFileSubmissionCall(submission_id)).data
  const assignment_id = submission_data.value.file_submission.assignment_id
  assignment_data.value = (await getAssignmentCall(assignment_id)).data
  chapter_data.value = (await getChapterCall(assignment_data.value.chapter_id)).data
  course_data.value = (await getCourseCall(chapter_data.value.course_id)).data
  student_data.value = (await getUserPublicInfoCall(submission_data.value.file_submission.student_id)).data
  submission_list.value = (await getAllSubmissionCall(assignment_id)).data.content
  is_teacher.value = course_data.value.teacher_id===auth_store.user.user_id

  for(let i=0; i<submission_list.value.length; i++)
    if(submission_list.value[i].file_submission_id===submission_data.value.file_submission.file_submission_id) {
      current_index.value = i
      break
    }
}
const watch_id = watch(() => route.params.submission_id,
  async (new_id) => {
    submission_id = Number(new_id)
    await load()
  },
  {immediate: true}
)

async function update_score(data: FileSubmissionEntity) {
  const msg = await updateScoreCall(data.file_submission_id, {gained_score: data.gained_score})
  if(msg.code!=200) {
    ElMessage({
      message: 'update score network error',
      type: 'error',
    })
    return
  }
}

const open_form = (data: AssignmentEntity|undefined, mode: 'Add'|'Edit') => {
  if(mode=='Edit') {
    if(data!==undefined)
      form_store.open_form(data, mode)
  }
  else {
    if(!assignment_data.value)
      return
    let temp: AssignmentEntity = {...form_store.assignment_null}
    temp.chapter_id = assignment_data.value.chapter_id
    form_store.open_form(temp, mode)
  }
}

const router = useRouter()

function back() {
  router.push('/assignment/' + assignment_data.value?.assignment_id)
}

</script>
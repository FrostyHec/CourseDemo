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

      <div style="overflow: hidden; display: flex; align-items: center; gap: 8px; margin-top: 12pt;">
        <div i="ep-clock"/>
        <span style="white-space-collapse: break-spaces;;">
          {{ 'Deadline:' }}
        </span>
        <span style="white-space-collapse: break-spaces; color: var(--ep-color-primary); font-weight: bold;">
          {{ assignment_data ? (new Date(assignment_data.latest_submission_time)).toLocaleString() : '' }}
        </span>
      </div>

      <div style="overflow: hidden; display: flex; align-items: center; gap: 8px; margin-top: 6pt;">
        <div i="ep-histogram"/>
        <span style="white-space-collapse: break-spaces;;">
          {{ 'Total score:' }}
        </span>
        <span style="white-space-collapse: break-spaces; color: var(--ep-color-primary); font-weight: bold;">
          {{ assignment_data?.maximum_score }}
        </span>
      </div>

      <div style="display: flex; gap: 10px; flex-direction: row; align-items: center; margin-top: 12px; font-size: small; margin-bottom: 16px;">
        <div style="color: var(--ep-text-color-placeholder); width: 130px;">  
          {{ assignment_data ? (new Date(assignment_data.updated_at)).toLocaleString() : '' }}
        </div>
        <el-link
          v-if="course_data?.teacher_id===auth_store.user.user_id"
          type="primary" :underline="false" style="font-size: small;" @click="open_form(assignment_data, 'Edit')">
          Update
        </el-link>
        <el-link 
          v-if="course_data?.teacher_id===auth_store.user.user_id" 
          type="danger" :underline="false" style="font-size: small;" @click="del(assignment_data)">
          Delete
        </el-link>
      </div>
    </div>

    <el-button
      style="float: right; margin-right: 20px;"
      @click="back">
      <span style="margin-left: -12px; margin-right: 5px;" i="ep-arrow-left"/>Back to chapter
    </el-button>

  </div>

  <div v-for="c in list_with_student" :key="c.submission.file_submission_id" style="width: 100%; margin-top: 20px; border-top: solid 1px var(--ep-border-color); padding-top: 18px; display: flex; overflow: hidden;">
    <div style="overflow: hidden; width: 100%;">
      <div style="display: flex; gap: 5px; flex-direction: row; align-items: baseline;">
        <div style="font-weight: bold; margin-top: 5px;">
          {{ c.student.first_name+' '+c.student.last_name }}
        </div>
        <div style="font-size: small; color: var(--ep-color-primary-light-3);">
          {{ c.student.email }}
        </div>  
      </div>
      <div style="display: flex; gap: 5px; flex-direction: row; align-items: center; margin-top: 12px;">
        <div i="ep-histogram"/>
        <el-input-number
          v-model="c.submission.gained_score"
          :min="0"
          :max="assignment_data?.maximum_score"
          :step="1"
          :value-on-clear="'min'"
          controls-position="right"
          style="margin-left: 5px;"
          @change="update_score(c.submission)"
        />
        <div style="margin-left: 8px;">/</div>
        <div style="font-weight: bold; color: var(--ep-color-primary);">{{ assignment_data?.maximum_score }}</div>
      </div>
      <div style="margin-top: 15px; font-size: small; color: var(--ep-text-color-placeholder);">
        {{ (new Date(c.submission.updated_at)).toLocaleString() }}
      </div>
    </div>
    <el-button 
      style="float: right; margin-right: 20px;"
      @click="router.push('/assignment/submission/' + c.submission.file_submission_id)">
      View this submission <span style="margin-left: 5px; margin-right: -12px;" i="ep-arrow-right"/>
    </el-button>
  </div>
  <div style="width: 100%; margin-top: 20px; border-top: solid 1px var(--ep-border-color); padding-top: 18px;"/>
</div>
</template>
<script setup lang="ts">
import { deleteAssignmentCall, getAssignmentCall, type AssignmentEntity } from '@/api/course/AssignmentAPI';
import { getChapterCall, type ChapterEntity } from '@/api/course/ChapterAPI';
import { getCourseCall, type CourseEntity } from '@/api/course/CourseAPI';
import { getAllSubmissionCall, updateScoreCall, type FileSubmissionEntity } from '@/api/course/FileSubmissionAPI';
import { getUserPublicInfoCall, type UserEntity, type UserPublicInfoEntity } from '@/api/user/UserAPI';
import { useAuthStore } from '@/stores/auth';
import { useCourseStore } from '@/stores/course';
import { useFormStore } from '@/stores/form';
import { ElMessage, ElScrollbar } from 'element-plus';
import { reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const auth_store = useAuthStore()
const form_store = useFormStore()

const route = useRoute()
const assignment_id = Number(route.params.assignment_id)
const assignment_data = ref<AssignmentEntity>()
const chapter_data = ref<ChapterEntity>()
const course_data = ref<CourseEntity>()
const list_with_student: {submission: FileSubmissionEntity, student: UserPublicInfoEntity}[] = reactive([])

async function load() {
  assignment_data.value = (await getAssignmentCall(assignment_id)).data
  chapter_data.value = (await getChapterCall(assignment_data.value.chapter_id)).data
  course_data.value = (await getCourseCall(chapter_data.value.course_id)).data
  if(course_data.value?.teacher_id===auth_store.user.user_id) {
    const submission_list = (await getAllSubmissionCall(assignment_id)).data.content
    for(const sub of submission_list) {
      list_with_student.push({
        submission: sub,
        student: (await getUserPublicInfoCall(sub.student_id)).data
      })
    }
  }
}
load()

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
async function del(data: AssignmentEntity|undefined) {
  if(!data) return
  const msg = await deleteAssignmentCall(data.assignment_id)
  if(msg.code!=200) {
    ElMessage({
      message: 'delete assignment network error',
      type: 'error',
    })
    return
  }
  back()
}

function back() {
  router.push('/course' + '/' + course_data.value?.course_id + '/' + chapter_data.value?.chapter_title.replace(/ /g, '-'))
}

</script>
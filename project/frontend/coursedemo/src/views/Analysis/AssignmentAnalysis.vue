<template>
<div style="margin: 40px;">
  <el-button
    style="float: right; margin-right: 20px;"
    @click="router.push('/course' + '/' + course_data?.course_id)">
    <span style="margin-left: -12px; margin-right: 5px;" i="ep-arrow-left"/>Back to course
  </el-button>
  <div style="margin-bottom: 20px; font-size: x-large; font-weight: bold;">
    {{ course_data?.course_name }} - Assignments
  </div>

  <div style="display: flex; align-items: baseline;">
    <h1 style="font-size: larger; margin-bottom: 10px;">
      Assignments
    </h1>
    <p style="font-size: medium; margin-left: 8px; color: var(--ep-color-primary); margin-bottom: 0;">
      {{ data?.assignment.total_chapter_count }}
    </p>
  </div>
  Completed
  <progress-bar :top="data?.assignment.completed_count" :bot="data?.assignment.max_possible_completed_count" style="margin-top: 6px;"/>
    
  <div v-for="c in list" :key="c.chapter.chapter.chapter_id" style="width: 100%; margin-top: 20px; border-top: solid 1px var(--ep-border-color); padding-top: 18px; overflow: hidden;">

    <el-button 
      style="float: right;" @click="
      table_visibility=true; table_data=c.detail.completed_status_list; new_announcement=`Please do the homework of chapter ${c.chapter.chapter.chapter_title}`"
    >View details</el-button>
    <el-button
      style="float: right; margin-right: 20px;"
      @click="router.push('/course' + '/' + course_data?.course_id + '/' + c.chapter.chapter.chapter_title.replace(/ /g, '-'))">
      <span style="margin-left: -12px; margin-right: 5px;" i="ep-arrow-left"/>Back to chapter
    </el-button>
    <div style="display: flex; gap: 5px; flex-direction: row; align-items: baseline;">
      <div style="font-weight: bold; margin-top: 5px;">
        {{ c.chapter.chapter.chapter_title }}
      </div>
    </div>
    <div style="margin-top: 8px; display: flex; gap: 5px;">
      <div i="ep-histogram" style="margin-right: 10px;"/>
      Average score: <div style="color: var(--ep-color-primary); width: 40px;">{{ c.detail.average_score }}</div>
      Uncompleted count: <div style="color: var(--ep-color-primary); width: 40px;">{{ c.detail.uncompleted_count }}</div>
      Ungraded count: <div style="color: var(--ep-color-primary); width: 40px;">{{ c.detail.ungraded_count }}</div>
    </div>
    <div style="display: flex; gap: 5px; flex-direction: row; align-items: center; margin-top: 12px;">
      <div style="width: 100%;">
        Completed
        <progress-bar :top="c.chapter.completed_count" :bot="c.chapter.max_completed_count" style="margin-top: 6px;"/>
      </div>
      <div style="width: 20px;"/>
      <div style="width: 100%;">
        Score
        <progress-bar :top="c.chapter.average_score" :bot="c.chapter.max_score" style="margin-top: 6px;"/>
      </div>
    </div>
    <div style="margin-top: 15px; font-size: small; color: var(--ep-text-color-placeholder);">
      {{ (new Date(c.chapter.chapter.updated_at)).toLocaleString() }}
    </div>
    <!-- <el-button 
      style="float: right; margin-right: 20px;"
      @click="router.push('/assignment/submission/' + c.submission.file_submission_id)">
      View this submission <span style="margin-left: 5px; margin-right: -12px;" i="ep-arrow-right"/>
    </el-button> -->
  </div>
  <div style="width: 100%; margin-top: 20px; border-top: solid 1px var(--ep-border-color); padding-top: 18px; overflow: hidden;"></div>

</div>

<el-dialog 
  v-model="table_visibility"
  title="Completed status" 
  width="600">

  <el-table :data="table_data" height="400">
    <el-table-column label="Name">
      <template #default="scope: {row: ScoreCompletedStatus}">
        {{ scope.row.student.first_name+' '+scope.row.student.last_name }}
      </template>
    </el-table-column>
    <el-table-column label="Graded">
      <template #default="scope: {row: ScoreCompletedStatus}">
        <span :style="`color: var(--ep-color-${scope.row.is_graded ? 'success': 'danger'})`">
          {{ scope.row.is_graded }}
        </span>
      </template>
    </el-table-column>
    <el-table-column label="Completed">
      <template #default="scope: {row: ScoreCompletedStatus}">
        <span :style="`color: var(--ep-color-${scope.row.is_completed ? 'success': 'danger'})`">
          {{ scope.row.is_completed }}
        </span>
      </template>
    </el-table-column>
  </el-table>

  <el-input
    v-model="new_announcement"
    :autosize="{ minRows: 3 }"
    type="textarea"
    placeholder="Add an announcement ..."
    style="margin-top: 10px;"
  />
  <template #footer>
    <el-button 
    @click="send_announcement(
      table_data
        .filter((v)=>!v.is_completed)
        .map((v)=>v.student.user_id)
    )">Send announcement to uncompleted students</el-button>
  </template>
</el-dialog>
</template>
<script setup lang="ts">
import { get_all_students_score_call, get_all_students_warning_call, teacher_check_chapter_study_situation_call, teacher_check_course_study_situation_call, WarningType, type CourseStudySituation, type ScoreChapter, type ScoreCompletedStatus, type SingleChapterInfo, type StudentsScoreTable, type WarningInfo } from '@/api/course/analysis/StudyAnalysisAPI';
import { createAnnouncementCall } from '@/api/course/AnnouncementAPI';
import { getCourseCall, type CourseEntity } from '@/api/course/CourseAPI';
import { getUserPublicInfoCall, type UserPublicInfoEntity } from '@/api/user/UserAPI';
import { ElMessage, type TableColumnCtx } from 'element-plus';
import { ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter()
const route = useRoute()
let course_id = 0
const data = ref<CourseStudySituation>()
const course_data = ref<CourseEntity>()
const list = ref<{ chapter: SingleChapterInfo, detail: ScoreChapter }[]>([])

const table_visibility = ref(false)
const table_data = ref<ScoreCompletedStatus[]>([])

async function load() {
  const msg = await teacher_check_course_study_situation_call(course_id)
  if(msg.code!==200) {
    ElMessage({
      message: 'get overall score network error',
      type: 'error',
    })
    return
  }
  data.value = msg.data
  const msg_ = await getCourseCall(course_id)
  if(msg_.code!==200) {
    ElMessage({
      message: 'get course network error',
      type: 'error',
    })
    return
  }
  course_data.value = msg_.data

  list.value = []
  for(const c of data.value.assignment.changing_rate) {
    list.value.push({
      chapter: c,
      detail: (await teacher_check_chapter_study_situation_call(course_id, c.chapter.chapter_id)).data.data as ScoreChapter
    })
  }
}

const watch_id = watch(() => route.params.course_id,
  async (new_id) => {
    course_id = Number(new_id)
    await load()
  },
  {immediate: true}
)

const new_announcement = ref('')
async function send_announcement(student_list: number[]) {
  if(new_announcement.value==='')
    return
  const msg = await createAnnouncementCall(course_id, {
    notification_id: 0, course_id: course_id, 
    receiver_ids: student_list,
    message: new_announcement.value, 
    created_at: new Date(), updated_at: new Date(),
  })
  if(msg.code!=200) {
    ElMessage({
      message: 'send announcement network error',
      type: 'error',
    })
    return
  }
  else {
    ElMessage({
      message: 'success',
      type: 'success',
    })
  }
}

</script>
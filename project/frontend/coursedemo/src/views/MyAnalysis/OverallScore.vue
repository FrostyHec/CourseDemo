<template>
<div style="margin: 40px;">
  <el-button
    style="float: right; margin-right: 20px;"
    @click="router.push('/course' + '/' + course_data?.course_id)">
    <span style="margin-left: -12px; margin-right: 5px;" i="ep-arrow-left"/>Back to course
  </el-button>
  <div style="margin-bottom: 20px; font-size: x-large; font-weight: bold;">
    {{ course_data?.course_name }} - Scores
  </div>

  <base-chart style="width: 100%; height: 400px;" :labels="labels" :values="scores" />

  <div style="display: flex; align-items: baseline;">
    <h1 style="font-size: larger; margin-bottom: 0px;">
      Chapters
    </h1>
    <!-- <p style="font-size: medium; margin-left: 8px; color: var(--ep-color-primary); margin-bottom: 0;">
      {{ data? }}
    </p> -->
  </div>
  <!-- <progress-bar :top="data?.assignment.completed_count" :bot="data?.assignment.max_possible_completed_count" style="margin-top: 6px;"/> -->
    
  <div v-for="c in data" :key="c.chapter_id" style="width: 100%; margin-top: 20px; border-top: solid 1px var(--ep-border-color); padding-top: 18px; overflow: hidden;">

    <!-- <el-button style="float: right;" @click="table_visibility=true; table_data=c.detail.completed_status_list">View details</el-button> -->
    <el-button
      style="float: right; margin-right: 20px;"
      @click="router.push('/course' + '/' + course_data?.course_id + '/' + c.chapter_name.replace(/ /g, '-'))">
      <span style="margin-left: -12px; margin-right: 5px;" i="ep-arrow-left"/>Back to chapter
    </el-button>
    <div style="display: flex; gap: 5px; flex-direction: row; align-items: baseline;">
      <div style="font-weight: bold; margin-top: 5px;">
        {{ c.chapter_name }}
      </div>
    </div>
    <div style="margin-top: 8px; display: flex; gap: 5px;">
      <div i="ep-histogram" style="margin-right: 10px;"/>
      Total score: <div style="color: var(--ep-color-primary); width: 40px;">
        {{ c.assignments.map((v) => v.received_scores).reduce((a,b)=>a+b, 0) }}
      </div>
      Chapter type: <div style="color: var(--ep-color-primary); width: 100px;">{{ c.chapter_type }}</div>
      All graded: <div style="color: var(--ep-color-primary); width: 40px;">{{ c.is_all_graded }}</div>
    </div>
    <el-table 
      :data="c.assignments" 
      style="width: 100%; height: 100%; margin-top: 15px;"
    >
      <el-table-column label="Assignment" fixed>
        <template #default="scope: {row: AssignmentSituation}">
          {{ scope.row.assignment.assignment_name }}
        </template>
      </el-table-column>
      <el-table-column label="Graded">
        <template #default="scope: {row: AssignmentSituation}">
          <span :style="`color: var(--ep-color-${scope.row.is_graded ? 'success': 'danger'})`">
            {{ scope.row.is_graded }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="Submitted">
        <template #default="scope: {row: AssignmentSituation}">
          <span :style="`color: var(--ep-color-${scope.row.has_submission ? 'success': 'danger'})`">
            {{ scope.row.has_submission }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="Score" prop="received_scores"/>
    </el-table>
  </div>
  <div style="width: 100%; margin-top: 20px; border-top: solid 1px var(--ep-border-color); padding-top: 18px; overflow: hidden;"></div>
</div>
</template>
<script setup lang="ts">
import { get_all_students_score_call, get_my_score_call, type AssignmentChapterSituation, type AssignmentChapterSituationList, type AssignmentSituation, type StudentsScoreTable } from '@/api/course/analysis/StudyAnalysisAPI';
import { getCourseCall, type CourseEntity } from '@/api/course/CourseAPI';
import { ElMessage, type TableColumnCtx } from 'element-plus';
import { ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter()
const route = useRoute()
let course_id = 0
let data = ref<AssignmentChapterSituation[]>([])
const course_data = ref<CourseEntity>()
const scores = ref<number[]>([])
const labels = ref<string[]>([])

async function load() {
  const msg = await get_my_score_call(course_id)
  if(msg.code!==200) {
    ElMessage({
      message: 'get overall score network error',
      type: 'error',
    })
    return
  }
  data.value = msg.data.content
  const msg_ = await getCourseCall(course_id)
  if(msg_.code!==200) {
    ElMessage({
      message: 'get course network error',
      type: 'error',
    })
    return
  }
  course_data.value = msg_.data

  scores.value = []
  labels.value = []
  for(const c of data.value) {
    scores.value = scores.value.concat(c.assignments.map((v) => v.received_scores))
    labels.value = labels.value.concat(c.assignments.map((v) => c.chapter_name + '/' + v.assignment.assignment_name))
  }
  console.log(scores.value, labels.value)
}

const watch_id = watch(() => route.params.course_id,
  async (new_id) => {
    course_id = Number(new_id)
    await load()
  },
  {immediate: true}
)


</script>
<template>
  <div style="margin: 40px;">
    <el-button
      style="float: right; margin-right: 20px;"
      @click="router.push('/course' + '/' + course_data?.course_id)">
      <span style="margin-left: -12px; margin-right: 5px;" i="ep-arrow-left"/>Back to course
    </el-button>
    <div style="margin-bottom: 20px; font-size: x-large; font-weight: bold;">
      {{ course_data?.course_name }}
    </div>
    <div style="display: flex; align-items: baseline;">
      <h1 style="font-size: larger; margin-bottom: 10px;">
        Teaching
      </h1>
      <p style="font-size: medium; margin-left: 8px; color: var(--ep-color-primary); margin-bottom: 0;"> 
        {{ data?.teaching.total_chapter_count }}
      </p>
    </div>
    Completed
    <progress-bar :top="data?.teaching.completed_count" :bot="data?.teaching.max_possible_completed_count" style="margin-bottom: 30px; margin-top: 6px;"/>

    <div style="display: flex; align-items: baseline;">
      <h1 style="font-size: larger; margin-bottom: 10px;">
        Assignments
      </h1>
      <p style="font-size: medium; margin-left: 8px; color: var(--ep-color-primary); margin-bottom: 0;">
        {{ data?.assignment.total_chapter_count }}
      </p>
    </div>
    <el-button style="float: right; margin-top: -12px;">
      View details<span style="margin-right: -12px; margin-left: 5px;" i="ep-arrow-right"/>
    </el-button>
    Completed
    <progress-bar :top="data?.assignment.completed_count" :bot="data?.assignment.max_possible_completed_count" style="margin-top: 6px;"/>
    <base-chart 
      :labels="data?.assignment.changing_rate.map((value) => value.chapter.chapter_title)"
      :values="data?.assignment.changing_rate.map((value) => value.completed_count/value.max_completed_count*100)"
      title="Completed rate"
      style="width: 100%; height: 400px;"
    />

    <div style="display: flex; align-items: baseline;">
      <h1 style="font-size: larger; margin-bottom: 10px;">
        Projects
      </h1>
      <p style="font-size: medium; margin-left: 8px; color: var(--ep-color-primary); margin-bottom: 0;">
        {{ data?.project.total_chapter_count }}
      </p>
    </div>
    <el-button style="float: right; margin-top: -12px;">
      View details<span style="margin-right: -12px; margin-left: 5px;" i="ep-arrow-right"/>
    </el-button>
    Completed
    <progress-bar :top="data?.project.completed_count" :bot="data?.project.max_possible_completed_count" style="margin-top: 6px;"/>
    <base-chart 
      :labels="data?.project.changing_rate.map((value) => value.chapter.chapter_title)"
      :values="data?.project.changing_rate.map((value) => value.completed_count/value.max_completed_count*100)"
      title="Completed rate"
      style="width: 100%; height: 400px;"
    />

    <div style="display: flex; align-items: baseline;">
      <h1 style="font-size: larger; margin-bottom: 10px;">
        Difficult
      </h1>
      <p style="font-size: medium; margin-left: 8px; color: var(--ep-color-primary); margin-bottom: 0;">
        {{ data?.difficult_chapters.length }}
      </p>
    </div>
    <el-table 
      :data="data?.difficult_chapters" 
      style="width: 100%; height: 100%;"
    >
      <el-table-column label="Chapter" fixed>
        <template #default="scope: {row: DifficultChapter}">
          {{ scope.row.chapter.chapter_title }}
        </template>
      </el-table-column>
      <el-table-column label="Type">
        <template #default="scope: {row: DifficultChapter}">
          <span style="color: var(--ep-color-danger);">{{ scope.row.warning_info.type==WarningType.homework_uncompleted ? 'Homework uncompleted' : 'Low score' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Detail">
        <template #default="scope: {row: DifficultChapter}">
          {{ scope.row.warning_info.type==WarningType.homework_uncompleted ? 'Completed rate:' : 'Average score:' }} 
          <span style="color: var(--ep-color-primary);">{{ scope.row.warning_info.description }}</span>
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>
<script setup lang="ts">
import { get_all_students_score_call, get_all_students_warning_call, teacher_check_course_study_situation_call, WarningType, type CourseStudySituation, type DifficultChapter, type StudentsScoreTable, type WarningInfo } from '@/api/course/analysis/StudyAnalysisAPI';
import { getCourseCall, type CourseEntity } from '@/api/course/CourseAPI';
import { getUserPublicInfoCall, type UserPublicInfoEntity } from '@/api/user/UserAPI';
import { ElMessage, type TableColumnCtx } from 'element-plus';
import { ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter()
const route = useRoute()
let course_id = 0
let data = ref<CourseStudySituation>()
const course_data = ref<CourseEntity>()

const search_string = ref('')

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
}

const watch_id = watch(() => route.params.course_id,
  async (new_id) => {
    course_id = Number(new_id)
    await load()
  },
  {immediate: true}
)

</script>
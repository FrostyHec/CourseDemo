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
    <div style="margin-bottom: 20px; font-size: large; font-weight: bold;">
      Warnings
    </div>
    <el-input
      v-model="search_string"
      placeholder="Search students"
      style="width: 200px; margin-bottom: 10px;"
    />
    <el-table 
      :data="data?.filter((value) => (value.warning_student.first_name + ' ' + value.warning_student.last_name).startsWith(search_string))" 
      style="width: 100%; height: 100%;"
    >
      <el-table-column label="Student" >
        <template #default="scope: {row: WarningInfo}">
          {{ scope.row.warning_student.first_name + ' ' + scope.row.warning_student.last_name }}
        </template>
      </el-table-column>
      <el-table-column label="Type" sortable prop="warning_type">
        <template #default="scope: {row: WarningInfo}">
          <span style="color: var(--ep-color-danger);">{{ scope.row.warning_type==WarningType.homework_uncompleted ? 'Homework uncompleted' : 'Low score' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Date" sortable prop="date">
        <template #default="scope: {row: WarningInfo}">
          {{ (new Date(scope.row.date)).toLocaleString() }}
        </template>
      </el-table-column>
      <el-table-column label="Assignment">
        <template #default="scope: {row: WarningInfo}">
          <el-link @click="router.push(`/assignment/${scope.row.description.assignment_entity.assignment_id}`)">
            {{ scope.row.description.assignment_entity.assignment_name }}<span i="ep-arrow-right"/>
          </el-link>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script setup lang="ts">
import { get_all_students_score_call, get_all_students_warning_call, get_my_warning_call, WarningType, type StudentsScoreTable, type WarningInfo } from '@/api/course/analysis/StudyAnalysisAPI';
import type { AssignmentEntity } from '@/api/course/AssignmentAPI';
import { getCourseCall, type CourseEntity } from '@/api/course/CourseAPI';
import { getUserPublicInfoCall, type UserPublicInfoEntity } from '@/api/user/UserAPI';
import { ElMessage, type TableColumnCtx } from 'element-plus';
import { ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter()
const route = useRoute()
let course_id = 0
let data = ref<WarningInfo[]>()
const course_data = ref<CourseEntity>()

const search_string = ref('')

async function load() {
  const msg = await get_my_warning_call(course_id)
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
}

const watch_id = watch(() => route.params.course_id,
  async (new_id) => {
    course_id = Number(new_id)
    await load()
  },
  {immediate: true}
)

</script>
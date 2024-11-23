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
    <el-table 
      :data="data?.filter((value) => value.name.startsWith(search_string))" 
      style="width: 100%; height: 100%;"
    >
      <el-table-column label="Type" sortable prop="warning_type">
        <template #default="scope: {row: WarningInfoWithName}">
          <span style="color: var(--ep-color-danger);">{{ scope.row.warning_type==WarningType.homework_uncompleted ? 'Homework uncompleted' : 'Low score' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Date" sortable prop="date">
        <template #default="scope: {row: WarningInfoWithName}">
          {{ (new Date(scope.row.date)).toLocaleString() }}
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script setup lang="ts">
import { get_all_students_score_call, get_all_students_warning_call, get_my_warning_call, WarningType, type StudentsScoreTable, type WarningInfo } from '@/api/course/analysis/StudyAnalysisAPI';
import { getCourseCall, type CourseEntity } from '@/api/course/CourseAPI';
import { getUserPublicInfoCall, type UserPublicInfoEntity } from '@/api/user/UserAPI';
import { ElMessage, type TableColumnCtx } from 'element-plus';
import { ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter()
const route = useRoute()
let course_id = 0
interface WarningInfoWithName extends WarningInfo {
  name: string
}
let data = ref<WarningInfoWithName[]>()
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
  data.value = []
  for(const w of msg.data.content) {
    const s = (await getUserPublicInfoCall(w.warning_student)).data
    data.value.push({
      ...w,
      name: `${s.first_name} ${s.last_name}`
    })
  }
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
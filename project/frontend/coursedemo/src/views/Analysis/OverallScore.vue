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
      Scores
    </div>
    <el-table 
      :data="table" 
      style="width: 100%; height: 100%;" 
      :summary-method="getSummaries"
      show-summary
      border
    >
      <el-table-column label="Students" prop="name" fixed/>
      <el-table-column 
        v-for="(c, j) in data?.column" :key="c.chapter_id" 
        :label="c.assignment_name" :prop="`${j}`" sortable
      />
      <el-table-column label="Total score" sortable prop="total" fixed="right"/>
    </el-table>
  </div>
</template>
<script setup lang="ts">
import { get_all_students_score_call, type StudentsScoreTable } from '@/api/course/analysis/StudyAnalysisAPI';
import { getCourseCall, type CourseEntity } from '@/api/course/CourseAPI';
import { ElMessage, type TableColumnCtx } from 'element-plus';
import { ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter()
const route = useRoute()
let course_id = 0
let data: undefined|StudentsScoreTable = undefined
const table = ref<Object[]>([])
const course_data = ref<CourseEntity>()

async function load() {
  const msg = await get_all_students_score_call(course_id)
  if(msg.code!==200) {
    ElMessage({
      message: 'get overall score network error',
      type: 'error',
    })
    return
  }
  data = msg.data
  table.value = []
  for(let i=0; i<data.row.length; i++) {
    let o = {
      name: `${data.row[i].first_name} ${data.row[i].last_name}`,
      total: data.total_score[i]
    } as any
    for(let j=0; j<data.column.length; j++)
      o[`${j}`] = data.data[i][j]
    table.value.push(o)
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

const getSummaries = () => {
  if(data)
    return ['Avg. score']
      .concat(data.average_score.map(String))
      .concat(String(data.average_total_score))
  else
    return []
}

</script>
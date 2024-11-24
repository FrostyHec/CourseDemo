<template>
<div style="margin: 40px;">
  <el-button
    style="float: right; margin-right: 20px;"
    @click="router.push('/course' + '/' + course_data?.course_id)">
    <span style="margin-left: -12px; margin-right: 5px;" i="ep-arrow-left"/>Back to course
  </el-button>
  <div style="margin-bottom: 20px; font-size: x-large; font-weight: bold;">
    {{ course_data?.course_name }} - Progresses
  </div>

  Completed rate
  <ProgressBar 
    :top="data?.chapter_progress.reduce<number>((n, v) => v.is_completed ? n+1 : n, 0)"
    :bot="data?.chapter_progress.length"
    style="margin-top: 5px; margin-bottom: 10px;"
  />

  <div style="display: flex; align-items: baseline;">
    <h1 style="font-size: larger; margin-bottom: 0px;">
      Chapters
    </h1>
    <!-- <p style="font-size: medium; margin-left: 8px; color: var(--ep-color-primary); margin-bottom: 0;">
      {{ data? }}
    </p> -->
  </div>
  <!-- <progress-bar :top="data?.assignment.completed_count" :bot="data?.assignment.max_possible_completed_count" style="margin-top: 6px;"/> -->
    
  <div v-for="c in data?.chapter_progress" :key="c.chapter_id" style="width: 100%; margin-top: 20px; border-top: solid 1px var(--ep-border-color); padding-top: 18px; overflow: hidden;">

    <!-- <el-button style="float: right;" @click="table_visibility=true; table_data=c.detail.completed_status_list">View details</el-button> -->
    <el-button
      style="float: right; margin-right: 20px;"
      @click="router.push('/course' + '/' + course_data?.course_id + '/' + chapter_map.get(c.chapter_id)?.chapter_title.replace(/ /g, '-'))">
      <span style="margin-left: -12px; margin-right: 5px;" i="ep-arrow-left"/>Back to chapter
    </el-button>
    <div style="font-weight: bold; margin-top: 5px; margin-bottom: 8px;">
      {{ chapter_map.get(c.chapter_id)?.chapter_title }}
    </div>
    Completed rate
    <ProgressBar 
      :top="c.video_resources.reduce<number>((n, v) => v.is_completed&&top_set.has(v.resource_id) ? n+1 : n, 0)"
      :bot="c.video_resources.reduce<number>((n, v) => top_set.has(v.resource_id) ? n+1 : n, 0)"
      style="margin-top: 5px;"
    />
  </div>
  <div style="width: 100%; margin-top: 20px; border-top: solid 1px var(--ep-border-color); padding-top: 18px; overflow: hidden;"></div>
</div>
</template>
<script setup lang="ts">
import { get_all_students_score_call, get_my_score_call, type AssignmentChapterSituation, type AssignmentChapterSituationList, type AssignmentSituation, type StudentsScoreTable } from '@/api/course/analysis/StudyAnalysisAPI';
import type { ChapterEntity } from '@/api/course/ChapterAPI';
import { getCourseCall, type CourseEntity } from '@/api/course/CourseAPI';
import { checkCourseProgressCall, type CourseProgress } from '@/api/course/CourseProgressAPI';
import { get_all, type AllInOneEntity } from '@/stores/course';
import { ElMessage, type TableColumnCtx } from 'element-plus';
import { ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter()
const route = useRoute()
let course_id = 0
const data = ref<CourseProgress>()

const top_set = new Set<number>
const chapter_map = new Map<number, ChapterEntity>

const course_data = ref<CourseEntity>()

async function load() {
  const all = await get_all(course_id)
  if(all===undefined)
    return
  top_set.clear()
  for(const c of all.chapters) {
    chapter_map.set(c.chapter_info.chapter_id, c.chapter_info)
    for(const r of c.resources)
      top_set.add(r.top.resource_id)
  }
  const msg = await checkCourseProgressCall(course_id)
  if(msg.code!==200) {
    ElMessage({
      message: 'get course progress network error',
      type: 'error',
    })
    return
  }
  data.value = msg.data

  course_data.value = all.course_info
}

const watch_id = watch(() => route.params.course_id,
  async (new_id) => {
    course_id = Number(new_id)
    await load()
  },
  {immediate: true}
)


</script>
<template>
  <el-scrollbar v-if="course_store.current_data">
    <div style="margin: 32px;">
      <div style="font-family: monospace; float: right; color: var(--ep-text-color-placeholder); margin: 0%; font-size: small;"> 
        {{ '&nbsp;created at: '+moment(course_store.current_data?.data.created_at).format('YYYY-MM-DD HH:mm:ss')}}
        <br/>
        {{ 'last update: '+moment(course_store.current_data?.data.updated_at).format('YYYY-MM-DD HH:mm:ss')}} 
      </div>
      <h1 style="font-size: x-large; margin: 0%;">
        {{ course_store.current_data?.label }}
      </h1>
      
      <div 
      v-if="course_store.current_data?.complete && 
      auth_store.user.user_id!==course_store.current_course_teacher() &&
      !('chapter_type' in course_store.current_data?.data && course_store.current_data?.data.chapter_type===ChapterType.teaching)
      " style="color: var(--ep-color-success)">
        Completed
      </div>

      <div v-if="'course_name' in course_store.current_data.data" style="margin-top: 10px;">
        <div v-if="course_store.current_course_teacher()==auth_store.user.user_id">
          <el-button @click="router.push(`/analysis/scores/${course_store.current_course_id()}`)">
            All scores
          </el-button>
          <el-button type="primary" @click="router.push(`/analysis/${course_store.current_course_id()}`)">
            Course analysis
          </el-button>
          <el-button type="warning" @click="router.push(`/analysis/warnings/${course_store.current_course_id()}`)">
            Warnings
          </el-button>
        </div>
        <div v-else>
          <el-button @click="router.push(`/my-analysis/scores/${course_store.current_course_id()}`)">
            My scores
          </el-button>
          <el-button type="primary" @click="router.push(`/my-analysis/progress/${course_store.current_course_id()}`)">
            My progress
          </el-button>
          <el-button type="warning" @click="router.push(`/my-analysis/warnings/${course_store.current_course_id()}`)">
            My warnings
          </el-button>
        </div>
      </div>

      <el-button v-if="'course_name' in course_store.current_data.data" type="primary" @click="router.push(`/course/liveStream/${course_store.current_course_id()}`)" style="margin-top: 10px;">
        Live stream
      </el-button>

      <div v-if="course_store.current_data?.description != ''">
        <h1 style="font-size: large;">
          Description
        </h1>
        {{ course_store.current_data?.description }}
      </div>
      <div v-if="'course_name' in course_store.current_data.data" style="display: flex; justify-content: center; align-items: center;">
        <div style="font-size: large; color: var(--ep-color-primary); margin-right: 10px;">~</div>
        <base-like
          :like="async () => {
            const id = course_store.current_course_id()
            if(id!==undefined)
              return await createCourseLikeCall(id)
            return undefined
          }"
          :cancel="async () => {
            const id = course_store.current_course_id()
            if(id!==undefined)
              return await cancelCourseLikeCall(id)
            return undefined
          }"
          :get="async () => {
            const id = course_store.current_course_id()
            if(id!==undefined)
              return await getCourseLikeCall(id)
            return undefined
          }"
        />
        <div style="font-size: large; color: var(--ep-color-primary); margin-left: 10px;">Give your like ~</div>
      </div>
      <base-resource/>
      <base-announcement/>
      <base-comment/>
      <base-assignment/>
    </div>
  </el-scrollbar>
  <div v-else style="font-size: large;">404</div>
</template>
<script setup lang="ts">
import { ChapterType } from '@/api/course/ChapterAPI';
import { cancelCourseLikeCall, createCourseLikeCall, getCourseLikeCall } from '@/api/course/CourseLikeAPI';
import { useAuthStore } from '@/stores/auth';
import { useCourseStore } from '@/stores/course';
import moment from 'moment';
import { useRouter } from 'vue-router';
const router = useRouter()
const course_store = useCourseStore()
const auth_store = useAuthStore()
</script>
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
      <div v-if="course_store.current_data?.description != ''">
        <h1 style="font-size: large;">
          Description
        </h1>
        {{ course_store.current_data?.description }}
      </div>
      <base-resource/>
      <base-announcement/>
      <base-comment/>
    </div>
    <div style="display: flex; justify-content: center; align-items: center;">
      <base-like
        v-if="'course_name' in course_store.current_data.data"
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
    </div>
  </el-scrollbar>
  <div v-else style="font-size: large;">404</div>
</template>
<script setup lang="ts">
import { cancelCourseLikeCall, createCourseLikeCall, getCourseLikeCall } from '@/api/course/CourseLikeAPI';
import { useCourseStore } from '@/stores/course';
import moment from 'moment';
const course_store = useCourseStore()
</script>
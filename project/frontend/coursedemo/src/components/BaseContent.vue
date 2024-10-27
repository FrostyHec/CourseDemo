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
      <base-comment/>
    </div>
  </el-scrollbar>
  <div v-else style="font-size: large;">404</div>
</template>
<script setup lang="ts">
import { useCourseStore } from '@/stores/course';
import moment from 'moment';
const course_store = useCourseStore()
</script>
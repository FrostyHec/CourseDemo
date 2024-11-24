<template>
  <div v-if="is_resource" style="margin-top: 20px;">
    <file-display
      :suffix="current_resource?.suffix"
      :file_name="current_resource?.file_name"
      :access_key="current_resource?.access_key"
      :is_attachment="current_resource?.resource_type===ResourceType.attachment"
      :resource_id="current_resource?.resource_id"
    />
  </div>
</template>
<script setup lang="ts">
import { ref, watch } from 'vue'
import { useCourseStore } from '@/stores/course';
import { type CourseEntity } from '@/api/course/CourseAPI'
import { type ChapterEntity } from '@/api/course/ChapterAPI'
import type { ResourceEntityPlus } from '@/stores/course';
import { ResourceType } from '@/api/course/CourseResourceAPI';
import type FileDisplay from './FileDisplay.vue';

const course_store = useCourseStore()
const is_resource = ref(false)
const current_resource = ref<undefined|ResourceEntityPlus>(undefined)

const watch_current_data = watch(() => course_store.current_data?.data,
  async (new_data: CourseEntity|ChapterEntity|ResourceEntityPlus|undefined) => {
    is_resource.value = !!new_data && 'resource_name' in new_data
    if(!new_data || !('resource_name' in new_data))
      return
    if(new_data.resource_id===current_resource.value?.resource_id)
      return
    current_resource.value = new_data
  },
  { immediate: true }
)

</script>
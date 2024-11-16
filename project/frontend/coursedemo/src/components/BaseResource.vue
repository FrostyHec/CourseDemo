<template>
  <div v-if="is_resource">
    <base-markdown v-if="resource_type==='md'" :markdown-content="md_file"/>
    <embed v-if="resource_type==='pdf'" :src="link" type="application/pdf" style="margin-top: 30px; height: auto; width: 100%; height: 1000px;"/>
    <video v-if="resource_type==='video'" controls><source :src=link :type="current_resource?.suffix"></video>
  </div>
</template>
<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import { useCourseStore } from '@/stores/course';
import { type CourseEntity } from '@/api/course/CourseAPI'
import { type ChapterEntity } from '@/api/course/ChapterAPI'
import type { ResourceEntityPlus } from '@/stores/course';
import { addCommentToResourceCall, addReplyToCommentCall, getResourceCommentsCall, type ResourceCommentEntity } from '@/api/course/ResourceCommentAPI';
import { ElMessage } from 'element-plus';
import { useAuthStore } from '@/stores/auth';
import { getResourceAccessLink } from '@/api/resource_access/ResourceAccessAPI';
import { AxiosAPI } from '@/utils/APIUtils';
import axios from 'axios'

const course_store = useCourseStore()
const auth_store = useAuthStore()
const is_resource = ref(false)
const resource_type = ref<'md'|'video'|'pdf'|undefined>(undefined)
const current_resource = ref<undefined|ResourceEntityPlus>(undefined)
const md_file = ref('')
const link = ref('')

async function load_resource() {
  // //!!!!!!!!!!!!!!!!!!!!
  // if(course_store)
  //   return
  // //!!!!!!!!!!!!!!!!!!!!
  if(!current_resource.value) {
    resource_type.value = undefined
    return
  }
  if(current_resource.value.resource_name.split('.').pop()==='md')
    resource_type.value = 'md'
  if(current_resource.value.suffix.split('/')[0]==='video')
    resource_type.value = 'video'
  if(current_resource.value.suffix.split('/').pop()==='pdf')
    resource_type.value = 'pdf'
  link.value = getResourceAccessLink(current_resource.value.file_name, auth_store.user.user_id, current_resource.value.access_key)
  if(resource_type.value!=='md')
    return
  md_file.value = await axios({method: 'get', url: link.value});
}
const watch_current_data = watch(() => course_store.current_data?.data,
  async (new_data: CourseEntity|ChapterEntity|ResourceEntityPlus|undefined) => {
    is_resource.value = !!new_data && 'resource_name' in new_data
    if(!new_data || !('resource_name' in new_data))
      return
    if(new_data.resource_id===current_resource.value?.resource_id)
      return
    current_resource.value = new_data
    await load_resource()
  },
  { immediate: true }
)
</script>
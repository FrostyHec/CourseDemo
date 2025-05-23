<template>
  <div v-if="resource_type==='attachment' || downloadable" style="margin-bottom: 10px;">
    <p>{{ 'File: '+download_name }}</p>
    <a :href="link" :download="download_name" class="ep-button ep-button--primary" >Download</a>
  </div>
  <base-markdown v-if="resource_type==='md'" :markdown-content="md_file"/>
  <base-pdf
    v-if="resource_type==='pdf'"
    :link="link"
    :real_link="pdf_real_link"
    :resource_id="resource_id"
  />
  <base-video
    v-if="resource_type==='video'"
    :link="link"
    :file_type="file_type"
    :resource_id="resource_id"
  />
</template>
<script setup lang="ts">
import { reactive, ref, watch, toRef } from 'vue'
import { useAuthStore } from '@/stores/auth';
import { getResourceAccessLink } from '@/api/resource_access/ResourceAccessAPI';
import { AxiosAPI } from '@/utils/APIUtils';
import axios from 'axios'

const auth_store = useAuthStore()
const resource_type = ref<'md'|'video'|'pdf'|'attachment'|undefined>(undefined)
const md_file = ref('')
const link = ref('')
const pdf_real_link = ref('')
const file_type = ref('')
const download_name = ref('')

async function load_resource(suffix: string, file_name: string, access_key: string, is_attachment: boolean) {
  console.log('load_resource')
  if(suffix===undefined)
    return
  const type_and_name = suffix.split(':')
  if(type_and_name.length<2)
    return
  const type = type_and_name[0]
  const name = type_and_name[1]
  console.log(name, suffix)
  download_name.value = name
  link.value = ''
  resource_type.value = undefined
  if(is_attachment)
    resource_type.value = 'attachment'
  else if(name.split('.').pop()==='md')
    resource_type.value = 'md'
  else if(type.split('/')[0]==='video') {
    link.value = getResourceAccessLink(file_name, auth_store.user.user_id, access_key)
    file_type.value = type
    resource_type.value = 'video'
    return
  }
  else if(type.split('/').pop()==='pdf')
    resource_type.value = 'pdf'
  else
    resource_type.value = 'attachment'
  const l = getResourceAccessLink(file_name, auth_store.user.user_id, access_key)
  if(resource_type.value=='md') {
    md_file.value = (await axios({method: 'get', url: l})).data
    return
  }
  if(resource_type.value=='pdf') {
    const bin = (await axios({method: 'get', url: l, responseType: 'blob'})).data
    link.value = URL.createObjectURL(new Blob([bin], {type: 'application/pdf; chartset=UTF-8'}))
    pdf_real_link.value = l
    return
  }
  link.value = l
  file_type.value = type
}

const p = defineProps({
  suffix: {type: String, default: ''}, 
  file_name: {type: String, default: ''}, 
  access_key: {type: String, default: ''}, 
  is_attachment: {type: Boolean, default: false},
  resource_id: {type: Number, default: undefined},
  downloadable: {type: Boolean, default: true},
})
const props = toRef(p)

const watch_props = watch(() => props, async (new_data: typeof props) => {
  load_resource(new_data.value.suffix, new_data.value.file_name, new_data.value.access_key, new_data.value.is_attachment)
}, {immediate: true, deep: true})

</script>
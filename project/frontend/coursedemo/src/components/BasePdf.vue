<template>
  <iframe
    ref="pdf_ref"
    style="width: 100%; height: 1000px;"
    :src="`/pdfjs-4.8.69-legacy-dist/web/viewer.html?file=${link}#toolbar=0`"
    frameborder="0"
  />
</template>
<script setup lang="ts">
import { completeResourceCall } from '@/api/course/CourseProgressAPI';
import Cookies from 'js-cookie';
import { onUnmounted, ref, toRef } from 'vue';

const pdf_ref = ref()

const p = defineProps({
  link: {type: String, default: ''},
  resource_id: {type: Number, default: undefined}
})
const props = toRef(p)

const watch_pdf = props.value.resource_id===undefined ? undefined : setInterval(async () => {
  const app = pdf_ref.value?.contentWindow?.PDFViewerApplication
  if(app===undefined)
    return
  if(props.value.resource_id && app.page===app.pagesCount)  {
    // await completeResourceCall(props.value.resource_id)
  }
}, 500)

let last_link = ''
const watch_page = setInterval(() => {
    const app = pdf_ref.value?.contentWindow?.PDFViewerApplication
    if(last_link!=props.value.link) {
      last_link = props.value.link
      const save = Cookies.get('pdf-save: '+last_link)
      if(save!==undefined && app!==undefined)
        app.page = Number(save)
    }
    if(app!==undefined)
      Cookies.set('pdf-save: '+last_link, String(app.page))
  }, 1000
)

onUnmounted(() => {
  clearInterval(watch_page)
  if(watch_pdf!==undefined)
    clearInterval(watch_pdf)
})

</script>
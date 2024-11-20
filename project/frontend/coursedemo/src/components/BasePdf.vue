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
  if(props.value.resource_id && app?.page===app?.pagesCount)
    await completeResourceCall(props.value.resource_id)
}, 500)

onUnmounted(() => {
  if(watch_pdf!==undefined)
    clearInterval(watch_pdf)
})

</script>
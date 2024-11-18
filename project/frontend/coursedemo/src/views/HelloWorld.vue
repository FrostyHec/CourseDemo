<template>
  <file-uploader ref="uploader"/>
  <el-button @click="func">func</el-button>
  <!-- <embed :src="link" style="width:100%; height:500px;" type="application/pdf"/> -->
  <div/>
  <!-- <iframe
    ref="pdf"
    style="width: 100%; height: 1000pX;"
    :src="`/pdfjs-4.8.69-legacy-dist/web/viewer.html?file=${link}#toolbar=0`"
    frameborder="0"
  /> -->
  <video 
    v-if="open" ref="videoPlayer" controls 
    @loadeddata="videoPlayer.currentTime = 4"
    @timeupdate="updateProgress" 
    @play="(event) => console.log('start', (event.target as any)?.currentTime)" 
    @pause="(event) => console.log('end', (event.target as any)?.currentTime)">
    <source :src="link" type="video/mp4">
    Your browser does not support the video tag.
  </video>
</template>
<script setup lang="ts">
import { ref, watch } from 'vue';

const link = ref('')
const uploader = ref()
const videoPlayer = ref()
const open = ref(false)
const pdf = ref()
function func() {
  const file = uploader.value.file_get as File
  console.log(file.type) 
  link.value = URL.createObjectURL(file)
  const video = document.createElement('video')
  // video.addEventListener("loadedmetadata", function() {
  //   console.log(video.duration);
  // });
  // video.src = link.value
  open.value = true
}

function updateProgress(event: Event) {
  const video = event.target as any;
  console.log(video.currentTime);
}

// const watch_page = setInterval(() => {
//     const app = pdf.value?.contentWindow?.PDFViewerApplication
//     console.log(app?.page, app?.pagesCount)
//   }, 1000
// )

</script>
<template>
  <file-uploader ref="uploader"/>
  <el-button @click="open1">func</el-button>
  <!-- <embed :src="link" style="width:100%; height:500px;" type="application/pdf"/> -->
  <div/>
  <!-- <iframe
    ref="pdf"
    style="width: 100%; height: 1000pX;"
    :src="`/pdfjs-4.8.69-legacy-dist/web/viewer.html?file=${link}#toolbar=0`"
    frameborder="0"
  /> -->
  <!-- <ProgressBar :top="131" :bot="512" style="margin: 100px;"/>
  <base-chart :labels="['A', 'C', 'B']" :values="[2, 4, 1]" :title="'Happy'" style="width: 100%; height: 400px;"/> -->
  <!-- <div style="margin: 100px;">
    <div style="margin-bottom: 5px; float: right;">
      <span style="color: var(--ep-color-primary);">1203</span> / 7481
    </div>
    <div style="font-weight: bold; font-size: large; margin-bottom: 5px;">
      50%
    </div>
    <div style="width: 100%; height: 25px; border-radius: 4px; background-color: var(--ep-border-color);">
      <div style="background-color: var(--ep-color-primary); width: 30%; height: 100%; border-radius: 4px;"></div>
    </div>
  </div> -->
  <div style="margin: 20px;">
    <video 
      v-if="open" ref="videoPlayer" controls 
      @loadeddata="videoPlayer.currentTime = 4"
      @timeupdate="updateProgress" 
      @play="(event) => console.log('start', (event.target as any)?.currentTime)" 
      @pause="(event) => console.log('end', (event.target as any)?.currentTime)"
      style="width: 100%; aspect-ratio: 16/9;"
    >
      <source :src="link" type="video/mp4">
      Your browser does not support the video tag.
    </video>
  </div>
</template>
<script setup lang="ts">
import { ElNotification } from 'element-plus';
import { h, ref, watch } from 'vue';

const open1 = () => {
  ElNotification({
    title: 'Title',
    message: 'happy',
    duration: 0,
  })
}

const link = ref('')
const uploader = ref()
const videoPlayer = ref()
const open = ref(false)
const pdf = ref()
function func() {
  const file = uploader.value.file_get as File
  console.log(file.type) 
  link.value = URL.createObjectURL(file)
  // const video = document.createElement('video')
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
//     if(app)
//       app.page = 23
//   }, 1000
// )

</script>
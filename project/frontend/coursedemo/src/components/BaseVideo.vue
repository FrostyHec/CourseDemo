<template>
  <video
    v-if="open"
    ref="video_ref" controls
    @loadeddata="video_ref.currentTime=last_watch_video"
    @play="set_watch_video"
    @pause="(event) => {clear_watch_video((event.target as any)?.currentTime)}"
  >
    <source :src=link :type="file_type">
  </video>
</template>
<script setup lang="ts">
import { getLastWatchedCall, keepWatchAliveCall, startWatchAliveCall, stopWatchAliveCall } from '@/api/course/CheatCheckAPI';
import { completeResourceCall } from '@/api/course/CourseProgressAPI';
import { onMounted, onUnmounted, ref, toRef } from 'vue';

const video_ref = ref()
const last_watch_video = ref(0)
const open = ref(false)

async function load() {
  if(props.value.resource_id!==undefined) {
    const msg = await getLastWatchedCall(props.value.resource_id)
    last_watch_video.value = msg.data.last_watched_seconds
    if(msg.data.remain_required_seconds<=0) {
      await completeResourceCall(props.value.resource_id)
    }
  }
  open.value = true
}
load()

const p = defineProps({
  link: {type: String, default: ''}, 
  file_type: {type: String, default: ''},
  resource_id: {type: Number, default: undefined}
})
const props = toRef(p)

let watch_video: number|undefined = undefined
async function set_watch_video() {
  if(props.value.resource_id===undefined)
    return
  if(watch_video!==undefined)
    clearInterval(watch_video)
  await startWatchAliveCall(props.value.resource_id)
  watch_video = setInterval(async () => {
      if(props.value.resource_id===undefined)
        return
      await keepWatchAliveCall(props.value.resource_id)
    }, 60*1000
  )
}
async function clear_watch_video(time: number) {
  if(props.value.resource_id===undefined)
    return
  await stopWatchAliveCall(props.value.resource_id, {
    watched_seconds: time,
    watched_until: 0,
  })
  if(watch_video!==undefined)
    clearInterval(watch_video)
}

onUnmounted(() => {
  clear_watch_video(video_ref.value.currentTime)
})

</script>
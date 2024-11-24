<template>
  <video
    v-if="open"
    ref="video_ref" controls
    @loadeddata="video_ref.currentTime=last_watch_video;"
    @play="(event) => {set_watch_video((event.target as any)?.currentTime)}"
    @pause="async (event) => {
      console.log('pause');
      await clear_watch_video((event.target as any)?.currentTime); 
      await load(resource_id);
    }"
  >
    <source :src=link :type="file_type">
  </video>
</template>
<script setup lang="ts">
import { getLastWatchedCall, keepWatchAliveCall, startWatchAliveCall, stopWatchAliveCall } from '@/api/course/CheatCheckAPI';
import { completeResourceCall } from '@/api/course/CourseProgressAPI';
import { useCourseStore } from '@/stores/course';
import { onBeforeUnmount, onMounted, onUnmounted, ref, toRef, watch } from 'vue';

const video_ref = ref()
let last_watch_video: number = 0
const open = ref(false)
const course_store = useCourseStore()
let check = true

console.log('video')

async function load(resource_id: number|undefined) {
  if(props.value.resource_id!==undefined && check) {
    const msg = await getLastWatchedCall(props.value.resource_id)
    if(msg.code!==200) {
      check = false
    } else {
      last_watch_video = msg.data.last_watched_seconds
      if(msg.data.remain_required_seconds<=0) {
        course_store.complete_resource(props.value.resource_id)
      }
    }
    // open.value = true
    // return
    //TODO:
  }
  open.value = true
}

const p = defineProps({
  link: {type: String, default: ''}, 
  file_type: {type: String, default: ''},
  resource_id: {type: Number, default: undefined}
})
const props = toRef(p)

let watch_video: number|undefined = undefined

let start_time: number = 0
async function set_watch_video(time: number) {
  if(props.value.resource_id===undefined || !check)
    return
  if(watch_video!==undefined)
    clearInterval(watch_video)
  await startWatchAliveCall(props.value.resource_id)
  start_time = time
  watch_video = setInterval(async () => {
      if(props.value.resource_id===undefined)
        return
      await keepWatchAliveCall(props.value.resource_id)
    }, 60*1000
  )
}
async function clear_watch_video(time: number) {
  if(props.value.resource_id===undefined || !check)
    return
  await stopWatchAliveCall(props.value.resource_id, {
    watched_seconds: Math.floor(time) - Math.floor(start_time),
    watched_until: Math.floor(time),
  })
  if(watch_video!==undefined)
    clearInterval(watch_video)
}


const watch_prop = watch(() => props, async (new_data, old_data) => {
  if(open.value) {
    await clear_watch_video(video_ref.value.currentTime)
    open.value = false
    await load(old_data?.value.resource_id)
  }
  await load(new_data.value.resource_id)
}, {immediate: true, deep: true})

</script>
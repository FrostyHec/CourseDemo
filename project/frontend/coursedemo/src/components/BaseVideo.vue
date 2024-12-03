<template>
  <video
    v-if="open"
    ref="video_ref" controls
    @loadeddata="video_ref.currentTime=last_watch_video;"
    @play="(event) => {set_watch_video((event.target as any)?.currentTime)}"
    @pause="async (event) => {
      console.log('pause')
      await clear_watch_video((event.target as any)?.currentTime); 
      await load(resource_id);
    }"
    style="width: 100%; aspect-ratio: 16/9;"
  >
    <source :src=link :type="file_type">
  </video>
  <div v-if="timer_open" style="margin-top: 10px;">Required watch time remains: <span style="color: var(--ep-color-primary); font-weight: bold;">{{ complete ? 'Competed' : remain_second }}</span> {{complete ? '' : 'seconds'}}</div>
</template>
<script setup lang="ts">
import { getLastWatchedCall, keepWatchAliveCall, startWatchAliveCall, stopWatchAliveCall } from '@/api/course/CheatCheckAPI';
import { completeResourceCall } from '@/api/course/CourseProgressAPI';
import { useCourseStore } from '@/stores/course';
import { useVideoStore } from '@/stores/video';
import { ElMessage } from 'element-plus';
import { onBeforeUnmount, onMounted, onUnmounted, ref, toRef, watch, type VideoHTMLAttributes } from 'vue';

const video_ref = ref()
let last_watch_video: number = 0
const open = ref(false)
const course_store = useCourseStore()
let check = true

let remain_second = ref(0)
let timer: number|undefined = undefined
let timer_open = ref(false)
let complete = ref(false)

async function load(resource_id: number|undefined) {
  if(props.value.resource_id!==undefined && check) {
    const msg = await getLastWatchedCall(props.value.resource_id)
    if(msg.code!==200) {
      check = false
    } else {
      last_watch_video = msg.data.last_watched_seconds
      console.log('remain', msg.data.remain_required_seconds)
      if(msg.data.remain_required_seconds<=0) {
        course_store.complete_resource(props.value.resource_id)
      }
      else {
        remain_second.value = msg.data.remain_required_seconds
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
let min15_timer: number|undefined = undefined
let start_time: number = 0
async function set_watch_video(time: number) {
  if(props.value.resource_id===undefined || !check)
    return
  if(watch_video!==undefined)
    clearInterval(watch_video)
  if(timer!==undefined)
    clearInterval(timer)
  if(min15_timer!==undefined)
    clearTimeout(min15_timer)
  if(remain_second.value>0) {
    timer = setInterval(() => {
      remain_second.value-=1; 
      if(remain_second.value<0) {
        if(timer!==undefined) {    
          clearInterval(timer)
          complete.value = true
          load(props.value.resource_id)
        }
      }
    }, 1000)
    timer_open.value = true
  }
  await startWatchAliveCall(props.value.resource_id)
  start_time = time
  watch_video = setInterval(async () => {
      if(props.value.resource_id===undefined)
        return
      await keepWatchAliveCall(props.value.resource_id)
    }, 60*1000
  )
  min15_timer = setTimeout(() => {
    video_ref.value.pause()
    ElMessage({
      message: 'You have watched 15mins',
      type: 'warning',
      duration: 0,
      showClose: true,
    })
  }, 15*60*1000)
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
  if(timer!==undefined) {    
    clearInterval(timer)
    timer_open.value = false
  }
  if(min15_timer!==undefined)
    clearInterval(min15_timer)
  load(props.value.resource_id)
}


const watch_prop = watch(() => props, async (new_data, old_data) => {
  if(open.value) {
    await clear_watch_video(video_ref.value.currentTime)
    open.value = false
    await load(old_data?.value.resource_id)
  }
  await load(new_data.value.resource_id)
}, {immediate: true, deep: true})

const video_store = useVideoStore()
const watch_id = watch(() => video_store.current_video, (new_id) => {
  if(new_id!==undefined && props.value.resource_id!==undefined && props.value.resource_id!==new_id) {
    video_ref.value.pause()
    ElMessage({
      message: 'Pause because of multiple watch'+props.value.resource_id,
      type: 'warning',
      duration: 0,
      showClose: true,
    })
    load(props.value.resource_id)
  }
})

</script>
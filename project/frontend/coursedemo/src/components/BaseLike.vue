<template>
  <el-button 
    :type="liked ? 'primary' : ''" 
    :icon="Star" circle size="large" style="font-size: 22px;" 
    @click="flip"
  />
</template>

<script setup lang="ts">
import type { APIResult } from '@/utils/APIUtils';
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { Star } from '@element-plus/icons-vue';

const props = defineProps({
  like: {
    type: Function,
    default: () => {},
  },
  cancel: {
    type: Function,
    default: () => {},
  },
  get: {
    type: Function,
    default: () => {},
  }
})

const liked = ref<boolean|undefined>(undefined)
const load = async () => {
  const msg: APIResult<{ is_like: boolean }>|undefined = await props.get()
  if(!msg || msg.code!==200) {
    ElMessage({
      message: 'Comment network error',
      type: 'error',
    })
    return
  }
  liked.value = msg.data.is_like
  console.log('!!!', liked.value)
}
load()

async function flip() {
  if(liked.value===undefined)
    return
  console.log('flip')
  let msg: APIResult<boolean>|undefined
  if(liked.value)
    msg = await props.cancel()
  else
    msg = await props.like()
  if(!msg || msg.code!==200) {
    ElMessage({
      message: 'Comment network error',
      type: 'error',
    })
    return
  }
  liked.value = !liked.value
}


</script>
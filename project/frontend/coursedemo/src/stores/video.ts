import { ref, computed} from 'vue'
import { defineStore } from 'pinia'


export const useVideoStore = defineStore('video', () => {
  const current_video = ref<number>()
  return { current_video }
})
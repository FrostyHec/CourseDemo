import { ref } from 'vue'
import { defineStore } from 'pinia'
import type Node from 'element-plus/es/components/tree/src/model/node'


export const useFormStore = defineStore('form', () => {
  
  const visibility = ref(false)
  const node = ref<Node|undefined>(undefined)
  const mode = ref<'Add'|'Edit'>('Add')

  function get_level() {
    if(!node.value)
      return 0
    else
      return node.value?.level + (mode.value=='Add'?0:-1)
  }

  return { visibility, node, mode, get_level }
})
<template>
  <el-scrollbar style="border-right: solid 1px var(--ep-border-color); margin-right: -1px;">
    <el-tree
      :draggable="true"
      :allow-drag="allowDrag"
      :allow-drop="allowDrop"
      :default-expand-all="true"
      :expand-on-click-node="false"
      :data="[folder]"
      :props="{
        children: 'children',
        label: 'label',
      }"
      @node-drop="handleDrop"
      @node-click="handleClick"
    >
      <template #default="{ node }">
        <div style="overflow: hidden;">
          <p 
          style="overflow: hidden; text-overflow: ellipsis; margin: 0%;"
          :style="node.level==1 ? 'font-weight: bold;' : 
                  !node.data.children ? '' : 'font-weight: bold; color: var(--ep-color-primary);'">
            {{ node.data.label }}
          </p>
        </div>
      </template>
    </el-tree>
  </el-scrollbar>
</template>

<script lang="ts" setup>
import { useRouter } from "vue-router";

interface Tree {
  label: string,
  children?: Tree[],
}

const folder: Tree = reactive(
{ label: 'Object Oriented Analysis Design', children: [
  { label: 'Chapter1', children: [
    { label: 'video' }, 
    { label: 'pdf' },
  ]},
  { label: 'Chapter2', children: [
    { label: 'video'},
    { label: 'md'},
    { label: 'test'},
  ]},
  { label: 'Chapter3', children: [
    { label: 'video', children: [] }, 
    { label: 'md'},
    { label: 'quiz'},
  ]},
]}
)


const router = useRouter()
const handleClick = (data: Tree, node: Node) => {
  let link = ''
  while(node.parent!==null) {
    link = '/' + node.data.label.replace(/ /g, '-') + link
    node = node.parent
  }
  router.push('/course' + link)
}


import type Node from 'element-plus/es/components/tree/src/model/node'
import type { DragEvents } from 'element-plus/es/components/tree/src/model/useDragNode'
import type {
  AllowDropType,
  NodeDropType,
} from 'element-plus/es/components/tree/src/tree.type'
import { reactive } from "vue";

const allowDrop = (draggingNode: Node, dropNode: Node, type: AllowDropType) => {
  return draggingNode.level==dropNode.level && (type=='prev' || type=='next')
}
const allowDrag = (draggingNode: Node) => {
  return draggingNode.level > 1
}
const handleDrop = (
  draggingNode: Node,
  dropNode: Node,
  dropType: NodeDropType,
  ev: DragEvents
) => {
  console.log(folder)
}

</script>
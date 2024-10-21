<template>
  <div>
    <el-scrollbar style="border-right: solid 2px var(--ep-border-color); margin-right: -1px;">
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
        <template #default="{ node: node } : { node: Node }">
          <div class="edit-line" style="overflow: hidden; display: flex; flex: 1; justify-content: space-between; align-items: center;">
            <p 
              :class="!node.data.hide ? '' : 'hide-line'"
              style="overflow: hidden; text-overflow: ellipsis; margin: 0%;"
              :style="node.level==1 ? 'font-weight: bold;' :
                      !node.data.children ? '' : 'font-weight: bold; color: var(--ep-color-primary);'">
                {{ node.data.label }}
            </p>
            <el-popover
              v-if="node.isCurrent"
              placement="right-start"
              :width="100"
              trigger="hover"
              transition="None"
              :hide-after="100"
              :offset="8"
            >
              <template #default>
                <div style="display: flex; gap: 5px; flex-direction: column">
                  <el-button type="" style="margin: 0;" @click="open_form(node, 'Edit')">Edit</el-button>
                  <el-button type="" style="margin: 0;" @click="node.data.hide = !node.data.hide">
                    {{node.data.hide ? 'Show' : 'Hide'}}
                  </el-button>
                  <el-button v-if="node.data.children" type="primary" style="margin: 0;" @click="open_form(node, 'Add')">Add new</el-button>
                  <el-button type="danger" style="margin: 0;" @click="node.remove()">Delete</el-button>
                </div>
              </template>
              <template #reference>
                <div i="ep-edit" style="margin-right: 2px; flex-shrink: 0; height: 20px; width: 20px;"/>
              </template>
            </el-popover>
          </div>
        </template>
      </el-tree>
    </el-scrollbar>

    <base-form></base-form>
  </div>
</template>

<script lang="ts" setup>
import { useRouter } from "vue-router";

const form_store = useFormStore()

const open_form = (node: Node, mode: 'Add'|'Edit') => {
  form_store.node = node
  form_store.mode = mode
  form_store.visibility = true
}

interface Tree {
  label: string,
  children?: Tree[],
  hide?: boolean,
}

const folder: Tree = reactive(
{ label: 'Object Oriented Analysis Design', hide: false, children: [
  { label: 'Chapter1', hide: false, children: [
    { label: 'video', hide: false }, 
    { label: 'pdf', hide: false },
  ]},
  { label: 'Chapter2', hide: false, children: [
    { label: 'video', hide: false},
    { label: 'md', hide: false},
    { label: 'test', hide: false},
  ]},
  { label: 'Chapter3', hide: false, children: [
    { label: 'video', hide: false, children: [] }, 
    { label: 'md', hide: false},
    { label: 'quiz', hide: false},
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
import { reactive, ref } from "vue";
import { useFormStore } from "@/stores/form";

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

const currentNode = ref<Node|undefined>(undefined);
const editFormVisible = ref(false)


</script>


<style scoped>
.edit-buttom {
  opacity: 0%;
  color: var(--ep-color-primary);
}
.edit-line:hover .edit-buttom {
  opacity: 100%;
}
.hide-line {
  text-decoration: line-through;
  opacity: 50%;
}
</style>
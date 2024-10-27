<template>
  <div>
    <el-scrollbar style="border-right: solid 2px var(--ep-border-color); margin-right: -1px;">
      <el-tree
        :draggable="true"
        :allow-drag="allowDrag"
        :allow-drop="allowDrop"
        :default-expand-all="true"
        :expand-on-click-node="false"
        :data="course_store.unify_course_data"
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
                      node.level==2 ? 'font-weight: bold; color: var(--ep-color-primary);': ''">
                {{ node.data.label }}
            </p>
            <el-popover
              v-if="node.data === course_store.current_data"
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
                  <!-- <el-button type="" style="margin: 0;" @click="node.data.hide = !node.data.hide">
                    {{node.data.hide ? 'Show' : 'Hide'}}
                  </el-button> -->
                  <el-button v-if="!('resource_name' in node.data.data)" type='primary' style="margin: 0;" @click="open_form(node, 'Add')">Add new</el-button>
                  <el-button v-if="node.parent.parent!==null" type="danger" style="margin: 0;" @click="handleDelete(node)">Delete</el-button>
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

    <CourseForm/>
    <ChapterForm/>
    <ResourseForm/>
  </div>
</template>

<script lang="ts" setup>
import { useRouter } from "vue-router"
import { useCourseStore } from "@/stores/course";
import { type UnifyTree } from "@/stores/course";
import { ElMessage } from 'element-plus'

const form_store = useFormStore()
const course_store = useCourseStore()
//course_store.init()

const open_form = (node: Node, mode: 'Add'|'Edit') => {
  let data = (node.data as UnifyTree).data
  if(mode=='Edit')
    form_store.open_form(data, mode)
  else {
    if('course_name' in data) {
      let temp = {...form_store.chapter_null}
      temp.course_id = data.course_id
      form_store.open_form(temp, mode)
    }
    if('chapter_title' in data) {
      let temp = {...form_store.resource_null}
      temp.chapter_id = data.chapter_id
      form_store.open_form(temp, mode)
    }
  }
}

interface Tree {
  label: string,
  children?: Tree[],
  hide?: boolean,
}

const router = useRouter()
const handleClick = (data: any, node: Node) => {
  let link = ''
  while(node.parent.parent!==null) {
    link = '/' + node.data.label.replace(/ /g, '-') + link
    node = node.parent
  }
  router.push('/course' + '/' + course_store.current_course_id() + link)
}
const handleDelete = async (node: Node) => {
  const d = node.data as UnifyTree
  let msg
  if('chapter_title' in d.data)
    msg = await deleteChapterCall(d.data.chapter_id)
  if('resource_name' in d.data)
    msg = await deleteResourceCall(d.data.resource_id)
  if(!msg || msg.code!=200) {
    ElMessage({
      message: 'Network error',
      type: 'error',
    })
    return
  }
  handleClick(null, node.parent)
  await course_store.load_from_route(true)
}

import type Node from 'element-plus/es/components/tree/src/model/node'
import type { DragEvents } from 'element-plus/es/components/tree/src/model/useDragNode'
import type {
  AllowDropType,
  NodeDropType,
} from 'element-plus/es/components/tree/src/tree.type'
import { reactive, ref } from "vue";
import { useFormStore } from "@/stores/form";
import ChapterForm from "../forms/ChapterForm.vue";
import CoureseForm from "../forms/CoureseForm.vue";
import ResourseForm from "../forms/ResourseForm.vue";
import { deleteChapterCall } from "@/api/course/ChapterAPI";
import { deleteResourceCall } from "@/api/course/CourseResourceAPI";

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
  //console.log(folder)
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
<template>
  <div>
    <StudentTable ref="student_table_ref"/>
    <StudentEnroll ref="student_enroll_ref"/>
    <el-scrollbar style="border-right: solid 2px var(--ep-border-color); margin-right: -1px;">
      <el-tree
        :draggable="true"
        :allow-drag="allowDrag"
        :allow-drop="allowDrop"
        node-key="id"
        :default-expanded-keys="course_store.default_open"
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
              v-if="node.data===course_store.current_data && 
                    course_store.current_course_teacher()===auth_store.user.user_id"
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
                  <el-button v-if="node.level!=4" type='primary' style="margin: 0;" @click="open_form(node, 'Add')">{{ node.level==3 ? 'New version' : 'Add new' }}</el-button>
                  <el-button v-if="node.level==4" type='primary' style="margin: 0;" @click="to_top(node)">To top</el-button>
                  <el-button v-if="node.parent.parent!==null" type="danger" style="margin: 0;" @click="handleDelete(node)">Delete</el-button>
                  <el-button v-if="'course_name' in node.data.data" style="margin: 0;" type='primary' @click="student_table_ref.open_table()">Invite</el-button>
                  <el-button v-if="'course_name' in node.data.data" style="margin: 0;" type='primary' @click="student_enroll_ref.open_enroll()">Enroll</el-button>
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
    <ResourceForm/>
  </div>
</template>

<script lang="ts" setup>
import { useRouter } from "vue-router"
import { useCourseStore, type ResourceEntityPlus } from "@/stores/course";
import { type UnifyTree } from "@/stores/course";
import { ElMessage } from 'element-plus'

const auth_store = useAuthStore()
const form_store = useFormStore()
const course_store = useCourseStore()
const student_table_ref = ref()
const student_enroll_ref = ref()

const open_form = (node: Node, mode: 'Add'|'Edit') => {
  const children = (node.data as UnifyTree).children
  const cnt = children[children.length-1].order + 1
  const data = (node.data as UnifyTree).data
  if(mode=='Edit')
    form_store.open_form(data, mode)
  else {
    if('course_name' in data) {
      let temp: ChapterEntity = {...form_store.chapter_null}
      temp.course_id = data.course_id
      temp.chapter_order = cnt
      form_store.open_form(temp, mode)
    }
    if('chapter_title' in data) {
      let temp: ResourceEntityPlus = {...form_store.resource_null}
      temp.chapter_id = data.chapter_id
      temp.resource_order = cnt
      form_store.resource_mode = 'init'
      form_store.open_form(temp, mode)
    }
    if('resource_name' in data) {
      let temp: ResourceEntityPlus = {...data}
      temp.resource_version_order -= 1
      form_store.resource_mode = 'new_version'
      form_store.open_form(temp, mode)
    }
  }
}

async function to_top(node: Node) {
  const data = (node.data as UnifyTree).data
  const parent = (node.parent.data as UnifyTree).data
  if(!('resource_name' in data) || !('resource_name' in parent))
    return
  form_store.mode = 'Edit'
  form_store.resource_form = {...data}
  form_store.resource_form.chapter_id = parent.chapter_id
  form_store.resource_form.resource_name = parent.resource_name
  form_store.resource_form.resource_order = parent.resource_order
  form_store.resource_form.resource_version_order = parent.resource_version_order - 1
  if(await form_store.modify_resource()) {
    handleClick(null, node.parent)
    course_store.load_from_route(true)
  } else {
    ElMessage({
      message: 'To top network error',
      type: 'error',
    })
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
  if(msg && msg.code!=200) {
    ElMessage({
      message: 'Delete network error',
      type: 'error',
    })
    return
  }
  await course_store.load_from_route(true)
  handleClick(null, node.parent)
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
import { deleteChapterCall, updateChapterCall, type ChapterEntity } from "@/api/course/ChapterAPI";
import { deleteResourceCall, updateResourceMetadataCall, type ResourceEntity } from "@/api/course/CourseResourceAPI";
import { useAuthStore } from "@/stores/auth";

const allowDrop = (draggingNode: Node, dropNode: Node, type: AllowDropType) => {
  return draggingNode.level==dropNode.level && (type=='prev' || type=='next')
}
const allowDrag = (draggingNode: Node) => {
  return draggingNode.level > 1
}
const handleDrop = async (
  draggingNode: Node,
  p: Node,
) => {
  const parent = p.parent.data as UnifyTree
  let min = (draggingNode.level==4 && ('resource_name' in parent.data)) 
            ? parent.data.resource_version_order : -1
  console.log(parent.children)
  for(const sub of parent.children) {
    if(sub.order<=min) {
      min += 1
      let msg
      if('chapter_title' in sub.data) {
        console.log(min)
        sub.data.chapter_order = min
        msg = await updateChapterCall(sub.data.chapter_id, sub.data)
      }
      if('resource_name' in sub.data) {
        console.log(min)
        if(draggingNode.level!=4) {
          sub.data.resource_order = min
          for(const subsub of sub.children)
            if('resource_name' in subsub.data) {
              subsub.data.resource_order = min
              msg = await updateResourceMetadataCall(subsub.data.resource_id, subsub.data)
              if(msg && msg.code!=200) {
                ElMessage({
                  message: 'To top network error',
                  type: 'error',
                })
                return
              }
            }
        } else
          sub.data.resource_version_order = min
        msg = await updateResourceMetadataCall(sub.data.resource_id, sub.data)
      }
      if(msg && msg.code!=200) {
        ElMessage({
          message: 'To top network error',
          type: 'error',
        })
        return
      }
    }
    else
      min = sub.order + 1
  }
  await course_store.load_from_route(true)
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
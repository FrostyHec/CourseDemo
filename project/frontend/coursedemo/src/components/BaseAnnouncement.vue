<template>
  <div v-if="is_course">
    <div style="display: flex; align-items: baseline;">
      <h1 style="font-size: large; margin-bottom: 0;">
        Announcements
      </h1>
      <p style="font-size: medium; margin-left: 8px; color: var(--ep-color-primary); margin-bottom: 0;">
        {{ announcements.length }}
      </p>
    </div>

    <div v-if="course_store.current_course_teacher()===auth_store.user.user_id" style="width: 100%; margin-top: 18px;">
      <el-input
        v-model="new_announcement"
        :autosize="{ minRows: 4 }"
        type="textarea"
        placeholder="Add an announcement ..."
      />
      <div style="margin: 10px;"></div>
      <el-button type="primary" @click="send_announcement">
        Send
      </el-button>
      <el-button @click="new_announcement = ''">
        Reset
      </el-button>
      <el-button @click="dialog_visibility = true">
        Select students
      </el-button>
    </div>

    <div v-for="c in announcements" :key="c.notification_id" style="width: 100%; margin-top: 20px; border-top: solid 1px var(--ep-border-color); padding-top: 18px; overflow: hidden;">

        <div style="overflow: hidden; display: flex; align-items: center; gap: 8px;">
          <div i="ep-bell"/>
          <span style="line-height: 1.5; white-space-collapse: break-spaces; font-weight: 600;">
            {{ c.message }}
          </span>
        </div>

        <div style="display: flex; gap: 10px; flex-direction: row; align-items: center; margin-top: 12px; font-size: small;">
          <div style="color: var(--ep-text-color-placeholder); width: 130px;">  
            {{ (new Date(c.updated_at)).toLocaleString() }}
          </div>
          <el-popover v-if="course_store.current_course_teacher()===auth_store.user.user_id" placement="bottom-start" trigger="click" :hide-after="0" transition="None">
            <template #reference>
              <el-link type="primary" :underline="false" style="font-size: small;">Notify</el-link>
            </template>
            <div style="display: flex; gap: 5px; flex-direction: column">
              <el-button @click="do_something(c.notification_id, notifyViaSiteCall)">
                Via site
              </el-button>
              <el-button style="margin: 0;" @click="do_something(c.notification_id, notifyViaEmailCall)">
                Via Email
              </el-button>
            </div>
          </el-popover>
          <el-link v-if="course_store.current_course_teacher()===auth_store.user.user_id" type="danger" :underline="false" style="font-size: small;" @click="do_something(c.notification_id, deleteAnnouncementCall)">Delete</el-link>
        </div>
        
    </div>
    <div style="width: 100%; margin-top: 20px; border-top: solid 1px var(--ep-border-color); padding-top: 18px;"/>


    <el-dialog 
      v-model="dialog_visibility" 
      :title="'Students to announce'" 
      width="600"
      :open="load_students">
      <el-table :data="student_list" height="400">
        <el-table-column label="Name">
          <template #default="scope: {row: {student: StudentInfoWithEnrollStatus, select: boolean}}">
            {{ scope.row.student.first_name+' '+scope.row.student.last_name }}
          </template>
        </el-table-column>
        <el-table-column label="Email">
          <template #default="scope: {row: {student: StudentInfoWithEnrollStatus, select: boolean}}">
            {{ scope.row.student.email }}
          </template>
        </el-table-column>
        <el-table-column label="">
          <template #default="scope: {row: {student: StudentInfoWithEnrollStatus, select: boolean}}">
            <el-switch v-model="scope.row.select"/>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin: 10px;"></div>
      <el-button @click="all_set_false">
        Clear
      </el-button>
      <el-button @click="all_set_true">
        Select all
      </el-button>
    </el-dialog>
  
  </div>
</template>

<script lang='ts' setup>
import { reactive, ref, watch } from 'vue'
import { useCourseStore } from '@/stores/course';
import { type CourseEntity } from '@/api/course/CourseAPI'
import { type ChapterEntity } from '@/api/course/ChapterAPI'
import type { ResourceEntityPlus } from '@/stores/course';
import { ElMessage } from 'element-plus';
import { useAuthStore } from '@/stores/auth';
import { UserType } from '@/api/user/UserAPI';
import { createAnnouncementCall, deleteAnnouncementCall, getAnnouncementsByCourseIdCall, notifyViaEmailCall, notifyViaSiteCall, type AnnouncementEntity } from '@/api/course/AnnouncementAPI';
import { getAllStudentList, studentEnrollCourseCall, type StudentInfoWithEnrollStatus } from '@/api/course/CourseMemberAPI';
import type { APIResult } from '@/utils/APIUtils';

const course_store = useCourseStore()
const auth_store = useAuthStore()
const is_course = ref(false)
let current_course_id = -1
const announcements = ref<AnnouncementEntity[]>([{
  notification_id: 0, course_id: 0, receiver_ids: [], message: 'enjoy!!!',
  created_at: new Date(), updated_at: new Date()
}, {
  notification_id: 1, course_id: 0, receiver_ids: [], message: 'OOAD',
  created_at: new Date(), updated_at: new Date()
},
])

async function load_announcements() {
  announcements.value = []
  const msg = await getAnnouncementsByCourseIdCall(current_course_id)
  if(msg.code!=200) {
    ElMessage({
      message: 'Announcement network error',
      type: 'error',
    })
    return
  }
  announcements.value = msg.data.content
}
const watch_current_data = watch(() => course_store.current_data?.data,
  async (new_data: CourseEntity|ChapterEntity|ResourceEntityPlus|undefined) => {
    is_course.value = !!new_data && 'course_name' in new_data
    if(!new_data || !('course_name' in new_data))
      return
    if(new_data.course_id==current_course_id)
      return
    current_course_id = new_data.course_id
    await load_announcements()
    await load_students()
  },
  { immediate: true }
)

const new_announcement = ref('')
const student_list = ref<{student: StudentInfoWithEnrollStatus, select: boolean}[]>([])
async function send_announcement() {
  if(new_announcement.value==='')
    return
  const msg = await createAnnouncementCall(current_course_id, {
    notification_id: 0, course_id: current_course_id, 
    receiver_ids: student_list.value.reduce<number[]>(
    (pre, cur) => {
      if(cur.select)
        pre.push(cur.student.user_id)
      return pre
    }
    , []),
    message: new_announcement.value, 
    created_at: new Date(), updated_at: new Date(),
  })
  if(msg.code!=200) {
    ElMessage({
      message: 'send announcement network error',
      type: 'error',
    })
    return
  }
  new_announcement.value = ''
  await load_announcements()
}

async function load_students() {
  const msg = await getAllStudentList(current_course_id, 1, 100)
  if(msg.code!=200) {
    ElMessage({
      message: 'Announcement network error',
      type: 'error',
    })
    return
  }
  student_list.value = msg.data.content.map((value) => {
    return {student: value, select: true}
  })
}
function all_set_false() {
  student_list.value.forEach((value) => {value.select = false})
}
function all_set_true() {
  student_list.value.forEach((value) => {value.select = true})
}

const dialog_visibility = ref(false)

async function do_something(id: number, func: (id: number)=>Promise<APIResult<null>>, reload: boolean = false) {
  const msg = await func(id)
  if(msg.code==200) {
    ElMessage({
      message: 'Success',
      type: 'success',
    })
  } else {
    ElMessage({
      message: 'Announcement network error',
      type: 'error',
    })
    return
  }
  if(reload)
    await load_announcements()
}

</script>
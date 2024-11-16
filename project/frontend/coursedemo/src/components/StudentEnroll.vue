<template>
<el-dialog 
  v-model="table_visibility"
  title="Students' enroll" 
  width="900">

  <el-table :data="list" height="400">
    <el-table-column label="Name">
      <template #default="scope: {row: StudentInfoWithEnrollStatus}">
        {{ scope.row.first_name+' '+scope.row.last_name }}
      </template>
    </el-table-column>
    <el-table-column label="Email">
      <template #default="scope: {row: StudentInfoWithEnrollStatus}">
        {{ scope.row.email }}
      </template>
    </el-table-column>
    <el-table-column label="Status" width="350">
      <template #default="scope: {row: StudentInfoWithEnrollStatus}">
        <el-radio-group v-model="scope.row.status" style="padding: 0;" @change="update_status(scope.row)">
          <el-radio-button :label="StudentEnrollType.invited" :value="StudentEnrollType.invited"/>
          <el-radio-button :label="StudentEnrollType.publik" :value="StudentEnrollType.publik"/>
        </el-radio-group>
      </template>
    </el-table-column>
    <el-table-column label="Remove">
      <template #default="scope: {row: StudentInfoWithEnrollStatus}">
        <el-button type="danger" style="width: 0;" @click="remove_student(scope.row)">Remove</el-button>
      </template>
    </el-table-column>
  </el-table>

</el-dialog>
</template>
<script setup lang="ts">
import { getAllStudentList, removeStudentFromCourse, StudentEnrollType, teacherInviteStudentToCourseCall, updateStudentEnrollmentStatus } from '@/api/course/CourseMemberAPI';
import { type StudentInfoWithEnrollStatus } from '@/api/course/CourseMemberAPI';
import { UserType, type UserPublicInfoEntity } from '@/api/user/UserAPI';
import { reactive, watch, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useCourseStore } from '@/stores/course';
import { searchUserCall } from '@/api/user/UserAPI';


const course_store = useCourseStore()
const table_visibility = ref(false)
const list = ref<StudentInfoWithEnrollStatus[]>([])

async function load_list(id: number) {
  const msg = await getAllStudentList(id, 1, 100);
  if(msg.code!==200) {
    ElMessage({
      message: 'Student list network error',
      type: 'error',
    })
    return
  }
  list.value = msg.data.content
}
async function open_enroll() {
  const id = course_store.current_course_id()
  if(id===undefined || table_visibility.value)
    return
  load_list(id)
  table_visibility.value = true
}

async function invite(row: UserPublicInfoEntity) {
  const id = course_store.current_course_id()
  if(id===undefined)
    return
  const msg = await teacherInviteStudentToCourseCall(id, [row.user_id])
  if(msg.code!==200) {
    ElMessage({
      message: 'Student invite network error',
      type: 'error',
    })
  }
  return
}

async function update_status(row: StudentInfoWithEnrollStatus) {
  const id = course_store.current_course_id()
  if(id===undefined)
    return
  const msg = await updateStudentEnrollmentStatus(id, row.user_id, row.status)
  if(msg.code!==200) {
    ElMessage({
      message: 'Update status network error',
      type: 'error',
    })
  }
}

async function remove_student(row: StudentInfoWithEnrollStatus) {
  const id = course_store.current_course_id()
  if(id===undefined)
    return
  const msg = await removeStudentFromCourse(id, row.user_id)
  if(msg.code!==200) {
    ElMessage({
      message: 'Remove student network error',
      type: 'error',
    })
    return
  }
  await load_list(id)
}

defineExpose({open_enroll})

</script>
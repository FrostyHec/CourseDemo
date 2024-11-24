<template>
<el-dialog 
  v-model="table_visibility"
  title="Invite students" 
  width="600">

  <el-row :gutter="20" style="margin-bottom: 10px;">
    <el-col :span="9"> 
      <el-input v-model="first_name_input" placeholder="First Name" clearable></el-input>
    </el-col>
    <el-col :span="9">
      <el-input v-model="last_name_input" placeholder="Last Name" clearable/>
    </el-col>
    <el-col :span="6">
      <el-button type="primary" style="margin-bottom: 10px;" @click="load_list">Search</el-button>
    </el-col>
  </el-row>

  <el-table :data="list" height="400">
    <el-table-column label="Name">
      <template #default="scope: {row: UserPublicInfoEntity}">
        {{ scope.row.first_name+' '+scope.row.last_name }}
      </template>
    </el-table-column>
    <el-table-column label="Email">
      <template #default="scope: {row: UserPublicInfoEntity}">
        {{ scope.row.email }}
      </template>
    </el-table-column>
    <el-table-column label="Invite">
      <template #default="scope: {row: UserPublicInfoEntity}">
        <el-button 
          type="primary"
          @click="invite(scope.row)" 
        >
          Invite
        </el-button>
      </template>
    </el-table-column>
  </el-table>

</el-dialog>
</template>
<script setup lang="ts">
import { getAllStudentList, StudentEnrollType, teacherInviteStudentToCourseCall } from '@/api/course/CourseMemberAPI';
import { type StudentInfoWithEnrollStatus } from '@/api/course/CourseMemberAPI';
import { UserType, type UserPublicInfoEntity } from '@/api/user/UserAPI';
import { reactive, watch, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useCourseStore } from '@/stores/course';
import { searchUserCall } from '@/api/user/UserAPI';


const course_store = useCourseStore()
const table_visibility = ref(false)
const list = ref<UserPublicInfoEntity[]>([])

const first_name_input = ref('')
const last_name_input = ref('')

async function load_list() {
  const msg = await searchUserCall(first_name_input.value, last_name_input.value, 1, 100);
  if(msg.code!==200) {
    ElMessage({
      message: 'Student list network error',
      type: 'error',
    })
    return
  }
  list.value = msg.data.content
}
async function open_table() {
  const id = course_store.current_course_id()
  if(id===undefined || table_visibility.value)
    return
  first_name_input.value = ''
  last_name_input.value = ''
  table_visibility.value = true
}

async function invite(row: UserPublicInfoEntity) {
  const id = course_store.current_course_id()
  if(!id)
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

defineExpose({open_table})

</script>
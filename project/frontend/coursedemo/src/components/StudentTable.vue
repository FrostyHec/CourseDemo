<template>
<el-dialog 
  v-model="table_visibility"
  title="Invite students" 
  width="600">

  <el-table :data="list">
    <el-table-column label="Name">
      <template #default="scope: {row: StudentInfoWithEnrollStatus}">
        {{ scope.row.first_name }}
      </template>
    </el-table-column>
    <el-table-column label="Invite">
      <template #default="scope: {row: StudentInfoWithEnrollStatus}">
        <el-button 
          :type="scope.row.status===StudentEnrollType.invited ? '' : 'primary'" 
          :disabled="scope.row.status===StudentEnrollType.invited"
          @click="invite(scope.row)"
          style="width: 0;" 
        >
          {{ scope.row.status===StudentEnrollType.invited ? 'Invited' : 'Invite' }}
        </el-button>
      </template>
    </el-table-column>
  </el-table>

</el-dialog>
</template>
<script setup lang="ts">
import { getAllStudentList, StudentEnrollType, teacherInviteStudentToCourseCall } from '@/api/course/CourseMemberAPI';
import { type StudentInfoWithEnrollStatus } from '@/api/course/CourseMemberAPI';
import { UserType } from '@/api/user/UserAPI';
import { reactive, watch, ref } from 'vue';
import { ElMessage } from 'element-plus';


const props = defineProps({
  course_id: {
      type: Number,
      default: undefined,
  },
})
const table_visibility = ref(false)
const list = ref<StudentInfoWithEnrollStatus[]>([{
  status: StudentEnrollType.invited,
  user_id: 0, first_name: '123', last_name: 'happy',
  role: UserType.STUDENT, email: '123'
}, {
  status: StudentEnrollType.publik,
  user_id: 0, first_name: '123', last_name: 'happy',
  role: UserType.STUDENT, email: '123'
}, {
  status: StudentEnrollType.invited,
  user_id: 0, first_name: '123', last_name: 'happy',
  role: UserType.STUDENT, email: '123'
},
])

async function load_list(course_id: number) {
  // return
  const msg = await getAllStudentList(course_id, 0, 100);
  if(msg.code!==200) {
    ElMessage({
      message: 'Student list network error',
      type: 'error',
    })
    return
  }
  list.value = msg.content
}
async function open_table() {
  table_visibility.value = props.course_id!==undefined
  if(!props.course_id || table_visibility.value)
    return
  await load_list(props.course_id)
  table_visibility.value = true
}

async function invite(row: StudentInfoWithEnrollStatus) {
  if(!props.course_id)
    return
  const msg = await teacherInviteStudentToCourseCall(props.course_id, [row.user_id])
  if(msg.code!==200) {
    ElMessage({
      message: 'Student invite network error',
      type: 'error',
    })
  }
  row.status = StudentEnrollType.invited
  return
}

defineExpose({open_table})

</script>
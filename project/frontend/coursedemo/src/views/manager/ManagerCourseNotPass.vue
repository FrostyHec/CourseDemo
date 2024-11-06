<template>
  <el-config-provider namespace="ep">
    <el-container style="height: 100vh;">
      <el-header style="padding: 0%; height: auto;">
        <base-header />
      </el-header>
      <el-main>
        <el-header class="app-header">
          <div class="topbar">
            <el-row>
              <el-col :span="24">课程审批</el-col>
            </el-row>
          </div>
        </el-header>
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal">
            <el-menu-item index="1">待审批</el-menu-item>
            <el-menu-item index="2" @click="navigateTo('/manager/Passed')">已处理</el-menu-item>
        </el-menu>
        <el-container>
          <el-main>
            <el-table :data="courses" style="width: 100%">
              <el-table-column prop="course_name" label="课程名称"></el-table-column>
              <el-table-column prop="teacher_id" label="授课老师"></el-table-column>
              <el-table-column prop="status" label="状态" width="180"></el-table-column>
              <el-table-column prop="action" label="操作" width="500">       
                <template #default="scope">
                  <el-button @click="approveCourse(scope.row)">通过</el-button>
                  <el-button @click="rejectCourse(scope.row)">不通过</el-button>
                </template>             
              </el-table-column>
            </el-table>
          </el-main>
        </el-container>
        <el-button style="margin-left: 90%; margin-top: 10px" type="primary" @click="createNewCourse">创建课程</el-button>
      </el-main>
    </el-container>
      <!-- 创建课程对话框 -->
      <el-dialog
        title="添加课程"
        v-model="dialogVisible"
        width="40%"
      >
        <el-form
          :model="courseForm"
          label-width="auto"
          label-position="right"
          size="default"
        >
          <el-form-item label="课程名称" prop="course_name">
            <el-input v-model="courseForm.course_name"/>
          </el-form-item>
          <el-form-item label="授课老师id" prop="teacher_id">
            <el-input v-model="courseForm.teacher_id"/>
          </el-form-item>          
          <el-form-item label="描述" prop="description">
            <el-input v-model="courseForm.description"/>
          </el-form-item>
          <el-form-item label="课程类型" prop="publication">
            <el-radio-group v-model="courseForm.publication">
              <el-radio :label="Publication.open">开放</el-radio>
              <el-radio :label="Publication.closed">私密</el-radio>
              <el-radio :label="Publication.semi_open">半开放</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="AddCourse">创建</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
  </el-config-provider>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
const activeIndex = ref('1');
import BaseHeader from '../../layouts/BaseHeader.vue';
import router from '@/router';
import { useAuthStore } from '@/stores/auth';
import { getAllPendingApprovedCourse } from '@/api/course/CourseMemberAPI';
import { CourseEntity, CourseStatus, Publication, updateCoursePublicationCall, updateCourseStatusCall, type CourseStatusUpdateParam } from '@/api/course/CourseAPI';


onMounted(async () => {
    fetchCourses();
});

const currentPage = ref(1);
const pageSize = ref(10);
const authStore = useAuthStore();
const courses = ref<CourseEntity[]>([
{
    course_id: 1, course_name: 'CS303', description: 'xxx', teacher_id: 1, created_at: new Date(), updated_at: new Date(),
    status: CourseStatus.submitted,
    publication: Publication.open
}
]);
const courseForm = ref([
{
    course_id: 1, 
    course_name: '', 
    description: '', 
    teacher_id: 1, 
    created_at: new Date(), updated_at: new Date(),
    status: CourseStatus.submitted,
    publication: Publication.open
}
]);
const dialogVisible = ref(false);
const rules = ref({});

const navigateTo = (path: string) => {
    router.push(path); // 使用 router.push 进行路由跳转
};

const approveCourse = async (row: CourseEntity) => {
  const result = updateCourseStatusCall(row.course_id, CourseStatus.published);
  if((await result).code == 200){
      fetchCourses;
  }
  else{
      console.error('服务异常');
  }
};

const rejectCourse = async (row: CourseEntity) => {
  const result = updateCourseStatusCall(row.course_id, CourseStatus.rejected);
  if((await result).code == 200){
      fetchCourses;
  }
  else{
      console.error('服务异常');
  }
};

const createNewCourse = () => {
  dialogVisible.value = true;
};

const checkDuplicate = () => {
  const { CourseName } = courseForm.course_name;
  const isDuplicate = courses.value.some(course => {
    return course.course_name === CourseName;
  });
  return isDuplicate;
};

const AddCourse = () => {
  const form = courseForm.value;
  if (checkDuplicate()) {
    alert('Duplicate value!');
  } else {
    courses.value.push();
  }
  dialogVisible.value = false;
};

const fetchCourses = async () => {
try {
    const response = await getAllPendingApprovedCourse(authStore.user.user_id, currentPage.value, pageSize.value);
    courses.value = response.data.content;
} catch (error) {
    console.error('获取课程列表失败:', error);
}
};

</script>

<style scoped>
.app-header {
  background-color: #c2cbcc;
  color: #0e0b0b;
  text-align: center;
  line-height: 60px;
}

.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}

.el-dropdown-link:hover {
  color: #333;
}
</style>
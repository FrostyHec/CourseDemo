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
              <el-table-column prop="action" label="操作" width="500">       
                <template #default="scope">
                  <el-button @click="checkCourse(scope.row)">查看申请</el-button>
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
      <el-dialog
          title="课程信息"
          v-model="checkVisible"
          width="40%"
        >
        <div v-if="selectedCourse.course_name">
          <p><strong>课程名称：</strong>{{ selectedCourse.course_name }}</p>
          <p><strong>描述：</strong>{{ selectedCourse.description }}</p>
        </div>
      </el-dialog>
  </el-config-provider>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
const activeIndex = ref('1');
import BaseHeader from '@/layouts/BaseHeader.vue';
import router from '@/router';
import { useAuthStore } from '@/stores/auth';
import { getAllPendingApprovedCourse } from '@/api/course/CourseMemberAPI';
import { type CourseEntity, CourseStatus, createCourseCall, Publication, updateCourseStatusCall } from '@/api/course/CourseAPI';
import { ElMessage } from 'element-plus';


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
const courseForm = ref<CourseEntity>({
  course_id: 1,
  course_name: '',
  description: '',
  teacher_id: authStore.user.user_id,
  status: CourseStatus.submitted,
  publication: Publication.open,
  created_at: new Date(),
  updated_at: new Date(),
});

const checkVisible = ref(false);
const dialogVisible = ref(false);

const navigateTo = (path: string) => {
    router.push(path); // 使用 router.push 进行路由跳转
};

const selectedCourse = ref<CourseEntity>({} as CourseEntity); // 用于存储当前查看的课程信息

const checkCourse = (row: CourseEntity) => {
  selectedCourse.value = row; // 设置当前查看的课程信息
  checkVisible.value = true; // 显示对话框
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

const checkDuplicate = (name: string) => {
  const isDuplicate = courses.value.some(course => course.course_name.toLowerCase() === name.toLowerCase());
  return isDuplicate;
};

const AddCourse = () => {
  if (checkDuplicate(courseForm.value.course_name)) {
    ElMessage.error('课程已存在!');
    return
  } else {
    createCourseCall(courseForm.value);
    courses.value.push(courseForm.value);
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
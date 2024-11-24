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
            <el-menu-item index="1" @click="navigateTo('/manager/NotPass')">待审批</el-menu-item>
            <el-menu-item index="2">已处理</el-menu-item>
        </el-menu>
        <el-container>
          <el-main>
            <el-table :data="courses" style="width: 100%">
              <el-table-column prop="course_name" label="课程名称"></el-table-column>
              <el-table-column prop="teacher_id" label="授课老师"></el-table-column>
              <el-table-column prop="status" label="状态" width="180"></el-table-column>
            </el-table>
          </el-main>
        </el-container>
      </el-main>
    </el-container>
  </el-config-provider>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
const activeIndex = ref('2');
import BaseHeader from '../../layouts/BaseHeader.vue';
import router from '@/router';
import { CourseEntity, CourseStatus, EvaluationType, Publication } from '@/api/course/CourseAPI';
import { getAllPendingApprovedCourse } from '@/api/course/CourseMemberAPI';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();

onMounted(async () => {
    fetchCourses;
});

const currentPage = 1;
const pageSize = 10;

const courses = ref<CourseEntity[]>([
{
  course_id: 1, course_name: 'CS303', description: 'xxx', teacher_id: 1, created_at: new Date(), updated_at: new Date(),
  status: CourseStatus.published,
  publication: Publication.open,
  evaluation_type: EvaluationType.practice
}
]);
const navigateTo = (path: string) => {
    router.push(path); // 使用 router.push 进行路由跳转
};
const fetchCourses = async () => {
try {
    const response = await getAllPendingApprovedCourse(authStore.user.user_id, currentPage, pageSize);
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
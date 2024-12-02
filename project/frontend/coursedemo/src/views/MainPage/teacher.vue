<template>
  <el-config-provider namespace="ep">
    <el-container style="height: 100vh;">
      <el-header style="padding: 0; height: auto;">
        <base-header />
      </el-header>
      <el-main>
        <el-header class="app-header">
          <div class="topbar">
            <el-row>
              <el-col>负责课程列表</el-col>
            </el-row>
          </div>
        </el-header> 
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal">
          <el-menu-item index="1">管理中的课程</el-menu-item>
          <el-menu-item index="2" @click="navigateTo('/MainPage/teacher/manage')">课程状态</el-menu-item>
        </el-menu>
        <el-container>
          <el-main>
            <el-table :data="tableData" style="width: 100%">
              <el-table-column prop="course_name" label="课程名称">
                <template v-slot="{ row }">
                  <router-link :to="`/course/${row.course_id}`" class="course-link">{{ row.course_name }}</router-link>
                </template>
              </el-table-column>
              <el-table-column prop="checkJudge" label="">
                  <template v-slot="{ row }">
                    <el-button @click="navigateToCheckJudge(row)">课程评价</el-button>
                  </template>
                </el-table-column>
            </el-table>
            <el-pagination
              @current-change="handlePageChange"
              :current-page="currentPage"
              :page-size="pageSize"
              layout="prev, pager, next">
            </el-pagination>
          </el-main>
        </el-container>
      </el-main>
    </el-container>
  </el-config-provider>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import BaseHeader from '@/layouts/BaseHeader.vue';
import { useAuthStore } from '@/stores/auth';
import { getAllTeachingCourseList } from '@/api/course/CourseMemberAPI';
import { CourseStatus, EvaluationType ,createCourseCall, Publication, type CourseEntity } from '@/api/course/CourseAPI';
import router from '@/router';

const authStore = useAuthStore();
const activeIndex = ref('1');
const tableData = ref<CourseEntity[]>([
  {
    course_id: 1, course_name: 'CS303', description: 'xxx', teacher_id: 1, created_at: new Date(), updated_at: new Date(),
    status: CourseStatus.published,
    publication: Publication.open,
    evaluation_type: EvaluationType.practice
  }
]);
const currentPage = ref(1);
const pageSize = ref(10);
const dialogVisible = ref(false);
const deleteDialogVisible = ref(false);
const currentCourseToDelete = ref<CourseEntity | null>(null);
const courseForm = ref<CourseEntity>({
  course_id: 0,
  course_name: '',
  description: '',
  teacher_id: authStore.user.user_id,
  status: CourseStatus.creating,
  publication: Publication.open,
  created_at: new Date(),
  updated_at: new Date(),
  evaluation_type: EvaluationType.practice
});

onMounted(async () => {
  try {
    const response = await getAllTeachingCourseList(authStore.user.user_id, currentPage.value, pageSize.value);
    tableData.value = response.data.content;
  } catch (error) {
    console.error('获取课程列表失败:', error);
  }
});

const navigateToCheckJudge = (row:CourseEntity) =>{
  router.push({ path: '/course/CheckEvaluation', query: { course_id: row.course_id } });
}

const navigateTo = (path: string) => {
  router.push(path); // 使用 router.push 进行路由跳转
};

const handlePageChange = (newPage: number) => {
  currentPage.value = newPage;
  fetchCourses();
};

const fetchCourses = async () => {
  try {
    const response = await getAllTeachingCourseList(authStore.user.user_id, currentPage.value, pageSize.value);
    tableData.value = response.data.content;
  } catch (error) {
    console.error('获取课程列表失败:', error);
  }
};
</script>

<style scoped>
.el-header {
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

.app-header {
  background-color: #409eff;
  color: white;
  text-align: center;
  line-height: 60px;
}

.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.search-col {
  display: flex;
  justify-content: flex-end;
}

.button-row {
  margin-top: 10px;
  text-align: right;
  background-color: #fff;
  padding: 10px 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.el-table {
  margin-top: 20px;
}

.el-table-column {
  text-align: center;
}

.el-main {
  padding: 20px;
}

.el-button:hover {
  background-color: #66b1ff;
  border-color: #66b1ff;
}

.el-input__inner {
  border-radius: 20px;
  padding: 0 20px;
  height: 40px;
}

.el-button--primary {
  background-color: #67c23a;
  border-color: #67c23a;
}

.el-button--primary:hover {
  background-color: #85ce61;
  border-color: #85ce61;
}

.el-table th {
  background-color: #f0f9ff;
  color: #333;
}

.course-link {
  text-decoration: none;
  color: #409eff;
  font-weight: bold;
  transition: color 0.3s;
}

.course-link:hover {
  color: #66b1ff;
  text-decoration: underline;
}
</style>
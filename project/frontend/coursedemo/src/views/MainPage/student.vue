<template>
  <el-config-provider namespace="ep">
    <el-container style="height: 100vh;">
      <el-header style="padding: 0; height: auto;">
        <base-header />
      </el-header>
      <el-button class="return" @click="navigateToHome">返回主页</el-button>
      <el-main>
        <el-container>
          <el-header class="app-header">
            <div class="topbar">
              <el-row :gutter="20" type="flex" justify="space-between" align="middle">
                <el-col :span="24">
                  已选课程列表
                </el-col>
              </el-row>
            </div>
          </el-header>
          <el-col :span="6" class="search-col">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索课程名称"
              clearable
              style="width: 200px;"
            />
            <el-button type="primary" icon="el-icon-search" class="search-button" @click="handleSearch">search</el-button>
          </el-col>
          
          <el-container>
            <el-main>
              <el-table :data="tableData" style="width: 100%">
                <el-table-column prop="course_name" label="课程名称">
                  <template v-slot="{ row }">
                    <router-link :to="`/course/${row.course_id}`" class="course-link">{{ row.course_name }}</router-link>
                  </template>
                </el-table-column>
              </el-table>
            </el-main>
          </el-container>
        </el-container>
      </el-main>
    </el-container>
  </el-config-provider>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import BaseHeader from '@/layouts/BaseHeader.vue';
import { useRouter } from 'vue-router'; // 导入 useRouter
import { getAllJoinedCourseList } from '@/api/course/CourseMemberAPI';
import { CourseStatus, EvaluationType, Publication, type CourseEntity } from '@/api/course/CourseAPI';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();
// 表格数据
const tableData = ref<CourseEntity[]>([
  {
    course_id: 1, course_name: 'CS303', description: 'xxx', teacher_id: 1, created_at: new Date(), updated_at: new Date(),
    status: CourseStatus.published,
    publication: Publication.open,
    evaluation_type: EvaluationType.practice
  }
]);

onMounted(async () => {
    fetchCourses();
});

const router = useRouter(); 
const searchKeyword = ref('');

const navigateToHome = () => {
  router.push('/MainPage'); 
};

const handleSearch = () => {
  tableData.value = tableData.value.filter(repo =>
    repo.course_name.toLowerCase().includes(searchKeyword.value.toLowerCase())
  );
};

const fetchCourses = async () => {
try {
    const response = await getAllJoinedCourseList(authStore.user.user_id, currentPage.value, pageSize.value);
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
  margin-top: 10px;
  margin-left: 10px;
  margin-right: auto;
}

.search-input {
  margin-right: 10px; /* 搜索框和搜索按钮之间的间隔 */
  width: 200px;
}

.search-button {
  width: 90px; /* 调整按钮宽度 */
  height: 36px; /* 调整按钮高度 */
  font-size: 14px; /* 调整字体大小 */
  padding: 15; /* 调整内部填充，如果需要可以设置为其他值 */
  margin-left: 10px;
  margin-right: auto;
}
.return {
  display: flex;
  align-items: center;
  margin-left: 20px;
  margin-right: auto;
  margin-top: 10px;
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
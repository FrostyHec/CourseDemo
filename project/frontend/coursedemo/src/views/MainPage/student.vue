<template>
  <el-config-provider namespace="ep">
    <el-container style="height: 100vh;">
      <el-header style="padding: 0; height: auto;">
        <base-header />
      </el-header>
      <el-main>
        <el-container>
          <el-header class="app-header">
            <div class="topbar">
              <el-row gutter={20} type="flex" justify="space-between" align="middle">
                <el-col :span="24">
                  已选课程列表
                </el-col>
              </el-row>
            </div>
          </el-header>
    
          <div class="button-row">
            <el-col :span="6" class="search-col">
                  <el-input
                    v-model="searchKeyword"
                    placeholder="搜索课程名称或老师"
                    clearable
                    style="width: 200px;"
                  />
            </el-col>
            <el-button @click="toggleCourses('all')">全部课程</el-button>
            <el-button @click="toggleCourses('mine')">我的课程</el-button>
          </div>
    
          <el-container>
            <el-main>
              <el-table :data="filteredTableData" style="width: 100%">
                <el-table-column prop="course_name" label="课程名称">
                  <template v-slot="{ row }">
                    <router-link :to="`/course/${row.course_id}`" class="course-link">{{ row.CourseName }}</router-link>
                  </template>
                </el-table-column>
                <el-table-column prop="action" label="操作" width="200">
                  <template v-slot="{ row }">
                    <el-button @click="row.enrolled ? quitCourse(row) : joinCourse(row)">
                      {{ row.enrolled ? '退课' : '加入' }}
                    </el-button>
                  </template>
                </el-table-column>
                <el-table-column prop="action" label="课程评价" width="200">
                  <template v-slot="{ row }">
                    <el-button @click="evaluateCourse(row)">评价</el-button>
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

<script  lang="ts">
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

// 获取 router 实例
const router = useRouter();

import {useAuthStore} from '@/stores/auth';

const authStore = useAuthStore();

interface Course {
  course_id: string;
  CourseName: string;
  teacher?: string;
  enrolled?: boolean;
}

const tableData = ref<Course[]>([
  { course_id: '2', CourseName: 'CS303' } // 假设初始数据
]);

const searchKeyword = ref('');
const myCourses = ref(true);

import { onMounted } from 'vue';
import { getAllJoinedCourseList } from '@/api/course/CourseMemberAPI';

onMounted(async () => {
  try {
    const response = await getAllJoinedCourseList(authStore.user.user_id, 1, 10);
    const courses = response.content; 
    tableData.value = [...tableData.value, ...courses];
  } catch (error) {
    console.error('获取课程列表失败:', error);
  }
});

const filteredTableData = computed(() => {
  let filtered = tableData.value;
  if (myCourses.value) {
    filtered = filtered.filter(row => row.enrolled);
  }
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase();
    filtered = filtered.filter(row =>
      row.CourseName.toLowerCase().includes(keyword) ||
      row.teacher?.toLowerCase().includes(keyword)
    );
  }
  return filtered;
});

const quitCourse = (row: Course) => {
  console.log('quit course:', row);
  // 更新row.enrolled为false
  row.enrolled = false;
};

const joinCourse = (row: Course) => {
  console.log('join course:', row);
  // 更新row.enrolled为true
  row.enrolled = true;
};

const evaluateCourse = (row: Course) => {
  // 将课程信息传递给课程评价页面
  router.push({ path: '/courseEvaluation', query: { courseId: row.course_id } });
};

const toggleCourses = (type: string) => {
  myCourses.value = type === 'mine';
};
</script>

<style scoped>
.el-config-provider {
  font-family: 'Arial', sans-serif; /* 设置字体 */
}

.el-header {
  background-color: #f5f7fa; /* 浅色背景 */
  border-bottom: 1px solid #e4e7ed; /* 底部边框 */
}

.app-header {
  background-color: #409eff; /* 主题蓝色背景 */
  color: white;
  text-align: center;
  line-height: 60px;
}

.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px; /* 内边距 */
}

.search-col {
  display: flex;
  justify-content: flex-end;
  margin-right: 0px; /* 搜索框距离容器右边的距离 */
}

.button-row {
  margin-top: 0px;
  margin-bottom: 0px;
  text-align: right; /* 按钮行右对齐 */
  background-color: #fff; /* 背景颜色 */
  padding: 10px 20px; /* 内边距 */
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1); /* 阴影效果 */
}

.button-row .el-button {
  margin-left: 10px; /* 按钮间距 */
  border-radius: 24px; /* 按钮圆角 */
  padding: 12px 20px; /* 按钮内边距 */
  font-size: 14px; /* 按钮字体大小 */
}

.el-table {
  margin-top: 0px; /* 表格上方间距 */
}

.el-button {
  margin-left: 10px; /* 按钮间距 */
}

.el-table {
  margin-top: 20px; /* 表格上方间距 */
}

.el-table-column {
  text-align: center; /* 列内容居中 */
}

.el-main {
  padding: 20px; /* 主内容区域的内边距 */
}

/* 添加一些悬停效果 */
.el-button:hover {
  background-color: #66b1ff;
  border-color: #66b1ff;
}

/* 自定义输入框样式 */
.el-input__inner {
  border-radius: 20px; /* 输入框圆角 */
  padding: 0 20px; /* 输入框内边距 */
  height: 40px; /* 输入框高度 */
}

/* 自定义按钮样式 */
.el-button--primary {
  background-color: #67c23a; /* 按钮背景色 */
  border-color: #67c23a; /* 按钮边框色 */
}

.el-button--primary:hover {
  background-color: #85ce61;
  border-color: #85ce61;
}

/* 自定义表格样式 */
.el-table th {
  background-color: #f0f9ff; /* 表头背景色 */
  color: #333; /* 表头字体色 */
}

.el-table td {
  border-bottom: 1px solid #ebeef5; /* 单元格底部边框 */
}

/* 自定义分页组件样式 */
.el-pagination {
  margin-top: 20px; /* 分页组件上方间距 */
  text-align: center; /* 分页组件居中 */
}

.course-link {
  text-decoration: none; /* 去除下划线 */
  color: #409eff; /* 设置链接颜色 */
  font-weight: bold; /* 设置字体加粗 */
  transition: color 0.3s; /* 平滑颜色变化 */
}

.course-link:hover {
  color: #66b1ff; /* 鼠标悬停时的颜色 */
  text-decoration: underline; /* 鼠标悬停时添加下划线 */
}
</style>
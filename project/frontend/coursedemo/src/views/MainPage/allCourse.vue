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
                <el-row :gutter="20" type="flex" justify="space-between" align="middle">
                  <el-col :span="24">
                    全部课程列表
                  </el-col>
                </el-row>
              </div>
            </el-header>
            <div class="button-row">
              <el-col :span="6" class="search-col">
                <el-input
                  v-model="searchKeyword"
                  placeholder="搜索课程名称"
                  clearable
                  style="width: 200px;"
                />
              </el-col>
              <router-link to="/MainPage/student" class="all-courses-link">我的课程</router-link>
            </div>
      
            <el-container>
              <el-main>
                <el-table :data="filteredTableData" style="width: 100%">
                  <el-table-column prop="course_name" label="课程名称">
                  </el-table-column>
                </el-table>
              </el-main>
            </el-container>
          </el-container>
        </el-main>
      </el-container>
    </el-config-provider>
  </template>
  
  <script lang="ts">
  import { ref, computed, onMounted } from 'vue';
  import { useAuthStore } from '@/stores/auth';
  import { CourseStatus, Publication, searchCourseCall, type CourseEntity } from '@/api/course/CourseAPI';
  
  const authStore = useAuthStore();
  
  // 表格数据
  const tableData = ref<CourseEntity[]>([
    {
      course_id: 1, course_name: 'CS303', description: 'xxx', teacher_id: 1, created_at: new Date(), updated_at: new Date(),
      status: CourseStatus.published,
      publication: Publication.open
    }
  ]);
  
  onMounted(async () => {
    try {
      const response = await searchCourseCall(10, 1);
      const courses = response.data.content; 
      if (courses) {
        tableData.value = [...tableData.value, ...courses];
      }
    } catch (error) {
      console.error('获取课程列表失败:', error);
    }
  });
  
  const searchKeyword = ref('');
  
  const filteredTableData = computed(() => {
    let result = tableData.value;
    if (searchKeyword.value.trim() !== '') {
      result = result.filter(course => 
        course.course_name.toLowerCase().includes(searchKeyword.value.toLowerCase())
      );
    }
    return result;
  });
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
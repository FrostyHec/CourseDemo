<template>
    <el-container style="height: 100vh;">
      <el-header style="padding: 0; height: 60px; background-color: #409eff; color: white; display: flex; align-items: center; justify-content: space-between;">
        <h1>热门课程</h1>
        <el-input
          v-model="searchKeyword"
          placeholder="搜索课程名称或老师"
          clearable
          style="width: 200px;"
        />
      </el-header>
      <el-main>
        <el-table :data="filteredCourses" style="width: 100%">
          <el-table-column prop="name" label="课程名称" width="180"></el-table-column>
          <el-table-column prop="teacher" label="授课老师" width="180"></el-table-column>
          <el-table-column prop="students" label="已选人数" width="180"></el-table-column>
          <el-table-column prop="hot" label="热度" width="180"></el-table-column>
        </el-table>
      </el-main>
    </el-container>
  </template>
  
  <script setup>
  import { ref, computed } from 'vue';
  import { ElContainer, ElHeader, ElMain, ElTable, ElTableColumn, ElInput } from 'element-plus';
  
  const courses = ref([
    { id: 1, name: 'CS303 Artificial Intelligence', teacher: 'John Doe', students: 30, hot: 95 },
    { id: 2, name: 'CS309 OOAD', teacher: 'Jane Smith', students: 25, hot: 85 },
    { id: 3, name: 'CS324 Deep Learning', teacher: 'Alice Johnson', students: 40, hot: 98 },
    // ...更多课程数据
  ]);
  
  const searchKeyword = ref('');
  
  const filteredCourses = computed(() => {
    return courses.value.filter(course =>
      course.name.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
      course.teacher.toLowerCase().includes(searchKeyword.value.toLowerCase())
    );
  });
  </script>
  
  <style scoped>
  .el-header {
    padding: 0 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .el-main {
    padding: 20px;
  }
  </style>
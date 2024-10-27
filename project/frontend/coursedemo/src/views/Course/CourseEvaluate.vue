<template>
    <el-container style="height: 100vh;">
      <el-aside width="200px" style="background-color: #f4f4f5;">
        <el-menu default-active="1">
          <el-menu-item index="1">课程评价</el-menu-item>
          <!-- 可以添加更多菜单项 -->
        </el-menu>
      </el-aside>
      <el-main>
        <el-card class="course-evaluation-card">
          <!-- 使用 v-slot 指令定义 header 插槽内容 -->
          <template v-slot:header>
            <div class="course-evaluation-header">
              课程评价 - CS101 Introduction to Computer Science
            </div>
          </template>
          <el-form ref="evaluationForm" :model="evaluationForm" label-width="120px">
            <el-form-item label="课程满意度">
              <el-rate v-model="evaluationForm.satisfaction" show-text></el-rate>
            </el-form-item>
            <el-form-item label="教学内容质量">
              <el-rate v-model="evaluationForm.contentQuality" show-text></el-rate>
            </el-form-item>
            <el-form-item label="教师授课质量">
              <el-rate v-model="evaluationForm.teachingQuality" show-text></el-rate>
            </el-form-item>
            <el-form-item label="课程难度">
              <el-slider v-model="evaluationForm.difficulty" show-stops></el-slider>
            </el-form-item>
            <el-form-item label="综合评价">
              <el-input
                type="textarea"
                v-model="evaluationForm.overview"
                placeholder="请输入内容"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitEvaluation">提交评价</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-main>
    </el-container>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import { ElMessage } from 'element-plus';
  
  const evaluationForm = ref({
    satisfaction: 0,
    contentQuality: 0,
    teachingQuality: 0,
    difficulty: 0,
    overview: '',
  });
  
  const submitEvaluation = () => {
    console.log('提交的评价:', evaluationForm.value);
    // 在这里添加提交评价的逻辑，例如发送请求到后端API
    ElMessage.success('评价提交成功！');
  };
  </script>
  
  <style scoped>
  .course-evaluation-card {
    max-width: 600px;
    margin: 20px auto;
  }
  
  .course-evaluation-header {
    font-size: 18px;
    font-weight: bold;
  }
  </style>
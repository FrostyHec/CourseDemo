<template>
  <div class="survey-container">
    <h2 class="survey-title">{{ course[0].course_name }}课程教学评价问卷</h2>
    
    <div class="question">
      <p>1. 理论课教学中，您是否得到了满足？</p>
      <div class="options">
        <label v-for="option in question1Options" :key="option">
          <input type="radio" :value="option" v-model="question1Result" />{{ option }}
        </label>
      </div>
    </div>
  
    <div class="question">
      <p>2. 您在理论课中的学习体验如何？</p>
      <div class="options">
        <label v-for="option in question2Options" :key="option">
          <input type="radio" :value="option" v-model="question2Result" />{{ option }}
        </label>
      </div>
    </div>
  
    <div class="question">
      <p>3. 您对理论课教师的教学质量感到满意吗？</p>
      <div class="options">
        <label v-for="option in question3Options" :key="option">
          <input type="radio" :value="option" v-model="question3Result" />{{ option }}
        </label>
      </div>
    </div>
  
    <!-- 添加课程评分 -->
    <div class="question">
      <p>4. 请为这门课程打分（1-5分）：</p>
      <el-rate v-model="evaluationForm.score" show-text></el-rate>
    </div>
  
    <!-- 添加评论框 -->
    <div class="question">
      <p>5. 对这门课程，您有什么建议或评论？</p>
      <el-input
        type="textarea"
        v-model="evaluationForm.comment"
        placeholder="请输入您的建议或评论..."
      ></el-input>
    </div>
  
    <el-button @click="submitForm">提交评价</el-button>
  </div>
</template>

<script setup lang="ts">
import { CourseStatus, EvaluationType, getCourseCall, Publication, type CourseEntity } from '@/api/course/CourseAPI';
import { evaluationType, updateEvaluationCall, type answer, type CourseEvaluationEntity } from '@/api/course/CourseEvaluationAPI';
import router from '@/router';
import { onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';

// 存储单选问题的结果
const question1Result = ref('');
const question2Result = ref('');
const question3Result = ref('');

const evaluation_form_answer = ref<answer[]>([
  {
    id: 1,
    type: evaluationType.rating,
    result: '',
  },
  {
    id: 2,
    type: evaluationType.rating,
    result: '',
  },
  {
    id: 3,
    type: evaluationType.rating,
    result: '',
  },
]);

const course_id = 0;
const student_id = 0;

const course = ref<CourseEntity[]>([
  {
    course_id: 1,
    course_name: 'CS303',
    description: 'xxx',
    teacher_id: 1,
    created_at: new Date(),
    updated_at: new Date(),
    status: CourseStatus.published,
    publication: Publication.open,
    evaluationType: EvaluationType.practice,
  },
]);

const evaluationForm = ref<CourseEvaluationEntity>({
  course_id: course_id,
  student_id: student_id,
  comment: '',
  score: 0,
  evaluation_form_answer: evaluation_form_answer,
  created_at: new Date(),
  updated_at: new Date(),
});

onMounted(async () => {
  const course_id = router.currentRoute.value.params.course_id;
  await fetchCourses();
});

const fetchCourses = async () => {
  try {
    const response = await getCourseCall(course_id);
    const courseData = response.data;
    course.value = [courseData];
  } catch (error) {
    console.error('获取课程失败:', error);
  }
};

const question1Options = [
  "非常不满意",
  "不满意",
  "一般",
  "满意",
  "非常满意"
];

const question2Options = [
  "非常不满意",
  "不满意",
  "一般",
  "满意",
  "非常满意"
];

const question3Options = [
  "非常不满意",
  "不满意",
  "一般",
  "满意",
  "非常满意"
];

const submitForm = () => {
  evaluation_form_answer.value[0].result = question1Result.value;
  evaluation_form_answer.value[1].result = question2Result.value;
  evaluation_form_answer.value[2].result = question3Result.value;

  console.log(evaluationForm.value);
  updateEvaluationCall(course_id, evaluationForm.value);
  ElMessage.success('评价提交成功！');
};
</script>

<style scoped>
.survey-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Arial', sans-serif;
}

.survey-title {
  text-align: center;
  font-size: 24px;
  margin-bottom: 20px;
}

.question {
  margin-bottom: 20px;
}

.options label {
  display: block;
  margin-bottom: 10px;
}

.options input[type="radio"] {
  margin-right: 5px;
}

.el-button {
  margin-top: 20px;
}
</style>
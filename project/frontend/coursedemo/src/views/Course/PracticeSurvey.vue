<template>
    <div class="survey-container">
      <h2 class="survey-title">{{course[0].course_name}}课程教学评价问卷</h2>
      
      <div class="question">
        <p>1. 实践课教学中，您认为以下哪些方面得到了满足？</p>
        <div class="options">
          <label v-for="option in question1Options" :key="option">
            <input type="checkbox" />{{ option }}
          </label>
        </div>
      </div>
  
      <div class="question">
        <p>2. 您在实践课中的学习体验如何？</p>
        <div class="options">
          <label v-for="option in question2Options" :key="option">
            <input type="checkbox" />{{ option }}
          </label>
        </div>
      </div>
  
      <div class="question">
        <p>3. 您认为实践课教师最大的优点是什么？</p>
        <div class="options">
          <label v-for="option in question3Options" :key="option">
            <input type="radio" name="teacherMerit" />{{ option }}
          </label>
        </div>
      </div>
    </div>
</template>
  
<script setup lang="ts">
import { CourseStatus, getCourseCall, Publication, type CourseEntity } from '@/api/course/CourseAPI';
import router from '@/router';
import { onMounted, ref } from 'vue';

const course = ref<CourseEntity[]>([
    {
    course_id: 1, course_name: 'CS303', description: 'xxx', teacher_id: 1, created_at: new Date(), updated_at: new Date(),
    status: CourseStatus.published,
    publication: Publication.open
    }
]);

const course_id = 0;

onMounted(async () => {
  const course_id = router.currentRoute.course_id;
  await fetchCourses();
});

const fetchCourses = async () => {
  try {
    const response = await getCourseCall(course_id);
    const course = response.data;
  } catch (error) {
    console.error('获取课程失败:', error);
  }
};


const question1Options = [
  "A. 实践内容与理论课程紧密相关",
  "B. 实践指导老师专业且易于沟通",
  "C. 实践设备充足且维护良好",
  "D. 实践课程安排合理，时间充足"
];

const question2Options = [
  "实践课提高了我的动手能力",
  "我在实践课中能够很好地应用理论知识",
  "实践课的学习氛围积极",
  "我对实践课的成果感到满意"
];

const question3Options = [
  "A. 教师指导细致，易于理解",
  "B. 教师能够激发学生的探索兴趣",
  "C. 教师在实践中引入创新方法",
  "D. 教师对设备操作非常熟练",
  "E. 其他"
];
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
  
  .options input[type="checkbox"],
  .options input[type="radio"] {
    margin-right: 5px;
  }
  </style>
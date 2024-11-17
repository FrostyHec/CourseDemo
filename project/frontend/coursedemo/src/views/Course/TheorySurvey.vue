<template>
    <div class="survey-container">
      <h2 class="survey-title">{{course[0].course_name}}课程教学评价问卷</h2>
      
      <div class="question">
        <p>1. 理论课教学中，您认为以下哪些方面得到了满足？</p>
        <div class="options">
          <label v-for="option in question1Options" :key="option">
            <input type="checkbox" />{{ option }}
          </label>
        </div>
      </div>
  
      <div class="question">
        <p>2. 您在理论课中的学习体验如何？</p>
        <div class="options">
          <label v-for="option in question2Options" :key="option">
            <input type="checkbox" />{{ option }}
          </label>
        </div>
      </div>
  
      <div class="question">
        <p>3. 您认为理论课教师最大的优点是什么？</p>
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
    "A. 课程内容深入且有挑战性",
    "B. 教师讲解清晰，易于理解",
    "C. 课程资料丰富，便于课后复习",
    "D. 课程与实际应用紧密相关"
  ];
  
  const question2Options = [
    "理论课提高了我的理解能力",
    "我在理论课中能够深入思考问题",
    "理论课的学习氛围严谨",
    "我对理论课的深度感到满意"
  ];
  
  const question3Options = [
    "A. 教师讲解逻辑清晰，易于跟随",
    "B. 教师能够引导学生进行批判性思考",
    "C. 教师在课程中引入跨学科视角",
    "D. 教师对复杂概念的解释非常透彻",
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
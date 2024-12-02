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
  
    <!-- 作业的合理性 -->
    <div class="question">
      <p>4. 您认为作业量是否合理？</p>
      <div class="options">
        <label v-for="option in homeworkReasonableOptions" :key="option">
          <input type="radio" :value="option" v-model="homeworkReasonableResult" />{{ option }}
        </label>
      </div>
    </div>
    
    <!-- 教师的教学能力 -->
    <div class="question">
      <p>5. 您如何评价教师的教学能力？</p>
      <div class="options">
        <label v-for="option in teachingAbilityOptions" :key="option">
          <input type="radio" :value="option" v-model="teachingAbilityResult" />{{ option }}
        </label>
      </div>
    </div>
    
    <!-- 考试难度 -->
    <div class="question">
      <p>6. 您认为考试难度是否适中？</p>
      <div class="options">
        <label v-for="option in examDifficultyOptions" :key="option">
          <input type="radio" :value="option" v-model="examDifficultyResult" />{{ option }}
        </label>
      </div>
    </div>

    <!-- 添加课程评分 -->
    <div class="question">
      <p>7. 请为这门课程打分（1-5分）：</p>
      <el-rate v-model="evaluationForm.score" show-text></el-rate>
    </div>
  
    <!-- 添加评论框 -->
    <div class="question">
      <p>8. 对这门课程，您有什么建议或评论？</p>
      <el-input
        type="textarea"
        v-model="evaluationForm.comment"
        placeholder="请输入您的建议或评论..."
      ></el-input>
    </div>
  
    <el-button @click="submitJudge = true">提交评价</el-button>
  </div>
  <el-dialog
          title="加入课程"
          v-model="submitJudge"
        >
          <span>确定要提交课程评价吗？</span>
          <template #footer>
            <el-button @click="submitJudge = false">取消</el-button>
            <el-button type="primary" @click="submitForm(); submitJudge=false;">确认</el-button>
          </template>
        </el-dialog>
</template>

<script setup lang="ts">
import { CourseStatus, EvaluationType, getCourseCall, Publication, type CourseEntity } from '@/api/course/CourseAPI';
import { createEvaluationCall, evaluationType, getMyEvaluationCall, updateEvaluationCall, type answer, type CourseEvaluationEntity } from '@/api/course/CourseEvaluationAPI';
import { onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useRoute } from 'vue-router';
import router from '@/router';

const submitJudge = ref(false)
// 存储单选问题的结果
const question1Result = ref('');
const question2Result = ref('');
const question3Result = ref('');
const homeworkReasonableResult = ref('');
const teachingAbilityResult = ref('');
const examDifficultyResult = ref('');

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
  {
    id: 4,
    type: evaluationType.rating,
    result: '',
  },
  {
    id: 5,
    type: evaluationType.rating,
    result: '',
  },
  {
    id: 6,
    type: evaluationType.rating,
    result: '',
  },
]);

let course_id = 0;
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
    evaluation_type: EvaluationType.practice,
  },
]);

const evaluationForm = ref<CourseEvaluationEntity>(
  {
    course_id: 0,
    student_id: 0,
    comment: '',
    score: 0,
    evaluation_form_answer: evaluation_form_answer.value,
    created_at: new Date(),
    updated_at: new Date()
  }
);

onMounted(async () => {
  const route = useRoute(); // 使用 useRoute 钩子获取当前路由对象
  const courseId = Number(route.query.course_id); // 从查询参数中获取 course_id 并转换为数字
  course_id = Number(courseId);
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

const homeworkReasonableOptions = [
  "非常不合理",
  "不合理",
  "一般",
  "合理",
  "非常合理"
];

const teachingAbilityOptions = [
  "非常差",
  "差",
  "一般",
  "好",
  "非常好"
];

const examDifficultyOptions = [
  "过于简单",
  "简单",
  "适中",
  "难",
  "过于难"
];

const submitForm = async () => {
  evaluation_form_answer.value[0].result = question1Result.value;
  evaluation_form_answer.value[1].result = question2Result.value;
  evaluation_form_answer.value[2].result = question3Result.value;
  evaluation_form_answer.value[3].result = homeworkReasonableResult.value;
  evaluation_form_answer.value[4].result = teachingAbilityResult.value;
  evaluation_form_answer.value[5].result = examDifficultyResult.value;

  console.log(evaluationForm.value);
  const response=await getMyEvaluationCall(course_id);
  if(response.data==null){
    await createEvaluationCall(course_id, evaluationForm.value);
  }
  else{
    await updateEvaluationCall(course_id, evaluationForm.value);
  }
  ElMessage.success('评价提交成功！');
  router.push('/MainPage/student')
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
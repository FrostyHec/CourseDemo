<template>
  <el-header style="padding: 0; height: auto;">
      <base-header />
  </el-header>
  <el-button @click="router.push('/MainPage/teacher')">返回主页</el-button>
  <div class="course-evaluation">
      <el-rate v-model="course_score" disabled></el-rate>
          <p>课程平均分: {{ course_score }} 分</p>
      <div v-for="review in evaluation" :key="review.student_id" class="course-review">
        学生评价：
        <el-rate v-model="review.score" disabled></el-rate>
          <el-button @click="viewEvaluation(review)">查看评价</el-button>
      </div>
      <el-pagination
        @current-change="handleCurrentChange"
        layout="prev, pager, next"
        :total="50">
      </el-pagination>
  </div>
  <el-drawer
      title="评价详情"
      v-model="isSidebarVisible"
      direction="rtl"
      size="30%"
  >
      <div v-if="selectedEvaluation">
          <p>学生ID: {{ selectedEvaluation.student_id }}</p>
          <p>评分: {{ selectedEvaluation.score }}</p>
          <p>评论: {{ selectedEvaluation.comment }}</p>
          <p>回答:</p>
          <p>问题一: {{ selectedEvaluation.evaluation_form_answer[0].result }}</p>
          <p>问题二: {{ selectedEvaluation.evaluation_form_answer[1].result }}</p>
          <p>问题三: {{ selectedEvaluation.evaluation_form_answer[2].result }}</p>
          <p>问题四: {{ selectedEvaluation.evaluation_form_answer[3].result }}</p>
          <p>问题五: {{ selectedEvaluation.evaluation_form_answer[4].result }}</p>
          <p>问题六: {{ selectedEvaluation.evaluation_form_answer[5].result }}</p>
      </div>
  </el-drawer>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import BaseHeader from '@/layouts/BaseHeader.vue';
import { useRoute, useRouter } from 'vue-router';
import { CourseStatus, EvaluationType, getCourseCall, Publication, type CourseEntity } from '@/api/course/CourseAPI';
import { getEvaluationMetadataCall, getEvaluationsCall, type CourseEvaluationEntity } from '@/api/course/CourseEvaluationAPI';


const router = useRouter();
const course = ref<CourseEntity>(
  {
    course_id: 1, course_name: 'CS303', description: 'xxx', teacher_id: 1, created_at: new Date(), updated_at: new Date(),
    status: CourseStatus.published,
    publication: Publication.open,
    evaluation_type: EvaluationType.theory
  }
);

let currentPage = 1;
const pageSize = 10;

const handleCurrentChange = (newPage:number) =>{
  currentPage = newPage;
  fetchCourses();
}

const course_score = ref(4);

const evaluation = ref<CourseEvaluationEntity[]>([
  {
    course_id: course.value.course_id,
    student_id: 1,
    comment: 'pretty good',
    score: 4,
    evaluation_form_answer: [],
    created_at: new Date(),
    updated_at: new Date()
  }
]);

let course_id = 0;
const selectedEvaluation = ref<CourseEvaluationEntity>({
  course_id: 0,
  student_id: 0,
  comment: '',
  score: 0,
  evaluation_form_answer: [],
  created_at: new Date(),
  updated_at: new Date()
});

const isSidebarVisible = ref(false);

onMounted(async () => {
  const route = useRoute(); // 使用 useRoute 钩子获取当前路由对象
  const courseId = Number(route.query.course_id); // 从查询参数中获取 course_id 并转换为数字
  course_id = Number(courseId);
  await fetchCourses();
});

const fetchCourses = async () => {
  try {
    const response = await getCourseCall(course_id);
    course.value = response.data;
    const response_evaluation = await getEvaluationsCall(course_id,pageSize,currentPage);
    evaluation.value = response_evaluation.data.content;
    const evaluationResponse = await getEvaluationMetadataCall(course_id);
    course_score.value = evaluationResponse.data.average_score;
  } catch (error) {
    console.error('获取课程列表失败:', error);
  }
};

const viewEvaluation = (review: CourseEvaluationEntity) => {
  selectedEvaluation.value = review;
  isSidebarVisible.value = true;
};
</script>


<style scoped>
.course-evaluation {
    margin-top: 20px;
    margin-left: 50px;
    display: flex;
    flex-direction: column;
    align-items: center; /* 添加此行代码使内容居中 */
}

.course-average-score {
    text-align: center; /* 使文本居中 */
    margin-bottom: 20px; /* 添加一些底部间距 */
}

.course-review {
    border: 1px solid #ebeef5; /* 灰色边框 */
    padding: 15px; /* 内边距 */
    margin-bottom: 10px; /* 评价之间的间距 */
    border-radius: 4px; /* 边框圆角 */
    background-color: #fff; /* 背景色 */
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); /* 阴影效果 */
    width: 100%; /* 使评价框宽度适应父容器 */
    max-width: 600px; /* 设置一个最大宽度，防止评价框过长 */
}

.el-rate {
    margin-bottom: 10px; /* 评分和评论之间的间距 */
}

/* 可以添加一个 hover 效果，当鼠标悬停在评价框上时有视觉效果 */
.course-review:hover {
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
    transition: box-shadow 0.3s ease-in-out;
}
</style>
<template>
    <el-header style="padding: 0; height: auto;">
        <base-header />
    </el-header>
    <div class="course-evaluation">
        <el-rate v-model="course_score" disabled></el-rate>
            <p>课程评分: {{ course_score }} 分</p>
        <div v-for="review in evaluation" :key="review.student_id" class="course-review">
            <el-rate v-model="review.score" disabled></el-rate>
            <p>学生评价：{{ review.comment }}</p>
            <el-button>查看评价</el-button>
        </div>
    </div>
</template>

<script setup lang="ts">
import { onMounted, ref,watch } from 'vue';
import BaseHeader from '@/layouts/BaseHeader.vue';
import { useRoute, useRouter } from 'vue-router';
import { CourseStatus, EvaluationType ,getCourseCall, Publication, type CourseEntity } from '@/api/course/CourseAPI';
import { getEvaluationMetadataCall, getEvaluationsCall, type CourseEvaluationEntity } from '@/api/course/CourseEvaluationAPI';

const router = useRouter();
let showJoinButton = ref(false); 
const course = ref<CourseEntity>(
  {
    course_id: 1, course_name: 'CS303', description: 'xxx', teacher_id: 1, created_at: new Date(), updated_at: new Date(),
    status: CourseStatus.published,
    publication: Publication.open,
    evaluation_type: EvaluationType.theory
  }
);

const course_score = ref(4);

const evaluation = ref<CourseEvaluationEntity[]>([
  {
    course_id: course.value.course_id,
    student_id: 1,
    comment: 'pretty good',
    score: 4,
    evaluation_form_answer: '',
    created_at: new Date(),
    updated_at: new Date()
  }
]);

let course_id = 0;

onMounted(async () => {
  const route = useRoute(); // 使用 useRoute 钩子获取当前路由对象
  const courseId = Number(route.query.course_id); // 从查询参数中获取 course_id 并转换为数字
  course_id = Number(courseId);
  await fetchCourses();
});

const activeMenu = ref('1');


const fetchCourses = async () => {
  try {
    const response = await getCourseCall(course_id);
    course.value = response.data;
    const response_evaluation = await getEvaluationsCall(course_id,10,1);
    evaluation.value = response_evaluation.data.content;
    const evaluationResponse = await getEvaluationMetadataCall(course_id);
    course_score.value = evaluationResponse.data.average_score;
  } catch (error) {
    console.error('获取课程列表失败:', error);
  }
};

</script>


<style scoped>
.course-evaluation {
    margin-top: 20px;
    margin-left: 50px;
}
</style>
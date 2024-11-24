<template>
  <div id="app">
    <el-header style="padding: 0; height: auto;">
        <base-header />
    </el-header>
    <el-container>
      <el-header>
        <el-button type="primary" class="back-button" @click="routerBack">返回</el-button>
        <el-button type="primary" class="join-button">申请加入</el-button>
      </el-header>

      <el-container>
        <el-aside width="200px">
          <el-menu @select="handleMenuSelect">
            <el-menu-item index="1">课程详情</el-menu-item>
            <el-menu-item index="2">课程评价</el-menu-item>
          </el-menu>
        </el-aside>

        <el-main>
          <h2>{{course[0].course_name}}</h2>
          <div v-if="activeMenu === '1'" class="course-description">
            <p>课程简介：{{course[0].description}}</p>
            <p>授课教师：{{course[0].teacher_id}}</p>
          </div>
          <div v-if="activeMenu === '2'" class="course-evaluation">
          <el-rate v-model="evaluation.average_score" disabled></el-rate>
            <p>课程评分: {{ evaluation.average_score }} 分</p>
          <div v-for="review in evaluation.reviews" :key="review.id" class="course-review">
            <el-rate v-model="review.score" disabled></el-rate>
            <p>学生评价：{{ review.content }}</p>
          </div>
        </div>
        </el-main>
      </el-container>

      <el-footer>
        <div class="teacher-info">
          
        </div>
      </el-footer>
    </el-container>
  </div>
</template>
<script setup lang="ts">
import { onMounted, ref,watch } from 'vue';
import BaseHeader from '@/layouts/BaseHeader.vue';
import { useRouter } from 'vue-router';
import { CourseStatus, EvaluationType ,getCourseCall, Publication, type CourseEntity } from '@/api/course/CourseAPI';
import { getEvaluationCall } from '@/api/course/CourseEvaluationAPI';

const router = useRouter();
const activeIndex = ref('1');
const course = ref<CourseEntity[]>([
  {
    course_id: 1, course_name: 'CS303', description: 'xxx', teacher_id: 1, created_at: new Date(), updated_at: new Date(),
    status: CourseStatus.published,
    publication: Publication.open,
    evaluationType: EvaluationType.theory
  }
]);

const course_score = ref(0);

const evaluation = ref({
  average_score: course_score,
  reviews: [
    { id: 1, score: 5, content: 'Excellent course!' },
    { id: 2, score: 4, content: 'Very good, but could be better.' },
    { id: 3, score: 3, content: 'Average experience.' },
    { id: 4, score: 5, content: 'Highly recommend!' },
    { id: 5, score: 4, content: 'Good course, a bit too fast-paced.' }
  ]
});

let course_id = 0;

onMounted(async () => {
  const routeCourseId = router.currentRoute.value.params.course_id;
  if (routeCourseId) {
    course_id = Number(routeCourseId);
    await fetchCourses();
  }
});

const routerBack = () => {
  router.back();
};

const handleMenuSelect = (key: string) => {
  activeMenu.value = key;
};

const navigateTo = (path: string) => {
  router.push(path); 
};

const activeMenu = ref('1');

watch(activeMenu, (newVal) => {
  console.log(`Active menu changed to: ${newVal}`);
});

const fetchCourses = async () => {
  try {
    const response = await getCourseCall(course_id);
    course.value = [response.data];
    const evaluationResponse = await getEvaluationCall(course_id);
    course_score.value = evaluationResponse.data.score;
  } catch (error) {
    console.error('获取课程列表失败:', error);
  }
};

</script>

<style>

.back-button {
  position: absolute;
  left: 20px;
  top: 60px;
}

.join-button {
  position: absolute;
  right: 20px;
  top: 60px;
}

.course-info, .course-evaluation {
  margin-top: 20px;
}

.course-description, .course-evaluation {
  margin-top: 20px;
}

.teacher-info {
  margin-top: 40px;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #0f151b;
  margin-top: 0px;
}

.main{
  margin-top: auto;
}

.el-menu-demo {
  background-color: #eaebec;
  color: #857c7c;
}

.el-menu-demo .el-menu-item {
  color: #fff;
}

.el-menu-demo .el-menu-item.is-active {
  background-color: #ffd04b;
}

.box-card {
  margin: 10px;
}

.search-box {
  display: flex;
  align-items: center;
  margin-left: 10px;
  margin-right: 200px;
  margin-top: 10px;
}

.container {
  margin-top: 10px;
  margin-left: 10px;
  margin-right: auto;
}

.search-input {
  margin-right: 10px;
  width: 200px; /* 调整搜索框宽度 */
}

.search-button {
  margin-left: 0; /* 调整按钮样式 */
}

.course-review {
  margin-top: 10px;
  padding: 10px;
  border: 1px solid #eaeaea;
  border-radius: 5px;
}

</style>
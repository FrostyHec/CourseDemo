<template>
  <div id="app">
    <el-header style="padding: 0; height: auto;">
        <base-header />
    </el-header>
    <el-container>
      <el-header>
        <el-button type="primary" class="back-button" @click="routerBack">返回</el-button>
        <el-button type="primary" class="join-button" v-show="showJoinButton" @click="joinClass">申请加入</el-button>
      </el-header>

      <el-container>
        <el-aside width="200px">
          <el-menu @select="handleMenuSelect">
            <el-menu-item index="1">课程详情</el-menu-item>
            <el-menu-item index="2">课程评价</el-menu-item>
          </el-menu>
        </el-aside>

        <el-main>
          <h2>{{course.course_name}}</h2>
          <div v-if="activeMenu === '1'" class="course-description">
            <p>课程简介：{{course.description}}</p>
            <p>授课教师：{{course.teacher_id}}</p>
          </div>
          <div v-if="activeMenu === '2'" class="course-evaluation">
          <el-rate v-model="course_score" disabled></el-rate>
            <p>课程评分: {{ course_score }} 分</p>
          <div v-for="review in evaluation" :key="review.student_id" class="course-review">
            <el-rate v-model="review.score" disabled></el-rate>
            <p>学生评价：{{ review.comment }}</p>
          </div>
        </div>
        </el-main>
      </el-container>

      <el-dialog
          title="加入课程"
          v-model="joinDialogVisible"
        >
          <span>确定要加入这个课程吗？</span>
          <template #footer>
            <el-button @click="joinDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleJoin(); joinDialogVisible=false;">确认加入</el-button>
          </template>
        </el-dialog>

    </el-container>
  </div>
</template>
<script setup lang="ts">
import { onMounted, ref,watch } from 'vue';
import BaseHeader from '@/layouts/BaseHeader.vue';
import { useRoute, useRouter } from 'vue-router';
import { CourseStatus, EvaluationType ,getCourseCall, Publication, type CourseEntity } from '@/api/course/CourseAPI';
import { getEvaluationMetadataCall, type CourseEvaluationEntity } from '@/api/course/CourseEvaluationAPI';
import { getAllJoinedCourseList, studentEnrollCourseCall } from '@/api/course/CourseMemberAPI';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const joinDialogVisible = ref(false);
const showJoinButton = ref(false); 
const activeIndex = ref('1');
const authStore = useAuthStore();
const course = ref<CourseEntity>(
  {
    course_id: 1, course_name: 'CS303', description: 'xxx', teacher_id: 1, created_at: new Date(), updated_at: new Date(),
    status: CourseStatus.published,
    publication: Publication.open,
    evaluation_type: EvaluationType.theory
  }
);

const course_score = ref(4);

const checkJoin = async () =>{
  const response = await getAllJoinedCourseList(authStore.user.user_id,1,100);
  const courseId = course.value.course_id;
  const isJoined = response.data.content.some((course: { course_id: number; }) => course.course_id === courseId);
  if(!isJoined){
    showJoinButton.value = true;
  }
}

const joinClass = () =>{
  joinDialogVisible.value = true;
}

const handleJoin = async () => {
  await studentEnrollCourseCall(course.value.course_id);
  fetchCourses();
};

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
  console.log(courseId); // 打印 course_id 的值
  course_id = Number(courseId);
  await fetchCourses();
  checkJoin();
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
    course.value = response.data;
    const evaluationResponse = await getEvaluationMetadataCall(course_id);
    course_score.value = evaluationResponse.data.average_score;
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
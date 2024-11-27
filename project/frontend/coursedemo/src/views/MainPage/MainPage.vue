<template>
  <div id="app">
    <el-header style="padding: 0; height: auto;">
      <base-header />
    </el-header>
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal">
      <el-menu-item index="1">全部课程</el-menu-item>
      <el-menu-item index="2" @click="navigateTo('/MainPage/student')">我的课程</el-menu-item>
      <el-menu-item index="3" @click="navigateTo('/MainPage/learningScore')">学习积分</el-menu-item>
    </el-menu>
    <div class="search-box">
      <el-input
        placeholder="搜索课程..."
        v-model="searchQuery"
        class="search-input"
        @keyup.enter="handleSearch"
        suffix-icon="el-icon-search"
      ></el-input>
      <el-button type="primary" class="search-button" @click="handleSearch">搜索</el-button>
    </div>  
    <el-container class="container">
      <el-aside width="200px">
        <el-menu>
          <el-menu-item index="1-1" @click="activeIndex2 = 'course';getHotCourses()">热门课程</el-menu-item>
          <el-menu-item index="1-2" @click="activeIndex2 = 'teacher';getHotTeachers()">热门教师</el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="main">
        <!-- 热门课程列表 -->
        <div v-if="activeIndex2 === 'course'">
          <el-card class="box-card" v-for="repo in repositories" :key="repo.course.course_id">
            <div class="clearfix">
              <span>{{ repo.course.course_name }}</span>
            </div>
            <div>
              <p>Description: {{ repo.course.description }}</p>
              <p>Teacher: {{ repo.course.teacher_id }}</p>
            </div>
            <el-button style="float: none;" type="primary" @click="enterCourse(repo.course.course_id)">查看课程信息</el-button>
          </el-card>
       </div> 
        <!-- 热门教师列表 -->
        <div v-if="activeIndex2 === 'teacher'">
          <el-card class="box-card" v-for="(teacher, index) in hotTeachers" :key="teacher.teacher.user_id">
            <div class="clearfix">
              <span style="float: left;">名次：{{ index + 1 }}</span>
              <span>{{ teacher.teacher.first_name }} {{ teacher.teacher.last_name }}</span>
            </div>
            <div>
              <p>Student Number: {{ teacher.student_num }}</p>
              <p>Email: {{ teacher.teacher.email }}</p>
            </div>
          </el-card>
        </div>
      </el-main>
      <el-dialog
          title="退出登录"
          v-model="quitVisible"
          @close="router.push('/login')"
        >
          <span>另一个用户登录，您将被登出</span>
          <template #footer>
            <el-button type="primary" @click="router.push('/login')">确认</el-button>
          </template>
        </el-dialog>
    </el-container>
    <div class="messages" v-if="announcementMessages.length > 0">
      <div v-for="(msg, index) in announcementMessages" :key="index">
        {{ msg }}
      </div>
    </div>
    <llm />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import BaseHeader from '@/layouts/BaseHeader.vue';
import llm from '@/views/Course/llm.vue'
import { useRouter } from 'vue-router';
import { CourseStatus, EvaluationType, Publication, type CourseEntity } from '@/api/course/CourseAPI';
import { getHotCoursesCall, getHotTeachersCall, type CourseWithStudentCount, type TeacherWithStudentCount } from '@/api/course/HotCourseAPI';
import { UserType, type UserPublicInfoEntity } from '@/api/user/UserAPI';
import { getAnnouncementMessages } from '@/api/sse/SSEEventHandle';
import { useEventStore } from '@/stores/event';
import { EventType } from '@/utils/EventBus';
import type { SSEBody } from '@/api/sse/SSEHandler';

const router = useRouter();
const activeIndex = ref('1');
const activeIndex2 = ref('course');
const searchQuery = ref('');
const quitVisible = ref(false);

onMounted(async () => {
    getHotCourses();
});

const cs303 = ref<CourseEntity>({
  course_id: 1, course_name: 'CS303', description: 'xxx', teacher_id: 1, created_at: new Date(), updated_at: new Date(),
  status: CourseStatus.published,
  publication: Publication.open,
  evaluation_type: EvaluationType.theory
});

const teacher1 = ref<UserPublicInfoEntity>({
  user_id: 1,
  first_name: 'Zhang',
  last_name: 'San',
  role: UserType.TEACHER,
  email: ''
})

const repositories = ref<CourseWithStudentCount[]>([
  {
    course: cs303.value,
    studentNum: 100
  }
]);
let hotTeachers = ref<TeacherWithStudentCount[]>([
  {
    teacher: teacher1.value,
    student_num: 0
  }
])

const pageSize = 10;
const pageNum = 1;

const getHotCourses = async () => {
  try {
    const response = await getHotCoursesCall(pageSize,pageNum);
    repositories.value = response.data.content;
  } catch (error) {
    console.error('获取热门课程失败:', error);
  }
};

const getHotTeachers = async () => {
  try {
    const response = await getHotTeachersCall(pageSize,pageNum);
    hotTeachers.value = response.data.content;
  } catch (error) {
    console.error('获取热门教师失败:', error);
    return [];
  }
};

const enterCourse = (course_id: number) => {
  router.push({ path: '/MainPage/courseInfo', query: { course_id: course_id } });
};

const handleSearch = () => {
  router.push({ path: '/MainPage/searchCourse', query: { search: searchQuery.value } });
};

const navigateTo = (path: string) => {
  router.push(path);
};

const announcementMessages = ref<string[]>([
  'this is an example','this is an example','this is an example'
]);

const {registerEvent} = useEventStore();

registerEvent(EventType.quitEvent,(message: { body: SSEBody; })=>{
  handleQuit()
})
function handleQuit() {
  quitVisible.value = true;
}

watch(() => getAnnouncementMessages(), (newMessages) => {
  announcementMessages.value = newMessages;
  console.log(announcementMessages.value);
}, { immediate: true });

;
</script>

<style>
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
.messages {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1000;
}

.messages div {
  margin-bottom: 10px;
  padding: 10px;
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  border-radius: 5px;
}
</style>
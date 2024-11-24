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
          <el-menu-item index="1-1" @click="showHotCourses">热门课程</el-menu-item>
          <el-menu-item index="1-2" @click="showHotTeachers">热门教师</el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="main">
        <!-- 热门课程列表 -->
        <el-card class="box-card" v-for="repo in repositories" :key="repo.course.course_id" v-show="showCourses">
          <div class="clearfix">
            <span>{{ repo.course.course_name }}</span>
          </div>
          <div>
            <p>Description: {{ repo.course.description }}</p>
            <p>Teacher: {{ repo.course.teacher_id }}</p>
          </div>
          <el-button style="float: none;" type="primary" @click="enterCourse(repo.course.course_id)">查看课程信息</el-button>
        </el-card>
        <!-- 热门教师列表 -->
        <el-card class="box-card" v-for="teacher in hotTeachers" :key="teacher.teacher.user_id" v-show="showTeachers">
          <div class="clearfix">
            <span>{{ teacher.teacher.first_name }} {{ teacher.teacher.last_name }}</span>
          </div>
          <div>
            <p>Role: {{ teacher.teacher.role }}</p>
            <p>Email: {{ teacher.teacher.email }}</p>
          </div>
        </el-card>
      </el-main>
    </el-container>
    <div class="messages" v-if="announcementMessages.length > 0">
      <div v-for="(msg, index) in announcementMessages" :key="index">
        {{ msg }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref, watch } from 'vue';
import BaseHeader from '@/layouts/BaseHeader.vue';
import { useRouter } from 'vue-router';
import { CourseStatus, EvaluationType, Publication, type CourseEntity } from '@/api/course/CourseAPI';
import { getHotCoursesCall, getHotTeachersCall, type CourseWithStudentCount, type TeacherWithStudentCount } from '@/api/course/HotCourseAPI';
import { getUserPublicInfoCall, UserType, type UserPublicInfoEntity } from '@/api/user/UserAPI';
import { getAnnouncementMessages, SSEBodyType, SSEMessageType, subscribeToSSE, unSubscribeSSE, type AnnouncementBody, type EventHandler, type MessagePacket, type NewLoginBody, type ReceiveCreditsBody, type SSEBody } from '@/api/sse/SSEHandler';

const router = useRouter();
const activeIndex = ref('1');

// 组件挂载时注册 SSE
onMounted(() => {
  subscribeToSSE();
  getHotCourses
});

// 组件卸载时取消注册 SSE
onUnmounted(() => {
  if (announcementTimer) {
    clearTimeout(announcementTimer);
  }
  unSubscribeSSE();
});


const cs303 = ref<CourseEntity>({
  course_id: 1, course_name: 'CS303', description: 'xxx', teacher_id: 1, created_at: new Date(), updated_at: new Date(),
  status: CourseStatus.published,
  publication: Publication.open,
  evaluationType: EvaluationType.theory
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
const hotTeachers = ref<TeacherWithStudentCount[]>([
  {
    teacher: teacher1.value,
    studentNum: 0
  }
])

const searchQuery = ref('');
let showCourses = true;
let showTeachers = false;

const getTeacherName = async (teacher_id: number) => {
  try {
    const result = await getUserPublicInfoCall(teacher_id); 
    return `${result.data.first_name} ${result.data.last_name}`;
  } catch (error) {
    console.error('获取老师名字失败:', error);
    return '';
  }
};

const pageSize = 10;
const pageNum = 1;

const getHotCourses = async () => {
  try {
    const response = await getHotCoursesCall(pageNum, pageSize);
    repositories.value = response.data.content;
    showCourses = true;
    showTeachers = false;
  } catch (error) {
    console.error('获取热门课程失败:', error);
  }
};

const getHotTeachers = async () => {
  try {
    const response = await getHotTeachersCall(1, 10);
    const hotTeachers = response.data.content.map(teacher => ({
      teacher,
      studentNum: teacher.studentNum
    }));
    showCourses = false;
    showTeachers = true;
    return hotTeachers;
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

const showHotCourses = () => {
  getHotCourses();
  showCourses = true;
  showTeachers = false;
};

const showHotTeachers = () => {
  getHotTeachers();
  showCourses = false;
  showTeachers = true;
};

const announcementMessages = ref<string[]>([
  'this is an example','this is an example','this is an example'
]);

watch(() => getAnnouncementMessages(), (newMessages) => {
  announcementMessages.value = newMessages;
}, { immediate: true });

let announcementTimer: number | null = null;

// 如果有新的公告消息，显示通知
if (announcementMessages.value.length > 0) {
  const combinedMessage = announcementMessages.value.join(',');
  alert(combinedMessage); // 使用 alert 或其他方式显示通知
  // 清空公告信息数组以便下一次通知
  announcementMessages.value = [];
}
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
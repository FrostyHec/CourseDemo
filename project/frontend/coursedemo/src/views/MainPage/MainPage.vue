<template>
  <div id="app">
      <el-header style="padding: 0; height: auto;">
        <base-header />
      </el-header>
      <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal">
        <el-menu-item index="1">全部课程</el-menu-item>
        <el-menu-item index="2" @click="navigateTo('/MainPage/student')">我的课程</el-menu-item>
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
          <el-menu-item index="1-1" @click="getHotCourses">热门课程</el-menu-item>
          <el-menu-item index="1-2" @click="getHotTeachers">热门教师</el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="main">
        <el-card class="box-card" v-for="repo in repositories" :key="repo.course.course_id">
          <div class="clearfix">
            <span>{{ repo.course.course_name }}</span>
          </div>
          <div>
            <p>Description: {{ repo.course.description }}</p>
            <p>Teacher: {{ getTeacherName(repo.course.teacher_id) }}</p>
          </div>
          <el-button style="float: none;" type="primary" @click="enterCourse(repo.course.course_id)">查看课程信息</el-button>
        </el-card>
      </el-main>
      </el-container>
  </div>
</template>
<script setup lang="ts">
import { ref } from 'vue';
import BaseHeader from '@/layouts/BaseHeader.vue';
import { useRouter } from 'vue-router';
import { CourseStatus, EvaluationType, Publication, type CourseEntity } from '@/api/course/CourseAPI';
import { getHotCoursesCall, getHotTeachersCall, type CourseWithStudentCount } from '@/api/course/HotCourseAPI';
import { getUserPublicInfoCall } from '@/api/user/UserAPI';

const router = useRouter();
const activeIndex = ref('1');

const cs303 = ref<CourseEntity>(
  {
    course_id: 1, course_name: 'CS303', description: 'xxx', teacher_id: 1, created_at: new Date(), updated_at: new Date(),
    status: CourseStatus.published,
    publication: Publication.open,
    evaluationType: EvaluationType.theory
  }
)

const repositories = ref<CourseWithStudentCount[]>([
  {
    course: cs303.value,
    studentNum: 100
  }
]);

const searchQuery = ref('');

const getTeacherName = async (teacher_id: number) => {
  try {
    const result = await getUserPublicInfoCall(teacher_id); 
    const name = `${result.data.first_name} ${result.data.last_name}`; 
    return name;
  } catch (error) {
    console.error('获取老师名字失败:', error);
    return '';
  }
};

const pageSize = 10;
const pageNum = 1

const getHotCourses = async () => {
  try {
    const response = await getHotCoursesCall(pageNum,pageSize);
    repositories.value = response.data.map(course => ({
      teacherName: '', // 初始化老师名字为空
    }));
    await Promise.all(repositories.value.map(async (repo) => {
      repo.teacherName = await getTeacherName(repo.course.teacher_id); // 异步获取老师名字
    }));
  } catch (error) {
    console.error('获取热门课程失败:', error);
  } 
};

const getHotTeachers = async () =>{
  const response = await getHotTeachersCall(1,10);
}

const enterCourse = (course_id:number) => {
  router.push({ path: '/MainPage/courseInfo', query: { course_id: course_id } });
};

const handleSearch = () => {
  router.push({ path: '/MainPage/searchCourse', query: { search: searchQuery.value } });
};
const navigateTo = (path: string) => {
  router.push(path); 
};

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
</style>
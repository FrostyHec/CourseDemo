<template>
    <div id="app">
        <el-header style="padding: 0; height: auto;">
          <base-header />
        </el-header>
        <div class="search-box">
          <el-input
            placeholder="搜索课程..."
            v-model="searchQuery"
            class="search-input"
            @keyup.enter="handleSearch"
            suffix-icon="el-icon-search"
          ></el-input>
          <el-button type="primary" class="search-button" @click="handleSearch">搜索</el-button>
          <el-button class="return-button" @click="navigateTo('/MainPage')">返回主页</el-button>
        </div>  
        <el-main class="main">
          <el-card class="box-card" v-for="repo in repositories" :key="repo.course_id">
            <div class="clearfix">
              <span>{{ repo.course_name }}</span>
            </div>
            <div>
              <p>Description: {{ repo.description }}</p>
              <p>Teacher: {{ repo.teacher_id }}</p>
            </div>
            <el-button style="float: none;" type="primary" @click="enterCourse(repo.course_id)">查看课程信息</el-button>
          </el-card>
        </el-main>
    </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import BaseHeader from '@/layouts/BaseHeader.vue';
import { useRouter } from 'vue-router'; 
import { CourseStatus, Publication, searchCourseCall, type CourseEntity } from '@/api/course/CourseAPI';

const router = useRouter(); 
const searchQuery = ref<string | undefined>(undefined);
const repositories = ref<CourseEntity[]>([]);

const pageSize = 1;
const pageNum = 10;


const handleSearch = () => {
  searchCourseCall(pageSize, pageNum, searchQuery.value).then(response => {
    repositories.value = response.data.content;
  });
};

const enterCourse = (course_id:number) => {
  router.push({ path: '/MainPage/courseInfo', query: { course_id: course_id } });
};

const navigateTo = (path: string) => {
  router.push(path);
};

const fetchCourses = async () => {
  try {
    const response = await searchCourseCall(pageSize, pageNum, searchQuery.value);
    repositories.value = response.data.content;
  } catch (error) {
    console.error('获取课程列表失败:', error);
  }
};

onMounted(async () => {
  searchQuery.value = router.currentRoute.value.query.search ? String(router.currentRoute.value.query.search) : undefined;
  await fetchCourses();
});

watch(searchQuery, async (newVal) => {
  await fetchCourses();
}, { immediate: true });

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


.search-input {
margin-right: 10px;
margin-left: 10px;
width: 200px; /* 调整搜索框宽度 */
}

.search-button {
margin-left: 0;
}

.return-button{
margin-right: auto;
}
</style>
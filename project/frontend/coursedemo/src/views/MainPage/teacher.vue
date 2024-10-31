<template>
  <el-config-provider namespace="ep">
    <el-container style="height: 100vh;">
      <el-header style="padding: 0; height: auto;">
        <base-header />
      </el-header>
      <el-main>
        <el-header class="app-header">
          <div class="topbar">
            <el-row>
              <el-col>负责课程列表</el-col>
            </el-row>
          </div>
        </el-header>
    
        <el-container>
          <el-main>
            <el-table :data="tableData" style="width: 100%">
              <el-table-column prop="course_name" label="课程名称">
                <template v-slot="{ row }">
                    <router-link :to="`/course/${row.course_id}`" class="course-link">{{ row.course_name }}</router-link>
                  </template>
              </el-table-column>
              <el-table-column prop="action" label="操作" width="200">
                <template v-slot="{ row }">
                  <el-button @click="deleteCourse(row)">删除课程</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-main>
        </el-container>
        <el-button style="margin-left: 90%; margin-top: 10px" type="primary" @click="createNewCourse">创建课程</el-button>
      </el-main>
    </el-container>
    
    <el-dialog
      v-model="dialogVisible"
      title="添加课程"
      width="40%"
    >
      <el-form
        ref="courseForm"
        :model-value="courseForm"
        :rules="rules"
        label-width="auto"
        label-position="right"
        size="default"
      >
        <el-form-item label="课程名称" prop="course_name">
          <el-input v-model="courseForm.course_name"/>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="courseForm.description"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="AddCourse('courseForm')">创建</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </el-config-provider>
</template>

<script>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth'
import BaseHeader from '@/layouts/BaseHeader.vue';
import { getAllTeachingCourseList } from '@/api/course/CourseMemberAPI';
export default {
  name: 'CourseApproval',
  components: {
    BaseHeader
  },
  setup() {
    const store = useAuthStore()
    const tableData = ref([
      {course_id:1,course_name:'CS303',description:'xxx',teacher_id:1}
    ]);
    onMounted(async () => {
      try {
        const response = await getAllTeachingCourseList(store.user.user_id, 1, 10);
        const courses = response.content; 
        tableData.value = [...tableData.value,...courses];
      } catch (error) {
        console.error('获取课程列表失败:', error);
      }
    });
    const courseForm = ref({
      course_id: '',
      course_name: '',
      description: '',
      teacher_id: '',
      status: '',
      publication: '',
      created_at: Date,
      updated_at: Date,
    });

    const dialogVisible = ref(false);
    const rules = ref({});

    const createNewCourse = () => {
      courseForm.value = {course_id:'',course_name:'', description:''};
      dialogVisible.value = true;
    };

    const AddCourse = (formName) => {
      tableData.value.push({
        course_id: '',
        course_name: courseForm.value.course_name,
        description: '',
        teacher_id: 1,
        status: '',
        publication: '',
        created_at: Date,
        updated_at: Date,
      });
      dialogVisible.value = false;
    };

    const deleteCourse = (row) => {
      const index = tableData.value.indexOf(row);
      if (index !== -1) {
        tableData.value.splice(index, 1);
      }
    };

    return {
      tableData,
      courseForm,
      dialogVisible,
      rules,
      createNewCourse,
      AddCourse,
      deleteCourse,
    };
  },
};
</script>

<style scoped>
.app-header {
  background-color: #b3c0d1;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px; /* 内边距 */
}

.el-button {
  margin-left: 90%; /* 调整按钮位置 */
  margin-top: 10px;
}
</style>
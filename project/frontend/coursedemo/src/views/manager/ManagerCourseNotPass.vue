<template>
  <el-config-provider namespace="ep">
    <el-container style="height: 100vh;">
      <el-header style="padding: 0%; height: auto;">
        <base-header />
      </el-header>
      <el-main>
        <el-header class="app-header">
          <div class="topbar">
            <el-row>
              <el-col :span="24">课程审批</el-col>
            </el-row>
          </div>
        </el-header>
        <el-container>
          <el-aside width="200px" class="app-aside">
            <el-menu default-active="1">
              <el-menu-item index="1"><router-link to="/manager/NotPass">待审批</router-link></el-menu-item>
              <el-menu-item index="2"><router-link to="/manager/Passed">已通过</router-link></el-menu-item>
            </el-menu>
          </el-aside>
          <el-main>
            <el-table :data="courses" style="width: 100%">
              <el-table-column prop="CourseName" label="课程名称"></el-table-column>
              <el-table-column prop="teacher" label="授课老师"></el-table-column>
              <el-table-column prop="status" label="状态" width="180"></el-table-column>
              <el-table-column prop="action" label="操作" width="200">
                <template v-slot="{ row }">
                  <el-button @click="approveCourse(row)">通过</el-button>
                  <el-button @click="rejectCourse(row)">不通过</el-button>
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
        <el-form-item label="课程名称" prop="CourseName">
          <el-input v-model="courseForm.CourseName"/>
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
import BaseHeader from '../../layouts/BaseHeader.vue';

export default {
  name: 'CourseApproval',
  components: {
    BaseHeader
  },
  setup() {
    const courses = ref([
      { id: "", status: '待审批', teacher: 'xxx', action: '', CourseName: 'CS303 Artificial Intelligence' },
      { id: "", status: '待审批', teacher: 'xxx', action: '', CourseName: 'CS309 OOAD' },
    ]);
    const courseForm = ref({
      id: "",
      CourseName: "",
      status: "",
      action: "",
    });
    const dialogVisible = ref(false);
    const rules = ref({});

    const approveCourse = (row) => {
      console.log('Approve course:', row);
    };

    const rejectCourse = (row) => {
      console.log('Reject course:', row);
    };

    const createNewCourse = () => {
      dialogVisible.value = true;
    };

    const checkDuplicate = () => {
      const { CourseName } = courseForm.value;
      const isDuplicate = courses.value.some(course => {
        return course.CourseName === CourseName;
      });
      return isDuplicate;
    };

    const AddCourse = (formName) => {
      const form = courseForm.value;
      if (checkDuplicate()) {
        alert('Duplicate value!');
      } else {
        courses.value.push({
          id: "",
          CourseName: form.CourseName,
          status: "待审批",
          action: ""
        });
        alert('submit!');
      }
      dialogVisible.value = false;
    };

    return {
      courses,
      courseForm,
      dialogVisible,
      rules,
      approveCourse,
      rejectCourse,
      createNewCourse,
      AddCourse,
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
}

.user-info {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}

.el-dropdown-link:hover {
  color: #333;
}
</style>
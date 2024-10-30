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
              <el-table-column prop="CourseName" label="课程名称"></el-table-column>
              <el-table-column prop="teacher" label="授课老师"></el-table-column>
              <el-table-column prop="total_students" label="已选人数"></el-table-column>
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
import BaseHeader from '@/layouts/BaseHeader.vue';

export default {
  name: 'CourseApproval',
  components: {
    BaseHeader
  },

  setup() {
    const tableData = ref([
      { id: "1", teacher: 'xxx', CourseName: 'CS303 Artificial Intelligence', total_students: 100, action: '' },
      { id: "2", teacher: 'yyy', CourseName: 'CS309 OOAD', total_students: 100, action: '' },
    ]);
    const courseForm = ref({
      CourseName: "",
      teacher: '',
      total_students: '',
    });
    const dialogVisible = ref(false);
    const rules = ref({});

    const createNewCourse = () => {
      courseForm.value = { CourseName: "", teacher: '', total_students: '' };
      dialogVisible.value = true;
    };

    const AddCourse = (formName) => {
      // Add validation logic here
      tableData.value.push({
        id: "",
        teacher: courseForm.value.teacher,
        CourseName: courseForm.value.CourseName,
        total_students: courseForm.value.total_students,
        action: ""
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
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
          <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="courseName" label="课程名称"></el-table-column>
            <el-table-column prop="status" label="状态" width="180"></el-table-column>
          </el-table>
        </el-main>
      </el-container>
      </el-main>
    </el-container>
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
    const tableData = ref([
      { status: '已通过', action: '', courseName: 'CS303 Artificial Intelligence' },
      { status: '已通过', action: '', courseName: 'CS309 OOAD' },
    ]);

    const approveCourse = (row) => {
      console.log('Approve course:', row);
    };

    const rejectCourse = (row) => {
      console.log('Reject course:', row);
    };

    return {
      tableData,
      approveCourse,
      rejectCourse,
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
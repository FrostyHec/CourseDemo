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
          <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal">
            <el-menu-item index="1" @click="navigateTo('/MainPage/teacher')">管理中的课程</el-menu-item>
            <el-menu-item index="2">课程状态</el-menu-item>
          </el-menu>
          <el-container>
            <el-main>
              <el-table :data="tableData" style="width: 100%">
                <el-table-column prop="course_name" label="课程名称"></el-table-column>
                <el-table-column prop="status" label="状态" width="200"></el-table-column>
                <el-table-column prop="action" label="操作" width="400">
                  <template v-slot="{ row }">
                    <el-button type="primary" @click="changeCourse(row)">编辑</el-button>
                    <el-button type="danger" @click="confirmDelete(row)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-pagination
                @current-change="handleCurrentChange"
                layout="prev, pager, next"
                :total="50">
              </el-pagination>
            </el-main>
          </el-container>
          <el-button style="margin-left: 90%; margin-top: 10px" type="primary" @click="createCourse">创建课程</el-button>
        </el-main>
  
        <!-- 删除确认对话框 -->
        <el-dialog
          title="删除课程"
          v-model="deleteDialogVisible"
        >
          <span>确定要删除这个课程吗？</span>
          <template #footer>
            <el-button @click="deleteDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleDelete(); deleteDialogVisible=false;">确认删除</el-button>
          </template>
        </el-dialog>
  
        <!-- 创建课程对话框 -->
        <el-dialog
          title="添加课程"
          v-model="dialogVisible"
          width="40%"
        >
          <el-form
            :model="courseForm"
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
            <el-form-item label="课程开放度" prop="publication">
              <el-radio-group v-model="courseForm.publication">
                <el-radio :label="Publication.open">开放</el-radio>
                <el-radio :label="Publication.closed">私密</el-radio>
                <el-radio :label="Publication.semi_open">半开放</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="课程类型" prop="evaluationType">
              <el-radio-group v-model="courseForm.evaluation_type">
                <el-radio :label="EvaluationType.practice">实践</el-radio>
                <el-radio :label="EvaluationType.theory">理论</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveCourse">保存</el-button>
              <el-button type="primary" @click="AddCourse">创建</el-button>
            </el-form-item>
          </el-form>
        </el-dialog>
      </el-container>
    </el-config-provider>
  </template>
  
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import BaseHeader from '@/layouts/BaseHeader.vue';
import { useAuthStore } from '@/stores/auth';
import { getAllTeachingCourseList } from '@/api/course/CourseMemberAPI';
import { CourseStatus, EvaluationType ,createCourseCall, deleteCourseCall, Publication, type CourseEntity, updateCourseInfoCall, updateCourseStatusCall } from '@/api/course/CourseAPI';
import router from '@/router';
import { ElMessage } from 'element-plus';

const authStore = useAuthStore();
const activeIndex = ref('2');
const tableData = ref<CourseEntity[]>([
{
  course_id: 1, course_name: 'CS303', description: 'xxx', teacher_id: 1, created_at: new Date(), updated_at: new Date(),
  status: CourseStatus.published,
  publication: Publication.open,
  evaluation_type: EvaluationType.practice
}
]);

let currentPage = 1;
const pageSize = 10;

const handleCurrentChange = (newPage:number) =>{
  currentPage = newPage;
  fetchCourses();
}
const dialogVisible = ref(false);
const deleteDialogVisible = ref(false);
const currentCourseToDelete = ref<CourseEntity | null>(null);

const courseForm = ref<CourseEntity>({
  course_id: 0,
  course_name: '',
  description: '',
  teacher_id: authStore.user.user_id,
  status: CourseStatus.creating,
  publication: Publication.open,
  created_at: new Date(),
  updated_at: new Date(),
  evaluation_type: EvaluationType.practice
});

onMounted(async () => {
    fetchCourses();
});

const navigateTo = (path: string) => {
    router.push(path); // 使用 router.push 进行路由跳转
};


const createCourse = () => {
  courseForm.value = {
        course_id: 0,
        course_name: '',
        description: '',
        teacher_id: authStore.user.user_id,
        status: CourseStatus.creating,
        publication: Publication.open,
        evaluation_type: EvaluationType.practice,
        created_at: new Date(),
        updated_at: new Date(),
  };
  dialogVisible.value = true;
};

const changeCourse = (row: CourseEntity) => {
  if(row.status==CourseStatus.creating||row.status==CourseStatus.rejected){
    dialogVisible.value = true;
    courseForm.value = row
  }
  else{
    ElMessage.error('此状态的课程无法编辑');
  }
};

const isCourseNameExist = (name: string) => {
  return tableData.value.some(course => course.course_name.toLowerCase() === name.toLowerCase());
};

const saveCourse = async () => {
    if (isCourseNameExist(courseForm.value.course_name)) {
        await updateCourseInfoCall(courseForm.value.course_id,courseForm.value);
        dialogVisible.value = false;
        fetchCourses(); 
        return;
    }
    dialogVisible.value = false;
    await createCourseCall(courseForm.value);
    tableData.value.push(courseForm.value);
    fetchCourses(); 
};

const AddCourse = async () => {
    if(courseForm.value.course_id!=0){
      await updateCourseStatusCall(courseForm.value.course_id,{status:CourseStatus.submitted});
      dialogVisible.value = false;
      return;
    }
    if (isCourseNameExist(courseForm.value.course_name)) {
      ElMessage.error('课程名称已存在');
      return;
    }
    courseForm.value.status = CourseStatus.submitted;
    await createCourseCall(courseForm.value);
    dialogVisible.value = false;
    fetchCourses(); 
};

const confirmDelete = (row: CourseEntity) => {
    currentCourseToDelete.value = row;
    deleteDialogVisible.value = true;
};

const handleDelete = () => {
  if (currentCourseToDelete.value) {
    if (currentCourseToDelete.value.status === CourseStatus.creating) {
      const index = tableData.value.indexOf(currentCourseToDelete.value);
      if (index !== -1) {
        tableData.value.splice(index, 1);
      }
      deleteDialogVisible.value = false;
    } else {
      const index = tableData.value.indexOf(currentCourseToDelete.value);
      deleteCourseCall(currentCourseToDelete.value.course_id).then(async (response: { code: number; }) => {
        if (response.code == 200) {
          tableData.value.splice(index, 1);
        } else {
          ElMessage.error('删除课程失败');
        }
      }).catch((error: any) => {
        ElMessage.error('删除课程失败');
        console.error('删除课程失败:', error);
      });
    }
  }
};

const fetchCourses = async () => {
  try {
      const response = await getAllTeachingCourseList(authStore.user.user_id, currentPage, pageSize);
      tableData.value = response.data.content;
  } catch (error) {
      console.error('获取课程列表失败:', error);
  }
};
</script>

<style scoped>
.el-header {
background-color: #f5f7fa;
border-bottom: 1px solid #e4e7ed;
}

.app-header {
background-color: #409eff;
color: white;
text-align: center;
line-height: 60px;
}

.topbar {
display: flex;
justify-content: space-between;
align-items: center;
padding: 0 20px;
}

.search-col {
display: flex;
justify-content: flex-end;
}

.button-row {
margin-top: 10px;
text-align: right;
background-color: #fff;
padding: 10px 20px;
box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.el-table {
margin-top: 20px;
}

.el-table-column {
text-align: center;
}

.el-main {
padding: 20px;
}

.el-button:hover {
background-color: #66b1ff;
border-color: #66b1ff;
}

.el-input__inner {
border-radius: 20px;
padding: 0 20px;
height: 40px;
}

.el-button--primary {
background-color: #67c23a;
border-color: #67c23a;
}

.el-button--primary:hover {
background-color: #85ce61;
border-color: #85ce61;
}

.el-table th {
background-color: #f0f9ff;
color: #333;
}

.course-link {
text-decoration: none;
color: #409eff;
font-weight: bold;
transition: color 0.3s;
}

.course-link:hover {
color: #66b1ff;
text-decoration: underline;
}
</style>
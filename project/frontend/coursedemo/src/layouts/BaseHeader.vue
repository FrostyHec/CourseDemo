<script lang="ts" setup>
import { useDark, useToggle } from "@vueuse/core";
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { onMounted, ref } from 'vue';
import { ArrowRight } from '@element-plus/icons-vue';
import { useAuthStore } from "@/stores/auth";
import { getUserPublicInfoCall, UserType, type UserPublicInfoEntity } from "@/api/user/UserAPI";

const authStore = useAuthStore()
const isDark = useDark();
const toggleDark = useToggle(isDark);
const router = useRouter(); // 使用 Vue Router

const userData = ref({
    user_id:authStore.user.user_id
})

const userInfo = ref<UserPublicInfoEntity>({
  user_id:0,
  first_name:'',
  last_name:'',
  role:UserType.STUDENT,
  email:'',
}); // 用于存储用户信息

// 获取用户信息
const getUserInfo = async () => {
  try {
    const result = await getUserPublicInfoCall(userData.value.user_id);
    userInfo.value = result.data;
  } catch (error) {
    console.error('获取用户信息失败:', error);
    ElMessage.error('获取用户信息失败');
  }
};

onMounted(getUserInfo); // 组件挂载时获取用户信息

const l = ref([
  { key: 0, label: 'hello', link: '/course/hello' },
  { key: 1, label: 'world' },
]);

function generate_breadcrumb(s: string[] | string | undefined): { key: number, label: string, link?: string }[] {
  if (!s) return [];
  if (typeof s === "string") s = [s];
  let prefix = '/course';
  let res = [];
  for (let i = 0; i < s.length; i++) {
    prefix += '/' + s[i];
    if (i === s.length - 1) {
      res.push({ key: i, label: s[i].replace(/-/g, ' ') });
    } else {
      res.push({ key: i, label: s[i].replace(/-/g, ' '), link: prefix });
    }
  }
  return res;
}

// 退出登录的方法
function logout() {
  authStore.logout(userData.value)
  // 显示退出登录的消息
  ElMessage({
    message: '您已成功退出登录',
    type: 'success',
  });
  // 跳转到登录页面
  router.push('/login');
}
</script>

<template>
  <el-page-header icon="" title=" " style="border-bottom: solid 1px var(--ep-border-color);" px="5" py="1">
    <template #title>
      <span style="font-weight: 600; font-size: large; margin: 0%;" @click="$router.push('/')">
        Course Demo
      </span>
    </template>

    <template #content>
      <el-breadcrumb :separator-icon="ArrowRight">
        <el-breadcrumb-item v-for="it in generate_breadcrumb($route.params.labels)" :key="it.key" :to="it.link">
          {{ it.label }}
        </el-breadcrumb-item>
      </el-breadcrumb>
    </template>

    <template #extra>
      <div style="width: 100%; display: flex; place-items: center">
        
        <el-popover :width="300">
          <template #reference>
            <el-avatar :size="36" style="border: solid 1px var(--ep-border-color); margin-right: 10px;"
              src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
          </template>
          <template #default>
            <div class="demo-rich-conent" style="display: flex; gap: 5px; flex-direction: column">
              <div style="display: flex; gap: 10px; align-items: center;">
                <el-avatar :size="64" style="border: solid 1px var(--ep-border-color); margin-left: 5px;"
                  src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
                <div>
                  <h2 style="margin: 0%;">{{ userInfo.first_name + ' ' + userInfo.last_name }}</h2>
                  <p style="margin: 0%; font-size: 14px; color: var(--el-color-info)">
                    {{ userInfo.email }}
                  </p>
                </div>
              </div>
              <el-button>Settings</el-button>
              <el-button @click="logout">Sign out</el-button>
            </div>
          </template>
        </el-popover>
        <span class="text-large font-600 mr-3">{{ userInfo.first_name + ' ' + userInfo.last_name }}</span>
        <el-button>
          Calendar
        </el-button>
        <el-button
          circle
          class="bg-transparent cursor-pointer"
          @click="toggleDark()"
        >
          <i inline-flex i="dark:ep-moon ep-sunny" />
        </el-button>
      </div>
    </template>
  </el-page-header>
</template>
<script setup lang='ts'>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useFormStore } from '@/stores/form';
import { UserType, type UserEntity } from '@/api/user/UserAPI'
import { createUserCall } from '@/api/user/UserAPI'
import { useRouter } from 'vue-router' 
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore()
// 控制注册与登录表单的显示， 默认显示注册
const isRegister = ref(false)

// 定义数据模型
const loginData = ref({
  email: '',
  password: ''
})

const registerData = ref({
  user_id:0,
  first_name:'',
  last_name:'',
  role:UserType.STUDENT,
  email:'',
  password: '',
  rePassword: ''
})

// 校验二次输入密码是否相同
const checkRePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次确认密码'))
  } else if (value !== registerData.value.password) {
    callback(new Error('二次确认密码不相同，请重新输入'))
  } else {
    callback()
  }
}

// 登录校验规则
const loginRule = ref({
  email: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
  ],
})

// 注册校验规则
const registerRule = ref({
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, max: 16, message: '请输入长度5~16非空字符', trigger: 'blur' }
  ],
  rePassword: [{ validator: checkRePassword, trigger: 'blur' }]
})

const form_store = useFormStore()
const router = useRouter() 

// 登录函数
const handleLogin = async () => {
  try {
    let result = await authStore.login(loginData.value);
    if (result.code == 200) {
      ElMessage.success('登录成功!')
      form_store.open_form(form_store.course_null, 'Add')
      if(authStore.user.role==UserType.STUDENT)router.push('/MainPage/student');
      else if(authStore.user.role==UserType.TEACHER)router.push('/MainPage/teacher')
      else router.push('/manager')
    } else {
      ElMessage.error('登录失败：' + result.code);
    }
  } catch (error) {
    ElMessage.error('服务异常');
  }
}

// 注册函数
const handleRegister = async () => {
  try {
    let result = await createUserCall({
      email: registerData.value.email,
      password: registerData.value.password,
      create_at: new Date(), update_at: new Date(),
      user_id: 0,
      first_name: registerData.value.first_name, last_name: registerData.value.last_name,
      role: registerData.value.role,
    })
    if (result.code === 200) {
      ElMessage.success('注册成功!')
      isRegister.value = false; // 注册成功后跳转到登录页
    } else {
      ElMessage.error('注册失败：' + result.msg);
    }
  } catch (error) {
    ElMessage.error('服务异常');
  }
}

// 定义函数，清空数据模型
const clearRegisterData = () => {
  registerData.value = {
    user_id:0,
    first_name:'',
    last_name:'',
    role:UserType.STUDENT,
    email:'',
    password: '',
    rePassword: ''
  }
}
</script>

<template>
  <el-row class="login-page">
    <el-col :span="12" class="bg"></el-col>
    <el-col :span="6" :offset="3" class="form">
      <!-- 注册表单 -->
      <el-form ref="registerForm" size="large" autocomplete="off" v-if="isRegister" :model="registerData" :rules="registerRule">
        <el-form-item>
          <h1>注册</h1>
        </el-form-item>
        <el-form-item prop="email">
          <el-input :prefix-icon="User" placeholder="请输入邮箱" v-model="registerData.email"></el-input>
        </el-form-item>
        <el-form-item prop="first_name">
          <el-input :prefix-icon="User" placeholder="first name" v-model="registerData.first_name"></el-input>
        </el-form-item>
        <el-form-item prop="last_name">
          <el-input :prefix-icon="User" placeholder="last name" v-model="registerData.last_name"></el-input>
        </el-form-item>
        <el-form-item prop="role">
          <el-radio-group :prefix-icon="User" placeholder="用户类型" v-model="registerData.role">
            <el-radio :label="UserType.STUDENT">学生</el-radio>
            <el-radio :label="UserType.TEACHER">老师</el-radio>
            <el-radio :label="UserType.ADMIN">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="password">
          <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码" v-model="registerData.password"></el-input>
        </el-form-item>
        <el-form-item prop="rePassword">
          <el-input :prefix-icon="Lock" type="password" placeholder="请再次输入密码" v-model="registerData.rePassword"></el-input>
        </el-form-item>
        <!-- 注册按钮 -->
        <el-form-item>
          <el-button class="button" type="primary" @click="handleRegister">注册</el-button>
        </el-form-item>
        <el-form-item class="flex">
          <el-link type="info" :underline="false" @click="isRegister = false; clearRegisterData()">
            ← 返回登录
          </el-link>
        </el-form-item>
      </el-form>
      <!-- 登录表单 -->
      <el-form ref="loginForm" size="large" autocomplete="off" v-else :model="loginData" :rules="loginRule">
        <el-form-item>
          <h1>登录</h1>
        </el-form-item>
        <el-form-item>
          <el-input :prefix-icon="User" placeholder="请输入邮箱" v-model="loginData.email"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码" v-model="loginData.password"></el-input>
        </el-form-item>
        <el-form-item class="flex">
          <div class="flex">
            <el-checkbox>记住我</el-checkbox>
            <el-link type="primary" :underline="false">忘记密码？</el-link>
          </div>
        </el-form-item>
        <!-- 登录按钮 -->
        <el-form-item>
          <el-button class="button" type="primary" native-type="submit" @click="handleLogin">登录</el-button>
        </el-form-item>
        <el-form-item class="flex">
          <el-link type="info" :underline="false" @click="isRegister = true">
            注册 →
          </el-link>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<style lang="scss" scoped>
/* 样式 */
.login-page {
  height: 100vh;
  background-color: #fff;

  .bg {
    background: url('../../assets/loginPage/logo2.png') no-repeat 60% center / 240px auto,
      url('../../assets/loginPage/login_bg.jpg') no-repeat center / cover;
    border-radius: 0 20px 20px 0;
  }

  .form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    user-select: none;

    .title {
      margin: 0 auto;
    }

    .button {
      width: 100%;
    }

    .flex {
      width: 100%;
      display: flex;
      justify-content: space-between;
    }
  }
}
</style>
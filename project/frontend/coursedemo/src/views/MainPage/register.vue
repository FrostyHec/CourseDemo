<template>
	<div class="login-wrap">
		<el-form class="login-container">
			<h1 class="title">用户注册</h1>
			<el-form-item label="">
				<el-input type="text" v-model="username" placeholder="注册账号" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item label="">
				<el-input type="password" v-model="password" placeholder="注册密码" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" style="width:100%;" @click="doSubmit()">提交</el-button>
			</el-form-item>
			<el-row style="text-align: center;margin-top:-10px">
				<el-link type="primary" @click="gotoLogin()">用户登录</el-link>
			</el-row>
		</el-form>
	</div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';

const username = ref('');
const password = ref('');

const doSubmit = () => {
    const params = {
        username: username.value,
        password: password.value
    };
    console.log(params);
    const url = "http://localhost:4173/MainPage/login";

    axios.get(url, { params: params })
        .then(res => {
            console.log(res);
            if (res.data.success) {
                // Assuming you have a message component or plugin
                alert(res.data.msg); // Replace with your actual message display method
            } else {
                alert('用户暂未注册！'); // Replace with your actual error message display method
            }
        })
        .catch(error => {
            console.error(error);
            alert('注册失败，请重试！');
        });
};

const gotoLogin = () => {
    // Assuming you have router setup in your Vue 3 application
    router.push('/');
};
</script> 
<style scoped>
	.login-wrap {
		box-sizing: border-box;
		width: 100%;
		height: 100%;
		padding-top: 10%;
		background-image: url();
		/* background-color: #112346; */
		background-repeat: no-repeat;
		background-position: center right;
		background-size: 100%;
	}
 
	.login-container {
		border-radius: 10px;
		margin: 0px auto;
		width: 350px;
		padding: 30px 35px 15px 35px;
		background: #fff;
		border: 1px solid #eaeaea;
		text-align: left;
		box-shadow: 0 0 20px 2px rgba(0, 0, 0, 0.1);
	}
 
	.title {
		margin: 0px auto 40px auto;
		text-align: center;
		color: #505458;
	}
</style>
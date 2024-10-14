import { defineStore } from 'pinia'

import { loginCall, type LoginParam, logoutCall,type LogoutParam, type UserEntity, UserType } from '@/api/UserAPI'
import { reactive, ref } from 'vue'
export const useAuthStore = defineStore('auth', () => {
  const token = ref('')
  const emptyUser:UserEntity = {
    user_id: BigInt(0),
    first_name: '',
    last_name: '',
    password: '',
    user_type: UserType.STUDENT,
    create_at: new Date(0),
    update_at: new Date(0)
  }
  const user= reactive({ ...emptyUser })
  
  async function login(loginParam:LoginParam){
    const result = await loginCall(loginParam)
    token.value = result.data.token;
    console.log(result)
  }

  async function logout(logoutParam:LogoutParam){
    await logoutCall(logoutParam)
    token.value = '';
    Object.assign(user, emptyUser);
  }

  return {
    token,
    user,
    login,
    logout
  }
})
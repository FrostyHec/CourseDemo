import { defineStore } from 'pinia'

import { loginCall,type LoginParam,type UserEntity, UserType } from '@/api/UserAPI'
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
  function login(loginParam:LoginParam){
    loginCall(loginParam).then((result)=>{
      token.value = result.data.token;
    }).catch((error)=>{
      console.log(error);
    });
  }

  function logout(){
    token.value = '';
    Object.assign(user, emptyUser);
  }
})
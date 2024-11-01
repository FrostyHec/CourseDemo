import { defineStore } from 'pinia'
import {
  loginCall,
  type LoginParam,
  logoutCall,
  type LogoutParam,
  type
  UserPublicInfoEntity,
  UserType
} from '@/api/user/UserAPI'
import { reactive, ref } from 'vue'
import Cookies from 'js-cookie'

export const useAuthStore = defineStore('auth', () => {
  const token = ref('')
  const emptyUser: UserPublicInfoEntity = {
    user_id: 0,
    first_name: '',
    last_name: '',
    email: '',
    role: UserType.STUDENT,
  }
  const user = reactive({ ...emptyUser })

  function init() {
    const res = getLoginToken()
    if (res) {
      token.value = res
    }
  }
  init()

  async function login(loginParam: LoginParam) {
    const result = await loginCall(loginParam)
    if(result.code!==200)
      return result
    token.value = result.data.token
    Object.assign(user, result.data.user)
    setLoginToken(token.value)
    console.log(result)
    return result
  }

  async function logout(logoutParam: LogoutParam) {
    await logoutCall(logoutParam)
    token.value = ''
    setLoginToken('')
    Object.assign(user, emptyUser)
  }

  return {
    token,
    user,
    login,
    logout
  }
})

export function setLoginToken(token: string): void {
  Cookies.set('token', token)
}

export function getLoginToken(): string {
  const token = Cookies.get('token')
  if(token)
    return token
  return ''
}
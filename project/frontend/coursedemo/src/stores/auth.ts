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
    const res_ = getLoginUser()
    if (res) {
      token.value = res
    }
    if(res_) {
      Object.assign(user, res_)
    }
  }
  init()

  async function login(loginParam: LoginParam) {
    const result = await loginCall(loginParam)
    if(result.code!==200)
      return result
    token.value = result.data.token
    Object.assign(user, result.data.user)
    setLoginTokenUser(token.value, user)
    console.log(result, user)
    return result
  }

  async function logout(logoutParam: LogoutParam) {
    await logoutCall(logoutParam)
    token.value = ''
    setLoginTokenUser('', emptyUser)
    Object.assign(user, emptyUser)
  }

  return {
    token,
    user,
    login,
    logout
  }
})

export function setLoginTokenUser(token: string, user: UserPublicInfoEntity): void {
  Cookies.set('token', token)
  Cookies.set('user', JSON.stringify(user))
}

export function getLoginToken(): string {
  const token = Cookies.get('token')
  if(token)
    return token
  return ''
}

export function getLoginUser(): UserPublicInfoEntity|undefined {
  const user = Cookies.get('user')
  if(user)
    return JSON.parse(user)
  return undefined
}
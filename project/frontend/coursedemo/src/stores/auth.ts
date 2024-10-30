import { defineStore } from 'pinia'
import { loginCall, type LoginParam, logoutCall, type LogoutParam, type UserEntity, UserType } from '@/api/user/UserAPI'
import { reactive, ref } from 'vue'
import Cookies from 'js-cookie'

export const useAuthStore = defineStore('auth', () => {
  const token = ref('')
  const emptyUser: UserEntity = {
    user_id: BigInt(0),
    first_name: '',
    last_name: '',
    password: '',
    email: '',
    role: UserType.STUDENT,
    create_at: new Date(0),
    update_at: new Date(0)
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
    token.value = result.data.token
    setLoginToken(token.value)
    console.log(result)
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
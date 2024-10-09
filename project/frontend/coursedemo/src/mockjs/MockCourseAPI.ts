import type { LoginParam, LoginResult, LogoutParam } from '@/api/UserAPI'
import { backend_base } from '@/utils/Cosntant'
import { setMockFunc } from '@/utils/MockUtils'
import { RequestType } from '@/utils/APIUtils'
export function mockUserLogin(data: LoginResult) {
  const url = backend_base + '/auth/login'
  setMockFunc(url, RequestType.POST, null, () => data)
}

export function mockUserLogout() {
  const url = backend_base + '/auth/logout'
  setMockFunc(url, RequestType.POST, null, () => null)
}
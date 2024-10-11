import type { LoginResult } from '@/api/UserAPI'
import { backend_base } from '@/utils/Cosntant'
import { setMockFunc } from '@/utils/MockUtils'
import { APIResult, RequestType } from '@/utils/APIUtils'

export function mockUserLoginCall(data: APIResult<LoginResult>) {
  const url = backend_base + '/auth/login'
  setMockFunc(url, RequestType.POST, null, () => data)
}

export function mockUserLogoutCall() {
  const url = backend_base + '/auth/logout'
  setMockFunc(url, RequestType.POST, null, () => null)
}
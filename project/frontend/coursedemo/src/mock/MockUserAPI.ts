import type { LoginResult } from '@/api/user/UserAPI'
import { backend_base } from '@/utils/Cosntant'
import { setMockFunc } from '@/utils/MockUtils'
import { APIResult, RequestType } from '@/utils/APIUtils'

export function mockLoginCall(data: APIResult<LoginResult>) {
  const url = backend_base + '/auth/login'
  setMockFunc(url, RequestType.POST, null, () => data)
}

export function mockLogoutCall() {
  const url = backend_base + '/auth/logout'
  setMockFunc(url, RequestType.POST, null, () => null)
}


import type { LoginResult, UserEntity, UserPublicInfoEntity } from '@/api/user/UserAPI'
import { service_backend_base } from '@/utils/Constant'
import { setMockFunc } from '@/utils/MockUtils'
import { APIResult, RequestType } from '@/utils/APIUtils'

export function mockLoginCall(data: APIResult<LoginResult>) {
  const url = service_backend_base + '/auth/login'
  setMockFunc(url, RequestType.POST, null, () => data)
}

export function mockLogoutCall(data : APIResult<null>) {
  const url = service_backend_base + '/auth/logout'
  setMockFunc(url, RequestType.POST, null, () => data)
}

// Mock for Create User
export function mockCreateUserCall(data : APIResult<null>) {
  const url = service_backend_base + '/user/create'
  setMockFunc(url, RequestType.POST, null, () => data)
}

// Mock for Update User
export function mockUpdateUserCall(userId: number,data : APIResult<null>) {
  const url = service_backend_base + '/user/' + userId
  setMockFunc(url, RequestType.PUT, null, () => data)
}

// Mock for Delete User
export function mockDeleteUserCall(userId: number,data : APIResult<null>) {
  const url = service_backend_base + '/user/' + userId
  setMockFunc(url, RequestType.DELETE, null, () => data)
}

// Mock for Get User All Info
export function mockGetUserAllInfoCall(userId: number, data: APIResult<UserEntity>) {
  const url = service_backend_base + '/user/' + userId
  setMockFunc(url, RequestType.GET, null, () => data)
}

// Mock for Get User Public Info
export function mockGetUserPublicInfoCall(userId: number, data: APIResult<UserPublicInfoEntity>) {
  const url = service_backend_base + '/user/public/' + userId
  setMockFunc(url, RequestType.GET, null, () => data)
}

// Mock for Search User
export function mockSearchUserCall(data: APIResult<{ content: UserPublicInfoEntity[] }>) {
  const url = service_backend_base + '/user/search'
  setMockFunc(url, RequestType.GET, null, () => data)
}
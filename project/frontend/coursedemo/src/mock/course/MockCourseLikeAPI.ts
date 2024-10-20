// Mock Func
import { service_backend_base } from '@/utils/Constant'
import { setMockFunc } from '@/utils/MockUtils'
import { APIResult, RequestType } from '@/utils/APIUtils'

/**
 * Mock 点赞课程
 * @param data API调用结果
 */
export function mockCreateCourseLikeCall(data: APIResult<null>) {
  const url = `${service_backend_base}/course/{courseId}/like`
  setMockFunc(url, RequestType.POST, null, () => data)
}

/**
 * Mock 取消点赞课程
 * @param data API调用结果
 */
export function mockCancelCourseLikeCall(data: APIResult<null>) {
  const url = `${service_backend_base}/course/{courseId}/like`
  setMockFunc(url, RequestType.DELETE, null, () => data)
}

/**
 * Mock 检查用户是否点赞了课程
 * @param data 是否点赞的结果
 */
export function mockGetCourseLikeCall(data: APIResult<{ is_like: boolean }>) {
  const url = `${service_backend_base}/course/{courseId}/like`
  setMockFunc(url, RequestType.GET, null, () => data)
}

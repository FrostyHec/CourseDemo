import { service_backend_base } from '@/utils/Constant'
import { AxiosAPI } from '@/utils/APIUtils'

/////////////////////   COURSE LIKE   ///////////////////////////////

/**
 * 点赞课程
 * @param courseId 课程ID
 * @returns API调用结果
 */
export async function createCourseLikeCall(courseId: number) {
  const url = `${service_backend_base}/course/${courseId}/like`
  return AxiosAPI.authPost<null>(url) // 使用POST方法，并传递userId
}

/**
 * 取消点赞课程
 * @param courseId 课程ID
 * @returns API调用结果
 */
export async function cancelCourseLikeCall(courseId: number) {
  const url = `${service_backend_base}/course/${courseId}/like`
  return AxiosAPI.authDelete<null>(url)
}

/**
 * 检查用户是否点赞了课程
 * @param courseId 课程ID
 * @returns 是否点赞的结果
 */
export async function getCourseLikeCall(courseId: number) {
  const url = `${service_backend_base}/course/${courseId}/like`
  return AxiosAPI.authGet<{ is_like: boolean }>(url)
}

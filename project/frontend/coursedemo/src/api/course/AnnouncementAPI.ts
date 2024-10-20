import { backend_base } from '@/utils/Constant'
import { APIResult, AxiosAPI } from '@/utils/APIUtils'

/////////////////////   ANNOUNCEMENT   ///////////////////////////////

export interface AnnouncementEntity {
  notification_id: number
  course_id: number
  receiver_ids: number[]
  message: string
  created_at: Date
  updated_at: Date
}

export async function createAnnouncementCall(courseId: number, param: AnnouncementEntity): Promise<APIResult<null>> {
  const url = `${backend_base}/course/${courseId}/announcement`
  return AxiosAPI.authPost(url, param)
}

export async function updateAnnouncementCall(id: number, param: AnnouncementEntity): Promise<APIResult<null>> {
  const url = `${backend_base}/announcement/${id}`
  return AxiosAPI.authPut(url, param)
}

export async function deleteAnnouncementCall(id: number): Promise<APIResult<null>> {
  const url = `${backend_base}/announcement/${id}`
  return AxiosAPI.authDelete(url)
}

export async function getAnnouncementByIdCall(id: number): Promise<APIResult<AnnouncementEntity>> {
  const url = `${backend_base}/announcement/${id}`
  return AxiosAPI.authGet(url)
}

export async function getAnnouncementsByCourseIdCall(courseId: number): Promise<APIResult<AnnouncementEntity[]>> {
  const url = `${backend_base}/course/${courseId}/announcement`
  return AxiosAPI.authGet(url)
}

export async function notifyViaSiteCall(id: number): Promise<APIResult<null>> {
  const url = `${backend_base}/announcement/${id}/site-notify`
  return AxiosAPI.authPost(url)
}

export async function notifyViaEmailCall(id: number): Promise<APIResult<null>> {
  const url = `${backend_base}/announcement/${id}/email-notify`
  return AxiosAPI.authPost(url)
}

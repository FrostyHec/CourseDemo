import { service_backend_base } from '@/utils/Constant'
import { AxiosAPI } from '@/utils/APIUtils'

/////////////////////   ANNOUNCEMENT   ///////////////////////////////

export interface AnnouncementEntity {
  title: string
  notification_id: number
  course_id: number
  receiver_ids: number[]
  message: string
  created_at: Date
  updated_at: Date
}

export async function createAnnouncementCall(courseId: number, param: AnnouncementEntity){
  const url = `${service_backend_base}/course/${courseId}/announcement`
  return AxiosAPI.authPost<null>(url, param)
}

export async function updateAnnouncementCall(id: number, param: AnnouncementEntity){
  const url = `${service_backend_base}/announcement/${id}`
  return AxiosAPI.authPut<null>(url, param)
}

export async function deleteAnnouncementCall(id: number){
  const url = `${service_backend_base}/announcement/${id}`
  return AxiosAPI.authDelete<null>(url)
}

export async function getAnnouncementByIdCall(id: number){
  const url = `${service_backend_base}/announcement/${id}`
  return AxiosAPI.authGet<AnnouncementEntity>(url)
}

export async function getAnnouncementsByCourseIdCall(courseId: number){
  const url = `${service_backend_base}/course/${courseId}/announcement`
  return AxiosAPI.authGet<{content:AnnouncementEntity[]}>(url)
}

export async function notifyViaSiteCall(id: number){
  const url = `${service_backend_base}/announcement/${id}/site-notify`
  return AxiosAPI.authPost<null>(url)
}

export async function notifyViaEmailCall(id: number){
  const url = `${service_backend_base}/announcement/${id}/email-notify`
  return AxiosAPI.authPost<null>(url)
}

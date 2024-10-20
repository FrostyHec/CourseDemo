// Mock Func
import type { AnnouncementEntity } from '@/api/course/AnnouncementAPI'
import { backend_base } from '@/utils/Constant'
import { setMockFunc } from '@/utils/MockUtils'
import { APIResult, RequestType } from '@/utils/APIUtils'

export function mockCreateAnnouncementCall(data: APIResult<null>, courseId: number) {
  const url = `${backend_base}/course/${courseId}/announcement`
  setMockFunc(url, RequestType.POST, null, () => data)
}

export function mockUpdateAnnouncementCall(data: APIResult<null>, id: number) {
  const url = `${backend_base}/announcement/${id}`
  setMockFunc(url, RequestType.PUT, null, () => data)
}

export function mockDeleteAnnouncementCall(data: APIResult<null>, id: number) {
  const url = `${backend_base}/announcement/${id}`
  setMockFunc(url, RequestType.DELETE, null, () => data)
}

export function mockGetAnnouncementByIdCall(data: APIResult<AnnouncementEntity>, id: number) {
  const url = `${backend_base}/announcement/${id}`
  setMockFunc(url, RequestType.GET, null, () => data)
}

export function mockGetAnnouncementsByCourseIdCall(data: APIResult<AnnouncementEntity[]>, courseId: number) {
  const url = `${backend_base}/course/${courseId}/announcement`
  setMockFunc(url, RequestType.GET, null, () => data)
}

export function mockNotifyViaSiteCall(data: APIResult<null>, id: number) {
  const url = `${backend_base}/announcement/${id}/site-notify`
  setMockFunc(url, RequestType.POST, null, () => data)
}

export function mockNotifyViaEmailCall(data: APIResult<null>, id: number) {
  const url = `${backend_base}/announcement/${id}/email-notify`
  setMockFunc(url, RequestType.POST, null, () => data)
}

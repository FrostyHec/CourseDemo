import { backend_base } from '@/utils/Constant'
import { APIResult, AxiosAPI } from '@/utils/APIUtils'
import type { UserPublicInfoEntity } from '@/api/user/UserAPI'

/////////////////////   COURSE   ///////////////////////////////
export interface CourseEntity {
  course_id: number,
  course_name: string,
  description: string,
  teacher_id: number,
  status: CourseStatus,
  publication: Publication
  create_at: Date,
  update_at: Date
}

export enum Publication {
  open = 'open',
  closed = 'closed',
  semi_open = 'semi_open'
}

export enum CourseStatus {
  creating = 'creating',
  submitted = 'submitted',
  published = 'published',
  rejected = 'rejected',
  archived = 'archived'
}

export interface CourseStatusUpdateParam {
  status: CourseStatus
}

export async function createCourseCall(param: CourseEntity) {
  const url = backend_base + '/course'
  return AxiosAPI.authPost<null>(url, param)
}

export async function updateCourseStatusCall(courseId: number, param: CourseStatusUpdateParam) {
  const url = backend_base + '/course/' + courseId + '/status'
  return AxiosAPI.authPatch<null>(url, param)
}

export async function updateCourseInfoCall(courseId: number, param: CourseEntity) {
  const url = backend_base + '/course/' + courseId
  return AxiosAPI.authPut<null>(url, param)
}

export async function getCourseCall(courseId: number) {
  const url = backend_base + '/course/' + courseId
  return AxiosAPI.authGet<CourseEntity>(url, {})
}

export async function deleteCourseCall(courseId: number) {
  const url = backend_base + '/course/' + courseId
  return AxiosAPI.authDelete<null>(url, {})
}

export async function updateCoursePublicationCall(courseId: number, publication: Publication) {
  const url = backend_base + '/course/' + courseId + '/publication'
  return AxiosAPI.authPatch<null>(url, { publication: publication })
}

export async function searchCourseCall(pageSize: number, pageNum: number, name: string = '') {
  const url = backend_base + '/course/search'
  return AxiosAPI.authGet<{ content: CourseEntity[] }>(url, {
    page_num: pageNum,
    page_size: pageSize,
    name: name
  })
}
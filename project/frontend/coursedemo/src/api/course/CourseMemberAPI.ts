import { service_backend_base } from '@/utils/Constant'
import { AxiosAPI } from '@/utils/APIUtils'
import type { UserPublicInfoEntity } from '@/api/user/UserAPI'
import type { CourseEntity } from '@/api/course/CourseAPI'

export async function studentEnrollCourseCall(courseId: number) {
  const url = service_backend_base + '/course/' + courseId + '/student/enroll'
  return AxiosAPI.authPost<null>(url, {})
}

export async function teacherInviteStudentToCourseCall(courseId: number, studentList: number[]) {
  const url = service_backend_base + '/course/' + courseId + '/teacher/invite'
  return AxiosAPI.authPost<null>(url, { student_list: studentList })
}

export async function getAllStudentList(courseId: number, pageNum: number, pageSize: number) {
  const url = service_backend_base + '/course/' + courseId + '/student'
  return AxiosAPI.authGet<{ content: StudentInfoWithEnrollStatus[] }>(url, { page_size: pageSize, page_num: pageNum })
}

export async function getAllJoinedCourseList(studentId: number, pageNum: number, pageSize: number) {
  const url = service_backend_base + '/student/' + studentId + '/courses'
  return AxiosAPI.authGet<{ content: CourseEntity[] }>(url, { page_size: pageSize, page_num: pageNum })
}

export async function getAllTeachingCourseList(teacherId: number, pageNum: number, pageSize: number) {
  const url = service_backend_base + '/teacher/' + teacherId + '/courses'
  return AxiosAPI.authGet<{ content: CourseEntity[] }>(url, { page_size: pageSize, page_num: pageNum })
}

export async function getStudentEnrollStatus(courseId: number, studentId: number, pageNum: number, pageSize: number) {
  const url = service_backend_base + '/course/' + courseId + '/student/' + studentId + '/status'
  return AxiosAPI.authGet<{ status: StudentEnrollType }>(url, { page_size: pageSize, page_num: pageNum })
}

export async function getAllPendingApprovedCourse(adminId: number, pageNum: number, pageSize: number) {
  const url = service_backend_base + '/admin/' + adminId + '/courses/submitted'
  return AxiosAPI.authGet<{ content: CourseEntity[] }>(url, { page_size: pageSize, page_num: pageNum })
}

export async function getAdminIdCourseHandle(adminId: number, pageNum: number, pageSize: number) {
  const url = service_backend_base + '/admin/' + adminId + '/courses/handle'
  return AxiosAPI.authGet<{ content: CourseEntity[] }>(url, { page_size: pageSize, page_num: pageNum })
}

export async function updateStudentEnrollmentStatus(courseId: number, studentId: number, status: StudentEnrollType) {
  const url = service_backend_base + '/course/' + courseId + '/student/' + studentId + '/status'
  return AxiosAPI.authPut<null>(url, { status: status })
}

export async function removeStudentFromCourse(courseId: number, studentId: number) {
  const url = service_backend_base + '/course/' + courseId + '/student/' + studentId
  return AxiosAPI.authDelete<null>(url, {})
}

export interface StudentInfoWithEnrollStatus {
  student: UserPublicInfoEntity
  status: StudentEnrollType
}

export enum StudentEnrollType {
  public = 'publik',
  invited = 'invited'
}
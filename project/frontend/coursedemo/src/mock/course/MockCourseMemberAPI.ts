// Mock Functions
import { service_backend_base } from '@/utils/Constant'
import { setMockFunc } from '@/utils/MockUtils'
import { APIResult, RequestType } from '@/utils/APIUtils'
import type { StudentInfoWithEnrollStatus } from '@/api/course/CourseMemberAPI'
import type { CourseEntity } from '@/api/course/CourseAPI'
import type { StudentEnrollType } from '@/api/course/CourseMemberAPI'

// Mock for studentEnrollCourseCall
export function mockStudentEnrollCourseCall(data: APIResult<null>, courseId: number) {
  const url = service_backend_base + '/course/' + courseId + '/student/enroll';
  setMockFunc(url, RequestType.POST, null, () => data);
}

// Mock for teacherInviteStudentToCourseCall
export function mockTeacherInviteStudentToCourseCall(data: APIResult<null>, courseId: number) {
  const url = service_backend_base + '/course/' + courseId + '/teacher/invite';
  setMockFunc(url, RequestType.POST, null, () => data);
}

// Mock for getAllStudentList
export function mockGetAllStudentList(data: APIResult<{ content: StudentInfoWithEnrollStatus[] }>, courseId: number) {
  const url = service_backend_base + '/course/' + courseId + '/student';
  setMockFunc(url, RequestType.GET, null, () => data);
}

// Mock for getAllJoinedCourseList
export function mockGetAllJoinedCourseList(data: APIResult<{ content: CourseEntity[] }>, studentId: number) {
  const url = service_backend_base + '/student/' + studentId + '/courses';
  setMockFunc(url, RequestType.GET, null, () => data);
}

// Mock for getAllTeachingCourseList
export function mockGetAllTeachingCourseList(data: APIResult<{ content: CourseEntity[] }>, studentId: number) {
  const url = service_backend_base + '/teacher/' + studentId + '/courses';
  setMockFunc(url, RequestType.GET, null, () => data);
}

// Mock for getStudentEnrollStatus
export function mockGetStudentEnrollStatus(data: APIResult<{ status: StudentEnrollType }>, courseId: number, studentId: number) {
  const url = service_backend_base + '/course/' + courseId + '/student/' + studentId + '/status';
  setMockFunc(url, RequestType.GET, null, () => data);
}

// Mock for getAllPendingApprovedCourse
export function mockGetAllPendingApprovedCourse(data: APIResult<{ content: CourseEntity[] }>, adminId: number) {
  const url = service_backend_base + '/admin/' + adminId + '/courses/submitted';
  setMockFunc(url, RequestType.GET, null, () => data);
}

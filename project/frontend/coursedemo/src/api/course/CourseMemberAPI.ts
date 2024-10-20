import { backend_base } from '@/utils/Constant'
import { AxiosAPI } from '@/utils/APIUtils'
import type { UserPublicInfoEntity } from '@/api/user/UserAPI'
import type { CourseEntity } from '@/api/course/CourseAPI'

export async function studentEnrollCourseCall(courseId:number){
  const url = backend_base+'/course/'+courseId+'/student/enroll'
  return AxiosAPI.authPost<null>(url,{})
}
export async function teacherInviteStudentToCourseCall(courseId:number){
  const url = backend_base+'/course/'+courseId+'/teacher/invite'
  return AxiosAPI.authPost<null>(url,{})
}

export async function getAllStudentList(courseId:number,pageNum:number,pageSize:number){
  const url = backend_base+'/course/'+courseId+'/student'
  return AxiosAPI.authGet<{ content:StudentInfoWithEnrollStatus[] }>(url,{page_size:pageSize,page_num:pageNum,})
}
export async function getAllJoinedCourseList(studentId:number,pageNum:number,pageSize:number){
  const url = backend_base+'/student/'+studentId+'/courses'
  return AxiosAPI.authGet<{content:CourseEntity[]}>(url,{page_size:pageSize,page_num:pageNum,})
}
export async function getAllTeachingCourseList(studentId:number,pageNum:number,pageSize:number){
  const url = backend_base+'/teacher/'+studentId+'/courses'
  return AxiosAPI.authGet<{ content:CourseEntity[] }>(url,{page_size:pageSize,page_num:pageNum,})
}

export async function getStudentEnrollStatus(courseId:number,studentId:number,pageNum:number,pageSize:number){
  const url = backend_base+'/course/'+courseId+'/student/'+studentId+'/status'
  return AxiosAPI.authGet<{ status:StudentEnrollType }>(url,{page_size:pageSize,page_num:pageNum,})
}
export async function getAllPendingApprovedCourse(adminId:number,pageNum:number,pageSize:number){
  const url = backend_base+'/admin/'+adminId+"/courses/submitted"
  return AxiosAPI.authGet<{ content:CourseEntity[] }>(url,{page_size:pageSize,page_num:pageNum,})
}
export interface StudentInfoWithEnrollStatus extends UserPublicInfoEntity{
  status:StudentEnrollType
}
export enum StudentEnrollType{
  publik = 'publik',
  invited = 'invited'
}

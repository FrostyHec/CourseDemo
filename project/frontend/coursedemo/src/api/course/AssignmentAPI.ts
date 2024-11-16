import { service_backend_base } from '@/utils/Constant'
import { AxiosAPI } from '@/utils/APIUtils'

/////////////////////   ASSIGNMENT   ///////////////////////////////

export interface AssignmentEntity {
  assignment_id: number
  chapter_id: number
  description: string
  assignment_type: AssignmentType
  allow_update_submission: boolean
  latest_submission_time: Date
  maximum_score: number
  allow_student_to_view_score: boolean
  created_at: Date
  updated_at: Date
}

export enum AssignmentType {
  file_upload = 'file_upload',
  online_form = 'online_form'
}

export async function createAssignmentCall(chapterId: number, param: AssignmentEntity) {
  const url = service_backend_base + '/chapter/' + chapterId + '/assignment'
  return AxiosAPI.authPost<null>(url, param)
}

export async function updateAssignmentCall(assignmentId: number, param: AssignmentEntity) {
  const url = service_backend_base + '/assignment/' + assignmentId
  return AxiosAPI.authPut<null>(url, param)
}

export async function deleteAssignmentCall(assignmentId: number) {
  const url = service_backend_base + '/assignment/' + assignmentId
  return AxiosAPI.authDelete<null>(url, {})
}

export async function getAssignmentCall(assignmentId: number) {
  const url = service_backend_base + '/assignment/' + assignmentId
  return AxiosAPI.authGet<AssignmentEntity>(url, {})
}

export async function getAssignmentsByChapterIdCall(chapterId: number){
  const url = `${service_backend_base}/chapter/${chapterId}/assignment`
  return AxiosAPI.authGet<{content:AssignmentEntity[]}>(url)
}

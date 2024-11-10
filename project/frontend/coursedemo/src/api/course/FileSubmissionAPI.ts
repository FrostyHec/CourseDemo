import { service_backend_base } from '@/utils/Constant'
import { AxiosAPI } from '@/utils/APIUtils'
import axios from 'axios'

/////////////////////   FILE SUBMISSION   ///////////////////////////////

export interface FileSubmissionEntity {
  file_submission_id: number
  assignment_id: number
  student_id: number
  file_name: string
  gained_score: number
  created_at: Date
  updated_at: Date
}

export interface FileSubmissionWithAccessKey {
  file_submission: FileSubmissionEntity
  access_key: string
}

export interface SubmissionScore {
  gained_score: number
}

export async function submitFileCall(assignmentId: number, fileSubmission: FileSubmissionEntity, file: File) {
  const url = service_backend_base + '/assignment/' + assignmentId + '/upload-submission'
  const formData = new FormData()
  formData.append('data', new Blob([JSON.stringify(fileSubmission)], { type: 'application/json' }))
  formData.append('file', file)
  const config = AxiosAPI.setAuthHeader();
  (config.headers as any)['Content-Type'] = 'multipart/form-data'
  return AxiosAPI.extractResult<null>(await axios.post(url, formData, config))
}

export async function deleteFileSubmissionCall(fileSubmissionId: number) {
  const url = service_backend_base + '/upload-submission/' + fileSubmissionId
  return AxiosAPI.authDelete<null>(url, {})
}

export async function getFileSubmissionCall(fileSubmissionId: number) {
  const url = service_backend_base + '/upload-submission/' + fileSubmissionId
  return AxiosAPI.authGet<FileSubmissionWithAccessKey>(url, {})
}

export async function updateScoreCall(fileSubmissionId: number, submissionScore: SubmissionScore) {
  const url = service_backend_base + '/upload-submission/' + fileSubmissionId + '/score'
  return AxiosAPI.authPatch<null>(url, submissionScore)
}

export async function getStudentSubmissionCall(assignmentId: number) {
  const url = service_backend_base + '/assignment/' + assignmentId + '/my-submission'
  return AxiosAPI.authGet<FileSubmissionWithAccessKey>(url, {})
}

export async function getAllSubmissionCall(assignmentId: number) {
  const url = service_backend_base + '/assignment/' + assignmentId + '/all-submission'
  return AxiosAPI.authGet<{ content: FileSubmissionEntity[] }>(url, {})
}

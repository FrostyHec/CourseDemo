import { service_backend_base } from '@/utils/Constant'
import { AxiosAPI } from '@/utils/APIUtils'

/////////////////////   CHAPTER   ///////////////////////////////

export interface ChapterEntity {
  chapter_id: number
  course_id: number
  chapter_order: number
  chapter_title: string
  chapter_type: ChapterType
  content: string
  created_at: Date
  updated_at: Date
}

export enum ChapterType {
  teaching = 'teaching',
  assignment = 'assignment',
  project = 'project'
}

export async function createChapterCall(courseId: number, param: ChapterEntity){
  const url = service_backend_base + '/course/' + courseId + '/chapter'
  return AxiosAPI.authPost<null>(url, param)
}

export async function updateChapterCall(chapterId: number, param: ChapterEntity){
  const url = service_backend_base + '/chapter/' + chapterId
  return AxiosAPI.authPut<null>(url, param)
}

export async function deleteChapterCall(chapterId: number) {
  const url = service_backend_base + '/chapter/' + chapterId
  return AxiosAPI.authDelete<null>(url, {})
}

export async function getChapterCall(chapterId: number){
  const url = service_backend_base + '/chapter/' + chapterId
  return AxiosAPI.authGet<ChapterEntity>(url, {})
}

export async function getAllChapterCall(courseId: number){
  const url = service_backend_base + '/course/' + courseId + '/chapter'
  return AxiosAPI.authGet<{content:ChapterEntity[]}>(url, {})
}

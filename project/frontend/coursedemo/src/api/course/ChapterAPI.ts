import { backend_base } from '@/utils/Cosntant'
import { APIResult, AxiosAPI } from '@/utils/APIUtils'
import type { UserPublicInfoEntity } from '@/api/user/UserAPI'

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

export async function createChapterCall(courseId: number, param: ChapterEntity): Promise<APIResult<null>> {
  const url = backend_base + '/course/' + courseId + '/chapter'
  return AxiosAPI.authPost(url, param)
}

export async function updateChapterCall(chapterId: number, param: ChapterEntity): Promise<APIResult<null>> {
  const url = backend_base + '/chapter/' + chapterId
  return AxiosAPI.authPut(url, param)
}

export async function deleteChapterCall(chapterId: number): Promise<APIResult<null>> {
  const url = backend_base + '/chapter/' + chapterId
  return AxiosAPI.authDelete(url, {})
}

export async function getChapterCall(chapterId: number): Promise<APIResult<ChapterEntity>> {
  const url = backend_base + '/chapter/' + chapterId
  return AxiosAPI.authGet(url, {})
}

export async function getAllChapterCall(courseId: number): Promise<APIResult<ChapterEntity[]>> {
  const url = backend_base + '/course/' + courseId + '/chapter'
  return AxiosAPI.authGet(url, {})
}

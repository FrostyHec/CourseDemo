/////////////////////   CHAPTER   ///////////////////////////////
import type { APIResult } from '@/utils/APIUtils'
import { backend_base } from '@/utils/Constant'
import { RequestType } from '@/utils/APIUtils'
import { setMockFunc } from '@/utils/MockUtils'
import type { ChapterEntity } from '@/api/course/ChapterAPI'

export function mockCreateChapterCall(courseId: number, data: APIResult<null>) {
  const url = backend_base + '/course/' + courseId + '/chapter'
  setMockFunc(url, RequestType.POST, null, () => data)
}

export function mockUpdateChapterCall(chapterId: number, data: APIResult<null>) {
  const url = backend_base + '/chapter/' + chapterId
  setMockFunc(url, RequestType.PUT, null, () => data)
}

export function mockDeleteChapterCall(chapterId: number, data: APIResult<null>) {
  const url = backend_base + '/chapter/' + chapterId
  setMockFunc(url, RequestType.DELETE, null, () => data)
}

export function mockGetChapterCall(chapterId: number, data: APIResult<ChapterEntity>) {
  const url = backend_base + '/chapter/' + chapterId
  setMockFunc(url, RequestType.GET, null, () => data)
}

export function mockGetAllChapterCall(courseId: number, data: APIResult<ChapterEntity[]>) {
  const url = backend_base + '/course/' + courseId + '/chapter'
  setMockFunc(url, RequestType.GET, null, () => data)
}
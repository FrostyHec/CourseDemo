import type { ChapterEntity, CourseEntity, CourseStatusUpdateParam } from '@/api/CourseAPI'
import type { APIResult } from '@/utils/APIUtils'
import { backend_base } from '@/utils/Constant'
import { AxiosAPI, RequestType } from '@/utils/APIUtils'
import { setMockFunc } from '@/utils/MockUtils'

/////////////////////   COURSE   ///////////////////////////////
export function mockCreateCourseCall(data: APIResult<CourseEntity>) {
    const url = backend_base + '/course'
    setMockFunc(url, RequestType.POST, null, () => data)
}

export function mockUpdateCourseStatusCall(courseId: number, data: APIResult<null>) {
    const url = backend_base + '/course/' + courseId + '/status'
    setMockFunc(url, RequestType.PATCH, null, () => data)
}

export function mockUpdateCourseInfoCall(courseId: number, data: APIResult<CourseEntity[]>) {
    const url = backend_base + '/course/' + courseId
    setMockFunc(url, RequestType.PUT, null, () => data)
}

export function mockGetCourseCall(courseId: number, data: APIResult<CourseEntity>) {
    const url = backend_base + '/course/' + courseId
    setMockFunc(url, RequestType.GET, null, () => data)
}

export function mockDeleteCourseCall(courseId: number, data: APIResult<null>) {
    const url = backend_base + '/course/' + courseId
    setMockFunc(url, RequestType.DELETE, null, () => data)
}
/////////////////////   CHAPTER   ///////////////////////////////
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


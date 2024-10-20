import type { CourseEntity} from '@/api/course/CourseAPI'
import type { APIResult } from '@/utils/APIUtils'
import { backend_base } from '@/utils/Constant'
import { RequestType } from '@/utils/APIUtils'
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

export function mockUpdateCoursePublicationCall(courseId: number, data: APIResult<null>) {
    const url = backend_base + '/course/' + courseId + '/publication'
    setMockFunc(url, RequestType.PATCH, null, () => data)
}

export function mockSearchCourseCall(pageSize: number, pageNum: number, name: string, data: APIResult<{ content: CourseEntity[] }>) {
    const url = backend_base + '/course/search'
    setMockFunc(url, RequestType.GET, { page_num: pageNum, page_size: pageSize, name: name }, () => data)
}

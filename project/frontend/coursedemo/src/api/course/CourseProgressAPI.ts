import { service_backend_base } from '@/utils/Constant'
import { AxiosAPI } from '@/utils/APIUtils'

/////////////////////   COURSE PROGRESS   ///////////////////////////////

export interface CourseProgress {
    course_id: number
    chapter_progress: ChapterProgress[]
    is_completed: boolean
}

export interface ChapterProgress {
    chapter_id: number
    video_resources: ResourceProgress[]
    is_completed: boolean
}

export interface ResourceProgress {
    resource_id: number
    is_completed: boolean
}

export async function completeResourceCall(resourceId: number) {
    const url = service_backend_base + '/resource/' + resourceId + '/study/complete'
    return AxiosAPI.authPut<null>(url, {})
}

export async function completeChapterCall(chapterId: number) {
    const url = service_backend_base + '/chapter/' + chapterId + '/study/complete'
    return AxiosAPI.authPut<null>(url, {})
}

export async function completeCourseCall(courseId: number) {
    const url = service_backend_base + '/course/' + courseId + '/study/complete'
    return AxiosAPI.authPut<null>(url, {})
}

export async function checkCourseProgressCall(courseId: number) {
    const url = service_backend_base + '/course/' + courseId + '/study'
    return AxiosAPI.authGet<CourseProgress>(url, {})
}

export async function clearAllStudentCourseProgressCall(courseId: number) {
    const url = service_backend_base + '/course/' + courseId + '/study/all-clear'
    return AxiosAPI.authPut<null>(url, {})
}

export async function clearAllStudentChapterProgressCall(chapterId: number) {
    const url = service_backend_base + '/chapter/' + chapterId + '/study/all-clear'
    return AxiosAPI.authPut<null>(url, {})
}

export async function clearAllStudentResourceProgressCall(resourceId: number) {
    const url = service_backend_base + '/resource/' + resourceId + '/study/all-clear'
    return AxiosAPI.authPut<null>(url, {})
}

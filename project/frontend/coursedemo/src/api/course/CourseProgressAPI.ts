import { service_backend_base } from '@/utils/Constant'
import { AxiosAPI } from '@/utils/APIUtils'

/////////////////////   COURSE PROGRESS   ///////////////////////////////

export interface CourseProgress {
    chapters: string
    chapterProgress: ChapterProgress[]
    isCompleted: boolean
}

export interface ChapterProgress {
    chapterId: number
    videoResources: ResourceProgress[]
    isCompleted: boolean
}

export interface ResourceProgress {
    resourceId: number
    isCompleted: boolean
}

export async function completeResourceCall(resourceId: string) {
    const url = service_backend_base + '/resource/' + resourceId + '/study/complete'
    return AxiosAPI.authPut<null>(url, {})
}

export async function completeChapterCall(chapterId: string) {
    const url = service_backend_base + '/chapter/' + chapterId + '/study/complete'
    return AxiosAPI.authPut<null>(url, {})
}

export async function completeCourseCall(courseId: string) {
    const url = service_backend_base + '/course/' + courseId + '/study/complete'
    return AxiosAPI.authPut<null>(url, {})
}

export async function checkCourseProgressCall(courseId: string) {
    const url = service_backend_base + '/course/' + courseId + '/study'
    return AxiosAPI.authGet<CourseProgress>(url, {})
}

export async function clearAllStudentCourseProgressCall(courseId: string) {
    const url = service_backend_base + '/course/' + courseId + '/study/all-clear'
    return AxiosAPI.authPut<null>(url, {})
}

export async function clearAllStudentChapterProgressCall(chapterId: string) {
    const url = service_backend_base + '/chapter/' + chapterId + '/study/all-clear'
    return AxiosAPI.authPut<null>(url, {})
}

export async function clearAllStudentResourceProgressCall(resourceId: string) {
    const url = service_backend_base + '/resource/' + resourceId + '/study/all-clear'
    return AxiosAPI.authPut<null>(url, {})
}

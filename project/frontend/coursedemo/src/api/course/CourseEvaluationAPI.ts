import { service_backend_base } from '@/utils/Constant'
import { AxiosAPI } from '@/utils/APIUtils'

/////////////////////   COURSE EVALUATION   ///////////////////////////////

export interface CourseEvaluationEntity {
    course_id: number
    student_id: number
    comment: string
    score: number
    evaluation_form_answer: answer[],//某个{ xxxxx的json}，只要是可转成json的实体就可以
    created_at: Date
    updated_at: Date
}

export enum evaluationType {
    rating = 'rating',
    filling = 'filling',
}

export interface answer {
    id: number;
    type: evaluationType;
    result: string;
}

export async function createEvaluationCall(courseId: number, param: CourseEvaluationEntity) {
    const url = service_backend_base + `/course/${courseId}/evaluation`
    return AxiosAPI.authPost<null>(url, param)
}

export async function updateEvaluationCall(courseId: number, param: CourseEvaluationEntity) {
    const url = service_backend_base + `/course/${courseId}/evaluation`
    return AxiosAPI.authPut<null>(url, param)
}

export async function deleteEvaluationCall(courseId: number) {
    const url = service_backend_base + `/course/${courseId}/evaluation`
    return AxiosAPI.authDelete<null>(url, {})
}

export async function getMyEvaluationCall(courseId: number) {
    const url = service_backend_base + `/course/${courseId}/evaluation`
    return AxiosAPI.authGet<CourseEvaluationEntity>(url, {})
}

export async function getEvaluationsCall(courseId: number, pageSize: number, pageNum: number) {
    const url = service_backend_base + `/course/${courseId}/evaluations?page_size=${pageSize}&page_num=${pageNum}`
    return AxiosAPI.authGet<{ content: CourseEvaluationEntity[] }>(url, {})
}


export async function getEvaluationMetadataCall(courseId: number) {
    const url = service_backend_base + `/course/${courseId}/evaluations/metadata'`
    return AxiosAPI.authGet<{ average_score:number }>(url, {})
}
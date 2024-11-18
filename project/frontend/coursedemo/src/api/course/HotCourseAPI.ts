import type {UserPublicInfoEntity} from "@/api/user/UserAPI";
import type {CourseEntity} from "@/api/course/CourseAPI";
import {service_backend_base} from "@/utils/Constant";
import {AxiosAPI} from "@/utils/APIUtils";

export interface CourseWithStudentCount {
    course: CourseEntity;
    student_num: number; // 已注册学生数
}

export interface TeacherWithStudentCount {
    teacher: UserPublicInfoEntity;
    student_num: number; // 已注册学生数
}

/////////////////////   RECOMMEND   ///////////////////////////////

export async function getHotCoursesCall(pageSize: number, pageNum: number) {
    const url = `${service_backend_base}/recommend/courses/hot?page_size=${pageSize}&page_num=${pageNum}`;
    return AxiosAPI.authGet<{ content: CourseWithStudentCount[] }>(url, {});
}

export async function getHotTeachersCall(pageSize: number, pageNum: number) {
    const url = `${service_backend_base}/recommend/teachers/hot?page_size=${pageSize}&page_num=${pageNum}`;
    return AxiosAPI.authGet<{ content: TeacherWithStudentCount[] }>(url, {});
}
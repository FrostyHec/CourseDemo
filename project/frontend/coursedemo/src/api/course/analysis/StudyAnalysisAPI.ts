import { service_backend_base } from '@/utils/Constant';
import { AxiosAPI } from '@/utils/APIUtils';
import type { AssignmentEntity } from "@/api/course/AssignmentAPI";
import type { UserPublicInfoEntity } from "@/api/user/UserAPI";
import type { ChapterEntity, ChapterType } from "@/api/course/ChapterAPI";

/////////////////////   STUDY ANALYSIS   ///////////////////////////////

export interface WarningInfoList {
    content: WarningInfo[];
}

export interface WarningInfo {
    warning_type: WarningType;
    warning_student: number;
    description: WarningInfoContent;
    date: Date;
}

export enum WarningType {
    homework_uncompleted = 'homework_uncompleted',
    low_score = 'low_score'
}

export interface WarningInfoContent {}

export interface AssignmentWarningInfoContent extends WarningInfoContent{
    //两个type都是只有这个content
    assignment:AssignmentEntity
}


export interface StudentsScoreTable {
    column: AssignmentEntity[];
    row: UserPublicInfoEntity[];
    data: number[][];
    total_score: number[];
    average_score: number[];
    average_total_score: number;
}

export interface AssignmentChapterSituationList {
    content: AssignmentChapterSituation[];
}

export interface AssignmentChapterSituation {
    chapter_id: number;
    chapter_type: ChapterType;
    chapter_name: string;
    is_all_graded: boolean;
    assignments: AssignmentSituation[];
}

export interface AssignmentSituation {
    assignment: AssignmentEntity;
    has_submission: boolean;
    is_graded: boolean;
    received_scores: number;
}

export interface CourseStudySituation {
    teaching: TeachingChapterInfo;
    assignment: ScoringChapterInfo;
    project: ScoringChapterInfo;
    difficult_chapters: DifficultChapter[];
}

export interface TeachingChapterInfo {
    total_chapter_count: number;
    completed_count: number;
    max_possible_completed_count: number;
}

export interface ScoringChapterInfo {
    total_chapter_count: number;
    completed_count: number;
    max_possible_completed_count: number;
    changing_rate: SingleChapterInfo[];
}

export interface SingleChapterInfo {
    chapter: ChapterEntity;
    average_score: number;
    max_score: number;
    completed_count: number;
    max_completed_count: number;
}

export interface DifficultChapter {
    chapter: ChapterEntity;
    warning_info: DifficultWarnInfo;
}

export interface DifficultWarnInfo {
    type: WarningType;
    description: number; // 多少人完成/平均得分为多少
}

export interface ChapterStudySituation {
    chapter_type: ChapterType;
    data: ChapterStudySituationData;
}

export interface ChapterStudySituationData {}

export interface TeachingChapter extends ChapterStudySituationData {
    uncompleted_count: number;
    completed_status_list: TeachingCompletedStatus[];
}

export interface ScoreChapter extends ChapterStudySituationData {
    average_score: number;
    ungraded_count: number;
    uncompleted_count: number;
    completed_status_list: ScoreCompletedStatus[];
}

export interface TeachingCompletedStatus {
    student: UserPublicInfoEntity;
    is_completed: boolean;
}
export interface ScoreCompletedStatus {
    student: UserPublicInfoEntity;
    is_completed: boolean;
    is_graded: boolean;
}
export async function get_all_students_warning_call(cid: number) {
    const url = `${service_backend_base}/course/${cid}/study/analysis/students-warning`;
    return AxiosAPI.authGet<WarningInfoList>(url, {});
}

export async function get_my_warning_call(cid: number) {
    const url = `${service_backend_base}/course/${cid}/study/analysis/my-warning`;
    return AxiosAPI.authGet<WarningInfoList>(url, {});
}

export async function get_all_students_score_call(cid: number) {
    const url = `${service_backend_base}/course/${cid}/study/analysis/students-score`;
    return AxiosAPI.authGet<StudentsScoreTable>(url, {});
}

export async function get_my_score_call(cid: number) {
    const url = `${service_backend_base}/course/${cid}/study/analysis/my-score`;
    return AxiosAPI.authGet<AssignmentChapterSituationList>(url, {});
}

export async function teacher_check_chapter_study_situation_call(cid: number, cpid: number) {
    const url = `${service_backend_base}/course/${cid}/study/analysis/teacher/chapter/${cpid}`;
    return AxiosAPI.authGet<ChapterStudySituation>(url, {});
}

export async function teacher_check_course_study_situation_call(cid: number) {
    const url = `${service_backend_base}/course/${cid}/study/analysis/teacher/course`;
    return AxiosAPI.authGet<CourseStudySituation>(url, {});
}

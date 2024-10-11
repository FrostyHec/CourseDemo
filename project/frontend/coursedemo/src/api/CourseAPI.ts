import { backend_base } from '@/utils/Cosntant'
import axios from 'axios'
import { APIResult, AxiosAPI } from '@/utils/APIUtils'
import { InternalException } from '@/utils/Exceptions'
/////////////////////   COURSE   ///////////////////////////////
export interface CourseEntity{
  course_id:number,
  course_name:string,
  description:string,
  teacher_id:number,
  status:CourseStatus,
  create_at:Date,
  update_at:Date
}
export enum CourseStatus{
  creating='creating',
  submitted='submitted',
  published='published',
  rejected='rejected',
  archived='archived'
}
export interface CourseStatusUpdateParam{
  status:CourseStatus
}
export async function createCourseCall(param:CourseEntity):Promise<APIResult<null>>{
  const url = backend_base + '/course';
  return AxiosAPI.authPost(url,param);
}

export async function updateCourseStatusCall(courseId:number,param:CourseStatusUpdateParam){
  const url = backend_base + '/course/'+courseId+'/status';
  return AxiosAPI.authPatch(url,param);
}

export async function updateCourseInfoCall(courseId:number,param:CourseEntity):Promise<APIResult<CourseEntity[]>>{
  const url = backend_base + '/course/'+courseId;
  return AxiosAPI.authPut(url,param);
}

export async function getCourseCall(courseId:number){
  const url = backend_base + '/course/'+courseId;
  return AxiosAPI.authGet(url,{});
}

export async function deleteCourseCall(courseId:number){
  const url = backend_base + '/course/'+courseId;
  return AxiosAPI.authDelete(url,{});
}

/////////////////////   CHAPTER   ///////////////////////////////

export interface ChapterEntity{
  chapter_id: number
  course_id: number
  chapter_order: number
  chapter_title: string
  chapter_type: ChapterType
  content: string
  created_at: Date
  updated_at: Date
}
export enum ChapterType{
  teaching='teaching',
  assignment='assignment',
  project='project'
}

export async function createChapterCall(courseId:number,param:ChapterEntity):Promise<APIResult<null>>{
  const url = backend_base + '/course/'+courseId+'/chapter';
  return AxiosAPI.authPost(url,param);
}

export async function updateChapterCall(chapterId:number,param:ChapterEntity):Promise<APIResult<null>>{
  const url = backend_base + '/chapter/'+chapterId;
  return AxiosAPI.authPut(url,param);
}

export async function deleteChapterCall(chapterId:number):Promise<APIResult<null>>{
  const url = backend_base + '/chapter/'+chapterId;
  return AxiosAPI.authDelete(url,{});
}

export async function getChapterCall(chapterId:number):Promise<APIResult<ChapterEntity>>{
  const url = backend_base + '/chapter/'+chapterId;
  return AxiosAPI.authGet(url,{});
}

export async function getAllChapterCall(courseId:number):Promise<APIResult<ChapterEntity[]>>{
  const url = backend_base + '/course/'+courseId+'/chapter';
  return AxiosAPI.authGet(url,{});
}
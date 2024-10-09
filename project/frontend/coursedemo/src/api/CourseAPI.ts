import { backend_base } from '@/utils/Cosntant'
import axios from 'axios'
import { APIResult } from '@/utils/APIUtils'
import { InternalException } from '@/utils/Exceptions'
export interface CourseEntity{
  course_id:bigint,
  course_name:string,
  description:string,
  teacher_id:bigint,
  status:string,
  create_at:Date,
  update_at:Date
}

export async function createCourseCall(param:CourseEntity):Promise<APIResult<null>>{
  const url = backend_base + '/course';
  await axios.post(url,param).then((response)=> {
    return APIResult.fromAxiosResponse(response);
  }).catch((error)=>{
    throw error;
  });
  throw new InternalException('unreachable code');
}
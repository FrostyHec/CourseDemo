import axios from 'axios'
import { backend_base } from '@/utils/Cosntant'
import { APIResult, type APIParam, type APIDataResult } from '@/utils/APIUtils'
import { InternalException } from '@/utils/Exceptions'
export async function loginCall(param:LoginParam):Promise<APIResult<LoginResult>>{
  const url = backend_base + '/auth/login';
  const response = await axios.post(url,param)
  return APIResult.fromAxiosResponse(response);
}
export interface LoginParam extends APIParam{
  user_id:number,
  password:string
}
export interface LoginResult extends APIDataResult{
  token:string
}

export async function logoutCall(logoutParam:LogoutParam):Promise<APIResult<null>>{
  const url = backend_base + '/auth/logout';
  await axios.post(url,logoutParam).then((response)=> {
    return APIResult.fromAxiosResponse(response);
  }).catch((error)=>{
    throw error;
  });
  throw new InternalException('unreachable code');
}

export interface LogoutParam extends APIParam{
  user_id:number
}


export async function createUser(param:UserEntity):Promise<APIResult<null>>{
  const url = backend_base + '/user/create';
  await axios.post(url,param).then((response)=> {
    return APIResult.fromAxiosResponse(response);
  }).catch((error)=>{
    throw error;
  });
  throw new InternalException('unreachable code');
}

export interface UserEntity extends APIDataResult,APIParam{
  user_id:bigint,
  first_name:string,
  last_name:string,
  password:string,
  user_type:UserType,
  create_at:Date,
  update_at:Date
}

export enum UserType{
  TEACHER = 'teacher',
  STUDENT = 'student',
  ADMIN = 'admin'
}
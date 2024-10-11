import { backend_base } from '@/utils/Cosntant'
import { APIResult, type APIParam, type APIDataResult, AxiosAPI } from '@/utils/APIUtils'
/////////////////////   LOGIN   ///////////////////////////////
export interface LoginParam extends APIParam{
  user_id:number,
  password:string
}
export interface LoginResult extends APIDataResult{
  token:string
}

export async function loginCall(param:LoginParam):Promise<APIResult<LoginResult>>{
  const url = backend_base + '/auth/login';
  return await AxiosAPI.post(url,param);

}
/////////////////////   LOGOUT   ///////////////////////////////
export interface LogoutParam extends APIParam{
  user_id:number
}

export async function logoutCall(logoutParam:LogoutParam):Promise<APIResult<null>>{
  const url = backend_base + '/auth/logout';
  return await AxiosAPI.post(url,logoutParam);
}

/////////////////////   USER   ///////////////////////////////
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

export async function createUserCall(param:UserEntity):Promise<APIResult<null>>{
  const url = backend_base + '/user/create';
  return await AxiosAPI.post(url,param);
}

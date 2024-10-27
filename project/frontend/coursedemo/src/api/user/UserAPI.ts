import { service_backend_base } from '@/utils/Constant'
import { APIResult, type APIParam, type APIDataResult, AxiosAPI } from '@/utils/APIUtils'
/////////////////////   LOGIN   ///////////////////////////////
export interface LoginParam extends APIParam{
  email:string,
  password:string
}
export interface LoginResult extends APIDataResult{
  token:string
}

export async function loginCall(param:LoginParam):Promise<APIResult<LoginResult>>{
  const url = service_backend_base + '/auth/login';
  return await AxiosAPI.post(url,param);

}
/////////////////////   LOGOUT   ///////////////////////////////
export interface LogoutParam extends APIParam{
  user_id:number
}

export async function logoutCall(logoutParam:LogoutParam):Promise<APIResult<null>>{
  const url = service_backend_base + '/auth/logout';
  return await AxiosAPI.post(url,logoutParam);
}

/////////////////////   USER   ///////////////////////////////
export interface UserEntity extends UserPublicInfoEntity{
  password:string,
  create_at:Date,
  update_at:Date
}

export interface UserPublicInfoEntity extends APIDataResult,APIParam{
  user_id:bigint,
  first_name:string,
  last_name:string,
  role:UserType,
  email:string,
}

export enum UserType{
  TEACHER = 'teacher',
  STUDENT = 'student',
  ADMIN = 'admin'
}

export async function createUserCall(param:UserEntity):Promise<APIResult<null>>{
  const url = service_backend_base + '/user/create';
  return await AxiosAPI.post(url,param);
}

export async function updateUserCall(userId:number,param:UserEntity):Promise<APIResult<null>>{
  const url = service_backend_base + '/user/'+userId;
  return await AxiosAPI.authPut(url,param);
}
export async function deleteUserCall(userId:number):Promise<APIResult<null>>{
  const url = service_backend_base + '/user/'+userId;
  return await AxiosAPI.authDelete(url,{});
}

export async function getUserAllInfoCall(userId:number){
  const url = service_backend_base + '/user/'+userId;
  return await AxiosAPI.authGet<UserEntity>(url,{});
}
export async function getUserPublicInfoCall(userId:number){
  const url = service_backend_base + '/user/public/'+userId;
  return await AxiosAPI.authGet<UserPublicInfoEntity>(url,{});
}

export async function searchUserCall(firstName:string,lastName:string){
  const url = service_backend_base + '/user/search'
  return await AxiosAPI.authGet<{content:UserPublicInfoEntity[]}>(url,{first_name:firstName,last_name:lastName});
}
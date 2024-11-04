import { storage_backend_base } from '@/utils/Constant'


export function getResourceAccessLink(objName:string,userId:number,accessKey:string){
  return `${storage_backend_base}/${objName}?case_name=resource-${userId}&access_key=${accessKey}`
}

export function getResourceCaseName(uid:number){
  return `resource-${uid}`
}
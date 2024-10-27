import { storage_backend_base } from '@/utils/Constant'


export function getResourceAccessLink(objName:string,caseName:string,accessKey:string){
  return `${storage_backend_base}/${objName}?case_name=${caseName}&access_key=${accessKey}`
}
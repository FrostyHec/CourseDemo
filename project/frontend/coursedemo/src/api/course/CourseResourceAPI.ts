import { service_backend_base } from '@/utils/Constant';
import { AxiosAPI } from '@/utils/APIUtils';
import axios from 'axios'

/////////////////////   RESOURCE   ///////////////////////////////

export interface ResourceEntity {
  resource_id: number;
  chapter_id: number;
  resource_name: string;
  suffix: string;
  file_name: string;
  resource_order: number,
  resource_version_name: string;
  resource_version_order: number;
  resource_type: ResourceType;
  student_can_download: boolean;
  created_at: Date;
  updated_at: Date;
}

export enum ResourceType {
  description = 'description',
  courseware = 'courseware',
  video = 'video',
  attachment = 'attachment'
}

// 上传资源
export async function uploadResourceCall(chapterId: number, resource: ResourceEntity, file: File) {
  const url = `${service_backend_base}/chapter/${chapterId}/resource`;
  const formData = new FormData();
  formData.append('data', JSON.stringify(resource));
  formData.append('file', file);

  const config=AxiosAPI.setAuthHeader();
  (config.headers as any)['Content-Type'] = 'multipart/form-data' // TODO check correctness
  return AxiosAPI.extractResult(await axios.post(url,formData,config))
}

// 获取资源元数据
export async function getResourceMetaDataCall(id: number) {
  const url = `${service_backend_base}/resource/${id}`;
  return AxiosAPI.authGet<ResourceEntity>(url);
}

// 更新资源元数据
export async function updateResourceMetadataCall(id: number, updatedResource: ResourceEntity) {
  const url = `${service_backend_base}/resource/${id}`;
  return AxiosAPI.authPut<null>(url, updatedResource);
}

// 删除资源
export async function deleteResourceCall(id: number) {
  const url = `${service_backend_base}/resource/${id}`;
  return AxiosAPI.authDelete<null>(url);
}

// 获取章节下的所有资源
export async function getResourcesByChapterCall(chapterId: number) {
  const url = `${service_backend_base}/chapter/${chapterId}/resource`;
  return AxiosAPI.authGet<{ content: ResourceEntity[] }>(url);
}

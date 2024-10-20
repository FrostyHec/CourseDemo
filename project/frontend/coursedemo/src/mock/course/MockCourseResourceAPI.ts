// Mock Func
import type { ResourceEntity } from '@/api/course/CourseResourceAPI'
import { service_backend_base } from '@/utils/Constant'
import { setMockFunc } from '@/utils/MockUtils'
import { APIResult, RequestType } from '@/utils/APIUtils'

// Mock 上传资源
export function mockUploadResourceCall(chapterId: number, data: APIResult<null>) {
  const url = `${service_backend_base}/chapter/${chapterId}/resource`;
  setMockFunc(url, RequestType.POST, null, () => data);
}

// Mock 获取资源元数据
export function mockGetResourceMetaDataCall(id: number, data: APIResult<ResourceEntity>) {
  const url = `${service_backend_base}/resource/${id}`;
  setMockFunc(url, RequestType.GET, null, () => data);
}

// Mock 更新资源元数据
export function mockUpdateResourceMetadataCall(id: number, data: APIResult<null>) {
  const url = `${service_backend_base}/resource/${id}`;
  setMockFunc(url, RequestType.PUT, null, () => data);
}

// Mock 删除资源
export function mockDeleteResourceCall(id: number, data: APIResult<null>) {
  const url = `${service_backend_base}/resource/${id}`;
  setMockFunc(url, RequestType.DELETE, null, () => data);
}

// Mock 获取章节下的所有资源
export function mockGetResourcesByChapterCall(chapterId: number, data: APIResult<{ content: ResourceEntity[] }>) {
  const url = `${service_backend_base}/chapter/${chapterId}/resource`;
  setMockFunc(url, RequestType.GET, null, () => data);
}

// Mock Functions
import { backend_base } from '@/utils/Constant';
import { setMockFunc } from '@/utils/MockUtils';
import { APIResult, RequestType } from '@/utils/APIUtils';
import type { ResourceCommentEntity } from '@/api/course/ResourceCommentAPI';

// Mock for adding a comment to a resource
export function mockAddCommentToResourceCall(resourceId: number, data: APIResult<null>) {
  const url = `${backend_base}/resource/${resourceId}/comment`;
  setMockFunc(url, RequestType.POST, null, () => data);
}

// Mock for adding a reply to a comment
export function mockAddReplyToCommentCall(commentId: number, data: APIResult<null>) {
  const url = `${backend_base}/resource/comment/${commentId}/comment`;
  setMockFunc(url, RequestType.POST, null, () => data);
}

// Mock for updating a comment
export function mockUpdateCommentCall(commentId: number, data: APIResult<null>) {
  const url = `${backend_base}/resource/comment/${commentId}`;
  setMockFunc(url, RequestType.PUT, null, () => data);
}

// Mock for deleting a comment
export function mockDeleteCommentCall(commentId: number, data: APIResult<null>) {
  const url = `${backend_base}/resource/comment/${commentId}`;
  setMockFunc(url, RequestType.DELETE, null, () => data);
}

// Mock for getting a comment
export function mockGetCommentCall(commentId: number, data: APIResult<ResourceCommentEntity>) {
  const url = `${backend_base}/resource/comment/${commentId}`;
  setMockFunc(url, RequestType.GET, null, () => data);
}

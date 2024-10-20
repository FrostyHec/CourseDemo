import { backend_base } from '@/utils/Constant';
import { AxiosAPI } from '@/utils/APIUtils';

/////////////////////   RESOURCE COMMENT   ///////////////////////////////

export interface ResourceCommentEntity {
  comment_id: number;
  resource_id: number;
  user_id: number;
  comment_text: string;
  created_at: Date;
  updated_at: Date;
}

// Add a comment to a resource
export async function addCommentToResourceCall(resourceId: number, comment: ResourceCommentEntity) {
  const url = `${backend_base}/resource/${resourceId}/comment`;
  return AxiosAPI.authPost<null>(url, comment);
}

// Add a reply to a comment
export async function addReplyToCommentCall(commentId: number, reply: ResourceCommentEntity) {
  const url = `${backend_base}/resource/comment/${commentId}/comment`;
  return AxiosAPI.authPost<null>(url, reply);
}

// Update a comment
export async function updateCommentCall(commentId: number, updatedComment: ResourceCommentEntity) {
  const url = `${backend_base}/resource/comment/${commentId}`;
  return AxiosAPI.authPut<null>(url, updatedComment);
}

// Delete a comment
export async function deleteCommentCall(commentId: number) {
  const url = `${backend_base}/resource/comment/${commentId}`;
  return AxiosAPI.authDelete<null>(url);
}

// Get a comment
export async function getCommentCall(commentId: number) {
  const url = `${backend_base}/resource/comment/${commentId}`;
  return AxiosAPI.authGet<ResourceCommentEntity>(url);
}

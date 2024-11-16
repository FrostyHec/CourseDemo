import { service_backend_base } from '@/utils/Constant';
import { AxiosAPI } from '@/utils/APIUtils';
import type {UserPublicInfoEntity} from "@/api/user/UserAPI";

/////////////////////   RESOURCE COMMENT   ///////////////////////////////

export interface ResourceCommentEntity {
  comment_id?: number
  resource_id: number
  user_id: number
  comment_text: string
  created_at: Date
  updated_at: Date
  comment_reply: number
}

export interface CommentResource {
  id: number
  commentId: number
  resourceName: string
  fileName: string
  suffix: string
}

export interface CommentResourceWithAccessKey {
  resourceEntity: CommentResource
  accessKey: string
}

export interface CommentWithUserAndFileAndAccessKey {
  commentId: number
  resourceId: number
  user: UserPublicInfoEntity
  commentText: string
  commentFiles: CommentResourceWithAccessKey[]
  commentReply: number
  createdAt: Date
  updatedAt: Date
}

// Add a comment to a resource
export async function addCommentToResourceCall(resourceId: number, comment: ResourceCommentEntity) {
  const url = `${service_backend_base}/resource/${resourceId}/comment`;
  return AxiosAPI.authPost<null>(url, comment);
}

// Add a reply to a comment
export async function addReplyToCommentCall(commentId: number, reply: ResourceCommentEntity) {
  const url = `${service_backend_base}/resource/comment/${commentId}/comment`;
  return AxiosAPI.authPost<null>(url, reply);
}

// Update a comment
export async function updateCommentCall(commentId: number, updatedComment: ResourceCommentEntity) {
  const url = `${service_backend_base}/resource/comment/${commentId}`;
  return AxiosAPI.authPut<null>(url, updatedComment);
}

// Delete a comment
export async function deleteCommentCall(commentId: number) {
  const url = `${service_backend_base}/resource/comment/${commentId}`;
  return AxiosAPI.authDelete<null>(url);
}

// Get a comment
export async function getCommentCall(commentId: number) {
  const url = `${service_backend_base}/resource/comment/${commentId}`;
  return AxiosAPI.authGet<ResourceCommentEntity>(url);
}

// Get all comments under a resource
export async function getResourceCommentsCall(resourceId: number) {
  const url = service_backend_base + '/resource/' + resourceId + '/comments'
  return AxiosAPI.authGet<{ content: CommentWithUserAndFileAndAccessKey[] }>(url, {})
}


export async function uploadFilesCall(commentId: number, commentResource: CommentResource) {
  const url = service_backend_base + '/resource/comment/' + commentId + '/file'
  return AxiosAPI.authPost<null>(url, commentResource)
}

export async function removeFilesCall(commentId: number, fileId: number) {
  const url = service_backend_base + `/resource/comment/${commentId}/file/${fileId}`
  return AxiosAPI.authDelete<null>(url, {})
}

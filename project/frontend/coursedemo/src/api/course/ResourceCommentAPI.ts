import { service_backend_base } from '@/utils/Constant';
import { AxiosAPI } from '@/utils/APIUtils';
import type {UserPublicInfoEntity} from "@/api/user/UserAPI";
import axios from "axios";

/////////////////////   RESOURCE COMMENT   ///////////////////////////////

export interface ResourceCommentEntity {
  comment_id: number
  resource_id: number
  user_id: number
  comment_text: string
  created_at: Date
  updated_at: Date
  comment_reply: number
}

export interface CommentResource {
  id: number
  comment_id: number
  resource_name: string
  file_name: string
  suffix: string
}

export interface CommentResourceWithAccessKey {
  resource_entity: CommentResource
  access_key: string
}

export interface CommentWithUserAndFileAndAccessKey {
  comment_id: number
  resource_id: number
  user: UserPublicInfoEntity
  comment_text: string
  comment_files: CommentResourceWithAccessKey[]
  comment_reply: number
  created_at: Date
  updated_at: Date
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


export async function uploadFilesCall(commentId: number, commentResource: CommentResource,file:File) {
  const url = service_backend_base + '/resource/comment/' + commentId + '/file'

  const formData = new FormData();
  formData.append('data', new Blob([JSON.stringify(commentResource)], {type: 'application/json'}));
  formData.append('file', file);

  const config=AxiosAPI.setAuthHeader();
  (config.headers as any)['Content-Type'] = 'multipart/form-data'
  return AxiosAPI.extractResult<null>(await axios.post(url,formData,config))
}

export async function removeFilesCall(commentId: number, fileId: number) {
  const url = service_backend_base + `/resource/comment/${commentId}/file/${fileId}`
  return AxiosAPI.authDelete<null>(url, {})
}

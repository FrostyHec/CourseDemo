
export interface ChatEntity {
    id: number;
    title: string;
    createdAt: Date;
    updatedAt: Date;
}

export interface TitleEntity {
    title: string;
}

export interface SingleChatMessage {
    role: Role;
    content: string;
}

export enum Role{
    user = 'user',
    assistant = 'assistant'
}

export interface ChatContext {
    messages: SingleChatMessage[];
}

export interface ChatMetadataList {
    chat_history: ChatEntity[];
}
import { service_backend_base } from '@/utils/Constant';
import { AxiosAPI } from '@/utils/APIUtils';

/////////////////////   LANGCHAIN   ///////////////////////////////

export async function sendChatCall(context: ChatContext) {
    const url = service_backend_base + '/langchain/chat';
    return AxiosAPI.authPost<ChatContext>(url, context);
}
 

export async function generateTitleCall(chatContext: ChatContext) {
    const url = service_backend_base + '/langchain/title';
    return AxiosAPI.authPost<TitleEntity>(url, chatContext);
}

export async function createNewChatCall(titleEntity: TitleEntity) {
    const url = service_backend_base + '/langchain/';
    return AxiosAPI.authPost<ChatEntity>(url, titleEntity);
}

export async function saveChatHistoryCall(context: ChatContext, id: number) {
    const url = service_backend_base + `/langchain/${id}`;
    return AxiosAPI.authPut<void>(url, context);
}

export async function getAllMyChatMetadataCall() {
    const url = service_backend_base + '/langchain/all';
    return AxiosAPI.authGet<ChatMetadataList>(url);
}

export async function getChatContentCall(id: number) {
    const url = service_backend_base + `/langchain/${id}`;
    return AxiosAPI.authGet<ChatContext>(url);
}

export async function setChatTitleCall(titleEntity: TitleEntity, id: number) {
    const url = service_backend_base + `/langchain/${id}/title`;
    return AxiosAPI.authPatch<ChatEntity>(url, titleEntity);
}

export async function deleteChatCall(id: number) {
    const url = service_backend_base + `/langchain/${id}`;
    return AxiosAPI.authDelete<ChatEntity>(url);
}
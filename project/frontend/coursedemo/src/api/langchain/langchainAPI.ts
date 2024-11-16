
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
    role: string;
    content: string;
}

export interface ChatContext {
    messages: SingleChatMessage[];
}

export interface ChatMetadataList {
    chatHistory: ChatEntity[];
}
import { service_backend_base } from '@/utils/Constant';
import { AxiosAPI } from '@/utils/APIUtils';

/////////////////////   LANGCHAIN   ///////////////////////////////

export async function sendChatCall(context: ChatContext) {
    const url = service_backend_base + '/langchain/chat';
    return AxiosAPI.authPost<ChatContext>(url, context);
}

// export async function sendChatAndGetFlowCall(context: ChatContext) { //TODO
//     const url = service_backend_base + '/langchain/chat/flow';
//     return AxiosAPI.authPost<void>(url, context, { responseType: 'stream' });
// }

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


<template>
  <div class="kimi-chat-container">
    <el-button id="chatButton" type="primary" circle @click="toggleChatWindow">
      <el-icon>chat</el-icon>
    </el-button>
    <el-dialog v-model="chatWindowVisible" :title="title.title" width="50%" custom-class="kimi-chat-dialog" @close="saveChat()">
      <el-button type="text" @click="saveChat();viewHistory()">查看历史对话</el-button>
      <div class="chat-messages">
        <!-- 使用 v-for 渲染 context.messages -->
        <div v-for="(message, index) in context.messages" :key="index" class="chat-message"
          :class="{'self': message.role === Role.user, 'other': message.role === Role.assistant}">
        {{ message.content }}
      </div>
      </div>
      <div class="chat-input-area">
        <el-input
          v-model="inputMessage.content"
          placeholder="Type your message..."
          @keyup.enter="sendMessage"
          class="chat-input"
        />
        <el-button type="primary" @click="sendMessage" class="send-button">Send</el-button>
      </div>
    </el-dialog>
    <el-dialog v-model="historyDialogVisible" title="历史对话" width="50%">
      <div class="history-pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="chatHistories.chat_history.length">
        </el-pagination>
      </div>
      <div v-for="chat in paginatedChatHistory" :key="chat.id" class="history-item" @click="selectChatHistory(chat)">
        {{ chat.title }}
        <el-button type="danger" icon="el-icon-delete" @click="deleteChatHistory(chat.id)" class="delete-button">删除记录</el-button> <!-- 阻止点击事件冒泡 -->
      </div>
    </el-dialog>
  </div>
</template>
  
<script setup lang="ts">
import { createNewChatCall, type SingleChatMessage, type ChatContext, type ChatEntity, type ChatMetadataList, type TitleEntity, getAllMyChatMetadataCall, sendChatCall, getChatContentCall, Role, saveChatHistoryCall, setChatTitleCall, generateTitleCall, deleteChatCall } from '@/api/langchain/langchainAPI';
import { computed, onMounted, ref } from 'vue';

const chatWindowVisible = ref(false);
const historyDialogVisible = ref(false);
const currentPage = ref(1);
const pageSize = ref(5);

const paginatedChatHistory = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return chatHistories.value.chat_history.slice(start, end);
});

const handleSizeChange = (newSize:number) => {
  pageSize.value = newSize;
};

const handleCurrentChange = (newPage:number) => {
  currentPage.value = newPage;
};

onMounted(async () => {
    getChatHistory;
});

const inputMessage = ref<SingleChatMessage>({
  role: Role.user,
  content: ''
});

let currentChat = ref<ChatEntity>({
  id: 1,
  title: 'New Chat',
  createdAt: new Date(),
  updatedAt: new Date()
});

let title = ref<TitleEntity>({
  title: 'new chat'
});

const context = ref<ChatContext>({
  messages: []
});

const deleteChatHistory = async (id:number) => {
  await deleteChatCall(id);
  if(id==currentChat.value.id){
    Refresh;
    const response = await createNewChatCall(title.value);
    currentChat.value = response.data;
    historyDialogVisible.value = false;
  }
  getChatHistory();
}

const chatHistories = ref<ChatMetadataList>(
  {
    chat_history: []
  }
);

const Refresh = () =>{
  title.value = {title: 'new chat'};
  inputMessage.value = { role: Role.user, content: '' };
  context.value = {messages:[]};
  isFirstMessage = true;
}

const getChatHistory = async () => {
  const response = await getAllMyChatMetadataCall()
  chatHistories.value = response.data;
}

let isFirstMessage = true;
let isFirstTitle = true;

const saveChat = async () =>{
  await setChatTitleCall(title.value,currentChat.value.id);
  await saveChatHistoryCall(context.value,currentChat.value.id);
  Refresh();
}

const toggleChatWindow = async () => {
  if (isFirstMessage) {
    const response = await createNewChatCall(title.value);
    currentChat.value = response.data;
    isFirstMessage = false;
  }
  chatWindowVisible.value = !chatWindowVisible.value;
};

const viewHistory = () => {
  getChatHistory();
  historyDialogVisible.value = true;
};

const sendMessage = async () => {
  if (inputMessage.value.content !== '') {
    context.value.messages.push(inputMessage.value);
    inputMessage.value = { role: Role.user, content: '' }; // 重置输入消息
    if(isFirstTitle){
      isFirstTitle = false;
      title.value = (await generateTitleCall(context.value)).data;
      currentChat.value.title = title.value.title;
    }
    const response = await sendChatCall(context.value);
    context.value.messages.push(response.data.messages[response.data.messages.length - 1]);
  }
};

const selectChatHistory = async (selectedChat: ChatEntity) => {
  currentChat.value = selectedChat;
  title.value.title = selectedChat.title;
  context.value = (await getChatContentCall(currentChat.value.id)).data;
  historyDialogVisible.value = false;
  chatWindowVisible.value = true;
};
</script>
  
  
<style scoped>
.kimi-chat-container {
  position: relative;
  width: 100%;
  height: 100%;
}

.history-pagination {
  margin-bottom: 10px;
  text-align: right;
}

#chatButton {
  position: fixed;
  bottom: 20px;
  right: 20px;
  background-color: #4a90e2;
  color: white;
  border-radius: 50%;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease;
}

#chatButton:hover {
  transform: scale(1.1);
}

.chat-messages {
  display: flex;
  flex-direction: column;
  height: 400px;
  overflow-y: auto;
  margin-bottom: 10px;
}

.chat-message {
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 5px;
  max-width: 70%;
  word-wrap: break-word;
  display: flex;
  align-items: center;
}

.self {
  margin-left: auto;
  background-color: #e0f7fa;
}

.other {
  margin-right: auto;
  background-color: #fff;
}

.chat-input-area {
  display: flex;
  align-items: center;
  width: 100%;
}

.chat-input {
  flex-grow: 1;
  margin-right: 10px;
}

.send-button {
  margin-left: 10px;
}

.kimi-chat-dialog {
  max-height: 60vh;
  overflow-y: auto;
}

.history-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #ccc;
  position: relative; /* 为删除按钮定位做准备 */
}

.delete-button {
  position: absolute;
  right: 10px; /* 紧贴右侧 */
  top: 0;
  bottom: 0;
  width: 100px; /* 按钮宽度 */
  display: flex; /* 使用Flexbox布局 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  height: 100%; /* 按钮高度填满父元素 */
}
</style>
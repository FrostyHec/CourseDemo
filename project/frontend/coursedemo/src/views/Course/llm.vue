<template>
  <div class="kimi-chat-container">
    <el-button id="chatButton" type="primary" circle @click="toggleChatWindow">
      <el-icon>chat</el-icon>
    </el-button>
    <el-dialog v-model="chatWindowVisible" title="Chat with Kimi" width="50%" custom-class="kimi-chat-dialog">
      <el-button type="text" @click="viewHistory">查看历史对话</el-button>
      <div class="chat-messages">
        <!-- 使用 v-for 渲染 context.messages -->
        <div v-for="message in context.messages" class="chat-message"
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
      </div>
    </el-dialog>
  </div>
</template>
  
<script setup lang="ts">
import { createNewChatCall, type SingleChatMessage, type ChatContext, type ChatEntity, type ChatMetadataList, type TitleEntity, getAllMyChatMetadataCall, sendChatCall, getChatContentCall, Role } from '@/api/langchain/langchainAPI';
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

const currentChat = ref<ChatEntity>({
  id: 1,
  title: 'New Chat',
  createdAt: new Date(),
  updatedAt: new Date()
});

const title = ref<TitleEntity>({
  title: 'new chat'
});

const context = ref<ChatContext>({
  messages: []
});

const chatHistories = ref<ChatMetadataList>(
  {
    chat_history: []
  }
);

const getChatHistory = async () => {
  const response = await getAllMyChatMetadataCall()
  chatHistories.value = response.data;
}

let isFirstMessage = true;

const toggleChatWindow = () => {
  if (isFirstMessage) {
    createNewChatCall(title.value);
    isFirstMessage = false;
  }
  chatWindowVisible.value = !chatWindowVisible.value;
};

const viewHistory = () => {
  getChatHistory();
  console.log(chatHistories);
  historyDialogVisible.value = true;
};

const sendMessage = async () => {
  if (inputMessage.value.content !== '') {
    context.value.messages.push(inputMessage.value);
    inputMessage.value = { role: Role.user, content: '' }; // 重置输入消息
    const response = await sendChatCall(context.value);
    context.value.messages.push(response.data.messages[response.data.messages.length - 1]);
  }
};

const selectChatHistory = async (selectedChat: { id: number; title: string, createdAt: Date, updatedAt: Date }) => {
  currentChat.value = selectedChat;
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
}

.self {
  align-self: flex-end;
  background-color: #e0f7fa;
}

.other {
  align-self: flex-start;
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
  /* ... 其他样式 ... */
  max-height: 60vh; /* 设置对话框的最大高度为视窗高度的60% */
  overflow-y: auto; /* 当内容超出时显示滚动条 */
}

.history-item {
  cursor: pointer;
  padding: 10px;
  border-bottom: 1px solid #ccc;
}
</style>
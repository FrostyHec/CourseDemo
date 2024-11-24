<template>
    <div class="kimi-chat-container">
      <el-button id="chatButton" type="primary" circle @click="toggleChatWindow">
        <el-icon>chat</el-icon>
      </el-button>
      <el-dialog v-model="chatWindowVisible" title="Chat with Kimi" width="50%" custom-class="kimi-chat-dialog">
        <el-button type="text" @click="viewHistory">查看历史对话</el-button>
        <div class="chat-messages">
          <div v-for="chat in chatHistories.chatHistory" :key="chat.id" class="chat-message">
            <div :class="{'self': chat.title, 'other': !chat.title}">
              {{ chat.title }}
            </div>
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
      <el-dialog v-model="historyDialogVisible" title="历史对话" width="50%" @click="getChatHistory">
        <div v-for="chat in chatHistories.chatHistory" :key="chat.id" class="history-item" @click="selectChatHistory(chat)">
          {{ chat.title }}
        </div>
      </el-dialog>
    </div>
  </template>
  
  <script setup lang="ts">
  import { createNewChatCall, type SingleChatMessage, type ChatContext, type ChatEntity, type ChatMetadataList, type TitleEntity, getAllMyChatMetadataCall, sendChatCall, getChatContentCall } from '@/api/langchain/langchainAPI';
import { ref } from 'vue';
  
  const chatWindowVisible = ref(false);
  const historyDialogVisible = ref(false);
  
  const inputMessage = ref<SingleChatMessage>(
    {
      role: '',
      content: ''
    }
  );

  const currentChat = ref<ChatEntity>({
    id: 1,
    title: 'New Chat',
    createdAt: new Date(),
    updatedAt: new Date()
  });

  const title = ref<TitleEntity>({
    title: 'new chat'
  })
  
  const context = ref<ChatContext>(
    {
      messages: []
    }
  );

  const chatHistories = ref<ChatMetadataList>(
    {
      chatHistory: []
    }
  );
  
  const getChatHistory = async () => {
    chatHistories.value = (await getAllMyChatMetadataCall()).data
  }

  let isFirstMessage = true; // 新增变量，用于跟踪是否是第一次发送消息

  const toggleChatWindow = () => {
  if (isFirstMessage) {
    createNewChatCall(title.value);
    isFirstMessage = false;
  }
  chatWindowVisible.value = !chatWindowVisible.value;
};
  
  const viewHistory = () => {
    historyDialogVisible.value = true;
    getChatHistory;
  };
  
  const sendMessage = () => {
    if (inputMessage.value.content !== '') {
      context.value.messages.push(inputMessage.value);
      sendChatCall(context.value);
      inputMessage.value ={role: '', content: ''};
    }
  };
  
  const selectChatHistory = async (selectedChat: { id: number; title: string, createdAt: Date,updatedAt:Date}) => {
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
  
  .kimi-chat-dialog {
    margin: 0 auto;
    display: flex;
    flex-direction: column;
    align-items: center;
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
  </style>
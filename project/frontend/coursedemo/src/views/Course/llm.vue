<template>
    <div class="kimi-chat-container">
      <el-button id="chatButton" type="primary" circle @click="toggleChatWindow">
        <el-icon>chat</el-icon>
      </el-button>
      <el-dialog v-model="chatWindowVisible" title="Chat with Kimi" width="50%" custom-class="kimi-chat-dialog">
        <el-button type="text" @click="viewHistory">查看历史对话</el-button>
        <div class="chat-messages">
          <div v-for="(msg, index) in currentChat.messages" :key="index" class="chat-message">
            <div :class="{'self': msg.self, 'other': !msg.self}">
              {{ msg.content }}
            </div>
          </div>
        </div>
        <div class="chat-input-area">
          <el-input
            v-model="inputMessage"
            placeholder="Type your message..."
            @keyup.enter="sendMessage"
            class="chat-input"
          />
          <el-button type="primary" @click="sendMessage" class="send-button">Send</el-button>
        </div>
      </el-dialog>
      <el-dialog v-model="historyDialogVisible" title="历史对话" width="50%">
        <div v-for="chat in chatHistories" :key="chat.id" class="history-item" @click="selectChatHistory(chat)">
          {{ chat.title }}
        </div>
      </el-dialog>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref } from 'vue';
  
  const chatWindowVisible = ref(false);
  const historyDialogVisible = ref(false);
  const inputMessage = ref('');
  const currentChat = ref({
    id: 1,
    title: 'New Chat',
    messages: [{ content: 'Hello, this is Kimi. How can I assist you today?', self: false }]
  });
  const chatHistories = ref([
    { id: 1, title: 'Chat with Kimi' },
    { id: 2, title: 'Another Conversation' }
  ]);
  
  const toggleChatWindow = () => {
    chatWindowVisible.value = !chatWindowVisible.value;
  };
  
  const viewHistory = () => {
    historyDialogVisible.value = true;
  };
  
  const sendMessage = () => {
    if (inputMessage.value.trim() !== '') {
      currentChat.value.messages.push({ content: inputMessage.value, self: true });
      inputMessage.value = '';
      setTimeout(() => {
        currentChat.value.messages.push({ content: 'Understood. Let me think about it.', self: false });
      }, 1000);
    }
  };
  
  const selectChatHistory = (selectedChat: { id: number; title: string }) => {
    currentChat.value = selectedChat;
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
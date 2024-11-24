<template>
  <div class="live-stream-viewer">
    <div class="header">
      <h1>Live Stream Viewer</h1>
      <div class="buttons">
        <el-button type="primary" @click="getStreamName">Get Stream</el-button>
        <el-button type="success" @click="playVideo">Play Video</el-button>
      </div>
    </div>
    <div class="main-content">
      <div class="video-container">
        <video ref="videoElement" controls autoplay></video>
      </div>
      <div class="chat-room">
        <div class="chat-messages">
          <div v-for="(message, index) in messages" :key="index">{{ message }}</div>
        </div>
        <div class="chat-input">
          <input type="text" v-model="newMessage" @keyup.enter="sendMessage" placeholder="Type a message...">
          <el-button type="primary" @click="sendMessage">Send</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import flvjs from 'flv.js';
import { ElButton } from 'element-plus';
import { chatRoomAPI } from '@/api/liveStream/ChatRoomAPI';
import type { ReceivedMessage, SendMessage } from '@/api/livestream/ChatRoomAPI';


const courseId = 1;
const user_id = 1;
const baseUrl = 'http://localhost:9977';
const videoUrl = 'http://localhost:8088'; // 流媒体服务器的url
let streamName = '';

const videoElement = ref<HTMLVideoElement | null>(null);
const messages = ref<string[]>([]);
const newMessage = ref('');

// 弹幕消息处理函数
const handleBarrageMessage = (message: ReceivedMessage) => {
    messages.value.push(`${message.from_user.first_name} ${message.from_user.last_name}: ${message.content}`);
};

// 连接 WebSocket
const connectWebSocket = () => {
    chatRoomAPI.connectWebSocket('liveStream', user_id, handleBarrageMessage);
};

const getStreamName = async () => {
  try {
    const response = await fetch(`${baseUrl}/api/v1/course/${courseId}/live-stream/push`, {
      method: 'GET',
      headers: {
        'X-Forwarded-User': JSON.stringify({
          authStatus: 'PASS',
          authInfo: {
            userID: 1,
          },
         }),
      },
    });
    const data = await response.json();
    if (response.ok && data.code === 200) {
      streamName = data.data.name;
      alert(`streamName is: ${streamName}`);
      connectWebSocket(); // 获取流名称后连接 WebSocket
    } else {
      alert(`Failed to get info: ${response}`);
    }
  } catch (error) {
    console.error('Error fetching stream name:', error);
  }
};

const playVideo = async () => {
  try {
    if (flvjs.isSupported()) {
      const flvPlayer = flvjs.createPlayer({
        type: 'flv',
        url: `${videoUrl}/live?app=course&stream=${streamName}`,
        cors: true,
        requestModifier: (request: { headers: { [x: string]: string; }; }, _flv: any) => {
          request.headers['X-Forwarded-User'] = JSON.stringify({
            authStatus: 'PASS',
            authInfo: {
              userID: 1,
            },
          });
        },
      });
      flvPlayer.attachMediaElement(videoElement.value);
      flvPlayer.load();
      flvPlayer.play();
    } else {
      console.error('FLV format is not supported in this browser.');
    }
  } catch (error) {
    console.error('Error playing video:', error);
  }
};

const sendMessage = () => {
  if (newMessage.value.trim() !== '') {
    const sendMessageData: SendMessage = {
      target: 'liveStream',
      content: newMessage.value,
    };
    chatRoomAPI.sendMessage(sendMessageData);
    newMessage.value = '';
  }
};
</script>

<style scoped>
/* Global styles */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body, html {
  height: 100%;
  font-family: 'Arial', sans-serif;
  background-color: #f4f4f5;
}

/* Live stream viewer styles */
.live-stream-viewer {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #282c34;
  color: white;
}

.header {
  padding: 1rem;
  background-color: #20232a;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

.header h1 {
  margin: 0;
  font-size: 1.5rem;
}

.buttons {
  display: flex;
  gap: 10px;
}

.el-button {
  padding: 12px 20px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  color: #fff;
  border: none;
  outline: none;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
  background-image: linear-gradient(45deg, #ff9a9e 0%, #fad0c4 99%);
  box-shadow: 0 5px 15px rgba(255, 155, 158, 0.4);
}

.el-button:hover {
  background-image: linear-gradient(45deg, #fad0c4 0%, #ff9a9e 99%);
  transform: translateY(-2px);
}

.el-button:active {
  box-shadow: 0 5px 15px rgba(255, 155, 158, 0.6);
  transform: translateY(-1px);
}

/* Style for the Get Stream button specifically */
.el-button.get-stream-button {
  background-image: linear-gradient(45deg, #9e9eff 0%, #c4c4ff 99%);
  box-shadow: 0 5px 15px rgba(158, 158, 255, 0.4);
}

.el-button.get-stream-button:hover {
  background-image: linear-gradient(45deg, #c4c4ff 0%, #9e9eff 99%);
}

/* Style for the Play Video button specifically */
.el-button.play-video-button {
  background-image: linear-gradient(45deg, #9effa5 0%, #c4ffda 99%);
  box-shadow: 0 5px 15px rgba(158, 255, 158, 0.4);
}

.el-button.play-video-button:hover {
  background-image: linear-gradient(45deg, #c4ffda 0%, #9effa5 99%);
}

.main-content {
  display: flex;
  flex-grow: 1;
  padding: 1rem;
  gap: 1rem;
}

.video-container {
  flex-grow: 1;
  position: relative;
  background-color: #000;
}

.video-container video {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.chat-room {
  width: 300px;
  background-color: #20232a;
  display: flex;
  flex-direction: column;
}

.chat-messages {
  flex-grow: 1;
  padding: 1rem;
  overflow-y: auto;
  background-color: rgba(0, 0, 0, 0.5); /* 半透明背景 */
}

.chat-messages div {
  color: #fff;
  margin-bottom: 5px;
  font-size: 14px;
  opacity: 0.8;
  transition: all 0.5s;
}

.chat-input {
  display: flex;
  padding: 0.5rem;
  background-color: #373c49;
}

.chat-input input {
  flex-grow: 1;
  padding: 0.5rem;
  border: 1px solid #4a4e69;
  border-radius: 4px;
}

.chat-input button {
  padding: 0.5rem 1rem;
  border-radius: 4px;
  background-color: #67c23a;
  color: white;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s;
}

.chat-input button:hover {
  background-color: #85ce61;
}

/* Responsive styles */
@media (max-width: 768px) {
  /* Responsive adjustments */
}
</style>
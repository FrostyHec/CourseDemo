<template>
  <div class="live-stream-viewer">
    <div class="header">
      <h1>Live Stream Viewer</h1>
      <div class="buttons">
        <el-button @click="getStreamName">Get Stream</el-button>
        <el-button @click="playVideo">Play Video</el-button>
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
          <el-button @click="sendMessage">Send</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import flvjs from 'flv.js';
import { ElButton } from 'element-plus';

const courseId = '1';
const baseUrl = 'http://localhost:9977';
const videoUrl = 'http://localhost:8088'; // 流媒体服务器的url
let streamName = '';

const videoElement = ref<HTMLVideoElement | null>(null);
const messages = ref<string[]>([]);
const newMessage = ref('');

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
    messages.value.push(`You: ${newMessage.value}`);
    newMessage.value = '';
  }
};
</script>

<style scoped>
.live-stream-viewer {
  display: flex;
  flex-direction: column;
  align-items: center;
  font-family: Arial, sans-serif;
  height: 100vh;
  margin: 0;
}

.header {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #f5f5f5;
}

.buttons {
  display: flex;
  gap: 10px;
}

.main-content {
  display: flex;
  flex: 1;
  width: 100%;
}

.video-container {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #000;
}

.video-container video {
  width: 80%;
  height: auto;
}

.chat-room {
  width: 300px;
  background-color: #f5f5f5;
  border-left: 1px solid #ddd;
}

.chat-messages {
  height: calc(100% - 60px);
  overflow-y: auto;
  padding: 10px;
  margin-bottom: 10px;
}

.chat-input {
  display: flex;
  align-items: center;
  padding: 10px;
}

.chat-input input {
  flex-grow: 1;
  margin-right: 10px;
  padding: 5px;
}

.chat-input button {
  padding: 5px 10px;
}
</style>
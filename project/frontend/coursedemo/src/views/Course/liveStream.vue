<template>
  <div class="live-stream-viewer">
    <div class="header">
      <h1>Live Stream Viewer</h1>
      <div class="buttons">
        <el-button type="primary" @click="router.back()">返回课程页面</el-button>
        <el-button type="primary"  v-show="showStream">Get Stream</el-button>
      </div>
    </div>
    <div class="main-content">
      <div class="video-container">
        <video ref="videoElement" controls autoplay></video>
        <!-- 当前无直播的提示 -->
        <div v-if="!isLive" class="no-live-stream" >当前无直播</div>
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
import { onMounted, onUnmounted, ref } from 'vue';
import { ElButton } from 'element-plus';
import { chatRoomAPI } from '@/api/course/liveStream/ChatRoomAPI';
import type { ReceivedMessage, SendMessage } from '@/api/course/livestream/ChatRoomAPI';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { UserType } from '@/api/user/UserAPI';
import { getFlvConfig, getLivestreamPullUrl, getLivestreamPushUrl, getPullName, getPushName } from '@/api/course/livestream/LivestreamAPI';
import flvjs from 'flv.js';

const router = useRouter();
const showStream = ref(true)
const authStore = useAuthStore()
const user_id = authStore.user.user_id;
const route = useRoute()
const courseId = Number(route.params.course_id);
let streamName = '';

let videoElement = ref<HTMLVideoElement>();
const messages = ref<string[]>([]);
const newMessage = ref('');
const isLive = ref(true); // 用于跟踪是否有直播
let intervalId: NodeJS.Timeout | null = null;

onMounted(async () => {
  intervalId = setInterval(async () => {
    if (authStore.user.role !== UserType.STUDENT) return; // 只有学生需要请求拉流
    const pullNameResponse = await getPullName(courseId);
    if (pullNameResponse && pullNameResponse.data) {
      streamName = pullNameResponse.data.name;
      const pullUrl = getLivestreamPullUrl(streamName);
      if(!isLive)setupFlvPlayer(pullUrl);
      isLive.value = true; // 有直播
    } else {
      isLive.value = false; // 无直播
    }
  }, 5000);
  if (authStore.user.role == UserType.TEACHER) {
    // 获取推流
    const pushNameResponse = await getPushName(courseId);
    if (pushNameResponse && pushNameResponse.data) {
      streamName = pushNameResponse.data.name;
      const pushUrl = getLivestreamPushUrl(streamName);
      alert(pushUrl); // 推流URL，供主播使用
      const pullUrl = getLivestreamPullUrl(streamName);
      setupFlvPlayer(pullUrl);
    }
  }

});

onUnmounted(() => {
  if (intervalId) {
    clearInterval(intervalId); // 清除定时器
  }
});

function setupFlvPlayer(url: string) {
  if (flvjs.isSupported() && videoElement.value) {
    const flvPlayer = flvjs.createPlayer(getFlvConfig(url));
    flvPlayer.attachMediaElement(videoElement.value);
    flvPlayer.on(flvjs.Events.ERROR, () => {
      console.error('Error playing FLV stream.');
      isLive.value = false;
    });
    flvPlayer.load();
    flvPlayer.play();
    isLive.value = true;
  } else {
    console.error('FLV format is not supported in this browser.');
    isLive.value = false;
  }
}

// 弹幕消息处理函数
const handleBarrageMessage = (message: ReceivedMessage) => {
    messages.value.push(`${message.from_user.first_name} ${message.from_user.last_name}: ${message.content}`);
};

// 连接 WebSocket
const connectWebSocket = () => {
    chatRoomAPI.connectWebSocket('liveStream', user_id, handleBarrageMessage);
};

// const getStreamName = async () => {
//   try {
//     const response = await fetch(`${baseUrl}/api/v1/course/${courseId}/live-stream/push`, {
//       method: 'GET',
//       headers: {
//         'X-Forwarded-User': JSON.stringify({
//           authStatus: 'PASS',
//           authInfo: {
//             userID: user_id,
//           },
//          }),
//       },
//     });
//     const data = await response.json();
//     if (response.ok && data.code === 200) {
//       streamName = data.data.name;
//       console.log(`streamName is: ${streamName}`);
//       connectWebSocket(); // 获取流名称后连接 WebSocket
//     } else {
//       alert(`Failed to get info: ${response}`);
//     }
//   } catch (error) {
//     console.error('Error fetching stream name:', error);
//   }
// };

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

.no-live-stream {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #fff;
  font-size: 24px;
  text-align: center;
  z-index: 10; /* 增加z-index属性，确保提示显示在最顶层 */
}

/* Responsive styles */
@media (max-width: 768px) {
  /* Responsive adjustments */
}
</style>
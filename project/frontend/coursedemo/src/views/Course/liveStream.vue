<template>
  <div class="live-stream-viewer">
    <div class="header">
      <h1>Live Stream Viewer</h1>
      <div class="buttons">
        <el-button class = "el-button" type="primary" @click="router.back()">返回课程页面</el-button>
        <el-button class = "el-button" type="primary" v-show="showStream" @click="HandleGetPushName()">Get Stream</el-button>
      </div>
    </div>
    <div class="main-content">
      <div class="video-container">
        <video ref="videoElement" controls autoplay></video>
        <vue-danmaku
          v-model:danmus="danmus"
          :speeds="50"
          :use-slot="true"
          style="position: absolute; top: 5%; left: 0; width: 100%; height: 50%;"
        >
          <template v-slot:dm="{ danmu }">
            <div class="custom-danmu">
              <span>{{ danmu }}</span>
            </div>
          </template>
        </vue-danmaku>
        <!-- 当前无直播的提示 -->
        <div v-if="!isLive" class="no-live-stream">当前无直播</div>
      </div>
      <div class="chat-room">
        <div class="chat-messages">
          <div v-for="(message, index) in messages" :key="index">{{ message }}</div>
        </div>
        <div class="chat-input">
          <input type="text" v-model="newMessage" @keyup.enter="sendMessage" placeholder="Type a message...">
          <el-button class = "el-button" type="primary" @click="sendMessage">Send</el-button>
        </div>
      </div>
    </div>
    <el-dialog
          title=""
          v-model="pushVisible"
          @close="pushVisible = false"
        >
          <span>推流地址: {{ pushUrl }}</span>
          <p></p>
          <span>密钥: {{ pushName }}</span>
    </el-dialog>
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
import vueDanmaku from 'vue3-danmaku'

const router = useRouter();
const showStream = ref(true);
const authStore = useAuthStore();
const user_id = authStore.user.user_id;
const route = useRoute();
const courseId = Number(route.params.course_id);
let streamName = '';
const videoElement = ref<HTMLVideoElement | null>(null);
const messages = ref<string[]>([]);
const newMessage = ref('');
const isLive = ref(false);
const pushVisible = ref(false);
let intervalId: NodeJS.Timeout | null = null;

const danmus = ref<string[]>([]);

onMounted(async () => {
  connectWebSocket();
  console.log(videoElement.value);
  if (videoElement.value) {
    intervalId = setInterval(async () => {
      if (authStore.user.role == UserType.STUDENT) {
        showStream.value = false;
      };
      const pullNameResponse = await getPullName(courseId);
      if (pullNameResponse && pullNameResponse.data) {
        streamName = pullNameResponse.data.name;
        try {
          const pullUrl = getLivestreamPullUrl(streamName);
          if (!isLive.value) setupFlvPlayer(pullUrl);
          isLive.value = true;
        } catch (error) {}
      } else {
        isLive.value = false;
      }
    }, 5000);
  }
});

let pushUrl = ref('');
let pushName = ref('');

const HandleGetPushName = async () =>{
  pushUrl.value = getLivestreamPushUrl();
  pushName.value = (await getPushName(courseId)).data.name;
  pushVisible.value = true;
}

onUnmounted(() => {
  if (intervalId) {
    clearInterval(intervalId);
  }
});

function setupFlvPlayer(url: string) {
  if (flvjs.isSupported() && videoElement.value) {
    const flvPlayer = flvjs.createPlayer(getFlvConfig(url));
    flvPlayer.attachMediaElement(videoElement.value);
    flvPlayer.on(flvjs.Events.ERROR, () => {
      console.error('Error playing FLV stream.');
      flvPlayer.destroy();
      isLive.value = false;
    });
    flvPlayer.on(flvjs.Events.LOADING_COMPLETE,()=>{
      console.log('liveStream Complete!')
      flvPlayer.destroy();
      isLive.value = false;
    })
    flvPlayer.load();
    flvPlayer.play();
    isLive.value = true;
  } else {
    console.error('FLV format is not supported in this browser.');
    isLive.value = false;
  }
}

const handleBarrageMessage = (message: ReceivedMessage) => {
  messages.value.push(`${message.from_user.first_name} ${message.from_user.last_name}: ${message.content}`);
  danmus.value.push(`${message.from_user.first_name} ${message.from_user.last_name}: ${message.content}`);
};

const connectWebSocket = () => {
  chatRoomAPI.connectWebSocket(String(courseId), user_id, handleBarrageMessage);
};

const sendMessage = () => {
  if (newMessage.value.trim() !== '') {
    const sendMessageData: SendMessage = {
      target: -1,
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
  font-family: 'Arial', sans-serif; /* 使用Arial字体 */
  background-color: #ffffff; /* 亮色调背景 */
}

/* Live stream viewer styles */
.live-stream-viewer {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f4f4f5; /* 亮色调背景 */
  color: #333; /* 深色文字 */
}

.header {
  padding: 1rem;
  background-color: #e0e0e0; /* 亮色调背景 */
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* 轻微阴影 */
}

.header h1 {
  margin: 0;
  font-size: 1.5rem;
}

.buttons {
  display: flex;
  justify-content: space-between; /* 使按钮分别靠近父容器的两侧 */
  align-items: center;
}

.el-button {
  min-width: 60px; /* 设置按钮的最小宽度 */
  height: 30px; /* 设置按钮的高度 */
  line-height: 40px; /* 使文本垂直居中 */
  padding: 0 20px; /* 调整按钮的内边距 */
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  color: #fff;
  border: none;
  outline: none;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
  background-image: linear-gradient(45deg, #67c23a 0%, #85ce61 99%);
  box-shadow: 0 5px 15px rgba(103, 194, 58, 0.4);
  text-align: center; /* 使文本居中 */
  margin: 5px; /* 为按钮添加外边距 */
}

.el-button:hover {
  background-image: linear-gradient(45deg, #85ce61 0%, #67c23a 99%);
  transform: translateY(-2px);
}

.el-button:active {
  box-shadow: 0 5px 15px rgba(103, 194, 58, 0.6);
  transform: translateY(-1px);
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
  background-color: #fff; /* 亮色调背景 */
}

.video-container video {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.custom-danmu {
  font-weight: bold; /* 字体加粗 */
  color: #fff; /* 字体颜色 */
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5); /* 字体阴影 */
  background-color: rgba(0, 0, 0, 0.5); /* 背景颜色 */
  border-radius: 18px; /* 圆角 */
  padding: 2px 10px; /* 内边距 */
  white-space: nowrap; /* 防止换行 */
  margin: 2px; /* 弹幕间距 */
  display: inline-block; /* 使div像行内元素一样显示 */
}

.chat-room {
  width: 300px;
  background-color: #e0e0e0; /* 亮色调背景 */
  display: flex;
  flex-direction: column;
}

.chat-messages {
  flex-grow: 1;
  padding: 1rem;
  overflow-y: auto; /* 允许垂直滚动 */
  max-height: 760px; /* 设置最大高度，根据需要调整 */
  background-color: rgba(255, 255, 255, 0.8); /* 亮色调背景 */
}

.chat-messages div {
  color: #333;
  margin-bottom: 5px;
  font-size: 14px;
  opacity: 0.8;
  transition: all 0.5s;
}

.chat-input {
  display: flex;
  padding: 0.5rem;
  background-color: #d9d9d9; /* 亮色调背景 */
}

.chat-input input {
  flex-grow: 1;
  padding: 0.5rem;
  border: 1px solid #ccc;
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
  color: #333;
  font-size: 24px;
  text-align: center;
  z-index: 10;
}

/* Responsive styles */
@media (max-width: 768px) {
  .live-stream-viewer {
    flex-direction: column;
  }

  .chat-room {
    width: 100%;
  }
}
</style>
// 在SSEHandler.ts中添加一个全局状态来存储公告信息
import { ElMessage } from 'element-plus';
import { ref } from 'vue';

// 定义一个响应式的公告信息数组
const announcementMessages = ref<string[]>([]);
let announcementTimer: number | null = null;
export const getAnnouncementMessages = () => announcementMessages.value;

export const handleAnnouncement = (message: string) => {
  ElMessage({
    message: message
  })
  // announcementMessages.value.push(message);
  // // 如果已经有一个定时器存在，先清除
  // if (announcementTimer !== null) {
  //   clearTimeout(announcementTimer);
  // }
  // // 设置一个新的10秒计时器来清空公告消息
  // announcementTimer = window.setTimeout(() => {
  //   announcementMessages.value = [];
  // }, 10000); // 10000毫秒 = 10秒
};
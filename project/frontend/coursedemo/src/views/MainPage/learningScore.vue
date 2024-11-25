<template>
    <div id="app">
      <el-header style="padding: 0; height: auto;">
        <base-header />
      </el-header>
      <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal">
        <el-menu-item index="1" @click="navigateTo('/MainPage')">全部课程</el-menu-item>
        <el-menu-item index="2" @click="navigateTo('/MainPage/student')">我的课程</el-menu-item>
        <el-menu-item index="3" @click="activeIndex = 'mall'">学习积分</el-menu-item>      
      </el-menu>
      <el-container class="container">
        <el-aside width="200px">
          <el-menu>
            <el-menu-item index="1-1" @click="activeIndex = '3';getMyMedal">我的勋章</el-menu-item>
            <el-menu-item index="1-2" @click="activeIndex = 'mall';getScoreMall">积分商城</el-menu-item>
            <el-menu-item index="1-3" @click="activeIndex = 'history';getScoreHistory">积分变化历史</el-menu-item>
          </el-menu>
        </el-aside>
        <el-main class="main">
          <div v-if="activeIndex === 'history'" class="score-history-container">
            <el-card v-for="(record, index) in scoreHistory" :key="record.record_id" class="score-history-card">
              <div>记录{{ index + 1 }}: {{ new Date(record.created_at).toLocaleString() }}  操作: {{ record.action_type }}</div>
              <div>变更积分:{{ record.changed_score > 0 ? `+${record.changed_score}` : record.changed_score }} 剩余积分:{{ record.remain_score }}</div>
            </el-card>
          </div>
          <div v-if="activeIndex === 'mall'" class="mall-container">
          <el-menu :default-active="activeTab" class="mall-menu" mode="horizontal">
            <el-menu-item index="1" @click="activeTab = '1'">兑换勋章</el-menu-item>
            <el-menu-item index="2" @click="activeTab = '2'">兑换chat使用</el-menu-item>
          </el-menu>
            <div class="mall-body">
              <el-card v-for="(item, index) in mallItems" :key="index" class="mall-item-card">
                <img :src="item.image" class="mall-item-image">
                <div>{{ item.badge_name }}</div>
                <el-button type="primary" @click="getMedalIndex(index);exchangeMetalVisible = true">兑换</el-button>
              </el-card>
            </div>
          </div>
          <div v-if="activeIndex === '3'" class="medals-container">
            <el-card v-for="(medal, index) in medals" :key="index" class="medal-card">
              <img :src="medal.image"  class="medal-image">
              <div>{{ medal.badge_name }}</div>
            </el-card>
          </div>
        </el-main>
        <el-dialog
          title="兑换勋章"
          v-model="exchangeMetalVisible"
        >
          <span>确定要兑换这个勋章吗？</span>
          <template #footer>
            <el-button @click="exchangeMetalVisible = false">取消</el-button>
            <el-button type="primary" @click="exchangeMetalVisible=false;exchangeItem(medalIndex);">确认加入</el-button>
          </template>
        </el-dialog>
      </el-container>
    </div>
  </template>
  
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import BaseHeader from '@/layouts/BaseHeader.vue';
import { useRouter } from 'vue-router';
import { buyBadgeCall, ConsumeActionType, getMyCanBuyBadgeCall, getMyHistoryCall, type BadgeInfo, type ConsumeRecord } from '@/api/market/MarketAPI';

const router = useRouter();
const activeIndex = ref('3');
const activeTab = ref('1'); 
const exchangeMetalVisible = ref(false);
const medalIndex = 1;

const getMedalIndex = (index:number)=>{
  const medalIndex = index;
}

const medals = ref<BadgeInfo[]>([
  { user_id: 1, badge_id: 1, badge_name: '勤学好问', image: '../../assets/Medal/medal1.png', market_score: 100 },
  { user_id: 1, badge_id: 2, badge_name: '作业达人', image: '../../assets/Medal/medal2.png', market_score: 100 },
  { user_id: 1, badge_id: 3, badge_name: '课堂之星', image: '../../assets/Medal/medal3.png', market_score: 100 },
  { user_id: 1, badge_id: 4, badge_name: '思考先锋', image: '../../assets/Medal/medal4.png', market_score: 100 },
  { user_id: 1, badge_id: 5, badge_name: '团队合作者', image: '../../assets/Medal/medal5.png', market_score: 100 },
  { user_id: 1, badge_id: 6, badge_name: '创新思维', image: '../../assets/Medal/medal6.png', market_score: 100 },
  { user_id: 1, badge_id: 7, badge_name: '学习领袖', image: '../../assets/Medal/medal7.png', market_score: 100 },
  { user_id: 1, badge_id: 8, badge_name: '知识探索者', image: '../../assets/Medal/medal8.png', market_score: 100 },
  { user_id: 1, badge_id: 9, badge_name: '时间管理大师', image: '../../assets/Medal/medal9.png', market_score: 100 },
  { user_id: 1, badge_id: 10, badge_name: '作业完美主义者', image: '../../assets/Medal/medal10.png', market_score: 100 },
  { user_id: 1, badge_id: 11, badge_name: '课堂参与奖', image: '../../assets/Medal/medal11.png', market_score: 100 },
  { user_id: 1, badge_id: 12, badge_name: '问题解决者', image: '../../assets/Medal/medal12.png', market_score: 100 },
  { user_id: 1, badge_id: 13, badge_name: '学习进步奖', image: '../../assets/Medal/medal13.png', market_score: 100 },
  { user_id: 1, badge_id: 14, badge_name: '作业创新奖', image: '../../assets/Medal/medal14.png', market_score: 100 },
  { user_id: 1, badge_id: 15, badge_name: '课堂贡献奖', image: '../../assets/Medal/medal15.png', market_score: 100 },

]);
const mallItems = ref<BadgeInfo[]>([
  {  user_id: 1, badge_id: 16, badge_name: '学习毅力奖', image: '../../assets/Medal/medal16.png', market_score: 100 },
  {  user_id: 1, badge_id: 17, badge_name: '作业勤奋奖', image: '../../assets/Medal/medal17.png', market_score: 100 },
  {  user_id: 1, badge_id: 18, badge_name: '学习热情奖', image: '../../assets/Medal/medal18.png', market_score: 100 },
  {  user_id: 1, badge_id: 19, badge_name: '作业创意奖', image: '../../assets/Medal/medal19.png', market_score: 100 },
  {  user_id: 1, badge_id: 20, badge_name: '课堂互动奖', image: '../../assets/Medal/medal20.png', market_score: 100 },
  {  user_id: 1, badge_id: 21, badge_name: '作业卓越奖', image: '../../assets/Medal/medal21.png', market_score: 100 },
  {  user_id: 1, badge_id: 22, badge_name: '作业精确奖', image: '../../assets/Medal/medal22.png', market_score: 100 },
  {  user_id: 1, badge_id: 23, badge_name: '课堂活力奖', image: '../../assets/Medal/medal23.png', market_score: 100 },
  {  user_id: 1, badge_id: 24, badge_name: '课堂全勤奖', image: '../../assets/Medal/medal24.png', market_score: 100 },
  {  user_id: 1, badge_id: 25, badge_name: '初学者', image: '../../assets/Medal/medal25.png', market_score: 100 },
  {  user_id: 1, badge_id: 26, badge_name: '勤奋学者', image: '../../assets/Medal/medal26.png', market_score: 100 },
  {  user_id: 1, badge_id: 27, badge_name: '努力学者', image: '../../assets/Medal/medal27.png', market_score: 100 },
  {  user_id: 1, badge_id: 28, badge_name: '渐入佳境', image: '../../assets/Medal/medal28.png', market_score: 100 },
  {  user_id: 1, badge_id: 29, badge_name: '一代宗师', image: '../../assets/Medal/medal29.png', market_score: 100 },
  {  user_id: 1, badge_id: 30, badge_name: '终极大师', image: '../../assets/Medal/medal30.png', market_score: 100 }
]);

const navigateTo = (path: string) => {
  router.push(path); 
};

onMounted(() => {
  getMyMedal;
  getScoreMall;
  getScoreHistory;
});

const getMyMedal = async () => {
  medals.value = (await getMyCanBuyBadgeCall()).data.content;
};

const getScoreMall = async () => {
  mallItems.value = (await getMyCanBuyBadgeCall()).data.content;
};

const getScoreHistory = async () => {
  try {
    const response = await getMyHistoryCall();
    scoreHistory.value = response.data.content;
  } catch (error) {
    console.error('Error fetching score history:', error);
  }
};

const exchangeItem = (index: number) => {
  buyBadgeCall(mallItems.value[index]);
  medals.value.push(mallItems.value[index]);
  mallItems.value.splice(index, 1);
};

const scoreHistory = ref<ConsumeRecord[]>([
  {
    record_id: 1,
    action_type: ConsumeActionType.buy_badge,
    action_param: '',
    changed_score: 20,
    remain_score: 100,
    created_at: new Date()
  }
]);

</script>
  

<style>
#app {
font-family: Avenir, Helvetica, Arial, sans-serif;
-webkit-font-smoothing: antialiased;
-moz-osx-font-smoothing: grayscale;
text-align: center;
color: #0f151b;
margin-top: 0px;
}
.main {
  flex: 1; /* 占满剩余空间 */
  margin-right: 0px; /* 与右边框的间隔 */
  overflow-x: hidden; /* 隐藏水平滚动条 */
  overflow-y: auto; /* 允许垂直滚动 */
}
.mall-menu {
margin-bottom: 20px;
}

.mall-body {
  display: flex;
  flex-wrap: wrap; /* 允许换行 */
  justify-content: center; /* 居中对齐 */
  gap: 20px; /* 添加间隔 */
}

.mall-item-card {
  width: 200px; /* 设置卡片宽度，可以根据需要调整 */
  margin: 0; /* 移除卡片的外边距，因为 gap 已经提供了间隔 */
  text-align: center;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}
.container {
margin-top: 10px;
margin-left: 10px;
margin-right: auto;
}

.medals-container {
display: flex;
flex-wrap: wrap; /* 允许换行 */
justify-content: flex-start; /* 左对齐 */
align-items: center; /* 垂直居中 */
}

.medal-card {
width: 100px; /* 设置卡片宽度，高度将自动设置为相同 */
margin: 10px; /* 卡片之间的间距 */
text-align: center;
}

.medal-image {
width: 100%; /* 图片宽度占满卡片 */
height: auto;
border-radius: 50%; /* 圆形图片 */
}


.score-history-container {
  display: flex;
  flex-direction: column; /* 设置为列方向 */
  align-items: flex-start; /* 左对齐 */
}

.score-history-card {
  width: 100%; /* 让卡片宽度占满容器 */
  margin-bottom: 20px; /* 卡片之间的间距 */
  border: 1px solid #ebeef5;
  border-radius: 4px;
}
</style>
<template>
  <el-config-provider namespace="ep">
    <RouterView></RouterView>
  </el-config-provider>
  <el-dialog
      title="退出登录"
      v-model="quitVisible"
      @close="handleLogout()"
    >
      <span>另一个用户登录，您将被登出</span>
      <template #footer>
        <el-button type="primary" @click="handleLogout">确认</el-button>
      </template>
    </el-dialog>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useEventStore } from "./stores/event";
import { EventType } from "./utils/EventBus";
import router from "./router";
import type { SSEBody } from "./api/sse/SSEHandler";
const quitVisible = ref(false);
const {emitEvent} = useEventStore();

const handleLogout = () =>{
  emitEvent(EventType.currentlyIsLoggedOut);
  router.push('/login');
  quitVisible.value = false
}
const {registerEvent} = useEventStore();

registerEvent(EventType.quitEvent,(message: { body: SSEBody; })=>{
  handleQuit()
})

function handleQuit() {
  quitVisible.value = true;
}
</script>
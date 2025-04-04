<template>
  <div style="width: 100%;" v-if="bot!==0">
    <div style="width: 100%; height: 25px; border-radius: 4px; background-color: var(--ep-border-color);">
      <div style="background-color: var(--ep-color-primary); width: 30%; height: 100%; border-radius: 4px;" :style="`width: ${(top/bot*100)}%`"></div>
    </div>
    <div style="margin-top: 5px; float: right;">
      <span style="color: var(--ep-color-primary);">{{ top }}</span> / {{ bot }}
    </div>
    <div style="margin-top: 5px;">
      {{ (top/bot*100).toFixed(2) }}%
    </div>
  </div>
  <div v-else>No data</div>
</template>
<script setup lang="ts">
defineProps({
  top: {type: Number, default: 1},
  bot: {type: Number, default: 1},
})
</script>
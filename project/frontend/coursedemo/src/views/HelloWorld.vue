<template>
  <file-uploader ref="uploader"/>
  <el-button @click="func">func</el-button>
  <embed :src="link" style="width:100%; height:500px;" type="application/pdf"/>
</template>
<script setup lang="ts">
import { ref } from 'vue';

const link = ref('')
const uploader = ref()
function func() {
  const file = uploader.value.file_get as File 
  link.value = URL.createObjectURL(file)
}
</script>
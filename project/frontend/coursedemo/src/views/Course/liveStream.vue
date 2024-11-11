<template>
    <div>
      <h1>Live Stream Viewer</h1>
      <div>
        <el-button id="startStream" @click="getStreamName">Get Stream</el-button>
        <el-button id="getVideo" @click="playVideo">Get Video</el-button>
      </div>
      <video ref="videoElement" controls autoplay></video>
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
      console.error('Error fetching stream name:', error);
    }
  };
  </script>
  
  <style scoped>
  #video {
    width: 100%;
    height: auto;
  }
  </style>
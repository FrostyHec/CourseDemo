<template>
  <div style="display: flex; align-items: baseline;">
    <h1 style="font-size: large;">
      Comments
    </h1>
    <p style="font-size: medium; margin-left: 8px; color: var(--ep-color-primary);">
      {{ comment_list.length }}
    </p>
  </div>

  <div style="display: flex;">
    <el-avatar :size="40" style="border: solid 1px var(--ep-border-color); margin-right: 10px; flex-shrink: 0;"
      src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"/>
    <div style="width: 100%;">
      <el-input
        v-model="new_comment"
        :autosize="{ minRows: 4 }"
        type="textarea"
        placeholder="Please leave your comments ..."
      />
      <div style="margin: 10px;"></div>
      <el-button @click="send_comment">
        Send
      </el-button>
    </div>
  </div>

  <div v-for="c in comment_list" :key="c.id">
    <div style="margin-top: 20px; border-top: solid 1px var(--ep-border-color); padding-top: 18px; display: flex;">
      <el-avatar :size="40" style="border: solid 1px var(--ep-border-color); margin-right: 10px; flex-shrink: 0;"
        src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"/>
      <div>
        <p style="font-weight: bold; margin-top: 2px; margin-bottom: 5px;">
          {{ 'User-'+c.id }}
        </p>
        <p style="line-height: 1.5; margin: 0%;">
          {{ c.content }}
        </p> 
        <p style="color: var(--ep-text-color-placeholder); margin: 0%; margin-top: 10px; font-size: small;">  
          {{ (new Date(c.timestamp)).toLocaleString() }}
        </p>
      </div>
    </div>
  </div>
</template>

<script lang='ts' setup>
import { reactive, ref } from 'vue'
const new_comment = ref('')

interface CommentInfo {
  id: number,
  timestamp: number,
  user_id: number,
  content: string,
  ref?: number,
}

function send_comment() {
  if(new_comment.value=='')
    return
  comment_list.unshift({
    id: comment_list.length,
    timestamp: Date.now(),
    user_id: 0,
    content: new_comment.value,
  })
  new_comment.value = ''
}

const comment_list = reactive<CommentInfo[]>([
  {id: 0, timestamp: Date.now(), user_id: 0, content: 'A quick brown fox jumps over a lazy dog.'},
  {id: 1, timestamp: Date.now(), user_id: 0, content: 'Hard Disk Drive 就是我们平时最多说的普通磁盘，物理结构为柱形，由磁盘头和盘面组成。系统层面的 IO 到达物理设备层面就是磁盘头移动到指定位置然后执行某些操作的过程。关于 HDD，书中主要讨论了磁盘寻址耗时以及顺序读写和随机读写的性能量化计算，理解了磁盘寻址就能理解为什么 Kafka 的随机写性能为什么好了。最后还讨论了 IO 调度算法。'},
  {id: 2, timestamp: Date.now(), user_id: 0, content: 'A quick brown fox jumps over a lazy dog.'},
  {id: 3, timestamp: Date.now(), user_id: 0, content: 'Hard Disk Drive 就是我们平时最多说的普通磁盘，物理结构为柱形，由磁盘头和盘面组成。系统层面的 IO 到达物理设备层面就是磁盘头移动到指定位置然后执行某些操作的过程。关于 HDD，书中主要讨论了磁盘寻址耗时以及顺序读写和随机读写的性能量化计算，理解了磁盘寻址就能理解为什么 Kafka 的随机写性能为什么好了。最后还讨论了 IO 调度算法。'},
  {id: 4, timestamp: Date.now(), user_id: 0, content: 'A quick brown fox jumps over a lazy dog.'},
  {id: 5, timestamp: Date.now(), user_id: 0, content: 'Hard Disk Drive 就是我们平时最多说的普通磁盘，物理结构为柱形，由磁盘头和盘面组成。系统层面的 IO 到达物理设备层面就是磁盘头移动到指定位置然后执行某些操作的过程。关于 HDD，书中主要讨论了磁盘寻址耗时以及顺序读写和随机读写的性能量化计算，理解了磁盘寻址就能理解为什么 Kafka 的随机写性能为什么好了。最后还讨论了 IO 调度算法。'},
  {id: 6, timestamp: Date.now(), user_id: 0, content: 'Hard Disk Drive 就是我们平时最多说的普通磁盘，物理结构为柱形，由磁盘头和盘面组成。系统层面的 IO 到达物理设备层面就是磁盘头移动到指定位置然后执行某些操作的过程。关于 HDD，书中主要讨论了磁盘寻址耗时以及顺序读写和随机读写的性能量化计算，理解了磁盘寻址就能理解为什么 Kafka 的随机写性能为什么好了。最后还讨论了 IO 调度算法。'},
])
</script>
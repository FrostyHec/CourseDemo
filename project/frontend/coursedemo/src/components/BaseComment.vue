<template>
  <div v-if="is_resource">
    <div style="display: flex; align-items: baseline;">
      <h1 style="font-size: large;">
        Comments
      </h1>
      <p style="font-size: medium; margin-left: 8px; color: var(--ep-color-primary);">
        {{ comments.length }}
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
        <el-button type="primary" @click="send_comment()">
          Send
        </el-button>
        <el-button @click="new_comment = ''">
          Reset
        </el-button>
      </div>
    </div>

    <div v-for="c in comments" :key="c.comment_id" style="width: 100%;">
      <div style="width: 100%; margin-top: 20px; border-top: solid 1px var(--ep-border-color); padding-top: 18px; display: flex; overflow: hidden;">
        <el-avatar :size="40" style="border: solid 1px var(--ep-border-color); margin-right: 10px; flex-shrink: 0;"
          src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"/>
        <div style="overflow: hidden;">
          <div style="font-weight: bold; margin-top: 5px; margin-bottom: 10px;">
            {{ 'User-'+c.user_id }}
          </div>
          <div v-if="comment_map.has(c.comment_reply)" style="width: 100%; margin-bottom: 10px; font-size: small; opacity: 0.4; height: 18px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;">
            {{ (() => {
              const reply = comment_map.get(c.comment_reply)
              return '▌Reply User-' + reply?.user_id + ': ' + reply?.comment_text
            })() }}
          </div>
          <span style="line-height: 1.5; white-space-collapse: break-spaces;">
            {{ c.comment_text }}
          </span>
          <div style="display: flex; gap: 10px; flex-direction: row; align-items: center; margin-top: 12px; font-size: small;">
            <el-popover placement="bottom-start" :width="400" trigger="click" :hide-after="0" transition="None">
              <template #reference>
                <el-link type="primary" :underline="false" style="font-size: small;">Reply</el-link>
              </template>
              <el-input
                v-model="reply_new_comment"
                :autosize="{ minRows: 4 }"
                type="textarea"
                placeholder="Please leave your replies ..."
              />
              <div style="margin: 10px;"></div>
              <el-button type="primary" @click="send_comment(c.comment_id)">
                Send
              </el-button>
              <el-button @click="reply_new_comment = ''">
                Reset
              </el-button>
            </el-popover>
            <div style="color: var(--ep-text-color-placeholder);">  
              {{ c.updated_at.toLocaleString() }}
            </div>
          </div>
        </div>
      </div>
    </div>
  
  </div>
</template>

<script lang='ts' setup>
import { reactive, ref, watch } from 'vue'
import { useCourseStore } from '@/stores/course';
import { type CourseEntity } from '@/api/course/CourseAPI'
import { type ChapterEntity } from '@/api/course/ChapterAPI'
import type { ResourceEntityPlus } from '@/stores/course';
import { addCommentToResourceCall, addReplyToCommentCall, getResourceCommentsCall, type ResourceCommentEntity } from '@/api/course/ResourceCommentAPI';
import { ElMessage } from 'element-plus';
import { useAuthStore } from '@/stores/auth';

const course_store = useCourseStore()
const auth_store = useAuthStore()
const is_resource = ref(false)
let current_resource_id = -1
const comments = ref<ResourceCommentEntity[]>([{
  comment_id: 0, resource_id: 0, user_id: 1,
  comment_text: 'happy happy happy',
  created_at: new Date(), updated_at: new Date(),
  comment_reply: -1,
}, {
  comment_id: 1, resource_id: 0, user_id: 42,
  comment_text: 'tic tac toe',
  created_at: new Date(), updated_at: new Date(),
  comment_reply: -1,
}, {
  comment_id: 2, resource_id: 0, user_id: 23,
  comment_text: `踊れ💃踊れ💃嘘に踊れ💃
今までを捨↑て↑て↑
腕を振れよ🤛🤜
中身がなんもなくても💃
✌🎵未来はあるのさ🎵✌
Liar🤛Liar🤜Liar🤛Liar🤜Dancer🤛
素直で傷ついたあ↗の→日↘を↗
Liar🤛Liar🤜Liar🤛Liar🤜Dancer🤛
🎵💃嘘で踊るのさ💃🎵`,
  created_at: new Date(), updated_at: new Date(),
  comment_reply: 2,
}])
let comment_map = new Map<number, ResourceCommentEntity>()
comments.value.forEach((comment) => comment_map.set(comment.comment_id, comment))

async function load_comments() {
  //!!!!!!!!!!!!!!!!!!!!
  if(comments.value)
    return
  //!!!!!!!!!!!!!!!!!!!!
  comments.value = []
  const msg = await getResourceCommentsCall(current_resource_id)
  if(msg.code!=200) {
    ElMessage({
      message: 'Comment network error',
      type: 'error',
    })
    return
  }
  comments.value = msg.data.content
  comment_map.clear()
  comments.value.forEach((comment) => comment_map.set(comment.comment_id, comment))
}
const watch_current_data = watch(() => course_store.current_data?.data,
  async (new_data: CourseEntity|ChapterEntity|ResourceEntityPlus|undefined) => {
    is_resource.value = !!new_data && 'resource_name' in new_data
    console.log(is_resource)
    if(!new_data || !('resource_name' in new_data))
      return
    if(new_data.resource_id==current_resource_id)
      return
    current_resource_id = new_data.resource_id
    await load_comments()
  },
  { immediate: true }
)

const new_comment = ref('')
const reply_new_comment = ref('')
async function send_comment(reply_id?: number) {
  if(new_comment.value==='')
    return
  let msg
  if(!reply_id) {
    if(new_comment.value==='')
      return
    msg = await addCommentToResourceCall(current_resource_id, {
      comment_id: 0, resource_id: current_resource_id, user_id: Number(auth_store.user.user_id),
      comment_text: new_comment.value, 
      created_at: new Date(), updated_at: new Date(),
      comment_reply: -1
    })
  }
  else {
    if(reply_new_comment.value==='')
      return
    msg = await addReplyToCommentCall(reply_id, {
      comment_id: 0, resource_id: current_resource_id, user_id: Number(auth_store.user.user_id),
      comment_text: new_comment.value, 
      created_at: new Date(), updated_at: new Date(),
      comment_reply: reply_id,
    })
  }
  if(msg.code!=200) {
    ElMessage({
      message: 'send comment network error',
      type: 'error',
    })
    return
  }
  if(!reply_id)
    new_comment.value = ''
  else
    reply_new_comment.value = ''
  await load_comments()
}

</script>
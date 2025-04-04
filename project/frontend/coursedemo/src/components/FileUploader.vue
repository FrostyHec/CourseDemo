<template>
  <el-upload
    ref="upload"
    style="width: 100%;"
    drag
    action=""
    :auto-upload="false"
    :limit="1"
    :on-exceed="handleExceed"
    :on-change="handleChange"
    :on-remove="handleRemove"
  >
    <div>
      <em i="ep-edit"></em>Drop file here or <em style="color: var(--ep-color-primary);">click to upload</em>
    </div>
    <!-- <template #tip>
      <p style="margin: 0; font-size: small;">
        jpg/png files with a size less than 500kb
      </p>
    </template> -->
  </el-upload>
</template>
<script setup lang="ts">
import { genFileId, type UploadInstance, type UploadProps, type UploadRawFile } from 'element-plus';
import { ref } from 'vue';

const upload = ref<UploadInstance>()
let file_get = ref<File|undefined>(undefined)

const handleExceed: UploadProps['onExceed'] = (files) => {
  upload.value!.clearFiles()
  const file = files[0] as UploadRawFile
  file.uid = genFileId()
  upload.value!.handleStart(file)
} 

const emit = defineEmits({change: (file: File|undefined) => true})
const handleChange: UploadProps['onChange'] = (file) => {
  file_get.value = file.raw as File
  emit('change', file_get.value)
}

const handleRemove: UploadProps['onRemove'] = (file) => {
  file_get.value = undefined
  emit('change', undefined)
}

function clear() {
  upload.value?.clearFiles()
}

defineExpose({file_get, clear})

</script>
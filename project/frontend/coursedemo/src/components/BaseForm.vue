<template>
  <el-dialog v-model="form_store.visibility" :title="form_store.mode+' a '+form_store.get_level()" width="500">
    
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="auto"
    >
      <el-form-item label="Name" prop="name">
        <el-input v-model="form.name" placeholder="Enter the name"/>
      </el-form-item>

      <el-form-item label="Type" prop="type">
        <el-segmented v-model="form.type" :options="type_list()" />
      </el-form-item>

      <el-form-item label="File">
        <el-upload
          ref="upload"
          style="width: 100%;"
          drag
          action=""
          :auto-upload="false"
          :limit="1"
          :on-exceed="handleExceed"
          :on-change="handleChange"
        >
          <div>
            <em i="ep-edit"></em>Drop file here or <em style="color: var(--ep-color-primary);">click to upload</em>
          </div>
          <template #tip>
            <p style="margin: 0; font-size: small;">
              jpg/png files with a size less than 500kb
            </p>
          </template>
        </el-upload>
      </el-form-item>

      <el-form-item label="Description" prop="description">
        <el-input v-model="form.description" type="textarea" placeholder="Enter the description"/>
      </el-form-item>

    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="resetForm(formRef)">
          Reset
        </el-button>
        <el-button type="primary" @click="submitForm(formRef)">
          Confirm
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { useFormStore } from '@/stores/form';
import { computed, reactive, ref } from 'vue';
import { type FormInstance, type FormRules, ElMessage} from 'element-plus'
import { genFileId } from 'element-plus'
import type { UploadInstance, UploadProps, UploadRawFile } from 'element-plus'

const upload = ref<UploadInstance>()

const handleExceed: UploadProps['onExceed'] = (files) => {
  upload.value!.clearFiles()
  const file = files[0] as UploadRawFile
  file.uid = genFileId()
  upload.value!.handleStart(file)
} 

const handleChange: UploadProps['onChange'] = (file) => {
  console.log(file)
}

const form_store = useFormStore()

interface FormInfo {
  name: string,
  type?: string,
  description: string
}

const type_list = () => {
  if(form_store.get_level()==1)
    return ['teaching', 'assignment', 'project']
  else
    return ['pdf', 'md', 'movie', 'archive']
}

const form = reactive<FormInfo>({
  name: '',
  description: '',
})

const rules = reactive<FormRules<FormInfo>>({
    name: [
      { required: true, message: 'Please enter the name', trigger: 'blur', },
    ],
    type: [
      { required: true, message: 'Please select a type', trigger: 'blur', },
    ],
    description: [],
})

const formRef = ref<FormInstance>()

const resetForm = (formIn: FormInstance | undefined) => {
  if (!formIn) return
  formIn.resetFields()
}

const submitForm = async (formIn: FormInstance | undefined) => {
    if (!formIn) return
    await formIn.validate((valid, fields) => {
      if (valid) {
        ElMessage({
          message: "Success",
          type: "success",
        })
        console.log('submit!')
        form_store.visibility = false
        if(form_store.node)
          if(form_store.mode=='Edit')
            form_store.node.data.label = form.name
          else {
            let new_node: {label: string, children?: []} = {label: form.name}
            if(form_store.node.level==1)
              new_node.children = []
            form_store.node.data.children?.push(new_node)
          }
        formIn.resetFields()
      } else {
        console.log('error submit!', fields)
      }
    })
  }

</script>
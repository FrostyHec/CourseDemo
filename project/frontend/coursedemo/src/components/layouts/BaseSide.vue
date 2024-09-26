<template>
  <el-scrollbar style="border-right: solid 1px var(--ep-border-color);">
    <el-tree
      style="max-width: 200px;"
      :default-expand-all="true"
      :expand-on-click-node="false"
      :data="[folder]"
      :props="{
        children: 'children',
        label: 'label',
      }"
      @node-click="click_jump"
    />
  </el-scrollbar>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import {
  Location,
  Document,
  Menu as IconMenu,
  Setting,
} from "@element-plus/icons-vue";
import { useRoute, useRouter } from "vue-router";

interface Tree {
  label: string,
  link?: string,
  children?: Tree[],
}

function generate_link(t: Tree, prefix: string = '') {
  t.link = prefix + '/' + t.label.replace(/ /g, '-')
  t.children?.forEach((value: Tree) => generate_link(value, t.link))
}

const folder: Tree = 
{ label: 'Object Oriented Analysis Design', children: [
  { label: 'Chapter1', children: [
    { label: 'video' }, 
    { label: 'pdf' },
  ]},
  { label: 'Chapter2', children: [
    { label: 'video'},
    { label: 'md'},
    { label: 'test'},
  ]},
  { label: 'Chapter3', children: [
    { label: 'video'}, 
    { label: 'md'},
    { label: 'quiz'},
  ]},
]}

generate_link(folder)

const router = useRouter()
const click_jump = (data: Tree) => {
  router.push('/course' + data.link)
}

</script>

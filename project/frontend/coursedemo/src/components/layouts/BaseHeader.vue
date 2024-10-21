<script lang="ts" setup>
import { useDark, useToggle } from "@vueuse/core";

const isDark = useDark();
const toggleDark = useToggle(isDark);
import { ArrowRight } from '@element-plus/icons-vue'

function generate_breadcrumb(s: string[] | string | undefined): {key: number, label: string, link?: string}[] {
  if(!s)
    return []
  if(typeof s === "string")
    s = [s]
  let prefix = '/course'
  let res = []
  for(let i=0;i<s.length;i++) {
    prefix += '/' + s[i]
    if(i==s.length-1) {
      res.push({key: i, label: s[i].replace(/-/g, ' ')})
    } else {
      res.push({key: i, label: s[i].replace(/-/g, ' '), link: prefix})
    }
  }
  return res
}

</script>

<template>
  <el-page-header icon="" title=" " style="border-bottom: solid 1px var(--ep-border-color);" px="5" py="1">

    <template #title>
      <span style="font-weight: 600; font-size: large; margin: 0%;" @click="$router.push('/')">
        Course Demo
      </span>
    </template>

    <template #content>
      <el-breadcrumb :separator-icon="ArrowRight">
        <el-breadcrumb-item></el-breadcrumb-item>
        <el-breadcrumb-item v-for="it in generate_breadcrumb($route.params.labels)" :key="it.key" :to="it.link">
          {{ it.label }}
        </el-breadcrumb-item>
        <!-- <el-breadcrumb-item :to="{ path: '/' }">homepage</el-breadcrumb-item>
        <el-breadcrumb-item>promotion management</el-breadcrumb-item>
        <el-breadcrumb-item>promotion list</el-breadcrumb-item>
        <el-breadcrumb-item>{{ $route.params.labels }}</el-breadcrumb-item> -->
      </el-breadcrumb>
    </template>

    <template #extra>
      <div style="width: 100%; display: flex; place-items: center">
        
        <el-popover
          :width="300"
        >
          <template #reference>
            <el-avatar :size="36" style="border: solid 1px var(--ep-border-color); margin-right: 10px;"
            src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
          </template>
          <template #default>
            <div
              style="display: flex; gap: 5px; flex-direction: column"
            >
              <div style="display: flex; gap: 10px; align-items: center;">
                <el-avatar :size="64" style="border: solid 1px var(--ep-border-color); margin-left: 5px;"
                src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"/>
                <div>
                  <h2 style="margin: 0%;">Alice Bob</h2>
                  <p style="margin: 0%; font-size: 14px; color: var(--el-color-info)">
                    alice_bob@sustech.edu.cn
                  </p>
                </div>
              </div>

              <el-button>Settings</el-button>
              <el-button type="primary" style="margin: 0%;">Sign out</el-button>
            </div>
          </template>
        </el-popover>
        <span class="text-large font-600 mr-3"> Alice Bob </span>
        <el-button>
          Calendar
        </el-button>
        <el-button
          circle
          class="bg-transparent cursor-pointer"
          @click="toggleDark()"
        >
          <i inline-flex i="dark:ep-moon ep-sunny" />
        </el-button>
      </div>
    </template>
  </el-page-header>
</template>

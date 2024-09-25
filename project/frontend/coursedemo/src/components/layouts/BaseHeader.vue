<script lang="ts" setup>
import { useDark, useToggle } from "@vueuse/core";

const isDark = useDark();
const toggleDark = useToggle(isDark);
import { ArrowRight } from '@element-plus/icons-vue'
</script>

<template>
  <el-page-header icon="" title=" " style="border-bottom: solid 1px var(--ep-menu-border-color);" px="5" py="1">

    <template #title>
      <p style="font-weight: 600; font-size: large; margin: 0%;">
        Course Demo
      </p>
    </template>

    <template #content>
      <el-breadcrumb :separator-icon="ArrowRight">
        <el-breadcrumb-item :to="{ path: '/' }">homepage</el-breadcrumb-item>
        <el-breadcrumb-item>promotion management</el-breadcrumb-item>
        <el-breadcrumb-item>promotion list</el-breadcrumb-item>
        <el-breadcrumb-item>promotion detail</el-breadcrumb-item>
      </el-breadcrumb>
    </template>

    <template #extra>
      <div style="width: 100%; display: flex; place-items: center">
        
        <el-popover
          :width="300"
        >
          <template #reference>
            <el-avatar :size="36" style="border: solid 1px var(--ep-menu-border-color); margin-right: 10px;"
            src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
          </template>
          <template #default>
            <div
              class="demo-rich-conent"
              style="display: flex; gap: 5px; flex-direction: column"
            >
              <div style="display: flex; gap: 10px; align-items: center;">
                <el-avatar :size="64" style="border: solid 1px var(--ep-menu-border-color); margin-left: 5px;"
                src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"/>
                <div>
                  <h2 style="margin: 0%;">Alice Bob</h2>
                  <p style="margin: 0%; font-size: 14px; color: var(--el-color-info)">
                    alice_bob@sustech.edu.cn
                  </p>
                </div>
              </div>
              
              <div>
                
              </div>

              <el-button>Settings</el-button>
              <el-button type="primary">Sign out</el-button>
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

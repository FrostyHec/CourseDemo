<template>
  <el-container style="height: 100vh;">
    <el-header style="padding: 0%; height: auto;">
      <BaseHeader />
    </el-header>
    <el-container style="height: calc(100% - 49px);">
      <DragCol style="height: 100%; width: 100%;" :leftPercent="15" :sliderWidth="5">
        <template #left>
          <BaseSide style="width: 100%; height: 100%;"/>
        </template>
        <template #right>
          <BaseContent/>
        </template>
      </DragCol>
    </el-container>
  </el-container>
</template>
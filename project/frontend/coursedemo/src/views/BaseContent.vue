<template>
  <el-scrollbar>
    <div style="margin: 32px;">
      <h1 style="font-size: xx-large;">
        Title
      </h1>
      <h1 style="font-size: large;">
        Description
      </h1>
      {{ $route.params.labels }}
      <base-comment/>
    </div>
  </el-scrollbar>
</template>
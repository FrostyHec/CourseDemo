<template>
  <el-config-provider namespace="ep">
    <el-container style="height: 100vh;">
      <el-header style="padding: 0%; height: auto;">
        <BaseHeader />
      </el-header>
      <el-container style="height: calc(100% - 49px);">
        <el-aside style="width: auto; height: auto;">
          <BaseSide/>
        </el-aside>
        <el-main>
          <div class="flex main-container">
            <div w="full" py="4">
              <Logos my="4" />
              <RouterView/>
            </div>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </el-config-provider>
</template>


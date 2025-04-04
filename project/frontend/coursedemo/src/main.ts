import { createApp } from "vue"
import router from "./router"
import App from "./App.vue"
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// import "~/styles/element/index.scss";

// import ElementPlus from "element-plus";
// import all element css, uncommented next line
// import "element-plus/dist/index.css";

// or use cdn, uncomment cdn link in `index.html`

import '~/styles/index.scss'
import 'uno.css'
// If you want to use ElMessage, import it.
import 'element-plus/theme-chalk/src/message.scss'
import 'element-plus/theme-chalk/src/notification.scss'
import { createPinia } from 'pinia'
import { handleBackendPath, handleMockStatus } from './utils/EnvUtils'

import ECharts from "vue-echarts"
import * as echarts from "echarts"

export async function main() {
  const app = createApp(App)

  app.use(router)
  app.use(createPinia())
  handleBackendPath()
  await handleMockStatus()
  app.use(ElementPlus);
  return app
}
const app = await main()
app.component('e-charts', ECharts)
app.config.globalProperties.$echarts = echarts
app.mount('#app')




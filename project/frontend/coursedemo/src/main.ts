import { createApp } from 'vue'
import router from './router'
import App from './App.vue'

// import "~/styles/element/index.scss";

// import ElementPlus from "element-plus";
// import all element css, uncommented next line
// import "element-plus/dist/index.css";

// or use cdn, uncomment cdn link in `index.html`

import '~/styles/index.scss'
import 'uno.css'

// If you want to use ElMessage, import it.
import 'element-plus/theme-chalk/src/message.scss'
import { createPinia } from 'pinia'
import { handleBackendPath, handleMockStatus } from './utils/EnvUtils'
export async function main() {
  const app = createApp(App)
  app.use(router)
  app.use(createPinia())
<<<<<<< HEAD
  handleBackendPath(backendUrl)
  await handleMockStatus(mockStatus)
=======
  handleBackendPath()
  await handleMockStatus()
>>>>>>> 3640c459e70beff6ea00cba0bb3cb2c49bd07b11
// app.use(ElementPlus);
  return app
}

const app = await main()
app.mount('#app')




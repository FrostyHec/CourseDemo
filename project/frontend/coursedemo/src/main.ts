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
import { handleMockStatus } from './utils/EnvUtils'

const mockStatus = import.meta.env.VITE_MOCK_STATUS

export async function main() {
  const app = createApp(App)
  app.use(router)
  app.use(createPinia())
  await handleMockStatus(mockStatus)

// app.use(ElementPlus);
  return app
}

const app = await main()
app.mount('#app')




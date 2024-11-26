import { enableTempMock } from '@/utils/MockUtils'
import { InternalException } from '@/utils/Exceptions'
import {
  setMockStatus,
  setAllBackendBase,
  setStorageBackendBase,
  setSSEBackendBase,
  setWebSocketBackendBase, setLivePushUrl, setLivePullUrl
} from '@/utils/Constant'
const mockStatus = import.meta.env.VITE_MOCK_STATUS
export enum MockStatus {
  DEV = 'dev',
  PROD = 'prod',
  E2E = 'e2e',
}

export async function handleMockStatus() {
  switch (mockStatus) {
    case MockStatus.DEV:
      console.log('Using temp mock')
      await enableTempMock()
      break
    case MockStatus.PROD:
      console.log('Connecting to the backend url')
      break
    case MockStatus.E2E:
      console.log('Using e2e mock')
      break
    default:
      throw new InternalException('unknown mock status: ' + mockStatus)
  }
  setMockStatus((mockStatus as MockStatus))
}
const backendUrl = import.meta.env.VITE_BACKEND_URL
const storageBackendUrl = import.meta.env.VITE_STORAGE_BACKEND_URL
const sseBackendUrl = import.meta.env.VITE_SSE_BACKEND_URL
const websocketBackendUrl = import.meta.env.VITE_WEBSOCKET_BACKEND_URL
const livePushUrl = import.meta.env.VITE_LIVE_PUSH_URL
const livePullUrl = import.meta.env.VITE_LIVE_PULL_URL
export function handleBackendPath(){
  setAllBackendBase(backendUrl)
  if(storageBackendUrl){
    setStorageBackendBase(storageBackendUrl)
  }
  if(sseBackendUrl){
    setSSEBackendBase(sseBackendUrl)
  }
  if(websocketBackendUrl){
    setWebSocketBackendBase(websocketBackendUrl)
  }
  if(livePushUrl){
    setLivePushUrl(livePushUrl)
  }
  if(livePullUrl){
    setLivePullUrl(livePullUrl)
  }
}
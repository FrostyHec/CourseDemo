import type { MockStatus } from '@/utils/EnvUtils'
import MockAdapter from 'axios-mock-adapter'
import axios from 'axios'

const api_base = '/api/v1'
export let service_backend_base: string = ''
export let storage_backend_base:string = ''
export let sse_backend_base:string = ''
export let websocket_backend_base:string = ''
export let live_push_url:string = ''
export let live_pull_url:string = ''

export function setAllBackendBase(backend_url: string) {
  setServiceBackendBase(backend_url)
  setStorageBackendBase(backend_url)
  setSSEBackendBase(backend_url)
}

export function setSSEBackendBase(backend_url:string){
  sse_backend_base = backend_url + api_base
}
export function setServiceBackendBase(backend_url:string){
  service_backend_base = backend_url + api_base
}

export function setStorageBackendBase(backend_url:string){
  storage_backend_base = backend_url +api_base
}

export function setWebSocketBackendBase(backend_url:string){
    websocket_backend_base = backend_url + api_base
}
export function setLivePushUrl(url:string) {
    live_push_url = url
}
export function setLivePullUrl(url:string) {
    live_pull_url = url
}

export let mock_status: MockStatus | null = null

export function setMockStatus(mockStatus:MockStatus){
  mock_status = mockStatus
}
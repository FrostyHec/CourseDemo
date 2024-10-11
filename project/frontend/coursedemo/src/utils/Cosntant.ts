import type { MockStatus } from '@/utils/EnvUtils'
import MockAdapter from 'axios-mock-adapter'
import axios from 'axios'

const api_base = '/api/v1'
export let backend_base: string = ''

export function setBackendBase(backend_url: string) {
  backend_base = backend_url + api_base
}
export let mock_status: MockStatus | null = null

export function setMockStatus(mockStatus:MockStatus){
  mock_status = mockStatus
}
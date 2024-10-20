import { enableTempMock } from '@/utils/MockUtils'
import { InternalException } from '@/utils/Exceptions'
import { setMockStatus, setBackendBase } from '@/utils/Constant'

export enum MockStatus {
  DEV = 'dev',
  PROD = 'prod',
  E2E = 'e2e',
}

export async function handleMockStatus(mockStatus: string) {
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

export function handleBackendPath(backend_url:string){
  setBackendBase(backend_url)
}
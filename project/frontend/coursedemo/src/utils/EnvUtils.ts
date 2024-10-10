import { enableTempMock } from '@/utils/MockUtils'
import { InternalException } from '@/utils/Exceptions'

enum MockStatus {
  Dev = 'dev',
  Prod = 'prod',
  E2E = 'e2e',
}

export async function handleMockStatus(mockStatus: string) {
  switch (mockStatus) {
    case MockStatus.Dev:
      console.log('Using temp mock')
      await enableTempMock()
      break
    case MockStatus.Prod:
      console.log('Connecting to the backend url')
      break
    case MockStatus.E2E:
      console.log('Using e2e mock')
      break
    default:
      throw new InternalException('unknown mock status: ' + mockStatus)
  }
}

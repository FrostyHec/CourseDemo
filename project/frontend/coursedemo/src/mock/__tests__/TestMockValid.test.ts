import { describe, it, expect,beforeEach } from 'vitest'
import type { LoginResult } from '@/api/UserAPI'
import { mockLoginCall } from '@/mock/MockUserAPI'
import { useAuthStore } from '@/stores/auth'
import { APIResult } from '@/utils/APIUtils'
import { createPinia, setActivePinia, storeToRefs } from 'pinia'
describe('Mock valid test', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })
  it('should be ok for both mock and pinia', async () => {
    const res: LoginResult = {
      token: '123'
    }
    mockLoginCall(new APIResult<LoginResult>(200, '', res))

    const store = useAuthStore()
    const login = store.login
    const {token} = storeToRefs(store) // important! use storeToRefs to get the ref value

    await login({
        user_id: 1,
        password: ''
      })
    expect(token.value).toBe('123')

    res.token = '456'
    mockLoginCall(new APIResult<LoginResult>(200, '', res))
    await login({
        user_id: 2,
        password: ''
      })
    expect(token.value).toBe('456')

  })
})

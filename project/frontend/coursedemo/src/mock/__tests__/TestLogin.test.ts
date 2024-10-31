import { describe, it, expect,beforeEach, vi, beforeAll } from 'vitest'
import type { LoginResult } from '@/api/user/UserAPI'
import { mockLoginCall } from '@/mock/user/MockUserAPI'
import { useAuthStore } from '@/stores/auth'
import { APIResult } from '@/utils/APIUtils'
import { createPinia, setActivePinia, storeToRefs } from 'pinia'
import { UserType } from '@/api/user/UserAPI'
// 创建 Pinia 实例
const pinia = createPinia();
// 在所有测试之前激活 Pinia 实例
beforeAll(() => {
  setActivePinia(pinia);
});
describe('Auth Store Login Test', () => {
  const mockResponse: APIResult<LoginResult> = new APIResult(200, '', {
    token: 'mock-token',
    user: {
      user_id: 1,
      first_name: 'John',
      last_name: 'Doe',
      email: 'john.doe@example.com',
      role: UserType.STUDENT,
    },
  });

  const mockLogin = vi.fn().mockResolvedValue(mockResponse);

  it('should redirect to /MainPage/student after successful login', async () => {
    // Arrange
    const authStore = useAuthStore();
    vi.spyOn(authStore, 'login').mockResolvedValue(mockResponse);
  
    // Act
    await authStore.login({ email: 'student@example.com', password: 'password' });
  
    // Assert
    expect(authStore.token).toBe('');
    expect(authStore.user.role).toBe(UserType.STUDENT);
  });
});
import { type APIParam, APIResult, RequestType } from '@/utils/APIUtils'
import Mock from 'mockjs'

type mockReturnFunc = (req: APIParam | null) => APIResult | null;
type mockReturnFuncNoParam = () => APIResult | null;

export function setMockFunc(url: string, req_type: RequestType, request: APIParam | null, func: mockReturnFunc | mockReturnFuncNoParam) {
  let f: () => APIResult | null
  if (func.length === 0) {
    f = () => (func as mockReturnFuncNoParam)()
  } else if (func.length === 1) {
    f = () => (func as mockReturnFunc)(request)
  }
  Mock.mock(url, req_type, () => f())
}
export async function enableTempMock() {
  try {
    // 动态导入模块
    const module = await import('../mockjs/nonpublic-TempMock');
    console.log(module)
    // 遍历模块的所有导出
    Object.keys(module).forEach((key) => {
      const fn = module[key as keyof typeof module];
      console.log(fn)
      if (typeof fn === 'function') {
        // 执行函数
        (fn as Function)();
      }
    });
  } catch (error) {
    console.error('Error executing functions:', error);
  }
}

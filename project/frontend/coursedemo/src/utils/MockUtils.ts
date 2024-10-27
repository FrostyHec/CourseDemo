import { type APIParam, APIResult, RequestType } from '@/utils/APIUtils'
import { mock_status} from '@/utils/Constant'
import { MockStatus } from '@/utils/EnvUtils'
import MockAdapter from 'axios-mock-adapter'
import axios from 'axios'
import { InternalException } from '@/utils/Exceptions'

type mockReturnFunc = (req: APIParam | null) => APIResult | null;
type mockReturnFuncNoParam = () => APIResult | null;
const mockAdapter = new MockAdapter(axios);
function mock(url:string,req_type:RequestType,func:(()=>any)){
  switch (req_type) {
    case RequestType.GET:
      mockAdapter.onGet(url).reply(() => {
        const response = func();
        return [200, response];
      });
      break;
    case RequestType.POST:
      mockAdapter.onPost(url).reply(() => {
        const response = func();
        return [200, response];
      });
      break;
    case RequestType.PUT:
      mockAdapter.onPut(url).reply(() => {
        const response = func();
        return [200, response];
      });
      break;
    case RequestType.DELETE:
      mockAdapter.onDelete(url).reply(() => {
        const response = func();
        return [200, response];
      });
      break;
    default:
      throw new Error(`Unsupported request type: ${req_type}`);
  }
}
export function clearMock(){
  mockAdapter.reset()
}
export function setMockFunc(url: string, req_type: RequestType, request: APIParam | null, func: mockReturnFunc | mockReturnFuncNoParam,
                            forceEnable: boolean | null = null) {
  /*
   * 1. 多次注册，最后有效
   * 2. 当环境为PROD时，只有forceEnable下会生效
   */
  if (mock_status == MockStatus.PROD) {
    if(!forceEnable){
      return
    }
    console.warn('enabling mock under production config, url:'+url+'req_type:'+req_type+'request:'+request+'func:'+func)
  }
  let f: () => APIResult | null
  if (func.length === 0) {
    f = () => (func as mockReturnFuncNoParam)()
  } else if (func.length === 1) {
    f = () => (func as mockReturnFunc)(request)
  }else{
    throw new InternalException('invalid api registered type: '+func.toString())
  }
  mock(url, req_type, f)
}

export async function enableTempMock() {
  try {
    // 动态导入模块
    const module = await import('@/mock/nonpublic-TempMock')
    console.log(module)
    // 遍历模块的所有导出
    Object.keys(module).forEach((key) => {
      const fn = module[key as keyof typeof module]
      console.log(fn)
      if (typeof fn === 'function') {
        // 执行函数
        (fn as Function)()
      }
    })
  } catch (error) {
    console.error('Error executing functions:', error)
  }
}
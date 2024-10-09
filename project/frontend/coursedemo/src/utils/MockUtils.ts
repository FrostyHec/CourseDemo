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

import axios, { type AxiosResponse } from 'axios'
import { Ex } from '@/utils/ParamCheckUtils'

export enum ResultCodeType {
  OK = 200,
  BAD_REQUEST = 400
}

export enum RequestType {
  GET = 'get',
  POST = 'post',
  PUT = 'put',
  DELETE = 'delete',
  PATCH = 'patch'
}

// 定义 API 结果类
export class APIResult<T = any> {
  code: ResultCodeType
  msg: string
  data: T

  constructor(code: ResultCodeType, msg: string, data: T) {
    this.code = code
    this.msg = msg
    this.data = data
  }

  static fromAxiosResponse<T>(response: AxiosResponse): APIResult<T> {
    Ex.check(response.status == 200, 'error in backend server: ' + response)
    const resp = response.data
    return new APIResult(resp.code as ResultCodeType, resp.msg, resp.data)
  }
}

export interface APIParam {

}

export interface APIDataResult{
}
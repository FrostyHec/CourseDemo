import axios, { type AxiosResponse,type AxiosRequestConfig } from 'axios'
import { Ex } from '@/utils/ParamCheckUtils'
import { useAuthStore } from '@/stores/auth'


export enum ResultCodeType {
  OK = 200,
  BAD_REQUEST = 400,
  UNAUTHORIZED = 401,
  NO_FOUND = 404,
  NOT_MODIFIED = 304,
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

export interface APIDataResult {
}

export class AxiosAPI {
  private static setAuthHeader(config: AxiosRequestConfig = {}): AxiosRequestConfig {
    const { token } = useAuthStore();
    config.headers = {
      'Authorization': 'Bearer ' + token,
    };
    return config;
  }

  private static async request<T>(
    method: 'get' | 'post' | 'put' | 'delete' | 'patch',
    url: string,
    dataOrParams: APIParam,
    auth: boolean = false
  ): Promise<APIResult<T>> {
    const config: AxiosRequestConfig = auth ? this.setAuthHeader() : {};
    if (method === 'get' || method === 'delete') {
      config.params = dataOrParams;
    } else {
      config.data = dataOrParams;
    }
    const response = await axios({ method, url, ...config });
    return APIResult.fromAxiosResponse<T>(response);
  }

  static async get<T>(url: string, param: APIParam): Promise<APIResult<T>> {
    return await this.request<T>('get', url, param);
  }

  static async authGet<T>(url: string, param: APIParam): Promise<APIResult<T>> {
    return await this.request<T>('get', url, param, true);
  }

  static async post<T>(url: string, data: APIParam): Promise<APIResult<T>> {
    return await this.request<T>('post', url, data);
  }

  static async authPost<T>(url: string, data: APIParam): Promise<APIResult<T>> {
    return await this.request<T>('post', url, data, true);
  }

  static async put<T>(url: string, data: APIParam): Promise<APIResult<T>> {
    return await this.request<T>('put', url, data);
  }

  static async authPut<T>(url: string, data: APIParam): Promise<APIResult<T>> {
    return await this.request<T>('put', url, data, true);
  }

  static async delete<T>(url: string, param: APIParam): Promise<APIResult<T>> {
    return await this.request<T>('delete', url, param);
  }

  static async authDelete<T>(url: string, param: APIParam): Promise<APIResult<T>> {
    return await this.request<T>('delete', url, param, true);
  }

  static async patch<T>(url: string, data: APIParam): Promise<APIResult<T>> {
    return await this.request<T>('patch', url, data);
  }

  static async authPatch<T>(url: string, data: APIParam): Promise<APIResult<T>> {
    return await this.request<T>('patch', url, data, true);
  }
}
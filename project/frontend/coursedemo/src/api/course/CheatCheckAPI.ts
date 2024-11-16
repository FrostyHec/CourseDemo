import { service_backend_base } from '@/utils/Constant'
import { AxiosAPI } from '@/utils/APIUtils'

/////////////////////   CHEAT CHECK   ///////////////////////////////

export interface SetMinRequiredTimeEntity {
    requiredSeconds: number;
}

export interface LastWatchedEntity {
    remainRequiredSeconds: number;
    lastWatchedSeconds: number;
}

export interface WatchedInfoEntity {
    watchedSeconds: number;
    watchedUntil: number;
}

// 设置最少观看时间
export async function setMinRequiredTimeCall(id: number, param: SetMinRequiredTimeEntity) {
    const url = `${service_backend_base}/resource/${id}/watch/required-time`;
    return AxiosAPI.authPut<null>(url, param);
}

// 获取最少观看时间
export async function getMinRequiredTimeCall(id: number) {
    const url = `${service_backend_base}/resource/${id}/watch/required-time`;
    return AxiosAPI.authGet<SetMinRequiredTimeEntity>(url, {});
}

// 获取最后观看信息
export async function getLastWatchedCall(id: number) {
    const url = `${service_backend_base}/resource/${id}/watch/last`;
    return AxiosAPI.authGet<LastWatchedEntity>(url, {});
}

// 开始观看
export async function startWatchAliveCall(id: number) {
    const url = `${service_backend_base}/resource/${id}/watch/start`;
    return AxiosAPI.authPut<null>(url, {});
}

// 保持观看
export async function keepWatchAliveCall(id: number) {
    const url = `${service_backend_base}/resource/${id}/watch/alive`;
    return AxiosAPI.authGet<null>(url, {});
}

// 停止观看
export async function stopWatchAliveCall(id: number, param: WatchedInfoEntity) {
    const url = `${service_backend_base}/resource/${id}/watch/stop`;
    return AxiosAPI.authGet<null>(url, param);
}

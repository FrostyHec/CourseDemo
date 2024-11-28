import {live_pull_url, live_push_url, service_backend_base} from "@/utils/Constant";
import {AxiosAPI} from "@/utils/APIUtils";

export interface LivestreamPushNameEntity {
    name: string
}

export function getPushName(courseId:number){
    const url = `${service_backend_base}/course/${courseId}/live-stream/push`
    return AxiosAPI.authGet<LivestreamPushNameEntity>(url)
}

export function getPullName(courseId:number){
    const url = `${service_backend_base}/course/${courseId}/live-stream/pull`
    return AxiosAPI.authGet<LivestreamPushNameEntity>(url)
    // if the live stream is not started, the response will be 400 no-live-stream
}

export function getLivestreamPullUrl(streamName:string){
    // get video from here
    return `${live_pull_url}/live?app=course&stream=${streamName}`
}

export function getFlvConfig(streamName:string){
    return {
        type: 'flv',
        url:getLivestreamPullUrl(streamName),
        cors: true,
        headers: AxiosAPI.getAuthHeaderJson()
    }
}

export function getLivestreamPushUrl(streamName:string){
    return `${live_push_url}/course/${streamName}`
}
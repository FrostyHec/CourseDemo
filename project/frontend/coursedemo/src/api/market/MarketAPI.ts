import { service_backend_base } from '@/utils/Constant'
import { AxiosAPI } from '@/utils/APIUtils'

/////////////////////   MARKET   ///////////////////////////////

export interface MyMarketScore {
    user_id: number
    market_score: number
}

export async function getMyMarketScoreCall() {
    const url = service_backend_base + '/market/my-market-score'
    return AxiosAPI.authGet<MyMarketScore>(url, {})
}

/////////////////////   BADGE   ///////////////////////////////

export interface BadgeInfo {
    user_id: number
    badge_id: number
    image:string
    badge_name: string
    market_score: number
    owned: boolean
}

export async function getMyBadgeCall() {
    const url = service_backend_base + '/market/badge/my'
    return AxiosAPI.authGet<{content: BadgeInfo[]}>(url, {})
}

export async function buyBadgeCall(badgeInfo: BadgeInfo) {
    const url = service_backend_base + '/market/badge/buy'
    return AxiosAPI.authPost<null>(url, badgeInfo)
}

export async function getMyCanBuyBadgeCall() {
    const url = service_backend_base + '/market/badge/my-canbuy'
    return AxiosAPI.authGet<{content: BadgeInfo[]}>(url, {})
}

/////////////////////   MARKET HISTORY   ///////////////////////////////

export interface ConsumeRecord {
    record_id: number
    action_type: ConsumeActionType
    action_param: ActionParam
    changed_score: number
    remain_score: number
    created_at: Date
}

export enum ConsumeActionType {
    buy_badge = 'buy_badge',
    daily_comment = 'daily_comment',
    complete_course = 'complete_course'
}

export interface ActionParam {
}

export interface BuyBadgeActionParam extends ActionParam{
    badge_id:number,
    badge_name:string
}
export interface CompleteCourseActionParam extends ActionParam{
    course_id:number,
    course_name:string
}
export interface DailyCommentActionParam extends ActionParam{
    course_id:number,
    course_name:string
}
export async function getMyHistoryCall() {
    const url = service_backend_base + '/market/history/history/my'
    return AxiosAPI.authGet<{content:ConsumeRecord[]}>(url, {})
}
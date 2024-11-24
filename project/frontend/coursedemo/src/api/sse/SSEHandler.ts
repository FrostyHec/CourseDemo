import {useEventStore} from "@/stores/event";
import {EventBus, EventType} from "@/utils/EventBus";
import {InternalException} from "@/utils/Exceptions";
import {sse_backend_base} from "@/utils/Constant";
import {useAuthStore} from "@/stores/auth";

let eventSource: EventSource | null = null;

//////////////single message for common cases////////////////
export interface SSEMessage {
    message_id: number,
    from_id: number,
    to_id: number,
    type: SSEMessageType,
    required_ack: boolean,
    body_type: SSEBodyType,
    body: SSEBody
}

export interface SSEBody {
}

export enum SSEMessageType {
    NEW = 1,
    UPDATE = 2,
    DELETE = 3,
}

export enum SSEBodyType {
    announcement = 'announcement',
    new_login = 'new_login',
    receive_credits = 'receive_credits',
}

interface EventHandler {
    (message: SSEMessage): void;
}

export const EventHandlerMaps: { [key in SSEBodyType]: EventHandler } = {
    // TODO create another TS, complete the body structure(extent SSEBody)
    //  and handler function, and then register at here
    [SSEBodyType.announcement]: (message) => {
        // 在页面的右下角弹出一个框，提示“您收到了一条来自xxx课程的公告，xxxx",然后点击可跳转查看公告
    },
    [SSEBodyType.new_login]: (message) => {
        // 校验是否与当前store里存储的token一致，如果一致则忽略，否则提示"另一个用户登录“并且退出登录
    },
    [SSEBodyType.receive_credits]: (message) => {
        // 跳出提示框”XXX原因，积分+200“之类的
    },
};
/////////////messages packages///////////////
// TODO register handler here and remove nullable
const multipleMessageHandler: ((message: MessagePacket) => void) = (e) => {
    // 你可以考虑一下怎么处理，对于new_login的pkt应该忽略（应该是某种极端情况，理论上我觉得应该登出的，但我不知道有没有什么意外情况，因此就暂时忽略吧），
    // receive_credits可以忽略。announcement应该合并说：”您收到了来自xxx课程，xxx课程与xxx课程的公告。“
    // 理论上只有unposed的list会有东西
}

interface SSEPackage {
    push_type: PackageType,
    body: MessagePacket | SSEMessage
}

enum PackageType {
    single = 1,
    packet = 2,
}

export interface MessagePacket {
    unposed: SSEMessage[],
    unacked: SSEMessage[]
}


export function subscribeToSSE(uid: number) {
    return
    if (eventSource) {
        console.log('SSE is registered status:', eventSource.readyState)
        return
    }
    console.log('subscribing To SSE')
    // const {user} = useAuthStore()
    // const uid = user.user_id;
    if (uid <= 0) {
        throw new InternalException('unexpected user id', uid)
    }
    eventSource = new EventSource(sse_backend_base + "/msg/site/user/" + uid);
    eventSource.onmessage = (event) => {
        const packet: SSEPackage = JSON.parse(event.data)
        if (packet.push_type === PackageType.packet) {
            multipleMessageHandler(packet.body as MessagePacket)
        } else if (packet.push_type === PackageType.single) {
            const body: SSEMessage = (packet.body as SSEMessage)
            EventHandlerMaps[body.body_type](body)
        } else {
            throw new InternalException('unexpected sse message type', packet)
        }
    }
    eventSource.onerror = (error) => {
        console.error(error);
        unSubscribeSSE();
        const {user} = useAuthStore()
        if (user && user.user_id > 0) {
            subscribeToSSE(user.user_id);
        }
    }
}

export function unSubscribeSSE() {
    eventSource?.close()
    eventSource = null
}

export function sseEventSubscribe(eventBus: EventBus) {
    eventBus.register(EventType.currentlyIsLoggedIn, (e: number) => {
        subscribeToSSE(e)
    })
    eventBus.register(EventType.currentlyIsLoggedOut, () => {
        unSubscribeSSE()
    })
}
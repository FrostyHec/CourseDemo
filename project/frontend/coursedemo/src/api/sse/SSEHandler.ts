import {EventBus, EventType} from "@/utils/EventBus";
import {InternalException} from "@/utils/Exceptions";
import {sse_backend_base} from "@/utils/Constant";
import {useAuthStore} from "@/stores/auth";
import { useRouter } from "vue-router";
import { ref } from "vue";

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
  
export interface AnnouncementBody extends SSEBody{
    BodyType: SSEBodyType.announcement
    course_id: number;
    course_name: string,
    announcement_id: number
    Title: string;
}

export interface NewLoginBody extends SSEBody{
    BodyType: SSEBodyType.new_login
    Token: string;
}

export interface ReceiveCreditsBody extends SSEBody{
    BodyType: SSEBodyType.receive_credits
    type: CreditType
    count:  number
}

export enum CreditType {
    daily_comment = "daily_comment",
    complete_course = "complete_course"
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

export interface EventHandler {
    (message: SSEMessage): void;
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


export function subscribeToSSE() {
    if (eventSource) {
        console.log('SSE is registered status:', eventSource.readyState)
        return
    }
    console.log('subscribing To SSE')
    const {user} = useAuthStore()
    const uid = user.user_id;
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
        subscribeToSSE();
    }
}

export function unSubscribeSSE() {
    eventSource?.close()
    eventSource = null
}

export function sseEventSubscribe(eventBus:EventBus) {
    eventBus.register(EventType.currentlyIsLoggedIn, () => {
        subscribeToSSE()
    })
    eventBus.register(EventType.currentlyIsLoggedOut, () => {
        unSubscribeSSE()
    })
}
import {useEventStore} from "@/stores/event";
import {EventType} from "@/utils/EventBus";
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
    },
    [SSEBodyType.new_login]: (message) => {
    },
    [SSEBodyType.receive_credits]: (message) => {
    },
};
/////////////messages packages///////////////
// TODO register handler here and remove nullable
const multipleMessageHandler: ((message: MessagePacket) => void) = (e) => {
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

function init() {
    const {registerEvent} = useEventStore();
    registerEvent(EventType.currentlyIsLoggedIn, () => {
        subscribeToSSE()
    })
    registerEvent(EventType.currentlyIsLoggedOut, () => {
        unSubscribeSSE()
    })
}

init()
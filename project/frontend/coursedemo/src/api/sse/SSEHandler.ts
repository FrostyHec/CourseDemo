import {useEventStore} from "@/stores/event";
import {EventBus, EventType} from "@/utils/EventBus";
import {InternalException} from "@/utils/Exceptions";
import {sse_backend_base} from "@/utils/Constant";
import {useAuthStore} from "@/stores/auth";
// import { EventSourcePolyfill } from 'event-source-polyfill';
import {AxiosAPI} from "@/utils/APIUtils";
import { useRouter } from "vue-router";
import { handleAnnouncement } from "./SSEEventHandle";

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


const EventHandlerMaps: { [key in SSEBodyType]: EventHandler } = {
    [SSEBodyType.announcement]: (message: { body: SSEBody; }) => {
      const announcementBody = message.body as AnnouncementBody;
      handleAnnouncement(`您收到了一条来自课程 ${announcementBody.course_name} 的公告：${announcementBody.Title}`);
    },
    [SSEBodyType.new_login]: (message: { body: SSEBody; }) => {
      const authStore = useAuthStore();
      const newLoginBody = message.body as NewLoginBody;
      // 校验 token 是否一致
      if (authStore.token !== newLoginBody.Token) {
        handleAnnouncement("另一个用户登录，您将被登出");
        // 执行登出操作
        authStore.logout({user_id:authStore.user.user_id});
        // 跳转到登录页面
        const router = useRouter();
        router.push('/MainPage/login');
      }
    },
    [SSEBodyType.receive_credits]: (message: { body: SSEBody; }) => {
      const receiveCreditsBody = message.body as ReceiveCreditsBody;
      handleAnnouncement(`${receiveCreditsBody.type}，积分+${receiveCreditsBody.count}`);
    },
  };


const multipleMessageHandler: ((message: MessagePacket) => void) = (packet) => {
  const authStore = useAuthStore();
  const router = useRouter();

  packet.unposed.forEach((message: { type: SSEMessageType; body: SSEBody; body_type: SSEBodyType; }) => {
    switch (message.type) {
      case SSEMessageType.NEW: {
        const body = message.body;
        switch (message.body_type) {
          case SSEBodyType.new_login: {
            const newLoginBody = body as NewLoginBody;
            if (authStore.token !== newLoginBody.Token) {
                handleAnnouncement("另一个用户登录，您将被登出");
                authStore.logout({ user_id: authStore.user.user_id });
                router.push('/login');
            }
            break;
          }
          case SSEBodyType.receive_credits: {
            const receiveCreditsBody = message.body as ReceiveCreditsBody;
            handleAnnouncement(`${receiveCreditsBody.type}，积分+${receiveCreditsBody.count}`);
            break;
          }
          case SSEBodyType.announcement: {
            const announcementBody = body as AnnouncementBody;
            handleAnnouncement(`您收到了一条来自课程 ${announcementBody.course_name} 的公告：${announcementBody.Title}`);
            break;
          }
          default:
            console.error('未知的消息类型:', message.type);
        }
        break;
      }
      case SSEMessageType.UPDATE:
        break;
      case SSEMessageType.DELETE:
        break;
      default:
        // 处理未知类型的消息
        console.error('未知的消息类型:', message.type);
    }
  });
};

export function subscribeToSSE(uid: number) {
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
    // eventSource = new EventSourcePolyfill(sse_backend_base + "/msg/site/user/" + uid, {
    //     headers: AxiosAPI.getAuthHeaderJson()
    // });
    eventSource = new EventSource(sse_backend_base + "/msg/site/user/" + uid)
    if(!eventSource){
        throw new InternalException('unexpected event source', eventSource)
    }
    eventSource.onmessage = (event) => {
        const packet: SSEPackage = JSON.parse(event.data).data
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
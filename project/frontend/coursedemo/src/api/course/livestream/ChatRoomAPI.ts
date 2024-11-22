import {InternalException} from "@/utils/Exceptions";
import type {UserPublicInfoEntity} from "@/api/user/UserAPI";

export interface OnMessageFunc {
    (message:ReceivedMessage):void
}
export interface ReceivedMessage {
    target: string,
    from_user:UserPublicInfoEntity,
    content: string,
}
export interface SendMessage {
    target: string,
    content: string,
}
class ChatRoomAPI {
    private socket: WebSocket | null = null

    constructor() {
    }

    connectWebSocket(roomId: string, userId: number, onMessageFunc:OnMessageFunc) {
        const url = `ws://localhost:9977/websocket/livestream/${roomId}/${userId}`;

        this.socket = new WebSocket(url);

        this.socket.onopen = function (event) {
            console.log("WebSocket is open now.");
        };

        this.socket.onmessage = function (event) {
            console.log("Received Message");
            const received:ReceivedMessage = JSON.parse(event.data)
            onMessageFunc(received)
        };

            this.socket.onclose = function (event) {
                console.log("WebSocket is closed now.");
            };

        this.socket.onerror = function (event) {
            console.error("WebSocket error observed:", event);
        };
    }

    sendMessage(msg:SendMessage) {
        const message = JSON.stringify(msg);
        if (this.socket) {
            this.socket.send(message);
        } else {
            throw new InternalException("socket is null")
        }
    }
}

export const chatRoomAPI = new ChatRoomAPI()
import {sseEventSubscribe} from "@/api/sse/SSEHandler";
import type {EventBus} from "@/utils/EventBus";

export function eventInitialize(eventBus:EventBus){
    sseEventSubscribe(eventBus)
}
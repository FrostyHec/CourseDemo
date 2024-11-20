import {defineStore} from "pinia";
import {EventBus} from "@/utils/EventBus";

export const useEventStore = defineStore('event', () => {
    const eventBus = new EventBus()

    return {eventBus,
        registerEvent: eventBus.on,
        emitEvent: eventBus.emit,
        removeEvent: eventBus.remove
       }
}
)
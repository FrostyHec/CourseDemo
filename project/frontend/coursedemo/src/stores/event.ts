import {defineStore} from "pinia";
import {EventBus} from "@/utils/EventBus";
import {eventInitialize} from "@/utils/EventInitialRegisteryTable";

export const useEventStore = defineStore('event', () => {
        const eventBus = new EventBus()
        eventInitialize(eventBus)
        return {
            eventBus,
            registerEvent: eventBus.register.bind(eventBus),
            emitEvent: eventBus.emit.bind(eventBus),
            removeEvent: eventBus.remove.bind(eventBus)
        }
    }
)
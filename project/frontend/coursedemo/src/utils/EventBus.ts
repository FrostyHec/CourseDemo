export class EventBus {
	private list: { [key: string]: Function[] };
	constructor() {
		this.list = {};  // 收集订阅
	}

	// 订阅
	register(name: EventType, fn: Function) {
		this.list[name] = this.list[name] || [];
		this.list[name].push(fn);
	}

	// 发布
	emit(name:EventType, param:any = null) {
		if (this.list[name]) {
			this.list[name].forEach((fn) => {
				fn(param);
			});
		}
	}

	remove(name: EventType) {
		if (this.list[name]) {
			delete this.list[name];
		}
	}
}

export enum EventType {
	currentlyIsLoggedIn = 'currentlyIsLoggedIn',
	currentlyIsLoggedOut = 'currentlyIsLoggedOut',
	quitEvent = 'quitEvent'
}
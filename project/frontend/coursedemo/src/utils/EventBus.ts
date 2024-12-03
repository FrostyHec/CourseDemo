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
		if(name==EventType.currentlyIsLoggedIn)
			console.log("emitting event:"+'login');
		if(name==EventType.currentlyIsLoggedOut)
			console.log("emitting event:"+'logout');
		
		if(name==EventType.quitEvent)
			console.log("emitting event:"+'quit');
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
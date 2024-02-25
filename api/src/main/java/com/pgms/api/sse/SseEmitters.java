package com.pgms.api.sse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SseEmitters {

	private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

	// SSE Emitter 등록 메소드
	public void addEmitter(SseEmitter emitter) {
		this.emitters.add(emitter);
		log.info(">>>>>> SSE Emitter Add {}", emitter);

		// Broken Pipe 발생 시 연결 종료 및 리소스 정리
		emitter.onError(throwable -> {
			log.warn(">>>>>> SSE Emitter on Error");
			emitter.complete();
			this.emitters.remove(emitter);
		});

		// Time-Out 발생 시 연결 종료 및 리소스 정리
		emitter.onTimeout(() -> {
			log.warn(">>>>>> SSE Emitter Timeout");
			emitter.complete();
			this.emitters.remove(emitter);
		});

		// 비동기 요청 완료시 emitter 객체 삭제
		emitter.onCompletion(() -> {
			log.warn(">>>>>> SSE Emitter Complete");
			this.emitters.remove(emitter);
		});
	}

	public void remove(SseEmitter emitter) {
		log.warn(">>>>>> SSE Emitter Removed");
		this.emitters.remove(emitter);
	}

	public void updateGameRoom(Object roomInfo) {
		log.warn(">>>>>> SSE Emitter Change GameRoom");
		emitters.forEach(emitter -> {
			try {
				emitter.send(SseEmitter.event()
					.name("changeGameRoom")
					.data(roomInfo));
			} catch (IOException e) {
				log.warn(">>>>>> SSE Emitter Change GameRoom Exception");
				emitter.complete();
				this.emitters.remove(emitter);
			}
		});
	}

	public void connect(SseEmitter emitter, Object roomInfo) {
		try {
			emitter.send(SseEmitter.event()
				.name("connect")
				.data(roomInfo));
		} catch (IOException e) {
			emitter.complete(); // 예외 발생 시 emitter를 종료
			this.emitters.remove(emitter);
		}
	}
}

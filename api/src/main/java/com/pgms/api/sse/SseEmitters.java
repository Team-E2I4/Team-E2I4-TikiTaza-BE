package com.pgms.api.sse;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SseEmitters {

	private final ConcurrentHashMap<SseEmitter, Boolean> emitters = new ConcurrentHashMap<>();
	private final ExecutorService executorService = Executors.newCachedThreadPool();

	// SSE Emitter 등록 메소드
	public void addEmitter(SseEmitter emitter) {
		this.emitters.put(emitter, true);
		log.info(">>>>>> SSE Emitter Add {}", emitter);

		// Broken Pipe 발생 시 연결 종료 및 리소스 정리
		emitter.onError(throwable -> handleEmitterError(emitter));

		// Time-Out 발생 시 연결 종료 및 리소스 정리
		emitter.onTimeout(() -> handleEmitterError(emitter));

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
		for (SseEmitter emitter : emitters.keySet()) {
			CompletableFuture.runAsync(() -> {
				try {
					emitter.send(SseEmitter.event()
						.name("changeGameRoom")
						.data(roomInfo));
				} catch (IOException e) {
					handleEmitterError(emitter);
				}
			}, executorService);
		}
	}

	public void connect(SseEmitter emitter, Object roomInfo) {
		try {
			emitter.send(SseEmitter.event()
				.name("connect")
				.data(roomInfo));
		} catch (IOException e) {
			handleEmitterError(emitter);
		}
	}

	private void handleEmitterError(SseEmitter emitter) {
		log.warn(">>>>>> SSE Emitter Error");
		emitter.complete();
		this.emitters.remove(emitter);
	}
}

package com.pgms.api.socket.dto.request;

public record GameFinishRequest(Long currentRound, double cpm, double accuracy) {
}

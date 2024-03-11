package com.pgms.coreinfraredis.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "랭킹 응답", requiredProperties = {"nickname", "ranking", "score"})
public record RankingResponse(String nickname, Long ranking, Double score) {
}

package com.pgms.apigame.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageCondition {
	private static final Integer DEFAULT_PAGE = 1;
	private static final Integer DEFAULT_SIZE = 10;

	@Min(value = 1, message = "페이지는 1보다 작을 수 없습니다.")
	@Max(value = 100, message = "페이지는 100을 넘을 수 없습니다.")
	private Integer page;

	@Min(value = 1, message = "사이즈는 1보다 작을 수 없습니다.")
	@Max(value = 100, message = "사이즈는 100을 넘을 수 없습니다.")
	private Integer size;

	public PageCondition(Integer page, Integer size) {
		this.page = Boolean.TRUE.equals(isValidPage(page)) ? page : DEFAULT_PAGE;
		this.size = Boolean.TRUE.equals(isValidSize(size)) ? size : DEFAULT_SIZE;
	}

	@Hidden
	public Pageable getPageable() {
		return PageRequest.of(this.page - 1, this.size);
	}

	private Boolean isValidPage(Integer page) {
		return page != null && page > 0;
	}

	private Boolean isValidSize(Integer size) {
		return size != null && size > 0;
	}
}

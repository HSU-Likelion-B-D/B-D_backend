package com.likelion.bd.global.response.code.ai;

import com.likelion.bd.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AiErrorResponseCode implements BaseResponseCode {

    KEYWORDS_NOT_FOUND_404("KEYWORDS_NOT_FOUND_404", 404, "추출할 키워드가 리뷰에 존재하지 않습니다."),
    KEYWORD_EXTRACTION_FAILED_500("KEYWORD_EXTRACTION_FAILED_500", 500, "외부 API 연동 중 오류가 발생하여 키워드 추출에 실패했습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}

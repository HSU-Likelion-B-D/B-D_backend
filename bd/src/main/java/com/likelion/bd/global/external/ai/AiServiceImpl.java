package com.likelion.bd.global.external.ai;

import com.likelion.bd.global.config.OpenAiConfig;
import com.likelion.bd.global.exception.CustomException;
import com.likelion.bd.global.external.ai.dto.AiDto;
import com.likelion.bd.global.response.code.ai.AiErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final RestTemplate restTemplate;
    private final OpenAiConfig openAiConfig;

    @Override
    public String extractKeywords(String text) {
        String prompt = createPrompt(text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiConfig.getApiKey());

        AiDto.Request request = new AiDto.Request(openAiConfig.getModel(), prompt);
        HttpEntity<AiDto.Request> entity = new HttpEntity<>(request, headers);

        try {
            AiDto.Response response = restTemplate.postForObject(
                    openAiConfig.getApiUrl(),
                    entity,
                    AiDto.Response.class
            );

            if (response != null && !response.choices().isEmpty()) {
                return response.choices().getFirst().message().content();
            }
            throw new CustomException(AiErrorResponseCode.KEYWORDS_NOT_FOUND_404);
        } catch (Exception e) {
            throw new CustomException(AiErrorResponseCode.KEYWORD_EXTRACTION_FAILED_500);
        }
    }

    private String createPrompt(String text) {
        return String.format(
                """
                너는 사용자의 평판, 리뷰를 분석하여 특징을 요약하는 AI야.
                너의 임무는 아래 리뷰들에서 음식, 메뉴, 분위기, 인테리어, 서비스, 친절도, 가격, 가성비 등과 관련된
                긍정적인 핵심 표현을 1개에서 5개까지 추출하는 거야.
                
                [분석할 리뷰]
                %s
                        
                [출력 규칙]
                - 형식: `핵심` 표현 형식이어야 해.
                - 구분자: 각 표현은 쉼표(,)로 구분해야 해.
                - 내용: '친절해요', '맛있어요', '깔끔해요' 와 같이 간결한 핵심 표현 (형용사/동사 위주)으로 추출해줘.
                        리뷰 내용이 목록의 표현과 유사한 의미를 가지면, 가장 적합한 표준 키워드로 분류하고 횟수를 합산해줘.
                        (예: 답변이 빨라요 -> 응답이 빨라요, 결과물이 좋아요 -> 퀄리티가 좋아요)
                - 순서: 반드시 추출된 키워드들을 키워드 언급이 많았던 순으로 내림차순 정렬해야 해.
                - 분리: '친절하고 전문적이다'와 같이 여러 특징이 한 문장에 있으면, 각각 별개의 키워드로 분리해야 해. (예: 친절해요, 전문적이에요)
                - 예외: 다른 어떤 설명, 인사, 번호도 붙이지 말고 오직 결과만 출력해야 해.
                               
                [출력 예시]
                친절해요, 퀄리티가 좋아요, 응답이 빨라요, 전문적이에요, 만족스러워요
                """, text
        );
    }
}
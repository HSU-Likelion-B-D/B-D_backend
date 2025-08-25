package com.likelion.bd.global.external.ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * OpenAI API 에 요청을 보낼 때 사용하는 DTO
 */
public class AiDto {

    /**
     * OpenAI API에 요청을 보낼 때 사용하는 DTO
     */
    @Getter
    @NoArgsConstructor
    public static class Request {
        private String model;
        private List<Message> messages;

        public Request(String model, String prompt) {
            this.model = model;
            this.messages = List.of(new Message("user", prompt));
        }
    }

    /**
     * OpenAI API로부터 응답을 받을 때 사용하는 DTO
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(List<Choice> choices) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Choice(Message message) {}

    public record Message(String role, String content) {}
}



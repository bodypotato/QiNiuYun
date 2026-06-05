package com.body.aiscript.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * DeepSeek API 请求（OpenAI 兼容格式）
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeepSeekRequest {

    private String model;
    private List<Message> messages;
    private Double temperature;
    @JsonProperty("max_tokens")
    private Integer maxTokens;
    private Boolean stream = false;

    public DeepSeekRequest() {}

    public DeepSeekRequest(String model, List<Message> messages, Double temperature, Integer maxTokens) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
    }



    /**
     * 聊天消息
     */
    @Data
    public static class Message {
        private String role;     // system | user | assistant
        private String content;

        public Message() {}

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}

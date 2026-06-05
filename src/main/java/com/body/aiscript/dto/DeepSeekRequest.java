package com.body.aiscript.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DeepSeek API 请求（OpenAI 兼容格式）
 */
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

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }

    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public Integer getMaxTokens() { return maxTokens; }
    public void setMaxTokens(Integer maxTokens) { this.maxTokens = maxTokens; }

    public Boolean getStream() { return stream; }
    public void setStream(Boolean stream) { this.stream = stream; }

    /**
     * 聊天消息
     */
    public static class Message {
        private String role;     // system | user | assistant
        private String content;

        public Message() {}

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}

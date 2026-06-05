package com.body.aiscript.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * DeepSeek API 响应（OpenAI 兼容格式）
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeepSeekResponse {

    private String id;
    private List<Choice> choices;
    private Usage usage;



    /**
     * 提取第一个 choice 的文本内容
     */
    public String getFirstContent() {
        if (choices != null && !choices.isEmpty()
                && choices.get(0).message != null) {
            return choices.get(0).message.content;
        }
        return null;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private int index;
        private Message message;
        @JsonProperty("finish_reason")
        private String finishReason;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        private String role;
        private String content;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;
        @JsonProperty("completion_tokens")
        private int completionTokens;
        @JsonProperty("total_tokens")
        private int totalTokens;

    }
}

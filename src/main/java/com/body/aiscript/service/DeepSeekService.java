package com.body.aiscript.service;

import com.body.aiscript.config.AppConfig;
import com.body.aiscript.dto.DeepSeekRequest;
import com.body.aiscript.dto.DeepSeekResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * DeepSeek API 调用服务
 *
 * DeepSeek API 兼容 OpenAI Chat Completions 格式：
 * POST https://api.deepseek.com/v1/chat/completions
 */
@Service
public class DeepSeekService {

    private static final Logger log = LoggerFactory.getLogger(DeepSeekService.class);

    private final RestTemplate restTemplate;
    private final AppConfig config;

    public DeepSeekService(RestTemplate restTemplate, AppConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    /**
     * 发送聊天请求到 DeepSeek
     *
     * @param systemPrompt 系统提示词
     * @param userMessage  用户消息
     * @param temperature  温度（0-2），默认 0.7
     * @param maxTokens    最大输出 token 数
     * @return API 响应内容
     */
    public String chat(String systemPrompt, String userMessage, Double temperature, Integer maxTokens) {
        String url = config.getDeepseekApiUrl() + "/v1/chat/completions";

        DeepSeekRequest body = new DeepSeekRequest(
                config.getDeepseekModel(),
                List.of(
                        new DeepSeekRequest.Message("system", systemPrompt),
                        new DeepSeekRequest.Message("user", userMessage)
                ),
                temperature != null ? temperature : 0.7,
                maxTokens != null ? maxTokens : 16000
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(config.getDeepseekApiKey());

        HttpEntity<DeepSeekRequest> request = new HttpEntity<>(body, headers);

        log.info("Calling DeepSeek API: model={}, maxTokens={}, temperature={}",
                config.getDeepseekModel(), body.getMaxTokens(), body.getTemperature());

        try {
            ResponseEntity<DeepSeekResponse> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, DeepSeekResponse.class);

            if (response.getBody() == null) {
                throw new RuntimeException("DeepSeek API 返回空响应");
            }

            String content = response.getBody().getFirstContent();
            if (content == null || content.isBlank()) {
                throw new RuntimeException("DeepSeek API 返回空内容，finish_reason 可能不是 stop");
            }

            DeepSeekResponse.Usage usage = response.getBody().getUsage();
            if (usage != null) {
                log.info("DeepSeek API 调用成功: prompt_tokens={}, completion_tokens={}, total_tokens={}",
                        usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());
            }

            return content;

        } catch (RestClientException e) {
            log.error("DeepSeek API 调用失败: {}", e.getMessage());
            throw new RuntimeException("DeepSeek API 调用失败: " + e.getMessage(), e);
        }
    }
}

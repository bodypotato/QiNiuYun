package com.body.aiscript.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * AI 改写响应
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RewriteResponse {

    private boolean success;
    private String message;

    /** 改写后的文本 */
    private String rewrittenText;

    /** 改写建议/说明（AI 可能提供改写理由） */
    private String suggestion;

    /** 消耗的 token 数 */
    private Integer tokensUsed;

    public static RewriteResponse ok(String rewrittenText, String suggestion, int tokensUsed) {
        RewriteResponse r = new RewriteResponse();
        r.success = true;
        r.message = "改写完成";
        r.rewrittenText = rewrittenText;
        r.suggestion = suggestion;
        r.tokensUsed = tokensUsed;
        return r;
    }

    public static RewriteResponse fail(String message) {
        RewriteResponse r = new RewriteResponse();
        r.success = false;
        r.message = message;
        return r;
    }
}

package com.body.aiscript.dto;

import com.body.aiscript.model.Script;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 转换响应
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConversionResponse {

    private boolean success;
    private String message;
    private Script script;
    private String rawYaml;    // 原始 YAML 文本（方便调试）
    private int inputWordCount;
    private int outputSceneCount;
    private String modelUsed;
    private long processingTimeMs;

    // --- Factory methods ---

    public static ConversionResponse ok(Script script, String rawYaml) {
        ConversionResponse r = new ConversionResponse();
        r.success = true;
        r.message = "转换成功";
        r.script = script;
        r.rawYaml = rawYaml;
        return r;
    }

    public static ConversionResponse fail(String message) {
        ConversionResponse r = new ConversionResponse();
        r.success = false;
        r.message = message;
        return r;
    }
}

package com.body.aiscript.dto;

import com.body.aiscript.model.Script;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 转换响应
 */
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

    // --- Getters & Setters ---

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Script getScript() { return script; }
    public void setScript(Script script) { this.script = script; }

    public String getRawYaml() { return rawYaml; }
    public void setRawYaml(String rawYaml) { this.rawYaml = rawYaml; }

    public int getInputWordCount() { return inputWordCount; }
    public void setInputWordCount(int inputWordCount) { this.inputWordCount = inputWordCount; }

    public int getOutputSceneCount() { return outputSceneCount; }
    public void setOutputSceneCount(int outputSceneCount) { this.outputSceneCount = outputSceneCount; }

    public String getModelUsed() { return modelUsed; }
    public void setModelUsed(String modelUsed) { this.modelUsed = modelUsed; }

    public long getProcessingTimeMs() { return processingTimeMs; }
    public void setProcessingTimeMs(long processingTimeMs) { this.processingTimeMs = processingTimeMs; }
}

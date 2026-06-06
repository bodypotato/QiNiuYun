package com.body.aiscript.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

/**
 * AI 改写请求
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RewriteRequest {

    /** 原始内容块的文本 */
    private String originalText;

    /** 内容块类型：action | dialogue | transition | note */
    private String blockType;

    /** 用户的改写指令，如"更简洁""更悲伤""加一个比喻" */
    private String instruction;

    /** 改写风格偏好（可选） */
    private String style;

    // --- 上下文信息（帮助 AI 理解场景） ---

    /** 场景标题 */
    private String sceneTitle;

    /** 场景地点 */
    private String sceneLocation;

    /** 场景时间 */
    private String sceneTime;

    /** 场景氛围 */
    private String sceneMood;

    /** 说话人角色名（仅在改写对话时提供） */
    private String characterName;

    /** 说话人性格特质（仅在改写对话时提供） */
    private String characterPersonality;

    /** 当前场景中的其他对话/动作（可选，帮助 AI 理解上下文） */
    private String surroundingText;
}

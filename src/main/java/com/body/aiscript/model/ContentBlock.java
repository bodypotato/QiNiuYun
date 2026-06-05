package com.body.aiscript.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 内容块——场景中的最小叙事单元
 */
@Data
@Accessors(chain = true)
@JsonPropertyOrder({"type", "character", "character_name", "text", "emotion", "subtext", "camera", "duration_hint", "metadata"})
public class ContentBlock {

    public enum BlockType {
        @JsonProperty("action")     ACTION,
        @JsonProperty("dialogue")   DIALOGUE,
        @JsonProperty("transition") TRANSITION,
        @JsonProperty("note")       NOTE
    }

    @JsonProperty("type")
    private BlockType type;

    @JsonProperty("character")
    private String character;  // 说话人角色 id（仅 dialogue）

    @JsonProperty("character_name")
    private String characterName;  // 说话人显示名（仅 dialogue）

    @JsonProperty("text")
    private String text;

    @JsonProperty("emotion")
    private String emotion;

    @JsonProperty("subtext")
    private String subtext;

    @JsonProperty("camera")
    private String camera;

    @JsonProperty("duration_hint")
    private String durationHint;

    @JsonProperty("metadata")
    private java.util.Map<String, Object> metadata;

    public ContentBlock() {}

    public ContentBlock(BlockType type, String text) {
        this.type = type;
        this.text = text;
    }

}

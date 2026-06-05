package com.body.aiscript.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * 内容块——场景中的最小叙事单元
 */
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

    // --- Builder-style setters ---

    public ContentBlock setType(BlockType type) { this.type = type; return this; }
    public ContentBlock setCharacter(String character) { this.character = character; return this; }
    public ContentBlock setCharacterName(String characterName) { this.characterName = characterName; return this; }
    public ContentBlock setText(String text) { this.text = text; return this; }
    public ContentBlock setEmotion(String emotion) { this.emotion = emotion; return this; }
    public ContentBlock setSubtext(String subtext) { this.subtext = subtext; return this; }
    public ContentBlock setCamera(String camera) { this.camera = camera; return this; }
    public ContentBlock setDurationHint(String durationHint) { this.durationHint = durationHint; return this; }
    public ContentBlock setMetadata(java.util.Map<String, Object> metadata) { this.metadata = metadata; return this; }

    // --- Getters ---
    public BlockType getType() { return type; }
    public String getCharacter() { return character; }
    public String getCharacterName() { return characterName; }
    public String getText() { return text; }
    public String getEmotion() { return emotion; }
    public String getSubtext() { return subtext; }
    public String getCamera() { return camera; }
    public String getDurationHint() { return durationHint; }
    public java.util.Map<String, Object> getMetadata() { return metadata; }
}

package com.body.aiscript.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.Instant;
import java.util.List;

/**
 * 剧本元信息
 */
@JsonPropertyOrder({
    "title", "original_title", "original_author", "script_version",
    "genre", "sub_genre", "tags", "language",
    "source_chapters", "word_count", "created_at", "notes"
})
public class ScriptMeta {

    @JsonProperty("title")
    private String title;

    @JsonProperty("original_title")
    private String originalTitle;

    @JsonProperty("original_author")
    private String originalAuthor;

    @JsonProperty("script_version")
    private String scriptVersion;

    @JsonProperty("genre")
    private String genre;

    @JsonProperty("sub_genre")
    private String subGenre;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("language")
    private String language = "zh-CN";

    @JsonProperty("source_chapters")
    private Integer sourceChapters;

    @JsonProperty("word_count")
    private Integer wordCount;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("notes")
    private String notes;

    public ScriptMeta() {}

    // --- Builder-style setters ---

    public ScriptMeta setTitle(String title) { this.title = title; return this; }
    public ScriptMeta setOriginalTitle(String originalTitle) { this.originalTitle = originalTitle; return this; }
    public ScriptMeta setOriginalAuthor(String originalAuthor) { this.originalAuthor = originalAuthor; return this; }
    public ScriptMeta setScriptVersion(String scriptVersion) { this.scriptVersion = scriptVersion; return this; }
    public ScriptMeta setGenre(String genre) { this.genre = genre; return this; }
    public ScriptMeta setSubGenre(String subGenre) { this.subGenre = subGenre; return this; }
    public ScriptMeta setTags(List<String> tags) { this.tags = tags; return this; }
    public ScriptMeta setLanguage(String language) { this.language = language; return this; }
    public ScriptMeta setSourceChapters(Integer sourceChapters) { this.sourceChapters = sourceChapters; return this; }
    public ScriptMeta setWordCount(Integer wordCount) { this.wordCount = wordCount; return this; }
    public ScriptMeta setCreatedAt(String createdAt) { this.createdAt = createdAt; return this; }
    public ScriptMeta setNotes(String notes) { this.notes = notes; return this; }

    // --- Getters ---
    public String getTitle() { return title; }
    public String getOriginalTitle() { return originalTitle; }
    public String getOriginalAuthor() { return originalAuthor; }
    public String getScriptVersion() { return scriptVersion; }
    public String getGenre() { return genre; }
    public String getSubGenre() { return subGenre; }
    public List<String> getTags() { return tags; }
    public String getLanguage() { return language; }
    public Integer getSourceChapters() { return sourceChapters; }
    public Integer getWordCount() { return wordCount; }
    public String getCreatedAt() { return createdAt; }
    public String getNotes() { return notes; }
}

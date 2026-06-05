package com.body.aiscript.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

/**
 * 剧本元信息
 */
@Data
@Accessors(chain = true)
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

}

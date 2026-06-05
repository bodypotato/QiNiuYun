package com.body.aiscript.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 场——剧本的基本结构单元，通常对应一个地点/一段连续时间内的故事
 */
@Data
@Accessors(chain = true)
@JsonPropertyOrder({
    "scene_number", "scene_title", "location", "time", "time_period",
    "mood", "lighting", "characters_present", "source_chapter",
    "synopsis", "content"
})
public class Scene {

    @JsonProperty("scene_number")
    private int sceneNumber;

    @JsonProperty("scene_title")
    private String sceneTitle;

    @JsonProperty("location")
    private String location;

    @JsonProperty("time")
    private String time;

    @JsonProperty("time_period")
    private String timePeriod;

    @JsonProperty("mood")
    private String mood;

    @JsonProperty("lighting")
    private String lighting;

    @JsonProperty("characters_present")
    private List<String> charactersPresent;

    @JsonProperty("source_chapter")
    private Integer sourceChapter;

    @JsonProperty("synopsis")
    private String synopsis;

    @JsonProperty("content")
    private List<ContentBlock> content;

}

package com.body.aiscript.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * 场——剧本的基本结构单元，通常对应一个地点/一段连续时间内的故事
 */
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

    public Scene() {}

    // --- Builder-style setters ---

    public Scene setSceneNumber(int sceneNumber) { this.sceneNumber = sceneNumber; return this; }
    public Scene setSceneTitle(String sceneTitle) { this.sceneTitle = sceneTitle; return this; }
    public Scene setLocation(String location) { this.location = location; return this; }
    public Scene setTime(String time) { this.time = time; return this; }
    public Scene setTimePeriod(String timePeriod) { this.timePeriod = timePeriod; return this; }
    public Scene setMood(String mood) { this.mood = mood; return this; }
    public Scene setLighting(String lighting) { this.lighting = lighting; return this; }
    public Scene setCharactersPresent(List<String> charactersPresent) { this.charactersPresent = charactersPresent; return this; }
    public Scene setSourceChapter(Integer sourceChapter) { this.sourceChapter = sourceChapter; return this; }
    public Scene setSynopsis(String synopsis) { this.synopsis = synopsis; return this; }
    public Scene setContent(List<ContentBlock> content) { this.content = content; return this; }

    // --- Getters ---
    public int getSceneNumber() { return sceneNumber; }
    public String getSceneTitle() { return sceneTitle; }
    public String getLocation() { return location; }
    public String getTime() { return time; }
    public String getTimePeriod() { return timePeriod; }
    public String getMood() { return mood; }
    public String getLighting() { return lighting; }
    public List<String> getCharactersPresent() { return charactersPresent; }
    public Integer getSourceChapter() { return sourceChapter; }
    public String getSynopsis() { return synopsis; }
    public List<ContentBlock> getContent() { return content; }
}

package com.body.aiscript.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * 幕——由若干场组成，代表一个完整的叙事段落
 */
@JsonPropertyOrder({"act_number", "title", "synopsis", "scenes"})
public class Act {

    @JsonProperty("act_number")
    private int actNumber;

    @JsonProperty("title")
    private String title;

    @JsonProperty("synopsis")
    private String synopsis;

    @JsonProperty("scenes")
    private List<Scene> scenes;

    public Act() {}

    public Act setActNumber(int actNumber) { this.actNumber = actNumber; return this; }
    public Act setTitle(String title) { this.title = title; return this; }
    public Act setSynopsis(String synopsis) { this.synopsis = synopsis; return this; }
    public Act setScenes(List<Scene> scenes) { this.scenes = scenes; return this; }

    public int getActNumber() { return actNumber; }
    public String getTitle() { return title; }
    public String getSynopsis() { return synopsis; }
    public List<Scene> getScenes() { return scenes; }
}

package com.body.aiscript.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 幕——由若干场组成，代表一个完整的叙事段落
 */
@Data
@Accessors(chain = true)
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

}

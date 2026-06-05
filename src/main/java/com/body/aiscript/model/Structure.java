package com.body.aiscript.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * 剧本结构——包含多幕
 */
@JsonPropertyOrder({"acts"})
public class Structure {

    @JsonProperty("acts")
    private List<Act> acts;

    public Structure() {}

    public Structure setActs(List<Act> acts) { this.acts = acts; return this; }
    public List<Act> getActs() { return acts; }
}

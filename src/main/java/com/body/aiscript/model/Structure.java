package com.body.aiscript.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 剧本结构——包含多幕
 */
@Data
@Accessors(chain = true)
@JsonPropertyOrder({"acts"})
public class Structure {

    @JsonProperty("acts")
    private List<Act> acts;

}

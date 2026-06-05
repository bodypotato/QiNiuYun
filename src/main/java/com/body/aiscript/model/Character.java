package com.body.aiscript.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色定义——与场景内容分离，场景中通过 id 引用
 */
@Data
@Accessors(chain = true)
@JsonPropertyOrder({
    "id", "name", "alias", "role", "archetype",
    "gender", "age", "appearance", "personality",
    "background", "motivation", "arc", "relationships", "notes"
})
public class Character {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("alias")
    private List<String> alias;

    @JsonProperty("role")
    private String role;  // protagonist | antagonist | supporting | minor | cameo

    @JsonProperty("archetype")
    private String archetype;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("age")
    private String age;

    @JsonProperty("appearance")
    private String appearance;

    @JsonProperty("personality")
    private List<String> personality;

    @JsonProperty("background")
    private String background;

    @JsonProperty("motivation")
    private String motivation;

    @JsonProperty("arc")
    private String arc;

    @JsonProperty("relationships")
    private List<CharacterRelationship> relationships;

    @JsonProperty("notes")
    private String notes;

    public Character() {}

    /**
     * 角色关系
     */
    @Data
    public static class CharacterRelationship {

        @JsonProperty("target")
        private String target;

        @JsonProperty("type")
        private String type;

        @JsonProperty("description")
        private String description;

        public CharacterRelationship() {}

        public CharacterRelationship(String target, String type, String description) {
            this.target = target;
            this.type = type;
            this.description = description;
        }
    }
}

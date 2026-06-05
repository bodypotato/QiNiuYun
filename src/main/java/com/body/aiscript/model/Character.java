package com.body.aiscript.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色定义——与场景内容分离，场景中通过 id 引用
 */
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

    // --- Builder-style setters ---

    public Character setId(String id) { this.id = id; return this; }
    public Character setName(String name) { this.name = name; return this; }
    public Character setAlias(List<String> alias) { this.alias = alias; return this; }
    public Character setRole(String role) { this.role = role; return this; }
    public Character setArchetype(String archetype) { this.archetype = archetype; return this; }
    public Character setGender(String gender) { this.gender = gender; return this; }
    public Character setAge(String age) { this.age = age; return this; }
    public Character setAppearance(String appearance) { this.appearance = appearance; return this; }
    public Character setPersonality(List<String> personality) { this.personality = personality; return this; }
    public Character setBackground(String background) { this.background = background; return this; }
    public Character setMotivation(String motivation) { this.motivation = motivation; return this; }
    public Character setArc(String arc) { this.arc = arc; return this; }
    public Character setRelationships(List<CharacterRelationship> relationships) { this.relationships = relationships; return this; }
    public Character setNotes(String notes) { this.notes = notes; return this; }

    // --- Getters ---
    public String getId() { return id; }
    public String getName() { return name; }
    public List<String> getAlias() { return alias; }
    public String getRole() { return role; }
    public String getArchetype() { return archetype; }
    public String getGender() { return gender; }
    public String getAge() { return age; }
    public String getAppearance() { return appearance; }
    public List<String> getPersonality() { return personality; }
    public String getBackground() { return background; }
    public String getMotivation() { return motivation; }
    public String getArc() { return arc; }
    public List<CharacterRelationship> getRelationships() { return relationships; }
    public String getNotes() { return notes; }

    /**
     * 角色关系
     */
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

        public String getTarget() { return target; }
        public String getType() { return type; }
        public String getDescription() { return description; }

        public void setTarget(String target) { this.target = target; }
        public void setType(String type) { this.type = type; }
        public void setDescription(String description) { this.description = description; }
    }
}

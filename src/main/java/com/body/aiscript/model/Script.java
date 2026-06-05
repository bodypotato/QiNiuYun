package com.body.aiscript.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 剧本——顶层数据对象，包含元信息、角色列表和叙事结构
 */
@Data
@Accessors(chain = true)
@JsonPropertyOrder({"meta", "characters", "structure", "revision_history"})
public class Script {

    @JsonProperty("meta")
    private ScriptMeta meta;

    @JsonProperty("characters")
    private List<Character> characters;

    @JsonProperty("structure")
    private Structure structure;

    @JsonProperty("revision_history")
    private List<RevisionEntry> revisionHistory;

    /**
     * 修订历史条目
     */
    @Data
    public static class RevisionEntry {
        @JsonProperty("version") private String version;
        @JsonProperty("date") private String date;
        @JsonProperty("author") private String author;
        @JsonProperty("changes") private String changes;

        public RevisionEntry() {}

        public RevisionEntry(String version, String date, String author, String changes) {
            this.version = version;
            this.date = date;
            this.author = author;
            this.changes = changes;
        }

    }
}

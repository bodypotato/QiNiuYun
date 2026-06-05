package com.body.aiscript.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * 剧本——顶层数据对象，包含元信息、角色列表和叙事结构
 */
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

    public Script() {}

    public Script setMeta(ScriptMeta meta) { this.meta = meta; return this; }
    public Script setCharacters(List<Character> characters) { this.characters = characters; return this; }
    public Script setStructure(Structure structure) { this.structure = structure; return this; }
    public Script setRevisionHistory(List<RevisionEntry> revisionHistory) { this.revisionHistory = revisionHistory; return this; }

    public ScriptMeta getMeta() { return meta; }
    public List<Character> getCharacters() { return characters; }
    public Structure getStructure() { return structure; }
    public List<RevisionEntry> getRevisionHistory() { return revisionHistory; }

    /**
     * 修订历史条目
     */
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

        public String getVersion() { return version; }
        public String getDate() { return date; }
        public String getAuthor() { return author; }
        public String getChanges() { return changes; }

        public void setVersion(String version) { this.version = version; }
        public void setDate(String date) { this.date = date; }
        public void setAuthor(String author) { this.author = author; }
        public void setChanges(String changes) { this.changes = changes; }
    }
}

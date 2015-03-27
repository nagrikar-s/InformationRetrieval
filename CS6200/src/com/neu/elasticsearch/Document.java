package com.neu.elasticsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;


public class Document {
    @JsonProperty("id")
    public final String id;
    @JsonProperty("text")
    public final String text;
    @JsonProperty("fileId")
    public final String fileId;

    public Document(String id, String fileId, String text) {
        this.id = id;
        this.text = text;
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("fileId", fileId).add("text", text).toString();
    }
}

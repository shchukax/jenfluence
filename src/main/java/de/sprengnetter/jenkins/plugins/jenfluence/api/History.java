package de.sprengnetter.jenkins.plugins.jenfluence.api;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class History {

    @JsonProperty("latest")
    private boolean latest;

    @JsonProperty("createdBy")
    private CreatedBy createdBy;

    @JsonProperty("createdDate")
    private String createdDate;

    @JsonProperty("_links")
    private Map<String, String> links;

    @JsonProperty("_expandable")
    private Map<String, String> expandable;

    @JsonIgnore
    private Map<String, Object> unmappedFields = new HashMap<>();

    public boolean isLatest() {
        return latest;
    }

    public void setLatest(boolean latest) {
        this.latest = latest;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public Map<String, Object> getUnmappedFields() {
        return unmappedFields;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    public Map<String, String> getExpandable() {
        return expandable;
    }

    public void setExpandable(Map<String, String> expandable) {
        this.expandable = expandable;
    }

    @JsonAnySetter
    public void setUnmappedFields(String name, Object o) {
        this.unmappedFields = unmappedFields;
    }

    @Override
    public String toString() {
        return "History{" +
                "latest=" + latest +
                ", createdBy=" + createdBy +
                ", createdDate='" + createdDate + '\'' +
                ", links=" + links +
                ", expandable=" + expandable +
                ", unmappedFields=" + unmappedFields +
                '}';
    }
}

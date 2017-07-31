package de.sprengnetter.jenkins.plugins.jenfluence.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result implements Serializable {

    private static final long serialVersionUID = 3172384008350406887L;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("status")
    private String status;

    @JsonProperty("title")
    private String title;

    @JsonProperty("extensions")
    private Extensions extensions;

    @JsonProperty("_links")
    private Links links;

    @JsonProperty("_expandable")
    private Map<String, String> expandable = new HashMap<>();

    private Map<String, Object> unmappedFields = new HashMap<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Extensions getExtensions() {
        return extensions;
    }

    public void setExtensions(Extensions extensions) {
        this.extensions = extensions;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Map<String, String> getExpandable() {
        return expandable;
    }

    public void setExpandable(Map<String, String> expandable) {
        this.expandable = expandable;
    }

    @JsonAnyGetter
    public Map<String, Object> getUnmappedFields() {
        return unmappedFields;
    }

    @JsonAnySetter
    public void setUnmappedFields(final String name, final Object o) {
        unmappedFields.put(name, o);
    }

    @Override
    public String toString() {
        return "Result{" +
            "id=" + id +
            ", type='" + type + '\'' +
            ", status='" + status + '\'' +
            ", title='" + title + '\'' +
            ", extensions=" + extensions +
            ", links=" + links +
            ", expandables=" + expandable +
            ", unmappedFields=" + unmappedFields +
            '}';
    }
}

package de.sprengnetter.jenkins.plugins.jenfluence.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Page implements Serializable {

    private static final long serialVersionUID = 2988711873193354989L;

    @JsonProperty("type")
    private String type;

    @JsonProperty("id")
    private String id;

    @JsonProperty("version")
    private Version version;

    @JsonProperty("title")
    private String title;

    @JsonProperty("space")
    private Space space;

    @JsonProperty("body")
    private Body body;

    @JsonProperty("status")
    private String status;

    @JsonProperty("ancestors")
    private List<Ancestor> ancestors;

    private Map<String, Object> unmappedFields = new HashMap<>();

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public List<Ancestor> getAncestors() {
        return ancestors;
    }

    public void setAncestors(List<Ancestor> ancestors) {
        this.ancestors = ancestors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public void setUnmappedFields(Map<String, Object> unmappedFields) {
        this.unmappedFields = unmappedFields;
    }

    public Map<String, Object> getUnmappedFields() {
        return unmappedFields;
    }

    @JsonAnySetter
    public void setUnmappedFields(String name, Object o) {
        this.unmappedFields.put(name, o);
    }

    public void clearUnmappedFields() {
        this.unmappedFields = null;
    }

    @Override
    public String toString() {
        return "Page{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", space=" + space +
                ", body=" + body +
                ", ancestors=" + ancestors +
                '}';
    }
}

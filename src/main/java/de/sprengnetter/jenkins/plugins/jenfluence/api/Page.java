package de.sprengnetter.jenkins.plugins.jenfluence.api;

import java.io.Serializable;
import java.util.List;
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

    @JsonProperty("title")
    private String title;

    @JsonProperty("space")
    private Space space;

    @JsonProperty("body")
    private Body body;

    @JsonProperty("ancestors")
    private List<Ancestor> ancestors;

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

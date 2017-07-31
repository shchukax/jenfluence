package de.sprengnetter.jenkins.plugins.jenfluence.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Links implements Serializable {

    private static final long serialVersionUID = -3865404921928369686L;

    @JsonProperty("webui")
    private String webUi;

    @JsonProperty("tinyui")
    private String tinyUi;

    @JsonProperty("self")
    private String self;

    private Map<String, Object> unmappedFields = new HashMap<>();

    public String getWebUi() {
        return webUi;
    }

    public void setWebUi(String webUi) {
        this.webUi = webUi;
    }

    public String getTinyUi() {
        return tinyUi;
    }

    public void setTinyUi(String tinyUi) {
        this.tinyUi = tinyUi;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public Map<String, Object> getUnmappedFields() {
        return unmappedFields;
    }

    @JsonAnySetter
    public void setUnmappedFields(String name, Object o) {
        this.unmappedFields.put(name, o);
    }

    @Override
    public String toString() {
        return "Links{" +
            "webUi='" + webUi + '\'' +
            ", tinyUi='" + tinyUi + '\'' +
            ", self='" + self + '\'' +
            ", unmappedFields=" + unmappedFields +
            '}';
    }
}

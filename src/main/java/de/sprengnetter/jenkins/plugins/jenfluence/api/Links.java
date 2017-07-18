package de.sprengnetter.jenkins.plugins.jenfluence.api;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Links {

    @JsonProperty("webui")
    private String webUi;

    @JsonProperty("tinyui")
    private String tinyUi;

    @JsonProperty("self")
    private String self;

    private Map<String, Object> unknownFields = new HashMap<>();

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

    @JsonAnyGetter
    public Map<String, Object> getUnknownFields() {
        return unknownFields;
    }

    @JsonAnySetter
    public void setUnknownFields(final String name, final Object o) {
        unknownFields.put(name, o);
    }
}

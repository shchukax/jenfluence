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
public class Storage {

    @JsonProperty("value")
    private String value;

    @JsonProperty("representation")
    private String representation;

    private Map<String, Object> unknownFields = new HashMap<>();

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRepresentation() {
        return representation;
    }

    public void setRepresentation(String representation) {
        this.representation = representation;
    }

    @JsonAnyGetter
    public Map<String, Object> getUnkownFields() {
        return unknownFields;
    }

    @JsonAnySetter
    public void setUnkownFields(final String name, final Object o) {
        unknownFields.put(name, o);
    }
}

package de.sprengnetter.jenkins.plugins.jenfluence.api;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Space implements Serializable {

    @JsonProperty("key")
    private String key;

    @JsonIgnore
    private Map<String, Object> unmappedFields = new HashMap<>();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
        return "Space{" +
                "key='" + key + '\'' +
                ", unmappedFields=" + unmappedFields +
                '}';
    }
}

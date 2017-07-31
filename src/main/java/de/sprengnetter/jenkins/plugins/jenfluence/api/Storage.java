package de.sprengnetter.jenkins.plugins.jenfluence.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Storage implements Serializable {

    private static final long serialVersionUID = 6166482514595995388L;

    @JsonProperty("value")
    private String value;

    @JsonProperty("representation")
    private String representation;

    @JsonIgnore
    private Map<String, Object> unmappedFields = new HashMap<>();

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

    public Map<String, Object> getUnmappedFields() {
        return unmappedFields;
    }

    @JsonAnySetter
    public void setUnmappedFields(String name, Object o) {
        this.unmappedFields.put(name, o);
    }

    @Override
    public String toString() {
        return "Storage{" +
            "value='" + value + '\'' +
            ", representation='" + representation + '\'' +
            ", unmappedFields=" + unmappedFields +
            '}';
    }
}

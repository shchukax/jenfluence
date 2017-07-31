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
public class Extensions implements Serializable {

    private static final long serialVersionUID = 4405373591017762587L;

    @JsonProperty("position")
    private String position;

    private Map<String, Object> unkownFields = new HashMap<>();

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @JsonAnyGetter
    public Map<String, Object> getUnkownFields() {
        return unkownFields;
    }

    @JsonAnySetter
    public void setUnkownFields(final String name, final Object o) {
        unkownFields.put(name, o);
    }

    @Override
    public String toString() {
        return "Extensions{" +
            "position='" + position + '\'' +
            ", unkownFields=" + unkownFields +
            '}';
    }
}

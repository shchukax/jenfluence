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
public class Ancestor implements Serializable {

    private static final long serialVersionUID = 3395025647468720967L;

    @JsonProperty("id")
    private Integer id;

    @JsonIgnore
    private Map<String, Object> unmappedFields = new HashMap<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Map<String, Object> getUnmappedFields() {
        return unmappedFields;
    }

    @JsonAnySetter
    public void setUnmappedFields(String name, Object o) {
        this.unmappedFields = unmappedFields;
    }

    @Override
    public String toString() {
        return "Ancestor{" +
            "id=" + id +
            ", unmappedFields=" + unmappedFields +
            '}';
    }
}

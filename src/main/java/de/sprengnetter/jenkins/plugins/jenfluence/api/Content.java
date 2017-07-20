package de.sprengnetter.jenkins.plugins.jenfluence.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Content implements Serializable {

    @JsonProperty("start")
    private Integer start;

    @JsonProperty("limit")
    private Integer limit;

    @JsonProperty("size")
    private Integer size;

    @JsonProperty("_links")
    private Links links;

    @JsonProperty("results")
    private List<Result> results = new ArrayList<>();

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Content{" +
                "start=" + start +
                ", limit=" + limit +
                ", size=" + size +
                ", links=" + links +
                ", results=" + results +
                '}';
    }
}

package de.sprengnetter.jenkins.plugins.jenfluence.step;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
public class By {

    private String value;

    private String parentIdentifier;

    @DataBoundConstructor
    public By(final String value, final String parentIdentifier) {
        this.value = value;
        this.parentIdentifier = parentIdentifier;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParentIdentifier() {
        return parentIdentifier;
    }

    public void setParentIdentifier(String parentIdentifier) {
        this.parentIdentifier = parentIdentifier;
    }
}

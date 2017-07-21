package de.sprengnetter.jenkins.plugins.jenfluence.step;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 * Model for the specification of a parent page in Confluence.
 */
public class By {

    // TITLE, ID or NONE
    private String value;

    //The value of the parent identifier (E.g The title or the ID of a page)
    private String parentIdentifier;

    /**
     * Constructor which takes the values of this model.
     *
     * @param value            The value.
     * @param parentIdentifier The parent identifier (ID or title of a page).
     */
    @DataBoundConstructor
    public By(final String value, final String parentIdentifier) {
        this.value = value;
        this.parentIdentifier = parentIdentifier;
    }

    /**
     * Returns the value.
     *
     * @return The value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns the parent identifier (ID or title of a page).
     *
     * @return The parent identifier.
     */
    public String getParentIdentifier() {
        return parentIdentifier;
    }
}

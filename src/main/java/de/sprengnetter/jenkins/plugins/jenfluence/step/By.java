package de.sprengnetter.jenkins.plugins.jenfluence.step;

import java.io.Serializable;

import hudson.Extension;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 *          Model for the specification of a parent page in Confluence.
 */
public class By extends Step implements Serializable {

    private static final long serialVersionUID = 3518103351909578986L;

    // TITLE, ID or NONE
    private String value;

    // The value of the parent identifier (E.g The title or the ID of a page)
    private String parentIdentifier;

    /**
     * Constructor which takes the values of this model.
     *
     * @param value
     *        The value.
     * @param parentIdentifier
     *        The parent identifier (ID or title of a page).
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

    /**
     * Dummy method to ensure the class can be instantiated - not used.
     *
     * @param stepContext   Dummy parameter
     * @return              Always null
     * @throws Exception    Never from this class
     */
    @Override
    public StepExecution start(StepContext stepContext) throws Exception {
        return null;
    }

    /**
     * Descriptor class is required to create the pipeline syntax helpers
     */
    @Extension
    public static class Descriptor extends AbstractStepDescriptor {

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Specifies a page in Confluence using a combination of identifier type and the actual identifier (e.g. ID/12345)";
        }

        @Override
        public String getFunctionName() {
            return "by";
        }
    }
}

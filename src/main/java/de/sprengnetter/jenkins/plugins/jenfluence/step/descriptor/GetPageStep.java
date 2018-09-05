package de.sprengnetter.jenkins.plugins.jenfluence.step.descriptor;

import javax.annotation.Nonnull;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStep;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStepDescriptor;
import de.sprengnetter.jenkins.plugins.jenfluence.step.execution.GetPageExecution;
import hudson.Extension;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 *          Respresentation of the step "getPage".
 */
public class GetPageStep extends AbstractStep {

    private static final long serialVersionUID = 3468957590142286492L;

    private String spaceKey;

    private String title;

    private String expand;

    /**
     * Constructor that takes the information about the searched page.
     *
     * @param spaceKey
     *        The key of the space where the searched page is located in.
     * @param title
     *        The title of the searched page.
     * @param expand
     *        Fields to expand in search results, comma-separated
     */
    @DataBoundConstructor
    public GetPageStep(final String spaceKey, final String title, final String expand) {
        this.spaceKey = spaceKey;
        this.title = title;
        this.expand = expand;
    }

    @Override
    public StepExecution start(final StepContext context) throws Exception {
        return new GetPageExecution(this, context, getSite());
    }

    /**
     * Returns the key of the space where the searched page is located in.
     *
     * @return The key of the space.
     */
    public String getSpaceKey() {
        return spaceKey;
    }

    /**
     * Returns the title of the searched page.
     *
     * @return The title of the searched page.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the comma-separated list of fields to expand in search results
     *
     * @return Comma-separate list of expand fields
     */
    public String getExpand() {
        return expand;
    }

    @Extension
    public static class Descriptor extends AbstractStepDescriptor {

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Returns information about a page";
        }

        @Override
        public String getFunctionName() {
            return "confluenceGetPage";
        }

    }
}

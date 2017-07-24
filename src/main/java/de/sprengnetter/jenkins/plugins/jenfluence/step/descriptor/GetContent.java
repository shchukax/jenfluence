package de.sprengnetter.jenkins.plugins.jenfluence.step.descriptor;

import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStep;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStepDesciptor;
import de.sprengnetter.jenkins.plugins.jenfluence.step.execution.GetContentExecution;
import hudson.Extension;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 * Representation of the getContent step.
 */
public class GetContent extends AbstractStep {

    private final String spaceKey;

    private final Integer limit;

    @DataBoundConstructor
    public GetContent(final String spaceKey, final Integer limit) {
        this.spaceKey = spaceKey;
        this.limit = limit;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new GetContentExecution(this, context, getSite());
    }

    /**
     * Returns the space key.
     *
     * @return The space key.
     */
    public String getSpaceKey() {
        return spaceKey;
    }

    /**
     * Returns the limit.
     * @return The limit.
     */
    public Integer getLimit() {
        return limit;
    }

    @Extension
    public static class Descriptor extends AbstractStepDesciptor {

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Returns information about the content of a space";
        }

        @Override
        public String getFunctionName() {
            return "confluenceGetContent";
        }
    }

}

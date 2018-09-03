package de.sprengnetter.jenkins.plugins.jenfluence.step.descriptor;

import javax.annotation.Nonnull;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStep;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStepDescriptor;
import de.sprengnetter.jenkins.plugins.jenfluence.step.execution.GetContentExecution;
import hudson.Extension;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 *          Representation of the getContent step.
 */
public class GetContentStep extends AbstractStep {

    private static final long serialVersionUID = -987192953228682101L;

    private final String spaceKey;

    private final Integer limit;

    @DataBoundConstructor
    public GetContentStep(final String spaceKey, final Integer limit) {
        this.spaceKey = spaceKey;
        this.limit = limit;
    }

    @Override
    public StepExecution start(final StepContext context) throws Exception {
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
     * 
     * @return The limit.
     */
    public Integer getLimit() {
        return limit;
    }

    @Extension
    public static class Descriptor extends AbstractStepDescriptor {

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

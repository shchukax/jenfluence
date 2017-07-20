package de.sprengnetter.jenkins.plugins.jenfluence.step.descriptor;

import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStep;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStepDesciptor;
import de.sprengnetter.jenkins.plugins.jenfluence.step.execution.GetPageExecution;
import hudson.Extension;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
public class GetPage extends AbstractStep {

    private String spaceKey;

    private String title;

    @DataBoundConstructor
    public GetPage(final String spaceKey, final String title) {
        this.spaceKey = spaceKey;
        this.title = title;
    }

    @Override
    public StepExecution start(final StepContext context) throws Exception {
        return new GetPageExecution(this, context, getSite());
    }

    public String getSpaceKey() {
        return spaceKey;
    }

    public void setSpaceKey(String spaceKey) {
        this.spaceKey = spaceKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Extension
    public static class Descriptor extends AbstractStepDesciptor {

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Returns all information about a page";
        }

        @Override
        public String getFunctionName() {
            return "confluenceGetPage";
        }

    }
}

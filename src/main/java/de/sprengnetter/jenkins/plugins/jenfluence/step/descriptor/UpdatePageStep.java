package de.sprengnetter.jenkins.plugins.jenfluence.step.descriptor;

import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStep;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStepDescriptor;
import de.sprengnetter.jenkins.plugins.jenfluence.step.execution.UpdatePageStepExecution;
import hudson.Extension;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;

public class UpdatePageStep extends AbstractStep {
    private static final long serialVersionUID = -7249517566943303127L;

    private String spaceKey;
    private String title;
    private String newTitle;
    private String newContent;
    private boolean append;

    @DataBoundConstructor
    public UpdatePageStep(String spaceKey, String title, String newTitle, String newContent, boolean append) {
        super();
        this.spaceKey = spaceKey;
        this.title = title;
        this.newTitle = newTitle;
        this.newContent = newContent;
        this.append = append;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new UpdatePageStepExecution(this, context, getSite());
    }

    public String getSpaceKey() {
        return spaceKey;
    }

    public String getTitle() {
        return title;
    }

    public String getNewTitle() {
        return newTitle;
    }

    public String getNewContent() {
        return newContent;
    }

    public boolean isAppend() {
        return append;
    }

    @Extension
    public static class Descriptor extends AbstractStepDescriptor {

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Updates a page";
        }

        @Override
        public String getFunctionName() {
            return "confluenceUpdatePage";
        }

    }
}

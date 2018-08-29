package de.sprengnetter.jenkins.plugins.jenfluence.step.descriptor;

import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStep;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStepDescriptor;
import de.sprengnetter.jenkins.plugins.jenfluence.step.execution.AttachFileExecution;
import hudson.Extension;
import jdk.Exported;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;

public class AttachFileStep extends AbstractStep {

    private String filePath;
    private String title;
    private String spaceKey;

    @DataBoundConstructor
    public AttachFileStep(String filePath, String title, String spaceKey) {
        super();
        this.filePath = filePath;
        this.title = title;
        this.spaceKey = spaceKey;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new AttachFileExecution(this, context, getSite());
    }


    @Extension
    public static class Descriptor extends AbstractStepDescriptor {

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Attaches a file to a page";
        }

        @Override
        public String getFunctionName() {
            return "confluenceAttachFile";
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public String getTitle() {
        return title;
    }

    public String getSpaceKey() {
        return spaceKey;
    }
}

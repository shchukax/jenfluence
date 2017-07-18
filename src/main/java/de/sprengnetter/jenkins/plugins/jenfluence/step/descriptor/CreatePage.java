package de.sprengnetter.jenkins.plugins.jenfluence.step.descriptor;

import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStep;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStepDesciptor;
import de.sprengnetter.jenkins.plugins.jenfluence.step.execution.CreatePageExecution;
import hudson.Extension;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
public class CreatePage extends AbstractStep {

    private String title;

    private String spaceKey;

    private String parentId;

    private String content;

    @DataBoundConstructor
    public CreatePage(final String title, final String spaceKey, final String parentId, final String content) {
        this.title = title;
        this.spaceKey = spaceKey;
        this.parentId = parentId;
        this.content = content;
    }

    @Override
    public StepExecution start(final StepContext context) throws Exception {
        return new CreatePageExecution(this, context);
    }  

    public String getTitle() {
        return title;
    }

    public String getSpaceKey() {
        return spaceKey;
    }

    public String getParentId() {
        return parentId;
    }

    public String getContent() {
        return content;
    }

    @Extension
    public static class Descriptor extends AbstractStepDesciptor {

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Creates a page in a given space optional with content. If you want to create a childpage you have to specify the parent page.";
        }

        @Override
        public String getFunctionName() {
            return "confluenceCreatePage";
        }

    }
}

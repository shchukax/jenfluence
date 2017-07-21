package de.sprengnetter.jenkins.plugins.jenfluence.step.descriptor;

import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStep;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStepDesciptor;
import de.sprengnetter.jenkins.plugins.jenfluence.step.By;
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
    private String content;
    private By by;

    @DataBoundConstructor
    public CreatePage(final String title, final String spaceKey, final By by, final String content) {
        super();
        this.title = title;
        this.spaceKey = spaceKey;
        this.by = by;
        this.content = content;
    }

    @Override
    public StepExecution start(final StepContext context) throws Exception {
        return new CreatePageExecution(this, context, getSite());
    }

    public String isIdentifiedBy(final String identifiedBy) {
        return this.by.getValue().equalsIgnoreCase(identifiedBy) ? "true" : "";
    }

    public String getTitle() {
        return title;
    }

    public String getSpaceKey() {
        return spaceKey;
    }

    public String getContent() {
        return content;
    }

    public By getBy() {
        return by;
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

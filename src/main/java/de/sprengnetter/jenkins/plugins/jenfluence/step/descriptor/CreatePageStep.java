package de.sprengnetter.jenkins.plugins.jenfluence.step.descriptor;

import javax.annotation.Nonnull;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStep;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStepDescriptor;
import de.sprengnetter.jenkins.plugins.jenfluence.step.By;
import de.sprengnetter.jenkins.plugins.jenfluence.step.execution.CreatePageExecution;
import hudson.Extension;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 *          Descriptor and definition of the step "createPage" which allows the user tpo create a page.
 */
public class CreatePageStep extends AbstractStep {

    private static final long serialVersionUID = -7249517566925303127L;

    private String title;
    private String spaceKey;
    private String content;
    private By parentPage;

    /**
     * Constructor which takes the necessary information to create a page.
     *
     * @param title
     *        The title of the page.
     * @param spaceKey
     *        The key of the space.
     * @param parentPage
     *        The specification of an optional parent page.
     * @param content
     *        The content of the page (can be in HTML format).
     */
    @DataBoundConstructor
    public CreatePageStep(final String title, final String spaceKey, final By parentPage, final String content) {
        super();
        this.title = title;
        this.spaceKey = spaceKey;
        this.parentPage = parentPage;
        this.content = content;
    }

    @Override
    public StepExecution start(final StepContext context) throws Exception {
        return new CreatePageExecution(this, context, getSite());
    }

    /**
     * Used by the radioboxes on the view to initially check the correct radio button if there is already a value defined in this
     * instance.
     *
     * @param identifiedBy
     *        Specification of the parent page identifier.
     * @return True if the input String equals the value defined in {@link By}.
     */
    public String isIdentifiedBy(final String identifiedBy) {
        return this.parentPage.getValue().equalsIgnoreCase(identifiedBy) ? "true" : "";
    }

    /**
     * Returns the title of the page that is going to be created.
     *
     * @return The title of the page.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the space key of the space where the page is going to be created.
     *
     * @return The space key.
     */
    public String getSpaceKey() {
        return spaceKey;
    }

    /**
     * Returns the content of the page that is going to be created.
     *
     * @return The content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the specification of the parent page of the page that is going to be created.
     *
     * @return The specification of the parent page.
     */
    public By getParentPage() {
        return parentPage;
    }

    @Extension
    public static class Descriptor extends AbstractStepDescriptor {

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

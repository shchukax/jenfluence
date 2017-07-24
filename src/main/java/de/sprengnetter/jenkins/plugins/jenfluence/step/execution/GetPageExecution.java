package de.sprengnetter.jenkins.plugins.jenfluence.step.execution;

import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import de.sprengnetter.jenkins.plugins.jenfluence.api.Content;
import de.sprengnetter.jenkins.plugins.jenfluence.service.ContentService;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStepExecution;
import de.sprengnetter.jenkins.plugins.jenfluence.step.descriptor.GetPage;
import org.jenkinsci.plugins.workflow.steps.StepContext;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 * Execution implementation of the step "getPage".
 */
public class GetPageExecution extends AbstractStepExecution<Content, GetPage> {

    /**
     * Constructor which takes the needed information to execute the step.
     *
     * @param getPage The step which gets executed.
     * @param context The context of the step.
     * @param site    The configured site of Confluence.
     */
    public GetPageExecution(final GetPage getPage, final StepContext context, final ConfluenceSite site) {
        super(getPage, context, site);
    }

    @Override
    public void validate(final GetPage step) {

        if (isNull(step)) {
            throw new IllegalStateException("Step \"confluenceGetPage\" is null");
        }

        if (step.getSpaceKey() == null || step.getSpaceKey().isEmpty()) {
            throw new IllegalStateException("Space key is null or empty");
        }

        if (step.getTitle() == null || step.getTitle().isEmpty()) {
            throw new IllegalStateException("Title of the searched page is null or emtpy");
        }

    }

    @Override
    protected Content run() throws Exception {
        try {
            return getService(ContentService.class).getPage(getStep().getSpaceKey(), getStep().getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
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
 */
public class GetPageExecution extends AbstractStepExecution<Content, GetPage> {

    private GetPage getPageStep;

    public GetPageExecution(final GetPage getPage, final StepContext context, final ConfluenceSite site) {
        super(getPage, context, site);
        this.getPageStep = getPage;
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
            return getService(ContentService.class).getPage(getPageStep.getSpaceKey(), getPageStep.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
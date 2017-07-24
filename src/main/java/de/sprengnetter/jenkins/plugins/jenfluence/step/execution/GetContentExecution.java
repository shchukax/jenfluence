package de.sprengnetter.jenkins.plugins.jenfluence.step.execution;

import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import de.sprengnetter.jenkins.plugins.jenfluence.api.Content;
import de.sprengnetter.jenkins.plugins.jenfluence.service.ContentService;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStepExecution;
import de.sprengnetter.jenkins.plugins.jenfluence.step.descriptor.GetContent;
import org.jenkinsci.plugins.workflow.steps.StepContext;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
public class GetContentExecution extends AbstractStepExecution<Content, GetContent> {

    private GetContent getContentStep;

    public GetContentExecution(final GetContent getContentStep, final StepContext context, final ConfluenceSite site) {
        super(getContentStep, context, site);
        this.getContentStep = getContentStep;
    }

    @Override
    public void validate(final GetContent step) {
        if (isNull(step)) {
            throw new IllegalStateException("Given step of type \"GetContent\" is null");
        }
    }

    @Override
    protected Content run() throws Exception {
        ContentService service = getService(ContentService.class);
            //Do we have no input?
        if ((getContentStep.getSpaceKey() == null || getContentStep.getSpaceKey().isEmpty())
                && getContentStep.getLimit() == null) {
            return service.getContent();
            //Do we have a limit?
        } else if ((getContentStep.getSpaceKey() == null || getContentStep.getSpaceKey().isEmpty())
                && (getContentStep.getLimit() != null)) {
            return service.getContent(getContentStep.getLimit());
            //Do we have a space key?
        } else if ((getContentStep.getLimit() == null)
                && (getContentStep.getSpaceKey() != null && !getContentStep.getSpaceKey().isEmpty())) {
            return service.getContent(getContentStep.getSpaceKey());
            //Do we have a space key AND a limit?
        } else {
            return service.getContent(getContentStep.getSpaceKey(), getContentStep.getLimit());
        }
    }

}

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
 * Execution for the step getContent.
 */
public class GetContentExecution extends AbstractStepExecution<Content, GetContent> {

    /**
     * Constructor that takes the step, the context of the step and the configured site for Confluence.
     *
     * @param getContentStep The step which gets executed.
     * @param context        The context of the step.
     * @param site           The congifured site of Confluence (Jenkins global config).
     */
    public GetContentExecution(final GetContent getContentStep, final StepContext context, final ConfluenceSite site) {
        super(getContentStep, context, site);
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
        if ((getStep().getSpaceKey() == null || getStep().getSpaceKey().isEmpty())
                && getStep().getLimit() == null) {
            return service.getContent();
            //Do we have a limit?
        } else if ((getStep().getSpaceKey() == null || getStep().getSpaceKey().isEmpty())
                && (getStep().getLimit() != null)) {
            return service.getContent(getStep().getLimit());
            //Do we have a space key?
        } else if ((getStep().getLimit() == null)
                && (getStep().getSpaceKey() != null && !getStep().getSpaceKey().isEmpty())) {
            return service.getContent(getStep().getSpaceKey());
            //Do we have a space key AND a limit?
        } else {
            return service.getContent(getStep().getSpaceKey(), getStep().getLimit());
        }
    }

}

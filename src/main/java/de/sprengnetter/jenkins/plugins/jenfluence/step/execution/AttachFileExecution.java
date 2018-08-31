package de.sprengnetter.jenkins.plugins.jenfluence.step.execution;

import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import de.sprengnetter.jenkins.plugins.jenfluence.api.Content;
import de.sprengnetter.jenkins.plugins.jenfluence.service.ContentService;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStepExecution;
import de.sprengnetter.jenkins.plugins.jenfluence.step.descriptor.AttachFileStep;
import org.jenkinsci.plugins.workflow.steps.StepContext;

public class AttachFileExecution extends AbstractStepExecution<String, AttachFileStep> {

    public AttachFileExecution(AttachFileStep step, StepContext context, ConfluenceSite confluenceSite) {
        super(step, context, confluenceSite);
    }

    @Override
    public void validate(AttachFileStep step) {
        if (isNull(step)) {
            throw new IllegalStateException("Given step of type \"AttachFileStep\" is null");
        }
    }

    @Override
    protected String run() throws Exception {
        ContentService service = getService(ContentService.class);
        Content pageContent = service.getPage(getStep().getSpaceKey(), getStep().getTitle());
        return service.attachFile(String.valueOf(pageContent.getResults().get(0).getId()),
                getStep().getFilePath());
    }
}
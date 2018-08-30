package de.sprengnetter.jenkins.plugins.jenfluence.step.execution;

import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStepExecution;
import de.sprengnetter.jenkins.plugins.jenfluence.step.descriptor.AttachFileStep;
import org.jenkinsci.plugins.workflow.steps.StepContext;

public class AttachFileExecution extends AbstractStepExecution<Boolean, AttachFileStep> {

    private final ConfluenceSite confluenceSite;

    public AttachFileExecution(AttachFileStep step, StepContext context, ConfluenceSite confluenceSite) {
        super(step, context, confluenceSite);
        this.confluenceSite = confluenceSite;
    }

    @Override
    public void validate(AttachFileStep step) {
        if (isNull(step)) {
            throw new IllegalStateException("Given step of type \"AttachFileStep\" is null");
        }
    }

    @Override
    protected Boolean run() throws Exception {
        return Boolean.FALSE;
    }
}
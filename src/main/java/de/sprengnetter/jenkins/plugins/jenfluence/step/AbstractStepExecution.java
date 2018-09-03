package de.sprengnetter.jenkins.plugins.jenfluence.step;

import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import de.sprengnetter.jenkins.plugins.jenfluence.service.BaseService;
import de.sprengnetter.jenkins.plugins.jenfluence.service.ContentService;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;

/**
 * @param <R> The return type of the execution.
 * @param <T> The type of the step which gets executed.
 * @author Oliver Breitenbach
 * @version 1.0.0
 * Abstract base class for all classes that are meant to execute steps.
 */
public abstract class AbstractStepExecution<R, T extends AbstractStep> extends SynchronousNonBlockingStepExecution<R> {

    private static final long serialVersionUID = 7535652081766832564L;

    private final transient T step;

    private final transient ConfluenceSite confluenceSite;

    /**
     * Constructor which takes the information to initialize the execution of the step.
     *
     * @param step           The step which gets executed.
     * @param context        The context of the step.
     * @param confluenceSite The configured site of Confluence (global Jenkins config).
     */
    public AbstractStepExecution(final T step, final StepContext context, final ConfluenceSite confluenceSite) {
        super(context);
        this.confluenceSite = confluenceSite;
        this.step = step;
        validate(step);
    }

    /**
     * Validates the step. Must be implemented by each step.
     *
     * @param step The step which gets validated.
     */
    public abstract void validate(final T step);

    /**
     * Checks if the given step is null.
     *
     * @param step The step which gets checked.
     * @return True if it is null, false if it is not.
     */
    protected boolean isNull(final T step) {
        return step == null;
    }

    /**
     * Returns the desired instance of a service. E.g. an instance of
     * {@link de.sprengnetter.jenkins.plugins.jenfluence.service.ContentService}
     * to execute it's methods.
     *
     * @param clazz The class of the desired service.
     * @param <S>   The type of the desired service.
     * @return An instance of the desired service.
     */
    protected <S extends BaseService> S getService(final Class<S> clazz) {
        switch (clazz.getSimpleName()) {
            case "ContentService":
                return clazz.cast(new ContentService(confluenceSite));
            default:
                throw new IllegalArgumentException(String.format("\"%s\" is not a valid service", clazz.getSimpleName()));
        }
    }

    /**
     * Returns the step which gets executed.
     *
     * @return The step.
     */
    public T getStep() {
        return step;
    }
}
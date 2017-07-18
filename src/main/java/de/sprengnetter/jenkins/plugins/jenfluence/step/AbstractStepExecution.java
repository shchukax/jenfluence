package de.sprengnetter.jenkins.plugins.jenfluence.step;

import de.sprengnetter.jenkins.plugins.jenfluence.client.BaseService;
import de.sprengnetter.jenkins.plugins.jenfluence.response.Response;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;

import java.net.URISyntaxException;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
public abstract class AbstractStepExecution<T, ST extends AbstractStep> extends SynchronousNonBlockingStepExecution<T> {

    private ResteasyClient client = new ResteasyClientBuilder().build();

    private ST step;

    public AbstractStepExecution(final ST step, final StepContext context) {
        super(context);
        this.step = step;
        validate(step);
    }

    public abstract void validate(final ST step);

    protected boolean isNull(final ST step) {
        return step == null;
    }

    protected <S extends BaseService> S getService(final Class<S> clazz) {
        try {
            ResteasyWebTarget target = client.target(step.getSite().getUrl().toURI());
            return target.proxy(clazz);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI build from URL " + step.getSite().getUrl() + " is malformed", e);
        }
    }

    protected void log(final Response response){

    }
}
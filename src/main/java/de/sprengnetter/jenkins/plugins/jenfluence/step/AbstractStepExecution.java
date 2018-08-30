package de.sprengnetter.jenkins.plugins.jenfluence.step;

import com.sun.org.apache.xerces.internal.util.EntityResolverWrapper;
import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import de.sprengnetter.jenkins.plugins.jenfluence.service.BaseService;
import de.sprengnetter.jenkins.plugins.jenfluence.service.ContentServiceImpl;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.FileProvider;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jboss.resteasy.plugins.providers.multipart.*;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * @param <R> The return type of the execution.
 * @param <T> The type of the step which gets executed.
 * @author Oliver Breitenbach
 * @version 1.0.0
 * Abstract base class for all classes that are meant to execute steps.
 */
public abstract class AbstractStepExecution<R, T extends AbstractStep> extends SynchronousNonBlockingStepExecution<R> {

    private static final long serialVersionUID = 7535652081766832564L;

    private static final transient Logger LOGGER = LoggerFactory.getLogger(AbstractStepExecution.class);

    private static transient ResteasyClient client;
    private static transient boolean clientInitialized = false;
    private final transient T step;
    private transient ResteasyWebTarget target;

    /**
     * Constructor which takes the information to initialize the execution of the step.
     *
     * @param step           The step which gets executed.
     * @param context        The context of the step.
     * @param confluenceSite The configured site of Confluence (global Jenkins config).
     */
    public AbstractStepExecution(final T step, final StepContext context, final ConfluenceSite confluenceSite) {
        super(context);
        this.step = step;
        try {
            validate(step);
            if (!clientInitialized) {
                initClient(confluenceSite);
            }
            target = client.target(step.getSite().getUrl().toURI());
        } catch (URISyntaxException e) {
            LOGGER.error("URI build from URL " + step.getSite().getUrl() + " is malformed");
        }
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
                return clazz.cast(new ContentServiceImpl());
            default:
                throw new IllegalArgumentException(String.format("\"%s\" is not a valid service", clazz.getSimpleName()));
        }
    }

    private void initClient(final ConfluenceSite confluenceSite) {

        clientInitialized = true;
    }

    /**
     * Returns the step which gets executed.
     *
     * @return The step.
     */
    public T getStep() {
        return step;
    }

    public static ResteasyClient getClient() {
        return client;
    }
}
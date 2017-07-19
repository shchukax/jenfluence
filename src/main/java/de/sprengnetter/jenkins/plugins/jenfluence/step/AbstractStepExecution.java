package de.sprengnetter.jenkins.plugins.jenfluence.step;

import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import de.sprengnetter.jenkins.plugins.jenfluence.service.BaseService;
import de.sprengnetter.jenkins.plugins.jenfluence.util.LoggingOutputStream;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.WriterInterceptor;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
public abstract class AbstractStepExecution<R, T extends AbstractStep> extends SynchronousNonBlockingStepExecution<R> {

    private static final transient Logger LOGGER = LoggerFactory.getLogger(AbstractStepExecution.class);

    private static transient ResteasyClient client;
    private static transient boolean clientInitialized = false;

    private transient ResteasyWebTarget target;

    public AbstractStepExecution(final T step, final StepContext context, final ConfluenceSite confluenceSite) {
        super(context);
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

    public abstract void validate(final T step);

    protected boolean isNull(final T step) {
        return step == null;
    }

    protected <S extends BaseService> S getService(final Class<S> clazz) {
        return target.proxyBuilder(clazz).classloader(clazz.getClassLoader()).build();
    }

    private void initClient(final ConfluenceSite confluenceSite) {
        client = new ResteasyClientBuilder()
                .register(ResteasyJackson2Provider.class)
                .register(new BasicAuthentication(confluenceSite.getUserName(), confluenceSite.getPassword()))
                .register((ClientRequestFilter) requestContext -> {
                    MultivaluedMap<String, Object> headers = requestContext.getHeaders();
                    headers.add("X-Atlassian-Token", "no-check");
                })
                .register((ClientRequestFilter) requestContext -> System.out.println("Headers: " + requestContext.getHeaders()))
                .register((WriterInterceptor) requestContext -> {
                    LoggingOutputStream loggingOutputStream = new LoggingOutputStream(requestContext.getOutputStream());
                    requestContext.setOutputStream(loggingOutputStream);
                    requestContext.proceed();
                    LOGGER.debug("Body:");
                    LOGGER.debug(loggingOutputStream.getStringBuilder(StandardCharsets.UTF_8).toString());
                })
                .register((ClientResponseFilter) (requestContext, responseContext) -> {
                    if (responseContext.getStatus() != 200) {
                        LOGGER.error("Response message:");
                        LOGGER.error(IOUtils.toString(responseContext.getEntityStream(), StandardCharsets.UTF_8));
                    }
                }).build();

        clientInitialized = true;
    }
}
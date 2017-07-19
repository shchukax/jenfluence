package de.sprengnetter.jenkins.plugins.jenfluence.step;

import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import de.sprengnetter.jenkins.plugins.jenfluence.api.Response;
import de.sprengnetter.jenkins.plugins.jenfluence.service.BaseService;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static de.sprengnetter.jenkins.plugins.jenfluence.util.HttpUtil.initClient;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
public abstract class AbstractStepExecution<T, ST extends AbstractStep> extends SynchronousNonBlockingStepExecution<T> {

    private static final transient Logger LOGGER = LoggerFactory.getLogger(AbstractStepExecution.class);

    private transient ResteasyClient client;
    private transient ResteasyWebTarget target;

    private ST step;

    public AbstractStepExecution(final ST step, final StepContext context, final ConfluenceSite confluenceSite) {
        super(context);
        try {
            this.step = step;
            validate(step);
            ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(
                    initClient(confluenceSite)
            );
            client = new ResteasyClientBuilder()
                    //.httpEngine(engine)
                    .register(ResteasyJackson2Provider.class)
                    .register(new BasicAuthentication(confluenceSite.getUserName(), confluenceSite.getPassword()))
                    .register(new ClientRequestFilter() {
                        @Override
                        public void filter(ClientRequestContext requestContext) throws IOException {
                            MultivaluedMap<String, Object> headers = requestContext.getHeaders();
                            headers.add("X-Atlassian-Token", "no-check");
                        }
                    })
                    .register(new ClientRequestFilter() {
                        @Override
                        public void filter(ClientRequestContext requestContext) throws IOException {
                            System.out.println("Headers: " + requestContext.getHeaders());
                        }
                    })
                    .register(new WriterInterceptor() {
                        @Override
                        public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
                            LoggingOutputStream loggingOutputStream = new LoggingOutputStream(context.getOutputStream());
                            context.setOutputStream(loggingOutputStream);
                            context.proceed();
                            System.out.println("Body:");
                            System.out.println(loggingOutputStream.getStringBuilder(Charset.forName("UTF-8")).toString());
                        }
                    })
                    .register(new ClientResponseFilter() {
                        @Override
                        public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
                            if (responseContext.getStatus() != 200) {
                                System.out.println("Response message:");
                                System.out.println(IOUtils.toString(responseContext.getEntityStream(), "UTF-8"));
                            }
                        }
                    })
                    .build();
            target = client.target(step.getSite().getUrl().toURI());
        } catch (SSLException e) {
            LOGGER.error("Error while applying self-signed-strategy", e);
            Response<T> response = new Response<>();
            response.setError(e.getMessage());
            log(response);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public abstract void validate(final ST step);

    protected boolean isNull(final ST step) {
        return step == null;
    }

    protected <S extends BaseService> S getService(final Class<S> clazz) {
        return target.proxyBuilder(clazz).classloader(clazz.getClassLoader()).build();
    }

    protected void log(final Response<T> response) {

    }

    private class LoggingOutputStream extends FilterOutputStream {
        private final StringBuilder sb = new StringBuilder();
        private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        public LoggingOutputStream(OutputStream out) {
            super(out);
        }

        StringBuilder getStringBuilder(Charset charset) {
            // write entity to the builder
            final byte[] entity = baos.toByteArray();

            sb.append(new String(entity, 0, entity.length, charset));

            return sb;
        }

        @Override
        public void write(final int i) throws IOException {
            baos.write(i);
            out.write(i);
        }

    }
}
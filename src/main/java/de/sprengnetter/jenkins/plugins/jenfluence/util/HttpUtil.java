package de.sprengnetter.jenkins.plugins.jenfluence.util;

import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
public final class HttpUtil {

    private HttpUtil() {
    }

    public static CloseableHttpClient initClient(final ConfluenceSite site) throws SSLException {
        return HttpClientBuilder.create()
                .setSSLSocketFactory(withSSL())
                .setConnectionManager(withPoolingConnectionManager(site.getPoolSize()))
                .setDefaultRequestConfig(withRequestConfig(site.getTimeout()))
                .build();
    }

    public static SSLConnectionSocketFactory withSSL() throws SSLException {
        try {
            SSLContextBuilder contextBuilder = new SSLContextBuilder();
            contextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            return new SSLConnectionSocketFactory(contextBuilder.build());
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new SSLException("Error while applying self-signed-strategy", e);
        }
    }

    public static HttpClientConnectionManager withPoolingConnectionManager(final int poolSize) {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(poolSize);
        return manager;
    }

    public static RequestConfig withRequestConfig(final int timeout) {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(timeout)
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
    }

    public static HttpContext withClientContext(final String username, final String password) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        HttpClientContext clientContext = HttpClientContext.create();
        clientContext.setCredentialsProvider(credentialsProvider);
        return clientContext;
    }

}

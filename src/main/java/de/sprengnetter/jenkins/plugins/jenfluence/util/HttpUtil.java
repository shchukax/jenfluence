package de.sprengnetter.jenkins.plugins.jenfluence.util;

import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 * Utility class for related to used Apache HTTP client.
 */
public final class HttpUtil {

    /**
     * Private constructor.
     */
    private HttpUtil() {
    }

    /**
     * Initializes an HTTP client based on a {@link ConfluenceSite}.
     *
     * @param site The configured {@link ConfluenceSite} from the global Jenkins config.
     * @return The configured HTTP client.
     * @throws SSLException When something went wrong while applying the self singed strategy.
     */
    public static CloseableHttpClient initClient(final ConfluenceSite site) throws SSLException {
        return HttpClientBuilder.create()
                .setSSLSocketFactory(withSSL())
                .setConnectionManager(withPoolingConnectionManager(site.getPoolSize()))
                .setDefaultRequestConfig(withRequestConfig(site.getTimeout()))
                .build();
    }

    /**
     * Constructs a {@link SSLConnectionSocketFactory} with a self signed strategy.
     *
     * @return The configured {@link SSLConnectionSocketFactory}
     * @throws SSLException When something went wrong while applying the self singed strategy.
     */
    public static SSLConnectionSocketFactory withSSL() throws SSLException {
        try {
            SSLContextBuilder contextBuilder = new SSLContextBuilder();
            contextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            return new SSLConnectionSocketFactory(contextBuilder.build());
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new SSLException("Error while applying self-signed-strategy", e);
        }
    }

    /**
     * Defines a {@link PoolingHttpClientConnectionManager} with a given pool size.
     *
     * @param poolSize The max pool size.
     * @return The configured {@link PoolingHttpClientConnectionManager}.
     */
    public static HttpClientConnectionManager withPoolingConnectionManager(final int poolSize) {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(poolSize);
        return manager;
    }

    /**
     * Configures a timeout on the connection.
     *
     * @param timeout The timeout in ms.
     * @return The configured {@link RequestConfig}.
     */
    public static RequestConfig withRequestConfig(final int timeout) {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(timeout)
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
    }

    /**
     * Validates a given URL.
     *
     * @param url The URL to validate.
     */
    public static void validateUrl(final String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("Given URL is null or empty");
        }
        String[] supportedProtocols = new String[]{"http", "https"};
        UrlValidator urlValidator = new UrlValidator(supportedProtocols);
        if (!urlValidator.isValid(url)) {
            throw new IllegalArgumentException("URL " + url + " is invalid. Maybe you forgot the protocol (http/s)?");
        }
    }

    /**
     * Checks if a given URL is reachable is within a given timeout.
     *
     * @param url     The URL which gets checkd.l
     * @param timeout The timeout.
     * @return True if the URL is reachable, false if not.
     */
    public static boolean isReachable(final URL url, final Integer timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(url.getHost(), 9090), timeout);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}

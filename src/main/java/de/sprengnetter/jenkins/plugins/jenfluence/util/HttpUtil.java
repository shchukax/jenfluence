package de.sprengnetter.jenkins.plugins.jenfluence.util;

import org.apache.commons.validator.routines.UrlValidator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

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

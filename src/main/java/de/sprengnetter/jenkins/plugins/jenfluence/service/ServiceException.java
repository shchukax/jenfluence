package de.sprengnetter.jenkins.plugins.jenfluence.service;

import java.io.IOException;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
public class ServiceException extends IOException {

    public ServiceException() {
        super();
    }

    public ServiceException(final String message) {
        super(message);
    }

    public ServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ServiceException(final Throwable cause) {
        super(cause);
    }

}

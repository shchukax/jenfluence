package de.sprengnetter.jenkins.plugins.jenfluence.service;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 * Base interface for all service interfaces. It contains basic information which can be used by the actual service interfaces.
 */
public interface BaseService {

    //The base resource URI to the Confluence REST API
    String BASE_RESOURCE = "/rest/api";

}

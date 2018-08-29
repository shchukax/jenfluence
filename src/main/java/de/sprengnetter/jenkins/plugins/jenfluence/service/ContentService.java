package de.sprengnetter.jenkins.plugins.jenfluence.service;


import de.sprengnetter.jenkins.plugins.jenfluence.api.Content;
import de.sprengnetter.jenkins.plugins.jenfluence.api.Page;
import de.sprengnetter.jenkins.plugins.jenfluence.api.PageCreated;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.jboss.resteasy.plugins.providers.multipart.MultipartOutput;

import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import java.io.File;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 * Service interface which defines the available methods related to some kind of content of confluence.
 * This content is usually the data, including metadata, of pages in Confluence.
 */
@Path(BaseService.BASE_RESOURCE + "/content")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ContentService extends BaseService {

    /**
     * Returns the unfiltered content limited by 25 results.
     *
     * @return The unfiltered content.
     */
    @GET
    Content getContent();

    /**
     * Returns the unfiltered content with the specification of the result limit.
     *
     * @param limit The max count of returned results.
     * @return The unfiltered content.
     */
    @GET
    Content getContent(@QueryParam("limit") final Integer limit);

    /**
     * Returns the content filtered by a Confluence space key limited by 25 results.
     * The results will all be located in the specified space.
     *
     * @param spaceKey The key of the space in Confluence.
     * @return The found content filtered by a space key.
     */
    @GET
    Content getContent(@QueryParam("spaceKey") final String spaceKey);

    /**
     * Returns the content filtered by a Confluence space key.
     * The results will all be located in the specified space.
     * Also the result count is specified by the limit.
     *
     * @param spaceKey The key of the space in Confluence.
     * @param limit    The max count of the returned results.
     * @return The found content filtered by a space key.
     */
    @GET
    Content getContent(@QueryParam("spaceKey") final String spaceKey, @QueryParam("limit") final Integer limit);

    /**
     * Returns the content filtered by a Confluence space key and a title of a page.
     *
     * @param spaceKey The key of the space in Confluence.
     * @param title    The title of the searched page.
     * @return The found content filtered by a space key and a page title.
     */
    @GET
    Content getPage(@QueryParam("spaceKey") final String spaceKey, @QueryParam("title") final String title);

    /**
     * Creates a page in Confluence.
     *
     * @param page The {@link Page} object, which contains information about the page that is going to be created.
     * @return The mapped response of the Confluence server.
     */
    @POST
    PageCreated createPage(final Page page);

    @POST
    @Path("{id}/child/attachment")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void attachFile(@PathParam("id") final String id, MultipartOutput output);
}
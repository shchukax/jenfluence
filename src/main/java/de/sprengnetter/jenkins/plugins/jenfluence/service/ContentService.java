package de.sprengnetter.jenkins.plugins.jenfluence.service;


import de.sprengnetter.jenkins.plugins.jenfluence.api.Content;
import de.sprengnetter.jenkins.plugins.jenfluence.api.Page;
import de.sprengnetter.jenkins.plugins.jenfluence.result.PageCreated;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
@Path(BaseService.BASE_RESOURCE + "/content")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ContentService extends BaseService {

    @GET
    Content getContent();

    @GET
    Content getContent(@QueryParam("limit") final Integer limit);

    @GET
    Content getContent(@QueryParam("spaceKey") final String spaceKey);

    @GET
    Content getContent(@QueryParam("spaceKey") final String spaceKey, @QueryParam("limit") final Integer limit);

    @GET
    Content getPage(@QueryParam("spaceKey") final String spaceKey, @QueryParam("title") final String title);

    @POST
    PageCreated createPage(final Page page);
}

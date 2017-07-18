package de.sprengnetter.jenkins.plugins.jenfluence.client;


import de.sprengnetter.jenkins.plugins.jenfluence.response.Content;
import de.sprengnetter.jenkins.plugins.jenfluence.response.Page;

import javax.ws.rs.*;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
@Path(BaseService.BASE_RESOURCE + "/content")
@Produces(BaseService.APPLICATION_JSON_UTF8)
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
    @Consumes(APPLICATION_JSON_UTF8)
    Content createPage(final Page page);
}

package de.sprengnetter.jenkins.plugins.jenfluence.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import de.sprengnetter.jenkins.plugins.jenfluence.api.Content;
import de.sprengnetter.jenkins.plugins.jenfluence.api.Page;
import de.sprengnetter.jenkins.plugins.jenfluence.api.PageCreated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ContentService extends BaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentService.class);

    private static final String CONTENT_RESOURCE = "/content";
    private static final String ATTACHMENT_RESOURCE_TEMPLATE = "%s/%s/child/attachment";
    private static final String EXPAND_BODY_TEMPLATE = "/%s?expand=body.storage";

    private static final String SPACE_KEY_QUERY_PARAM = "spaceKey";
    private static final String LIMIT_KEY_QUERY_PARAM = "limit";
    private static final String TITLE_KEY_QUERY_PARAM = "title";
    private static final String EXPAND_KEY_QUERY_PARAM = "expand";
    private static final String EXPAND_QUERY_PARAM_VALUE = "version";

    private final ObjectMapper objectMapper;

    public ContentService(final ConfluenceSite confluenceSite) {
        super(confluenceSite);
        this.objectMapper = new ObjectMapper();
        //Don't serialize null or empty fields
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    public Content getContent() {
        Request request = buildGetRequest(CONTENT_RESOURCE);
        return executeRequest(request, Content.class);
    }

    public Content getContent(final Integer limit) {
        Map<String, String> queryParams = Collections.singletonMap(LIMIT_KEY_QUERY_PARAM, String.valueOf(limit));
        Request request = buildGetRequest(CONTENT_RESOURCE, queryParams);
        return executeRequest(request, Content.class);
    }

    public Content getContent(final String spaceKey) {
        Map<String, String> queryParams = Collections.singletonMap(SPACE_KEY_QUERY_PARAM, spaceKey);
        Request request = buildGetRequest(CONTENT_RESOURCE, queryParams);
        return executeRequest(request, Content.class);
    }

    public Content getContent(final String spaceKey, final Integer limit) {
        HashMap<String, String> queryParams = new HashMap<String, String>() {{
            put(SPACE_KEY_QUERY_PARAM, spaceKey);
            put(LIMIT_KEY_QUERY_PARAM, String.valueOf(limit));
        }};
        Request request = buildGetRequest(CONTENT_RESOURCE, queryParams);
        return executeRequest(request, Content.class);
    }

    public Content getPage(final String spaceKey, final String title, final String expand) {
        HashMap<String, String> queryParams = new HashMap<String, String>() {{
            put(SPACE_KEY_QUERY_PARAM, spaceKey);
            put(TITLE_KEY_QUERY_PARAM, title);
        }};
        if (expand != null) {
            queryParams.put(EXPAND_KEY_QUERY_PARAM, expand);
        }
        Request request = buildGetRequest(CONTENT_RESOURCE, queryParams);
        return executeRequest(request, Content.class);
    }

    public Page getPageById(final String id) {
        Request request = buildGetRequest(String.format(CONTENT_RESOURCE + "/%s", id));
        return executeRequest(request, Page.class);
    }

    public Page getPageBodyById(final String id) {
        Request request = buildGetRequest(String.format(CONTENT_RESOURCE + EXPAND_BODY_TEMPLATE, id));
        return executeRequest(request, Page.class);
    }

    public PageCreated createPage(final Page page) {
        return modifyPage(page, HttpMethod.POST);
    }

    public PageCreated updatePage(final Page page) {
        return modifyPage(page, HttpMethod.PUT);
    }

    public String attachFile(final String id, final String filePath) {
        RequestBody requestBody = buildBodyForFileUpload(filePath, guessMediaType(new File(filePath)));
        Request request = buildPostRequest(String.format(ATTACHMENT_RESOURCE_TEMPLATE, CONTENT_RESOURCE, id), requestBody);
        return executeRequest(request, String.class);
    }

    private String guessMediaType(final File file) {
        try {
            return Files.probeContentType(Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            return "*/*";
        }
    }

    private PageCreated modifyPage(final Page page, final String httpMethod) {
        try {
            RequestBody body = RequestBody.create(com.squareup.okhttp.MediaType.parse(MediaType.APPLICATION_JSON),
                    this.objectMapper.writeValueAsString(page));
            Request request;
            //Build request based on the HTTP-Method
            if (HttpMethod.POST.equals(httpMethod)) {
                request = buildPostRequest(CONTENT_RESOURCE, body);
            } else if (HttpMethod.PUT.equals(httpMethod)) {
                request = buildPutRequest(CONTENT_RESOURCE + "/" + page.getId(), body);
            } else {
                throw new IllegalArgumentException(String.format("HTTP-Method %s is not allowed for modifying pages", httpMethod));
            }
            return executeRequest(request, PageCreated.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while processing JSON-File", e);
            throw new IllegalArgumentException(e);
        }
    }

    private <T> T executeRequest(final Request request, final Class<T> responseType) {
        try {
            Response response = getClient().newCall(request).execute();
            if (String.class.getSimpleName().equals(responseType.getSimpleName())) {
                return responseType.cast(response.body().string());
            }
            return this.objectMapper.readValue(response.body().string(), responseType);
        } catch (IOException e) {
            LOGGER.error("Error while executing request " + request.toString(), e);
            throw new IllegalArgumentException(e);
        }
    }
}

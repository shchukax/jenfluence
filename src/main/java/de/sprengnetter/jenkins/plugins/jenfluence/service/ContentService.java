package de.sprengnetter.jenkins.plugins.jenfluence.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import de.sprengnetter.jenkins.plugins.jenfluence.api.Content;
import de.sprengnetter.jenkins.plugins.jenfluence.api.Page;
import de.sprengnetter.jenkins.plugins.jenfluence.api.PageCreated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final ObjectMapper objectMapper;

    public ContentService(final ConfluenceSite confluenceSite) {
        super(confluenceSite);
        this.objectMapper = new ObjectMapper();
    }

    public Content getContent() {
        Request request = buildGetRequest(CONTENT_RESOURCE);
        return executeRequest(request, Content.class);
    }

    public Content getContent(final Integer limit) {
        Map<String, String> queryParams = Collections.singletonMap("limit", String.valueOf(limit));
        Request request = buildGetRequest(CONTENT_RESOURCE, queryParams);
        return executeRequest(request, Content.class);
    }

    public Content getContent(final String spaceKey) {
        Map<String, String> queryParams = Collections.singletonMap("spaceKey", spaceKey);
        Request request = buildGetRequest(CONTENT_RESOURCE, queryParams);
        return executeRequest(request, Content.class);
    }

    public Content getContent(final String spaceKey, final Integer limit) {
        HashMap<String, String> queryParams = new HashMap<String, String>() {{
            put("spaceKey", spaceKey);
            put("limit", String.valueOf(limit));
        }};
        Request request = buildGetRequest(CONTENT_RESOURCE, queryParams);
        return executeRequest(request, Content.class);
    }

    public Content getPage(final String spaceKey, final String title) {
        HashMap<String, String> queryParams = new HashMap<String, String>() {{
            put("spaceKey", spaceKey);
            put("title", title);
        }};
        Request request = buildGetRequest(CONTENT_RESOURCE, queryParams);
        return executeRequest(request, Content.class);
    }

    public PageCreated createPage(final Page page) {
        try {
            RequestBody body = RequestBody.create(MediaType.parse("application/json"),
                    this.objectMapper.writeValueAsString(page));
            Request request = buildGetRequest(CONTENT_RESOURCE);
            Request postRequest = request.newBuilder().post(body).build();
            return executeRequest(postRequest, PageCreated.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while processing JSON-File", e);
            throw new IllegalArgumentException(e);
        }
    }

    public String attachFile(final String id, final String filePath) {
        RequestBody requestBody = buildBodyForFileUpload(filePath, guessMediaType(new File(filePath)));
        Request request = buildPostRequest(String.format("%s/%s/child/attachment", CONTENT_RESOURCE, id), requestBody);
        return executeRequest(request, String.class);
    }

    private String guessMediaType(final File file) {
        try {
            return Files.probeContentType(Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            return "*/*";
        }
    }

    private <T> T executeRequest(final Request request, Class<T> responseType) {
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

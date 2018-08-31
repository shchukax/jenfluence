package de.sprengnetter.jenkins.plugins.jenfluence.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import de.sprengnetter.jenkins.plugins.jenfluence.api.Content;
import de.sprengnetter.jenkins.plugins.jenfluence.api.Page;
import de.sprengnetter.jenkins.plugins.jenfluence.api.PageCreated;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ContentService extends BaseService {

    private static final String CONTENT_RESOURCE = "/content";

    private final ObjectMapper objectMapper;

    public ContentService(ConfluenceSite confluenceSite) {
        super(confluenceSite);
        this.objectMapper = new ObjectMapper();
    }

    public Content getContent() throws IOException {
        Request request = buildGetRequest(CONTENT_RESOURCE);
        Response response = getClient().newCall(request).execute();
        return this.objectMapper.readValue(response.body().string(), Content.class);
    }

    public Content getContent(Integer limit) throws IOException {
        Map<String, String> queryParams = Collections.singletonMap("limit", String.valueOf(limit));
        Request request = buildGetRequest(CONTENT_RESOURCE, queryParams);
        Response response = getClient().newCall(request).execute();
        return this.objectMapper.readValue(response.body().string(), Content.class);
    }

    public Content getContent(String spaceKey) throws IOException {
        Map<String, String> queryParams = Collections.singletonMap("spaceKey", spaceKey);
        Request request = buildGetRequest(CONTENT_RESOURCE, queryParams);
        Response response = getClient().newCall(request).execute();
        return this.objectMapper.readValue(response.body().string(), Content.class);
    }

    public Content getContent(String spaceKey, Integer limit) throws IOException {
        HashMap<String, String> queryParams = new HashMap<String, String>() {{
            put("spaceKey", spaceKey);
            put("limit", String.valueOf(limit));
        }};
        Request request = buildGetRequest(CONTENT_RESOURCE, queryParams);
        Response response = getClient().newCall(request).execute();
        return this.objectMapper.readValue(response.body().string(), Content.class);
    }

    public Content getPage(String spaceKey, String title) throws IOException {
        HashMap<String, String> queryParams = new HashMap<String, String>() {{
            put("spaceKey", spaceKey);
            put("title", title);
        }};
        Request request = buildGetRequest(CONTENT_RESOURCE, queryParams);
        Response response = getClient().newCall(request).execute();
        return this.objectMapper.readValue(response.body().string(), Content.class);
    }

    public PageCreated createPage(Page page) throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),
                this.objectMapper.writeValueAsString(page));
        Request request = buildGetRequest(CONTENT_RESOURCE);
        Request postRequest = request.newBuilder().post(body).build();
        Response response = getClient().newCall(postRequest).execute();
        return this.objectMapper.readValue(response.body().string(), PageCreated.class);
    }

    public String attachFile(String id, String filePath) throws IOException {
        RequestBody requestBody = buildBodyForFileUpload(filePath, guessMediaType(new File(filePath)));
        Request request = buildPostRequest(String.format("%s/%s/child/attachment", CONTENT_RESOURCE, id), requestBody);
        Response response = getClient().newCall(request).execute();
        return response.body().string();
    }

    private String guessMediaType(File file) {
        try {
            return Files.probeContentType(Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            return "*/*";
        }
    }
}

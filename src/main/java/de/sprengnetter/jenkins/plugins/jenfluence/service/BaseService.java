package de.sprengnetter.jenkins.plugins.jenfluence.service;

import com.squareup.okhttp.*;
import de.sprengnetter.jenkins.plugins.jenfluence.AuthenticationType;
import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import de.sprengnetter.jenkins.plugins.jenfluence.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import javax.ws.rs.HttpMethod;
import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class BaseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseService.class);

    private static final String SSL_INSTANCE_TYPE = "SSL";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String CONFLUENCE_HEADER_KEY = "X-Atlassian-Token";
    private static final String CONFLUENCE_HEADER_VALUE = "no-check";
    private static final String FILE_UPLOAD_FORM_DATA_NAME = "file";

    protected static final String BASE_RESOURCE = "/rest/api";

    private ConfluenceSite confluenceSite;
    private OkHttpClient client;
    private Map<String, String> defaultRequestHeaders;
    private Map<String, String> customRequestHeaders;

    BaseService(final ConfluenceSite confluenceSite) {
        this.confluenceSite = confluenceSite;
        this.customRequestHeaders = new HashMap<>();
        initClient();
        initHeaders();
    }

    Request buildGetRequest(final String requestResource) {
        return buildRequest(requestResource, HttpMethod.GET, null, Collections.emptyMap());
    }

    Request buildGetRequest(final String requestResource, final Map<String, String> queryParams) {
        return buildRequest(requestResource, HttpMethod.GET, null, queryParams);
    }

    Request buildPostRequest(final String requestResource, final RequestBody requestBody) {
        return buildRequest(requestResource, HttpMethod.POST, requestBody, Collections.emptyMap());
    }

    Request buildPutRequest(final String requestResource, final RequestBody requestBody) {
        return buildRequest(requestResource, HttpMethod.PUT, requestBody, Collections.emptyMap());
    }

    RequestBody buildBodyForFileUpload(final String filePath, final String mediaType) {
        return buildBodyForFileUpload(filePath, mediaType, Collections.emptyList());
    }

    private RequestBody buildBodyForFileUpload(final String filePath, final String mediaType, final List<MultipartField> additionalMultipartFields) {
        MultipartBuilder multipartBuilder = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart(FILE_UPLOAD_FORM_DATA_NAME, filePath, RequestBody.create(MediaType.parse(mediaType), new File(filePath)));

        if (additionalMultipartFields != null && additionalMultipartFields.isEmpty()) {
            additionalMultipartFields.forEach(field -> multipartBuilder.addFormDataPart(
                    field.getKey(),
                    field.getValue())
            );
        }
        return multipartBuilder.build();
    }

    private Request buildRequest(final String requestResource, final String httpMethod, final RequestBody requestBody, final Map<String, String> queryParams) {
        Request.Builder requestBuilder = new Request.Builder();
        registerAllHeaders(requestBuilder);
        //clear the custom headers for the next upcoming request
        this.customRequestHeaders.clear();
        requestBuilder.url(buildUrl(confluenceSite.getUrl() + BASE_RESOURCE + requestResource, queryParams));
        requestBuilder.method(httpMethod, requestBody);
        return requestBuilder.build();
    }

    private void initClient() {
        this.client = new OkHttpClient();
        this.client.setConnectTimeout(this.confluenceSite.getTimeout(), TimeUnit.SECONDS);
        this.client.setWriteTimeout(this.confluenceSite.getTimeout(), TimeUnit.SECONDS);
        this.client.setReadTimeout(this.confluenceSite.getTimeout(), TimeUnit.SECONDS);
        this.client.setConnectionPool(new ConnectionPool(confluenceSite.getPoolSize(), 15, TimeUnit.SECONDS));

        if (confluenceSite.getTrustAllCertificates()) {
            installTrustManager(HttpUtil.buildAllTrustingManager());
            this.client.setHostnameVerifier((s, sslSession) -> true);
        }
    }

    private void installTrustManager(final TrustManager[] allTrustingManager) {
        try {
            SSLContext sslContext = SSLContext.getInstance(SSL_INSTANCE_TYPE);
            sslContext.init(null, allTrustingManager, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            this.client.setSocketFactory(sslSocketFactory);
        } catch (NoSuchAlgorithmException e) {
            //We should never land here
        } catch (KeyManagementException e) {
            LOGGER.error("Something went wrong with the key-management", e.getMessage());
        }
    }

    private void initHeaders() {
        this.defaultRequestHeaders = new HashMap<>();
        this.defaultRequestHeaders.put(CONFLUENCE_HEADER_KEY, CONFLUENCE_HEADER_VALUE);
        if (confluenceSite.getAuthenticationType() == AuthenticationType.BASIC) {
            this.defaultRequestHeaders.put(AUTHORIZATION_HEADER, Credentials.basic(confluenceSite.getUserName(), confluenceSite.getPassword()));
        }
    }

    private void registerAllHeaders(final Request.Builder builder) {
        defaultRequestHeaders.forEach(builder::addHeader);
        customRequestHeaders.forEach(builder::addHeader);
    }

    private void addQueryParams(final HttpUrl.Builder urlBuilder, final Map<String, String> queryParams) {
        if (queryParams != null && !queryParams.isEmpty()) {
            queryParams.forEach(urlBuilder::addQueryParameter);
        }
    }

    private HttpUrl buildUrl(final String url, final Map<String, String> queryParams) {
        HttpUrl urlObject = HttpUrl.parse(url);
        HttpUrl.Builder urlBuilder = urlObject.newBuilder();
        addQueryParams(urlBuilder, queryParams);
        return urlBuilder.build();
    }

    private void registerRequestHeader(final String name, final String value) {
        this.customRequestHeaders.put(name, value);
    }

    public OkHttpClient getClient() {
        return client;
    }

    protected static final class MultipartField {
        private String key;
        private String value;

        public MultipartField(final String key, final String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}

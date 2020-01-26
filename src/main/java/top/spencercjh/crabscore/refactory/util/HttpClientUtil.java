package top.spencercjh.crabscore.refactory.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 每次请求都创建一个HttpClient
 *
 * @author Spencer
 */
public class HttpClientUtil {
    static final int TEN_SECOND = 10 * 1000;
    /**
     * 每一个请求的超时配置
     */
    private static final RequestConfig TIMEOUT_CONFIG = RequestConfig.custom()
            .setConnectionRequestTimeout(TEN_SECOND)
            .setConnectTimeout(TEN_SECOND)
            .setSocketTimeout(TEN_SECOND)
            .build();
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private HttpClientUtil() {
    }

    /**
     * POST body ,没有结果就返回空
     *
     * @param url  target url
     * @param body body
     * @return Map with json result
     */
    public static Map<String, Object> bodyPostRequest(String url, Map<String, Object> body) {
        final HttpUriRequest httpPost = buildBodyPostRequest(url, body);
        return execute(httpPost);
    }

    private static HttpUriRequest buildBodyPostRequest(String url, Map<String, Object> body) {
        final RequestBuilder builder = RequestBuilder.post(url);
        try {
            builder.setEntity(new StringEntity(
                    new ObjectMapper().writeValueAsString(body),
                    StandardCharsets.UTF_8.name()));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        builder.setConfig(TIMEOUT_CONFIG);
        return builder.build();
    }

    /**
     * POST form表单请求,没有结果就返回空
     *
     * @param url    post target
     * @param params form parameters
     * @return Map with json result
     */
    public static Map<String, Object> formPostRequest(String url, Map<String, String> params) {
        final HttpUriRequest httpPost = buildFormPostRequest(url, params);
        return execute(httpPost);
    }


    private static HttpUriRequest buildFormPostRequest(String url, Map<String, String> params) {
        final RequestBuilder builder = RequestBuilder.post(url);
        final MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create()
                .setCharset(StandardCharsets.UTF_8)
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .setContentType(ContentType.MULTIPART_FORM_DATA);
        List<NameValuePair> form = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            form.add(new BasicNameValuePair(key, value));
        }
        builder.setHeader("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE)
                .setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.66 Safari/537.36 Edg/80.0.361.40")
                .setEntity(new UrlEncodedFormEntity(form, StandardCharsets.UTF_8));
        return builder.build();
    }

    /**
     * GET请求URL获取内容,没有结果就返回空
     *
     * @param url     Get host
     * @param queries queries parameters
     * @return Map with json result
     */
    public static Map<String, Object> getRequest(String url, Map<String, String> queries) {
        final HttpUriRequest httpGet = buildGetRequest(url, queries);
        return execute(httpGet);
    }

    private static HttpUriRequest buildGetRequest(String url, Map<String, String> queries) {
        final RequestBuilder builder = RequestBuilder.get(url);
        queries.forEach(builder::addParameter);
        builder.setConfig(TIMEOUT_CONFIG);
        return builder.build();
    }

    private static CloseableHttpClient initHttpClient() {
        TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
        SSLContext sslContext;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            logger.error(e.getMessage(), e.getCause());
            return HttpClients.createSystem();
        }
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("https", sslsf)
                        .register("http", new PlainConnectionSocketFactory())
                        .build();
        BasicHttpClientConnectionManager connectionManager =
                new BasicHttpClientConnectionManager(socketFactoryRegistry);
        return HttpClients.custom().setSSLSocketFactory(sslsf).setConnectionManager(connectionManager).build();
    }

    /**
     * 执行请求并返回结果,没有结果就返回空
     *
     * @param httpUriRequest processed request
     * @return map with json result.
     */
    public static Map<String, Object> execute(HttpUriRequest httpUriRequest) {
        try (final CloseableHttpClient httpClient = initHttpClient()) {
            CloseableHttpResponse response = httpClient.execute(httpUriRequest, HttpClientContext.create());
            return processResult(response);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private static Map<String, Object> processResult(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        EntityUtils.consume(entity);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(objectMapper.readTree(result), new TypeReference<Map<String, Object>>() {
        });
    }
}

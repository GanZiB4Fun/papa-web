package com.ganzib.papa.support.http;

import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpClient 连接管理，1.创建连接管理池，2.根据Post/Get请求建立连接处理方法。3.解析返回体
 *
 * @author raymond
 */
public class HttpFactory<T extends HttpUriRequest> {

    protected CloseableHttpClient client;
    protected String url;
    protected T request;
    protected HttpResponse response;
    private CookieStore cookieStore = new BasicCookieStore();
    public static final int DEFAULT_CONNECT_TIMEOUT = 6000;
    public static final int DEFAULT_SOCKET_TIMEOUT = 6000;
    public static final String DEFAULT_CHARSET = "UTF-8";

    public CloseableHttpClient getClient() {
        client = HttpClients.custom().setConnectionManager(PoolManager.getManager()).setConnectionManagerShared(true)
                .setDefaultCookieStore(cookieStore).build();
        return client;
    }

    public HttpFactory<T> addCookie(String name, String value) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookieStore.addCookie(cookie);
        return this;
    }

    public HttpFactory<T> addCookie(Cookie cookie) {
        cookieStore.addCookie(cookie);
        return this;
    }

    public HttpFactory<T> addHeader(String name, String value) {
        request.setHeader(name, value);
        return this;
    }

    public HttpFactory<T> addHeader(Header header) {
        request.setHeader(header);
        return this;
    }

    public RequestConfig getConfig(int connectionTimeout, int socketTimeout) {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(connectionTimeout).setSocketTimeout(socketTimeout)
                .setCookieSpec(CookieSpecs.STANDARD).build();
        return config;
    }

    public HttpFactory<T> execute() {
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public int getStatusCode() {
        int stateCode = response.getStatusLine().getStatusCode();
        return stateCode;
    }

    public String getContext() {
        return getContent(DEFAULT_CHARSET);
    }

    public String getContent(String charset) {
        String content = null;
        try {
            content = EntityUtils.toString(response.getEntity(), charset);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public Header[] getHeaders() {
        Header[] headers = response.getAllHeaders();
        return headers;
    }

    public Header[] getHeaders(String key) {
        Header[] headers = response.getHeaders(key);
        return headers;
    }

    public void destroy() {
        try {
            if (this.client != null) {
                this.client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * post请求处理
     *
     * @author raymond
     */
    public static class PostSupport extends HttpFactory<HttpPost> {

        private List<NameValuePair> parameters = new ArrayList<NameValuePair>();

        private String body;

        public PostSupport(String url) {
            this(url, HttpFactory.DEFAULT_CONNECT_TIMEOUT, HttpFactory.DEFAULT_SOCKET_TIMEOUT);
        }

        public PostSupport(String url, int connectionTimeout, int socketTimeout) {
            this.url = url;
            this.client = getClient();
            this.request = new HttpPost(url);
            this.request.setConfig(getConfig(connectionTimeout, socketTimeout));
        }

        public PostSupport addContentType(String contentType) {
            request.addHeader("Content-Type", contentType);
            return this;
        }

        public PostSupport addParameter(String name, String value) {
            parameters.add(new BasicNameValuePair(name, value));
            return this;
        }

        public PostSupport addParameters(Map<String, String> params) {
            for (String name : params.keySet()) {
                parameters.add(new BasicNameValuePair(name, params.get(name)));
            }
            return this;
        }


        public PostSupport addBody(String body) {
            this.body = body;
            return this;
        }

        public HttpFactory<HttpPost> execute() {
            try {
                HttpEntity entity = null;
                if (body == null || body.length() < 1) {
                    entity = new UrlEncodedFormEntity(parameters, "utf-8");
                } else {
                    entity = new StringEntity(body, "UTF-8");
                }
                request.setEntity(entity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return super.execute();
        }
    }

    /**
     * get请求处理
     *
     * @author raymond
     */
    public static class GetSupport extends HttpFactory<HttpGet> {
        public GetSupport(String url) {
            this(url, HttpFactory.DEFAULT_CONNECT_TIMEOUT, HttpFactory.DEFAULT_SOCKET_TIMEOUT);
        }

        public GetSupport(String url, int connectionTimeout, int socketTimeout) {
            this.url = url;
            this.client = getClient();
            this.request = new HttpGet(url);
            this.request.setConfig(getConfig(connectionTimeout, socketTimeout));
        }
    }

    public static class PoolManager {
        private static final String HTTP = "http";
        private static final String HTTPS = "https";
        private static final int MAX_POOL = 200;

        private static PoolingHttpClientConnectionManager poolingManager;

        private static SSLConnectionSocketFactory sslConnectionSocketFactory;

        private static SSLContext sslContext;

        private static PlainConnectionSocketFactory plainConnectionSocketFactory;

        private static SSLContext getContext() {
            if (sslContext == null) {
                try {
                    sslContext = SSLContextBuilder.create().loadTrustMaterial(null, new TrustStrategy() {
                        @Override
                        public boolean isTrusted(X509Certificate[] x509Certificates, String s)
                                throws CertificateException {
                            return true;
                        }
                    }).build();
                } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
                    e.printStackTrace();
                }
            }
            return sslContext;
        }

        private static SSLConnectionSocketFactory getSslConnectionSocketFactory() {
            if (sslConnectionSocketFactory == null) {
                sslConnectionSocketFactory = new SSLConnectionSocketFactory(getContext(),
                        new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null,
                        NoopHostnameVerifier.INSTANCE);
            }
            return sslConnectionSocketFactory;
        }

        private static PlainConnectionSocketFactory getPlainConnectionSocketFactory() {
            if (plainConnectionSocketFactory == null) {
                plainConnectionSocketFactory = new PlainConnectionSocketFactory();
            }
            return plainConnectionSocketFactory;
        }

        private static PoolingHttpClientConnectionManager getManager() {
            if (poolingManager == null) {
                Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                        .register(HTTP, getPlainConnectionSocketFactory())
                        .register(HTTPS, getSslConnectionSocketFactory()).build();
                poolingManager = new PoolingHttpClientConnectionManager(registry);
                poolingManager.setMaxTotal(MAX_POOL);// max connection
                poolingManager.setDefaultMaxPerRoute(MAX_POOL / 2);
            }
            return poolingManager;
        }
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }
}

package com.ganzib.papa.support.http;

import org.apache.http.ParseException;

import java.io.IOException;

/**
 * Http连接管理
 *
 * @author raymond
 */
public class HttpUtils {

    public static HttpFactory.PostSupport createPost(String url, int connectionTimeout, int socketTimeout) {
        return new HttpFactory.PostSupport(url, connectionTimeout, socketTimeout);
    }

    public static HttpFactory.PostSupport createPost(String url) {
        return createPost(url, HttpFactory.DEFAULT_CONNECT_TIMEOUT, HttpFactory.DEFAULT_SOCKET_TIMEOUT);
    }

    public static HttpFactory.GetSupport createGet(String url, int connectionTimeout, int socketTimeout) {
        return new HttpFactory.GetSupport(url, connectionTimeout, socketTimeout);
    }

    public static HttpFactory.GetSupport createGet(String url) {
        return createGet(url, HttpFactory.DEFAULT_CONNECT_TIMEOUT, HttpFactory.DEFAULT_SOCKET_TIMEOUT);
    }


    public static void main(String[] args) throws ParseException, IOException {
        String result = HttpUtils.createGet("http://www.baidu.com").execute().getContent("UTF-8");
        System.out.println(result);
    }

}

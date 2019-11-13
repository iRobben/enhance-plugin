package com.zrh.enhancer.plugin.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * httpClient工具类
 *
 * @author zhangrenhua
 * @version 1.0.0
 * @since 2018/1/31
 */
public class HttpClientUtils {

    private static PoolingHttpClientConnectionManager cm;
    private static final String UTF_8 = "UTF-8";
    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);

    private static void init() throws NoSuchAlgorithmException, KeyManagementException {
        if (cm == null) {
            SSLContext sslcontext = SSLContext.getInstance("SSLv3");

            // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                        String paramString) throws CertificateException {
                }
                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                        String paramString) throws CertificateException {
                }
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sslcontext.init(null, new TrustManager[] { trustManager }, null);
            // 设置协议http和https对应的处理socket链接工厂的对象
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE)).build();
            cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            cm.setMaxTotal(200);// 整个连接池最大连接数
            cm.setDefaultMaxPerRoute(20);// 每路由最大连接数，默认值是2
        }
    }



    /**
     * 通过连接池获取HttpClient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        try {
            init();
            return HttpClients.custom().setConnectionManager(cm).build();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param url
     * @return
     */
    public static String httpGetRequest(String url) {
        HttpGet httpGet = new HttpGet(url);
        return getResult(httpGet);
    }

    public static String httpGetRequest(String url, Map<String, Object> params) {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(ub.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return getResult(httpGet);
    }

    public static String httpGetRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        return getResult(httpGet);
    }

    public static String httpPostRequest(String url) {
        HttpPost httpPost = new HttpPost(url);

        return getResult(httpPost);
    }

    public static String httpPostRequest(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
        return getResult(httpPost);
    }

    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);

        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));

        return getResult(httpPost);
    }

    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params,File postFile,String fileKeyName)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        //头信息
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        //把文件转换成流对象FileBody
        FileBody fundFileBin = new FileBody(postFile);
        //设置传输参数
        MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
        //相当于<input type="file" name="media"/>
        multipartEntity.addPart(fileKeyName, fundFileBin);
        //设计文件以外的参数
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            multipartEntity.addPart(key, new StringBody(String.valueOf(params.get(key)), ContentType.create("text/plain", Consts.UTF_8)));
        }
        HttpEntity reqEntity =  multipartEntity.build();
        httpPost.setEntity(reqEntity);
        return getResult(httpPost);
    }

    public static String httpPostRequest(String url, String jsonStr) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity se = new StringEntity(jsonStr, "UTF-8");
        se.setContentType("application/json");
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httpPost.setEntity(se);
        return getResult(httpPost);
    }

    public static String httpPostRequestGzip(String url, String jsonStr) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Accept-Encoding", "gzip, deflate");
        StringEntity se = new StringEntity(jsonStr);
        se.setContentType("text/json");
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httpPost.setEntity(se);
        return getGzipResult(httpPost);
    }

    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }

        return pairs;
    }

    /**
     * 处理Http请求
     *
     * @param request
     * @return
     */
    public static String getResult(HttpRequestBase request) {
        CloseableHttpResponse response = null;
        String result = StringUtils.EMPTY;
        try {
            CloseableHttpClient httpClient = getHttpClient();
            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, "UTF-8");
                    System.out.print(result);
                    response.close();
                }
            }else{
                request.abort();
            }
        } catch (IOException e) {
            LOGGER.error("http请求失败", e);
        }finally {
            if(response != null ){
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("http请求失败", e);
                }
            }
        }
        return result;
    }

    /**
     * 处理Http请求 gzip
     *
     * @param request
     * @return
     */
    private static String getGzipResult(HttpRequestBase request) {
        CloseableHttpResponse response = null;
        String result = StringUtils.EMPTY;
        try {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                    .setSocketTimeout(5000).build();
            request.setConfig(requestConfig);
            CloseableHttpClient httpClient = getHttpClient();
            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(new GzipDecompressingEntity(response.getEntity()), "UTF-8");
                    response.close();
                }
            }else{
                request.abort();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(response != null ){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * https 请求
     * 
     * @param url
     * @return
     */
    public static String httpsGetRequest(String url, Map<String, Object> headers) {
        CloseableHttpResponse response = null;
        String result = StringUtils.EMPTY;
        try {
            HttpGet httpGet = new HttpGet(url);
            for (Map.Entry<String, Object> param : headers.entrySet()) {
                httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
            }
            CloseableHttpClient httpClient = getHttpClient();
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, "UTF-8");
                    response.close();
                }
            }else{
                httpGet.abort();
            }
        }  catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if(response != null ){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    public static void main(String[] args) throws UnsupportedEncodingException {

        String url = "https://114.55.36.16:8090/CreditFunc/v2.1/DriverCheck";
        String jsonStr = "{\n" +
                "  \"loginName\": \"test1\",\n" +
                "  \"pwd\": \"123456\",\n" +
                "  \"serviceName\":\"DriverCheck\",\n" +
                "  \"param\":{\n" +
                "    \"name\":\"仝帅坤\",\n" +
                "    \"idCard\":\"412725199312176959\",\n" +
                "    \"fileNumber\":\"310046026682\"\n" +
                "  }\n" +
                "}";
        String s = httpPostRequest(url, jsonStr);
        System.out.println(s);


    }
  
}

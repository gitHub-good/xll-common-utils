package com.xll.common.utils.http;

import com.xll.common.utils.json.JsonUtil;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpJsonClient {
    private static final Logger logger = LoggerFactory.getLogger(HttpJsonClient.class);
    static CloseableHttpClient httpClient = null;
    private static final int defautTimeoutSecond = 20;
    static RequestConfig defaultRequestConfig = null;

    public HttpJsonClient() {
    }

    public static String getJsonData(String url, Map<String, Object> params, int second) {
        try {
            return getJsonData(url, params, second, (Header[])null);
        } catch (Exception var4) {
            logger.error("get data from [" + url + "] error", var4);
            return "";
        }
    }

    public static String getJsonData(String url, Map<String, Object> params) {
        try {
            return getJsonData(url, params, 0, (Header[])null);
        } catch (Exception var3) {
            logger.error("get data from [" + url + "] error", var3);
            return "";
        }
    }

    private static RequestConfig makeLocalRequestConfig(int second) {
        RequestConfig requestConfig = null;
        if (second > 0) {
            requestConfig = RequestConfig.copy(defaultRequestConfig).setSocketTimeout(second * 1000).setConnectTimeout(second * 1000).setConnectionRequestTimeout(second * 1000).build();
        } else {
            requestConfig = RequestConfig.copy(defaultRequestConfig).setSocketTimeout(20000).setConnectTimeout(20000).setConnectionRequestTimeout(20000).build();
        }

        return requestConfig;
    }

    public static String getJsonData(String url, Map<String, Object> params, int second, Header[] headers) {
        try {
            String str;
            try {
                logger.debug("begin to get url:" + url);
                if (params != null && !params.isEmpty()) {
                    List<NameValuePair> values = new ArrayList();
                    Iterator var16 = params.entrySet().iterator();

                    while(var16.hasNext()) {
                        Map.Entry<String, Object> entity = (Map.Entry)var16.next();
                        BasicNameValuePair pare = new BasicNameValuePair((String)entity.getKey(), entity.getValue().toString());
                        values.add(pare);
                    }

                    str = URLEncodedUtils.format(values, "UTF-8");
                    if (url.indexOf("?") > -1) {
                        url = url + "&" + str;
                    } else {
                        url = url + "?" + str;
                    }
                }

                logger.debug("after url:" + url);
                HttpGet httpget = new HttpGet(url);
                httpget.setConfig(makeLocalRequestConfig(second));
                if (headers != null) {
                    httpget.setHeaders(headers);
                }

                CloseableHttpResponse r = httpClient.execute(httpget);
                String result = EntityUtils.toString(r.getEntity());
                r.close();
                if (result != null) {
                    Matcher m = Pattern.compile("<\\/script>", 2).matcher(result);
                    result = m.replaceAll("<\\\\/script>");
                }

                return result;
            } catch (ClientProtocolException var12) {
                logger.error("ClientProtocolException,get data from [" + url + "] error", var12);
                str = "";
                return str;
            } catch (IOException var13) {
                logger.error("IOException,get data from [" + url + "] error", var13);
                str = "";
                return str;
            }
        } finally {
            ;
        }
    }

    public static void getJsonDown(String url, Map<String, Object> params, String filePath, int second) {
        try {
            try {
                logger.debug("begin to get url:" + url);
                if (params != null && !params.isEmpty()) {
                    List<NameValuePair> values = new ArrayList();
                    Iterator var5 = params.entrySet().iterator();

                    while(true) {
                        if (!var5.hasNext()) {
                            String str = URLEncodedUtils.format(values, "UTF-8");
                            if (url.indexOf("?") > -1) {
                                url = url + "&" + str;
                            } else {
                                url = url + "?" + str;
                            }
                            break;
                        }

                        Map.Entry<String, Object> entity = (Map.Entry)var5.next();
                        BasicNameValuePair pare = new BasicNameValuePair((String)entity.getKey(), entity.getValue().toString());
                        values.add(pare);
                    }
                }

                logger.debug("after url:" + url);
                HttpGet httpget = new HttpGet(url);
                httpget.setConfig(makeLocalRequestConfig(second));
                HttpResponse response = httpClient.execute(httpget);
                if (response.getStatusLine().getStatusCode() != 200) {
                    logger.error("url:[" + url + "] error:" + response.getStatusLine().getStatusCode());
                    return;
                }

                HttpEntity entity = response.getEntity();
                InputStream input = null;

                try {
                    input = entity.getContent();
                    File file = new File(filePath);
                    FileOutputStream output = FileUtils.openOutputStream(file);

                    try {
                        IOUtils.copy(input, output);
                    } finally {
                        IOUtils.closeQuietly(output);
                    }
                } finally {
                    IOUtils.closeQuietly(input);
                }
            } catch (ClientProtocolException var30) {
                logger.error("ClientProtocolException,get data from [" + url + "] error", var30);
            } catch (IOException var31) {
                logger.error("IOException,get data from [" + url + "] error", var31);
            }

        } finally {
            ;
        }
    }

    public static String deleteIndexData(String url) throws ClientProtocolException, IOException {
        try {
            logger.debug("begin to get url:" + url);
            HttpDelete httpget = new HttpDelete(url);
            CloseableHttpResponse r = httpClient.execute(httpget);
            String result = EntityUtils.toString(r.getEntity());
            r.close();
            return result;
        } catch (Exception var8) {
            logger.error("get data from [" + url + "] error", var8);
            String var2 = "";
            return var2;
        } finally {
            ;
        }
    }

    public static String postJsonDataByJson(String url, Map<String, ?> params) throws ClientProtocolException, IOException {
        return postJsonDataByJson(url, (Map)params, 0);
    }

    public static String postJsonDataByJson(String url, String jsonParam, int second) throws ClientProtocolException, IOException {
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(makeLocalRequestConfig(second));
            if (jsonParam != null) {
                ContentType ct = ContentType.create("text/xml", Charset.forName("UTF-8"));
                ByteArrayEntity mult = new ByteArrayEntity(jsonParam.getBytes("UTF-8"), ct);
                httpPost.setEntity(mult);
            }

            logger.debug("begin to post url:" + url);
            CloseableHttpResponse r = httpClient.execute(httpPost);
            String result = EntityUtils.toString(r.getEntity());
            r.close();
            return result;
        } catch (Exception var10) {
            logger.error("get data from [" + url + "] error", var10);
            String var4 = "";
            return var4;
        } finally {
            ;
        }
    }

    public static InputStream postJsonDataByJson(String url, byte[] b, int second) throws IOException {
        try {
            ContentType ct;
            try {
                HttpPost httpPost = new HttpPost(url);
                httpPost.setConfig(makeLocalRequestConfig(second));
                if (b != null) {
                    ct = ContentType.create("text/xml", Charset.forName("UTF-8"));
                    ByteArrayEntity mult = new ByteArrayEntity(b, ct);
                    httpPost.setEntity(mult);
                }

                logger.debug("begin to post url:" + url);
                CloseableHttpResponse r = httpClient.execute(httpPost);
                InputStream is = r.getEntity().getContent();
                r.close();
                return is;
            } catch (Exception var10) {
                logger.error("get data from [" + url + "] error", var10);
                return null;
            }
        } finally {
            ;
        }
    }

    public static String postJsonDataByJson(String url, Map<String, ?> params, int second) throws ClientProtocolException, IOException {
        try {
            String var4;
            try {
                String str = null;
                if (params != null) {
                    str = JsonUtil.writeValue(params);
                }

                var4 = postJsonDataByJson(url, str, second);
                return var4;
            } catch (Exception var8) {
                logger.error("get data from [" + url + "] error", var8);
                var4 = "";
                return var4;
            }
        } finally {
            ;
        }
    }

    public static String postJsonData(String url, Map<String, ?> params, int second, Header[] headers) {
        try {
            String var5;
            try {
                HttpPost httpPost = new HttpPost(url);
                httpPost.setConfig(makeLocalRequestConfig(second));
                if (headers != null) {
                    httpPost.setHeaders(headers);
                }

                if (params != null) {
                    List<NameValuePair> values = new ArrayList();
                    Iterator var6 = params.entrySet().iterator();

                    while(var6.hasNext()) {
                        Map.Entry<String, ?> entity = (Map.Entry)var6.next();
                        BasicNameValuePair pare = new BasicNameValuePair((String)entity.getKey(), entity.getValue().toString());
                        values.add(pare);
                    }

                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(values, "UTF-8");
                    httpPost.setEntity(entity);
                }

                logger.debug("begin to post url:" + url);
                CloseableHttpResponse r = httpClient.execute(httpPost);
                String result = EntityUtils.toString(r.getEntity());
                r.close();
                return result;
            } catch (UnsupportedEncodingException var14) {
                logger.error("UnsupportedEncodingException!Post data to url:" + url + "error!", var14);
                var5 = "";
                return var5;
            } catch (ClientProtocolException var15) {
                logger.error("ClientProtocolException!Post data to url:" + url + "error!", var15);
                var5 = "";
                return var5;
            } catch (IOException var16) {
                logger.error("IOException!Post data to url:" + url + "error!", var16);
                var5 = "";
                return var5;
            }
        } finally {
            ;
        }
    }

    public static String postJsonData(String url, Map<String, ?> params) {
        try {
            return postJsonData(url, params, 0, (Header[])null);
        } catch (Exception var3) {
            logger.error("get data from [" + url + "] error", var3);
            return "";
        }
    }

    public static <T> T postJsonObjectData(String url, HashMap<String, Object> params, Class<T> cls) throws ClientProtocolException, IOException {
        String str = postJsonData(url, params, 0, (Header[])null);
        return JsonUtil.readValue(str, cls);
    }

    public static void main(String[] args) {
        String str = getJsonData("http://www.baidu.com", (Map)null);
        System.out.println(str);
        System.out.println("------------------------------------------------------");
        String str2 = getJsonData("https://cas-test.baozun.cn:8443/casweb/login", (Map)null);
        System.out.println(str2);
        String url = "http://127.0.0.1:8080/person/recive2";
        Map<String, Object> params = new HashMap();
        params.put("str", "中文");
        postJsonData(url, params);
    }

    static {
        defaultRequestConfig = RequestConfig.custom().setCookieSpec("standard-strict").build();
        httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
    }
}

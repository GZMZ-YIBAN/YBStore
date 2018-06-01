package utils;

import cn.yiban.util.HTTPSimple;
import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.*;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

/**
 * -----------------------------
 * Copyright © 2017, EchoCow
 * All rights reserved.
 *
 * @author EchoLZY
 * @version 2.0
 * <p>
 * -----------------------------
 * @description
 * @date 18:59 2018/4/17
 * @modified By EchoLZY
 */
public class HttpMethod {
    private static Logger logger = LogManager.getLogger(HttpMethod.class.getName());

    public static String get(String url) {
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            logger.error("Exception occur when send http get request!", e);
            logger.error("支付出现异常：" + e.getMessage());
            logger.fatal("异常详细信息：" + DoUtil.getExceptionInfo(e));
            return "error5";
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * 发送HttpPost请求
     *
     * @param strURL 服务地址
     * @param params 参数列表
     * @return 成功:返回json字符串<br/>
     */
    public static String post(String strURL, Map<String, String> params) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.append(JSON.toJSONString(params));
            out.flush();
            out.close();

            int code = connection.getResponseCode();
            InputStream is = null;
            if (code == 200) {
                is = connection.getInputStream();
            } else {
                is = connection.getErrorStream();
            }
            int length = connection.getContentLength();
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                String result = new String(data, "UTF-8");
                return result;
            }
        } catch (Exception e) {
            logger.error("Exception occur when send http post request!", e);
        }
        return null;
    }


    /**
     * HTTP的POST请求
     *
     * @param    url 请求的URL地址
     * @param    param 参数列表
     * @return String 返回内容
     */
    public static String getPOST(String url, List<NameValuePair> param) {
        String responseContext = "";
        int found = url.indexOf('?');
        if (found > 0) {
            url = url.substring(0, found);
        }
        try {
            CloseableHttpClient httpclient = getClientInstance(url);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(param,"UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if (status > 300 && status < 310) {
                Header[] h = response.getHeaders("Location");
                if (h.length > 0) {
                    httpclient.close();
                    return HTTPSimple.POST(h[0].toString().substring(10), param);
                }
            }
            HttpEntity entity = response.getEntity();
            responseContext = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            httpclient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return responseContext;
    }

    /**
     * 通过URL地址验证是否HTTPS安全链接
     *
     * @param    url 请求的URL地址
     * @return boolean
     */
    private static boolean isSecurity(String url) throws Exception {
        URL u = new URL(url);
        return u.getProtocol().contentEquals("https");
    }

    /**
     * 初始化HttpClient对象
     * <p>
     * 若url为https链接，则使用SSLConnection来初始化
     * 若为普通的HTTP链接，只使用默认的HttpClient参数初始化
     *
     * @param    url 请求的URL地址
     * @return CloseableHttpClient
     */
    private static CloseableHttpClient getClientInstance(String url) throws Exception {
        CloseableHttpClient httpclient = null;
        if (isSecurity(url)) {
            KeyStore myTrustKeyStore = KeyStore.getInstance(
                    KeyStore.getDefaultType()
            );
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(myTrustKeyStore, new TrustStrategy() {
                @Override
                public boolean isTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier()
            );
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } else {
            httpclient = HttpClients.createDefault();
        }
        return httpclient;
    }
}

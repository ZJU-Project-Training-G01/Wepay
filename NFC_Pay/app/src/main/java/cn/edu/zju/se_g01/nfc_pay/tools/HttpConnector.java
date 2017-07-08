package cn.edu.zju.se_g01.nfc_pay.tools;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dddong on 2017/7/8.
 */

@SuppressWarnings("deprecation")
public class HttpConnector {

    //得到HttpClient
    public static HttpClient getHttpClient(){
        HttpParams mHttpParams=new BasicHttpParams();
        //设置网络链接超时
        //即:Set the timeout in milliseconds until a connection is established.
        HttpConnectionParams.setConnectionTimeout(mHttpParams, 20*1000);
        //设置socket响应超时
        //即:in milliseconds which is the timeout for waiting for data.
        HttpConnectionParams.setSoTimeout(mHttpParams, 20*1000);
        //设置socket缓存大小
        HttpConnectionParams.setSocketBufferSize(mHttpParams, 8*1024);
        //设置是否可以重定向
        HttpClientParams.setRedirecting(mHttpParams, true);

        HttpClient httpClient=new DefaultHttpClient(mHttpParams);
        return httpClient;
    }


    public static JSONObject getJSONByHttpPost(String url, Map<String, String> paramsHashMap) {
        return getJSONByHttpPost(url,paramsHashMap,"utf-8");
    }

    public static JSONObject getJSONByHttpPost(String url, Map<String, String> paramsHashMap, String encoding) {
        JSONObject resultJsonObject = null;

        List<NameValuePair> nameValuePairArrayList = new ArrayList<NameValuePair>();
        // 将传过来的参数填充到List<NameValuePair>中
        if (paramsHashMap != null && !paramsHashMap.isEmpty()) {
            for (Map.Entry<String, String> entry : paramsHashMap.entrySet()) {
                nameValuePairArrayList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        UrlEncodedFormEntity entity = null;
        try {
            // 利用List<NameValuePair>生成Post请求的实体数据
            // 此处使用了UrlEncodedFormEntity!!!
            entity = new UrlEncodedFormEntity(nameValuePairArrayList, encoding);
            HttpPost httpPost = new HttpPost(url);
            // 为HttpPost设置实体数据
            httpPost.setEntity(entity);
            HttpClient httpClient = getHttpClient();
            // HttpClient发出Post请求
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                // 得到httpResponse的实体数据
                resultJsonObject = getJSONObject(httpResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJsonObject;
    }

    //得到JSONObject(Get方式)
    public JSONObject getJSONObjectByHttpGet(String uriString){
        JSONObject resultJsonObject=null;
        if ("".equals(uriString)||uriString==null) {
            return null;
        }
        HttpClient httpClient=getHttpClient();
        StringBuilder urlStringBuilder=new StringBuilder(uriString);
        //利用URL生成一个HttpGet请求
        HttpGet httpGet=new HttpGet(urlStringBuilder.toString());
        BufferedReader bufferedReader=null;
        HttpResponse httpResponse=null;
        try {
            //HttpClient发出一个HttpGet请求
            httpResponse=httpClient.execute(httpGet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //得到httpResponse的状态响应码
        int statusCode=httpResponse.getStatusLine().getStatusCode();
        if (statusCode== HttpStatus.SC_OK) {
            //得到httpResponse的实体数据
            resultJsonObject = getJSONObject(httpResponse);
        }
        return resultJsonObject;
    }

    public static JSONObject getJSONObject(HttpResponse httpResponse) {
        JSONObject resultJsonObject = null;

        HttpEntity httpEntity = httpResponse.getEntity();
        if (httpEntity != null) {
            try {
                //默认服务器返回的编码格式是 UTF-8
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpEntity.getContent(),"UTF-8"), 8 * 1024);
                StringBuilder entityStringBuilder = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    entityStringBuilder.append(line + "/n");
                }
                // 利用从HttpEntity中得到的String生成JsonObject
                resultJsonObject = new JSONObject(entityStringBuilder.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJsonObject;
        }
        return null;
    }
}

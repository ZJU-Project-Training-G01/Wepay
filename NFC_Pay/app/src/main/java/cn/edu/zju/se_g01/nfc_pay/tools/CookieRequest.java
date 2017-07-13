package cn.edu.zju.se_g01.nfc_pay.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.se_g01.nfc_pay.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dddong on 2017/7/9.
 */

public class CookieRequest extends Request<JSONObject> {

    private static final String TAG = "CookieRequest";
    private Map<String, String> mMap;
    private Response.Listener<JSONObject> mListener;
    private String mHeader;
    private Map<String, String> sendHeader=new HashMap<String, String>(1);
    private Context mAppContext;
    public CookieRequest(Context context, int method, String url, Map map, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        super(method, url, errorListener);
        mListener = listener;
        mMap = map;
        mAppContext = context.getApplicationContext();
    }
    //当http请求是post时，则需要该使用该函数设置往里面添加的键值对
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//获取头部信息
            mHeader = response.headers.toString();
//            Log.d(TAG,"get headers in parseNetworkResponse "+response.headers.toString());
//获取cookie头部信息
            Map<String, String> responseHeaders = response.headers;
            String rawCookies = responseHeaders.get("Set-Cookie");
            if(rawCookies != null) {
                //;分隔获取sessionid
                String[] splitCookie = rawCookies.split(";");
                for (int i = 0; i < splitCookie.length; i++) {
                    if(splitCookie[i].split("=")[0].contains("PHPSESSID")) {
                        Log.d(TAG, "get phpsessid from server:" + splitCookie[i]);
                        //使用SharedPreferences本地存储
                        SharedPreferences sp = mAppContext.getSharedPreferences(mAppContext.getString(R.string.cookie_preference_file),Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsWriter = sp.edit();
                        prefsWriter.putString("cookie",splitCookie[i]);
                        prefsWriter.apply();//
                        break;
                    }
                }
            }
//将cookie字符串添加到jsonObject中，该jsonObject会被deliverResponse递交，调用请求时则能在onResponse中得到
            JSONObject jsonObject = new JSONObject(jsonString);
//    jsonObject.put("Cookie",rawCookies);    //自行添加
//            Log.i(TAG,"jsonObject "+ response.toString());
            return Response.success(jsonObject,  HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        //获取存储的session值
        SharedPreferences sp = mAppContext.getSharedPreferences(mAppContext.getString(R.string.cookie_preference_file), MODE_PRIVATE);
        String localCookieStr = sp.getString("cookie", "");
        if (!localCookieStr.equals("")) {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Cookie", localCookieStr);//设置session
            Log.d(TAG, "headers：" + headers);
            return headers;
        } else {
            return super.getHeaders();
        }
    }
}

package cn.edu.zju.se_g01.nfc_pay.orders;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.edu.zju.se_g01.nfc_pay.tools.MySingleton;

import static android.content.ContentValues.TAG;

/**
 * Created by dddong on 2017/7/7.
 */

public class OrderLab {
    private static OrderLab sOrderLab;

    private ArrayList<Order> mOrders;
    /*
    CrimeLab类的构造方法需要一个Context参数。这在Android开发里很常见，使用Context 参数，单例可完成启动activity、获取项目资源，查找应用的私有存储空间等任务。
注意，在get(Context)方法里，我们并没有直接将Context参数传给构造方法。该Context 可能是一个Activity，也可能是另一个Context对象，如Service。在应用的整个生命周期里， 我们无法保证只要CrimeLab需要用到Context，Context就一定会存在。
因此，为保证单例总是有Context可以使用，可调用getApplicationContext()方法，将 不确定是否存在的Context替换成application context。application context是针对应用的全局性 Context。任何时候，只要是应用层面的单例，就应该一直使用application context。
     */
    private Context mAppContext;


    public OrderLab(Context mAppContext) {
        this.mAppContext = mAppContext;
        mOrders = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            mOrders.add(new Order(i));
        }

        String url = "http://localhost/getOrders.php";
        //TODO: 需要修改为CookieRequest
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int nOrders = response.getJSONArray("data").length();
                            for (int i = 0; i < nOrders; i++) {
                                JSONObject each_order = response.getJSONArray("data").getJSONObject(i);
                                DateFormat dfmt = new SimpleDateFormat("yyyy-MM-dd");    //TODO: 日期格式可能会变动
                                Date order_date = dfmt.parse(each_order.getString("order_time"));
                                Order order = new Order(each_order.getInt("order_id"), each_order.getString("good_name"), each_order.getString("img_url"),
                                        each_order.getInt("amount"), each_order.getDouble("unit_price"), each_order.getInt("order_status"),
                                        order_date);
                                mOrders.add(order);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        });
        MySingleton.getInstance(mAppContext.getApplicationContext()).addToRequestQueue(jsonObjReq);

        ImageLoader image;
        String SESSION_ID = "xxx"; //TODO: 这里需要获取 SESSION_ID
//        JSONObject jsonResult = HttpConnector.getJSONByHttpGet("http://localhost/getOrders.php?session_id=" + SESSION_ID );
        /*
        TODO: 通过网络请求向mOrders中填充订单数据
         */
    }

    public static OrderLab get(Context c) {
        if(sOrderLab == null) {
            sOrderLab = new OrderLab(c.getApplicationContext());
        }
        return sOrderLab;
    }

    public ArrayList<Order> getOrders() {
        return mOrders;
    }

    public Order getOrder(int order_id) {
        for (Order order: mOrders) {
            if(order.getOrder_id() == order_id) {
                return order;
            }
        }
        return null;
    }
}

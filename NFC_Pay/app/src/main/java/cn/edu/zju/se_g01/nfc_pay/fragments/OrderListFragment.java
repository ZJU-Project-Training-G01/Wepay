package cn.edu.zju.se_g01.nfc_pay.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.se_g01.nfc_pay.LoginActivity;
import cn.edu.zju.se_g01.nfc_pay.R;
import cn.edu.zju.se_g01.nfc_pay.config.Config;
import cn.edu.zju.se_g01.nfc_pay.orders.Order;
import cn.edu.zju.se_g01.nfc_pay.tools.CookieRequest;
import cn.edu.zju.se_g01.nfc_pay.tools.EasyMethod;
import cn.edu.zju.se_g01.nfc_pay.tools.ImageDownloader;
import cn.edu.zju.se_g01.nfc_pay.tools.MySingleton;

import static cn.edu.zju.se_g01.nfc_pay.tools.EasyMethod.*;

/**
 * Created by dddong on 2017/7/7.
 */

public class OrderListFragment extends ListFragment {
    private static final String TAG = "OrderListFragment";
    private ArrayList<Order> mOrders = null;
    private OrderAdapter orderAdapter;
    ImageDownloader<ImageView> mImageDownloadThread;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mOrders = OrderLab.get(getActivity()).getOrders();
        mOrders = new ArrayList<>();

        String url = Config.getFullUrl("GetOrders");
        CookieRequest orderLabReq = new CookieRequest(getActivity(), Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "Get Orders Response:" +  response.toString());
                    int code = response.getInt("code");

                    if(code == 1) {
                        Toast.makeText(getActivity(), response.getString("msg"), Toast.LENGTH_LONG).show();
                    } else if(code == 0) {
                        int nOrders = response.getJSONArray("data").length();
                        for (int i = 0; i < nOrders; i++) {
                            JSONObject each_order = response.getJSONArray("data").getJSONObject(i);
                            DateFormat dfmt = new SimpleDateFormat("yyyy-MM-dd");
                            Date order_date = dfmt.parse(each_order.getString("order_time"));
                            Order order = new Order(each_order.getInt("order_id"), each_order.getString("good_name"), each_order.getString("img_url"),
                                    each_order.getInt("amount"), each_order.getDouble("unit_price"), each_order.getInt("order_status"),
                                    order_date);
                            mOrders.add(order);
                        }
                    } else if(code == 2) {
                        Toast.makeText(getActivity(), response.getString("msg"), Toast.LENGTH_LONG).show();
                        EasyMethod.clearSharedPreferences(getActivity().getApplicationContext());
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                    orderAdapter = new OrderAdapter(mOrders);

                    setListAdapter(orderAdapter);

                    mImageDownloadThread = new ImageDownloader<>(new Handler());
                    mImageDownloadThread.setListener(new ImageDownloader.Listener<ImageView>() {
                        @Override
                        public void onImageDownloaded(ImageView imageView, Bitmap thumbnail) {
                            if (isVisible()) {
                                imageView.setImageBitmap(thumbnail);
                            }
                        }
                    });
                    mImageDownloadThread.start();
                    mImageDownloadThread.getLooper();
                    Log.i(TAG, "background thread start");
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }, null);
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(orderLabReq);
    }

    public class OrderAdapter extends ArrayAdapter<Order> {
        private static final String TAG = "OrderAdapter";
        public OrderAdapter(ArrayList<Order> orders) {
            super(getActivity(), 0, orders);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.order_list_item, null);
            }
            final Order o = getItem(position);
            if(o == null) {
                return convertView;
            }

            //获取UI组件
            TextView goodNameTextView = (TextView)convertView.findViewById(R.id.goodNameTextView);
            TextView totNumAndPriceTextView = (TextView)convertView.findViewById(R.id.totalNumAndPriceTextView);
            TextView orderStatusTextView = (TextView)convertView.findViewById(R.id.orderStatusTextView);
            final Button confirmRecvGoodsBtn = (Button)convertView.findViewById(R.id.confirmRecvGoodsbutton);
            TextView orderDateTextView = (TextView)convertView.findViewById(R.id.orderDateTextView);


            //更新 UI 组件的内容为订单信息
            goodNameTextView.setText(o.getGood_name());
            double totalPrice = o.getUnit_price() * o.getAmount();
            totNumAndPriceTextView.setText("数量: x" + o.getAmount() + " 合计:" + "￥" + String.format("%.2f", totalPrice));


            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            orderDateTextView.setText("日期:" + formatter.format(o.getOrder_date()));

            orderStatusTextView.setText(o.getOrder_status_str());
            if(!o.isWaitingForBuyerToConfirm()) {
                confirmRecvGoodsBtn.setEnabled(false);
//                confirmRecvGoodsBtn.setBackgroundColor(getResources().getColor(R.color.tab_background));
//                ((RelativeLayout)convertView).removeView(confirmRecvGoodsBtn);
            } else {
                confirmRecvGoodsBtn.setEnabled(true);
            }

            final String mConfirmRecvUrl = Config.getFullUrl("ConfirmRecvGoods");

            if(o.isWaitingForBuyerToConfirm()) {
                //用户点击确认收货按钮之后，按钮变灰，文字改变成"订单完成"
                confirmRecvGoodsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> paramsMap = new HashMap<>(1);
                        paramsMap.put("orderID", Integer.toString(o.getOrder_id()));
                        CookieRequest confirmRecvGoodsReq = new CookieRequest(getActivity(), Request.Method.POST,
                                mConfirmRecvUrl, paramsMap, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, "confirmRecvGoodsResponse:" + response.toString());
                                try {
                                    int code = response.getInt("code");
                                    if (code == 0) {
                                        confirmRecvGoodsBtn.setEnabled(false);
                                        confirmRecvGoodsBtn.setText("订单完成");
                                    } else if (code == 1) {
                                        Toast.makeText(getActivity(), response.getString("msg"), Toast.LENGTH_LONG).show();
                                    } else if (code == 2) {
                                        //用户未登录
                                        Toast.makeText(getActivity(), response.getString("msg"), Toast.LENGTH_LONG).show();
                                        clearSharedPreferences(getActivity().getApplicationContext());
                                        startActivity(new Intent(getActivity(), LoginActivity.class));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, error.getMessage());
                            }
                        });
                        MySingleton.getInstance(getActivity()).getRequestQueue().add(confirmRecvGoodsReq);
                    }
                });
            }

            //下载图片
            ImageView imageView = (ImageView)convertView.findViewById(R.id.orderGoodsimageView);
            mImageDownloadThread.queueImage(imageView, o.getImageUrl());
            return convertView;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageDownloadThread.quit();
        Log.i(TAG, "background thread destroyed");
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        mImageDownloadThread.clearQueue();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume() called");
//        mOrders.clear();
//
//        String url = Config.getFullUrl("GetOrders");
//        CookieRequest orderLabReq = new CookieRequest(getActivity(), Request.Method.GET, url,
//                null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    Log.d(TAG, "Get Orders Response:" +  response.toString());
//                    int code = response.getInt("code");
//
//                    if(code == 1) {
//                        Toast.makeText(getActivity(), response.getString("msg"), Toast.LENGTH_LONG).show();
//                    } else if(code == 0) {
//                        int nOrders = response.getJSONArray("data").length();
//                        for (int i = 0; i < nOrders; i++) {
//                            JSONObject each_order = response.getJSONArray("data").getJSONObject(i);
//                            DateFormat dfmt = new SimpleDateFormat("yyyy-MM-dd");
//                            Date order_date = dfmt.parse(each_order.getString("order_time"));
//                            Order order = new Order(each_order.getInt("order_id"), each_order.getString("good_name"), each_order.getString("img_url"),
//                                    each_order.getInt("amount"), each_order.getDouble("unit_price"), each_order.getInt("order_status"),
//                                    order_date);
//                            mOrders.add(order);
//                        }
//                    }
//                    orderAdapter.clear();
//                    orderAdapter.addAll(mOrders);
//
////                    setListAdapter(orderAdapter);
//
//                    mImageDownloadThread = new ImageDownloader<>(new Handler());
//                    mImageDownloadThread.setListener(new ImageDownloader.Listener<ImageView>() {
//                        @Override
//                        public void onImageDownloaded(ImageView imageView, Bitmap thumbnail) {
//                            if (isVisible()) {
//                                imageView.setImageBitmap(thumbnail);
//                            }
//                        }
//                    });
//                    mImageDownloadThread.start();
//                    mImageDownloadThread.getLooper();
//                    Log.i(TAG, "image downloader background thread start");
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                }
//            }
//        }, null);
//        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(orderLabReq);
    }
}

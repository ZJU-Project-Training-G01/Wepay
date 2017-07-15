package cn.edu.zju.se_g01.nfc_pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.FormatException;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.edu.zju.se_g01.nfc_pay.Good.Goods;
import cn.edu.zju.se_g01.nfc_pay.Good.GoodsLab;
import cn.edu.zju.se_g01.nfc_pay.tools.CookieRequest;
import cn.edu.zju.se_g01.nfc_pay.tools.MySingleton;
import cn.edu.zju.se_g01.nfc_pay.tools.NfcOperator;

/**
 * Created by Rexxar on 2017/7/8.
 */

public class GoodActivity extends Activity {
    public static final String TAG = "GGGoodActivity";

    public static final String EXTRA_GOOD_ID =
            "cn.edu.zju.se_g01.nfc_pay.good_id";
    private Goods g = new Goods();
    int buyAmount;
    List<Integer> list;

    ImageView img;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Bitmap bitmap = (Bitmap) msg.obj;
                img.setImageBitmap(bitmap);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good);

        String goodId = (String) getIntent().getSerializableExtra(EXTRA_GOOD_ID);

//        g = GoodsLab.getInstance().getGood(goodId);
//        g = new Goods("0000", "商品名称",
//                0, "http://www.lagou.com/image1/M00/31/84/Cgo8PFWLydyAKywFAACk6BPmTzc228.png", "none");

        Log.d("GoodActivity", "create good activity");

        img = (ImageView) findViewById(R.id.detail_good_img);
        final TextView goodName = (TextView) findViewById(R.id.detail_good_name);
        final TextView unitPrice = (TextView) findViewById(R.id.detail_unit_price);
        Spinner spinner = (Spinner) findViewById(R.id.buy_amount_spinner);
        final Button nfcButton = (Button) findViewById(R.id.set_nfc_button);
        final Button buyButton = (Button) findViewById(R.id.buy_button);


        //显示默认值
        img.setImageResource(R.drawable.ic_default_img);
//        imgDownloaderThread.queueImg(img, g.getImgUrl());
        list = new ArrayList<>();
        for (int i = 1; i <= 20; i++) list.add(i);
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>
                (this, android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setSelection(0);
        buyAmount = 1;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buyAmount = list.get(position);
//                Toast.makeText(getApplicationContext(), String.valueOf(buyAmount), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //TODO 请求商品用CookieRequest
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        String url = "http://120.77.34.254/business-system/backend/public/GetGood?good_id=" + goodId;
//        Map<String, String> goodMap = new HashMap<>();
//        goodMap.put("good_id", goodId);
        CookieRequest goodRequest = new CookieRequest(
                getApplicationContext(),
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            g.setGoodsId(response.getString("good_id"));
                            g.setGoodsName(response.getString("good_name"));
                            g.setUnitPrice(Double.valueOf(response.getString("unit_price")));
                            g.setImgUrl(response.getString("img_url"));

                            //TODO 下面的代码全部写在onResponse方法里
                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        URL url = new URL(g.getImgUrl());
                                        InputStream is = url.openStream();
                                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                                        Message msg = new Message();
                                        msg.what = 0;
                                        msg.obj = bitmap;
                                        handler.sendMessage(msg);

                                        is.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();


                            goodName.setText(g.getGoodsName());
                            unitPrice.setText(String.format("￥ %.2f", g.getUnitPrice()));


                            nfcButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
                                    NfcOperator nfcOperator = NfcOperator.getInstance();
                                    try {
//                    String content = nfcOperator.read();
//                    Toast.makeText(getApplicationContext(), "nfc:" + content, Toast.LENGTH_SHORT).show();

                                        nfcOperator.write(g.getGoodsId());
                                        Toast.makeText(getApplicationContext(), g.getGoodsId() + "写入成功", Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
//                }
                                }
                            });

                            buyButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getApplicationContext(), "购买" + buyAmount + "件商品", Toast.LENGTH_SHORT).show();

                                    RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
                                    String url = "http://120.77.34.254/business-system/backend/public/MakeNewOrder";
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("good_id", g.getGoodsId());
                                    map.put("amount", String.valueOf(buyAmount));
                                    map.put("unit_price", String.valueOf(g.getUnitPrice()));

                                    CookieRequest postRequest = new CookieRequest(getApplicationContext(),
                                            Request.Method.POST,
                                            url,
                                            map,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        Toast.makeText(getApplicationContext(), "状态返回结果：" + response.getString("status"), Toast.LENGTH_SHORT).show();

                                                        Log.d("buy button response", response.getString("status"));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getApplicationContext(), "提交订单超时", Toast.LENGTH_SHORT).show();
                                                    Log.d(TAG, "提交订单超时");
                                                }
                                            }
                                    );
                                    queue.add(postRequest);
                                }
                            });

                        } catch (JSONException e) {
                            Log.d(TAG, "获取商品时JSON错误");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "请求商品超时", Toast.LENGTH_SHORT).show();
                        byte[] htmlResponseBody = error.networkResponse.data;
                        Log.e(TAG, "请求商品超时");
                        Log.e(TAG, new String(htmlResponseBody));
                    }
                }
        );
        queue.add(goodRequest);



//        g = GoodsLab.getInstance().getGood(goodId);



    }

}



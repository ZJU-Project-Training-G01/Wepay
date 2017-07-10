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

    public static final String EXTRA_GOOD_ID =
            "cn.edu.zju.se_g01.nfc_pay.good_id";
    private Goods g;
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

        UUID goodUuid = (UUID) getIntent().getSerializableExtra(EXTRA_GOOD_ID);

        g = GoodsLab.get(this).getGoods(goodUuid);
        Log.d("GoodActivity", "create good activity");

        img = (ImageView) findViewById(R.id.detail_good_img);
        TextView goodName = (TextView) findViewById(R.id.detail_good_name);
        final TextView unitPrice = (TextView) findViewById(R.id.detail_unit_price);
        Spinner spinner = (Spinner) findViewById(R.id.buy_amount_spinner);
        Button nfcButton = (Button) findViewById(R.id.set_nfc_button);
        Button buyButton = (Button) findViewById(R.id.buy_button);


        img.setImageResource(R.drawable.ic_default_img);
//        imgDownloaderThread.queueImg(img, g.getImgUrl());

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
        unitPrice.setText(String.format("￥%.2f", g.getUnitPrice()));

        list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
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
                String url = "make_new_order.php";
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
                                    Toast.makeText(getApplicationContext(), response.getString("status"), Toast.LENGTH_SHORT).show();

                                    Log.d("Response", response.getString("status"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "请求超时", Toast.LENGTH_SHORT).show();

                            }
                        }
                );
                queue.add(postRequest);
            }
        });


    }

}



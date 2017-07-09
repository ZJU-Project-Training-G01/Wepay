package cn.edu.zju.se_g01.nfc_pay;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import cn.edu.zju.se_g01.nfc_pay.Good.Goods;
import cn.edu.zju.se_g01.nfc_pay.Good.GoodsLab;

/**
 * Created by Rexxar on 2017/7/8.
 */

public class GoodActivity extends Activity {

    public static final String EXTRA_GOOD_ID =
            "cn.edu.zju.se_g01.nfc_pay.good_id";
    private Goods g;
    int buy_amount;

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
        img.setImageResource(R.drawable.ic_default_img);
//        imgDownloaderThread.queueImg(img, g.getImgUrl());

        new Thread(){
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

        TextView goodName = (TextView) findViewById(R.id.detail_good_name);
        TextView unitPrice = (TextView) findViewById(R.id.detail_unit_price);

        goodName.setText(g.getGoodsName());
        unitPrice.setText(String.format("￥%.2f", g.getUnitPrice()));

        Spinner spinner = (Spinner) findViewById(R.id.buy_amount_spinner);
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>
                (this, android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setSelection(2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}



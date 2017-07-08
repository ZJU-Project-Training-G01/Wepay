package cn.edu.zju.se_g01.nfc_pay;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good);

        UUID goodUuid = (UUID) getIntent().getSerializableExtra(EXTRA_GOOD_ID);

        g = GoodsLab.get(this).getGoods(goodUuid);
        Log.d("GoodActivity", "create good activity");

        ImageView img = (ImageView) findViewById(R.id.detail_good_img);
        TextView goodName = (TextView) findViewById(R.id.detail_good_name);
        TextView unitPrice = (TextView) findViewById(R.id.detail_unit_price);

        goodName.setText(g.getGoodsName());
        unitPrice.setText(String.format("%.2f", g.getUnitPrice()));
    }

}



package cn.edu.zju.se_g01.nfc_pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class UserSettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        RelativeLayout deliveryAddressLayout = (RelativeLayout)findViewById(R.id.delivery_address_layout);
        deliveryAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserSettingActivity.this, EditUserInfoActivity.class);
                startActivity(i);
            }
        });
//        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}

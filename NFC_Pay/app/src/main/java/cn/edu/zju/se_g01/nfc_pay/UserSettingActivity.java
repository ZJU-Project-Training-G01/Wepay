package cn.edu.zju.se_g01.nfc_pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        Button logoutBtn = (Button)findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getApplicationContext().getSharedPreferences(getString(R.string.cookie_preference_file), Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = sp.edit();
                prefsEditor.clear();
                prefsEditor.commit();
            }
        });
//        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}

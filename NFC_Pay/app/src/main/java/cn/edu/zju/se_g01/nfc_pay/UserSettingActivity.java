package cn.edu.zju.se_g01.nfc_pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.zju.se_g01.nfc_pay.tools.CookieRequest;

public class UserSettingActivity extends Activity {

    private final static String mlogoutUrl = "http://localhost/LogOut.php";
    private final static String TAG = "UserSettingActivity";
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

                CookieRequest logOutReq = new CookieRequest(getApplicationContext(), Request.Method.GET, mlogoutUrl,
                        null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int code = response.getInt("code");
                            if(code == 1) {
                                Toast.makeText(UserSettingActivity.this, "登出失败", Toast.LENGTH_LONG);
                            } else if(code == 0) {
                                //跳转回登录界面
                                Intent i = new Intent(UserSettingActivity.this, LoginActivity.class);
                                startActivity(i);
                            } else {
                                Log.e(TAG, "服务器返回 json 数据格式有误!");
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
            }
        });
//        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}

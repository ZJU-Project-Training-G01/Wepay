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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.zju.se_g01.nfc_pay.config.Config;
import cn.edu.zju.se_g01.nfc_pay.tools.CookieRequest;
import cn.edu.zju.se_g01.nfc_pay.tools.EasyMethod;
import cn.edu.zju.se_g01.nfc_pay.tools.MySingleton;

public class UserSettingActivity extends Activity {

    private final static String mlogoutUrl = Config.getFullUrl("LogOut");
    private final static String TAG = "UserSettingActivity";

    private TextView userNameTextView;
    private TextView balanceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        RelativeLayout deliveryAddressLayout = (RelativeLayout)findViewById(R.id.delivery_address_layout);
        userNameTextView = (TextView)findViewById(R.id.userNameTextView);
        balanceTextView = (TextView)findViewById(R.id.balanceTextView);
        Button logoutBtn = (Button)findViewById(R.id.logoutBtn);
        RelativeLayout bindBankCardLayout = (RelativeLayout)findViewById(R.id.bindBankCardLayout);
        RelativeLayout chargeBalanceLayout = (RelativeLayout)findViewById(R.id.chargeBalanceLayout);

        //设置点击组件时候的监听器
        deliveryAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserSettingActivity.this, EditUserInfoActivity.class);
                startActivity(i);
            }
        });
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
                                Toast.makeText(UserSettingActivity.this, "登出失败", Toast.LENGTH_LONG).show();
                            } else if(code == 0) {
                                //跳转回登录界面
                                Toast.makeText(UserSettingActivity.this, "登出成功", Toast.LENGTH_LONG).show();
//                                EasyMethod.clearSharedPreferences(getApplicationContext());
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
                MySingleton.getInstance(getApplicationContext()).getRequestQueue().add(logOutReq);

            }
        });

        bindBankCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserSettingActivity.this, BindBankCardActivity.class));
            }
        });

        chargeBalanceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserSettingActivity.this, ChargeBalanceActivity.class));
            }
        });
        getUserInfo();


//        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getUserInfo() {
        //获取用户的个人信息
        CookieRequest cookieRequest = new CookieRequest(getApplicationContext(), Request.Method.GET,
                Config.getFullUrl("GetUserInfo"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "get user info response:" + response.toString());

                    int code = response.getInt("code");
                    if(code == 0) {
                        //修改界面上的用户昵称和余额
                        String userName = response.getJSONObject("data").getString("userName");
                        Double balance = response.getJSONObject("data").getDouble("balance");
                        userNameTextView.setText(userName);
                        balanceTextView.setText(String.valueOf(balance));
                    } else if (code == 1) {
                        Toast.makeText(UserSettingActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                    } else if (code == 2) { //未登录
                        Toast.makeText(UserSettingActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                        EasyMethod.clearSharedPreferences(getApplicationContext());
                        startActivity(new Intent(UserSettingActivity.this, LoginActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, null);
        MySingleton.getInstance(getApplicationContext()).getRequestQueue().add(cookieRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume() called");
        getUserInfo();
    }
}

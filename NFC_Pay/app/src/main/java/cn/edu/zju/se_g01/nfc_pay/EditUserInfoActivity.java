package cn.edu.zju.se_g01.nfc_pay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.se_g01.nfc_pay.tools.CookieRequest;
import cn.edu.zju.se_g01.nfc_pay.tools.MySingleton;

public class EditUserInfoActivity extends AppCompatActivity {

    private final static String getUserInfoUrl = "http://localhost/getUserInfo.php";
    private final static String editUserInfoUrl = "http://localhost/editUserInfo.php";
    private static final String TAG = "EditUserInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        final EditText userNameEditText = (EditText)findViewById(R.id.userNameEditText);
        final EditText realNameEditText = (EditText)findViewById(R.id.realNameEditText);
        final EditText phoneEditText = (EditText)findViewById(R.id.phoneEditText);
        final EditText addressEditText = (EditText)findViewById(R.id.addressEditText);
        Button userInfoCommitBtn = (Button)findViewById(R.id.userInfoCommitBtn);

        //发起一个网络请求，获取用户信息
        final CookieRequest userInfoReq = new CookieRequest(getApplicationContext(), Request.Method.GET, getUserInfoUrl,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    userNameEditText.setText(data.getString("userName"));
                    realNameEditText.setText(data.getString("realName"));
                    phoneEditText.setText(data.getString("phone"));
                    addressEditText.setText(data.getString("address"));
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
        MySingleton.getInstance(getApplicationContext()).getRequestQueue().add(userInfoReq);

        userInfoCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> postParams = new HashMap<String, String>();
                postParams.put("userName", userNameEditText.getText().toString());
                postParams.put("realName", realNameEditText.getText().toString());
                postParams.put("phone", phoneEditText.getText().toString());
                postParams.put("address", addressEditText.getText().toString());
                CookieRequest editUserInfoReq = new CookieRequest(getApplicationContext(), Request.Method.POST, editUserInfoUrl,
                        postParams, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int code = response.getInt("code");
                            if(code == 0) {
                                Toast.makeText(EditUserInfoActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                            } else if(code == 1) {
                                Toast.makeText(EditUserInfoActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
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
    }
}
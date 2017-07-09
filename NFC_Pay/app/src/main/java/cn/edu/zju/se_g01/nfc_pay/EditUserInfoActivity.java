package cn.edu.zju.se_g01.nfc_pay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.zju.se_g01.nfc_pay.tools.CookieRequest;

public class EditUserInfoActivity extends AppCompatActivity {

    private final static String sUrl = "http://localhost/getUserInfo.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        final EditText realNameEditText = (EditText)findViewById(R.id.realNameEditText);
        final EditText phoneEditText = (EditText)findViewById(R.id.phoneEditText);
        final EditText addressEditText = (EditText)findViewById(R.id.addressEditText);
        Button userInfoCommitBtn = (Button)findViewById(R.id.userInfoCommitBtn);

        CookieRequest userInfoReq = new CookieRequest(getApplicationContext(), Request.Method.GET, sUrl,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    realNameEditText.setText(data.getString("realName"));
                    phoneEditText.setText(data.getString(""));
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}

package cn.edu.zju.se_g01.nfc_pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.se_g01.nfc_pay.config.Config;
import cn.edu.zju.se_g01.nfc_pay.tools.CookieRequest;
import cn.edu.zju.se_g01.nfc_pay.tools.EasyMethod;
import cn.edu.zju.se_g01.nfc_pay.tools.MySingleton;

public class BindBankCardActivity extends AppCompatActivity {

    private EditText cardOwnerEditText;
    private EditText IDnumEditText;
    private EditText cardNumEditText;
    private EditText cardPasswordEditText;
    private Button bindCardCommitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_bank_card);

        cardOwnerEditText = (EditText)findViewById(R.id.cardOwnerEditText);
        IDnumEditText = (EditText)findViewById(R.id.IDnumberEditText);
        cardNumEditText = (EditText)findViewById(R.id.bankCardNumEditText);
        cardPasswordEditText = (EditText)findViewById(R.id.bankCardPasswordEditText);
        bindCardCommitBtn = (Button)findViewById(R.id.bindBankCardCommitBtn);

        bindCardCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> paramsMap = new HashMap<String, String>();
                paramsMap.put("cardOwner", cardOwnerEditText.getText().toString());
                paramsMap.put("IDnum", IDnumEditText.getText().toString());
                paramsMap.put("cardNum", cardNumEditText.getText().toString());
                paramsMap.put("cardPasswd", cardPasswordEditText.getText().toString());
                CookieRequest bindCardReq = new CookieRequest(getApplicationContext(), Request.Method.POST,
                        Config.getFullUrl("BindBankCard"), paramsMap, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int code = response.getInt("code");
                            if(code == 0) {
                                Toast.makeText(BindBankCardActivity.this, "绑定成功", Toast.LENGTH_LONG).show();
                                Thread.sleep(1000);
                                finish();
                            } else if(code == 1) {

                                Toast.makeText(BindBankCardActivity.this, "绑定失败", Toast.LENGTH_LONG).show();
                            } else if(code == 2) {
                                Toast.makeText(BindBankCardActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                                EasyMethod.clearSharedPreferences(getApplicationContext());
                                startActivity(new Intent(BindBankCardActivity.this, LoginActivity.class));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                }, null);
                MySingleton.getInstance(getApplicationContext()).getRequestQueue().add(bindCardReq);
            }
        });
    }
}

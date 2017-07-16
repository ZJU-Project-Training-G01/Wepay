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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.se_g01.nfc_pay.config.Config;
import cn.edu.zju.se_g01.nfc_pay.tools.CookieRequest;
import cn.edu.zju.se_g01.nfc_pay.tools.MySingleton;

public class ChargeBalanceActivity extends AppCompatActivity {

    private static final String TAG = "ChargeBalanceActivity";
    private EditText chargeNumEditText;
    private EditText paymentPasswordEditText;
    private Button confirmChargeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_balance);
        chargeNumEditText = (EditText)findViewById(R.id.chargeNumEditText);
        paymentPasswordEditText = (EditText)findViewById(R.id.paymentPasswordEditText);
        confirmChargeBtn = (Button)findViewById(R.id.confirmChargeBtn);
        confirmChargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chargeNum = chargeNumEditText.getText().toString();
                String paymentPassword = paymentPasswordEditText.getText().toString();
                Map<String, String> paramsMap = new HashMap<String, String>();
                paramsMap.put("chargeNum", chargeNum);
                paramsMap.put("cardPassword", paymentPassword);
                CookieRequest chargeBalanceReq = new CookieRequest(getApplicationContext(), Request.Method.POST,
                        Config.getFullUrl("ChargeBalance"), paramsMap, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, response.toString());
                            int code = response.getInt("code");
                            if(code == 0) {
                                Toast.makeText(ChargeBalanceActivity.this, "充值成功", Toast.LENGTH_LONG).show();
                                Thread.sleep(1000);
                                finish();
                            } else {
                                Toast.makeText(ChargeBalanceActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                }, null);
                MySingleton.getInstance(getApplicationContext()).getRequestQueue().add(chargeBalanceReq);
            }
        });
    }
}

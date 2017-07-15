package cn.edu.zju.se_g01.nfc_pay.tools;

import android.content.Context;
import android.content.SharedPreferences;

import cn.edu.zju.se_g01.nfc_pay.R;

/**
 * Created by dddong on 2017/7/15.
 */

public class EasyMethod {

    public static void clearSharedPreferences(Context c) {
        SharedPreferences sp = c.getSharedPreferences(c.getString(R.string.cookie_preference_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sp.edit();
        prefsEditor.clear();
        prefsEditor.commit();
    }
}

package cn.edu.zju.se_g01.nfc_pay;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.zju.se_g01.nfc_pay.tools.CookieRequest;
import cn.edu.zju.se_g01.nfc_pay.tools.HttpConnector;
import cn.edu.zju.se_g01.nfc_pay.tools.MySingleton;

import static android.Manifest.permission.READ_CONTACTS;
import static cn.edu.zju.se_g01.nfc_pay.config.Config.getFullUrl;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

    private final static String TAG = LoginActivity.class.getSimpleName();
    private final String mLoginUrl = getFullUrl("UserLogin");
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
//    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mEmailSignInButton;
    private Button mSignUpButton;

    private View focusView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.user_email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.user_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mSignUpButton = (Button)findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        //尝试使用 cookie 自动登录
        SharedPreferences sp = getSharedPreferences(getString(R.string.cookie_preference_file), MODE_PRIVATE);
        String localCookieStr = sp.getString("cookie", "");
        if(! localCookieStr.isEmpty()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
//        VolleyLog.DEBUG = true;
    }

    private void attemptSignUp() {
        // Show a progress spinner, and kick off a background task to
        // perform the user sign up attempt.
        showProgress(true);

        Map<String, String> postParams = getPostParams();
        String signUpUrl = getFullUrl("UserRegist");
        CookieRequest signUpReq = new CookieRequest(LoginActivity.this, Request.Method.POST, signUpUrl,
                postParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, response.toString());
                int code = 0;
                try {
                    code = response.getInt("code");
                    if(code == 0) {
                        //注册成功
                        Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else if(code == 1) {
                        Toast.makeText(LoginActivity.this, "注册失败:" + response.getString("msg"), Toast.LENGTH_LONG).show();
                        showProgress(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.e(error.getMessage());
                showProgress(false);
                Toast.makeText(LoginActivity.this, "网络出现问题", Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(getApplicationContext()).getRequestQueue().add(signUpReq);
    }

    private Map<String, String> getPostParams() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("email", email);
        paramsMap.put("password", password);
        return paramsMap;
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    boolean checkForm() {

        //Reset errors
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        return cancel;
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        boolean cancel = checkForm();
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            Map<String, String> paramsMap = getPostParams();

            //发起登录或注册请求
            CookieRequest loginOrRegistReq = new CookieRequest(getApplicationContext(), Request.Method.POST,
                    mLoginUrl, paramsMap, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "response json:" + response.toString());
                    try {
                        showProgress(false);
                        int success = response.getInt("code");

                        if (success == 0) { //登录成功
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                        } else if (success == 1){   //登录失败，用户名或者密码不正确
                            mPasswordView.setError(getString(R.string.error_incorrect_username_or_password));
                            mPasswordView.requestFocus();
                        } else {
                            Toast.makeText(LoginActivity.this, "服务器端返回的数据格式出了点问题", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "json key not found err");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    VolleyLog.e("LoginError: ", error.networkResponse.data);
                    showProgress(false);
                    Toast.makeText(LoginActivity.this, "网络出现问题", Toast.LENGTH_LONG).show();
                }
            });
            MySingleton.getInstance(getApplicationContext()).getRequestQueue().add(loginOrRegistReq);
//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

        private final static String TAG = "UserLoginTask";
        private final String mEmail;
        private final String mPassword;
        private final String mUrl = "http://localhost/userlogin.php";

        private final int JSON_KEY_NOT_FOUND_ERR = -1;
        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected Integer doInBackground(Void... params) {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("email", mEmail);
            paramsMap.put("password", mPassword);
            JSONObject json_response = HttpConnector.getJSONByHttpPost(mUrl, paramsMap);


            //TODO: 这里等待后端写好之后的测试
//            JSONObject json_response = null;
//            try {
//                json_response = new JSONObject("{result:0}");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            try {
                int result = json_response.getInt("result");
                return result;
            } catch (JSONException e) {
                Log.e(TAG, "json key not found err");
                return JSON_KEY_NOT_FOUND_ERR;
            }

//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }
//
//            return true;
        }

        /*
        该方法是在主线程中运行的
         */
        @Override
        protected void onPostExecute(final Integer success) {
//            mAuthTask = null;
            showProgress(false);

            if (success == 0) { //登录成功
//                finish();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            } else if (success == 1){   //登录失败，密码不正确
                mPasswordView.setError(getString(R.string.error_incorrect_username_or_password));
                mPasswordView.requestFocus();
            } else if (success == 2) {  //成功注册了一个新的账号并登录
                Toast.makeText(LoginActivity.this, "注册新账号成功", Toast.LENGTH_LONG).show();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(LoginActivity.this, "服务器端返回的数据格式出了点问题", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
//            mAuthTask = null;
            showProgress(false);
        }
    }
}


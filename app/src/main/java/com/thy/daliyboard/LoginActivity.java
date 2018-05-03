package com.thy.daliyboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    private Button CustomloginButton;
    LoginButton loginButton;

//    TextView tvEmail, tvId, tvName;
//    ImageView iv;
    String userName = "";

    ProgressDialog dialog;

    boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
//        loginButton = findViewById(R.id.login_button);
//        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
//
//        initControls();
//        loginWithFb();
        callbackManager = CallbackManager.Factory.create();  //로그인 응답을 처리할 콜백 관리자

        CustomloginButton = (Button)findViewById(R.id.loginBtn);
        CustomloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LoginManager - 요청된 읽기 또는 게시 권한으로 로그인 절차를 시작합니다.
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                        Arrays.asList("public_profile", "user_friends"));
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                Log.e("onSuccess", "onSuccess");
                                dialog = new ProgressDialog(LoginActivity.this);
                                dialog.setMessage("Loading...");
                                dialog.show();

                                String accesstoken = loginResult.getAccessToken().getToken();

                                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        dialog.dismiss();
                                        Log.d("respose", response.toString());
                                        getData(object);
                                    }
                                });

                                //Request Graph API
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id, email, name");
                                request.setParameters(parameters);
                                request.executeAsync();

                                sendInfo();
                            }

                            @Override
                            public void onCancel() {
                                Log.e("onCancel", "onCancel");
                                setResult(RESULT_CANCELED);
                                finish();
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                Log.e("onError", "onError " + exception.getLocalizedMessage());
                            }
                        });
            }
        });

    }

//    private void initControls(){
//        callbackManager = CallbackManager.Factory.create();
//
////        tvId = findViewById(R.id.tv_id);
////        tvEmail = findViewById(R.id.tv_email);
////        tvName = findViewById(R.id.tv_name);
////
////        iv = findViewById(R.id.iv);
//    }

//    private void loginWithFb(){
//
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                dialog = new ProgressDialog(LoginActivity.this);
//                dialog.setMessage("Loading...");
//                dialog.show();
//
//                String accesstoken = loginResult.getAccessToken().getToken();
//
//                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        dialog.dismiss();
//                        Log.d("respose", response.toString());
//                        getData(object);
//                    }
//                });
//
//                //Request Graph API
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id, email, name");
//                request.setParameters(parameters);
//                request.executeAsync();
//
//                sendInfo();
//            }
//
//            @Override
//            public void onCancel() {
//                setResult(RESULT_CANCELED);
//                finish();
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });
//
//    }

    private void getData(JSONObject object) {
        try {
            URL profile_picture = new URL("https://graph.facebook.com"+object.getString("id")+"/picture?width=250&height=250");

//            Picasso.with(this).load(profile_picture.toString()).into(iv);

//            tvId.setText(object.getString("id"));
//            tvEmail.setText(object.getString("email"));
//            tvName.setText(object.getString("name"));

            userName = object.getString("id");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendInfo(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

//        intent.putExtra("email", tvEmail.getText());
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

package com.thy.daliyboard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
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

    String userId ="n";
    String userName = "n";
    String userEmail ="n";

    ProgressDialog dialog;

    boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        initControls();
        loginWithFb();
//        Facebook Custom
//        callbackManager = CallbackManager.Factory.create();  //로그인 응답을 처리할 콜백 관리자
//
//        CustomloginButton = (Button)findViewById(R.id.loginBtn);
//        CustomloginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //LoginManager - 요청된 읽기 또는 게시 권한으로 로그인 절차를 시작합니다.
//                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
//                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        Log.e("onSuccess", "onSuccess");
//                        Log.e("토큰",loginResult.getAccessToken().getToken());
//                        Log.e("유저아이디",loginResult.getAccessToken().getUserId());
//                        Log.e("퍼미션 리스트",loginResult.getAccessToken().getPermissions()+"");
//
//                        //loginResult.getAccessToken() 정보를 가지고 유저 정보를 가져올수 있습니다.
//                        GraphRequest request =GraphRequest.newMeRequest(loginResult.getAccessToken() ,
//                                new GraphRequest.GraphJSONObjectCallback() {
//                                    @Override
//                                    public void onCompleted(JSONObject object, GraphResponse response) {
//                                        try {
//                                            Log.e("user profile",object.toString());
//                                            getData(object);
//                                            saveData();
//                                            sendLoginData();
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                });
//
//                        //Request Graph API
//                        Bundle parameters = new Bundle();
//                        parameters.putString("fields", "id, email, name");
//                        request.setParameters(parameters);
//                        request.executeAsync();
//
//                        sendInfo();
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        Log.e("onCancel", "onCancel");
//                        setResult(RESULT_CANCELED);
//                        finish();
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        Log.e("onError", "onError " + exception.getLocalizedMessage());
//                    }
//                });
//            }
//        });

    }

    public void clickGuest(View v){
        sendInfo();
    }

    private void initControls(){
        callbackManager = CallbackManager.Factory.create();

//        tvId = findViewById(R.id.tv_id);
//        tvEmail = findViewById(R.id.tv_email);
//        tvName = findViewById(R.id.tv_name);
//
//        iv = findViewById(R.id.iv);
    }

    private void loginWithFb(){

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                dialog = new ProgressDialog(LoginActivity.this);
                dialog.setMessage("Loading...");
                dialog.show();

                //프로필
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Profile profile = Profile.getCurrentProfile();
//                        String profilePic = profile.getProfilePictureUri(200,200).toString();
//                    }
//                });

                String accesstoken = loginResult.getAccessToken().getToken();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        dialog.dismiss();
                        Log.d("respose", response.toString());
                        getData(object);
                        saveData();
                        sendLoginData();
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
                setResult(RESULT_CANCELED);
                finish();
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void getData(JSONObject object) {
        try {
            Log.i("fffff", userId);
            userId = object.getString("id");
            userName = object.getString("name");
            userEmail = object.getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveData(){
        Log.i("fffff", userId);
        Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();
        SharedPreferences pref = getSharedPreferences("facebookLoginData", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("Id", userId);
        editor.putString("Name", userName);
        editor.putString("Email", userEmail);

        editor.commit();    //editor에게 완료됨을 알림.
    }

    public void sendInfo(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void sendLoginData(){
        String serverUrl = "http://thyun85.dothome.co.kr/dailyboard/insertloginDB.php";

        //insertDB.php에 보낼 파일전송요청 객체 생성
        SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //insertDB.php의 echo 결과 보여주기
                new AlertDialog.Builder(LoginActivity.this).setMessage(response).setPositiveButton("ok", null).create().show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //요청객체에 데이터 추가하기
        multiPartRequest.addStringParam("id", userId);
        multiPartRequest.addStringParam("name", userName);
        multiPartRequest.addStringParam("email", userEmail);

        //요청큐 객체 생성하기
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //요청큐에 요청객체 추가
        requestQueue.add(multiPartRequest);
    }
}

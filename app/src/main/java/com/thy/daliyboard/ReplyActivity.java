package com.thy.daliyboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class ReplyActivity extends AppCompatActivity {

    ArrayList<ReplyItem> replyItems = new ArrayList<>();

    TextView userName, userMsg, userDate;
    RecyclerView recyclerViewReply;
    Adapter_Reply adapterReply;
    EditText reply;
    SwipeRefreshLayout refreshLayout;

    String itemNo, itemMsg, itemDate, itemType;
    String name, email, msg;
    String[] datas;
    String upload_replyUrl, load_replyUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        recyclerViewReply = findViewById(R.id.recycler_reply);
        adapterReply = new Adapter_Reply(this, replyItems);
        recyclerViewReply.setAdapter(adapterReply);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewReply.setLayoutManager(layoutManager);

        userName = findViewById(R.id.tv_itemUserName);
        userMsg = findViewById(R.id.tv_itemMsg);
        userDate = findViewById(R.id.tv_itemDate);
        reply = findViewById(R.id.edit_reply);
        refreshLayout = findViewById(R.id.layout_swipe_reply);

        Intent intent = getIntent();

        itemNo = intent.getStringExtra("itemNo");
        Log.i("test-222", itemNo);
        itemMsg = intent.getStringExtra("itemMsg");
        itemDate = intent.getStringExtra("itemDate");
        itemType = intent.getStringExtra("type");

        userMsg.setText(itemMsg);

        SharedPreferences pref = getSharedPreferences("facebookLoginData", MODE_PRIVATE);
        name = pref.getString("Name", "no name");
        email = pref.getString("Email", "no email");

        userName.setText(name);
        userDate.setText(itemDate);

        loadDB();
    }

    public void clickReply(View v){
        uploadDB(itemNo);
    }

    public void loadDB(){
        if(itemType.equals("tips")){
            load_replyUrl = "http://thyun85.dothome.co.kr/dailyboard/loadReplyTipsDB.php";
        }else if(itemType.equals("review")){
            load_replyUrl = "http://thyun85.dothome.co.kr/dailyboard/loadReplyReviewDB.php";
        }else if(itemType.equals("spot")){
            load_replyUrl = "http://thyun85.dothome.co.kr/dailyboard/loadReplySpotDB.php";
        }

        //insertDB.php에 보낼 파일전송요청 객체 생성
        SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, load_replyUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("aaa", "a1");
                //insertDB.php의 echo 결과 보여주기
                new AlertDialog.Builder(ReplyActivity.this).setMessage(response).setPositiveButton("ok", null).create().show();

                if(response.equals("")){
                    new android.app.AlertDialog.Builder(ReplyActivity.this).setMessage("내용없음").setPositiveButton("ok", null).create().show();
                }else{
                    //읽어온 데이터 문자열에서 db의 row(레코드)별로 배열로 분리하기
                    String[] rows = response.split(";");

                    replyItems.clear();
                    Log.i("replys", "a2 : " + rows.length);
                    Log.i("replys", "response : " + response);

                    for(String row : rows) {
                        datas = row.split("&");
                        Log.i("datas.length", datas.length + "");
                        //배열 내용 확인
                        for (int i = 0; i < datas.length; i++)
                            Log.i("replys", "datas[" + i + "]" + " : " + datas[i]);

                        int no = Integer.parseInt(datas[0]);
                        String name = datas[1];
                        String msg = datas[2];
                        String date = datas[3];

                        replyItems.add(0, new ReplyItem(no, name, msg, date));

                        adapterReply.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                        Log.i("aaa", "a3");
                        Log.i("aaa1", email);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        //요청객체에 데이터 추가하기
//        multiPartRequest.addStringParam("status", status);
        multiPartRequest.addStringParam("itemNo", itemNo);
//        multiPartRequest.addStringParam("email", email);

        //요청큐 객체 생성하기
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //요청큐에 요청객체 추가
        requestQueue.add(multiPartRequest);
    }

    public void uploadDB(String itemNo){
        if(itemType.equals("tips")){
            upload_replyUrl = "http://thyun85.dothome.co.kr/dailyboard/insertReplyTipsDB.php";
        }else if(itemType.equals("review")){
            upload_replyUrl = "http://thyun85.dothome.co.kr/dailyboard/insertReplyReviewDB.php";
        }else if(itemType.equals("spot")){
            upload_replyUrl = "http://thyun85.dothome.co.kr/dailyboard/insertReplySpotDB.php";
        }

        msg = reply.getText().toString();

        if(msg != null){
            //insertpostDB.php에 보낼 파일전송요청 객체 생성
            SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, upload_replyUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //insertpostDB.php의 echo 결과 보여주기
                    new AlertDialog.Builder(ReplyActivity.this).setMessage(response).setPositiveButton("OK", null).create().show();
                    adapterReply.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                Toast.makeText(PostSpotActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            //요청객체에 데이터 추가하기
            multiPartRequest.addStringParam("no", itemNo);
            multiPartRequest.addStringParam("name", name);
            multiPartRequest.addStringParam("email", email);
            multiPartRequest.addStringParam("msg", msg);
            multiPartRequest.addStringParam("itemMsg", itemMsg);

            Log.i("email", email+"");

            //요청큐 객체 생성하기
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            //요청큐에 요청객체 추가..
            requestQueue.add(multiPartRequest);

            reply.setText("");
            loadDB();
        }else{
            new AlertDialog.Builder(ReplyActivity.this).setMessage("메세지를 작성해 주세요.").setPositiveButton("OK", null).create().show();
        }
    }
}

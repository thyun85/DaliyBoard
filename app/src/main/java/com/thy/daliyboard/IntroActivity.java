package com.thy.daliyboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {

    ImageView logo;

    //스케쥴관리 객체
    Timer timer = new Timer();

    LoadDBTask taskDB;

    String dbId;
    boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        logo = findViewById(R.id.logo);

        Animation ani = AnimationUtils.loadAnimation(this, R.anim.appear_logo);
        logo.startAnimation(ani);

        //스케쥴관리 객체에게 스케쥴 등록
        timer.schedule(task, 3000);

    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            //스케쥴링에 의해 3초 후에 이 메소드 실행
            loadDB();
        }
    };

    private  void loadDB(){
        String serverUrl = "http://thyun85.dothome.co.kr/dailyboard/loadLoginDB.php";

        try {
            URL url = new URL(serverUrl);
            taskDB = new LoadDBTask();
            taskDB.execute(url);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    class LoadDBTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            Log.i("aaa", "a1");
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setUseCaches(false);
                Log.i("aaa", "a2");
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                BufferedReader reader = new BufferedReader(isr);

                String line = reader.readLine();
                Log.i("aaa", "a3");
                Log.i("aaa", "line : " + line);
                //읽어온 데이터 문자열에서 db의 row(레코드)별로 배열로 분리하기
                String[] rows = line.split(";");
                Log.i(" aaa", "line : " + rows[0]);
                Log.i("aaa", "a4 : " + rows.length);
                dbId = rows[0];
                compareDB();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "읽기 완료";
        }
    }

    void compareDB(){
        SharedPreferences pref = getSharedPreferences("facebookLoginData", MODE_PRIVATE);
        String id = pref.getString("Id", "no Id");

        if(dbId.equals(id)){
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

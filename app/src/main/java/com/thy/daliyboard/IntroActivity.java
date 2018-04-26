package com.thy.daliyboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {

    ImageView logo;

    //스케쥴관리 객체
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        logo = findViewById(R.id.logo);

        Animation ani = AnimationUtils.loadAnimation(this, R.anim.appear_logo);
        logo.startAnimation(ani);

        //스케쥴관리 객체에게 스케쥴 등록
        timer.schedule(task, 4000);

    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            //스케쥴링에 의해 4초 후에 이 메소드 실행
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
}

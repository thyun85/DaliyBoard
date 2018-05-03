package com.thy.daliyboard;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by alofo on 2018-03-21.
 */

public class Fragment_Weather extends Fragment {

    String alarmApiKey = "6245706a4b74687936317944456670";

    String dustApiKey = "63744d466e74687935365a59414b6b";

    ArrayList<DustInfo> dustInfos = new ArrayList<>();

    SwipeRefreshLayout refreshLayout;
    URL url, urlAlarm;
    XMLParserTask task;
    XMLParserTaskAlarm taskAlarm;

    TextView tvName, tvDate, tvPm10, tvPm25, tvGrade, dustAlarm;
    ImageView ivSearch, ivGrade;
    String[] listItem;

    DustInfo dustInfo = null;

    String basic=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.weather, container, false);

        tvName = view.findViewById(R.id.tv_name);
        tvDate = view.findViewById(R.id.tv_date);
        tvPm10 = view.findViewById(R.id.tv_pm10info);
        tvPm25 = view.findViewById(R.id.tv_pm25info);
        tvGrade = view.findViewById(R.id.tv_grade);

        XMLParsing();

        refreshLayout = view.findViewById(R.id.layout_swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //갱신작업 수행.
                dustInfos.clear();
                XMLParsing();
            }
        });

        ivSearch = view.findViewById(R.id.iv_search);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSearch();
            }
        });

        ivGrade = view.findViewById(R.id.iv_grade);

        return view;
    }

    public void clickSearch(){

        listItem = new String[]{"강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구",
                                "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구",
                                "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구",
                                "종로구", "중구", "중랑구"};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("지역을 선택하세요");

        alertDialog.setSingleChoiceItems(listItem, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                tvName.setText(listItem[i]);
                dialogInterface.dismiss();

                search(listItem[i]);

//                basic = listItem[i];
//                Toast.makeText(getActivity(), basic, Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog.create().show();

//        XMLParsing();
    }

    void search(String city){
        for(int i=0; i<dustInfos.size(); i++){
            if(dustInfos.get(i).getMsrstename().equals(city)){
                tvName.setText(dustInfos.get(i).getMsrstename());
                tvDate.setText(dustInfos.get(i).getMsrdate());
                tvPm10.setText(dustInfos.get(i).getPm10());
                tvPm25.setText(dustInfos.get(i).getPm25());
                tvGrade.setText(dustInfos.get(i).getGrade());
                if(dustInfos.get(i).getGrade().equals("보통")) ivGrade.setImageResource(R.drawable.smile);
                if(dustInfos.get(i).getGrade().equals("나쁨")) ivGrade.setImageResource(R.drawable.sad);
                if(dustInfos.get(i).getGrade().equals("점검중")) ivGrade.setImageResource(R.drawable.setting);
            }
        }
    }

    void XMLParsing(){
        try {

            url = new URL("http://openapi.seoul.go.kr:8088/"+dustApiKey+"/xml/ListAirQualityByDistrictService/1/100/");
            //별도 스레드객체 생성
            //스레드의 백그라운드 작업과 UI작업을 같이 할 수 있는 객체를 생성
            task = new XMLParserTask();
            //doInBackground()메소드 실행시키는 명령(메소드)

            task.execute(url); //Thread의 start()와 같은 역할

        } catch (MalformedURLException e) {
            e.printStackTrace();
//            Log.i("qqq", e.getMessage());
        }
    }

    class XMLParserTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {

            URL url = urls[0];
            Log.i("eee", url.toString());
            try {
                InputStream is = url.openStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(is, "utf-8");

                int eventType = xpp.next();

                String tagName = null;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    eventType = xpp.next();

                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;

                        case XmlPullParser.START_TAG:
                            tagName = xpp.getName();

                            if (tagName.equals("row")) {
                                dustInfo = new DustInfo();
                            } else if (tagName.equals("MSRDATE")) {
                                xpp.next();
                                if (dustInfo != null) dustInfo.setMsrdate(xpp.getText());

                            } else if (tagName.equals("MSRSTENAME")) {
                                xpp.next();
                                if (dustInfo != null) dustInfo.setMsrstename(xpp.getText());

                            } else if (tagName.equals("GRADE")) {
                                xpp.next();
                                if (dustInfo != null) dustInfo.setGrade(xpp.getText());

                            } else if (tagName.equals("PM10")) {
                                xpp.next();
                                if (dustInfo != null) dustInfo.setPm10(xpp.getText());

                            } else if (tagName.equals("PM25")) {
                                xpp.next();
                                if (dustInfo != null) dustInfo.setPm25(xpp.getText());
                                Log.i("Aa", "----");
                            }
                            break;

                        case XmlPullParser.TEXT:
                            break;

                        case XmlPullParser.END_TAG:
                            tagName = xpp.getName();

                            if (tagName.equals("row")) {
                                if(dustInfo.getMsrstename() !=null){
                                    dustInfos.add(dustInfo);
                                }
//                                dustInfos.add(dustInfo);

//                                Logcat에 기록 남기기...(디버깅 작업시 용이)
                                Log.i("AAAA", "success");

                                publishProgress();
                            }
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                    }

                }
//                Log.i("ffffffff", "dsdd");

            } catch (IOException e) {
                Log.i("asdAA", e.getMessage());
            } catch (XmlPullParserException e) {
                Log.i("asdAfffA", e.getMessage());
            }
            return "읽기완료";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.i("ssss", "success");
            tvName.setText(dustInfo.getMsrstename().toString());
            tvDate.setText(dustInfo.getMsrdate().toString());
            tvPm10.setText(dustInfo.getPm10().toString());
            tvPm25.setText(dustInfo.getPm25().toString());
            tvGrade.setText(dustInfo.getGrade().toString());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //swipeRefresh 로딩아이콘 지우기
            refreshLayout.setRefreshing(false);
        }
    }

    void XMLParsingAlarm(){
        try {

            urlAlarm = new URL("http://openAPI.seoul.go.kr:8088/"+alarmApiKey+"/xml/ForecastWarningMinuteParticleOfDustService/1/1/");
            //별도 스레드객체 생성
            //스레드의 백그라운드 작업과 UI작업을 같이 할 수 있는 객체를 생성
            taskAlarm = new XMLParserTaskAlarm();
            //doInBackground()메소드 실행시키는 명령(메소드)

            taskAlarm.execute(urlAlarm); //Thread의 start()와 같은 역할

        } catch (MalformedURLException e) {
            e.printStackTrace();
//            Log.i("qqq", e.getMessage());
        }
    }

    class XMLParserTaskAlarm extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {

            URL url = urls[0];
            Log.i("eee", url.toString());
            try {
                InputStream is = url.openStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(is, "utf-8");

                int eventType = xpp.next();

                String tagName = null;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    eventType = xpp.next();

                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;

                        case XmlPullParser.START_TAG:
                            tagName = xpp.getName();

                            if (tagName.equals("row")) {
                                dustInfo = new DustInfo();
                            } else if (tagName.equals("ALARM_CNDT")) {
                                xpp.next();
                                if (dustInfo != null) dustInfo.setAlarm(xpp.getText());

                            }
                            break;

                        case XmlPullParser.TEXT:
                            break;

                        case XmlPullParser.END_TAG:
                            tagName = xpp.getName();

                            if (tagName.equals("row")) {
                                dustInfos.add(dustInfo);

//                                Logcat에 기록 남기기...(디버깅 작업시 용이)
                                Log.i("AAAA", "success");

                                publishProgress();

                            }
                            break;

                        case XmlPullParser.END_DOCUMENT:
                            break;

                    }
                }

//                Log.i("ffffffff", "dsdd");

            } catch (IOException e) {
                Log.i("asdAA", e.getMessage());
            } catch (XmlPullParserException e) {
                Log.i("asdAfffA", e.getMessage());
            }
            return "읽기완료";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
//            dustFa.setText(dustInfo.getFaOn().toString().equals("f") ? "예보" : dustInfo.getFaOn().toString().equals("a") ? "경보" : "-");
//            dustPollutant.setText(dustInfo.getPollutant().toString());
//            dustCaistep.setText(dustInfo.getCaistep().toString());
//            dustAlarm.setText(dustInfo.getAlarm().toString());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //swipeRefresh 로딩아이콘 지우기
            refreshLayout.setRefreshing(false);
        }
    }

}

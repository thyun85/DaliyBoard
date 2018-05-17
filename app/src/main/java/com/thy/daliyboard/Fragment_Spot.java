package com.thy.daliyboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.melnykov.fab.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by alofo on 2018-03-21.
 */

public class Fragment_Spot extends Fragment {

    ArrayList<SpotItem> spotItems = new ArrayList<>();

    RecyclerView recyclerViewSpot;
    Adapter_Spot adapterSpot;

    SwipeRefreshLayout refreshLayout;

    FloatingActionButton actionButton;

    LoadDBTask task;

    int no;
    String name;
    String msg;
    String aviPath;
    String date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.spot, container, false);

        recyclerViewSpot = view.findViewById(R.id.recycler_spot);
        adapterSpot = new Adapter_Spot(getActivity(), spotItems);
        recyclerViewSpot.setAdapter(adapterSpot);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewSpot.setLayoutManager(layoutManager);

        refreshLayout = view.findViewById(R.id.layout_swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //갱신작업 수행.
                spotItems.clear();
//                loadDB();

            }
        });

        actionButton = view.findViewById(R.id.fab_spot);
        actionButton.attachToRecyclerView(recyclerViewSpot);
        actionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostSpotActivity.class);
                startActivity(intent);
            }

        });

        adapterSpot.notifyDataSetChanged();

        return view;
    }

    private  void loadDB(){
        String serverUrl = "http://thyun85.dothome.co.kr/dailyboard/loadSpotDB.php";

        try {
            URL url = new URL(serverUrl);
            task = new LoadDBTask();
            task.execute(url);

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

                spotItems.clear();
                Log.i("aaa", "a4 : " + rows.length);
                for(String row : rows){
                    String[] datas = row.split("&");

                    for (int i = 0; i < datas.length; i++) Log.i("aaa2", i + " : " + datas[i]);
                    if(datas.length != 5) continue;
                    Log.i("aaa", "a5");
                    no = Integer.parseInt(datas[0]);
                    name = datas[1];
                    msg = datas[2];
                    aviPath = "http://thyun85.dothome.co.kr/dailyboard/"+datas[3];
                    date = datas[4];

                    spotItems.add(0, new SpotItem(no, name, msg, aviPath, date));

                    publishProgress();
                    Log.i("aaa", "a6");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "읽기 완료";
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            //리사이클러뷰의 갱신요청..
            adapterSpot.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //swipeRefresh 로딩아이콘 지우기
            refreshLayout.setRefreshing(false);
        }
    }
}

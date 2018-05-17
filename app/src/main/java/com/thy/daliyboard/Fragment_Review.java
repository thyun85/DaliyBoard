package com.thy.daliyboard;

import android.app.AlertDialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.ToggleButton;

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

public class Fragment_Review extends Fragment {

    TabHost tabHost;

    ArrayList<ReviewLbItem> reviewLbItems = new ArrayList<>();
    ArrayList<ReviewSk8Item> reviewSk8Items = new ArrayList<>();
    ArrayList<ReviewEtcItem> reviewEtcItems = new ArrayList<>();

    RecyclerView recyclerViewReview;
    Adapter_ReviewLb adapterReviewLb;
    Adapter_ReviewSk8 adapterReviewSk8;
    Adapter_ReviewEtc adapterReviewEtc;
    RecyclerView.LayoutManager layoutManager;

    SwipeRefreshLayout refreshLayout;

    FloatingActionButton actionButton;

    LoadDBTask task;

    int no;
    String name, msg, imgPath, date;
    String tag = "tab1";
    String serverUrl;

    ToggleButton tbFavorite;
    boolean favorite;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.review, container, false);

        tabHost = view.findViewById(android.R.id.tabhost);

        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("LB").setContent(R.id.tablb));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("SK8").setContent(R.id.tabsk8));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("ETC").setContent(R.id.tabetc));

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                tag = s;
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();

                if(tag.equals("tab1")){
                    recyclerViewReview = view.findViewById(R.id.recycler_lb);
                    adapterReviewLb = new Adapter_ReviewLb(getActivity(), reviewLbItems);
                    recyclerViewReview.setAdapter(adapterReviewLb);

//                    serverUrl = "http://thyun85.dothome.co.kr/dailyboard/loadLBReviewDB.php";

                    layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerViewReview.setLayoutManager(layoutManager);

                    loadDB();

                    actionButton = view.findViewById(R.id.fab_lb);
                    actionButton.setOnClickListener(clickLB);

                    refreshLayout = view.findViewById(R.id.layout_swipe);
                    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            //갱신작업 수행.
                            reviewLbItems.clear();
                            loadDB();

                        }
                    });
                    adapterReviewLb.notifyDataSetChanged();

                }else if(tag.equals("tab2")){
                    recyclerViewReview = view.findViewById(R.id.recycler_sk8);
                    adapterReviewSk8 = new Adapter_ReviewSk8(getActivity(), reviewSk8Items);
                    recyclerViewReview.setAdapter(adapterReviewSk8);

//                    serverUrl = "http://thyun85.dothome.co.kr/dailyboard/insertSK8ReviewDB.php";

                    layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerViewReview.setLayoutManager(layoutManager);

                    loadDB();

                    actionButton = view.findViewById(R.id.fab_sk8);
                    actionButton.setOnClickListener(clickSk8);

                    refreshLayout = view.findViewById(R.id.layout_swipe);
                    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            //갱신작업 수행.
                            reviewLbItems.clear();
                            loadDB();

                        }
                    });
                    adapterReviewLb.notifyDataSetChanged();

                }else if(tag.equals("tab3")){
                    recyclerViewReview = view.findViewById(R.id.recycler_etc);
                    adapterReviewEtc = new Adapter_ReviewEtc(getActivity(), reviewEtcItems);
                    recyclerViewReview.setAdapter(adapterReviewLb);

//                    serverUrl = "http://thyun85.dothome.co.kr/dailyboard/insertETCReviewDB.php";

                    layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerViewReview.setLayoutManager(layoutManager);

                    loadDB();

                    actionButton = view.findViewById(R.id.fab_etc);
                    actionButton.setOnClickListener(clickEtc);

                    refreshLayout = view.findViewById(R.id.layout_swipe);
                    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            //갱신작업 수행.
                            reviewLbItems.clear();
                            loadDB();

                        }
                    });
                    adapterReviewLb.notifyDataSetChanged();

                }
            }
        });

        if(tag.equals("tab1")){
            recyclerViewReview = view.findViewById(R.id.recycler_lb);
            adapterReviewLb = new Adapter_ReviewLb(getActivity(), reviewLbItems);
            recyclerViewReview.setAdapter(adapterReviewLb);

//            serverUrl = "http://thyun85.dothome.co.kr/dailyboard/loadLBReviewDB.php";

            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerViewReview.setLayoutManager(layoutManager);

            loadDB();

            refreshLayout = view.findViewById(R.id.layout_swipe);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //갱신작업 수행.
                    reviewLbItems.clear();
                    loadDB();

                }
            });

            actionButton = view.findViewById(R.id.fab_lb);
            actionButton.setOnClickListener(clickLB);

            adapterReviewLb.notifyDataSetChanged();
        }

        return view;
    }

    View.OnClickListener clickLB = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), PostReviewLbActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener clickSk8 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), PostReviewSk8Activity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener clickEtc = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), PostReviewEtcActivity.class);
            startActivity(intent);
        }
    };

    private  void loadDB(){
//        String serverUrl = "http://thyun85.dothome.co.kr/dailyboard/loadLBReviewDB.php";
        if(tag.equals("tab1")){
            serverUrl = "http://thyun85.dothome.co.kr/dailyboard/loadLBReviewDB.php";
        }else if (tag.equals("tab2")){
            serverUrl = "http://thyun85.dothome.co.kr/dailyboard/loadSK8ReviewDB.php";
        }else if (tag.equals("tab3")){
            serverUrl = "http://thyun85.dothome.co.kr/dailyboard/loadETCReviewDB.php";
        }

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

                if(tag.equals("tab1")){
                    reviewLbItems.clear();
                    Log.i("aaa", "a4 : " + rows.length);
                    Log.i("aaa", "line : " + line);
                    for(String row : rows){
                        String[] datas = row.split("&");

                        for (int i = 0; i < datas.length; i++) Log.i("aaa2", i + " : " + datas[i]);
                        if(datas.length != 5) continue;
                        Log.i("aaa", "a5");
                        no = Integer.parseInt(datas[0]);
                        name = datas[1];
                        msg = datas[2];
                        imgPath = "http://thyun85.dothome.co.kr/dailyboard/"+datas[3];
                        date = datas[4];

                        reviewLbItems.add(0, new ReviewLbItem(no, name, msg, imgPath, date));

                        publishProgress();
                        Log.i("aaa", "a6");
                    }
                }else if (tag.equals("tab2")){
                    reviewSk8Items.clear();
                    Log.i("aaa", "a4 : " + rows.length);
                    Log.i("aaa", "line : " + line);
                    for(String row : rows){
                        String[] datas = row.split("&");

                        for (int i = 0; i < datas.length; i++) Log.i("aaa2", i + " : " + datas[i]);
                        if(datas.length != 5) continue;
                        Log.i("aaa", "a5");
                        no = Integer.parseInt(datas[0]);
                        name = datas[1];
                        msg = datas[2];
                        imgPath = "http://thyun85.dothome.co.kr/dailyboard/"+datas[3];
                        date = datas[4];

                        reviewSk8Items.add(0, new ReviewSk8Item(no, name, msg, imgPath, date));

                        publishProgress();
                        Log.i("aaa", "a6");
                    }
                }else if (tag.equals("tab3")){
                    reviewEtcItems.clear();
                    Log.i("aaa", "a4 : " + rows.length);
                    Log.i("aaa", "line : " + line);
                    for(String row : rows){
                        String[] datas = row.split("&");

                        for (int i = 0; i < datas.length; i++) Log.i("aaa2", i + " : " + datas[i]);
                        if(datas.length != 5) continue;
                        Log.i("aaa", "a5");
                        no = Integer.parseInt(datas[0]);
                        name = datas[1];
                        msg = datas[2];
                        imgPath = "http://thyun85.dothome.co.kr/dailyboard/"+datas[3];
                        date = datas[4];

                        reviewEtcItems.add(0, new ReviewEtcItem(no, name, msg, imgPath, date));

                        publishProgress();
                        Log.i("aaa", "a6");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return "읽기 완료";
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            //리사이클러뷰의 갱신요청..
            adapterReviewLb.notifyDataSetChanged();
            Log.i("notify", "success");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //swipeRefresh 로딩아이콘 지우기
            refreshLayout.setRefreshing(false);
        }
    }

}

package com.thy.daliyboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
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

    ArrayList<ReviewItem> reviewItems = new ArrayList<>();

    RecyclerView recyclerViewReview;
    Adapter_Review adapterReview;
    RecyclerView.LayoutManager layoutManager;

    SwipeRefreshLayout refreshLayout;

    FloatingActionButton actionButton;

//    LoadDBTask dbTask;
//    LoadLikeDBTask likeDBTask;

    int no;
    String name, msg, imgPath, type, date;
    String tag = "Lb";
    String serverUrl;

    String status, email;

    String reviewNo = null;

    ToggleButton tbFavorite;

    String[] datas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.review, container, false);

        tbFavorite = view.findViewById(R.id.tb_favorite);
        tabHost = view.findViewById(android.R.id.tabhost);

        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("Lb").setIndicator("LB").setContent(R.id.tablb));
        tabHost.addTab(tabHost.newTabSpec("Sk8").setIndicator("SK8").setContent(R.id.tabsk8));
        tabHost.addTab(tabHost.newTabSpec("Etc").setIndicator("ETC").setContent(R.id.tabetc));

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                tag = s;
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();

                if(tag.equals("Lb")) {
                    status = "Longboard";
                    recyclerViewReview = view.findViewById(R.id.recycler_lb);
                    adapterReview = new Adapter_Review(getActivity(), reviewItems);
                    recyclerViewReview.setAdapter(adapterReview);

                    layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerViewReview.setLayoutManager(layoutManager);

                    reviewItems.clear();
                    loadDB();

                    refreshLayout = view.findViewById(R.id.layout_swipe);
                    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            //갱신작업 수행.
                            reviewItems.clear();
                            loadDB();

                        }
                    });
                    adapterReview.notifyDataSetChanged();
                }else if(tag.equals("Sk8")){
                    status = "Skate";
                    recyclerViewReview = view.findViewById(R.id.recycler_sk8);
                    adapterReview = new Adapter_Review(getActivity(), reviewItems);
                    recyclerViewReview.setAdapter(adapterReview);

                    layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerViewReview.setLayoutManager(layoutManager);

                    reviewItems.clear();
                    loadDB();

                    refreshLayout = view.findViewById(R.id.layout_swipe);
                    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            //갱신작업 수행.
                            reviewItems.clear();
                            loadDB();

                        }
                    });
                    adapterReview.notifyDataSetChanged();

                }else if(tag.equals("Etc")){
                    status = "Etc";
                    recyclerViewReview = view.findViewById(R.id.recycler_etc);
                    adapterReview = new Adapter_Review(getActivity(), reviewItems);
                    recyclerViewReview.setAdapter(adapterReview);

                    layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerViewReview.setLayoutManager(layoutManager);

                    reviewItems.clear();
                    loadDB();

                    refreshLayout = view.findViewById(R.id.layout_swipe);
                    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            //갱신작업 수행.
                            reviewItems.clear();
                            loadDB();

                        }
                    });
                    adapterReview.notifyDataSetChanged();

                }
            }
        });

        if(tag.equals("Lb")){
            status = "Longboard";
            recyclerViewReview = view.findViewById(R.id.recycler_lb);
            adapterReview = new Adapter_Review(getActivity(), reviewItems);
            recyclerViewReview.setAdapter(adapterReview);

            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerViewReview.setLayoutManager(layoutManager);

            reviewItems.clear();
            loadDB();

            refreshLayout = view.findViewById(R.id.layout_swipe);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //갱신작업 수행.
                    reviewItems.clear();
                    loadDB();

                }
            });

            adapterReview.notifyDataSetChanged();
        }

        actionButton = view.findViewById(R.id.fab_review);
        actionButton.setOnClickListener(clickFab);

        SharedPreferences pref = getActivity().getSharedPreferences("facebookLoginData", getActivity().MODE_PRIVATE);
        email = pref.getString("Email", "no email");

        return view;
    }



    View.OnClickListener clickFab = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), PostReviewActivity.class);
            startActivity(intent);
        }
    };

    public void loadDB(){
        serverUrl = "http://thyun85.dothome.co.kr/dailyboard/loadReviewDB.php";

        //insertDB.php에 보낼 파일전송요청 객체 생성
        SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //insertDB.php의 echo 결과 보여주기
                new AlertDialog.Builder(getActivity()).setMessage(response).setPositiveButton("ok", null).create().show();

                //읽어온 데이터 문자열에서 db의 row(레코드)별로 배열로 분리하기
                String[] rows = response.split(";");

                reviewItems.clear();
                Log.i("aaa", "a4 : " + rows.length);
                Log.i("aaa", "response : " + response);

                for(String row : rows) {
                    datas = row.split("&");

                    //배열 내용 확인
                    for (int i = 0; i < datas.length; i++) Log.i("aaa2", i + " : " + datas[i]);

                    if(datas.length == 5){
                        no = Integer.parseInt(datas[0]);
                        name = datas[1];
                        msg = datas[2];
                        imgPath = "http://thyun85.dothome.co.kr/dailyboard/"+datas[3];
                        date = datas[4];

                        reviewItems.add(0, new ReviewItem(no, name, msg, imgPath, date));

                        adapterReview.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                        Log.i("aaa", "a6");
                    }else if(datas.length == 6){
                        no = Integer.parseInt(datas[0]);
                        name = datas[1];
                        msg = datas[2];
                        imgPath = "http://thyun85.dothome.co.kr/dailyboard/"+datas[3];
                        date = datas[4];
                        reviewNo = datas[5];
                        reviewItems.add(0, new ReviewItem(no, name, msg, imgPath, date));

                        adapterReview.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                        Log.i("aaa", "a6");
                    }
                }


//                for(String row : rows){
//                    String[] datas = row.split("&");
//                    Log.i("aaa", "a5 : " + datas.length);
//                    //배열 내용 확인
//                    for (int i = 0; i < datas.length; i++) Log.i("aaa2", i + " : " + datas[i]);
//
//                    if(datas.length != 6) continue;
//
//                    no = Integer.parseInt(datas[0]);
//                    name = datas[1];
//                    msg = datas[2];
//                    imgPath = "http://thyun85.dothome.co.kr/dailyboard/"+datas[3];
//                    date = datas[4];
//                    reviewNo = datas[5];
//                    reviewItems.add(0, new ReviewItem(no, name, msg, imgPath, date));
//
//                    adapterReview.notifyDataSetChanged();
//                    refreshLayout.setRefreshing(false);
//                    Log.i("aaa", "a6");
//                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //요청객체에 데이터 추가하기
        multiPartRequest.addStringParam("status", status);
        multiPartRequest.addStringParam("email", email);

        //요청큐 객체 생성하기
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //요청큐에 요청객체 추가
        requestQueue.add(multiPartRequest);

    }

}

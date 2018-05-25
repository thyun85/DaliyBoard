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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

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

public class Fragment_Spot extends Fragment {

    ArrayList<SpotItem> spotItems = new ArrayList<>();

    RecyclerView recyclerViewSpot;
    Adapter_Spot adapterSpot;

    SwipeRefreshLayout refreshLayout;

    FloatingActionButton actionButton;

    int no;
    String num, name, msg, imgPath, date;
    String email;
    String serverUrl_Spot;
    String favorite, like;
    int likeCnt;

    String[] datas;

    ImageView reply;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.spot, container, false);

        recyclerViewSpot = view.findViewById(R.id.recycler_spot);
        adapterSpot = new Adapter_Spot(getActivity(), spotItems);
        recyclerViewSpot.setAdapter(adapterSpot);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewSpot.setLayoutManager(layoutManager);

        spotItems.clear();
        loadDB();

        refreshLayout = view.findViewById(R.id.layout_swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //갱신작업 수행.
                spotItems.clear();
                loadDB();

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

        SharedPreferences pref = getActivity().getSharedPreferences("facebookLoginData", getActivity().MODE_PRIVATE);
        email = pref.getString("Email", "no email");

        return view;
    }

    private void loadDB(){
        serverUrl_Spot = "http://thyun85.dothome.co.kr/dailyboard/loadSpotDB.php";

        //insertDB.php에 보낼 파일전송요청 객체 생성
        SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverUrl_Spot, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("aaa", "a1");
                //insertDB.php의 echo 결과 보여주기
                new AlertDialog.Builder(getActivity()).setMessage(response).setPositiveButton("ok", null).create().show();

                if(response.equals("")){
                    new AlertDialog.Builder(getActivity()).setMessage("내용없음").setPositiveButton("ok", null).create().show();
                }else{
                    //읽어온 데이터 문자열에서 db의 row(레코드)별로 배열로 분리하기
                    String[] rows = response.split(";");

                    spotItems.clear();
                    Log.i("spot", "a2 : " + rows.length);
                    Log.i("spot", "response : " + response);

                    for(String row : rows) {
                        datas = row.split("&");
                        Log.i("datas.length", datas.length + "");
                        //배열 내용 확인
                        for (int i = 0; i < datas.length; i++)
                            Log.i("spot", "datas[" + i + "]" + " : " + datas[i]);

                        no = Integer.parseInt(datas[0]);
                        num = datas[0];
                        name = datas[1];
                        msg = datas[2];
                        imgPath = "http://thyun85.dothome.co.kr/dailyboard/" + datas[3];
                        date = datas[4];
                        like = datas[5];
                        favorite = datas[6];
                        likeCnt = Integer.parseInt(datas[7]);

                        boolean favoriteNo = false;
                        boolean likeNo = false;
                        if (like.equals(num)) likeNo = true;
                        if (favorite.equals(num)) favoriteNo = true;

                        Log.i("favoriteNo", favoriteNo + "");
                        Log.i("likeNo", likeNo + "");

                        spotItems.add(0, new SpotItem(no, name, msg, imgPath, date, likeNo, favoriteNo, likeCnt));

                        adapterSpot.notifyDataSetChanged();
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
        multiPartRequest.addStringParam("email", email);

        //요청큐 객체 생성하기
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //요청큐에 요청객체 추가
        requestQueue.add(multiPartRequest);
    }
}

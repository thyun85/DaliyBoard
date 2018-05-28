package com.thy.daliyboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by alofo on 2018-03-21.
 */

public class Fragment_Tips extends Fragment {

    ArrayList<TipsItem> tipsItems = new ArrayList<>();

    RecyclerView recyclerViewTip;
    Adapter_Tips adapterTips;

    SwipeRefreshLayout refreshLayout;

    FloatingActionButton actionButton;

    VideoView videoView;

    int no;
    String num, name, msg, aviPath, date;
    String email;
    String favorite, like;
    int likeCnt;

    SharedPreferences pref;

    String[] datas;

    TextView noticeTips;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tips, container, false);

        noticeTips = view.findViewById(R.id.notice_tips);

        recyclerViewTip = view.findViewById(R.id.recycler_tips);
        adapterTips = new Adapter_Tips(getActivity(), tipsItems);
        recyclerViewTip.setAdapter(adapterTips);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewTip.setLayoutManager(layoutManager);

        tipsItems.clear();
        loadDB();

        refreshLayout = view.findViewById(R.id.layout_swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //갱신작업 수행.
                tipsItems.clear();
                loadDB();

            }
        });

        actionButton = view.findViewById(R.id.fab);

        pref = getActivity().getSharedPreferences("facebookLoginData", getActivity().MODE_PRIVATE);
        String id = pref.getString("Id", "");

        if(id.equals("")){
            actionButton.setVisibility(View.GONE);
        }else{
            actionButton.setVisibility(View.VISIBLE);
            actionButton.attachToRecyclerView(recyclerViewTip);
            actionButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), PostTipsActivity.class);
                    startActivity(intent);
                }

            });
        }

//        actionButton.attachToRecyclerView(recyclerViewTip);
//        actionButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), PostTipsActivity.class);
//                startActivity(intent);
//            }
//
//        });

        adapterTips.notifyDataSetChanged();

        videoView = view.findViewById(R.id.videoview);

        SharedPreferences pref = getActivity().getSharedPreferences("facebookLoginData", getActivity().MODE_PRIVATE);
        email = pref.getString("Email", "no email");

        return view;
    }

    private  void loadDB(){
        String serverUrl_Tips = "http://thyun85.dothome.co.kr/dailyboard/loadTipsDB.php";

        //insertDB.php에 보낼 파일전송요청 객체 생성
        SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverUrl_Tips, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("aaa", "a1");
                //insertDB.php의 echo 결과 보여주기
//                new AlertDialog.Builder(getActivity()).setMessage(response).setPositiveButton("ok", null).create().show();

                if(response.equals("")){
//                    new AlertDialog.Builder(getActivity()).setMessage("내용없음").setPositiveButton("ok", null).create().show();
                    noticeTips.setVisibility(View.VISIBLE);
                }else{
                    noticeTips.setVisibility(View.GONE);
                    //읽어온 데이터 문자열에서 db의 row(레코드)별로 배열로 분리하기
                    String[] rows = response.split(";");

                    tipsItems.clear();
                    Log.i("aaa", "a2 : " + rows.length);
                    Log.i("aaa", "response : " + response);

                    for(String row : rows) {
                        datas = row.split("&");
                        Log.i("datas.length", datas.length + "");
                        //배열 내용 확인
                        for (int i = 0; i < datas.length; i++)
                            Log.i("tips", "datas[" + i + "]" + " : " + datas[i]);

                        no = Integer.parseInt(datas[0]);
                        num = datas[0];
                        name = datas[1];
                        msg = datas[2];
                        aviPath = "http://thyun85.dothome.co.kr/dailyboard/" + datas[3];
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

                        tipsItems.add(0, new TipsItem(no, name, msg, aviPath, date, likeNo, favoriteNo, likeCnt));

                        adapterTips.notifyDataSetChanged();
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
        multiPartRequest.addStringParam("email", email);

        //요청큐 객체 생성하기
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //요청큐에 요청객체 추가
        requestQueue.add(multiPartRequest);
    }

}

package com.thy.daliyboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by alofo on 2018-03-21.
 */

public class Fragment_Review extends Fragment {

    TabHost tabHost;

    ArrayList<ReviewLbItem> reviewLbItems = new ArrayList<>();

    RecyclerView recyclerViewReview;
    Adapter_ReviewLb adapterReviewLb;

    SwipeRefreshLayout refreshLayout;

    FloatingActionButton actionButton;

    int no;
    String name;
    String msg;
    String imgPath;
    String date;

    String tag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.review, container, false);

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
            }
        });

        recyclerViewReview = view.findViewById(R.id.recycler_lb);
        adapterReviewLb = new Adapter_ReviewLb(getActivity(), reviewLbItems);
        recyclerViewReview.setAdapter(adapterReviewLb);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewReview.setLayoutManager(layoutManager);

        refreshLayout = view.findViewById(R.id.layout_swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //갱신작업 수행.
//                feeds.clear();
                reviewLbItems.clear();
//                loadDB();
//                createListFeed();
            }
        });

        actionButton = view.findViewById(R.id.fab);
        actionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(tag ==null || tag.equals("tab1")){
                    Intent intent = new Intent(getActivity(), PostReviewLbActivity.class);
                    startActivity(intent);
                }else if(tag.equals("tab2")){
                    Intent intent = new Intent(getActivity(), PostReviewSk8Activity.class);
                    startActivity(intent);
                }else if(tag.equals("tab3")){
                    Intent intent = new Intent(getActivity(), PostReviewEtcActivity.class);
                    startActivity(intent);
                }


            }

        });


        return view;
    }


}

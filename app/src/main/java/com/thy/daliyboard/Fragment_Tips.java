package com.thy.daliyboard;

import android.app.AlertDialog;
import android.content.Context;
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

import java.util.ArrayList;

/**
 * Created by alofo on 2018-03-21.
 */

public class Fragment_Tips extends Fragment {

    ArrayList<Feed> feeds = new ArrayList<>();

    RecyclerView recyclerView;
    Adapter_Tips adapterTips;

    SwipeRefreshLayout refreshLayout;

    com.melnykov.fab.FloatingActionButton actionButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tips, container, false);

        recyclerView = view.findViewById(R.id.recycler);
        adapterTips = new Adapter_Tips(getActivity(), feeds);
        recyclerView.setAdapter(adapterTips);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        createListFeed();

        refreshLayout = view.findViewById(R.id.layout_swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //갱신작업 수행.
                feeds.clear();
                createListFeed();
            }
        });

        actionButton = view.findViewById(R.id.fab);

        actionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("게시물 추가");
                builder.setIcon(android.R.drawable.ic_dialog_alert);

                View dialogView = getLayoutInflater().inflate(R.layout.dialog, null);
                builder.setView(dialogView);

                AlertDialog dialog = builder.create();

                dialog.show();
            }

        });

        adapterTips.notifyDataSetChanged();

        return view;
    }

    public void createListFeed(){

        Feed feed = new Feed("androidprime",
                "http://imageshack.com/a/img924/3231/rv62A2.gif",
                "In this tutorial, I’m going to teach you how to use Android Context Menu and " +
                        "GridView. We’re going to creating a simple wallpaper application that " +
                        "will allow the users to choose and apply a wallpaper in our gallery.",
                "3 DAYS AGO", R.drawable.ch_luffy);
        feeds.add(0, feed);

        feed = new Feed("androidprime",
                "http://imageshack.com/a/img921/4021/wZaOP8.gif",
                "In this tutorial, I’m going to teach you how to use Android Context Menu and " +
                        "GridView. We’re going to creating a simple wallpaper application that " +
                        "will allow the users to choose and apply a wallpaper in our gallery.",
                "2 DAYS AGO", R.drawable.ch_chopa);
        feeds.add(0, feed);

        feed = new Feed("androidprime",
                "http://imageshack.com/a/img924/6593/aRddp8.gif",
                "In this tutorial, I’m going to teach you how to use Android Context Menu and " +
                        "GridView. We’re going to creating a simple wallpaper application that " +
                        "will allow the users to choose and apply a wallpaper in our gallery.",
                "1 DAYS AGO", R.drawable.ch_nami);
        feeds.add(0, feed);

        feed = new Feed("androidprime",
                "http://imageshack.com/a/img922/5727/EIRTCe.gif",
                "In this tutorial, I’m going to teach you how to use Android Context Menu and " +
                        "GridView. We’re going to creating a simple wallpaper application that " +
                        "will allow the users to choose and apply a wallpaper in our gallery.",
                "3 WEEkS AGO", R.drawable.ch_sandi);
        feeds.add(0, feed);

        feed = new Feed("androidprime",
                "http://imageshack.com/a/img923/9702/QbNuqq.gif",
                "In this tutorial, I’m going to teach you how to use Android Context Menu and " +
                        "GridView. We’re going to creating a simple wallpaper application that " +
                        "will allow the users to choose and apply a wallpaper in our gallery.",
                "2 WEEKS AGO", R.drawable.ch_usoup);
        feeds.add(0, feed);

        feed = new Feed("androidprime",
                "http://imageshack.com/a/img922/5038/2elaZ2.gif",
                "In this tutorial, I’m going to teach you how to use Android Context Menu and " +
                        "GridView. We’re going to creating a simple wallpaper application that " +
                        "will allow the users to choose and apply a wallpaper in our gallery.",
                "1 WEEKS AGO", R.drawable.ch_zoro);
        feeds.add(0, feed);

    }

}

package com.thy.daliyboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

/**
 * Created by alofo on 2018-03-21.
 */

public class Fragment_Spot extends Fragment {

    FloatingActionButton actionButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.spot, container, false);

        actionButton = view.findViewById(R.id.fab);

//        actionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), PostspotActivity.class);
//                startActivity(intent);
//
//            }
//        });

        return view;
    }

}

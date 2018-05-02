package com.thy.daliyboard;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by alofo on 2018-03-21.
 */

public class Fragment_Review extends Fragment {

    com.melnykov.fab.FloatingActionButton actionButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.review, container, false);

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

        return view;
    }

}

package com.thy.daliyboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.login.widget.LoginButton;

/**
 * Created by alofo on 2018-03-21.
 */

public class Fragment_MyPage extends Fragment {

    LoginButton loginButton;
    SharedPreferences pref;

    Button btn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mypage, container, false);

        loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref = getActivity().getSharedPreferences("facebookLoginData", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

            }
        });

        btn = view.findViewById(R.id.loadBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getActivity().getSharedPreferences("facebookLoginData", getActivity().MODE_PRIVATE);

                String id = pref.getString("Id", "");
                String name = pref.getString("Name", "");
                String email = pref.getString("Email", "");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("다이얼로그");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setMessage(id+"\n"+name+"\n"+email+"\n");

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }
}

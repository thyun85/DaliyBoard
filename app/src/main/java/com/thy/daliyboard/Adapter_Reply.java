package com.thy.daliyboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Reply extends RecyclerView.Adapter {

    Context context;
    ArrayList<ReplyItem> replyItems;
    boolean isEditable;

    String id, msg, email;

    public Adapter_Reply(Context context, ArrayList<ReplyItem> replyItems) {
        this.context = context;
        this.replyItems = replyItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View spotView = inflater.inflate(R.layout.reply_recycler_item, parent, false);

        VH holder = new VH(spotView);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        VH vh = (VH)holder;

        ReplyItem replyItem = replyItems.get(position);

        vh.tvTitle.setText(replyItem.nickName);
        vh.tvMessage.setText(replyItem.msg);
        vh.tvTimes.setText(replyItem.upDate);

    }

    public int getItemCount() {
        return replyItems.size();
    }

    class VH extends RecyclerView.ViewHolder{

        CircleImageView ivIcon;
        TextView tvTitle, tvMessage, tvTimes;

        public VH(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTimes = itemView.findViewById(R.id.tv_times);

        }
    }
}

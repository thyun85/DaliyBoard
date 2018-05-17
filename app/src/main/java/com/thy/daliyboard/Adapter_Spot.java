package com.thy.daliyboard;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Spot extends RecyclerView.Adapter {

    Context context;
    ArrayList<SpotItem> spotItems;

    public Adapter_Spot(Context context, ArrayList<SpotItem> spotItems) {
        this.context = context;
        this.spotItems = spotItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View spotView = inflater.inflate(R.layout.spot_recycler_item, parent, false);

        VH holder = new VH(spotView);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        VH vh = (VH)holder;

        SpotItem spotItem = spotItems.get(position);

        vh.tvTitle.setText(spotItem.nickName);
        vh.tvMessage.setText(spotItem.msg);
        vh.tvTimes.setText(spotItem.upDate);

        Glide.with(context).load(spotItem.getImgPath()).into(vh.imageView);

    }

    public int getItemCount() {
        return spotItems.size();
    }

    class VH extends RecyclerView.ViewHolder{

        CircleImageView ivIcon;
        TextView tvTitle, tvMessage, tvTimes;
        ImageView imageView;
        VideoView videoView;

        public VH(View itemView) {
            super(itemView);

//            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTimes = itemView.findViewById(R.id.tv_times);
            imageView = itemView.findViewById(R.id.iv_image);

        }
    }
}

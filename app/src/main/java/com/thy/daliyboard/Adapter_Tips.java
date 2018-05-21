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

public class Adapter_Tips extends RecyclerView.Adapter {

    Context context;
//    ArrayList<Feed> feeds;
    ArrayList<TipsItem> tipsItems;

//    public Adapter_Tips(Context context, ArrayList<Feed> feeds) {
//        this.context = context;
//        this.feeds = feeds;
//    }

    public Adapter_Tips(Context context, ArrayList<TipsItem> tipsItems) {
        this.context = context;
        this.tipsItems = tipsItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View tipsView = inflater.inflate(R.layout.tips_recycler_item, parent, false);

        VH holder = new VH(tipsView);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        VH vh = (VH)holder;

        TipsItem tipsItem = tipsItems.get(position);

        vh.tvTitle.setText(tipsItem.nickName);
        vh.tvMessage.setText(tipsItem.msg);
        vh.tvTimes.setText(tipsItem.upDate);

//        Glide.with(context).load(tipsItem.getAviPath()).into(vh.imageView);

        MediaController mediaController = new MediaController(context);
        mediaController.setAnchorView(vh.videoView);
        Uri video = Uri.parse(tipsItem.aviPath);
        vh.videoView.setMediaController(mediaController);
        vh.videoView.setVideoURI(video);
        vh.videoView.requestFocus();
        vh.videoView.start();

    }

    @Override
//    public int getItemCount() {
//        return feeds.size();
//    }

    public int getItemCount() {
        return tipsItems.size();
    }

    class VH extends RecyclerView.ViewHolder{

        CircleImageView ivIcon;
        TextView tvTitle, tvMessage, tvTimes;
//        ImageView imageView;
        VideoView videoView;

        public VH(View itemView) {
            super(itemView);

//            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTimes = itemView.findViewById(R.id.tv_times);
//            imageView = itemView.findViewById(R.id.iv_image);
            videoView = itemView.findViewById(R.id.videoview);
        }
    }
}

package com.thy.daliyboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Tips extends RecyclerView.Adapter {

    Context context;
    ArrayList<Feed> feeds;

    public Adapter_Tips(Context context, ArrayList<Feed> feeds) {
        this.context = context;
        this.feeds = feeds;
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

        Feed feed = feeds.get(position);

        vh.tvTitle.setText(feed.title);
        vh.tvMessage.setText(feed.message);
        vh.tvTimes.setText(feed.times);

        Glide.with(context).load(feed.icon).into(vh.ivIcon);
        Glide.with(context).load(feed.getImageURL()).into(vh.imageView);

    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    class VH extends RecyclerView.ViewHolder{

        CircleImageView ivIcon;
        TextView tvTitle, tvMessage, tvTimes;
        ImageView imageView;

        public VH(View itemView) {
            super(itemView);

            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTimes = itemView.findViewById(R.id.tv_times);
            imageView = itemView.findViewById(R.id.iv_image);
        }
    }
}

package com.thy.daliyboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_ReviewLb extends RecyclerView.Adapter {

    Context context;
    ArrayList<ReviewLbItem> reviewLbItems;

    public Adapter_ReviewLb(Context context, ArrayList<ReviewLbItem> reviewLbItems) {
        this.context = context;
        this.reviewLbItems = reviewLbItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View tipsView = inflater.inflate(R.layout.reviewlb_recycler_item, parent, false);

        VH holder = new VH(tipsView);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        VH vh = (VH)holder;

        ReviewLbItem reviewLbItem = reviewLbItems.get(position);

        vh.tvTitle.setText(reviewLbItem.nickName);
        vh.tvMessage.setText(reviewLbItem.msg);
        vh.tvTimes.setText(reviewLbItem.upDate);
        vh.tbFavorite.setChecked(CheckedLb.isFavorite);

        Glide.with(context).load(reviewLbItem.getImgPath()).into(vh.imageView);

    }

    public int getItemCount() {
        return reviewLbItems.size();
    }

    class VH extends RecyclerView.ViewHolder{

        CircleImageView ivIcon;
        TextView tvTitle, tvMessage, tvTimes;
        ImageView imageView;
        ToggleButton tbFavorite;

        public VH(View itemView) {
            super(itemView);

//            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTimes = itemView.findViewById(R.id.tv_times);
            imageView = itemView.findViewById(R.id.iv_image);
            tbFavorite = itemView.findViewById(R.id.tb_favorite);
            tbFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    switch (compoundButton.getId()){
                        case R.id.tb_favorite:
                            CheckedLb.isFavorite = checked;
                            break;
                    }
                }
            });
        }
    }
}

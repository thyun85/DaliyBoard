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

public class Adapter_ReviewEtc extends RecyclerView.Adapter {

    Context context;
    ArrayList<ReviewEtcItem> reviewEtcItems;

    public Adapter_ReviewEtc(Context context, ArrayList<ReviewEtcItem> reviewEtcItems) {
        this.context = context;
        this.reviewEtcItems = reviewEtcItems;
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

        ReviewEtcItem reviewEtcItem = reviewEtcItems.get(position);

        vh.tvTitle.setText(reviewEtcItem.nickName);
        vh.tvMessage.setText(reviewEtcItem.msg);
        vh.tvTimes.setText(reviewEtcItem.upDate);
        vh.tbFavorite.setChecked(CheckedLb.isFavorite);

        Glide.with(context).load(reviewEtcItem.getImgPath()).into(vh.imageView);

    }

    public int getItemCount() {
        return reviewEtcItems.size();
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
//                            CheckedEtc.isFavorite = checked;
                            break;
                    }
                }
            });
        }
    }
}

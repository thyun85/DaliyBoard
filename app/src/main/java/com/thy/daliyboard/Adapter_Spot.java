package com.thy.daliyboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Spot extends RecyclerView.Adapter {

    Context context;
    ArrayList<SpotItem> spotItems;
    boolean isEditable;

    String id, msg, email;
    int no;

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

        isEditable = false;

        VH vh = (VH)holder;

        SpotItem spotItem = spotItems.get(position);

        vh.tvTitle.setText(spotItem.nickName);
        vh.tvMessage.setText(spotItem.msg);
        vh.tvTimes.setText(spotItem.upDate);
        vh.spotFavorite.setChecked(spotItem.isFavorite);
        vh.spotLike.setChecked(spotItem.isLike);
        vh.tvLikeCount.setText(spotItem.likeCnt + "");

        Glide.with(context).load(spotItem.getImgPath()).into(vh.imageView);

        isEditable = true;

    }

    public int getItemCount() {
        return spotItems.size();
    }

    class VH extends RecyclerView.ViewHolder{

        CircleImageView ivIcon;
        TextView tvTitle, tvMessage, tvTimes, tvLikeCount;
        ImageView imageView, reply;
        ToggleButton spotFavorite, spotLike;

        int cnt;

        public VH(View itemView) {
            super(itemView);

//            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTimes = itemView.findViewById(R.id.tv_times);
            imageView = itemView.findViewById(R.id.iv_image);
            tvLikeCount = itemView.findViewById(R.id.likecount);

            spotFavorite = itemView.findViewById(R.id.tb_spot_favorite);
            spotFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    switch (compoundButton.getId()){
                        case R.id.tb_spot_favorite:
                            if(isEditable){
                                spotItems.get(getLayoutPosition()).isFavorite = checked;
                                SharedPreferences pref = context.getSharedPreferences("facebookLoginData", context.MODE_PRIVATE);
                                email = pref.getString("Email", "no email");
                                no = spotItems.get(getLayoutPosition()).getNo();
                                Log.i("favorite checked", checked+"");
                                if(checked) uploadFavoriteDB();
                                else deleteFavoriteDB();
                            }
                            break;
                    }
                }
            });

            spotLike = itemView.findViewById(R.id.tb_spot_like);
            spotLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    switch (compoundButton.getId()){
                        case R.id.tb_spot_like:
                            if(isEditable){
                                Log.v("test-111", getLayoutPosition() + "");
                                spotItems.get(getLayoutPosition()).isLike = checked;
                                SharedPreferences pref = context.getSharedPreferences("facebookLoginData", context.MODE_PRIVATE);
                                email = pref.getString("Email", "no email");
                                no = spotItems.get(getLayoutPosition()).getNo();
                                Log.i("like checked", checked+"");
                                if(checked) uploadLikeDB();
                                else deleteLikeDB();

                                SpotItem item = spotItems.get(getLayoutPosition());
                                int likeCount = spotItems.get(getLayoutPosition()).likeCnt;
                                if (item.isLike) {
                                    item.likeCnt = ++likeCount;
                                }
                                else {
                                    item.likeCnt = --likeCount;
                                }
                                tvLikeCount.setText(likeCount + "");

                            }
                            break;
                    }
                }
            });
            reply = itemView.findViewById(R.id.spot_reply);
            reply.setOnClickListener(clickReply);
        }

        View.OnClickListener clickReply = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemNo = spotItems.get(getLayoutPosition()).getNo()+"";
                String itemMsg = spotItems.get(getLayoutPosition()).getMsg();
                String itemDate = spotItems.get(getLayoutPosition()).getUpDate();
                Intent intent = new Intent(context, ReplyActivity.class);
                intent.putExtra("itemNo", itemNo);
                intent.putExtra("itemMsg", itemMsg);
                intent.putExtra("itemDate", itemDate);
                intent.putExtra("type", "spot");
                Log.i("no", itemNo+"");
                context.startActivity(intent);
            }
        };

        public void deleteLikeDB(){
            String serverUrl = "http://thyun85.dothome.co.kr/dailyboard/deleteLikeSpotDB.php";
            Log.i("aaa", serverUrl);
            //insertpostDB.php에 보낼 파일전송요청 객체 생성
            SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    new AlertDialog.Builder(context).setMessage(response).create().show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            //요청객체에 데이터 추가하기
            multiPartRequest.addStringParam("email", email);
            multiPartRequest.addStringParam("no", no + "");

            //요청큐 객체 생성하기
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            //요청큐에 요청객체 추가..
            requestQueue.add(multiPartRequest);
        }

        public void uploadLikeDB(){
            String serverUrl = "http://thyun85.dothome.co.kr/dailyboard/insertLikeSpotDB.php";
            Log.i("aaa", serverUrl);
            //insertpostDB.php에 보낼 파일전송요청 객체 생성
            SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    new AlertDialog.Builder(context).setMessage(response).create().show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            //요청객체에 데이터 추가하기
            multiPartRequest.addStringParam("email", email);
//            multiPartRequest.addStringParam("msg", msg);
            multiPartRequest.addStringParam("no", no + "");

            //요청큐 객체 생성하기
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            //요청큐에 요청객체 추가..
            requestQueue.add(multiPartRequest);
        }

        public void deleteFavoriteDB(){
            String serverUrl = "http://thyun85.dothome.co.kr/dailyboard/deleteFavoriteSpotDB.php";
            Log.i("aaa", serverUrl);
            //insertpostDB.php에 보낼 파일전송요청 객체 생성
            SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    new AlertDialog.Builder(context).setMessage(response).create().show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            //요청객체에 데이터 추가하기
            multiPartRequest.addStringParam("email", email);
//            multiPartRequest.addStringParam("msg", msg);
            multiPartRequest.addStringParam("no", no + "");

            //요청큐 객체 생성하기
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            //요청큐에 요청객체 추가..
            requestQueue.add(multiPartRequest);
        }

        public void uploadFavoriteDB(){
            String serverUrl = "http://thyun85.dothome.co.kr/dailyboard/insertFavoriteSpotDB.php";
            Log.i("aaa", serverUrl);
            //insertpostDB.php에 보낼 파일전송요청 객체 생성
            SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    new AlertDialog.Builder(context).setMessage(response).create().show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            //요청객체에 데이터 추가하기
            multiPartRequest.addStringParam("email", email);
//            multiPartRequest.addStringParam("msg", msg);
            multiPartRequest.addStringParam("no", no + "");

            //요청큐 객체 생성하기
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            //요청큐에 요청객체 추가..
            requestQueue.add(multiPartRequest);
        }
    }
}

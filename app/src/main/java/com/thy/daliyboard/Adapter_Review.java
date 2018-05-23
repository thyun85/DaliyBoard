package com.thy.daliyboard;

import android.app.AlertDialog;
import android.content.Context;
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

public class Adapter_Review extends RecyclerView.Adapter {

    Context context;
    ArrayList<ReviewItem> reviewItems;
    boolean isEditable;

    String id, msg, email;

    public Adapter_Review(Context context, ArrayList<ReviewItem> reviewItems) {
        this.context = context;
        this.reviewItems = reviewItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View tipsView = inflater.inflate(R.layout.review_recycler_item, parent, false);

        VH holder = new VH(tipsView);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        isEditable = false;

        VH vh = (VH)holder;

        ReviewItem reviewItem = reviewItems.get(position);

        vh.tvTitle.setText(reviewItem.nickName);
        vh.tvMessage.setText(reviewItem.msg);
        vh.tvTimes.setText(reviewItem.upDate);

        vh.reviewFavorite.setChecked(reviewItem.isFavorite);


        Glide.with(context).load(reviewItem.getImgPath()).into(vh.imageView);

        isEditable = true;

    }

    public int getItemCount() {
        return reviewItems.size();
    }

    class VH extends RecyclerView.ViewHolder{

        CircleImageView ivIcon;
        TextView tvTitle, tvMessage, tvTimes;
        ImageView imageView;
        ToggleButton reviewFavorite, reviewLike;

        public VH(View itemView) {
            super(itemView);

//            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTimes = itemView.findViewById(R.id.tv_times);
            imageView = itemView.findViewById(R.id.iv_image);
            reviewFavorite = itemView.findViewById(R.id.tb_review_favorite);
            reviewFavorite.setOnCheckedChangeListener(checkedChangeListener);

            reviewLike = itemView.findViewById(R.id.tb_review_like);
            reviewLike.setOnCheckedChangeListener(checkedChangeListener);
//            tbFavorite.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(tbFavorite.isChecked()){
//                        //속성값 삭제(php)
//                        reviewItems.get(getLayoutPosition()).setFavorite(false);
//                        SharedPreferences pref = context.getSharedPreferences("facebookLoginData", context.MODE_PRIVATE);
//                        email = pref.getString("Email", "no email");
//                        msg = reviewItems.get(getLayoutPosition()).getMsg();
//                        delete();
//                    }else{
//                        //속성값 insert
//                        reviewItems.get(getLayoutPosition()).setFavorite(true);
//                        SharedPreferences pref = context.getSharedPreferences("facebookLoginData", context.MODE_PRIVATE);
//                        email = pref.getString("Email", "no email");
//                        msg = reviewItems.get(getLayoutPosition()).getMsg();
//                        upload();
//                    }
//                }
//            });
        }

        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                switch (compoundButton.getId()){
                    case R.id.tb_review_favorite:
                        if(isEditable){
                            reviewItems.get(getLayoutPosition()).isFavorite = checked;
                            SharedPreferences pref = context.getSharedPreferences("facebookLoginData", context.MODE_PRIVATE);
                            email = pref.getString("Email", "no email");
                            msg = reviewItems.get(getLayoutPosition()).getMsg();
                            Log.i("checked", checked+"");
                            if(checked) uploadFavoriteDB();
                            else deleteFavoriteDB();
                        }
                        break;
                    case R.id.tb_review_like:
                        if(isEditable){
                            reviewItems.get(getLayoutPosition()).isLike = checked;
                            SharedPreferences pref = context.getSharedPreferences("facebookLoginData", context.MODE_PRIVATE);
                            email = pref.getString("Email", "no email");
                            msg = reviewItems.get(getLayoutPosition()).getMsg();
                            Log.i("checked", checked+"");
                            if(checked) uploadLikeDB();
                            else deleteLikeDB();
                        }
                        break;
                }
            }
        };

        public void deleteLikeDB(){
            String serverUrl = "http://thyun85.dothome.co.kr/dailyboard/deleteReviewDB.php";
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
            multiPartRequest.addStringParam("msg", msg);

            //요청큐 객체 생성하기
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            //요청큐에 요청객체 추가..
            requestQueue.add(multiPartRequest);
        }

        public void uploadLikeDB(){
            String serverUrl = "http://thyun85.dothome.co.kr/dailyboard/insertLikeReviewDB.php";
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
            multiPartRequest.addStringParam("msg", msg);

            //요청큐 객체 생성하기
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            //요청큐에 요청객체 추가..
            requestQueue.add(multiPartRequest);
        }

        public void deleteFavoriteDB(){
            String serverUrl = "http://thyun85.dothome.co.kr/dailyboard/deleteFavoriteReviewDB.php";
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
            multiPartRequest.addStringParam("msg", msg);

            //요청큐 객체 생성하기
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            //요청큐에 요청객체 추가..
            requestQueue.add(multiPartRequest);
        }

        public void uploadFavoriteDB(){
            String serverUrl = "http://thyun85.dothome.co.kr/dailyboard/insertFavoriteReviewDB.php";
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
            multiPartRequest.addStringParam("msg", msg);

            //요청큐 객체 생성하기
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            //요청큐에 요청객체 추가..
            requestQueue.add(multiPartRequest);
        }
    }
}

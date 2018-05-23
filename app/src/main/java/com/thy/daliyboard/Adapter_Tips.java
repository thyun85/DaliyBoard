package com.thy.daliyboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
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

public class Adapter_Tips extends RecyclerView.Adapter {

    Context context;
    ArrayList<TipsItem> tipsItems;
    boolean isEditable;

    String id, msg, email;

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

        isEditable = false;

        VH vh = (VH)holder;

        TipsItem tipsItem = tipsItems.get(position);

        vh.tvTitle.setText(tipsItem.nickName);
        vh.tvMessage.setText(tipsItem.msg);
        vh.tvTimes.setText(tipsItem.upDate);
        vh.tipsFavorite.setChecked(tipsItem.isFavorite);
        vh.tipsLike.setChecked(tipsItem.isLike);

        MediaController mediaController = new MediaController(context);
        mediaController.setAnchorView(vh.videoView);
        Uri video = Uri.parse(tipsItem.aviPath);
        vh.videoView.setMediaController(mediaController);
        vh.videoView.setVideoURI(video);
        vh.videoView.requestFocus();
        vh.videoView.start();

        isEditable = true;

    }

    public int getItemCount() {
        return tipsItems.size();
    }

    class VH extends RecyclerView.ViewHolder{

        CircleImageView ivIcon;
        TextView tvTitle, tvMessage, tvTimes;
        VideoView videoView;
        ToggleButton tipsFavorite, tipsLike;

        public VH(View itemView) {
            super(itemView);

//            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTimes = itemView.findViewById(R.id.tv_times);
            videoView = itemView.findViewById(R.id.videoview);
            tipsFavorite = itemView.findViewById(R.id.tb_tips_favorite);
            tipsLike = itemView.findViewById(R.id.tb_tips_like);
            tipsFavorite.setOnCheckedChangeListener(checkedChangeListener);
            tipsLike.setOnCheckedChangeListener(checkedChangeListener);
        }

        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                switch (compoundButton.getId()){
                    case R.id.tb_tips_favorite:
                        if(isEditable){
                            tipsItems.get(getLayoutPosition()).isFavorite = checked;
                            SharedPreferences pref = context.getSharedPreferences("facebookLoginData", context.MODE_PRIVATE);
                            email = pref.getString("Email", "no email");
                            msg = tipsItems.get(getLayoutPosition()).getMsg();
                            Log.i("checked", checked+"");
                            if(checked) uploadFavoriteDB();
                            else deleteFavoriteDB();
                        }
                        break;
                    case R.id.tb_tips_like:
                        if(isEditable){
                            tipsItems.get(getLayoutPosition()).isLike = checked;
                            SharedPreferences pref = context.getSharedPreferences("facebookLoginData", context.MODE_PRIVATE);
                            email = pref.getString("Email", "no email");
                            msg = tipsItems.get(getLayoutPosition()).getMsg();
                            Log.i("checked", checked+"");
                            if(checked) uploadLikeDB();
                            else deleteLikeDB();
                        }
                        break;
                }
            }
        };

        public void deleteLikeDB(){
            String serverUrl = "http://thyun85.dothome.co.kr/dailyboard/deleteLikeDBtips.php";
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
            String serverUrl = "http://thyun85.dothome.co.kr/dailyboard/insertLikeDB.php";
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
            String serverUrl = "http://thyun85.dothome.co.kr/dailyboard/deleteFavoriteDBspot.php";
            Log.i("aaa", serverUrl);
            //insertpostDB.php에 보낼 파일전송요청 객체 생성
            SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    new android.app.AlertDialog.Builder(context).setMessage(response).create().show();
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
            String serverUrl = "http://thyun85.dothome.co.kr/dailyboard/insertFavoriteDBspot.php";
            Log.i("aaa", serverUrl);
            //insertpostDB.php에 보낼 파일전송요청 객체 생성
            SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    new android.app.AlertDialog.Builder(context).setMessage(response).create().show();
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

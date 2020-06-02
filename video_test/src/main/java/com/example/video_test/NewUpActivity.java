package com.example.video_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Response;

import static com.example.video_test.IndexActivity.setTranslucentStatus;
import static com.example.video_test.ReleaseDynamicActivity.stringToBitmap;

public class NewUpActivity extends AppCompatActivity {

    private RecyclerView attentionList;
    private String account;
    private String keyword;
    private TextView cancel;
    private List<AttentionListInfo>attentionInfo;
    private AttentionAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);//沉浸式状态栏
        setContentView(R.layout.activity_new_up);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        init();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewUpActivity.this,MyActivity.class));
                finish();
            }
        });
        attentionList = (RecyclerView)findViewById(R.id.userList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        attentionList.setLayoutManager(manager);
        adapter = new AttentionAdapter(attentionInfo);
        attentionList.setAdapter(adapter);

        attentionList.addOnItemTouchListener(new RecyclerItemClickListener(this,
                attentionList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final AttentionListInfo info = attentionInfo.get(position);
                if(info.getTag()==1) {
                    String web="deleteAttention.jsp";
                    HttpUtil.deleteAttention(web,account,info.getAccount(),new okhttp3.Callback(){
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(NewUpActivity.this, "网络异常，电波无法到达~", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            final String result=response.body().string().trim();
                            if(result.equals("取消关注成功")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        info.setTag(2);
                                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.focus, null);
                                        adapter.change(info, bitmap);
                                        Toast.makeText(NewUpActivity.this, "取消关注成功~", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                            else{
                                Toast.makeText(NewUpActivity.this, "取消关注失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else{
                    String web="addAttention.jsp";
                    HttpUtil.addAttention(web,account,info.getAccount(),new okhttp3.Callback(){
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(NewUpActivity.this, "网络异常，电波无法到达~", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            final String result=response.body().string().trim();
                            if(result.equals("关注成功")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        info.setTag(1);
                                        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.nofocus, null);
                                        adapter.change(info,bitmap);
                                        Toast.makeText(NewUpActivity.this, "关注成功~", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                                Toast.makeText(NewUpActivity.this, "关注失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //到时候写跳转到个人主页
            }
        }));
    }

    private void init(){
        cancel=(TextView)findViewById(R.id.cancel);
        Intent intent=getIntent();
        attentionInfo=new ArrayList<AttentionListInfo>();
        account=intent.getStringExtra("account");
        keyword=intent.getStringExtra("keyword");
        String web="getNewUp.jsp";
        HttpUtil.getNewUp(web,account,keyword,new okhttp3.Callback(){
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewUpActivity.this, "网络异常，电波无法到达~", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                attentionInfo=parseJson(response.body().string().trim());//必须用trim()，否则有非法字符
                if(attentionInfo!=null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new AttentionAdapter(attentionInfo);
                            attentionList.setAdapter(adapter);
                        }
                    });
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewUpActivity.this, "无此用户~", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    public class AttentionViewHolder extends RecyclerView.ViewHolder{
        private de.hdodenhof.circleimageview.CircleImageView userPhoto;
        private TextView nickName;
        private TextView signature;
        private ImageView attention;

        public AttentionViewHolder(View item){
            super(item);
            userPhoto=(de.hdodenhof.circleimageview.CircleImageView)item.findViewById(R.id.userPhoto);
            nickName=(TextView)item.findViewById(R.id.nickName);
            signature=(TextView)item.findViewById(R.id.signature);
            attention=(ImageView)item.findViewById(R.id.attention);

        }
    }

    public class AttentionAdapter extends RecyclerView.Adapter<AttentionViewHolder>{
        private List<AttentionListInfo> AttentionInfo;

        public AttentionAdapter(List<AttentionListInfo> AttentionInfo) {
            this.AttentionInfo = AttentionInfo;
        }

        @NonNull
        @Override
        public AttentionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.attention_list_item,
                            parent, false);
            AttentionViewHolder holder = new AttentionViewHolder(view);
            return holder;
        }

        //修改关注图标
        public void change(AttentionListInfo info , Bitmap bitmap) {
            info.setAttention(bitmap);
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(@NonNull AttentionViewHolder holder, int position) {
            AttentionListInfo info = AttentionInfo.get(position);
            holder.userPhoto.setImageBitmap(info.getUserPhoto());
            holder.nickName.setText(info.getNickName());
            holder.signature.setText(info.getSignature());
            holder.attention.setImageBitmap(info.getAttention());
        }

        @Override
        public int getItemCount() {
            return AttentionInfo.size();
        }
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        public interface OnItemClickListener {
            void onItemClick(View view, int position);

            void onItemLongClick(View view, int position);
        }

        private RecyclerItemClickListener.OnItemClickListener mListener;

        private GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, RecyclerItemClickListener.OnItemClickListener listener) {
            mListener = listener;

            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if (childView != null && mListener != null) {
                        mListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());

            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    private List<AttentionListInfo> parseJson(String jsonData){
        //初始化解析器
        List<AttentionListInfo> data= new ArrayList<AttentionListInfo>();
        try{
            JSONArray jsonArray=new JSONArray(jsonData);
            int len=jsonArray.length();
            for(int i=0;i<len;i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                AttentionListInfo info=new AttentionListInfo();
                info.setAccount(jsonObject.getString("account"));
                info.setUserPhoto(stringToBitmap(jsonObject.getString("userPhoto")));
                info.setNickName(jsonObject.getString("nickName"));
                info.setSignature(jsonObject.getString("signature"));
                info.setAttention(BitmapFactory.decodeResource(getResources(), R.mipmap.focus, null));
                info.setTag(2);
                data.add(info);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return data;
    }
}

package com.example.video_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
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

public class FansActivity extends AppCompatActivity {

    private RecyclerView fansList;
    private List<AttentionListInfo>attentionInfo;
    private ImageView back;
    private TextView number;
    private AttentionAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);//沉浸式状态栏
        setContentView(R.layout.activity_fans);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        init();
        fansList = (RecyclerView)findViewById(R.id.fansList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        fansList.setLayoutManager(manager);
        adapter = new AttentionAdapter(attentionInfo);
        fansList.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FansActivity.this,MyActivity.class));
                finish();
            }
        });

        fansList.addOnItemTouchListener(new FansActivity.RecyclerItemClickListener(this,
                fansList, new FansActivity.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //到时候写跳转到个人主页
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

    }

    private void init(){
        attentionInfo=new ArrayList<AttentionListInfo>();
        back=(ImageView)findViewById(R.id.back);
        number=(TextView)findViewById(R.id.number);
        Intent intent=getIntent();
        number.setText(intent.getStringExtra("number"));
        String web="getFans.jsp";
        HttpUtil.getFans(web,intent.getStringExtra("account"),new okhttp3.Callback(){
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FansActivity.this, "网络异常，电波无法到达~", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                attentionInfo=parseJson(response.body().string().trim());//必须用trim()，否则有非法字符
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new AttentionAdapter(attentionInfo);
                        fansList.setAdapter(adapter);
                    }
                });
            }
        });

    }

    public class AttentionViewHolder extends RecyclerView.ViewHolder{
        private de.hdodenhof.circleimageview.CircleImageView userPhoto;
        private TextView nickName;
        private TextView signature;

        public AttentionViewHolder(View item){
            super(item);
            userPhoto=(de.hdodenhof.circleimageview.CircleImageView)item.findViewById(R.id.userPhoto);
            nickName=(TextView)item.findViewById(R.id.nickName);
            signature=(TextView)item.findViewById(R.id.signature);

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

        @Override
        public void onBindViewHolder(@NonNull AttentionViewHolder holder, int position) {
            AttentionListInfo info = AttentionInfo.get(position);
            holder.userPhoto.setImageBitmap(info.getUserPhoto());
            holder.nickName.setText(info.getNickName());
            holder.signature.setText(info.getSignature());
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

        private FansActivity.RecyclerItemClickListener.OnItemClickListener mListener;

        private GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, FansActivity.RecyclerItemClickListener.OnItemClickListener listener) {
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
                data.add(info);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return data;
    }
}

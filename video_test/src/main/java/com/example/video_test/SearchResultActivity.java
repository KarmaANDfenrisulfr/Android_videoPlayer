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
import android.util.Log;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import okhttp3.Call;
import okhttp3.Response;

import static com.example.video_test.DynamicActivity.toRoundCornerImage;
import static com.example.video_test.IndexActivity.setTranslucentStatus;

public class SearchResultActivity extends AppCompatActivity {

    private List<IndexListInfo>SearchResultInfo=new ArrayList<IndexListInfo>();
    private RecyclerView searchResultList;
    private SearchResultAdapter adapter;
    private TextView cancel;
    private  VideoInfo video ;
    private Bitmap startImage;
    private Bitmap userPhoto;
    private List<VideoInfo> listVideo;
    private String url="https://api.vc.bilibili.com/clip/v1/video/index?page_size=50&need_playurl=0&next_offset=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);//沉浸式状态栏
        setContentView(R.layout.activity_search_result);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }

        searchResultList = (RecyclerView)findViewById(R.id.searchResultList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        searchResultList.setLayoutManager(manager);
        init();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchResultList.addOnItemTouchListener(new RecyclerItemClickListener(this, searchResultList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                IndexListInfo info = SearchResultInfo.get(position);
                IndexActivity.videoUpPhoto=info.getUserPhoto();
                IndexActivity.videoBackground=info.getVideoStartImage();
                Intent intent=new Intent(SearchResultActivity.this,MainActivity.class);
                intent.putExtra("video_url",info.getVideo_url());
                intent.putExtra("theme",info.getVideoTheme());
                intent.putExtra("upload_time",info.getUpload_time());
                intent.putExtra("name",info.getVideoAuthorAndTime());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

    }

    public void init(){
        final Intent intent=getIntent();
        cancel=(TextView)findViewById(R.id.cancel);
        HttpUtil.getVideo(url,new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                listVideo = parseJson(response.body().string().trim(),intent.getStringExtra("keyword"));
                if (listVideo.size()>0) {
                    Iterator<VideoInfo> iter = listVideo.iterator();
                    while (iter.hasNext()) {
                        video = iter.next();
                        InputStream is;
                        is = new java.net.URL(video.getFirst_pic()).openStream();
                        startImage = toRoundCornerImage(BitmapFactory.decodeStream(is),8);
                        is = new java.net.URL(video.getHead_url()).openStream();
                        userPhoto = BitmapFactory.decodeStream(is);
                        is.close();
                        SearchResultInfo.add(new IndexListInfo(startImage,userPhoto,video.getDescription(),
                                video.getName(),video.getUpload_time(), video.getVideo_time(),video.getShare_url()));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new SearchResultAdapter(SearchResultInfo);
                            searchResultList.setAdapter(adapter);
                        }
                    });
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SearchResultActivity.this,"没有相关视频",Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("信息","失败");
            }
        });
    }


    public class SearchResultViewHolder extends RecyclerView.ViewHolder{
        private ImageView videoStartImage;
        private TextView videoTheme;
        private TextView videoAuthor;
        private TextView videoRunTime;

        public SearchResultViewHolder(View item){
            super(item);
            videoStartImage=(ImageView)item.findViewById(R.id.videoStartImage);
            videoTheme=(TextView)item.findViewById(R.id.videoTheme);
            videoAuthor=(TextView)item.findViewById(R.id.videoAuthor);
            videoRunTime=(TextView)item.findViewById(R.id.videoRunTime);

        }
    }

    public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder>{
        private List<IndexListInfo> SearchResultInfo;

        public SearchResultAdapter(List<IndexListInfo> SearchResultInfo) {
            this.SearchResultInfo = SearchResultInfo;
        }

        @NonNull
        @Override
        public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_result_item,
                            parent, false);
            SearchResultViewHolder holder = new SearchResultViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
            IndexListInfo info = SearchResultInfo.get(position);
            holder.videoStartImage.setImageBitmap(info.getVideoStartImage());
            holder.videoTheme.setText(info.getVideoTheme());
            holder.videoAuthor.setText(info.getVideoAuthorAndTime());
            holder.videoRunTime.setText(info.getVideoRunTime());
        }

        @Override
        public int getItemCount() {
            return SearchResultInfo.size();
        }
    }

    private List<VideoInfo> parseJson(String jsonData,String str){
        List<VideoInfo> DATA= new ArrayList<VideoInfo>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject data=jsonObject.getJSONObject("data");
            JSONArray items=data.getJSONArray("items");
            int len=items.length();
            for(int i=0;i<len;i++) {
                VideoInfo info=new VideoInfo();
                JSONObject temp = items.getJSONObject(i);
                JSONObject user=temp.getJSONObject("user");
                JSONObject item=temp.getJSONObject("item");
                if(item.getString("description").contains(str)) {
                    info.setHead_url(user.getString("head_url"));
                    info.setFirst_pic(item.getString("first_pic"));
                    info.setShare_url(item.getString("video_playurl"));
                    info.setVideo_time(item.getString("video_time"));
                    info.setUpload_time(item.getString("upload_time"));
                    info.setName(user.getString("name"));
                    info.setDescription(item.getString("description"));
                    DATA.add(info);
                }
                else{
                    continue;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return DATA;
    }

    //列表里的item点击事件
    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        public interface OnItemClickListener {
            void onItemClick(View view, int position);

            void onItemLongClick(View view, int position);
        }

        private RecyclerItemClickListener.OnItemClickListener mListener;

        private GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView,RecyclerItemClickListener.OnItemClickListener listener) {
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
}

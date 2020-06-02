package com.example.video_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.baoyz.widget.PullRefreshLayout;
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


public class IndexActivity extends AppCompatActivity {

    private com.baoyz.widget.PullRefreshLayout swipeRefreshLayout;
    private EditText search_text;
    private int screen_width;
    private String page_size="5";
    private String next_offset="1";
    private List<VideoInfo> listVideo;
    public static int nowOffset;
    private String url="https://api.vc.bilibili.com/clip/v1/video/index?page_size="+page_size+"&need_playurl=0&next_offset="+next_offset;
    private String add_url="https://api.vc.bilibili.com/clip/v1/video/index?page_size="+page_size+"&need_playurl=0&next_offset=";
    private ImageView dynamic;
    private ImageView my;
    private String account;
    private String nickName;
    private de.hdodenhof.circleimageview.CircleImageView touxiang;
    private List<IndexListInfo>IndexInfo=new ArrayList<IndexListInfo>();
    private List<IndexListInfo>videoListInfo=new ArrayList<IndexListInfo>();
    private RecyclerView IndexList;
    private IndexAdapter adapter;
    public static Bitmap videoUpPhoto;
    public static Bitmap videoBackground;
    private  VideoInfo video ;
    private Bitmap startImage;
    private Bitmap userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);//沉浸式状态栏
        setContentView(R.layout.activity_index);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null){
            actionbar.hide();
        }
        IndexList = (RecyclerView)findViewById(R.id.indexVideoList);
        LinearLayoutManager manager = new LinearLayoutManager(IndexActivity.this,LinearLayoutManager.VERTICAL,true);
        IndexList.setLayoutManager(manager);
        initData();
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HttpUtil.getVideo(add_url+nowOffset,new okhttp3.Callback() {
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        nowOffset=nowOffset+5;
                        listVideo = parseJson(response.body().string().trim());
                        if (listVideo != null) {
                            Iterator<VideoInfo> iter = listVideo.iterator();
                            while (iter.hasNext()) {
                                video = iter.next();
                                InputStream is;
                                is = new java.net.URL(video.getFirst_pic()).openStream();
                                startImage = toRoundCornerImage(BitmapFactory.decodeStream(is),8);
                                is = new java.net.URL(video.getHead_url()).openStream();
                                userPhoto = BitmapFactory.decodeStream(is);
                                is.close();
                                videoListInfo.add(new IndexListInfo(startImage, userPhoto,video.getDescription(),
                                        video.getName(),video.getUpload_time(), video.getVideo_time(),video.getShare_url()));
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.addList(videoListInfo);
                                    videoListInfo.clear();
                                    swipeRefreshLayout.setRefreshing(false);
                                    IndexList.smoothScrollToPosition(adapter.getItemCount());
                                    Log.d("索引",String.valueOf(nowOffset));
                                    Log.d("视频个数",String.valueOf(adapter.getItemCount()));
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
        });
        IndexList.addOnItemTouchListener(new RecyclerItemClickListener(this, IndexList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                IndexListInfo info = IndexInfo.get(position);
                IndexActivity.videoUpPhoto=info.getUserPhoto();
                IndexActivity.videoBackground=info.getVideoStartImage();
                Intent intent=new Intent(IndexActivity.this,MainActivity.class);
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
        dynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(IndexActivity.this,DynamicActivity.class);
                intent.putExtra("account",account);
                intent.putExtra("nickName",nickName);
                startActivity(intent);
                //finish();
            }
        });
        my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexActivity.this,MyActivity.class));
                //finish();
            }
        });
        search_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(IndexActivity.this,SearchActivity.class);
                intent.putExtra("account",account);
                startActivity(intent);
            }
        });
    }
    private void initData(){
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screen_width = metric.widthPixels;
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);
        touxiang=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.head_picture);
        my=findViewById(R.id.my);
        touxiang.setImageBitmap(MyActivity.bitmap);
        dynamic=(ImageView)findViewById(R.id.dynamic);
        search_text=(EditText)findViewById(R.id.search_text);
        Intent intent=getIntent();
        account=intent.getStringExtra("account");
        nickName=intent.getStringExtra("nickName");
        HttpUtil.getVideo(url,new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                nowOffset=Integer.parseInt(next_offset)+5;
                listVideo = parseJson(response.body().string().trim());
                if (listVideo != null) {
                    Iterator<VideoInfo> iter = listVideo.iterator();
                    while (iter.hasNext()) {
                        video = iter.next();
                        InputStream is;
                        is = new java.net.URL(video.getFirst_pic()).openStream();
                        startImage = toRoundCornerImage(BitmapFactory.decodeStream(is),8);
                        is = new java.net.URL(video.getHead_url()).openStream();
                        userPhoto = BitmapFactory.decodeStream(is);
                        is.close();
                        IndexInfo.add(new IndexListInfo(startImage, userPhoto,video.getDescription(),
                                video.getName(),video.getUpload_time(), video.getVideo_time(),video.getShare_url()));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new IndexAdapter(IndexInfo);
                            IndexList.setAdapter(adapter);
                            IndexList.smoothScrollToPosition(adapter.getItemCount());
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

    public class IndexViewHolder extends RecyclerView.ViewHolder{
        private ImageView videoStartImage;
        private de.hdodenhof.circleimageview.CircleImageView userPhoto;
        private TextView videoTheme;
        private TextView videoAuthorAndTime;
        private TextView videoRunTime;

        public IndexViewHolder(View item){
            super(item);
            videoStartImage=(ImageView)item.findViewById(R.id.videoStartImage);
            userPhoto=(de.hdodenhof.circleimageview.CircleImageView) item.findViewById(R.id.userPhoto);
            videoTheme=(TextView)item.findViewById(R.id.videoTheme);
            videoAuthorAndTime=(TextView)item.findViewById(R.id.videoAuthorAndTime);
            videoRunTime=(TextView)item.findViewById(R.id.videoRunTime);

        }
    }

    public class IndexAdapter extends RecyclerView.Adapter<IndexViewHolder>{
        private List<IndexListInfo> bilibiliIndexInfo;

        public IndexAdapter(List<IndexListInfo> bilibiliIndexInfo) {
            this.bilibiliIndexInfo = bilibiliIndexInfo;
        }

        public void addList(List<IndexListInfo> video) {
            bilibiliIndexInfo.addAll(video);
            notifyItemRangeInserted(adapter.getItemCount()+1,5);
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public IndexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.index_list_item,
                            parent, false);
            IndexViewHolder holder = new IndexViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull IndexViewHolder holder, int position) {
            IndexListInfo info = bilibiliIndexInfo.get(position);
            holder.videoStartImage.setMaxHeight(get_Image_heigth(screen_width,info.getVideoStartImage()));
            holder.videoStartImage.setImageBitmap(info.getVideoStartImage());
            holder.userPhoto.setImageBitmap(info.getUserPhoto());
            holder.videoTheme.setText(info.getVideoTheme());
            holder.videoAuthorAndTime.setText(info.getVideoAuthorAndTime()+info.getUpload_time());
            holder.videoRunTime.setText(info.getVideoRunTime());
        }

        @Override
        public int getItemCount() {
            return bilibiliIndexInfo.size();
        }
    }

    public static void setTranslucentStatus(Activity activity){
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private List<VideoInfo> parseJson(String jsonData){
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
                info.setHead_url(user.getString("head_url"));
                info.setFirst_pic(item.getString("first_pic"));
                info.setShare_url(item.getString("video_playurl"));
                info.setVideo_time(item.getString("video_time"));
                info.setUpload_time(item.getString("upload_time"));
                info.setName(user.getString("name"));
                info.setDescription(item.getString("description"));
                DATA.add(info);
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

    public static int get_Image_heigth(int screen_width, Bitmap bitmap) {
        // 图片的宽度和高度
        int widget_height=0;
        if(bitmap!=null) {
            int image_width = bitmap.getWidth();
            int image_height = bitmap.getHeight();
            widget_height = screen_width * image_height / image_width;
            // height_dip = widget_height * (screen_dpi / 160);

        }
        return widget_height;
    }



    // System.currentTimeMillis() 当前系统的时间
    //第一次按下返回键的事件
    private long firstPressedTime;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstPressedTime < 2000) {
            super.onBackPressed();
        } else {
            Toast.makeText(IndexActivity.this, "再按一次退出APP", Toast.LENGTH_SHORT).show();
            firstPressedTime = System.currentTimeMillis();
        }
    }
}


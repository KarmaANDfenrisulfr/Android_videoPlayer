package com.example.video_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import okhttp3.Call;
import okhttp3.Response;
import static com.example.video_test.DynamicActivity.toRoundCornerImage;
import static com.example.video_test.IndexActivity.setTranslucentStatus;
import static com.example.video_test.ReleaseDynamicActivity.stringToBitmap;

public class HomePageActivity extends AppCompatActivity {

    private List<DynamicListInfo>DynamicInfo;
    private SelfDialog selfDialog;
    private List<Dynamic> Dynamic=new ArrayList<Dynamic>();
    private RecyclerView HomePageList;
    private HomePageAdapter adapter;
    private ImageView back;
    private int screen_width;
    private TextView editInformation;
    private TextView attention;
    private TextView fans;
    private TextView nickName;
    private TextView signature;
    private String name=null;
    private String sign=null;
    private String account=null;
    private String sex=null;
    private String birthday=null;
    private de.hdodenhof.circleimageview.CircleImageView userPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);//沉浸式状态栏
        setContentView(R.layout.activity_home_page);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        HomePageList = (RecyclerView)findViewById(R.id.homePageList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        HomePageList.setLayoutManager(manager);
        init();
        editInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomePageActivity.this,AccountInformationActivity.class);
                intent.putExtra("account",account);
                intent.putExtra("nickName",name);
                intent.putExtra("sex",sex);
                intent.putExtra("birthday",birthday);
                intent.putExtra("signature",sign);
                startActivity(intent);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this,MyActivity.class));
                finish();
            }
        });
        HomePageList.addOnItemTouchListener(new RecyclerItemClickListener(this, HomePageList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, final int position) {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.6f;
                getWindow().setAttributes(lp);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                selfDialog = new SelfDialog(HomePageActivity.this);
                selfDialog.setMessage("侬确定要删除黑历史么？\n~(￣▽￣)~~(￣▽￣)~~(￣▽￣)~");
                selfDialog.setYesOnclickListener("删除", new SelfDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        DynamicListInfo info = DynamicInfo.get(position);
                        String web="deleteDynamic.jsp";
                        HttpUtil.deleteDynamic(web,account,info.getDynamicTime(),new okhttp3.Callback(){
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(HomePageActivity.this,"网络异常，电波无法到达~",Toast.LENGTH_LONG).show();

                                    }
                                });
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                String result=response.body().string().trim();
                                if(result.equals("删除成功")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.removeDynamic(position);
                                            Toast.makeText(HomePageActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(HomePageActivity.this,"出现未知错误，删除失败",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        });
                        selfDialog.dismiss();
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().setAttributes(lp);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    }
                });
                selfDialog.setNoOnclickListener("取消", new SelfDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        selfDialog.dismiss();
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().setAttributes(lp);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    }
                });
                selfDialog.show();
            }
        }));

    }

    private void init(){
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screen_width = metric.widthPixels;
        editInformation=(TextView)findViewById(R.id.editInformation);
        userPhoto=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.userPhoto);
        nickName=(TextView)findViewById(R.id.nickName);
        signature=(TextView)findViewById(R.id.signature);
        attention=(TextView)findViewById(R.id.attention);
        fans=(TextView)findViewById(R.id.fans);
        back=(ImageView)findViewById(R.id.back);
        Intent intent=getIntent();
        userPhoto.setImageBitmap(MyActivity.bitmap);
        name=intent.getStringExtra("nickName");
        sign=intent.getStringExtra("signature");
        if(name!=null&&(!name.equals("null"))){
            nickName.setText(intent.getStringExtra("nickName"));
        }
        if(sign!=null&&(!sign.equals("null"))){
            signature.setText(intent.getStringExtra("signature"));
        }
        attention.setText(intent.getStringExtra("attention"));
        fans.setText(intent.getStringExtra("fans"));
        account=intent.getStringExtra("account");
        sex=intent.getStringExtra("sex");
        birthday=intent.getStringExtra("birthday");
        DynamicInfo =new ArrayList<DynamicListInfo>();
        String web="getPersonDynamic.jsp";
        HttpUtil.getPersonDynamic(web,account,new okhttp3.Callback(){
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HomePageActivity.this, "网络异常，电波无法到达~", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Dynamic=parseJson(response.body().string().trim());//必须用trim()，否则有非法字符
                if (Dynamic!=null) {
                    Iterator<Dynamic> dynamic=Dynamic.iterator();
                    while(dynamic.hasNext()){
                        Dynamic Info=dynamic.next();
                        if(Info.getDynamicImage().length()>0) {
                            Bitmap image= toRoundCornerImage(stringToBitmap(Info.getDynamicImage()), 8);
                            DynamicInfo.add(new DynamicListInfo(image, MyActivity.bitmap, Info.getDynamicAuthor(), Info.getDynamicTime(), Info.getDynamicTheme()));
                        }
                        else{
                            DynamicInfo.add(new DynamicListInfo(MyActivity.bitmap, Info.getDynamicAuthor(), Info.getDynamicTime(), Info.getDynamicTheme()));
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new HomePageAdapter(DynamicInfo);
                            HomePageList.setAdapter(adapter);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(HomePageActivity.this, "无动态或获取动态失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public class HomePageViewHolder extends RecyclerView.ViewHolder{
        private ImageView dynamicImage;
        private de.hdodenhof.circleimageview.CircleImageView userPhoto;
        private TextView dynamicTheme;
        private TextView dynamicAuthor;
        private TextView dynamicTime;

        public HomePageViewHolder(View item){
            super(item);
            dynamicImage=(ImageView)item.findViewById(R.id.dynamicImage);
            userPhoto=(de.hdodenhof.circleimageview.CircleImageView) item.findViewById(R.id.userPhoto);
            dynamicTheme=(TextView)item.findViewById(R.id.dynamicTheme);
            dynamicAuthor=(TextView)item.findViewById(R.id.dynamicAuthor);
            dynamicTime=(TextView)item.findViewById(R.id.dynamicTime);

        }
    }

    public class HomePageAdapter extends RecyclerView.Adapter<HomePageViewHolder>{
        private List<DynamicListInfo> dynamicListInfo;

        public HomePageAdapter(List<DynamicListInfo> dynamicListInfo) {
            this.dynamicListInfo = dynamicListInfo;
        }


        public void removeDynamic(int position) {
            dynamicListInfo.remove(position);
            //删除某个item
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dynamic_list_item,
                            parent, false);
            HomePageViewHolder holder = new HomePageViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final HomePageViewHolder holder, int position) {
            DynamicListInfo info = dynamicListInfo.get(position);
            holder.dynamicImage.setMaxHeight(get_Image_heigth(screen_width,info.getDynamicImage()));
            holder.dynamicImage.setImageBitmap(info.getDynamicImage());
            holder.userPhoto.setImageBitmap(info.getUserPhoto());
            holder.dynamicTheme.setText(info.getDynamicTheme());
            holder.dynamicAuthor.setText(info.getDynamicAuthor());
            holder.dynamicTime.setText(info.getDynamicTime());

        }

        @Override
        public int getItemCount() {
            return dynamicListInfo.size();
        }
    }

    private List<Dynamic> parseJson(String jsonData){
        //初始化解析器
        List<Dynamic> data= new ArrayList<Dynamic>();
        try{
            JSONArray jsonArray=new JSONArray(jsonData);
            int len=jsonArray.length();
            for(int i=0;i<len;i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                Dynamic info=new Dynamic();
                info.setDynamicAuthor(jsonObject.getString("author"));
                info.setDynamicTheme(jsonObject.getString("theme"));
                info.setDynamicImage(jsonObject.getString("image"));
                info.setDynamicTime(jsonObject.getString("uploadtime"));
                data.add(info);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return data;
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

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        public interface OnItemClickListener {
            void onItemClick(View view, int position);

            void onItemLongClick(View view, int position);
        }

        private OnItemClickListener mListener;

        private GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
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

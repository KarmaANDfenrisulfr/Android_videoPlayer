package com.example.video_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
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
import java.util.Iterator;
import java.util.List;
import okhttp3.Call;
import okhttp3.Response;
import static com.example.video_test.IndexActivity.setTranslucentStatus;
import static com.example.video_test.ReleaseDynamicActivity.stringToBitmap;

public class DynamicActivity extends AppCompatActivity {

    private ImageView home;
    private ImageView my;
    private int screen_width;
    private List<DynamicListInfo>DynamicInfo;
    private List<Dynamic> Dynamic=new ArrayList<Dynamic>();
    private RecyclerView DynamicList;
    private DynamicAdapter adapter;
    private String account;
    private String nickName;
    private ImageView edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);//沉浸式状态栏
        setContentView(R.layout.activity_dynamic);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null){
            actionbar.hide();
        }
        initData();
        DynamicList = (RecyclerView)findViewById(R.id.dynamicList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        DynamicList.setLayoutManager(manager);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DynamicActivity.this,IndexActivity.class);
                intent.putExtra("account",account);
                intent.putExtra("nickName",nickName);
                startActivity(intent);
                //finish();
            }
        });
        my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynamicActivity.this,MyActivity.class));
                //finish();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DynamicActivity.this,ReleaseDynamicActivity.class);
                intent.putExtra("account",account);
                intent.putExtra("nickName",nickName);
                startActivity(intent);

            }
        });
    }
    private void initData(){
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screen_width = metric.widthPixels;
        my=(ImageView)findViewById(R.id.my);
        home=(ImageView)findViewById(R.id.home);
        edit=(ImageView)findViewById(R.id.edit);
        Intent intent=getIntent();
        account=intent.getStringExtra("account");
        nickName=intent.getStringExtra("nickName");
        DynamicInfo =new ArrayList<DynamicListInfo>();
        String web="getAttentionDynamic.jsp";
        HttpUtil.getAttentionDynamic(web,account,new okhttp3.Callback(){
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DynamicActivity.this, "网络异常，电波无法到达~", Toast.LENGTH_SHORT).show();
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
                            Bitmap image = toRoundCornerImage(stringToBitmap(Info.getDynamicImage()), 8);
                            Bitmap userPhoto = stringToBitmap(Info.getUserPhoto());
                            DynamicInfo.add(new DynamicListInfo(image, userPhoto, Info.getDynamicAuthor(), Info.getDynamicTime(), Info.getDynamicTheme()));
                        }
                        else{
                            Bitmap userPhoto = stringToBitmap(Info.getUserPhoto());
                            DynamicInfo.add(new DynamicListInfo(userPhoto, Info.getDynamicAuthor(), Info.getDynamicTime(), Info.getDynamicTheme()));
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new DynamicAdapter(DynamicInfo);
                            DynamicList.setAdapter(adapter);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DynamicActivity.this, "无动态或获取动态失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public class DynamicViewHolder extends RecyclerView.ViewHolder{
        private ImageView dynamicImage;
        private de.hdodenhof.circleimageview.CircleImageView userPhoto;
        private TextView dynamicTheme;
        private TextView dynamicAuthor;
        private TextView dynamicTime;

        public DynamicViewHolder(View item){
            super(item);
            dynamicImage=(ImageView)item.findViewById(R.id.dynamicImage);
            userPhoto=(de.hdodenhof.circleimageview.CircleImageView) item.findViewById(R.id.userPhoto);
            dynamicTheme=(TextView)item.findViewById(R.id.dynamicTheme);
            dynamicAuthor=(TextView)item.findViewById(R.id.dynamicAuthor);
            dynamicTime=(TextView)item.findViewById(R.id.dynamicTime);

        }
    }

    public class DynamicAdapter extends RecyclerView.Adapter<DynamicViewHolder>{
        private List<DynamicListInfo> dynamicListInfo;

        public DynamicAdapter(List<DynamicListInfo> dynamicListInfo) {
            this.dynamicListInfo = dynamicListInfo;
        }

        @NonNull
        @Override
        public DynamicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dynamic_list_item,
                            parent, false);
            DynamicViewHolder holder = new DynamicViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull DynamicViewHolder holder, int position) {
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

    /**
     * 获取圆角位图的方法
     *
     * @param bitmap
     *            需要转化成圆角的位图
     * @param pixels
     *            圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    public static Bitmap toRoundCornerImage(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        // 抗锯齿
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
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
                info.setUserPhoto(jsonObject.getString("userPhoto"));
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

    // System.currentTimeMillis() 当前系统的时间
    //第一次按下返回键的事件
    private long firstPressedTime;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstPressedTime < 2000) {
            super.onBackPressed();
        } else {
            Toast.makeText(DynamicActivity.this, "再按一次退出APP", Toast.LENGTH_SHORT).show();
            firstPressedTime = System.currentTimeMillis();
        }
    }
}

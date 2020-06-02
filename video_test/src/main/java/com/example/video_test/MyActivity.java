package com.example.video_test;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Response;
import static com.example.video_test.IndexActivity.setTranslucentStatus;
import static com.example.video_test.ReleaseDynamicActivity.stringToBitmap;

public class MyActivity extends AppCompatActivity {

    public static Bitmap bitmap;
    private String flag=null;
    private TextView nickName;
    private ImageView home;
    private ImageView myHomePage;
    private ImageView dynamic;
    private ImageView fansPath;
    private ImageView attentionPath;
    private de.hdodenhof.circleimageview.CircleImageView userPhoto;
    private LinearLayout homePage;
    private ImageView changePassword;
    private TextView dynamicNumber;
    private TextView attentionNumber;
    private TextView fansNumber;
    private List<String> Lists=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag=start();
        if(flag!=null) {
            setTranslucentStatus(this);//沉浸式状态栏
            setContentView(R.layout.activity_my);
            ActionBar actionbar = getSupportActionBar();
            if (actionbar != null) {
                actionbar.hide();
            }
            init();
            attentionPath.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MyActivity.this,AttentionActivity.class);
                    intent.putExtra("account",flag);
                    intent.putExtra("number",attentionNumber.getText().toString());
                    startActivity(intent);
                    finish();
                }
            });
            fansPath.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MyActivity.this,FansActivity.class);
                    intent.putExtra("account",flag);
                    intent.putExtra("number",fansNumber.getText().toString());
                    startActivity(intent);
                    finish();
                }
            });
            homePage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MyActivity.this,HomePageActivity.class);
                    intent.putExtra("account",flag);
                    intent.putExtra("nickName",Lists.get(5));
                    intent.putExtra("sex",Lists.get(6));
                    intent.putExtra("birthday",Lists.get(7));
                    intent.putExtra("signature",Lists.get(8));
                    intent.putExtra("attention",attentionNumber.getText().toString());
                    intent.putExtra("fans",fansNumber.getText().toString());
                    startActivity(intent);
                    finish();
                }
            });
            myHomePage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MyActivity.this,HomePageActivity.class);
                    intent.putExtra("account",flag);
                    intent.putExtra("nickName",Lists.get(5));
                    intent.putExtra("sex",Lists.get(6));
                    intent.putExtra("birthday",Lists.get(7));
                    intent.putExtra("signature",Lists.get(8));
                    intent.putExtra("attention",attentionNumber.getText().toString());
                    intent.putExtra("fans",fansNumber.getText().toString());
                    startActivity(intent);
                    finish();
                }
            });
            changePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MyActivity.this,ChangePasswordActivity.class);
                    intent.putExtra("account",flag);
                    startActivity(intent);
                    finish();
                }
            });
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyActivity.bitmap=((BitmapDrawable)userPhoto.getDrawable()).getBitmap();
                    Intent intent=new Intent(MyActivity.this,IndexActivity.class);
                    intent.putExtra("account",flag);
                    intent.putExtra("nickName",nickName.getText().toString());
                    startActivity(intent);
                    //finish();
                }
            });
            dynamic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyActivity.bitmap=((BitmapDrawable)userPhoto.getDrawable()).getBitmap();
                    Intent intent=new Intent(MyActivity.this,DynamicActivity.class);
                    intent.putExtra("account",flag);
                    intent.putExtra("nickName",nickName.getText().toString());
                    startActivity(intent);
                    //finish();
                }
            });
        }
        else{
            startActivity(new Intent(MyActivity.this, LoginActivity.class));
            finish();
        }


    }

    private void init(){
        changePassword=(ImageView)findViewById(R.id.changePassword);
        myHomePage=(ImageView)findViewById(R.id.myHomePage);
        fansPath=(ImageView)findViewById(R.id.fansPath);
        attentionPath=(ImageView)findViewById(R.id.attentionPath);
        homePage=(LinearLayout)findViewById(R.id.homePage);
        dynamicNumber=(TextView)findViewById(R.id.dynamicNumber);
        attentionNumber=(TextView)findViewById(R.id.attentionNumber);
        fansNumber=(TextView)findViewById(R.id.fansNumber);
        userPhoto=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.userPhoto);
        nickName=(TextView)findViewById(R.id.nickName);
        home=(ImageView)findViewById(R.id.home);
        dynamic=(ImageView)findViewById(R.id.dynamic);
        String web="getMyActivityNeedNumber.jsp";
        HttpUtil.getMyActivityNeedNumber(web,flag,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Lists=parseJson(response.body().string().trim());//必须用trim()，否则有非法字符
                if (Lists.get(0).equals("true")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dynamicNumber.setText(Lists.get(1).toString());
                            attentionNumber.setText(Lists.get(2).toString());
                            fansNumber.setText(Lists.get(3).toString());
                            Bitmap bitmap=stringToBitmap(Lists.get(4));
                            userPhoto.setImageDrawable(null);
                            userPhoto.setImageBitmap(bitmap);
                            MyActivity.bitmap=bitmap;
                            if(Lists.get(5)!=null&&!(Lists.get(5).equals("null"))){
                                nickName.setText(Lists.get(5));
                            }

                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyActivity.this, "发生未知错误,获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call arg0, IOException e) {
                // TODO Auto-generated method stub
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyActivity.this, "网络异常，电波无法到达~", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //bitmap=DrawableToBitmap(userPhoto.getDrawable());
    }

    private String start(){
        String flag=null;
        StringBuilder builder=new StringBuilder();
        FileInputStream in=null;
        BufferedReader reader=null;
        try{
            in=openFileInput("data");
            reader=new BufferedReader(new InputStreamReader(in));
            String line=reader.readLine();
            if(line!=null){
                flag=line;
            }

        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(reader!=null) {
                try {
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    private List<String> parseJson(String jsonData){
        //初始化解析器
        List<String> data= new ArrayList<String>();
        try{
            JSONArray jsonArray=new JSONArray(jsonData);
            int len=jsonArray.length();
            for(int i=0;i<len;i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                data.add(jsonObject.getString("flag"));
                data.add(jsonObject.getString("dynamic"));
                data.add(jsonObject.getString("attention"));
                data.add(jsonObject.getString("fans"));
                data.add(jsonObject.getString("userPhoto"));
                data.add(jsonObject.getString("nickName"));
                data.add(jsonObject.getString("sex"));
                data.add(jsonObject.getString("birthday"));
                data.add(jsonObject.getString("signature"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return data;
    }

    // System.currentTimeMillis() 当前系统的时间
    //第一次按下返回键的事件
    private long firstPressedTime;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstPressedTime < 2000) {
            super.onBackPressed();
        } else {
            Toast.makeText(MyActivity.this, "再按一次退出APP", Toast.LENGTH_SHORT).show();
            firstPressedTime = System.currentTimeMillis();
        }
    }

}

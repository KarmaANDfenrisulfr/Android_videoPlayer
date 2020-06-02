package com.example.video_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import static com.example.video_test.IndexActivity.setTranslucentStatus;


public class MainActivity extends AppCompatActivity {

    private VideoView videoView ;
    private TextView theme;
    private ImageView userPhoto;
    private TextView upload_time;
    private TextView name;
    private LinearLayout test;
    private TextView exit;
    private int width2;
    private int height2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);//沉浸式状态栏
        setContentView(R.layout.activity_main);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null){
            actionbar.hide();
        }
        init();
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init(){
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        width2 = outMetrics.widthPixels;
        height2 = outMetrics.heightPixels;
        test=(LinearLayout)findViewById(R.id.test);
        exit=(TextView)findViewById(R.id.exit);
        theme=(TextView)findViewById(R.id.theme);
        videoView = (VideoView)findViewById(R.id.videoView);
        userPhoto=(ImageView)findViewById(R.id.userPhoto);
        upload_time=(TextView)findViewById(R.id.upload_time);
        name=(TextView)findViewById(R.id.author);
        userPhoto.setImageBitmap(IndexActivity.videoUpPhoto);
        Intent intent=getIntent();
        theme.setText(intent.getStringExtra("theme"));
        upload_time.setText(intent.getStringExtra("upload_time"));
        name.setText(intent.getStringExtra("name"));
        videoView.setMediaController(new MediaController(this));
        videoView.setOnCompletionListener( new MyPlayerOnCompletionListener());
        videoView.setVideoURI(Uri.parse(intent.getStringExtra("video_url")));
        videoView.start();
    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {
        //自定义播放完成后做的事
        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText( MainActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) videoView
               .getLayoutParams(); // 取控当前的布局参数
            layoutParams.height = width2;//设置当控件的高强
            layoutParams.width = height2;
            test.setLayoutParams(layoutParams); // 使设置好的布局参数应用到控件
        }else{
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) videoView
                    .getLayoutParams(); // 取控当前的布局参数
            layoutParams.height =620;//设置当控件的高强
            layoutParams.width = width2;
            test.setLayoutParams(layoutParams); // 使设置好的布局参数应用到控件
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoView.stopPlayback();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        videoView.stopPlayback();
    }
}

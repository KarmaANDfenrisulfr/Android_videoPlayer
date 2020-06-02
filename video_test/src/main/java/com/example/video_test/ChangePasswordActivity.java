package com.example.video_test;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Call;
import okhttp3.Response;

import static com.example.video_test.IndexActivity.setTranslucentStatus;

public class ChangePasswordActivity extends AppCompatActivity {

    private ImageView seeBeforePassword;
    private ImageView seeNewPassword;
    private ImageView seeAckPassword;
    private ImageView back;
    private EditText beforePassword;
    private EditText newPassword;
    private EditText ackPassword;
    private Button changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);//沉浸式状态栏
        setContentView(R.layout.activity_change_password);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null){
            actionbar.hide();
        }
        initData();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangePasswordActivity.this, MyActivity.class));
                finish();
            }
        });
        seeBeforePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seeBeforePassword.getTag().equals("noSee")){
                    seeBeforePassword.setTag("See");
                    beforePassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    beforePassword.setSelection(beforePassword.getText().length());
                    seeBeforePassword.setBackgroundResource(R.mipmap.cansee);
                }
                else{
                    seeBeforePassword.setTag("noSee");
                    beforePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    beforePassword.setSelection(beforePassword.getText().length());
                    seeBeforePassword.setBackgroundResource(R.mipmap.cantsee);
                }
            }
        });
        seeNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seeNewPassword.getTag().equals("noSee")){
                    seeNewPassword.setTag("See");
                    newPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    newPassword.setSelection(newPassword.getText().length());
                    seeNewPassword.setBackgroundResource(R.mipmap.cansee);
                }
                else{
                    seeNewPassword.setTag("noSee");
                    newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    newPassword.setSelection(newPassword.getText().length());
                    seeNewPassword.setBackgroundResource(R.mipmap.cantsee);
                }
            }
        });
        seeAckPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seeAckPassword.getTag().equals("noSee")){
                    seeAckPassword.setTag("See");
                    ackPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ackPassword.setSelection(ackPassword.getText().length());
                    seeAckPassword.setBackgroundResource(R.mipmap.cansee);
                }
                else{
                    seeAckPassword.setTag("noSee");
                    ackPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ackPassword.setSelection(ackPassword.getText().length());
                    seeAckPassword.setBackgroundResource(R.mipmap.cantsee);
                }
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String format_password="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
                if(!(isMatches(newPassword.getText().toString(),format_password))){
                    Toast.makeText(ChangePasswordActivity.this,"输入的新密码格式不正确",Toast.LENGTH_SHORT).show();
                }
                else if(beforePassword.getText().toString().equals(newPassword.getText().toString())){
                    Toast.makeText(ChangePasswordActivity.this,"新密码不能与原密码一致",Toast.LENGTH_SHORT).show();
                }
                else if(!(ackPassword.getText().toString().equals(newPassword.getText().toString()))){
                    Toast.makeText(ChangePasswordActivity.this,"确认密码与新密码不一致",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent=getIntent();
                    String account=intent.getStringExtra("account");
                    String web="changePassword.jsp";
                    HttpUtil.changePassword(web,account,newPassword.getText().toString(),new okhttp3.Callback(){
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String result=response.body().string().trim();
                            if(result.equals("修改成功")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                });
                            }
                            else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ChangePasswordActivity.this, "发生未知错误，修改失败", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(ChangePasswordActivity.this,"网络异常,电波无法到达~",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }
        });
    }
    private void initData(){
        seeBeforePassword=(ImageView)findViewById(R.id.seeBeforePassword);
        seeNewPassword=(ImageView)findViewById(R.id.seeNewPassword);
        seeAckPassword=(ImageView)findViewById(R.id.seeAckPassword);
        beforePassword=(EditText)findViewById(R.id.beforePassword);
        newPassword=(EditText)findViewById(R.id.newPassword);
        ackPassword=(EditText)findViewById(R.id.ackPassword);
        changePassword=(Button)findViewById(R.id.changePassword);
        back=(ImageView)findViewById(R.id.back);
    }

    private static boolean isMatches(String text, String format) {
        //判断正则表达式
        Pattern pattern = Pattern.compile(format);
        Matcher m = pattern.matcher(text);
        return m.matches();
    }
}

package com.example.video_test;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import okhttp3.Call;
import okhttp3.Response;

import static com.example.video_test.IndexActivity.setTranslucentStatus;

public class LoginActivity extends AppCompatActivity {

    private TextView text1;
    private TextView text2;
    private EditText account;
    private EditText password;
    private Button login;
    private Button registered;
    private ImageView bilibili;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);//沉浸式状态栏
        setContentView(R.layout.activity_login);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null){
            actionbar.hide();
        }
        initData();
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    bilibili.setBackgroundResource(R.mipmap.login2);
                    password.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if((password.getText().toString().length()>5)&&(account.getText().toString().length()!=0)){
                                login.setBackgroundColor(0xffE77596);
                                login.setEnabled(true);
                            }
                            else{
                                login.setBackgroundColor(0xaaFCAAC1);
                                login.setEnabled(false);
                            }
                        }
                    });

                }
                else{
                    bilibili.setBackgroundResource(R.mipmap.login1);
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String web="login.jsp";
                    HttpUtil.login(web, account.getText().toString(), password.getText().toString(), new okhttp3.Callback() {
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String result = response.body().string().trim();//必须用trim()，否则有非法字符
                            if (result.equals("登录成功")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        FileOutputStream out = null;
                                        BufferedWriter writer = null;
                                        try {
                                            out = openFileOutput("data", Context.MODE_PRIVATE);
                                            writer = new BufferedWriter(new OutputStreamWriter(out));
                                            writer.write(account.getText().toString());
                                            writer.flush();
                                            writer.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, MyActivity.class));
                                        finish();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "密码与用户名不匹配或用户不存在", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(LoginActivity.this, "网络异常，电波无法到达~", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
            }
        });
        registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisteredActivity.class));
                finish();
            }
        });
    }
    private void initData(){
        text1=(TextView)findViewById(R.id.text1);
        text2=(TextView)findViewById(R.id.text2);
        account=(EditText)findViewById(R.id.account);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);
        registered=(Button)findViewById(R.id.registered);
        bilibili=(ImageView)findViewById(R.id.bilibili);
        String str1=String.format("登录即代表你同意<font color=\"#E77596\">%s</font>", "用户协议")
                +String.format("%s","和")
                +String.format("<font color=\"#E77596\">%s</font>","隐私政策");
        String str2=String.format("遇到问题?<font color=\"#E77596\">%s</font>", "查看帮助");
        text1.setText(Html.fromHtml(str1));
        text2.setText(Html.fromHtml(str2));
    }
}

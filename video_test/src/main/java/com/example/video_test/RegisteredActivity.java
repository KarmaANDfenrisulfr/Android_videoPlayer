package com.example.video_test;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Call;
import okhttp3.Response;

import static com.example.video_test.IndexActivity.setTranslucentStatus;
import static com.example.video_test.ReleaseDynamicActivity.convertIconToString;

public class RegisteredActivity extends AppCompatActivity {

    private TextView text1;
    private TextView text2;
    private TextView text3;
    private EditText account;
    private EditText password;
    private Button registered;
    private ImageView bilibili;
    private ImageView seePassword;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);//沉浸式状态栏
        setContentView(R.layout.activity_registered);
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
                }
                else{
                    bilibili.setBackgroundResource(R.mipmap.login1);
                }
            }
        });
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
                    registered.setBackgroundColor(0xffE77596);
                    registered.setEnabled(true);
                }
                else{
                    registered.setBackgroundColor(0xaaFCAAC1);
                    registered.setEnabled(false);
                }
            }
        });
        registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String format_account="^(0|86|17951)?1[0-9]{10}";
                String format_password="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
                if(!(isMatches(account.getText().toString(),format_account))){
                    account.setText("");
                    Toast.makeText(RegisteredActivity.this,"输入的手机号格式不正确",Toast.LENGTH_SHORT).show();
                }
                if(!(isMatches(password.getText().toString(),format_password))){
                    password.setText("");
                    Toast.makeText(RegisteredActivity.this,"输入的密码格式不正确",Toast.LENGTH_SHORT).show();
                }
                if((password.getText().toString().length()!=0)&&(account.getText().toString().length()!=0)){
                    String web="registered.jsp";
                    String userPhoto=convertIconToString(getDrawable(R.mipmap.myhome));
                    HttpUtil.registered(web,account.getText().toString(),password.getText().toString(),userPhoto,new okhttp3.Callback() {
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String result=response.body().string().trim();
                            if(result.equals("注册成功")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisteredActivity.this, result, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisteredActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                });
                            }
                            else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisteredActivity.this, "此账号已被注册，亲", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(RegisteredActivity.this,"网络异常，电波无法到达~",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                }

            }
        });
        seePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seePassword.getTag().equals("noSee")){
                    seePassword.setTag("See");
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password.setSelection(password.getText().length());
                    seePassword.setBackgroundResource(R.mipmap.cansee);
                }
                else{
                    seePassword.setTag("noSee");
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setSelection(password.getText().length());
                    seePassword.setBackgroundResource(R.mipmap.cantsee);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisteredActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
    private void initData(){
        text1=(TextView)findViewById(R.id.text1);
        text2=(TextView)findViewById(R.id.text2);
        text3=(TextView)findViewById(R.id.text3);
        account=(EditText)findViewById(R.id.account);
        password=(EditText)findViewById(R.id.password);
        registered=(Button)findViewById(R.id.registered);
        bilibili=(ImageView)findViewById(R.id.bilibili);
        seePassword=(ImageView)findViewById(R.id.seePassword);
        back=(ImageView)findViewById(R.id.back);
        String str1="未注册过哔哩哔哩的手机号，我们将自动帮你注册账号";
        String str2=String.format("登录或完成注册即代表你同意<font color=\"#E77596\">%s</font>", "用户协议")
                +String.format("%s","和")
                +String.format("<font color=\"#E77596\">%s</font>","隐私政策");
        String str3=String.format("遇到问题?<font color=\"#E77596\">%s</font>", "查看帮助");
        text1.setText(str1);
        text2.setText(Html.fromHtml(str2));
        text3.setText(Html.fromHtml(str3));
    }
    private static boolean isMatches(String text, String format) {
        //判断正则表达式
        Pattern pattern = Pattern.compile(format);
        Matcher m = pattern.matcher(text);
        return m.matches();
    }

}

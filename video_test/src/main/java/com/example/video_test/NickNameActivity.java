package com.example.video_test;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.video_test.IndexActivity.setTranslucentStatus;

public class NickNameActivity extends AppCompatActivity {

    private EditText newNickName;
    private TextView save;
    private TextView textExtraNumber;
    private String oldNickName=null;
    private String signature=null;
    private String account=null;
    private String sex=null;
    private String birthday=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);//沉浸式状态栏
        setContentView(R.layout.activity_nick_name);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        init();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newNickName.getText().toString()!=null&&(!newNickName.getText().toString().equals("null"))) {
                    Intent intent = new Intent();
                    intent.putExtra("newNickName", newNickName.getText().toString());
                    intent.putExtra("account", account);
                    intent.putExtra("sex", sex);
                    intent.putExtra("birthday", birthday);
                    intent.putExtra("signature", signature);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else{
                    newNickName.setText("");
                    Toast.makeText(NickNameActivity.this,"昵称不能设置为null",Toast.LENGTH_SHORT).show();
                }
            }
        });
        newNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = 8 - s.length();
                textExtraNumber.setText(String.valueOf(i));
            }
        });
    }

    private void init(){
        newNickName=(EditText)findViewById(R.id.newNickName);
        save=(TextView)findViewById(R.id.save);
        textExtraNumber=(TextView)findViewById(R.id.textExtraNumber);
        Intent intent=getIntent();
        account=intent.getStringExtra("account");
        sex=intent.getStringExtra("sex");
        birthday=intent.getStringExtra("birthday");
        signature=intent.getStringExtra("signature");
        oldNickName=intent.getStringExtra("oldNickName");
        newNickName.setText(oldNickName);
        newNickName.setSelection(newNickName.getText().length());
    }
}

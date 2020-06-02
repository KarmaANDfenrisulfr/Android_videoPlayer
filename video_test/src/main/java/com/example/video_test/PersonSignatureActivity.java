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

public class PersonSignatureActivity extends AppCompatActivity {

    private EditText newSignature;
    private TextView save;
    private TextView theme;
    private TextView textExtraNumber;
    private String oldSignature;
    private String nickName=null;
    private String account=null;
    private String sex=null;
    private String birthday=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);//沉浸式状态栏
        setContentView(R.layout.activity_person_signature);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        init();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newSignature.getText().toString()!=null&&(!newSignature.getText().toString().equals("null"))) {
                    Intent intent = new Intent();
                    intent.putExtra("newSignature", newSignature.getText().toString());
                    intent.putExtra("account", account);
                    intent.putExtra("sex", sex);
                    intent.putExtra("birthday", birthday);
                    intent.putExtra("nickName", nickName);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else{
                    newSignature.setText("");
                    Toast.makeText(PersonSignatureActivity.this,"个性签名不能设置为null",Toast.LENGTH_SHORT).show();
                }
            }
        });
        newSignature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = 16 - s.length();
                textExtraNumber.setText(String.valueOf(i));
            }
        });
    }

    private void init(){
        newSignature=(EditText)findViewById(R.id.newSignature);
        save=(TextView)findViewById(R.id.save);
        theme=(TextView)findViewById(R.id.theme);
        textExtraNumber=(TextView)findViewById(R.id.textExtraNumber);
        theme.setText("个性签名");
        Intent intent=getIntent();
        account=intent.getStringExtra("account");
        sex=intent.getStringExtra("sex");
        birthday=intent.getStringExtra("birthday");
        nickName=intent.getStringExtra("nickName");
        oldSignature=intent.getStringExtra("oldSignature");
        newSignature.setText(oldSignature);
        newSignature.setSelection(newSignature.getText().length());
    }
}

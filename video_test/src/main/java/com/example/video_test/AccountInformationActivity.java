package com.example.video_test;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import okhttp3.Call;
import okhttp3.Response;

import static com.example.video_test.IndexActivity.setTranslucentStatus;
import static com.example.video_test.ReleaseDynamicActivity.convertIconToString;


public class AccountInformationActivity extends AppCompatActivity {

    private AlertDialog alertDialog;
    private SelfDialog selfDialog;
    public static final int choose_image = 2;
    public static final int change_nickName = 3;
    private static final int change_signature=4;
    private static int degree = 0;
    private TextView sex;
    private TextView nickName;
    private TextView signature;
    private TextView birthday;
    private ImageView back;
    private ImageView sexClick;
    private ImageView nickNameClick;
    private ImageView signatureClick;
    private ImageView birthdayClick;
    private TextView logout;
    private String account=null;
    private String name=null;
    private String sexual=null;
    private String birth=null;
    private String sign=null;
    private de.hdodenhof.circleimageview.CircleImageView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);//沉浸式状态栏
        setContentView(R.layout.activity_account_information);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        init();
        sexClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = {"男", "女", "保密"};
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AccountInformationActivity.this);
                alertBuilder.setTitle("请选择性别");
                alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sex.setText(items[i]);
                        alertDialog.dismiss();
                    }
                });
                alertDialog = alertBuilder.create();
                alertDialog.show();
            }
        });

        birthdayClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AccountInformationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AccountInformationActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });

        nickNameClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent=new Intent(AccountInformationActivity.this,NickNameActivity.class);
                 intent.putExtra("oldNickName",nickName.getText().toString());
                 intent.putExtra("account",account);
                 intent.putExtra("sex",sex.getText().toString());
                 intent.putExtra("birthday",birthday.getText().toString());
                 intent.putExtra("signature",signature.getText().toString());
                 startActivityForResult(intent,change_nickName);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.6f;
                getWindow().setAttributes(lp);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                selfDialog = new SelfDialog(AccountInformationActivity.this);
                selfDialog.setMessage("侬确定不是手滑了么？\n(＇￣д￣)八(￣д￣)八(￣д￣＇)");
                selfDialog.setYesOnclickListener("注销", new SelfDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        IndexActivity.nowOffset=0;
                        FileOutputStream out = null;
                        BufferedWriter writer = null;
                        try {
                            out = openFileOutput("data", Context.MODE_PRIVATE);
                            writer = new BufferedWriter(new OutputStreamWriter(out));
                            writer.write("");
                            writer.flush();
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        selfDialog.dismiss();
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().setAttributes(lp);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        startActivity(new Intent(AccountInformationActivity.this,LoginActivity.class));
                        finish();
                    }
                });
                selfDialog.setNoOnclickListener("我手滑了", new SelfDialog.onNoOnclickListener() {
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
        });
        signatureClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AccountInformationActivity.this,PersonSignatureActivity.class);
                intent.putExtra("oldSignature",signature.getText().toString());
                intent.putExtra("account",account);
                intent.putExtra("nickName",nickName.getText().toString());
                intent.putExtra("sex",sex.getText().toString());
                intent.putExtra("birthday",birthday.getText().toString());
                startActivityForResult(intent,change_signature);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String web1="setAccountInformation.jsp";
                    String userPhoto=convertIconToString(header.getDrawable());
                    HttpUtil.setAccountInformation(web1,account,userPhoto,nickName.getText().toString(),sex.getText().toString(),birthday.getText().toString()
                            ,signature.getText().toString(),new okhttp3.Callback(){
                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    final String result = response.body().string().trim();
                                    if (result.equals("编辑成功")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(AccountInformationActivity.this, "编辑信息成功", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(AccountInformationActivity.this,MyActivity.class));
                                            }
                                        });
                                    }
                                    else{
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(AccountInformationActivity.this, "编辑信息失败", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(AccountInformationActivity.this,MyActivity.class));
                                            }
                                        });
                                    }
                                }
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(AccountInformationActivity.this,"网络异常，电波无法到达~",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(AccountInformationActivity.this,MyActivity.class));
                                        }
                                    });
                                }
                            });

                }
        });
    }

    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(AccountInformationActivity.this,R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                birthday.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void init(){
        logout=(TextView)findViewById(R.id.logout);
        back=(ImageView)findViewById(R.id.back);
        sex=(TextView)findViewById(R.id.sex);
        birthday=(TextView)findViewById(R.id.birthday);
        header=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.header);
        nickName=(TextView)findViewById(R.id.nickName);
        signature=(TextView)findViewById(R.id.signature);
        nickNameClick=(ImageView)findViewById(R.id.nickNameClick);
        sexClick=(ImageView)findViewById(R.id.sexClick);
        birthdayClick=(ImageView)findViewById(R.id.birthdayClick);
        signatureClick=(ImageView)findViewById(R.id.signatureClick);
        header.setImageBitmap(MyActivity.bitmap);
        Intent intent=getIntent();
        account=intent.getStringExtra("account");
        name=intent.getStringExtra("nickName");
        sexual=intent.getStringExtra("sex");
        birth=intent.getStringExtra("birthday");
        sign=intent.getStringExtra("signature");
       if(name!=null&&(!name.equals("null"))){
            nickName.setText(name);
        }
        if(name!=null&&(!sexual.equals("null"))){
            sex.setText(sexual);
        }
        if(name!=null&&(!birth.equals("null"))){
            birthday.setText(birth);
        }
        if(sign!=null&&(!sign.equals("null"))){
            signature.setText(sign);
        }
    }

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, choose_image);//打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "你怎么不给权限呢？！亲！！！", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                //默认操作
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case choose_image:
                if (resultCode == RESULT_OK && data != null) {
                    try {
                        Bitmap bitmap=adjustImageXY(data);
                        MyActivity.bitmap=bitmap;
                        //Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        header.setImageDrawable(null);
                        header.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case change_nickName:
                if(resultCode==RESULT_OK){
                    //修改后的昵称
                    nickName.setText(data.getStringExtra("newNickName"));
                    header.setImageBitmap(MyActivity.bitmap);
                    sex.setText(data.getStringExtra("sex"));
                    account=data.getStringExtra("account");
                    signature.setText(data.getStringExtra("signature"));
                    birthday.setText(data.getStringExtra("birthday"));
                }
                    break;
            case change_signature:
                if(resultCode==RESULT_OK){
                    //修改后的个性签名
                    signature.setText(data.getStringExtra("newSignature"));
                    header.setImageBitmap(MyActivity.bitmap);
                    sex.setText(data.getStringExtra("sex"));
                    account=data.getStringExtra("account");
                    nickName.setText(data.getStringExtra("nickName"));
                    birthday.setText(data.getStringExtra("birthday"));
                }
                break;
            default:
                break;
        }
    }

    private String handleVideoOnKitKat(Intent data) {
        //处理选中的视频
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagesPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagesPath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagesPath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri，直接获取视频路径即可
            imagePath = uri.getPath();
        }

        return imagePath;
    }

    private String getImagesPath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private Bitmap adjustImageXY(Intent data) {
        String imagePath = handleVideoOnKitKat(data);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        ExifInterface exif;
        try {
            exif = new ExifInterface(imagePath);
            if (exif != null) {
                // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default:
                        degree = 0;
                        break;
                }
            }
            if (degree != 0) {
                // 旋转图片
                Matrix m = new Matrix();
                m.postRotate(degree);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), m, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}

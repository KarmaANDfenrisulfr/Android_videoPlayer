package com.example.video_test;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import okhttp3.Call;
import okhttp3.Response;
import static com.example.video_test.IndexActivity.setTranslucentStatus;

public class ReleaseDynamicActivity extends AppCompatActivity {

    private int flag=0;
    private EditText dynamicConnent;
    private TextView textExtraNumber;
    private ImageView photo;
    private ImageView back;
    private String account;
    private String nickName;
    public static final int choose_image = 2;
    private static int degree = 0;
    private TextView push;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);//沉浸式状态栏
        setContentView(R.layout.activity_release_dynamic);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        init();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dynamicConnent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = 233 - s.length();
                textExtraNumber.setText(String.valueOf(i));
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ReleaseDynamicActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ReleaseDynamicActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });
        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReleaseDynamicActivity.this,"正在传输",Toast.LENGTH_SHORT).show();
                String theme=dynamicConnent.getText().toString();
                String userPhoto=convertIconToString(new BitmapDrawable(getResources(),MyActivity.bitmap));
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time=format.format(new Date());
                String web1="pushDynamic.jsp";
                if(flag!=0) {
                    String image=convertIconToString(photo.getDrawable());
                    HttpUtil.pushDynamic(web1, account, userPhoto, nickName, theme, image, time, new okhttp3.Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ReleaseDynamicActivity.this, "网络异常，电波无法到达~", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            final String result = response.body().string().trim();//必须用trim()，否则有非法字符
                            if (result.equals("发布成功")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ReleaseDynamicActivity.this, "发布信息成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ReleaseDynamicActivity.this, "发布信息失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
                else{
                    String image="";
                    HttpUtil.pushDynamic(web1, account, userPhoto, nickName, theme, image, time, new okhttp3.Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ReleaseDynamicActivity.this, "网络异常，电波无法到达~", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            final String result = response.body().string().trim();//必须用trim()，否则有非法字符
                            if (result.equals("发布成功")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ReleaseDynamicActivity.this, "发布信息成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ReleaseDynamicActivity.this, "发布信息失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }

            }
        });
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
                    flag=1;
                    Bitmap bitmap=adjustImageXY(data);
                    photo.setBackground(null);
                    photo.setImageBitmap(bitmap);
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

    private void init() {
        dynamicConnent = (EditText) findViewById(R.id.dynamicConnent);
        textExtraNumber = (TextView) findViewById(R.id.textExtraNumber);
        photo = (ImageView) findViewById(R.id.photo);
        push=(TextView)findViewById(R.id.push);
        back=(ImageView)findViewById(R.id.back);
        Intent intent=getIntent();
        account=intent.getStringExtra("account");
        nickName=intent.getStringExtra("nickName");
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

    public static String convertIconToString(Drawable drawable) {
//        String string = null;
        Bitmap bitmap =((BitmapDrawable) drawable).getBitmap() ;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
       /* byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);*/
        int options = 100;
        while (baos.toByteArray().length / 1024 > 1000) { //循环判断如果压缩后图片是否大于1000kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            Log.i("Compress", baos.toByteArray().length + "");
        }
        byte[] bytes = baos.toByteArray();

//        string = Base64.encode(bytes);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string,Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}

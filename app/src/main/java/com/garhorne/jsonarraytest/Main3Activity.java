package com.garhorne.jsonarraytest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main3Activity extends AppCompatActivity {

    private ImageView iv_goods;
    private TextView tv_goods;

    private Handler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        iv_goods = (ImageView)findViewById(R.id.iv_goods);
        tv_goods = (TextView)findViewById(R.id.tv_goods);

        Goods goods = (Goods) getIntent().getParcelableExtra("good_data");
        tv_goods.setText(goods.getGoodName());

        getBitmapFromServer(goods);
    }

    private void getBitmapFromServer(final Goods goods) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                StringBuilder builder = new StringBuilder();
                Message msg = new Message();
                try {
                    URL url = new URL(Constant.PICTURE_URL + "?pictureName=" + goods.getGoodName());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(80000);
                    connection.setConnectTimeout(80000);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    connection.connect();

                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while((line = reader.readLine()) != null){
                        builder.append(line);
                    }

                    String str = builder.toString();
                    JSONObject object = new JSONObject(str.substring(str.indexOf("{")));
                    String resCode = object.getString("resCode");
                    Log.d("TAG",resCode);

                    if (resCode.equals("100")){
                        byte[] picture = Base64.decode(object.getString("picture"),Base64.DEFAULT);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = 1;
                        bitmap = BitmapFactory.decodeByteArray(picture,0,picture.length);
                        msg.obj = bitmap;
                        msg.what = 110;
                    } else {
                        msg.what = 250;
                    }
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    class MyHandler extends Handler {
        WeakReference<Main3Activity> mactivity;

        public MyHandler(Main3Activity activity) {
            mactivity = new WeakReference<Main3Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 110:
                    iv_goods.setImageBitmap((Bitmap) msg.obj);
                    break;
                case 250:
                    Toast.makeText(Main3Activity.this,"图片获取失败",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}

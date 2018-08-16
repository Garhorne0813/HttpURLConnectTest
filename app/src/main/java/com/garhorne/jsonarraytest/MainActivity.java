package com.garhorne.jsonarraytest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_goodName;
    private EditText et_goodPrice;
    private EditText et_goodDetail;
    private Button btn_post;
    private Button btn_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        et_goodName = findViewById(R.id.et_goodName);
        et_goodPrice = findViewById(R.id.et_goodPrice);
        et_goodDetail = findViewById(R.id.et_goodDetail);
        btn_post = findViewById(R.id.btn_post);
        btn_query = findViewById(R.id.btn_query);

        btn_post.setOnClickListener(this);
        btn_query.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_post:
                String name = et_goodName.getText().toString();
                String detail = et_goodDetail.getText().toString();
                String str_price = et_goodPrice.getText().toString();
                if (!name.isEmpty() && !detail.isEmpty() && !str_price.isEmpty()){
                    double price = Double.valueOf(str_price);
                    if (price < 0){
                        et_goodPrice.setText("价格不能为负值，请重新输入");
                    }else{
                        Goods goods = new Goods();
                        goods.setGoodName(name);
                        goods.setDetail(detail);
                        goods.setGoodPrice(price);
                        new MyAsyncTask(goods).execute(Constant.JSON_GOODS_URL);
                    }
                } else {
                    Toast.makeText(this,"名称、价格和详情都不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_query:
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private class MyAsyncTask extends AsyncTask<String,Integer,String>{

        Goods goods = null;

        public MyAsyncTask(Goods g) {
            this.goods = g;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {

            StringBuilder builder = new StringBuilder();
            try{
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(80000);
                connection.setReadTimeout(80000);
                connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                connection.connect();

                JSONObject object = new JSONObject();
                object.put("goodName",goods.getGoodName());
                object.put("goodPrice",goods.getGoodPrice());
                object.put("goodDetail",goods.getDetail());

                OutputStream out = connection.getOutputStream();
                out.write(object.toString().getBytes("utf-8"));
                out.flush();
                out.close();

                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null){
                    builder.append(line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return builder.toString();
        }

    }
}

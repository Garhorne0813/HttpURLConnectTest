package com.garhorne.jsonarraytest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private ListView listView;

    private SwipeRefreshLayout refreshLayout;

    private List<Goods> list;

    private GoodsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        list = new ArrayList<Goods>();

        listView = (ListView)findViewById(R.id.listview);
        adapter = new GoodsAdapter(Main2Activity.this,R.layout.list_item,list);
        refreshGoods();
        listView.setAdapter(adapter);

        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshGoods();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Goods goods = list.get(i);
                Toast.makeText(Main2Activity.this,goods.getGoodName() + "",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Main2Activity.this,Main3Activity.class);
                intent.putExtra("good_data",goods);
                startActivity(intent);
            }
        });
    }

    private void refreshGoods() {
        new MyAsyncTask().execute(Constant.JSON_GOODS_URL);
    }

    private class MyAsyncTask extends AsyncTask<String,Integer,String> {

        Goods goods = null;

        //List<Goods> list;

        public MyAsyncTask() {

        }

        @Override
        protected void onPostExecute(String s) {
            list.clear();
            try{
                JSONArray array = new JSONArray(s);
                for (int i = 0;i < array.length();i++){
                    goods = new Goods();
                    JSONObject object = array.getJSONObject(i);
                    goods.setGoodName(object.getString("goodName"));
                    goods.setGoodPrice(Double.valueOf(object.getString("goodPrice")));
                    goods.setDetail(object.getString("goodDetail"));
                    list.add(goods);
                }
                for (int i = 0;i<list.size();i++){
                    Log.d("myasynctask",list.get(i).getGoodName());
                }
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {

            StringBuilder builder = new StringBuilder();
            StringBuilder jsonData = new StringBuilder();
            try{
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(80000);
                connection.setReadTimeout(80000);
                connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                connection.connect();

                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null){
                    builder.append(line);
                }

                JSONObject object = new JSONObject(builder.toString());
                String str = object.getString("data");
                jsonData.append(str);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonData.toString();
        }

    }
}

package com.sw.library.widget.universalloadingview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.sw.library.widget.library.UniversalLoadingView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends ActionBarActivity {
    Handler mHandler;
    SwipeRefreshLayout refreshLayout;
    ImageView mImageView;
    UniversalLoadingView mLoadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_layout);
        refreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });

        mImageView = (ImageView) findViewById(R.id.imageview);
        mLoadingView = (UniversalLoadingView) findViewById(R.id.loadingView);
        mLoadingView.setOnReloadListener(new UniversalLoadingView.ReloadListner() {
            @Override
            public void reload() {
                loadImageView();
            }
        });

        loadImageView();
    }

    private void loadImageView() {
        new LoadImageTask().execute("http://d.hiphotos.baidu.com/zhidao/pic/item/38dbb6fd5266d016329c0b53952bd40734fa35c7.jpg");

    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingView.postLoadState(UniversalLoadingView.State.LOADING);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL imageUrl = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)imageUrl.openConnection();
                InputStream input = urlConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mImageView.setImageBitmap(bitmap);
            mLoadingView.postLoadState(UniversalLoadingView.State.GONE);
            super.onPostExecute(bitmap);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

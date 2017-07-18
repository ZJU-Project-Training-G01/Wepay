package cn.edu.zju.se_g01.nfc_pay.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.edu.zju.se_g01.nfc_pay.Good.Goods;
import cn.edu.zju.se_g01.nfc_pay.Good.GoodsLab;
import cn.edu.zju.se_g01.nfc_pay.Good.ImgDownloader;
import cn.edu.zju.se_g01.nfc_pay.GoodActivity;
import cn.edu.zju.se_g01.nfc_pay.MainActivity;
import cn.edu.zju.se_g01.nfc_pay.R;
import cn.edu.zju.se_g01.nfc_pay.tools.CookieRequest;
import cn.edu.zju.se_g01.nfc_pay.tools.MySingleton;
import cn.edu.zju.se_g01.nfc_pay.tools.NfcOperator;

/**
 * Created by dddong on 2017/7/5.
 */

public class SearchFragment extends ListFragment {
    private final String TAG = "GoodsListFragment";
    public ArrayList<Goods> goodsList;
    ImgDownloader<ImageView> imgDownloaderThread;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //CookieRequest
//        goodsList = GoodsLab.getInstance().getGoodsList();
        RequestQueue queue = MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        String url = "http://120.77.34.254/business-system/backend/public/GetGoodList";

        CookieRequest listRequest = new CookieRequest(
                getActivity().getApplicationContext(),
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "in list response");
                        //onResponse
                        //TODO 解析JSONObject获得goodList
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            goodsList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                Goods good = new Goods(
                                        jo.getString("good_id"),
                                        jo.getString("good_name"),
                                        Double.valueOf(jo.getString("unit_price")),
                                        jo.getString("img_url"),
                                        jo.getString("good_info")
                                );
                                goodsList.add(good);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        GoodAdapter adapter = new GoodAdapter(goodsList);
                        setListAdapter(adapter);

                        imgDownloaderThread = new ImgDownloader<>(new Handler());
                        imgDownloaderThread.setListener(new ImgDownloader.Listener<ImageView>() {

                            @Override
                            public void onImgDownload(ImageView imageView, Bitmap img) {
                                if (isVisible()) {
                                    imageView.setImageBitmap(img);
                                }
                            }
                        });

                        imgDownloaderThread.start();
                        imgDownloaderThread.getLooper();
                        Log.i(TAG, "Background thread started");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.e(TAG, "listRequestError");
                        Toast.makeText(getActivity(), "网络出现问题", Toast.LENGTH_LONG).show();

                    }
                }
        );
        queue.add(listRequest);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Goods g = ((GoodAdapter) getListAdapter()).getItem(position);
        Log.d(TAG, g.getGoodsName() + " was clicked");
        Log.d(TAG, g.getGoodsId());


        //Start GoodActivity
        Intent i = new Intent(getActivity(), GoodActivity.class);
        i.putExtra(GoodActivity.EXTRA_GOOD_ID, g.getGoodsId());
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imgDownloaderThread != null)
            imgDownloaderThread.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (imgDownloaderThread != null)
            imgDownloaderThread.clearQueue();
    }

    private class GoodAdapter extends ArrayAdapter<Goods> {
        public GoodAdapter(ArrayList<Goods> goodsList) {
            super(getActivity(), 0, goodsList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.search_layout, null);
            }

            Goods g = getItem(position);

            ImageView img = (ImageView) convertView.findViewById(R.id.good_img);
            img.setImageResource(R.drawable.ic_default_img);
            imgDownloaderThread.queueImg(img, g.getImgUrl());

            TextView goodName = (TextView) convertView.findViewById(R.id.good_name);
            TextView unitPrice = (TextView) convertView.findViewById(R.id.unit_price);

            goodName.setText(g.getGoodsName());
            unitPrice.setText("￥ " + String.format("%.2f", g.getUnitPrice()));

            return convertView;
        }
    }

}

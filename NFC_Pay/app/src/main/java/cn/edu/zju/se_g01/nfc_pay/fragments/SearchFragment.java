package cn.edu.zju.se_g01.nfc_pay.fragments;

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

import java.util.ArrayList;

import cn.edu.zju.se_g01.nfc_pay.Good.Goods;
import cn.edu.zju.se_g01.nfc_pay.Good.GoodsLab;
import cn.edu.zju.se_g01.nfc_pay.Good.ImgDownloader;
import cn.edu.zju.se_g01.nfc_pay.GoodActivity;
import cn.edu.zju.se_g01.nfc_pay.MainActivity;
import cn.edu.zju.se_g01.nfc_pay.R;
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
        goodsList = GoodsLab.get(getActivity()).getGoodsList();

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
//        bitmaps = new ArrayList<>(goodsList.size());
//        Thread t = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    Bitmap bitmap;
//                    Log.d("debug", "*******connect**************************");
//                    Log.d("debug", "*******connect**************************");
//                    Log.d("debug", "*******connect**************************");
//
//                    for (int i = 0; i < goodsList.size(); i++) {
//                        URL url = new URL("http://www.lagou.com/image1/M00/31/84/Cgo8PFWLydyAKywFAACk6BPmTzc228.png");
//                        InputStream is = url.openStream();
//                        bitmap = BitmapFactory.decodeStream(is);
//                        bitmaps.set(i, bitmap);
//                        is.close();
//                        Thread.sleep(1000);
//                    }
//
//                    Log.d("debug", "*******aaaconnect**************************");
//                    Log.d("debug", "*******aaaconnect**************************");
//                    Log.d("debug", "*******aaaconnect**************************");
//                } catch (Exception e) {
//                    Log.d("debug", "!!!!!!!!!!!cannot connect!!!!!!!!!!!!!!!!!!!");
//                    Log.d("debug", "!!!!!!!!!!!cannot connect!!!!!!!!!!!!!!!!!!!");
//                    Log.d("debug", "!!!!!!!!!!!cannot connect!!!!!!!!!!!!!!!!!!!");
//                    e.printStackTrace();
//                }
//
//            }
//        };
//        t.start();
//        try {
//            t.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Goods g = ((GoodAdapter) getListAdapter()).getItem(position);
        Log.d(TAG, g.getGoodsName() + " was clicked");

        //Start GoodActivity
        Intent i = new Intent(getActivity(), GoodActivity.class);
        i.putExtra(GoodActivity.EXTRA_GOOD_ID, g.getUuid());
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imgDownloaderThread.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
//            Drawable cachedImage = asyncImageLoader.loadDrawable(g.getImgUrl(), new AsyncImageLoader.ImageCallback() {
//                public void imageLoaded(Drawable imageDrawable, String imageUrl) {
//
//                    ImageView img = (ImageView) convertView.findViewById(R.id.good_img);
//                    if (imageViewByTag != null) {
//                        imageViewByTag.setImageDrawable(imageDrawable);
//                    }
//                }
//            });

            goodName.setText(g.getGoodsName());
            unitPrice.setText("￥ " + String.format("%.2f", g.getUnitPrice()));

            return convertView;
        }
    }

//    private class FetchItemsTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            new FlickrFetchr().fe
//            return null;
//        }
//    }

}

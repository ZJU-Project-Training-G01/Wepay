package cn.edu.zju.se_g01.nfc_pay.fragments;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.edu.zju.se_g01.nfc_pay.R;
import cn.edu.zju.se_g01.nfc_pay.orders.Order;
import cn.edu.zju.se_g01.nfc_pay.orders.OrderLab;
import cn.edu.zju.se_g01.nfc_pay.tools.ImageDownloader;

/**
 * Created by dddong on 2017/7/7.
 */

public class OrderListFragment extends ListFragment {
    private static final String TAG = "OrderListFragment";
    private ArrayList<Order> mOrders;
    ImageDownloader<ImageView> mImageDownloadThread;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrders = OrderLab.get(getActivity()).getOrders();

        OrderAdapter adapter = new OrderAdapter(mOrders);
        setListAdapter(adapter);

        mImageDownloadThread = new ImageDownloader<>(new Handler());
        mImageDownloadThread.setListener(new ImageDownloader.Listener<ImageView>() {
            @Override
            public void onImageDownloaded(ImageView imageView, Bitmap thumbnail) {
                if (isVisible()) {
                    imageView.setImageBitmap(thumbnail);
                }
            }
        });
        mImageDownloadThread.start();
        mImageDownloadThread.getLooper();
        Log.i(TAG, "background thread start");
    }

    private class OrderAdapter extends ArrayAdapter<Order> {
        public OrderAdapter(ArrayList<Order> orders) {
            super(getActivity(), 0, orders);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.order_list_item, null);
            }
            Order o = getItem(position);
            TextView goodNameTextView = (TextView)convertView.findViewById(R.id.goodNameTextView);
            TextView totNumAndPriceTextView = (TextView)convertView.findViewById(R.id.totalNumAndPriceTextView);
            TextView orderStatusTextView = (TextView)convertView.findViewById(R.id.orderStatusTextView);
            Button confirmRecvGoodsBtn = (Button)convertView.findViewById(R.id.confirmRecvGoodsbutton);

            goodNameTextView.setText(o.getGood_name());
            double totalPrice = o.getUnit_price() * o.getAmount();
            totNumAndPriceTextView.setText("数量: x" + o.getAmount() + " 合计:" + "￥" + String.format("%.2f", totalPrice));

            orderStatusTextView.setText(o.getOrder_status_str());
            if(!o.isWaitingForBuyerToConfirm()) {
                confirmRecvGoodsBtn.setEnabled(false);
            } else {
                confirmRecvGoodsBtn.setEnabled(true);
            }

            //下载图片
            ImageView imageView = (ImageView)convertView.findViewById(R.id.orderGoodsimageView);
            mImageDownloadThread.queueImage(imageView, o.getImageUrl());
            return convertView;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageDownloadThread.quit();
        Log.i(TAG, "background thread destroyed");
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        mImageDownloadThread.clearQueue();
    }
}

package cn.edu.zju.se_g01.nfc_pay.Good;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.se_g01.nfc_pay.R;
import cn.edu.zju.se_g01.nfc_pay.fragments.SearchFragment;

/**
 * Created by Rexxar on 2017/7/7.
 */

public class GoodAdapter extends BaseAdapter {

    private List<Goods> goodsList = new ArrayList<>();

    private Context context;
    private LayoutInflater layoutInflater;

    private SearchFragment searchFragment;

    public GoodAdapter(Context context, List<Goods> goodsList) {
        this.context = context;
        this.goodsList = goodsList;

        searchFragment = new SearchFragment();
        searchFragment.setContext(context);

//        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Goods getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.search_layout, null);//找到布局文件
            convertView.setTag(new ViewHolder(convertView));
        }
        initViews(getItem(position),(ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initViews(Goods goods, ViewHolder holder) {
        /*
         * 第一次初始话的时候通过 要请求的Url地址 为每个图片设置一个Tag标记,
         * 然后在设置图片的时候判断Tag标记如果是才把图片设置到ImageView上,
         * 这做的原因是为了防止ListView 中的图片错位...
         */
        holder.imageView.setTag(goods.getImgUrl());

        //设置新闻标题为集合中获得的标题
        holder.nameTextView.setText(goods.getGoodsName());

        holder.priceTextView.setText(String.format("%.2f",goods.getUnitPrice()));

    }

    protected class ViewHolder {
        private ImageView imageView;
        private TextView nameTextView;
        private TextView priceTextView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.good_img);
            nameTextView = (TextView) view.findViewById(R.id.good_name);
            priceTextView = (TextView) view.findViewById(R.id.unit_price);
        }
    }

    public void getImage(Context context, String imgUrl,
                         final ImageView imageView) {
        //TODO 将img显示在imageView上
        /*
         * 检测图片的Tag值 ,如果根请求的地址相同 才做图片的网络请求.
         */
//        if (imageView.getTag().toString().equals(imgUrl)) {
//
//            RequestQueue mQueue = Volley.newRequestQueue(context);
//            ImageRequest imageRequest = new ImageRequest(imgUrl,
//                    new Response.Listener<Bitmap>() {
//                        @Override
//                        public void onResponse(Bitmap response) {
//                            imageView.setImageBitmap(response);//将返回的Bitmap显示子啊ImageView上
//                        }
//                    }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                }
//            });
//            mQueue.add(imageRequest);
//        }
    }
}

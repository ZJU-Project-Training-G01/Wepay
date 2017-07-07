package cn.edu.zju.se_g01.nfc_pay.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.se_g01.nfc_pay.Good.GoodAdapter;
import cn.edu.zju.se_g01.nfc_pay.Good.Goods;
import cn.edu.zju.se_g01.nfc_pay.R;

/**
 * Created by dddong on 2017/7/5.
 */

public class SearchFragment extends Fragment {
    private Context context;
    private List<Goods> goodsList;
    private GoodAdapter adapter;

    private ListView listView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //通过inflater()方法获得fragment的布局
        View view=inflater.inflate(R.layout.search_layout, container, false);

        //在fragment中要通过view调用findViewById(),然后获得listview
        listView = (ListView) view.findViewById(R.id.listview);

        goodsList = getGoodsList(null);

        //这个可以忽略
        //mApplication = (MyApplication) getActivity().getApplication();

        adapter = new GoodAdapter(this.context, goodsList);

        return view;
    }

    public List<Goods> getGoodsList(String url) {
        List<Goods> goodsList = new ArrayList<>();
        //TODO 连接服务器，获取所有商品
        for (int i = 0; i < 5; i++) {
            Goods g = new Goods("01", "mobile phone "+String.valueOf(i),
                    500, "http://www.baidu.com", "none");
            goodsList.add(g);
        }
        return goodsList;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

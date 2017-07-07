package cn.edu.zju.se_g01.nfc_pay.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.zju.se_g01.nfc_pay.R;

/**
 * Created by dddong on 2017/7/5.
 */

public class SearchFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.search_layout, container, false);


        return view;
    }
}

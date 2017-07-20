package cn.edu.zju.se_g01.nfc_pay.fragments;

import android.nfc.FormatException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

import cn.edu.zju.se_g01.nfc_pay.MainActivity;
import cn.edu.zju.se_g01.nfc_pay.R;
import cn.edu.zju.se_g01.nfc_pay.tools.NfcOperator;

/**
 * Created by dddong on 2017/7/5.
 */

public class HomeFragment extends Fragment {
    public NfcOperator nfcOperator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_layout, container, false);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.nfc_scan_btn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity().getApplicationContext(), "click nfc button", Toast.LENGTH_SHORT).show();

                nfcOperator = ((MainActivity) getActivity()).nfcOperator;
                try {
                    String content = nfcOperator.read();
                    if (content != null)
                        Toast.makeText(getActivity().getApplicationContext(), "NFC中包含的内容:" + content, Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getActivity().getApplicationContext(), "未检测到NFC芯片", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (FormatException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

}

package cn.edu.zju.se_g01.nfc_pay.tools;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.Parcelable;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by Rexxar on 2017/7/9.
 */

public class NfcOperator {
    // NFC适配器
    public NfcAdapter nfcAdapter = null;
    // 传达意图
    public PendingIntent pi = null;
    // 滤掉组件无法响应和处理的Intent
    public IntentFilter tagDetected = null;
    // 是否支持NFC功能的标签
    public boolean isNFC_support = false;

    public Intent intent;

//    public Context context;

    public Tag tagFromIntent;

    private static NfcOperator nfcOperator;

    private NfcOperator() {

    }

    public static NfcOperator getInstance() {
        if (nfcOperator == null) {
            nfcOperator = new NfcOperator();
        }
        return nfcOperator;
    }

    //传入当前activity
    public void initNFCData(Context context) {
//        this.context = context;
        // 初始化设备支持NFC功能
        isNFC_support = true;
        // 得到默认nfc适配器
        nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        // 提示信息定义
        String metaInfo = "";
        // 判定设备是否支持NFC或启动NFC
        if (nfcAdapter == null) {
            metaInfo = "设备不支持NFC！";
            Toast.makeText(context.getApplicationContext(), metaInfo, Toast.LENGTH_SHORT).show();
            isNFC_support = false;
        }
        if (!nfcAdapter.isEnabled()) {
            metaInfo = "请在系统设置中先启用NFC功能！";
            Toast.makeText(context.getApplicationContext(), metaInfo, Toast.LENGTH_SHORT).show();
            isNFC_support = false;
        }

        if (isNFC_support) {
            init_NFC(context);
        }
    }

    public void init_NFC(Context context) {
        // 初始化PendingIntent，当有NFC设备连接上的时候，就交给当前Activity处理
        pi = PendingIntent.getActivity(context, 0, new Intent(context, context.getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // 新建IntentFilter，使用的是第二种的过滤机制
        tagDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);


    }

    public String read() throws IOException, FormatException {
        Tag tag = tagFromIntent;
        if (tag != null) {
//            //解析Tag获取到NDEF实例
//            Ndef ndef = Ndef.get(tag);
//            //打开连接
//            ndef.connect();
//            //获取NDEF消息
//            NdefMessage message = ndef.getNdefMessage();
//            //将消息转换成字节数组
//            byte[] data = message.toByteArray();
//            //将字节数组转换成字符串
//            String str = new String(data, Charset.forName("UTF-8"));
//            //关闭连接
//            ndef.close();
//            return str;
            String msg = "";
            Parcelable[] rawArray = intent
                    .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawArray != null) {
                NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
                NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
                try {
                    if (mNdefRecord != null) {
                        msg = new String(mNdefRecord.getPayload(), "UTF-8");
//                    return true;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return msg;
            }
        } else {
//            Toast.makeText(context, "设备与nfc卡连接断开，请重新连接...",
//                    Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    // 写入方法
    public void write(String msg) throws IOException, FormatException {
        Tag tag = tagFromIntent;
        if (tag != null) {
            //新建NdefRecord数组，本例中数组只有一个元素
            NdefRecord[] records = {createRecord(msg)};
            //新建一个NdefMessage实例
            NdefMessage message = new NdefMessage(records);
            // 解析TAG获取到NDEF实例
            Ndef ndef = Ndef.get(tag);
            // 打开连接
            ndef.connect();
            // 写入NDEF信息
            ndef.writeNdefMessage(message);
            // 关闭连接
            ndef.close();

//            promt.setText(promt.getText() + "写入数据成功！" + "\n");
        } else {
//            Toast.makeText(context.getApplicationContext(), "设备与nfc卡连接断开，请重新连接...",
//                    Toast.LENGTH_SHORT).show();
        }
    }

    //返回一个NdefRecord实例
    private NdefRecord createRecord(String msg) throws UnsupportedEncodingException {
        //组装字符串，准备好你要写入的信息
//        String msg = "BEGIN:VCARD\n" + "VERSION:2.1\n" + "中国湖北省武汉市\n"
//                + "武汉大学计算机学院\n" + "END:VCARD";
        //将字符串转换成字节数组
        byte[] textBytes = msg.getBytes();
        //将字节数组封装到一个NdefRecord实例中去
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                "text/plain".getBytes(), new byte[]{}, textBytes);
        return textRecord;
    }


    // 字符序列转换为16进制字符串
    private String bytesToHexString(byte[] src) {
        return bytesToHexString(src, true);
    }

    private String bytesToHexString(byte[] src, boolean isPrefix) {
        StringBuilder stringBuilder = new StringBuilder();
        if (isPrefix == true) {
            stringBuilder.append("0x");
        }
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.toUpperCase(Character.forDigit(
                    (src[i] >>> 4) & 0x0F, 16));
            buffer[1] = Character.toUpperCase(Character.forDigit(src[i] & 0x0F,
                    16));
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    public String processIntent(Intent intent) {
        String msg = "";
        if (isNFC_support == false)
            return msg;

        this.intent = intent;

        // 取出封装在intent中的TAG
        tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Parcelable[] rawArray = intent
                .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawArray != null) {
            NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
            NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
            try {
                if (mNdefRecord != null) {
                    msg = new String(mNdefRecord.getPayload(), "UTF-8");
//                    return true;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//            return false;
        }
//        return false;


//        String metaInfo = "";
//        metaInfo += "卡片ID：" + bytesToHexString(tagFromIntent.getId()) + "\n";
////        Toast.makeText(context.getApplicationContext(), metaInfo, Toast.LENGTH_SHORT).show();
//
//        // Tech List
//        String prefix = "android.nfc.tech.";
//        String[] techList = tagFromIntent.getTechList();
//
//        //分析NFC卡的类型： Mifare Classic/UltraLight Info
//        String CardType = "";
//        for (int i = 0; i < techList.length; i++) {
//            if (techList[i].equals(NfcA.class.getName())) {
//                // 读取TAG
//                NfcA mfc = NfcA.get(tagFromIntent);
//                try {
//                    if ("".equals(CardType))
//                        CardType = "MifareClassic卡片类型 \n 不支持NDEF消息 \n";
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else if (techList[i].equals(MifareUltralight.class.getName())) {
//                MifareUltralight mifareUlTag = MifareUltralight
//                        .get(tagFromIntent);
//                String lightType = "";
//                // Type Info
//                switch (mifareUlTag.getType()) {
//                    case MifareUltralight.TYPE_ULTRALIGHT:
//                        lightType = "Ultralight";
//                        break;
//                    case MifareUltralight.TYPE_ULTRALIGHT_C:
//                        lightType = "Ultralight C";
//                        break;
//                }
//                CardType = lightType + "卡片类型\n";
//
//                Ndef ndef = Ndef.get(tagFromIntent);
//                CardType += "最大数据尺寸:" + ndef.getMaxSize() + "\n";
//
//            }
//        }
//        metaInfo += CardType;
////        promt.setText(metaInfo);


//        try {
//            msg = read();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return msg;
    }

}

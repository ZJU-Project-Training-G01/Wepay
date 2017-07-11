package cn.edu.zju.se_g01.nfc_pay.Good;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.sip.SipAudioCall;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.edu.zju.se_g01.nfc_pay.FlickrFetchr;

/**
 * Created by Rexxar on 2017/7/7.
 */

public class ImgDownloader<Token> extends HandlerThread {
    private static final String TAG = "ImgDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    Handler handler;
    Map<Token, String> requestMap =
            Collections.synchronizedMap(new HashMap<Token, String>());
    Handler responseHandler;
    Listener<Token> listener;

    public interface Listener<Token> {
        void onImgDownload(Token token, Bitmap img);
    }

    public void setListener(Listener<Token> listener) {
        this.listener = listener;
    }
    public ImgDownloader(Handler responseHandler) {
        super(TAG);
        this.responseHandler = responseHandler;
    }

    @Override
    protected void onLooperPrepared() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    Token token = (Token) msg.obj;
                    Log.i(TAG, "Got a request for url: " + requestMap.get(token));
                    handleRequest(token);
                }
            }
        };
    }

    public void queueImg(Token token, String url) {
        Log.i(TAG, "Got an URL: " + url);
        requestMap.put(token, url);

        handler
                .obtainMessage(MESSAGE_DOWNLOAD, token)
                .sendToTarget();

    }

    private void handleRequest(final Token token) {
        try {
            final String url = requestMap.get(token);
            if (url==null)
                return;

            byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory
                    .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Log.i(TAG, "Bitmap created");

            responseHandler.post(new Runnable() {
                @Override
                public void run() {

                    if (requestMap.get(token) != url) {
                        return;
                    }
                    requestMap.remove(token);
                    listener.onImgDownload(token, bitmap);
                }
            });

        } catch (IOException e) {
            Log.i(TAG, "Error downloading img", e);
        }
    }

    public void clearQueue() {
        handler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }

}

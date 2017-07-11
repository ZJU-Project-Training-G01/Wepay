package cn.edu.zju.se_g01.nfc_pay.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
 * Created by dddong on 2017/7/8.
 */

public class ImageDownloader<Token> extends HandlerThread {
    private static final String TAG = "ImageDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    Handler mHandler;
    Handler mResponseHandler;
    Map<Token, String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());

    Listener<Token> mListener;

    public interface Listener<Token> {
        void onImageDownloaded(Token token, Bitmap thumbnail);
    }

    public void setListener(Listener<Token> listener) {
        mListener = listener;
    }
    public ImageDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    public void queueImage(Token token, String url) {
        Log.i(TAG, "Got an url:" + url);
        requestMap.put(token, url);
        mHandler.obtainMessage(MESSAGE_DOWNLOAD, token).sendToTarget();
    }

    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if(msg.what == MESSAGE_DOWNLOAD) {
                    Token token = (Token)msg.obj;
                    Log.i(TAG, "Get a request for url: " + requestMap.get(token));
                    handleRequest(token);
                }
            }
        };
    }

    private void handleRequest(final Token token) {
        try {
            final String url = requestMap.get(token);
            if(url == null) return;
            byte [] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Log.i(TAG, "bitmap created");
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(requestMap.get(token) != url) return;
                    requestMap.remove(token);
                    mListener.onImageDownloaded(token, bitmap);
                }
            });

        } catch (IOException ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }
    }

    public void clearQueue() {
        mHandler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }
}

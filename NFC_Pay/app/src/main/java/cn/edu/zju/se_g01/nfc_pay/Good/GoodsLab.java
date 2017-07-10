package cn.edu.zju.se_g01.nfc_pay.Good;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 该类为单例
 * 存储商品的列表
 */
public class GoodsLab {
    private static GoodsLab goodsLab;
    private ArrayList<Goods> goodsList;

    public static GoodsLab get(Context context) {
        if (goodsLab == null) {
            goodsLab = new GoodsLab(context);
        }
        return goodsLab;
    }

    private GoodsLab(Context context) {
        goodsList = new ArrayList<>();
        //TODO 连接服务器，获取所有商品
        for (int i = 0; i < 5; i++) {
            Goods g = new Goods("000" + (i + 1), "mobile phone " + (i + 1),
                    500, "http://www.lagou.com/image1/M00/31/84/Cgo8PFWLydyAKywFAACk6BPmTzc228.png", "none");
            goodsList.add(g);
        }
    }

    public ArrayList<Goods> getGoodsList() {
        return goodsList;
    }

    public Goods getGoods(UUID uuid) {
        for (Goods g : goodsList) {
            if (g.getUuid().equals(uuid)) {
                return g;
            }
        }
        return null;
    }
}

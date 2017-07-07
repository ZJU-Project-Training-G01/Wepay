package cn.edu.zju.se_g01.nfc_pay.Good;

import java.util.UUID;

/**
 * 单个商品的信息
 */
public class Goods {
    private UUID uuid;
    private String goodsId;
    private String goodsName;
    private double unitPrice;
    private String imgUrl;
    private String goodsInfo;

    public Goods() {
        uuid = UUID.randomUUID();
    }

    public Goods(String goodsId, String goodsName, float unitPrice, String imgUrl, String goodsInfo) {
        uuid = UUID.randomUUID();
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.unitPrice = unitPrice;
        this.imgUrl = imgUrl;
        this.goodsInfo = goodsInfo;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }
}

package cn.edu.zju.se_g01.nfc_pay.orders;

import java.util.Date;

/**
 * Created by dddong on 2017/7/7.
 */

public class Order {
    private int order_id;       //订单 id
    private int buyer_id;       //买家 id
    private int good_id;        //商品 id
    private String good_name;   //商品名称
//    private Bitmap good_image;  //商品图片
    private String imageUrl;
    private int amount;         //商品数量
    private double unit_price;  //单价，指订单产生时商品的价格
    private Order_status order_status;
    private Date order_data;    //订单创建的时间

    public Order(int order_id) {
        this.imageUrl = "http://cimage1.tianjimedia.com/uploadImages/thirdImages/2017/187/JLLT32DVUP6A.jpg";
        this.order_id = order_id;
        this.buyer_id = 100;
        this.good_id = 100;
        this.good_name = "美的变频空调";
        this.amount = 1;
        this.unit_price = Math.random();
        this.order_status = Order_status.undelivered;
        this.order_data = new Date();
    }

    public Order(int order_id, int buyer_id, int good_id, int amount, double unit_price, Order_status order_status, Date order_data) {
        this.order_id = order_id;
        this.buyer_id = buyer_id;
        this.good_id = good_id;
        this.amount = amount;
        this.unit_price = unit_price;
        this.order_status = order_status;
        this.order_data = order_data;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getOrder_id() {
        return order_id;
    }

    public String getGood_name() {
        return good_name;
    }

    public int getAmount() {
        return amount;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public String getOrder_status_str() {
        switch (this.order_status) {
            case undelivered:
                return "未发货";
            case unreceive_confirmed:
                return "已发货";
            case receive_confirmed:
                return "交易完成";
            default:
                throw new RuntimeException("order status invalid!");
        }
    }

    public Date getOrder_data() {
        return order_data;
    }

    public boolean isTransactionComplete() {
        if(order_status == Order_status.receive_confirmed) {
            return true;
        }
        return false;
    }

    public boolean isWaitingForBuyerToConfirm() {
        if(order_status == Order_status.unreceive_confirmed) {
            return true;
        }
        return false;
    }

    enum Order_status { //订单状态
        undelivered,    //未发货
        unreceive_confirmed,    //已经发货但是买家未确认收货
        receive_confirmed,  //已经确认收货（这个时候钱已经打到卖家)
    }

}


package cn.edu.zju.se_g01.nfc_pay.orders;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by dddong on 2017/7/7.
 */

public class OrderLab {
    private static OrderLab sOrderLab;

    private ArrayList<Order> mOrders;
    /*
    CrimeLab类的构造方法需要一个Context参数。这在Android开发里很常见，使用Context 参数，单例可完成启动activity、获取项目资源，查找应用的私有存储空间等任务。
注意，在get(Context)方法里，我们并没有直接将Context参数传给构造方法。该Context 可能是一个Activity，也可能是另一个Context对象，如Service。在应用的整个生命周期里， 我们无法保证只要CrimeLab需要用到Context，Context就一定会存在。
因此，为保证单例总是有Context可以使用，可调用getApplicationContext()方法，将 不确定是否存在的Context替换成application context。application context是针对应用的全局性 Context。任何时候，只要是应用层面的单例，就应该一直使用application context。
     */
    private Context mAppContext;


    public OrderLab(Context mAppContext) {
        this.mAppContext = mAppContext;
        mOrders = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            mOrders.add(new Order(i));
        }
        /*
        TODO: 通过网络请求向mOrders中填充订单数据
         */
    }

    public static OrderLab get(Context c) {
        if(sOrderLab == null) {
            sOrderLab = new OrderLab(c.getApplicationContext());
        }
        return sOrderLab;
    }

    public ArrayList<Order> getOrders() {
        return mOrders;
    }

    public Order getOrder(int order_id) {
        for (Order order: mOrders) {
            if(order.getOrder_id() == order_id) {
                return order;
            }
        }
        return null;
    }
}

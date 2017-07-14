<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/9
 * Time: 13:55
 */
namespace app\android\controller;
use think\Controller;
use think\Db;
use think\Request;
use think\Session;
use Exception;

class GetOrders extends  Controller
{
    public function GetOrders()
    {
        $request = Request::instance();
        $buyer_id = $request->session('buyer_id');

        //test
        /*$buyer_id = 9;
        Session::set('login' , 'true');*/

        if($request->session('login')!='true')
        {
            $code = 2;
            $msg = '您还未登录。';
            $data = NULL;
        }
        else
        {
            try {
                $data = Db::query('select orderId as order_id, goodName as good_name, imgUrl as img_url, orders.amount, orders.unitPrice as unit_price, orderStatus as order_status, orderTime as order_time from orders, good where buyerId =:buyer_id and orders.goodId = good.goodId', ['buyer_id' => $buyer_id]);
                $code = 0;
                $msg = '查询成功';
            } catch (Exception $e) {
                $code = 1;
                $data = NULL;
                $msg = $e->getMessage();
            }
        }
        $res = [
            'code' => $code,
            'msg' => $msg,
            'data' => $data,
        ];
        echo json_encode($res, JSON_UNESCAPED_UNICODE);
    }
}
?>
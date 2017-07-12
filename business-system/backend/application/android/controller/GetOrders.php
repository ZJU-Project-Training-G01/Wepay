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
use Exception;

class GetOrders extends  Controller
{
    public function GetOrders()
    {
        $request = Request::instance();
        $buyer_id = $request->session('buyer_id');
        //$buyer_id =1;
        try {
            $data = Db::query('select orderId, goodName, imgUrl, orders.amount, orders.unitPrice, orderStatus, orderTime from orders, good where buyerId =:buyer_id and orders.goodId = good.goodId', ['buyer_id' => $buyer_id]);
            $code = 0;
            $msg = '查询成功';
        }
        catch (Exception $e){
            $code = 1;
            $data = NULL;
            $msg = $e->getMessage();
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
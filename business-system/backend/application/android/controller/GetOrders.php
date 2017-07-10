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

class GetOrders extends  Controller
{
    public function GetOrders()
    {
        $request = Request::instance();
        //$buyer_id = $request->session('buyer_id');
        $buyer_id =1;
        try {
            $data = Db::query('select orderId, goodName, imgUrl, orders.amount, orders.unitPrice, orderStatus, orderTime from orders, good where buyerId =:buyer_id and orders.goodId = good.goodId', ['buyer_id' => $buyer_id]);
            echo json_encode($data, JSON_UNESCAPED_UNICODE);
        }
        catch (\Exception $e){
            echo $e->getMessage();
        }
    }
}
?>
<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/10
 * Time: 10:21
 */

namespace app\android\controller;
use think\Controller;
use think\Db;
use think\Request;

class MakeNewOrder extends  Controller
{
    public function MakeNewOrder()
    {
        $request = Request::instance();
        $goodId = $request->post('good_id');
        $amount = $request->post('amount');
        $unitPrice = $request->post('unit_price');
        $buyerId = $request->session('buyer_id');
        /*$goodId = 1;
        $amount = 3;
        $unitPrice = 3.5;
        $buyerId = 3;*/
        $orderTime = date('Y-m-d');

        $res = array();
        $result = Db::query('insert into orders (buyerId, goodId, amount, unitPrice, orderStatus, orderTime) values (:buyerId, :goodId, :amount, :unitPrice, 0, :orderTime)',['buyerId'=>$buyerId,
            'goodId'=>$goodId, 'amount'=>$amount, 'unitPrice'=>$unitPrice, 'orderTime'=>$orderTime]);
        if($result == true)
        {
            $res['status'] = 'true';
        }
        else
        {
            $res['status'] = 'false';
        }
        echo json_encode($res, JSON_UNESCAPED_UNICODE);
    }
}
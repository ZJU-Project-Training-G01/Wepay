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
use think\Session;
use Exception;

class MakeNewOrder extends  Controller
{
    public function MakeNewOrder()
    {
        $request = Request::instance();
        $goodId = $request->post('good_id');
        $amount = $request->post('amount');
        $unitPrice = $request->post('unit_price');
        $buyerId = $request->session('buyer_id');

        //test
        /*$goodId = 3;
        $amount = 3;
        $unitPrice = 3.5;
        $buyerId = 3;
        Session::set('login', 'true');*/

        $orderTime = date('Y-m-d');
        $res = array();
        if($request->session('login')!='true')
        {
            $res['status'] = 'false';
        }
        else
        {
            $price = $unitPrice * $amount;
            $data1 = Db::query('select balance from buyer where balance >= :price and buyerId = :buyerId', ['price' => $price,'buyerId' => $buyerId ]);
            $data2 = Db::query('select amount from good where amount > 0 and goodId = :goodId',['goodId' => $goodId]);
            if(count($data1) ==0 || count($data2) == 0) //账户余额不足或商品余量不足
            {
                //echo count($data1);
                //echo count($data2);
                $res['status'] = 'false';
            }
            else
            {
                Db::startTrans();
                try{
                    Db::execute('update good set amount = amount - :amount where goodId = :goodId', ['amount' => $amount, 'goodId' => $goodId]);
                    Db::execute('insert into orders (buyerId, goodId, amount, unitPrice, orderStatus, orderTime) values (:buyerId, :goodId, :amount, :unitPrice, 0, :orderTime)', ['buyerId' => $buyerId,
                        'goodId' => $goodId, 'amount' => $amount, 'unitPrice' => $unitPrice, 'orderTime' => $orderTime]);
                    $res['status'] = 'true';
                    Db::commit();
                }catch(Exception $e){
                    $res['status'] = 'false';
                }
            }
        }
        echo json_encode($res, JSON_UNESCAPED_UNICODE);
    }
}
<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/13
 * Time: 10:20
 */

namespace app\android\controller;
use think\Controller;
use think\Db;
use think\Request;
use think\Session;
use Exception;

class ConfirmRecvGoods extends Controller
{
    public function ConfirmRecvGoods()
    {
        $request = Request::instance();
        $orderId = $request->post('orderID');

        //test
        Session::set('login','true');
        $orderId = 1;

        if($request->session('login')!='true')
        {
            $code = 1;
            $msg = '您还未登录。';
            $data = NULL;
        }
        else
        {
            $data1 = Db::query('select unitPrice, amount, buyerId, goodId from orders where orderId = :orderId',['orderId'=>$orderId]);
            $price = $data1[0]['unitPrice'] * $data1[0]['amount'];
            $buyerId = $data1[0]['buyerId'];
            $amount = $data1[0]['amount'];
            $goodId = $data1[0]['goodId'];
            $data3 = Db::query('select sellerId from good where goodId = :goodId',['goodId' => $goodId]);
            $sellerId = $data3[0]['sellerId'];
            $data2 = Db::query('select balance from buyer where balance >= :price and buyerId = :buyerId',['price'=>$price, 'buyerId' => $buyerId]);
            if(count($data2)!= 0) //判断用余额是否足够
            {
                Db::startTrans();
                try {
                    Db::execute('update orders set orderStatus = 2 where orderId = :orderId', ['orderId' => $orderId]);
                    Db::execute('update buyer set balance = balance - :price where buyerId = :buyerId', ['price' => $price, 'buyerId' => $buyerId]);
                    Db::execute('update seller set balance = balance + :price where sellerId = :sellerId', ['price' => $price, 'sellerId' => $sellerId]);
                    Db::execute('update good set soldAmount = soldAmount + :amount where goodId = :goodId',['amount' => $amount, 'goodId' => $goodId]);
                    $code = 0;
                    $data = NULL;
                    $msg = '';
                    Db::commit();
                } catch (Exception $e) {
                    $code = 1;
                    $data = NULL;
                    $msg = $e->getMessage();
                    Db::rollback();
                }
            }
            else
            {
                $code = 1;
                $msg = '账户余额不够，无法完成订单';
                $data = NULL;
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
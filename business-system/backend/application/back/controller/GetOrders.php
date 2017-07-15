<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/8
 * Time: 11:41
 */
namespace  app\back\controller;
use think\Controller;
use think\Db;
use think\Request;
use think\Session;
use Exception;

class GetOrders extends Controller
{
    public function GetOrders()
    {
        $request = Request::instance();
        $pageSize = $request->post('pageSize');
        $pageNumber = $request->post('pageNumber');
        $status = $request->post('status');

        //test
        /*$pageNumber = 1;
        $pageSize = 10;
        $status = -1;
        Session::set('login', 'true');*/

        $recordP = ($pageNumber-1)*$pageSize;

        if($request->session('login')!='true')
        {
            $code = 2;
            $msg = '您还未登录';
            $data = NULL;
        }
        else
        {
            $sellerId = $request->session('sellerId');
            //$sellerId = 2;
            if ($status == -1) {
                try {
                    $data = Db::query('select orderId,sellerId, goodName, imgUrl, orders.amount, orders.unitPrice, orderStatus, orderTime from orders, good where good.sellerId = :sellerId and good.goodId = orders.goodId order by orderId  limit :recordP,:pageSize',
                        ['sellerId' => $sellerId, 'recordP' => $recordP, 'pageSize' => $pageSize]);
                    $data1 = Db::query('select orderId,sellerId, goodName, imgUrl, orders.amount, orders.unitPrice, orderStatus, orderTime from orders, good where good.sellerId = :sellerId and good.goodId = orders.goodId order by orderId',
                        ['sellerId' => $sellerId]);
                    $code = 0;
                    $msg = count($data1);
                } catch (Exception $e) {
                    $code = 3;
                    $msg = $e->getMessage();
                    $data = NULL;
                }
            } else {
                try {
                    $data = Db::query('select orderId, sellerId, goodName, imgUrl, orders.amount, orders.unitPrice, orderStatus, orderTime from orders, good where good.sellerId = :sellerId and good.goodId = orders.goodId and  orderStatus = :status order by orderId limit :recordP,:pageSize',
                        ['sellerId' => $sellerId, 'recordP' => $recordP, 'pageSize' => $pageSize, 'status' => $status]);
                    $data1 = Db::query('select orderId, sellerId, goodName, imgUrl, orders.amount, orders.unitPrice, orderStatus, orderTime from orders, good where good.sellerId = :sellerId and good.goodId = orders.goodId and  orderStatus = :status order by orderId',
                        ['sellerId' => $sellerId, 'status' => $status]);
                    $code = 0;
                    $msg = count($data1);
                } catch (Exception $e) {
                    $code = 3;
                    $msg = $e->getMessage();
                    $data = NULL;
                }
            }
        }
        $res = [
            'code' => $code,
            'msg' => $msg,
            'data' => $data,
        ];
        echo json_encode($res, JSON_UNESCAPED_UNICODE);
        //return json_encode($data);
    }
}
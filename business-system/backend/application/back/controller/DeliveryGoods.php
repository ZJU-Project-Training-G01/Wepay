<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/14
 * Time: 21:31
 */

namespace app\back\controller;
use think\Controller;
use think\Db;
use think\Request;
use think\Session;
use Exception;

class DeliveryGoods extends Controller
{
    public function DeliveryGoods()
    {
        $request = Request::instance();
        $orderId = $request->post('orderId');

        //test
        /*$orderId = 10;
        Session::set('login', 'true');*/

        if($request->session('login')!='true')
        {
            $code = 2;
            $msg = '您还未登录';
            $data = NULL;
        }
        else
        {
            try{
                Db::execute('update orders set orderStatus = 1 where orderId = :orderId',['orderId' => $orderId]);
                $code = 0;
                $msg = $orderId;
                $data = NULL;
            }catch (Exception $e){
                $code = 3;
                $msg = '发货失败';
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
<?php
/**
 * Created by PhpStorm.
 * User: Rexxar
 * Date: 2017/7/15
 * Time: 9:35
 */

namespace app\android\controller;

use think\Controller;
use think\Db;
use think\Request;
use think\Session;
use Exception;

class ChargeBalance extends Controller
{
    public function ChargeBalance()
    {
        $request = Request::instance();

        $chargeNum = $request->post('chargeNum');
        $cardPassword = $request->post('cardPassword');

        if ($request->session('login') != 'true') {
            $code = 2;
            $msg = '用户未登录';
            $data = NULL;
        } else {
            $buyerId = $request->session('buyer_id');
            $data = Db::execute('update buyer set balance = balance + ' . $chargeNum . ' where buyerId = ' . $buyerId . ';');
            $code = 0;
            $msg = NULL;
            $data = NULL;
        }
        $res = [
            'code' => $code,
            'msg' => $msg,
            'data' => $data,
        ];
        echo json_encode($res, JSON_UNESCAPED_UNICODE);
    }

}
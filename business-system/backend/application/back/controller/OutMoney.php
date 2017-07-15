<?php
/**
 * Created by PhpStorm.
 * User: Rexxar
 * Date: 2017/7/15
 * Time: 11:39
 */

namespace app\back\controller;


use think\Db;
use think\Request;

class OutMoney
{
    public function OutMoney()
    {
        $request = Request::instance();
        $payPassword = $request->post('payPassword');
        $outMoney = $request->post('outMoney');

        if($request->session('login')!='true')
        {
            $code = 2;
            $msg = '您还未登录';
            $data = NULL;
        } else {
            $sellerId = $request->session('sellerId');
            Db::execute('update seller set balance = balance - :inMoney where sellerId = :sellerId;',
            ['inMoney' => $outMoney, 'sellerId' => $sellerId]);

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
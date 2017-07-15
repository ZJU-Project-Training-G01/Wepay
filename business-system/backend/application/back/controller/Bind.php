<?php
/**
 * Created by PhpStorm.
 * User: Rexxar
 * Date: 2017/7/15
 * Time: 11:13
 */

namespace app\back\controller;


use think\Db;
use think\Request;

class Bind
{
    public function Bind()
    {
        $request = Request::instance();

        $bankName = $request->post('bankName');
        $bankCard = $request->post('bankCard');

        if ($request->session('login') != 'true') {
            $code = 2;
            $msg = '您还未登录';
            $data = NULL;
        } else {
            $sellerId = $request->session('sellerId');
            Db::execute('update seller set bankCard = ":bankCard" where sellerId = :sellerId;', ['bankCard' => $bankCard, 'sellerId' => $sellerId]);

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
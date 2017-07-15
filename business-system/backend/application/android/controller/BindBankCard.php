<?php
/**
 * Created by PhpStorm.
 * User: Rexxar
 * Date: 2017/7/14
 * Time: 15:28
 */

namespace app\android\controller;

use think\Controller;
use think\Db;
use think\Request;
use think\Session;
use Exception;

class BindBankCard extends Controller
{
    public function BindBankCard()
    {
        $request = Request::instance();
        $cardOwner = $request->post('cardOwner');
        $IDnum = $request->post('IDnum');
        $cardNum = $request->post('cardNum');
        $cardPassword = $request->post('cardPassword');

        if ($request->session('login') != 'true') {
            $code = 2;
            $msg = '您还未登录';
            $data = NULL;
        } else {
            $buyerId = $request->session('buyer_id');
            $data = Db::execute('update buyer set bankCard = ' . $cardNum . ' where buyerId = ' . $buyerId . ';');
            $code = 0;
            $msg = NULL;
            $data = NULL;
        }

        $res = [
            'code' => $code,
            'msg' => $msg,
            'data' => $data
        ];
        echo json_encode($res, JSON_UNESCAPED_UNICODE);
    }
}
<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/13
 * Time: 15:09
 */

namespace app\back\controller;
use think\Controller;
use think\Db;
use think\Request;
use think\Session;

class SellerLogin extends  Controller
{
    public function SellerLogin()
    {
        $request = Request::instance();
        $phoneNumber = $request->post('phoneNumber');
        $sellerPassword = $request->post('sellerPassword');

        //test
        /*$phoneNumber = '1234567';
        $sellerPassword = '123456';*/

        $data1 = Db::query('select sellerId from seller where phoneNumber = :phoneNumber and sellerPassword = :sellerPassword',['phoneNumber' => $phoneNumber, 'sellerPassword' => $sellerPassword]);
        if(count($data1))
        {
            $code = 0;
            $msg = '成功登陆';
            $data = NULL;
            Session::set('sellerId', $data1[0]['sellerId']);
            Session::set('login','true');
        }
        else
        {
            $code = 2;
            $msg = '手机号码或密码输入错误';
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

?>
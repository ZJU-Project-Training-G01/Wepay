<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/13
 * Time: 9:57
 */

namespace app\android\controller;
use think\Controller;
use think\Db;
use think\Request;
use think\Session;
use think\Cookie;

class LogOut extends Controller
{
    public function LogOut()
    {
        $request = Request::instance();
        Session::set('buyer_id',null);
        Session::set('login',null);
        Cookie::delete('buyer_id');
        Cookie::delete('login');
        $code = 0;
        $data = NULL;
        $msg = '';
        $res = [
            'code' => $code,
            'msg' => $msg,
            'data' => $data,
        ];
        echo json_encode($res, JSON_UNESCAPED_UNICODE);
    }
}
?>
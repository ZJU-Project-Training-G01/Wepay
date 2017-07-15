<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/15
 * Time: 13:25
 */

use think\Controller;
use think\Db;
use think\Request;
use think\Session;
use Exception;

class SellerLoginOut extends Controller
{
    public function SellerLoginOut()
    {
        $request = Request::instance();
        Session::set('sellerId', NULL);
        Session::set('login', NULL);
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
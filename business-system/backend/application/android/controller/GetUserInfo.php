<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/9
 * Time: 14:56
 */
namespace app\android\controller;
use think\Controller;
use think\Db;
use think\Request;
use Exception;
use think\Session;

class GetUserInfo extends Controller
{
    public function GetUserInfo()
    {
        $request = Request::instance();
        //test
        //Session::set('login','true');
        if($request->session('login')!='true')
        {
            $code = 2;
            $msg = '您还未登录。';
            $data = NULL;
        }
        else
        {
            $buyerId = $request->session('buyer_id');
            //$buyerId = 1 ;
            try {
                $data1 = Db::query('select buyerName as userName, realName, phoneNumber as phone, address, balance from buyer where buyerId = :buyerId', ['buyerId' => $buyerId]);
                //print_r($data);
                $data = $data1[0];
                $code = 0;
                $msg = '';
            } catch (Exception $e) {
                $code = 1;
                $data = NULL;
                $msg = $e->getMessage();
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
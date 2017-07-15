<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/14
 * Time: 14:07
 */

namespace app\back\controller;
use think\Controller;
use think\Db;
use think\Request;
use think\Session;
use Exception;

class GetSellerInfo extends Controller
{
    public function GetSellerInfo()
    {
        $request = Request::instance();

        //test
        //Session::set('login', 'true');

        if($request->session('login')!='true')
        {
            $code = 2;
            $msg = '您还未登录';
            $data = NULL;
        }
        else
        {
            $sellerId = $request->session('sellerId');
            //$sellerId = 1;
            try{
                $data1 = Db::query('select sellerName, phoneNumber, balance, bankName, bankCard, realName, sellerImgUrl from seller where sellerId = :sellerId',
                    ['sellerId' => $sellerId]);
                $data = $data1[0];
                $code = 0;
                $msg = $sellerId;
            }catch(Exception $e){
                $code = 3;
                $msg =$e->getMessage();
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
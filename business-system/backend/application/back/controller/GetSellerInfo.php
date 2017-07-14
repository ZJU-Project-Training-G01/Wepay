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
        if($request->session('login')!='true')
        {
            $code = 2;
            $msg = '您还未登录';
            $data = NULL;
        }
        else
        {
            $sellerId = $request->session('sellerId');
            try{
                $data1 = Db::query('select sellerName, phoneNumber, balance, bankCard, realName, sellerImgUrl from seller where sellerId = :sellerId',
                    ['sellerId' => $sellerId]);
                $data = $data1[0];
                $code = 0;
                $msg = '';
            }
        }


    }
}
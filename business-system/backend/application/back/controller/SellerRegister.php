<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/14
 * Time: 14:48
 */

namespace app\back\controller;
use think\Controller;
use think\Db;
use think\Request;
use think\Session;
use Exception;

class SellerRegister extends Controller
{
    public function SellerRegister()
    {
        $request = Request::instance();
        $realName = $request->post('realName');
        $sellerName = $request->post('sellerName');
        $sellerPassword = $request->post('sellerPassword');
        $phoneNumber = $request->post('phoneNumber');

        //test
        /*$realName = 'xiaoming';
        $sellerName = '小明';
        $sellerPassword = 123;
        $phoneNumber = 123;*/

        if(!isset($phoneNumber) || !isset($sellerPassword))
        {
            $code = 2;
            $msg = '电话号码和密码不能为空';
            $data = NULL;
        }
        else
        {
            try{
                Db::execute('insert into seller (sellerName, realName, sellerPassword, phoneNumber) values (:sellerName, :realName, :sellerPassword, :phoneNumber)',
                    ['sellerName' => $sellerName, 'realName' => $realName, 'sellerPassword' => $sellerPassword, 'phoneNumber' => $phoneNumber]);
                $code = 0;
                $msg = '注册成功';
                $data = NULL;
            }catch(Exception $e){
                $code = 3;
                $msg = $e->getMessage();
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
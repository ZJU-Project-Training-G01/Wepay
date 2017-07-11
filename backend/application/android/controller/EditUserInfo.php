<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/11
 * Time: 10:40
 */

namespace app\android\controller;
use think\Controller;
use think\Db;
use think\Request;
use think\Session;

class EditUserInfo extends Controller
{
    public function EditUserInfo()
    {
        $request = Request::instance();

        $buyerName = $request->post('userName');
        $realName = $request->post('realName');
        $phoneNumber = $request->post('phone');
        $address = $request->post('address');
        /*$buyerName = '小敏';
        $realName = 'xiaoming';
        $phoneNumber = 10016;
        $address = 'baita';*/


        if($request->session('login')=='false')
        {
            //返回上个页面；
            $code = 1;
            $msg = '用户未登录';
            $data = NULL;
        }
        $buyerId = $request->session('buyer_id');
        //$buyerId = 1;
        try{
            $data = Db::query('update buyer set buyerName = :buyerName, realName = :realName, phoneNumber = :phoneNumber, address = :address where buyerId = :buyerId',
                ['buyerName'=>$buyerName, 'realName'=>$realName, 'phoneNumber'=>$phoneNumber, 'address'=>$address, 'buyerId'=>$buyerId]);
            $code = 0;
            $msg = NULL;
            $data = NULL;
        }catch(Exception $e){
            $code = 1;
            $msg = $e->getMessage();
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
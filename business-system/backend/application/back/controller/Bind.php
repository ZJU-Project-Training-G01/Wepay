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
use Exception;

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
            try{
                Db::execute('update seller set bankCard = :bankCard , bankName = :bankName where sellerId = :sellerId;',
                    ['bankCard' => $bankCard, 'bankName' => $bankName, 'sellerId' => $sellerId]);
                $code = 0;
                $msg = NULL;
                $data = NULL;
            }catch (Exception $e){
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
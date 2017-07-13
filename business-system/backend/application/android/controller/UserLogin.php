<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/12
 * Time: 15:45
 */

namespace app\android\controller;
use think\Controller;
use think\Db;
use think\Request;
use think\Session;

class UserLogin extends  Controller
{
    public function UserLogin()
    {
        $request = Request::instance();
        $email = $request->post('email');
        $password = $request->post('password');
        /*$email = '';
        $password = '123';*/
        $data1 = Db::query('select buyerId from buyer where email = :email and buyerPassword = :password',['email'=>$email, 'password'=>$password]);
        if(count($data1))
        {
            $code = 0;
            $msg = '';
            $data = NULL;
            Session::set('buyer_id', $data1[0]['buyerId']);
            Session::set('login','true');
        }
        else
        {
            $code = 1;
            $msg = '登录失败，邮箱或密码输入错误';
            $data = NULL;
        }
        $res = [
            'code' => $code,
            'msg' => $msg,
            'data' => $data,
        ];
        echo json_encode($res, JSON_UNESCAPED_UNICODE);
        /*Session::set('buyer_id', 1);
        Session::set('login','true');
        $res = [
            'code' => 0,
            'msg' => '',
            'data' => NULL,
        ];
        echo json_encode($res, JSON_UNESCAPED_UNICODE);*/
    }
}

?>
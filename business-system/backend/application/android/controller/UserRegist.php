<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/13
 * Time: 11:25
 */

namespace app\android\controller;
use think\Controller;
use think\Db;
use think\Request;
use think\Session;
use Exception;

class UserRegist extends  Controller
{
    public function UserRegist()
    {
        $request = Request::instance();
        $email = $request->post('email');
        $password = $request->post('password');

        //test
        /*$email = 'hahah';
        $password = '1243';*/

        if(!isset($email) || !isset($password))
        {
            $code = 1;
            $msg = '邮箱和密码不能为空';
            $data = NULL;
        }
        else
        {
            try{
                Db::execute('insert into buyer (email, buyerPassword) values (:email, :password)',['email' => $email, 'password' => $password]);
                $data1 = Db::query('select buyerId from buyer where email = :email and buyerPassword = :password',
                    ['email' => $email, 'password' => $password]);
                Session::set('buyer_id', $data1[0]['buyerId']);
                Session::set('login','true');
                $code = 0;
                $data = NULL;
                $msg = '';
            }catch (Exception $e){
                $code = 1;
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
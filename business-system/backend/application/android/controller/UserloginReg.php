<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/9
 * Time: 15:13
 */

namespace app\android\controller;
use think\Controller;
use think\Db;
use think\Request;
use think\Session;

class UserloginReg extends Controller
{
    public function UserloginReg()
    {
        /*$request = Request::instance();
        $email = $request->post('email');
        $password = $request->post('password');
        /*$email = 'dsaa';
        $password = '123';*/
        $data = array();
        $data1 = Db::query('select buyerId from buyer where email = :email and buyerPassword = :password',['email'=>$email, 'password'=>$password]);
        //print_r($data1);
        if(count($data1)!=0)
        {
            $result = 0;
            //存session
            Session::set('buyer_id',$data1[0]['buyerId']);
            Session::set('login','true');
        }
        else
        {
            $data2 = Db::query('select buyerId from buyer where email = :email ',['email'=>$email]);
            if(count($data2)!=0)
            {
                $result = 1;
                Session::set('login','false');
            }
            else
            {
                Db::query('insert into buyer (email, buyerPassword) values (:email, :password)',['email'=>$email, 'password'=>$password]);
                $result = 2;
                //存session
                Session::set('login','true');
                $data3 = Db::query('select max(buyerId) as buyerId from buyer ');
                Session::set('buyer_id', $data3[0]['buyerId']);
            }
        }
        $data['result'] = $result;
        echo json_encode($data, JSON_UNESCAPED_UNICODE);
    }
}

?>

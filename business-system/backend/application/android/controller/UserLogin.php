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
        Session::set('buyer_id', 1);
        Session::set('login','true');
    }
}
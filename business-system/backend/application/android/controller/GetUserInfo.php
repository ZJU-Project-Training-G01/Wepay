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

class GetUserInfo extends Controller
{
    public function GetUserInfo()
    {
        $request = Request::instance();
        //$buyerId = $request->session('buyer_id');
        $buyerId = 1 ;
        try {
            $data = Db::query('select buyerName as userName, realName, phoneNumber as phone, address from buyer where buyerId = :buyerId',['buyerId' => $buyerId]);
            //print_r($data);
            echo json_encode($data, JSON_UNESCAPED_UNICODE);
        }
        catch (\Exception $e){
            echo $e->getMessage();
        }
    }
}

?>
<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/11
 * Time: 9:27
 */

namespace app\android\controller;
use think\Controller;
use think\Db;
use think\Request;

class GetGood extends  Controller
{
    public  function GetGood()
    {
        $request = Request::instance();
        $goodId = $request->get('good_id');
        //$goodId = 1;
        $data = Db::query('select goodId as good_id, goodName as good_name, unitPrice as unit_price, imgUrl as img_url, goodInfo as good_info from good where goodId = :goodId',['goodId'=> $goodId]);
        $res = array();
        if(!empty($data))
            $res = $data[0];
        echo json_encode($res, JSON_UNESCAPED_UNICODE);
    }
}

?>
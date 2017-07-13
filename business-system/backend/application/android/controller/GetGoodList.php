<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/10
 * Time: 10:11
 */

namespace app\android\controller;
use think\Controller;
use think\Db;
use think\Request;

class GetGoodList extends  Controller
{
    public function GetGoodList()
    {
        $request = Request::instance();
        $res = array();
        $good = Db::query('select goodId as good_id, goodName as good_name, unitPrice as unit_price, imgUrl as img_url, goodInfo as good_info from good');
        //print_r($good);
        $res['data'] = $good;
        echo json_encode($res, JSON_UNESCAPED_UNICODE);
    }
}
?>
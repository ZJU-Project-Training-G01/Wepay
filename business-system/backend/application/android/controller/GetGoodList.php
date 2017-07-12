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
        $good = Db::query('select goodId as good_id, goodName as good_name, unitPrice as unit_price, imgUrl as img_url, goodInfo as good_info from good');
        //print_r($good);
        echo json_encode($good, JSON_UNESCAPED_UNICODE);
    }
}
?>
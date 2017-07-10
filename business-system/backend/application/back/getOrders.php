<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/8
 * Time: 11:41
 */
namespace  app\back\controller;
use think\Controller;
use think\Db;
use think\Request;

public function getorders()
{
    $request = Request::instance();
    $pageNumber = 1;
    $pageSize = 2;
    //$pageSize = $request->post('pageSize');
    //$pageNumber = $request->post('pageNum');
    $result = Db::table('order')->chunk($pageSize * $pageNumber, function($orders){
        foreach ($orders as $order)
        {

        }
    });
    print_r($result);
}
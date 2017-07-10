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

class GetOrders extends Controller
{
    public function getorders()
    {
        $request = Request::instance();
        $pageNumber = 1;
        $pageSize = 2;
        //$pageSize = $request->post('pageSize');
        //$pageNumber = $request->post('pageNumber');
        $recordP = ($pageNumber-1)*$pageSize;
        $data = Db::query('select * from orders limit :recordP,:pageSize',['recordP'=>$recordP, 'pageSize'=>$pageSize]);
        return json_encode($data);
    }
}
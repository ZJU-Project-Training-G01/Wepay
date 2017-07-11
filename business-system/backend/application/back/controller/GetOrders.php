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
use Exception;

class GetOrders extends Controller
{
    public function GetOrders()
    {
        $request = Request::instance();
        $pageNumber = 2;
        $pageSize = 2;
        $status = 0;
        //$pageSize = $request->post('pageSize');
        //$pageNumber = $request->post('pageNumber');
        //$status = $request->post('status');
        $recordP = ($pageNumber-1)*$pageSize;
        if($status == -1)
        {
            try {
                $data = Db::query('select * from orders limit :recordP,:pageSize', ['recordP' => $recordP, 'pageSize' => $pageSize]);
                $code = 0;
                $msg = NULL;
            } catch (Exception $e) {
                $code = 2;
                $msg = $e->getMessage();
                $data = NULL;
            }
        }
        else
        {
            try {
                $data = Db::query('select * from orders where orderStatus = :status limit :recordP,:pageSize', ['recordP' => $recordP, 'pageSize' => $pageSize, 'status' => $status]);
                $code = 0;
                $msg = NULL;
            } catch(Exception $e) {
                $code = 2;
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
        //return json_encode($data);
    }
}
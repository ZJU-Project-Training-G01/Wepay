<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/14
 * Time: 21:11
 */

namespace app\back\controller;
use think\Controller;
use think\Db;
use think\Request;
use think\Session;
use Exception;

class DeleteGood extends Controller
{
    public function DeleteGood()
    {
        $request = Request::instance();
        $goodId = $request->post('goodId');

        //test
        /*$goodId = 9;
        Session::set('login', 'true');*/

        if($request->session('login')!='true')
        {
            $code = 2;
            $msg = '您还未登录';
            $data = NULL;
        }
        else
        {
            try{
                Db::execute('update good set amount = -1 where goodId = :goodId',['goodId' => $goodId]);
                $code = 0;
                $msg = $goodId;
                $data = NULL;
            }catch(Exception $e){
                $code = 3;
                $msg = '下架该商品失败';
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

?>
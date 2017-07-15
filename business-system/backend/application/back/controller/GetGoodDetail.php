<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/15
 * Time: 10:43
 */

namespace app\back\controller;
use think\Controller;
use think\Db;
use think\Request;
use think\Session;
use Exception;

class GetGoodDetail extends Controller
{
    public function GetGoodDetail()
    {
        $request = Request::instance();
        $goodId = $request->post('goodId');

        //test
        /*$goodId = 1;
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
                $data1 = Db::query('select goodId, goodName, imgUrl, unitPrice, amount, goodInfo from good where goodId = :goodId',
                    ['goodId' => $goodId]);
                $data = $data1[0];
                $code = 0;
                $msg = $goodId;
            }catch(Exception $e){
                $code = 3;
                $data = NULL;
                $msg = $e->getMessage();
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
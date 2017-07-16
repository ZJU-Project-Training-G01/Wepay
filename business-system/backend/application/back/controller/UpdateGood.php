<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/14
 * Time: 15:50
 */

namespace app\back\controller;
use think\Controller;
use think\Db;
use think\Request;
use think\Session;
use Exception;

class UpdateGood extends Controller
{
    public function UpdateGood()
    {
        $request = Request::instance();
        $goodId = $request->post('goodId');
        $goodName = $request->post('goodName');
        $amount = $request->post('amount');
        $unitPrice = $request->post('unitPrice');
        $goodInfo = $request->post('goodInfo');
        $imgUrl = $request->post('imgUrl');

        //test
        /*Session::set('login', 'true');
        $goodId = 9;
        $goodName = '鱼干';
        $amount = 50;
        $unitPrice = 5;
        $goodInfo = 'xxx';*/

        if($request->session('login')!='true')
        {
            $code = 2;
            $msg = '您还未登录';
            $data = NULL;
        }
        else
        {
            $sellerId = $request->session('sellerId');
            try{
                Db::execute('update good set goodName = :goodName, amount = :amount, unitPrice = :unitPrice, imgUrl = :imgUrl, goodInfo = :goodInfo where goodId = :goodId',
                    ['goodId' => $goodId, 'goodName' => $goodName, 'amount' => $amount, 'unitPrice' => $unitPrice,'imgUrl' => $imgUrl, 'goodInfo' => $goodInfo]);
                $data = NULL;
                $code = 0;
                $msg = $goodId;
            }catch (Exception $e)
            {
                $data = NULL;
                $code = 3;
                $msg = '更改商品信息发生错误';
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
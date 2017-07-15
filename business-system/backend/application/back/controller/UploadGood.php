<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/14
 * Time: 15:13
 */

namespace app\back\controller;
use think\Controller;
use think\Db;
use think\Request;
use think\Session;
use Exception;

class UploadGood extends Controller
{
    public function UploadGood()
    {
        $request = Request::instance();
        $goodName = $request->post('goodName');
        $amount = $request->post('amount');
        $unitPrice = $request->post('unitPrice');
        $goodInfo = $request->post('goodInfo');

        //test
        /*Session::set('login', 'true');
        $goodName = 'douzi';
        $amount = 100;
        $unitPrice = 3;
        $goodInfo = 'xxxx';*/

        if($request->session('login')!='true')
        {
            $code = 2;
            $msg = '您还未登录';
            $data = NULL;
        }
        else
        {
            $sellerId = $request->session('sellerId');
            //$sellerId = 1;
            try{
                Db::execute('insert into good (goodName, amount, unitPrice, goodInfo, soldAmount, sellerId) values (:goodName, :amount, :unitPrice, :goodInfo, 0, :sellerId)',
                    ['goodName' => $goodName, 'amount' => $amount, 'unitPrice' => $unitPrice, 'goodInfo' => $goodInfo, 'sellerId' => $sellerId]);
                $data1 = Db::query('select goodId from good where goodName = :goodName',['goodName' => $goodName]);
                $data = $data1[0];
                $code = 0;
                $msg = $sellerId;
            }catch (Exception $e)
            {
                $code = 3;
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
    }
}
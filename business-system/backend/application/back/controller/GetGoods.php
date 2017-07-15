<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/14
 * Time: 11:12
 */
namespace app\back\controller;
use think\Controller;
use think\Db;
use think\Request;
use think\Session;
use Exception;

class GetGoods extends Controller
{
    public function GetGoods()
    {
        $request = Request::instance();
        $pageSize = $request->post('pageSize');
        $pageNumber = $request->post('pageNumber');
        $keyword = $request->post('keyword');
        $recordP = ($pageNumber-1)*$pageSize;
        $a = '%';
        $keyword = $a.$keyword.$a;

        //test
        /*$pageSize = 3;
        $pageNumber = 1;
        //$keyword = NULL;
        $a = '%';
        $keyword = $a.$keyword.$a;
        //echo $keyword;
        Session::set('login', 'true');*/

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
            try {
                $data = Db::query('select goodId, goodName, imgUrl, amount, unitPrice, soldAmount as sellAmount from good where amount >= 0 and sellerId = :sellerId and goodName like :keyword order by soldAmount DESC limit :recordP, :pageSize',
                    [ 'sellerId' => $sellerId, 'keyword' => $keyword, 'recordP' => $recordP, 'pageSize' => $pageSize]);
                $code = 0;
                $msg = $sellerId;
            }catch (Exception $e){
                $code = 3;
                $msg = $e->getMessage();
                $data = NULL;
            }
            $res = [
                'code' => $code,
                'msg' => $msg,
                'data' => $data,
            ];
            echo json_encode($res, JSON_UNESCAPED_UNICODE);
        }
    }
}
?>
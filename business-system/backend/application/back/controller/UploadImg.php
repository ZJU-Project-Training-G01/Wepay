<?php
/**
 * Created by PhpStorm.
 * User: 12964
 * Date: 2017/7/16
 * Time: 9:22
 */

namespace app\back\controller;
use think\Controller;
use think\Db;
use think\Request;
use think\Session;
use Exception;

class UploadImg extends Controller
{
    public function UploadImg()
    {
        $request = Request::instance();
        if ($request->session('login') != 'true')
        {
            $code = 2;
            $msg = '您还未登录';
            $data = NULL;
        }
        else
        {
            $sellerId = $request->session('sellerId');
            $filename = $_FILES['file']['name'];
            $meta = $_POST;
            $destination = $meta['targetPath'].$filename;
            move_uploaded_file($_FILES['file']['tmp_name'], "D:\wampServer\wamp64\www\WePay\business-system\\".$destination);
           try{
                Db::execute('update seller set sellerImgUrl = :destination where sellerId = :sellerId',
                    ['destination' => $destination, 'sellerId' => $sellerId]);
                $code = 0;
                $msg = $sellerId;
                $data = NULL;
            }catch(Exception $e){
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

                
?>
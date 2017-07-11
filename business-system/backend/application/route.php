<?php
// +----------------------------------------------------------------------
// | ThinkPHP [ WE CAN DO IT JUST THINK ]
// +----------------------------------------------------------------------
// | Copyright (c) 2006~2016 http://thinkphp.cn All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.apache.org/licenses/LICENSE-2.0 )
// +----------------------------------------------------------------------
// | Author: liu21st <liu21st@gmail.com>
// +----------------------------------------------------------------------

return [
    '__pattern__' => [
        'name' => '\w+',
    ],
    '[hello]'     => [
        ':id'   => ['index/hello', ['method' => 'get'], ['id' => '\d+']],
        ':name' => ['index/hello', ['method' => 'post']],
    ],
    'SellersGetOrders' => 'back/GetOrders/GetOrders',
    'GetOrders' => 'android/GetOrders/GetOrders',
    'GetUserInfo' => 'android/GetUserInfo/GetUserInfo',
    'EditUserInfo' => 'android/EditUserInfo/EditUserInfo',
    'UserloginReg' => 'android/UserloginReg/UserloginReg',
    'GetGoodList' => 'android/GetGoodList/GetGoodList',
    'MakeNewOrder' => 'android/MakeNewOrder/MakeNewOrder',
    'GetGood' => 'android/GetGood/GetGood',
];

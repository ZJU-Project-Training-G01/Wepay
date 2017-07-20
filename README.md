## WePay1.0

#### 数据库

如若需要知道数据库的账号和密码以及IP请联系@AChaoZJU

#### 前后端的接口约定（B端）

```
$res=array{
    'code'=>$code,
    'msg'=>$msg,//仅限于当$code>1，也就是错误已知时，此变量需要返回
    'data'=>$data
}
echo json_encode($res, JSON_UNESCAPED_UNICODE);
```

$code说明：0代表成功，>0代表已知错误，发生已知错误时，$code和$msg具体如何请在接口中约定
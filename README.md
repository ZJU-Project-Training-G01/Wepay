#WePay1.0

####连接远程数据库
- 需要找到本地mysql的可执行程序的目录,在那个目录执行下面的命令
- 远程连接数据库即可对数据库进行增删改查
- Windows下dos命令：mysql -uroot -h 120.77.34.254 -p
- password:exciting
- Linux or Mac or Window Powershell命令 ./mysql -uroot -h 120.77.34.254 -p
- password:exciting
- php文件连接函数中，服务器ip把localhost改为120.77.34.254，账号改为root,密码改为exciting 端口名仍为3306，数据库名仍为teaching_db
- 后端统一include ‘connect.php’连接远程数据库

####B端的前后端接口约定(不知道C端能否借鉴)
```
$res=array{
    'code'=>$code,
    'msg'=>$msg,//仅限于当$code>1，也就是错误已知时，此变量需要返回
    'data'=>$data
}
echo json_encode($res, JSON_UNESCAPED_UNICODE);
```
$code说明：0代表成功，1代表未知错误，$code>1代表已知错误，发生已知错误时，$code和$msg具体如何请在接口中约定
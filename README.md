#WePay

####连接远程数据库
- 需要找到本地mysql的可执行程序的目录,在那个目录执行下面的命令
- 远程连接数据库即可对数据库进行增删改查
- Windows下命令：mysql -uroot -h 120.77.34.254 -p
password:exciting
- Linux or Mac命令 ./mysql -uroot -h 120.77.34.254 -p
password:exciting
- php文件连接函数中，服务器ip把localhost改为120.77.34.254，账号改为root,密码改为exciting 端口名仍为3306，数据库名为wepay
- 后端统一include ‘connect.php’连接远程数据库

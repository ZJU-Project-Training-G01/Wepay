
####订单模块的接口
|功能名      |前端调用后端的URL                             | 后端返回前端的数据(json)                   |备注          |后端是否完成    |
|-----------------|-------------------------------------------|--------------------------------------------|---------------|----------|
|获取订单|getOrders.php     | 失败:{"if_success":0,"err_message":"xxx"},成功:{"if_success":1,"question":"xxx"}|"if_success"为整数0或1 err_message包括但不限于:1."不存在此用户,返回上一步重新输入";2."您未设置密码问题,请回忆您的初始密码或者联系网站管理员"|是
|验证用户的回答密码问题是否正确|index/php/verify_question.php?id=xxx&answer=xxx      | 失败:{"if_success":0,"err_message":"xxx"},成功:"if_success":1|"if_success"为整数0或1 err_message包括但不限于:1."回答错误,请返回上一步重新输入答案或联系网站管理员"|是
|重置密码|index/php/reset_pwd.php?id=xxx&new_pwd=xxx      | 失败:{"if_success":0,"err_message":"xxx"},成功:"if_success":1|无|是
|获取友情链接| get_friend_link.php                        | [{ "link_name":"xxx", "link_address":"xxx }, ...] | |

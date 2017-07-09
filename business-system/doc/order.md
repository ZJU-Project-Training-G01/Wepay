#### 订单模块
|功能名      |前端调用后端的URL                             | 后端返回前端的数据(json)                   |备注          |后端是否完成    |
|-----------------|-------------------------------------------|--------------------------------------------|---------------|----------|
|获取订单功能|url:GetOrders.php,method:post,data:{pageSize:'xxx',pageNum:'xxx',status:'xxx'}      | data:[{orderId:'xxx',goodName:'xxx',imgUrl:'xxx',amount:'xxx',unitPrice:'xxx',orderStatus:'xxx',orderTime:'xxx'},{...}]|请求参数中：status为-1，0，1，-1表示所有订单，0表示未发货，1表示未收货，1|否

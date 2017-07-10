#### 订单模块
|功能名      |前端调用后端的URL                             | 后端返回前端的数据(json)                   |备注          |后端是否完成    |
|-----------------|-------------------------------------------|--------------------------------------------|---------------|----------|
|获取订单功能|url:GetOrders.php,method:post,data:{pageSize:'xxx',pageNum:'xxx'}      | data:[{orderId:'xxx',goodName:'xxx',imgUrl:'xxx',amount:'xxx',unitPrice:'xxx',orderStatus:'xxx',orderTime:'xxx'},{...}]|无|否
|获取商品功能|url:GetGoods.php,method:post,data:{pageSize:'xxx',pageNum:'xxx',keyword:'xxx'}      | data:[{goodId:'xxx',goodName:'xxx',imgUrl:'xxx',amount:'xxx',unitPrice:'xxx'},{...}]|amount是商品表库存，keyword是搜索关键词,这个字段可选|否
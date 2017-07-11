 (function() {
     angular
         .module('goodUpload', [])
         .controller('goodUpload', ['$scope', '$http', '$location', '$stateParams',
             function($scope, $http, $location, $stateParams) {
                 if ($stateParams.ifUpdate === 'update') {
                     $scope.title = '编辑商品信息';
                     $scope.operation = '编辑';
                     $scope.sure = '确定';
                     $scope.url = 'frontend/static/jsons/update.json';
                 } else {
                     $scope.title = '上架新商品';
                     $scope.sure = $scope.operation = '上架';
                     $scope.url = 'frontend/static/jsons/upload.json';
                 }

                 $scope.toDetail = function() {
                     $location.path('good/' + $scope.goodId)
                 }
                 $scope.continue = function() {
                     if ($stateParams.ifUpdate !== 'update') {
                         $scope.goodInfo = $scope.unitPrice = $scope.amount = $scope.goodName = undefined;
                     }
                     $scope.ifFeedback = false;
                 }
                 $scope.upload = function() {
                     $http({
                         url: $scope.url,
                         method: 'post',
                         data: { goodId: $scope.goodId, goodName: $scope.goodName, unitPrice: $scope.unitPrice, amount: $scope.amount, info: $scope.info }
                     }).then(function(data) {
                         $scope.ifFeedback = true;
                         let code = data.data.code;
                         if (code === 0) {
                             $scope.goodId = data.data.data.goodId;
                         }
                     })
                 }
             }
         ])
 })();
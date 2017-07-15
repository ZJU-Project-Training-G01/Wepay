     (function() {
         angular
             .module('goodUpload', [])
             .controller('goodUpload', ['$scope', '$http', '$location', '$stateParams', '$rootScope',
                 function($scope, $http, $location, $stateParams, $rootScope) {
                     $scope.goodDetail = {};
                     if ($stateParams.ifUpdate === 'update') {
                         $scope.title = '编辑商品信息';
                         $scope.operation = '编辑';
                         $scope.sure = '确定';
                         $scope.url = 'backend/public/UpdateGood';
                         $scope.goodDetail = $rootScope.goodDetail;
                     } else {
                         $scope.title = '上架新商品';
                         $scope.sure = $scope.operation = '上架';
                         $scope.url = 'frontend/static/jsons/upload.json';
                     }
                     $scope.toDetail = function() {
                         $location.path('good/' + $scope.goodDetail.goodId);
                     }
                     $scope.continue = function() {
                         if ($stateParams.ifUpdate !== 'update') {
                             $scope.goodDetail = undefined;
                         }
                         $scope.ifFeedback = false;
                     }
                     $scope.upload = function() {
                         $http({
                             url: $scope.url,
                             method: 'post',
                             data: $scope.goodDetail
                         }).then(function(data) {
                             $scope.ifFeedback = true;
                             let code = data.data.code;
                             if (code === 0) {
                                 if ($stateParams.ifUpdate !== 'update') {
                                     $scope.goodDetail.goodId = data.data.data.goodId;
                                 }
                             }
                         })
                     }
                 }
             ])
     })();
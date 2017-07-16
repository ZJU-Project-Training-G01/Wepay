     (function() {
         angular
             .module('goodUpload', [])
             .component('upload', {
                 templateUrl: 'frontend/pages/upload/upload.html',
                 controller: 'upload'
             })
             .controller('goodUpload', ['$scope', '$http', '$location', '$stateParams', '$rootScope',
                 function($scope, $http, $location, $stateParams, $rootScope) {
                     $scope.goodDetail = {};
                     $scope.$on('fileGoods', function(e, filePath) {
                         $scope.goodDetail.imgUrl = filePath;
                     })
                     if ($stateParams.ifUpdate === 'update') {
                         $scope.title = '编辑商品信息';
                         $scope.operation = '编辑';
                         $scope.sure = '确定';
                         $scope.url = 'backend/public/UpdateGood';
                         $scope.goodDetail = $rootScope.goodDetail;
                         $scope.goodDetail.unitPrice = $scope.goodDetail.unitPrice.substring(1);
                     } else {
                         $scope.title = '上架新商品';
                         $scope.sure = $scope.operation = '上架';
                         $scope.url = 'backend/public/UploadGood';
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
                         if (!$scope.goodDetail.imgUrl) {
                             $scope.$emit('transferErrorMsg', '请上传图片');
                             return;
                         } else {
                             $scope.goodDetail.imgUrl = 'http://120.77.34.254/business-system/' + $scope.goodDetail.imgUrl;
                         }
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
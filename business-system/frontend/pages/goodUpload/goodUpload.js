 (function() {
     angular
         .module('goodUpload', [])
         .controller('goodUpload', ['$scope', '$http', '$location',
             function($scope, $http, $location) {
                 $scope.toDetail = function() {
                     $location.path('good/' + $scope.goodId)
                 }
                 $scope.continue = function() {
                     $scope.goodInfo = $scope.unitPrice = $scope.amount = $scope.goodName = undefined;
                     $scope.ifFeedback = false;
                 }
                 $scope.upload = function() {
                     $http({
                         url: 'frontend/static/jsons/upload.json',
                         method: 'post',
                         data: { goodName: $scope.goodName, unitPrice: $scope.unitPrice, amount: $scope.amount, info: $scope.info }
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
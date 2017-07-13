(function() {
    angular.module('goodDetail', [])
        .controller('goodDetail', ['$scope', '$http', '$stateParams', 'setPricePrecision', '$location', '$rootScope',
            function($scope, $http, $stateParams, setPricePrecision, $location, $rootScope) {
                $scope.ifFeedback = false;
                $scope.update = function() {
                    $rootScope.goodDetail = $scope.goodDetail;
                    $location.path('goodUpload/update')
                }
                $scope.toGoods = function() {
                    $location.path('good')
                }
                $scope.delete = function() {
                    $http({
                        url: 'frontend/static/jsons/delete.json',
                        method: 'post',
                        data: { goodId: $scope.goodDetail.goodId }
                    }).then(function(data) {
                        let code = data.data.code;
                        if (code === 0) {
                            $scope.ifFeedback = true;
                        } else if (code !== 0) {
                            $scope.$emit('transferErrorMsg', data.data.msg);
                        }
                    });
                }
                $scope.goodDetailHttp = function(goodId) {
                    $http({
                        url: 'frontend/static/jsons/goodDetail.json',
                        method: 'post',
                        data: { goodId: goodId }
                    }).then(function(data) {
                        $scope.goodDetail = data.data.data;
                        $scope.goodDetail.unitPrice = setPricePrecision($scope.goodDetail.unitPrice);
                    }).then(function(data) {});
                }
                $scope.goodDetailHttp($stateParams.goodId);
            }
        ])
})();
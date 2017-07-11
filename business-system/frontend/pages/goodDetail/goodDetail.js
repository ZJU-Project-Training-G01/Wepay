(function() {
    angular.module('goodDetail', [])
        .controller('goodDetail', ['$scope', '$http', '$stateParams', 'setPricePrecision', '$location',
            function($scope, $http, $stateParams, setPricePrecision, $location) {
                $scope.ifFeedback = false;
                $scope.goodId = $stateParams.goodId;
                $scope.update = function() {
                    $location.path('goodUpload/update')
                }
                $scope.toGoods = function() {
                    $location.path('good')
                }
                $scope.delete = function() {
                    $http({
                        url: 'frontend/static/jsons/delete.json',
                        method: 'post',
                        data: { goodId: $scope.goodId }
                    }).then(function(data) {
                        let code = data.data.code;
                        if (code === 0) {
                            $scope.ifFeedback = true;
                        }
                    })
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
                $scope.goodDetailHttp($scope.goodId);
            }
        ])
})();
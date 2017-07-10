(function() {
    angular.module('goodItemDetail', [])
        .controller('goodItemDetailCtrl', ['$scope', '$http', '$routeParams', 'setPricePrecision',
            function($scope, $http, $routeParams, setPricePrecision) {
                console.log
                $scope.goodDetailHttp = function(goodId) {
                    $http({
                        url: 'frontend/static/json/goodDetai.json',
                        method: 'post',
                        data: { goodId: goodId }
                    }).then(function(data) {
                        $scope.goodDetail = data.data.data;
                        console.log($scope.goodDetail);
                        $scope.goodDetail.unitPrice = setPricePrecision($scope.goodDetail.unitPrice);
                    }).then(function(data) {});
                }
                $scope.goodDetailHttp($routeParams.goodId);
            }
        ])
})();
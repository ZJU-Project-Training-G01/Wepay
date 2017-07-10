(function() {
    angular.module('goodItem', [])
        .controller('goodItemCtrl', ['$scope', '$http', '$location', 'setPricePrecision',
            function($scope, $http, $location, setPricePrecision) {
                $scope.pageSize = 16;
                $scope.pageNumber = 1;
                $scope.toDetail = function(good) {
                    $location.path('good/' + good.goodId);
                };
                $scope.goodHttp = function(pageNumber) {
                    $http({
                        url: 'frontend/static/json/goods.json',
                        method: 'post',
                        data: { pageSize: $scope.pageSize, pageNumber: pageNumber }
                    }).then(function(data) {
                        $scope.goods = data.data.data;
                        $scope.total = $scope.goods.length;
                        $scope.total = 17;
                        $scope.goods.forEach(function(val) {
                            val.unitPrice = setPricePrecision(val.unitPrice);
                        });
                    }).then(function(data) {});
                };
                $scope.goodHttp($scope.pageNumber);
            }
        ])
})();
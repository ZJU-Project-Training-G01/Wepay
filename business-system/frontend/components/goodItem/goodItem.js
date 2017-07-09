(function() {
    angular.module('goodItem', [])
        .controller('goodItemCtrl', ['$scope', '$http', 'setPricePrecision',
            function($scope, $http, setPricePrecision) {
                $scope.pageSize = 16;
                $scope.pageNumber = 1;
                $scope.goodHttp = function() {
                    $http({
                        url: 'frontend/static/json/goods.json',
                        method: 'post',
                        data: { pageSize: $scope.pageSize, pageNumber: $scope.pageNumber }
                    }).then(function(data) {
                        $scope.goods = data.data.data;
                        $scope.total = $scope.goods.length;
                        $scope.total = 17;
                        $scope.goods.forEach(function(val) {
                            val.unitPrice = setPricePrecision(val.unitPrice);
                        });
                    }).then(function(data) {});
                }
                $scope.goodHttp();
            }
        ])
})();
angular.module('goodItem', [])
    .controller('goodItemCtrl', ['$scope', '$http', 'setPricePrecision',
        function($scope, $http, setPricePrecision) {
            $scope.goodHttp = function() {
                $http({
                    url: 'frontend/static/json/goods.json',
                    method: 'post',
                    data: {}
                }).then(function(data) {
                    $scope.goods = data.data.data;
                    $scope.goods.forEach(function(val) {
                        val.unitPrice = setPricePrecision(val.unitPrice);
                    });
                }).then(function(data) {});
            }
            $scope.goodHttp();
        }
    ])
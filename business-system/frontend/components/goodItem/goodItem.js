(function() {
    angular.module('goodItem', [])
        .controller('goodItemCtrl', ['$scope', '$http', '$location', 'setPricePrecision',
            function($scope, $http, $location, setPricePrecision) {
                $scope.pageSize = 16;
                $scope.pageNumber = 1;
                $scope.toDetail = function(good) {
                    $location.path('good/' + good.goodId);
                };
                $scope.$on('receiveKeyword', function(e, keyword) {
                    $scope.goodHttp($scope.pageNumber, keyword);
                });
                $scope.goodHttp = function(pageNumber, keyword) {
                    $http({
                        url: 'backend/public/GetGoods',
                        method: 'post',
                        data: { pageSize: $scope.pageSize, pageNumber: pageNumber, keyword: keyword }
                    }).then(function(data) {
                        $scope.goods = data.data.data;
                        $scope.total = data.data.msg;
                        $scope.goods.forEach(function(val) {
                            val.unitPrice = setPricePrecision(val.unitPrice);
                        });
                    }).then(function(data) {});
                };
                $scope.goodHttp($scope.pageNumber);
            }
        ]);
})();
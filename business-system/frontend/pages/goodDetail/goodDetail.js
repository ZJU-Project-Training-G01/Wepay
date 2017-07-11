(function() {
    angular.module('goodDetail', [])
        .controller('goodDetail', ['$scope', '$http', '$stateParams', 'setPricePrecision',
            function($scope, $http, $stateParams, setPricePrecision) {
                console.log
                $scope.goodDetailHttp = function(goodId) {
                    $http({
                        url: 'frontend/static/json/goodDetail.json',
                        method: 'post',
                        data: { goodId: goodId }
                    }).then(function(data) {
                        $scope.goodDetail = data.data.data;
                        $scope.goodDetail.unitPrice = setPricePrecision($scope.goodDetail.unitPrice);
                    }).then(function(data) {});
                }
                $scope.goodDetailHttp(3);
            }
        ])
})();
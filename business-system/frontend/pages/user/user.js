(function() {
    angular.module('user', [])
        .controller('user', ['$scope', '$http', 'setPricePrecision', function($scope, $http, setPricePrecision) {
            $http({
                url: 'frontend/static/jsons/user.json',
                method: 'post',
                data: {}
            }).then(function(data) {
                data = data.data.data;
                [$scope.sellerName, $scope.realName, $scope.phoneNumber, $scope.balance, $scope.bankCard, $scope.sellerImgUrl] = [data.sellerName, data.realName, data.phoneNumber, setPricePrecision(data.balance), data.bankCard, data.sellerImgUrl];
            })
        }]);
})();
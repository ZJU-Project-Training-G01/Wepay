(function() {
    angular.module('user', [])
        .controller('user', ['$scope', '$http', 'setPricePrecision', function($scope, $http, setPricePrecision) {
            $http({
                url: 'frontend/static/jsons/user.json',
                method: 'post',
                data: {}
            }).then(function(data) {
                data = data.data.data;
                $scope.sellerName = data.sellerName;
                $scope.balance = setPricePrecision($scope.balance);

            })
        }]);
})();
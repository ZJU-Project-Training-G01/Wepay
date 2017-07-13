(function() {
    angular.module('user', [])
        .controller('user', ['$scope', '$http', 'setPricePrecision', '$location', function($scope, $http, setPricePrecision, location) {
            $http({
                url: 'frontend/static/jsons/user.json',
                method: 'post',
                data: {}
            }).then(function(data) {
                let code = data.data.code;
                if (code === 0) {
                    data = data.data.data;
                    $scope.seller = data;
                    $scope.seller.balance = setPricePrecision($scope.seller.balance);
                    $scope.toBind = !$scope.seller.bankCard;
                    $scope.seller.bankCard = '未绑定';
                } else {
                    $scope.$emit('transferErrorMsg', '获取用户基础信息失败，原因:' + data.data.msg);
                }
            })
        }]);
})();
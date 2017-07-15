(function() {
    angular.module('out', [])
        .controller('out', ['$scope', '$http', '$window',
            function($scope, $http, $window) {
                $scope.out = function() {
                    if (!$scope.outMoney) {
                        $scope.$emit('transferErrorMsg', '转出金额不得为空');
                        return;
                    }
                    if (!$scope.payPassword) {
                        $scope.$emit('transferErrorMsg', '支付密码不得为空');
                        return;
                    }
                    $http({
                        url: 'backend/public/OutMoney',
                        method: 'post',
                        data: { payPassword: $scope.payPassword, outMoney: $scope.outMoney }
                    }).then(function(data) {
                        let code = data.data.code;
                        if (code === 0) {
                            $scope.$emit('transferErrorMsg', '转出成功', 'modal');
                        } else {
                            $scope.$emit('transferErrorMsg', data.data.msg);
                        }
                    })
                }
                $scope.cancel = function() {
                    $window.location.reload();
                }
            }
        ])
})();
(function() {
    angular.module('in', [])
        .controller('in', ['$scope', '$http', '$window',
            function($scope, $http, $window) {
                $scope.in = function() {
                    if (!$scope.inMoney) {
                        $scope.$emit('transferErrorMsg', '充值金额不得为空');
                        return;
                    }
                    if (!$scope.payPassword) {
                        $scope.$emit('transferErrorMsg', '支付密码不得为空');
                        return;
                    }
                    $http({
                        url: 'frontend/static/jsons/in.json',
                        method: 'post',
                        data: { payPassword: $scope.payPassword, inMoney: $scope.inMoney }
                    }).then(function(data) {
                        let code = data.data.code;
                        if (code === 0) {
                            $scope.$emit('transferErrorMsg', '充值成功', 'modal');
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
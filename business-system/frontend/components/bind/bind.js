(function() {
    angular.module('bind', [])
        .controller('bind', ['$scope', '$location', 'close',
            function($scope, $location, close) {
                $scope.bind = function() {
                    if ($scope.bankCard === undefined) {
                        $scope.$emit('transferErrorMsg', '银行卡号不得为空');
                        return;
                    }
                    if ($scope.idCard === undefined) {
                        $scope.$emit('transferErrorMsg', '身份证号不得为空');
                        return;
                    }
                    if ($scope.realName === undefined) {
                        $scope.$emit('transferErrorMsg', '姓名不得为空');
                        return;
                    }
                    if ($scope.password === undefined) {
                        $scope.$emit('transferErrorMsg', '密码不得为空');
                        return;
                    }
                }
                $scope.close = function(result) {
                    close(result, 500); // close, but give 500ms for bootstrap to animate
                };

            }
        ]);
})();
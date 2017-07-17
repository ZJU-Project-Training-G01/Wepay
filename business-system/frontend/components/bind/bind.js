(function() {
    angular.module('bind', [])
        .controller('bind', ['$scope', '$location', '$window', '$http',
            function($scope, $location, $window, $http) {
                $scope.bind = function() {
                    if ($scope.bankCard === undefined) {
                        $scope.$emit('transferErrorMsg', '银行卡号不得为空');
                        return;
                    }
                    if ($scope.bankName === undefined) {
                        $scope.$emit('transferErrorMsg', '银行名称不得为空');
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
                    $http({
                        url: 'backend/public/Bind',
                        method: 'post',
                        data: { bankName: $scope.bankName, bankCard: $scope.bankCard }
                    }).then(function(data) {
                        let code = data.data.code;
                        if (code === 0) {
                            $scope.$emit('transferErrorMsg', '绑定成功', 'modal');
                        }
                    });
                };
                $scope.cancel = function() {
                    $window.location.reload();
                };
            }
        ]);
})();
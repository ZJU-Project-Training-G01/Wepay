(function() {
    angular.module('bind', [])
        .controller('bind', ['$scope', '$location', '$window', '$http',
            function($scope, $location, $window, $http) {
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
                    $http({
                        url: 'frontend/static/jsons/bind.json',
                        method: 'post',
                        data: { bankCard: $scope.bankCard }
                    }).then(function(data) {
                        let code = data.data.code;
                        if (code === 0) {
                            $window.location.reload();
                            $scope.$emit('transferErrorMsg', '绑定成功', true);
                        }
                    })
                };
                $scope.cancel = function() {
                    $window.location.reload();
                };
            }
        ]);
})();
(function() {
    angular.module('register', [])
        .component('loginNavbar', {
            templateUrl: 'frontend/components/loginNavbar/loginNavbar.html'
        })
        .controller('register', ['$scope', '$http', '$location', function($scope, $http, $location) {
            $scope.$emit('hideNavbar');
            $scope.register = function() {
                if (!$scope.sellerName) {
                    $scope.$emit('transferErrorMsg', '用户名不得为空');
                    return;
                }
                if (!$scope.realName) {
                    $scope.$emit('transferErrorMsg', '法人真名不得为空');
                    return;
                }
                if (!$scope.phoneNumber) {
                    $scope.$emit('transferErrorMsg', '手机号码不得为空');
                    return;
                }
                if (!$scope.sellerPassword) {
                    $scope.$emit('transferErrorMsg', '密码不得为空');
                    return;
                }
                if (!$scope.reSellerPassword) {
                    $scope.$emit('transferErrorMsg', '请再次输入密码');
                    return;
                }
                if ($scope.sellerPassword !== $scope.reSellerPassword) {
                    $scope.$emit('transferErrorMsg', '密码不一致');
                    return;
                }
                $http({
                    url: 'backend/public/SellerRegister',
                    method: 'post',
                    data: { realName: $scope.realName, sellerName: $scope.sellerName, sellerPassword: $scope.sellerPassword, phoneNumber: $scope.phoneNumber }
                }).then(function(data) {
                    let code = data.data.code;
                    if (code === 0) {
                        $scope.$emit('transferErrorMsg', '注册成功,已经跳转到登录界面', 'register');
                        $location.path('/');
                    } else {
                        $scope.$emit('transferErrorMsg', '注册失败，原因：' + data.data.msg);
                    }
                });

            };
        }]);
})();
(function() {
    angular.module('login', [])
        .component('loginNavbar', {
            templateUrl: 'frontend/components/loginNavbar/loginNavbar.html',
        })
        .controller('login', ['$scope', '$http', '$location', function($scope, $http, $location) {
            $scope.$emit('hideNavbar'); //隐藏navbar
            $scope.register = function() {
                $location.path('/register');
            };
            $scope.login = function() {
                if (!$scope.phoneNumber) {
                    $scope.$emit('transferErrorMsg', '手机号不能为空'); //隐藏navbar
                    return;
                }
                if (!$scope.sellerPassword) {
                    $scope.$emit('transferErrorMsg', '密码不能为空');
                    return;
                }
                $http({
                    url: 'backend/public/SellerLogin',
                    method: 'post',
                    data: { phoneNumber: $scope.phoneNumber, sellerPassword: $scope.sellerPassword }
                }).then(function(data) {
                    let code = data.data.code;
                    if (code === 0) {
                        $scope.$emit('transferErrorMsg', '登录成功', true);
                        $location.path('/good');
                    } else {
                        $scope.$emit('transferErrorMsg', '登录失败，原因：' + data.data.msg);
                    }
                });
            };
        }]);
})();
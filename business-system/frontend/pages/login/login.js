(function() {
    angular.module('login', [])
        .component('loginNavbar', {
            templateUrl: 'frontend/components/loginNavbar/loginNavbar.html',
        })
        .controller('login', ['$scope', '$http', '$location', function($scope, $http, $location) {
            $scope.$emit('hideNavbar'); //隐藏navbar
            $scope.register = function() {
                $location.path('/register')
            }
            $scope.login = function() {
                $http({
                    url: 'frontend/static/jsons/login.json',
                    method: 'post',
                    data: { phoneNumber: $scope.phoneNumber, login: $scope.login }
                }).then(function(data) {
                    let code = data.data.code;
                    if (code === 0) {
                        $scope.$emit('transferErrorMsg', '登录成功', true);
                        $location.path('/good');
                    } else {
                        $scope.$emit('transferErrorMsg', data.data.msg);
                    }
                })
            }
        }]);
})();
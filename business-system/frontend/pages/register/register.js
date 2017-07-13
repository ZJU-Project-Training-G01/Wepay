(function() {
    angular.module('register', [])
        .controller('register', ['$scope', function() {
            $scope.$emit('hideNavbar');
        }]);
})();
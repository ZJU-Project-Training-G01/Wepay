 (function() {
     angular
         .module('goodSearch', [])
         .controller('goodSearchCtrl', ['$scope', '$http',
             function($scope, $http) {
                 $scope.search = function() {
                     let keyword =
                         $scope.$emit('transferKeyword')
                 }
             }
         ])
 })();
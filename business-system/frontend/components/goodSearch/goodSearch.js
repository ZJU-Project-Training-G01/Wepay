 (function() {
     angular
         .module('goodSearch', [])
         .controller('goodSearchCtrl', ['$scope', '$http', '$location',
             function($scope, $http, $location) {
                 $scope.search = function() {
                     let keyword = $scope.keyword;
                     $scope.$emit('transferKeyword', keyword);
                 }
                 $scope.upload = function() {
                     $location.path('goodUpload')
                 }
             }
         ])
 })();
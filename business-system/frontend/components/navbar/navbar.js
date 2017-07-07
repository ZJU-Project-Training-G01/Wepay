(function() {
    angular
        .module('myApp', [])
        .config(['$compileProvider', function($compileProvider) {
            $compileProvider.debugInfoEnabled(false);
        }])
        .component('navbar', {
            template: '<div>aa</div>',

        });
})();
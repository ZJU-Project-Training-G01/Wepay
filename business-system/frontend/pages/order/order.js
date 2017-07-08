(function() {
    var order = angular.module('order', [])
        .component('navs', {
            templateUrl: 'frontend/components/navs/navs.html',
        })
        .component('orderItem', {
            templateUrl: 'frontend/components/orderItem/orderItem.html',
            controller: 'orderItemCtrl'
        })
        .factory('setPricePrecision', function() {
            return function(originalPrice) {
                return '￥' + (originalPrice.toFixed(2)).toString();
            }
        })
})();
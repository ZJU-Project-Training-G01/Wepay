(function() {
    angular.module('user', [])
        .component('out', {

        })
        .controller('user', ['$scope', '$http', 'setPricePrecision', '$location', "ModalService",
            function($scope, $http, setPricePrecision, location, ModalService) {
                $scope.toBank = function() {
                    if ($scope.seller.bankCard === '未绑定') {
                        $scope.$emit('transferErrorMsg', '不能转出，原因:银行卡未绑定');
                        return;
                    }
                    ModalService.showModal({
                        template: '<out></out>',
                        controller: 'out'
                    }).then(function(modal) {
                        modal.element.modal();
                        modal.close.then(function(result) {
                            console.log(result);
                        });
                    })
                }
                $scope.dismissModal = function(result) {
                    close(result, 200)
                }
                $scope.$on('receClose', function() {
                    $scope.dismissModal();
                    console.log('zz');
                });
                $scope.bind = function() {
                    ModalService.showModal({
                        template: '<bind></bind>',
                        controller: 'bind'
                    }).then(function(modal) {
                        modal.element.modal();
                        $scope.$on('close', function(e) {
                            modal.close.then(function(result) {
                                console.log(result);
                            });
                        })
                        modal.close.then(function(result) {
                            console.log(result);
                        });
                    })
                }
                $http({
                    url: 'frontend/static/jsons/user.json',
                    method: 'post',
                    data: {}
                }).then(function(data) {
                    let code = data.data.code;
                    if (code === 0) {
                        data = data.data.data;
                        $scope.seller = data;
                        $scope.seller.balance = setPricePrecision($scope.seller.balance);
                        $scope.toBind = !$scope.seller.bankCard;
                        if ($scope.toBind === true) {
                            $scope.seller.bankCard = '未绑定';
                        }
                    } else {
                        $scope.$emit('transferErrorMsg', '获取用户基础信息失败，原因:' + data.data.msg);
                    }
                })
            }
        ])
        .component('bind', {
            templateUrl: 'frontend/components/bind/bind.html',
            controller: 'bind'
        })
})();
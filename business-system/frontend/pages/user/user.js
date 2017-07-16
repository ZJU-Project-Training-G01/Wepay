(function() {
    angular.module('user', [])
        .component('bind', {
            templateUrl: 'frontend/components/bind/bind.html',
            controller: 'bind'
        })
        .component('out', {
            templateUrl: 'frontend/components/out/out.html',
            controller: 'out'
        })
        .component('in', {
            templateUrl: 'frontend/components/in/in.html',
            controller: 'in'
        })
        .component('upload', {
            templateUrl: 'frontend/pages/upload/upload.html',
            controller: 'upload'
        })
        .controller('user', ['$scope', '$http', 'setPricePrecision', '$location', "ModalService", '$location',
            function($scope, $http, setPricePrecision, location, ModalService, $location) {
                $scope.$on('file', function(e, filePath) {
                    $scope.seller.sellerImgUrl = filePath;
                });
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
                        modal.close.then(function(result) {});
                    })
                };
                $scope.upload = function() {
                    $scope.$emit('upload');
                };
                $scope.bind = function() {
                    ModalService.showModal({
                        template: '<bind></bind>',
                        controller: 'bind',
                    }).then(function(modal) {
                        modal.element.modal();
                    });
                }
                $scope.in = function() {
                    if ($scope.seller.bankCard === '未绑定') {
                        $scope.$emit('transferErrorMsg', '不能充值，原因:银行卡未绑定');
                        return;
                    }
                    ModalService.showModal({
                        template: '<in></in>',
                        controller: 'in'
                    }).then(function(modal) {
                        modal.element.modal();
                    })
                }
                $http({
                    url: 'backend/public/GetSellerInfo',
                    method: 'post',
                    data: {}
                }).then(function(data) {
                    let code = data.data.code;
                    if (code === 0) {
                        data = data.data.data;
                        $scope.seller = data;
                        $scope.seller.balance = setPricePrecision($scope.seller.balance);
                        $scope.toBind = !$scope.seller.bankCard;
                        if (!$scope.seller.sellerImgUrl) {
                            $scope.seller.sellerImgUrl = 'frontend/static/imgs/sellerImgs/universal.png'
                        }
                        if ($scope.toBind === true) {
                            $scope.seller.bankCard = '未绑定';
                        } else {
                            $scope.seller.bankCard = $scope.seller.bankCard.substring($scope.seller.bankCard.length - 3);
                        }
                    } else {
                        $scope.$emit('transferErrorMsg', '获取用户基础信息失败，原因:' + data.data.msg);
                    }
                })
            }
        ]);
})();
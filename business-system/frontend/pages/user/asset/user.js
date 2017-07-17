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
                    });
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
                };
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
                    });
                };
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
                            $scope.seller.sellerImgUrl = 'frontend/static/imgs/sellerImgs/universal.png';
                        }
                        if ($scope.toBind === true) {
                            $scope.seller.bankCard = '未绑定';
                        } else {
                            $scope.seller.bankCard = $scope.seller.bankCard.substring($scope.seller.bankCard.length - 3);
                        }
                    } else {
                        $scope.$emit('transferErrorMsg', '获取用户基础信息失败，原因:' + data.data.msg);
                    }
                });
            }
        ]);
})();;(function() {
    angular.module('bind', [])
        .controller('bind', ['$scope', '$location', '$window', '$http',
            function($scope, $location, $window, $http) {
                $scope.bind = function() {
                    if ($scope.bankCard === undefined) {
                        $scope.$emit('transferErrorMsg', '银行卡号不得为空');
                        return;
                    }
                    if ($scope.bankName === undefined) {
                        $scope.$emit('transferErrorMsg', '银行名称不得为空');
                        return;
                    }
                    if ($scope.idCard === undefined) {
                        $scope.$emit('transferErrorMsg', '身份证号不得为空');
                        return;
                    }
                    if ($scope.realName === undefined) {
                        $scope.$emit('transferErrorMsg', '姓名不得为空');
                        return;
                    }
                    if ($scope.password === undefined) {
                        $scope.$emit('transferErrorMsg', '密码不得为空');
                        return;
                    }
                    $http({
                        url: 'backend/public/Bind',
                        method: 'post',
                        data: { bankName: $scope.bankName, bankCard: $scope.bankCard }
                    }).then(function(data) {
                        let code = data.data.code;
                        if (code === 0) {
                            $scope.$emit('transferErrorMsg', '绑定成功', 'modal');
                        }
                    });
                };
                $scope.cancel = function() {
                    $window.location.reload();
                };
            }
        ]);
})();;(function() {
    angular.module('out', [])
        .controller('out', ['$scope', '$http', '$window',
            function($scope, $http, $window) {
                $scope.out = function() {
                    if (!$scope.outMoney) {
                        $scope.$emit('transferErrorMsg', '转出金额不得为空');
                        return;
                    }
                    if (!$scope.payPassword) {
                        $scope.$emit('transferErrorMsg', '支付密码不得为空');
                        return;
                    }
                    $http({
                        url: 'backend/public/OutMoney',
                        method: 'post',
                        data: { payPassword: $scope.payPassword, outMoney: $scope.outMoney }
                    }).then(function(data) {
                        let code = data.data.code;
                        if (code === 0) {
                            $scope.$emit('transferErrorMsg', '转出成功', 'modal');
                        } else {
                            $scope.$emit('transferErrorMsg', data.data.msg);
                        }
                    });
                };
                $scope.cancel = function() {
                    $window.location.reload();
                };
            }
        ]);
})();;(function() {
    angular.module('in', [])
        .controller('in', ['$scope', '$http', '$window',
            function($scope, $http, $window) {
                $scope.in = function() {
                    if (!$scope.inMoney) {
                        $scope.$emit('transferErrorMsg', '充值金额不得为空');
                        return;
                    }
                    if (!$scope.payPassword) {
                        $scope.$emit('transferErrorMsg', '支付密码不得为空');
                        return;
                    }
                    $http({
                        url: 'backend/public/InMoney',
                        method: 'post',
                        data: { payPassword: $scope.payPassword, inMoney: $scope.inMoney }
                    }).then(function(data) {
                        let code = data.data.code;
                        if (code === 0) {
                            $scope.$emit('transferErrorMsg', '充值成功', 'modal');
                        } else {
                            $scope.$emit('transferErrorMsg', data.data.msg);
                        }
                    });
                };
                $scope.cancel = function() {
                    $window.location.reload();
                };
            }
        ]);
})();
(function() {
    angular.module('upload', [])
        .controller('upload', ['$scope', '$http', 'Upload', '$timeout', '$location',
            function($scope, $http, Upload, $timeout, $location) {
                $scope.$on('receUpload', function(e) {
                    $scope.ifUpload = true;
                });
                let targetPath;
                let ifGoods = $location.path().indexOf('goodUpload') >= 0;
                if (ifGoods) {
                    $scope.ifUpload = true;
                    targetPath = 'frontend/static/imgs/goodsImgs/';
                } else {
                    targetPath = 'frontend/static/imgs/sellerImgs/';
                }
                $scope.upload = function(dataUrl, name) {
                    Upload.upload({
                        method: 'post',
                        url: 'backend/public/UploadImg',
                        file: Upload.dataUrltoBlob(dataUrl, name),
                        data: {
                            isGoods: ifGoods,
                            targetPath: targetPath
                        },
                    }).then(function(response) {
                        $timeout(function() {
                            $scope.result = response.data;
                        });
                        let filePath = response.data.data.file;
                        filePath = filePath.substring(filePath.indexOf('frontend'));
                        if (ifGoods) {
                            $scope.$emit('fileGoods', filePath);
                        } else {
                            $scope.$emit('file', filePath);
                        }
                    }, function(response) {
                        if (response.status > 0) $scope.errorMsg = response.status +
                            ': ' + response.data;
                    }, function(evt) {
                        $scope.progress = parseInt(100.0 * evt.loaded / evt.total);
                    });
                };
            }
        ]);
})();
(function() {
    angular.module('upload', [])
        .controller('upload', ['$scope', '$http', 'Upload', '$timeout',
            function($scope, $http, Upload, $timeout) {
                $scope.$on('receUpload', function(e) {
                    $scope.ifUpload = true;
                })
                $scope.upload = function(dataUrl, name) {
                    Upload.upload({
                        method: 'post',
                        url: 'backend/public/UploadImg',
                        file: Upload.dataUrltoBlob(dataUrl, name),
                        data: {
                            targetPath: 'frontend/static/imgs/sellerImgs/'
                        },
                    }).then(function(response) {
                        $timeout(function() {
                            $scope.result = response.data;
                        });
                        let filePath = response.data.data.file;
                        filePath = filePath.substring(filePath.indexOf('frontend'));
                        $scope.$emit('file', filePath);
                    }, function(response) {
                        if (response.status > 0) $scope.errorMsg = response.status +
                            ': ' + response.data;
                    }, function(evt) {
                        $scope.progress = parseInt(100.0 * evt.loaded / evt.total);
                    });
                }
            }
        ])
})();
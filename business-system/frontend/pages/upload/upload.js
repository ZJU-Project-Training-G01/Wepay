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
                        url: 'frontend/static/jsons/img.json',
                        file: Upload.dataUrltoBlob(dataUrl, name),
                        // url: 'https://angular-file-upload-cors-srv.appspot.com/upload',
                        data: {
                            targetPath: 'frontend/static/imgs/sellerImgs/'
                        },
                    }).then(function(response) {
                        $timeout(function() {
                            $scope.result = response.data;
                        });
                        console.log(dataUrl);
                        console.log(name);
                        console.log(response);
                        console.log(Upload.dataUrltoBlob(dataUrl, name));
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
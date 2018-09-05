app.controller('xrfController', ['$scope', '$http', '$window', 'growl', 'Session', 'utilService', 'ExamXrfPreview', function ($scope, $http, $window, growl, Session, utilService, ExamXrfPreview) {
    $scope.xrfFile;
    $scope.model = {
        fileName: undefined,
        response: undefined
    };
    $scope.xrfPreview = [];
    $scope.filteredXrf = [];
    $scope.previewError;
    $scope.dashboard = {
        columns: [
            {
                display: "Included with Upload"
            },{
                display: "Reading Number"
            },{
                display: "Sample Number"
            },{
                display: "Highest Reading - Cadmium"
            },{
                display: "Highest Reading - Lead"
            },{
                display: "Result"
            /*},{
                display: "Flags"
            },{
                display: "Color"
            },{
                display: "SKU"
            },{
                display: "Location"
            },{
                display: "Misc"
            },{
                display: "Note"
            */}
        ]
    };

    $scope.preview = function() {
        var fd = new FormData();
        fd.append('file', $scope.xrfFile);

        return $http({
            method : 'POST',
            url : 'file/preview',
            data : fd,
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined},
            params: {
                currentUser: Session.userId,
                password: Session.password
            }
        }).then( function successCallback(response) {
            console.log("xrfController preview()", response.data);
            $scope.xrfPreview = response.data;
            if ($scope.xrfPreview.length > 0) {
                $scope.previewError = undefined;
                for (xrf in $scope.xrfPreview) {
                    $scope.xrfPreview[xrf] = new ExamXrfPreview($scope.xrfPreview[xrf])
                    $scope.xrfPreview[xrf].included = true;
                }
            } else {
                $scope.previewError = "File contains no new records";
            }
        }, function errorCallback(response) {
            console.log("ERROR: xrfController preview()", response);
            $scope.previewError = "Error processing file";
            growl.addErrorMessage("Error Getting XRF Preview", {ttl: 5000});
        });
    };

    $scope.upload = function() {
        $scope.model.response = undefined;
        $scope.filteredXrf = $scope.xrfPreview.filter(function (xrf) {
            return xrf.included;
        });
        var isValid = true;
        for (xrf in $scope.filteredXrf) {
            if (!$scope.filteredXrf[xrf].isValid()){
                isValid = false;
            }
        }

        if (isValid) {
            $scope.model.error = undefined;
            return $http({
                method : 'POST',
                url : 'file/uploadXRFSample',
                data : $scope.filteredXrf,
                params: {
                    currentUser: Session.userId,
                    password: Session.password
                }
            }).then( function successCallback(response) {
                console.log("xrfController upload()", response.data);
                $scope.model.response = response.data;
                growl.addInfoMessage("XRF Uploaded", {ttl: 5000});
            }, function errorCallback(response) {
                console.log("ERROR: xrfController save()", response);
                growl.addErrorMessage("Error Uploading XRF", {ttl: 5000});
            });
        } else {
            $scope.model.error = "Correct the XRF errors included for upload"
        }
    };

    $scope.cancel = function() {
        $window.history.back();
    };

    $scope.fileChanged = function (ele) {
        $scope.xrfFile = ele.files[0];
        $scope.model.fileName = $scope.xrfFile ? $scope.xrfFile.name : undefined;
        $scope.$apply();
    };
}]);

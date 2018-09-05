app.controller('sessionExpirationController', ['$scope', '$timeout', '$uibModalInstance', 'Session', 'localStorageService', function($scope, $timeout, $uibModalInstance, Session, localStorageService) {
    $scope.secondsRemaining = 60 * localStorageService.get('minutesAfterSessionTimeoutWarning');
    $scope.onTimeout = function(){
        $scope.secondsRemaining--;
        if ($scope.secondsRemaining == 0) {
            $uibModalInstance.dismiss("timeout");
        }
        mytimeout = $timeout($scope.onTimeout,1000);
    }
    var mytimeout = $timeout($scope.onTimeout,1000);


    $scope.continue = function () {
        $uibModalInstance.close();
    };

    $scope.formatMinutes = function () {
        return Math.floor($scope.secondsRemaining/60);
    };

    $scope.formatSeconds = function () {
        var seconds = $scope.secondsRemaining % 60;
        return seconds > 9 ? seconds : '0' + seconds;
    };

    $scope.formatTime = function(){
        return '' + $scope.formatMinutes() + ':' + $scope.formatSeconds();
    };
}]);

app.controller('homeController', ['$scope', '$rootScope', '$location', '$sce', '$http', 'authService',function ($scope, $rootScope, $location, $sce, $http, authService) {
	
	
	$scope.okProceed = false;

	$scope.init = function () {
    	$scope.getCpscNotificationList();
    }
	
    $scope.ok = function () {
    	$scope.okProceed = true;
        var landingPage = authService.getLandingPage();
        $location.path(landingPage);
    	
    };

    $scope.getVersion = function() {
        return $rootScope.ui_properties != undefined ? $rootScope.ui_properties.serverVersion : "";
    };
    
    
    $scope.getCpscNotificationList = function () {
        console.log("homeController::getCpscNotificationList()");
        $http({
            method: 'GET',
            url: "home/getCpscNotificationList"
        }).then(function (response) {
            $scope.messages = response.data;
            if($scope.messages && $scope.messages.length>0 && $scope.messages[0].note.length>0) {
                $scope.message = $sce.trustAsHtml('[' + $scope.messages[0].effectiveDate + '] - ' +  $scope.messages[0].note);
            } else {
            	$scope.message = $sce.trustAsHtml("");
            }
            console.log("get getCpscNotificationList success");
        }, function (response) {
            console.log("get getCpscNotificationList error");
            console.dir(response);
        });
    };
    
/*    $scope.message=$sce.trustAsHtml("Here are the Messages from the office of Import Surveillance...<br/>" +
    		"<span style='color:red'>RAM2.0 is scheduled to be offline at 8pm, Tuesday Sept 12 to new release deployment.</span>");
*/    
    $scope.init();
    
}]);



app.controller('loginController', ['$scope', '$rootScope', 'authService', 'AUTH_EVENTS', function ($scope, $rootScope, authService, AUTH_EVENTS) {
	
	///////////////////////////
	$scope.ssoLoginEnabled = false; // for local testing
	///////////////////////////
	
	
//	$scope.ssoLoginEnabled = true;
	
    $scope.credentials = {
        username: '',
        password: '',
        error: undefined
    };
    $scope.failedLogin = false;

    $scope.getVersion = function() {
        return $rootScope.ui_properties != undefined ? $rootScope.ui_properties.serverVersion : "";
    };

    $scope.login = function (credentials) {
        authService.login(credentials).then(function (user) {
            $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
            $scope.setCurrentUser(user);
        }, function () {
            $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
            $scope.credentials.error = "Error: Invalid Username or Password";
        });
    };
    
    $scope.ssoLogin = function () {
	    authService.ssoLogin().then(function (username) {
	    	if(username) {
	            $scope.credentials.username=username;
	            $scope.credentials.password="EXAMPLE_PASSWORD";
	            $scope.login($scope.credentials);
	    	} else {
	    		$scope.credentials.error = "Can not find your user information in ITDS/RAM2 User table."
	    	}
	    }, function (response) {
			$scope.credentials.error = "SSO failed - " + response.data;
	    });
    }
    
    // auto sign in via SSO
    if($scope.ssoLoginEnabled) {
        $scope.ssoLogin();
    }

}]);



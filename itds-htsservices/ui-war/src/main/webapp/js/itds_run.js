/**
 *
 */

app.run(['$rootScope', '$location', '$timeout', '$uibModal', 'AUTH_EVENTS', 'authService', 'localStorageService', 'propertyService', function ($rootScope, $location, $timeout, $uibModal, AUTH_EVENTS, authService, localStorageService, propertyService) {
    var modalInstance;
    var secondsBeforeWarning;
    var secondsRemaining;

    var createModal = function() {
        modalInstance = $uibModal.open({
            templateUrl: './session/expiration.html',
            controller: 'sessionExpirationController',
            backdrop: 'static'
        });
        modalInstance.result.then(function () {
            authService.continueSession(localStorageService.get('user'), localStorageService.get('lastActive'));
            timeout();
        },function () {
            authService.logout();
            $rootScope.$broadcast(AUTH_EVENTS.logoutSuccess);
        });
    };

    var onTimeout = function(){
    	if (secondsRemaining > 0) {
    		secondsRemaining--;
    	}
        if (!authService.isAuthenticated()){
            // do nothing
        } else if (secondsRemaining <= 0) {
            createModal();
        } else {
            $timeout(onTimeout,1000);
        }
    }

    var timeout = function() {
        if (!secondsRemaining) {
            propertyService.getProperties().then(function (properties) {
                secondsBeforeWarning = 60 * properties.minutesBeforeSessionTimeoutWarning;
                secondsRemaining = secondsBeforeWarning
                localStorageService.set('minutesAfterSessionTimeoutWarning',properties.minutesAfterSessionTimeoutWarning);
                $timeout(onTimeout,1000);
            });
        } else {
            secondsRemaining = secondsBeforeWarning;
        }
    };

    $rootScope.$on('$routeChangeStart', function (event, next) {
//        if (next.$$route.originalPath == '/home') {
//        	return;
//        }
//        
//        if(next.$$route.templateUrl == './login/home.html') {
//        	return;
//        }
  
	    if(next.$$route.templateUrl == './login/login.html') {
	    	return;
	    }
        if (!authService.isAuthenticated()) {
        	$rootScope.$broadcast(AUTH_EVENTS.notAuthenticated);
        } else {
            var session = authService.getSession().then( function(session) {
                if ( (session == null || session.username == null) && authService.isAuthenticated() ) {
                    authService.logout();
                    $rootScope.$broadcast(AUTH_EVENTS.logoutSuccess);
                }

        	var authorizedRoles = next.$$route.resolve && next.$$route.resolve.authorizedRoles ? next.$$route.resolve.authorizedRoles() : [];

            if (!authService.isAuthorized(authorizedRoles)) {
                $rootScope.$broadcast(AUTH_EVENTS.notAuthorized);
            }

            var lastActive = new Date();
            localStorageService.set('lastActive', lastActive.getTime());
            if (next.$$route.originalPath == '/login' || next.$$route.originalPath == '/') {
//                var landingPage = authService.getLandingPage();
//                $location.path(landingPage);
                $location.path('/home');
            } else {
                timeout();
            }
            return session;
            });
        }
    });

    $rootScope.$on(AUTH_EVENTS.loginSuccess, function (event) {
        console.log('login success');
        $location.path("/");
    });

    $rootScope.$on(AUTH_EVENTS.loginFailed, function (event) {
        console.log('login failed');
    });

    $rootScope.$on(AUTH_EVENTS.logoutSuccess, function (event) {
        console.log('logout success');
        $location.path("/login");
    });

    $rootScope.$on(AUTH_EVENTS.notAuthenticated, function (event) {
        console.log('not authenticated');
        $location.path("/login");
        //$location.path("/home");
    });

    $rootScope.$on(AUTH_EVENTS.notAuthorized, function (event) {
        console.log('not authorized');
        $location.path("/");
    });

    $rootScope.$on(AUTH_EVENTS.keepAlive, function (event) {
        console.log('keep alive');
        secondsRemaining = secondsBeforeWarning;
    });
}]);


console.log("loading itds.js");


app.config([ '$routeProvider', function($routeProvider) {

	$routeProvider.when('/', {
	}).when('/login', {
        templateUrl : './login/login.html',
        controller: 'loginController'
    }).when('/users', {
        templateUrl : './user/user-dashboard.html',
        controller: 'userDashboardController',
            resolve: {
                authorizedRoles: function () {return ["ROLE_USER_READ"];}
            }
    }).when('/users/new', {
        templateUrl : './user/user-form.html',
        controller: 'userFormController',
        resolve: {
            resolveUser: ['$route','userRepository', function ($route,userRepository) {
                return userRepository.getNew();
            }],
            resolveData: function () {
                return {
                    pageType: "Add"
                };
            },
            authorizedRoles: function () {return ["ROLE_USER_SAVE"];}
        }
    }).when('/users/:username', {
        templateUrl : './user/user-form.html',
        controller: 'userFormController',
        resolve: {
            resolveUser: ['$route','userRepository', function ($route,userRepository) {
                return userRepository.getUser($route.current.params.username);
            }],
            resolveData: function () {
                return {
                    pageType: "Edit"
                };
            },
            authorizedRoles: function () {return ["ROLE_USER_SAVE"];}
        }
    }).when('/account', {
        templateUrl : './user/user-form.html',
        controller: 'userFormController',
        resolve: {
            resolveUser: ['Session','userRepository', function (Session,userRepository) {
                return userRepository.getUser(Session.userId);
            }],
            resolveData: function () {
                return {
                    pageType: "Edit"
                };
            }
        }
    }).when('/ss', {
        templateUrl : './ss/ss.html',
        controller: 'ssController'
    }).when('/settings', {
        templateUrl : './settings/settings.html',
        controller: 'settingsController'
    }).when('/notifications', {
        templateUrl : './notifications/notifications.html',
        controller: 'notificationsController'
    }).when('/events', {
        templateUrl : './events/events.html',
        controller: 'eventsController'
    }).when('/xrf', {
        templateUrl : './upload/xrf-upload.html',
        controller: 'xrfController',
        resolve: {
            authorizedRoles: function () {return ["ROLE_ETL"];}
        }
    }).when('/metrics', {
        templateUrl : './metrics/metrics.html',
        controller: 'metricsController',
        resolve: {
            authorizedRoles: function () { return ['ROLE_METRICS_READ']; }
        }
    }).when('/home', {
		templateUrl : './login/home.html'
    }).when('/inbox', {
		templateUrl : './inbox/inbox.html',
        resolve: {
            authorizedRoles: function () {return ["ROLE_INBOX_READ"];}
        }
	}).when('/examlog', {
		templateUrl : './examlog/exams.html'
	}).otherwise({redirectTo : '/'});
} ]);


app.controller('appController', [
		'$scope',
		'$rootScope',
		'$route',
		'AUTH_EVENTS',
		'authService',
		'localStorageService',
		'propertyService',
		'$window',
		function($scope, $rootScope, $route, AUTH_EVENTS, authService, localStorageService, propertyService,$window) {
		    /*
		     * call REST API to fetch ITDS/RAM UI configuration properties
		     */
		    $scope.init = function() {
                propertyService.getProperties().then(function successCallback(response) {
                    console.log(response);
                    $rootScope.ui_properties = response;
                }, function errorCallback(response) {
                    console.log(response);
                    return $q.reject(response);
                });

                $scope.routes = $route.routes;

                var storedUser = localStorageService.get('user');
                var storedLastActive = new Date(localStorageService.get('lastActive'));
                if (storedUser && storedLastActive && authService.continueSession(storedUser, storedLastActive)) {
                    $scope.currentUser = localStorageService.get('user');
                } else {
                    $scope.logout();
                }
                

		    }
			
		    $scope.charRemaining = function(data, maxLength) {
		    	if(data) {
		    		var len = data.length;
		    		return (maxLength - len>0)?(maxLength - len):0;
		    	} 
		    	return maxLength;
		    };
		    
            $scope.isAuthenticated = function() {
                return authService.isAuthenticated();
            }

            $scope.isAuthorized = function(authorizedRoles) {
                return authService.isAuthorized(authorizedRoles);
            }

            $scope.setCurrentUser = function (user) {
                localStorageService.set('user',user);
                $scope.currentUser = localStorageService.get('user');
            };

            $scope.logout = function () {
                $scope.currentUser = null;
                authService.logout();
                $rootScope.$broadcast(AUTH_EVENTS.logoutSuccess);
            };

            $scope.keepAlive = function () {
                var lastActive = new Date(localStorageService.get('lastActive'));
                if (authService.isAuthenticated()) {
                    var now = new Date();
                    var idleTime = new Date() - lastActive;
                    if (idleTime > 60000) {
                        $rootScope.$broadcast(AUTH_EVENTS.keepAlive);
                        localStorageService.set('lastActive', now.getTime());
                        var session = authService.getSession();
                    }
                }
            };
            
            $scope.isHomePage = function() {
            	var isHomeUrl = $window.location.toString().indexOf('#/home') >0;
            	return isHomeUrl;
            }

            $scope.init();   
		} ]);

app.controller('itdsController', ['$scope','navigationService',function($scope, navigationService) {

			console.log("call itdsController");
			$scope.beforeLeavingCurrentPage = function($event) {
	    		if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage($event) ) {return false; }
			}
/*			navigationService.setCurrentTab("home");
			$scope.activeCss = function(tab) {
				return navigationService.activeCss(tab);
			};
*/
			$scope.message = "<h1>Welcome to ITDS/RAM 2.0!</h1>" + "<br>"
					+ "Announcement ....";
		} ]);


console.log("Done loading itds.js");

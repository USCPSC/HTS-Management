app.controller('notificationsController', ['$scope', '$http', 'growl', function ($scope, $http, growl) {
        $scope.newNotification = {};
        $scope.init = function () {
        	$scope.prePopulateNotification();
        };
        $scope.saveNotification = function (newNotification) {
            console.log("notifications::saveNotification()");
            //console.dir(prop);
            $http({
                method: 'POST',
                url: "home/create",
                data: newNotification
            }).then(function (response) {
    			growl.addInfoMessage("New notification record saved.", {ttl: 5000});
    			$scope.newNotification.notificationErrors = null;
            }, function (response) {
    			console.log(response);
    			if(response.status == 400) { // validation errors
    				newNotification.notificationErrors = response.data;
    				newNotification.notificationErrors.message = "Please correct following error(s):";
    			} else {
    				growl.addInfoMessage(response.statusText + ". " + response.data, {ttl: 10000});
    			}
    			//return $q.reject(response);
                
            });
        };
        $scope.prePopulateNotification = function () {
            console.log("notifications::prePopulateNotification()");
            //console.dir(prop);
            $http({
                method: 'POST',
                url: "home/prePopulateNotification"
            }).then(function (response) {
            	$scope.newNotification = response.data;
            }, function (response) {
                console.dir(response);
            });
        };
        $scope.cancelNotification = function () {
            //console.dir(prop);
        	$scope.newNotification = {};
        	
        };
        $scope.init();

    }]);

console.log("Done loading notifications.js");
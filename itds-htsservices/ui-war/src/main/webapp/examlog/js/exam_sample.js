/**
 * exam sample controller
 */
console.log ("loading exam_sample.js");

app.controller('examSampleController', ['$scope', '$http', '$route', 'navigationService', 'Session', 'utilService','workflowService', 'APP_CONSTANTS', function($scope,$http, $route, navigationService, Session, utilService,workflowService, APP_CONSTANTS) {
	
	/**
	 * initialize the controller: invoke this at end of this controller definition
	 */
	$scope.init = function() {
		console.log("build examSampleController");

	    $scope.detailTabSelected = "Details";
	    //$scope.editMode = "VIEW"; // VIEW/EDIT/CREATE mode

    	$scope.examAction.initCreateExam();

	}



// call init()
$scope.init();

}]);

console.log ("Done loading exam_sample.js");

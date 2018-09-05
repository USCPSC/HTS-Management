/**
 * Inbox Comments tab
 */

console.log ("loading inbox_importer.js");

app.controller('inboxImporterController', 
		['$scope', '$http', '$uibModal','inboxService', 'Session', 'utilService', 'inboxService','examlogService',
        function($scope, $http, $uibModal, inboxService, Session, utilService, inboxService, examlogService) {

	$scope.importerCounts=[];
	
	$scope.init = function() {
		$scope.getImportCounts($scope.detail.selectedEntryLine);
	}
	
	$scope.getImportCounts = function(selectedEntryLine) {
		console.log("getImportCounts");
		console.log(selectedEntryLine);
			var url = "inbox/importer/counts";
			$http({
				method: 'GET',
				url: url,
				params : { importer: selectedEntryLine.importerNumber }
			}).then(function successCallback(response) {
				$scope.importerCounts = response.data;
				var xticks = [[1,$scope.importerCounts.fyqtrMinus7],[2,$scope.importerCounts.fyqtrMinus6],
					[3,$scope.importerCounts.fyqtrMinus5],[4,$scope.importerCounts.fyqtrMinus4],
					[5,$scope.importerCounts.fyqtrMinus3],[6,$scope.importerCounts.fyqtrMinus2],
					[7,$scope.importerCounts.fyqtrMinus1],[8,$scope.importerCounts.fyqtrCurrent]];
				var examSer = [$scope.importerCounts.noFyqtrMinus7,$scope.importerCounts.noFyqtrMinus6,
					$scope.importerCounts.noFyqtrMinus5,$scope.importerCounts.noFyqtrMinus4,
					$scope.importerCounts.noFyqtrMinus3,$scope.importerCounts.noFyqtrMinus2,
					$scope.importerCounts.noFyqtrMinus1,$scope.importerCounts.noFyqtrCurrent];
				var sampleSer = [$scope.importerCounts.noFyqtrMinus7Samples,$scope.importerCounts.noFyqtrMinus6Samples,
					$scope.importerCounts.noFyqtrMinus5Samples,$scope.importerCounts.noFyqtrMinus4Samples,
					$scope.importerCounts.noFyqtrMinus3Samples,$scope.importerCounts.noFyqtrMinus2Samples,
					$scope.importerCounts.noFyqtrMinus1Samples,$scope.importerCounts.noFyqtrCurrentSamples];
				var maxY = Math.max.apply(null, [Math.max.apply(null, examSer), Math.max.apply(null, sampleSer)]);
				var tickIntervalY = (maxY>500)? 50: ((maxY>200)? 20: ((maxY>100)? 10: ((maxY>50)? 5: ((maxY>10)? 2 : 1))));
				var plot1 = $.jqplot( "chartdiv", [examSer,sampleSer], {
					animate: true,
					title: "Importer: " + selectedEntryLine.importerName,
					seriesDefaults: {
						linePattern: 'solid',
						showMarker: true,
						shadow: true
					},
					axes: {
						xaxis: {
				            angle: -90,
							ticks: xticks
						},
						yaxis: {
							min: 0,
							max: (maxY + 2),
							tickInterval: tickIntervalY, 
							tickOptions: { 
								formatString: '%d' 
							}
						}
					},
				    legend: {
				        renderer: $.jqplot.EnhancedLegendRenderer,
				    	show: true,
				    	labels: ["Exam","Samples"],
				    	location: 'ne',
				    	placement: "outside",
				    	marginBottom: "1px",
				    },
				});
				console.log($scope.importerCounts);
			}, function errorCallback(response) {
				console.log(response);
			});
	}

    $scope.displaySamples = function (importerNumber, flag) {
    	// flag: Violative, Non-violative, In Progress
		var date1=new Date();
		date1.setYear(date1.getFullYear()-2); // go back 2yrs
		date1=examlogService.formatDate(date1);
		$scope.importer_number = importerNumber;
        $scope.importer_sample_list = [];
    	$scope.ifsViewSampleUrl = examlogService.getParameters().ifs_view_sample_url;
        
        if(flag == "VIO") {
            $scope.importer_title = 'Violative Samples for Importer #: ' + importerNumber;
        } else if (flag == "NON") {
            $scope.importer_title = 'Non-Violative Samples for Importer #: ' + importerNumber;
        } else {
            $scope.importer_title = 'Samples in Progress for Importer #: ' + importerNumber;
        }
    	
    	inboxService.getImporterSampleList(importerNumber, flag, date1).then(
    			function successCallback(data) {
		            $scope.importer_sample_list = data;
					var modalInstance = $uibModal.open({
						templateUrl: './inbox/importer/importer_samples_popup.html',
						controller: 'importerSampleViewController',
						scope: $scope,
						backdrop: "static"
						});	  	
    			}, 
    			function errorCallback(response) {
					console.log(response);
		            $scope.importer_sample_list = response.data.data;
    			});
    }

    
	$scope.init();

}]);

app.controller('importerSampleViewController', ['$scope', '$http', '$uibModal', '$uibModalInstance', 'inboxService', 'Session', 'utilService', 
                                           function($scope, $http, $uibModal, $uibModalInstance, inboxService, Session, utilService) {
	$scope.ok = function () {
	    $uibModalInstance.close();
	},

	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	}
}]);

console.log ("Done loading inbox_importer.js");

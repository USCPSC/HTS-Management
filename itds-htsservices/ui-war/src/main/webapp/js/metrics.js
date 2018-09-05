app.controller('metricsController', ['$scope', '$http', 'utilService', 'Session', function($scope, $http, utilService, Session) {
    $scope.examSampleMetrics=[];
    $scope.workflowActivityMetrics=[];
    $scope.dataProcessingMetrics=[];
    $scope.workflowData=[];
    $scope.loading = true;
    $scope.dashboard = {
        examsWithSamples: 0,
        examsWithoutSamples: 0,
        numberofViolativeSamples: 0,
        numberofNonViolativeSamples: 0,
        numberofSamplesInProgress: 0,
        entryFilesReceived: [],
        entrySummaryFilesReceived: [],
        eventFilesReceived: [],
        entryLinesReceived: [],
        entryLinesUpdated: [],
        entrySummaryLinesReceived: [],
        examColumns: [
            {
                name: "username",
                display: "User ID",
                getValue: function(metric) {
                    return utilService.truncate(metric.username, 10);
            }},
            {
                name: "numberofExams",
                display: "Number of Exams",
                getValue: function(metric) {
                    return metric.numberofExams;
            }},
            {
                name: "numberofExamswithSamples",
                display: "Number of Exams with Samples",
                getValue: function(metric) {
                    return metric.numberofExamswithSamples;
            }},
            {
                name: "numberofProductsScreened",
                display: "Number of Products Screened",
                getValue: function(metric) {
                    return metric.numberofProductsScreened;
            }},
            {
                name: "numberofSamples",
                display: "Number of Samples",
                getValue: function(metric) {
                    return metric.numberofSamples;
            }},
            {
                name: "numberofViolativeSamples",
                display: "Number of Violative Samples",
                getValue: function(metric) {
                    return metric.numberofViolativeSamples;
            }},
            {
                name: "numberofNonViolativeSamples",
                display: "Number of Non Violative Samples",
                getValue: function(metric) {
                    return metric.numberofNonViolativeSamples;
            }},
            {
                name: "numberofSamplesInProgress",
                display: "Number of Samples in Progress",
                getValue: function(metric) {
                    return metric.numberofSamplesInProgress;
            }}
        ],
        workflowColumns: [
            {
                name: "username",
                display: "User ID",
                getValue: function(metric) {
                    return utilService.truncate(metric.username, 10);
            }},
            {
                name: "exisSupervisor",
                display: "EXIS Supervisor",
                getValue: function(metric) {
                    return metric.exisSupervisor;
            }},
            {
                name: "mayProceed",
                display: "May Proceed",
                getValue: function(metric) {
                    return metric.mayProceed ? metric.mayProceed : 0;
            }},
            {
                name: "cbpHoldRequested",
                display: "CBP Hold Requested",
                getValue: function(metric) {
                    return metric.cbpHoldRequested ? metric.cbpHoldRequested : 0;
            }},
            {
                name: "cbpHoldApproved",
                display: "CBP Hold Approved",
                getValue: function(metric) {
                    return metric.cbpHoldApproved ? metric.cbpHoldApproved : 0;
            }},
            {
                name: "cbpReleased",
                display: "CBP Released",
                getValue: function(metric) {
                    return metric.cbpReleased ? metric.cbpReleased : 0;
            }},
            {
                name: "ciUnavailable",
                display: "CI Unavailable",
                getValue: function(metric) {
                    return metric.ciUnavailable ? metric.ciUnavailable : 0;
            }},
            {
                name: "cpscDocReview",
                display: "CPSC Doc Review",
                getValue: function(metric) {
                    return metric.cpscDocReview ? metric.cpscDocReview : 0;
            }},
            {
                name: "entryReleased",
                display: "Entry Released",
                getValue: function(metric) {
                    return metric.entryReleased ? metric.entryReleased : 0;
            }},
            {
                name: "examinedAndReleased",
                display: "Examined and Released",
                getValue: function(metric) {
                    return metric.examinedAndReleased ? metric.examinedAndReleased : 0;
            }},
            {
                name: "holdRejected",
                display: "Hold Rejected",
                getValue: function(metric) {
                    return metric.holdRejected ? metric.holdRejected : 0;
            }},
            {
                name: "referToField",
                display: "Refer to Field",
                getValue: function(metric) {
                    return metric.referToField ? metric.referToField : 0;
            }},
            {
                name: "releaseWithoutPhysExam",
                display: "Release Without Physical Exam",
                getValue: function(metric) {
                    return metric.releaseWithoutPhysExam ? metric.releaseWithoutPhysExam : 0;
            }},
            {
                name: "requestRedelivery",
                display: "Request Redelivery",
                getValue: function(metric) {
                    return metric.requestRedelivery ? metric.requestRedelivery : 0;
            }},
            {
                name: "sampledAndConditionallyReleased",
                display: "Sampled and Conditionally Released",
                getValue: function(metric) {
                    return metric.sampledAndConditionallyReleased ? metric.sampledAndConditionallyReleased : 0;
            }},
            {
                name: "sampledAndDetained",
                display: "Sampled and Detained",
                getValue: function(metric) {
                    return metric.sampledAndDetained ? metric.sampledAndDetained : 0;
            }}
        ],
        dataProcessingColumns: [
            {
                name: "date",
                display: "Date",
                getValue: function(metric) {
                    return metric.date;
            }},
            {
                name: "entryFilesReceived",
                display: "Entry Files Received",
                getValue: function(metric) {
                return metric.entryFilesReceived;
            }},
            {
                name: "entrySummaryFilesReceived",
                display: "Entry Summary Files Received",
                getValue: function(metric) {
                    return metric.entrySummaryFilesReceived;
            }},
            {
                name: "eventFilesReceived",
                display: "Event Files Received",
                getValue: function(metric) {
                    return metric.eventFilesReceived;
            }},
            {
                name: "entryLinesReceived",
                display: "Entry Lines Received",
                getValue: function(metric) {
                    return metric.entryLinesReceived;
            }},
            {
                name: "entryLinesNew",
                display: "Entry Lines New",
                getValue: function(metric) {
                    return metric.entryLinesNew;
            }},
            {
                name: "entryLinesUpdated",
                display: "Entry Lines Updated",
                getValue: function(metric) {
                    return metric.entryLinesUpdated;
            }},
            {
                name: "entrySummaryLinesReceived",
                display: "Entry Summary Lines Received",
                getValue: function(metric) {
                    return metric.entrySummaryLinesReceived;
            }}
        ]
    };

    $scope.createPieGraphs = function() {
        $scope.examData = function () {
            return  [
                {
                    "label": "Exams With Samples",
                    "value" : $scope.dashboard.examsWithSamples
                } ,
                {
                    "label": "Exams Without Samples",
                    "value" : $scope.dashboard.examsWithoutSamples
                }
            ];
        };

        $scope.sampleData = function () {
            return  [
                {
                    "label": "Violative Samples",
                    "value" : $scope.dashboard.numberofViolativeSamples
                } ,
                {
                    "label": "Non-Violative Samples",
                    "value" : $scope.dashboard.numberofNonViolativeSamples
                } ,
                {
                    "label": "In Progress Samples",
                    "value" : $scope.dashboard.numberofSamplesInProgress
                }
            ];
        };

        nv.addGraph(function() {
            var chart = nv.models.pieChart()
                .x(function(d) { return d.label })
                .y(function(d) { return d.value })
                .showLabels(false);
            chart.legend.maxKeyLength(25);
            chart.valueFormat(d3.format('d'));
            d3.select("#exams svg")
                .datum($scope.examData())
                .transition().duration(350)
                .call(chart);

            return chart;
        });

        nv.addGraph(function() {
            var chart = nv.models.pieChart()
                .x(function(d) { return d.label })
                .y(function(d) { return d.value })
                .showLabels(false)
                .color(function (d, i) {
                    var colors = d3.scale.category20().range();
                    return colors[(i + 3) % colors.length-1];
                });
            chart.legend.maxKeyLength(25);
            chart.valueFormat(d3.format('d'));
            d3.select("#samples svg")
                .datum($scope.sampleData())
                .transition().duration(350)
                .call(chart);

            return chart;
        });
    };

    $scope.createBarGraph = function() {
        var data = [
            {
                values: $scope.workflowData,
                key: "User Activity"
            }
        ];

        nv.addGraph(function() {
           var chart = nv.models.discreteBarChart()
           .x(function(d) { return d.label })
           .y(function(d) { return d.value })
          // .staggerLabels(true)
           .valueFormat(d3.format('d'))
           .height(300) //gz
           .showValues(true);
           chart.xAxis.rotateLabels(-45);  //gz
           d3.select('#workflow svg')
                .datum(data)
                .call(chart);
            nv.utils.windowResize(chart.update);
            return chart;
        });
    };

    $scope.createLineGraph = function() {
        var data = [
            {
                values: $scope.dashboard.entryFilesReceived,
                key: "Entry Files Received"
            },
            {
                values: $scope.dashboard.entrySummaryFilesReceived,
                key: "Entry Summary Files Received"
            },
            {
                values: $scope.dashboard.eventFilesReceived,
                key: "Event Files Received"
            },
            {
            	
            	
                values: $scope.dashboard.entryLinesReceived,
                key: "Entry Lines Received"
            },
            {
                values: $scope.dashboard.entryLinesUpdated,
                key: "Entry Lines Updated"
            },
            {
                values: $scope.dashboard.entrySummaryLinesReceived,
                key: "Entry Summary Lines Received"
            }
        ];

        nv.addGraph(function() {
            var chart = nv.models.lineChart().options({
                transitionDuration: 300,
                useInteractiveGuideline: true
            }).height(320);  //gz
            chart.legend.maxKeyLength(30);
            chart.xAxis
               //gz .axisLabel("Date")
                    .tickFormat(function (d) {
                	return $scope.dataProcessingMetrics[d].date;
                	})
               .ticks(10); // $scope.dataProcessingMetrics.length);
            chart.yAxis
                .axisLabel('Activities')
                .tickFormat(d3.format('d'));
          //  chart.margin({right:100});
            
            chart.margin({right:75});  //gz
            chart.xAxis.rotateLabels(-45);  //gz
            d3.select("#dataprocessing svg")
                .datum(data)
                .call(chart);

            nv.utils.windowResize(chart.update);

            return chart;
        });
    };

    $scope.getExamSampleMetrics = function () {
        return $http({
            method: 'GET',
            url: 'metrics/examsample',
            params: {
                currentUser: Session.userId,
                password: Session.password
            }
        }).then(function successCallback(response) {
            console.log('metricsController getExamSampleMetrics()',response);
            $scope.examSampleMetrics = response.data ? response.data.filter(function (metric) {
                return metric.year === 'ALL';
            }) : [];
            $scope.examSampleMetrics.forEach(function (metric) {
                $scope.dashboard.examsWithSamples += metric.numberofExamswithSamples;
                $scope.dashboard.examsWithoutSamples += (metric.numberofExams - metric.numberofExamswithSamples);
                $scope.dashboard.numberofViolativeSamples += metric.numberofViolativeSamples;
                $scope.dashboard.numberofNonViolativeSamples += metric.numberofNonViolativeSamples;
                $scope.dashboard.numberofSamplesInProgress += metric.numberofSamplesInProgress;
            });
            $scope.createPieGraphs();
        }, function errorCallback(response) {
            console.log('Error: metricsController getExamSampleMetrics()',response);
        });
    }

    $scope.getWorkflowActivitySum = function (activity) {
        var sum = activity.mayProceed + activity.cbpHoldRequested + activity.cbpHoldApproved + activity.cbpReleased +
                  activity.ciUnavailable + activity.cpscDocReview + activity.entryReleased + activity.examinedAndReleased +
                  activity.holdRejected + activity.referToField + activity.releaseWithoutPhysExam +
                  activity.requestRedelivery + activity.sampledAndConditionallyReleased + activity.sampledAndDetained

        return sum;
    };

    $scope.getWorkflowActivityMetrics = function () {
        return $http({
            method: 'GET',
            url: 'metrics/workflow',
            params: {
                currentUser: Session.userId,
                password: Session.password
            }
        }).then(function successCallback(response) {
            console.log('metricsController getWorkflowMetrics()',response);
            $scope.workflowActivityMetrics = response.data ? response.data.filter(function (metric) {
                return metric.year === 'ALL';
            }) : [];
            $scope.workflowActivityMetrics.forEach(function (metric) {
                $scope.workflowData.push({
                    value: $scope.getWorkflowActivitySum(metric),
                    label: metric.username
                });
            });
            $scope.createBarGraph();
            $scope.loading = false;
        }, function errorCallback(response) {
            console.log('Error: metricsController getWorkflowMetrics()',response);
        });
    }

    $scope.getDataProcessingMetrics = function () {
        return $http({
            method: 'GET',
            url: 'metrics/dataprocessing',
            params: {
                currentUser: Session.userId,
                password: Session.password
            }
        }).then(function successCallback(response) {
            console.log('metricsController getDataProcessingMetrics()',response);
            $scope.dataProcessingMetrics = response.data ? response.data : [];
            $scope.dataProcessingMetrics.forEach(function (metric, index) {
                $scope.dashboard.entryFilesReceived.push({label:metric.date, x:index, y:metric.entryFilesReceived});
                $scope.dashboard.entrySummaryFilesReceived.push({label:metric.date, x:index, y:metric.entrySummaryFilesReceived});
                $scope.dashboard.eventFilesReceived.push({label:metric.date, x:index, y:metric.eventFilesReceived});
                $scope.dashboard.entryLinesReceived.push({label:metric.date, x:index, y:metric.entryLinesReceived});
                $scope.dashboard.entryLinesUpdated.push({label:metric.date, x:index, y:metric.entryLinesUpdated});
                $scope.dashboard.entrySummaryLinesReceived.push({label:metric.date, x:index, y:metric.entrySummaryLinesReceived});
            });
            $scope.createLineGraph();
        }, function errorCallback(response) {
            console.log('Error: metricsController getDataProcessingMetrics()',response);
        });
    }

    $scope.getMetrics = function() {
        $scope.getExamSampleMetrics();
        $scope.getWorkflowActivityMetrics();
        $scope.getDataProcessingMetrics();
    };

    $scope.examDataExists = function () {
        return ($scope.dashboard.examsWithSamples + $scope.dashboard.examsWithoutSamples) > 0;
    };

    $scope.sampleDataExists = function () {
        return ($scope.dashboard.numberofViolativeSamples + $scope.dashboard.numberofNonViolativeSamples + $scope.dashboard.numberofSamplesInProgress) > 0;
    };

    $scope.getFiscalYear = function () {
    	return utilService.getCurrentFiscalYear()
	};
	
	  $scope.getFiscalYearLastTwoDig = function () {   //gz+
	    	return utilService.getCurrentFiscalYear().toString().slice(2,4)
		};
    
    $scope.getMetrics();
}]);

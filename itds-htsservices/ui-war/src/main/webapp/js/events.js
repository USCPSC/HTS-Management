app.controller('eventsController', ['$scope', '$http', 'utilService', function ($scope, $http, utilService) {
        $scope.events = [];

        $scope.dashboard = {
            page: 0,
            pageSize: 25,
            columns: [
                {
                    name: "userIdentifier",
                    display: "User",
                    allowFilter: false,
                    getValue: function (event) {
                        return event.userIdentifier;
                    }},
                {
                    name: "appName",
                    display: "Application Name",
                    allowFilter: false,
                    getValue: function (event) {
                        return event.appName;
                    }},
                {
                    name: "appVersion",
                    display: "App Version",
                    allowFilter: true,
                    getValue: function (event) {
                        return event.appVersion;
                    }},
                {
                    name: "action",
                    display: "Action",
                    allowFilter: false,
                    getValue: function (event) {
                        return event.action;
                    }},
                {
                    name: "eventName",
                    display: "Event",
                    allowFilter: false,
                    getValue: function (event) {
                        return event.eventName;
                    }},
                {
                    name: "eventDescription",
                    display: "Description",
                    allowFilter: false,
                    getValue: function (event) {
                        return event.eventDescription;
                    }},
                {
                    name: "eventTimeStamp",
                    display: "Date/Time",
                    allowFilter: false,
                    getValue: function (event) {
                        return utilService.getFormattedDate(event.eventTimeStamp);
                    }}
            ]
        };

        $scope.getEvents = function () {
            $http({
                method: 'GET',
                url: 'shared/sysevents',
                params: {
                    page: $scope.dashboard.page,
                    pageSize: $scope.dashboard.pageSize
                }
            }).then(function successCallback(response) {
                console.log('eventsController getEvents()', response);
                $scope.events = response.data;
            }, function errorCallback(response) {
                console.log('Error: eventsController getEvents()', response);
            });
        };

        $scope.init = function () {
            $scope.getEvents();
            console.log("events::init()");
        };
        
        $scope.next = function () {
            if (!$scope.events.last) {
                $scope.dashboard.page += 1;
                $scope.getEvents();
            }
        };

        $scope.previous = function () {
            if (!$scope.users.first) {
                $scope.dashboard.page -= 1;
                $scope.getEvents();
            }
        };

		$scope.runEvent = function (eventType) {
			console.log('eventsController runEvent(' + eventType + ')');
            $http({
                method: 'GET',
                url: 'shared/sysevents/run',
                params: {
                    event: eventType
                }
            }).then(function successCallback(response) {
            }, function errorCallback(response) {
                console.log('Error: eventsController runEvent(): ', response);
            });
		};

		$scope.selectedEvent = null;

		$scope.eventTypes = [
			{const:'SEND_STALE_ENTRY_EMAIL',desc:'Send Stale Entry Emails'},
			{const:'SEND_CONDITIONAL_RELEASE_EMAIL',desc:'Send Conditional Release Emails'},
			{const:'SEND_PENDING_EVALUATION_EMAIL',desc:'Send Pending Evaluation Emails'},
			{const:'SEND_EXAM_ENTRY_NUMBER_CHECK_EMAIL',desc:'Send Exam Entry Number Check Emails'},
			{const:'SEND_ADVANCE_WITHIN_WORKFLOW_EMAIL',desc:'Send Advance Within Workflow Emails'}
		];

        $scope.init();

    }]);

console.log("Done loading events.js");

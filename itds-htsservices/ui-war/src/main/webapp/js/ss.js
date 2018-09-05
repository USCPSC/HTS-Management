app.factory('logService', ['$http', 'Session', function ($http, Session) {
        var logService = {};

        logService.addLog = function (log) {
            $http({
                method: 'POST',
                url: "logs/log",
                data: log
            }).then(function successCallback(response) {
                console.log("ss/addLog >>>" + response.data + "<<<");
            }, function errorCallback(response) {
                console.log(response);
            });
        };

        return logService;
    }]);

app.controller('ssController', ['$scope', '$http', '$filter', '$timeout', 'Session', 'utilService', 'logService', function ($scope, $http, $filter, $timeout, Session, utilService, logService) {
        $scope.dashboard = {
            auditColumns: [
                {
                    name: "principal",
                    display: "User ID",
                    getValue: function (audit) {
                        return utilService.truncate(audit.principal, 10);
                    }},
                {
                    name: "eventDate",
                    display: "Event Date",
                    getValue: function (audit) {
                        return utilService.getFormattedDate(audit.eventDate);
                    }},
                {
                    name: "eventType",
                    display: "Event Type",
                    getValue: function (audit) {
                        return utilService.truncate(audit.eventType, 10);
                    }},
                {
                    name: "dataMessage",
                    display: "Message",
                    getValue: function (audit) {
                        return utilService.truncate(audit.dataMessage, 20);
                    }},
            ]
        };

        $scope.audits = [];

        $scope.logLevels = [
            "Debug",
            "Info",
            "Warn",
            "Error",
            "Fatal"
        ];

        $scope.eventTypes = [
            "AUTHORIZATION_FAILURE",
            "AUTHORIZATION_SUCCESS"
        ];

        $scope.formTypes = [
            "Form163",
            "Form330",
            "Form352",
            "Form353",
            "Form354"
        ];

        $scope.email = {
            toEmailAddress: null,
            subject: null,
            body: null,
            sendHtml: false
        };

        $scope.cbpEntry = {
            actionMessage: "Testing ABC",
            opName: "OP 7",
            actualArrivalDate: "01-01-2016",
            estimatedArrivalDate: "01-15-2016",
            releaseDate: "02-01-2016",
            entryNo: "1234",
            lines: "23, 55, 91",
            hts: "HTS-1",
            bol: "BOL-1",
            carrier: "ABC",
            mot: "MOT-1",
            vessel: "Ship 17",
            foreignPort: "1234",
            portName: "Savanna, GA",
            unlandingPort: "2222",
            ftz: "FTZ-1",
            importerEin: "5025",
            importerName: "ACME",
            importerAddress: "123 Somewhere St",
            importerAddress2: "Suite 9",
            importerCity: "Vienna",
            importerState: "VA",
            importerZip: "12345",
            consigneeEin: "4999",
            consigneeName: "CORP",
            consigneeAddress: "555 Some Pl",
            consigneeAddress2: "",
            consigneeCity: "Rockville",
            consigneeState: "MD",
            consigneeZip: "33333",
            lineNo: "1 33 25",
            refMid: "MID-2",
            refName: "John Reference",
            refAddress: "800 Home Ct",
            refCity: "Portland",
            portId: "1234"
        };

        $scope.workflowEntry = {
            entryLine: {
                ramScore: "97",
                arrivalDate: "01-01-2016",
                releaseDate: "02-05-2016",
                entryNumber: 9001,
                entryLineNumber: 2,
                htsNumber: "R66Y",
                importerName: "Tea Time",
                importerNumber: 1500,
                billOfLading: "$5",
                portName: "Savanna, GA",
                portCode: 1234,
                manufacturerName: "ACME CO",
                workflowStatus: "Submitted"
            },
            cbpEntry: $scope.cbpEntry,
            listEmailAddresses: [$scope.email.toEmailAddress]
        };

        $scope.reconEmail = {
            reconRows: [{
                    entryNumber: 9001,
                    portCode: 1234,
                    operation: "Operation X",
                    oldStatus: "Submitted",
                    newStatus: "Approved"
                },
                {
                    entryNumber: 1006,
                    portCode: 0001,
                    operation: "Operation Y",
                    oldStatus: "Submitted",
                    newStatus: "Denied"
                }],
            listEmailAddresses: [$scope.email.toEmailAddress]
        };

        $scope.nonExisMap = {
            listNonExisMapEmailDto: [$scope.cbpEntry, {
                    actionMessage: "Testing ABC",
                    opName: "OP 3",
                    actualArrivalDate: "01-01-2016",
                    estimatedArrivalDate: "01-15-2016",
                    releaseDate: "02-01-2016",
                    entryNo: "2222",
                    lines: "23, 55, 91",
                    hts: "HTS-2",
                    bol: "BOL-2",
                    carrier: "ABC",
                    mot: "MOT-2",
                    vessel: "Ship 17",
                    foreignPort: "1234",
                    portName: "Savanna, GA",
                    unlandingPort: "1111",
                    ftz: "FTZ-2",
                    importerEin: "5025",
                    importerName: "ACME",
                    importerAddress: "123 Somewhere St",
                    importerAddress2: "Suite 9",
                    importerCity: "Vienna",
                    importerState: "VA",
                    importerZip: "12345",
                    consigneeEin: "4999",
                    consigneeName: "CORP",
                    consigneeAddress: "555 Some Pl",
                    consigneeAddress2: "",
                    consigneeCity: "Rockville",
                    consigneeState: "MD",
                    consigneeZip: "33333",
                    lineNo: "1 33 25",
                    refMid: "MID-2",
                    refName: "John Reference",
                    refAddress: "800 Home Ct",
                    refCity: "Portland",
                    portId: "2222"
                }],
            listEmailAddresses: [$scope.email.toEmailAddress]
        };

        $scope.automatedEmail = {
            inboxEntryLines: [{
                    entryNumber: "W34",
                    entryLineNumber: "2",
                    workflowStatus: "Submitted",
                    portCode: "1234",
                    portName: "Savanna, GA"
                }, {
                    entryNumber: "T67",
                    entryLineNumber: "7",
                    workflowStatus: "Approved",
                    portCode: "5555",
                    portName: "Portland, OR"
                }],
            listEmailAddresses: [$scope.email.toEmailAddress]
        };

        $scope.agingSamplesEmail = {
            agingSamples: [{
                    dispositionDate: "05/10/2016",
                    entryLineNumber: "120",
                    sampleNumber: "1234",
                    storageLocation: "Savanna, GA"
                }, {
                    dispositionDate: "01/15/2015",
                    entryLineNumber: "4032",
                    sampleNumber: "5555",
                    storageLocation: "Portland, OR"
                }],
            listEmailAddresses: [$scope.email.toEmailAddress]
        };

        $scope.log = {
            username: null,
            message: null,
            level: $scope.logLevels[0]
        };

        $scope.audit = {
            principal: null,
            eventType: $scope.eventTypes[0],
            dataMessage: null
        };

        $scope.pdfDto = {
            formType: "Form163",
            fromAddress: "123 Main St, Springfield, IL 41011",
            individualName: "Tester Man",
            individualTitle: "Compliance Person",
            collectionDate: "2016-04-13T14:58:44Z",
            firmName: "Acme Firm",
            firmAddress: "123 Acme Dr, Acmeville, MD 11111",
            entryNumber: "12345678901",
            importerName: "Acme Imports",
            importerAddress: "123 Acme Imports Cir, Acmeville, MD 11111",
            officerName: "Joe Schmo",
            listPdfSampleDto: [
                {
                    sampleNo: "sample123",
                    productDescription: "Big Big Product",
                    itemModel: "ItemModel123",
                    reason: "Section 123 of Government Code"
                },
                {
                    sampleNo: "sample456",
                    productDescription: "Small Small Product",
                    itemModel: "ItemModel456",
                    reason: "Section 456 of Government Code"
                }
            ]
        };

        $scope.sendEmail = function () {
            console.log("sending email", $scope.email);
            $http({
                method: 'POST',
                url: "email/send",
                data: $scope.email
            }).then(function successCallback(response) {
                console.log("ss/sendEmail >>>" + response.data + "<<<");
            }, function errorCallback(response) {
                console.log(response);
            });
        };

        $scope.sendWorkflowEmail = function () {
            $scope.workflowEntry.listEmailAddresses = [$scope.email.toEmailAddress];
            console.log("sending workflow email", $scope.workflowEntry);
            $http({
                method: 'POST',
                url: "email/workflow",
                data: $scope.workflowEntry
            }).then(function successCallback(response) {
                console.log("ss/sendWorkflowEmail >>>" + response.data + "<<<");
            }, function errorCallback(response) {
                console.log(response);
            });
        };

        $scope.sendExisMapEmail = function () {
            $scope.reconEmail.listEmailAddresses = [$scope.email.toEmailAddress];
            console.log("sending exis map email", $scope.reconEmail);
            $http({
                method: 'POST',
                url: "email/exismap",
                data: $scope.reconEmail
            }).then(function successCallback(response) {
                console.log("ss/sendExisMapEmail >>>" + response.data + "<<<");
            }, function errorCallback(response) {
                console.log(response);
            });
        };

        $scope.sendNonExisMapEmail = function () {
            $scope.nonExisMap.listEmailAddresses = [$scope.email.toEmailAddress];
            console.log("sending non exis map email", $scope.nonExisMap);
            $http({
                method: 'POST',
                url: "email/nonexismap",
                data: $scope.nonExisMap
            }).then(function successCallback(response) {
                console.log("ss/sendNonExisMapEmail >>>" + response.data + "<<<");
            }, function errorCallback(response) {
                console.log(response);
            });
        };

        $scope.sendOverrideMapEmail = function () {
            $scope.reconEmail.listEmailAddresses = [$scope.email.toEmailAddress];
            console.log("sending override map email", $scope.reconEmail);
            $http({
                method: 'POST',
                url: "email/overridemap",
                data: $scope.reconEmail
            }).then(function successCallback(response) {
                console.log("ss/sendOverrideMapEmail >>>" + response.data + "<<<");
            }, function errorCallback(response) {
                console.log(response);
            });
        };

        $scope.sendAgingSamplesEmail = function () {
            $scope.agingSamplesEmail.listEmailAddresses = [$scope.email.toEmailAddress];
            console.log("sending aging samples email", $scope.agingSamplesEmail);
            $http({
                method: 'POST',
                url: "email/agingsamples",
                data: $scope.agingSamplesEmail
            }).then(function successCallback(response) {
                console.log("ss/sendAgingSamplesEmail >>>" + response.data + "<<<");
            }, function errorCallback(response) {
                console.log(response);
            });
        };

        $scope.sendCBPHoldEmail = function () {
            $scope.automatedEmail.listEmailAddresses = [$scope.email.toEmailAddress];
            console.log("sending cbp hold email", $scope.automatedEmail);
            $http({
                method: 'POST',
                url: "email/cbphold",
                data: $scope.automatedEmail
            }).then(function successCallback(response) {
                console.log("ss/sendCBPHoldEmail >>>" + response.data + "<<<");
            }, function errorCallback(response) {
                console.log(response);
            });
        };

        $scope.sendStaleEntriesEmail = function () {
            $scope.automatedEmail.listEmailAddresses = [$scope.email.toEmailAddress];
            console.log("sending stale entries email", $scope.automatedEmail);
            $http({
                method: 'POST',
                url: "email/staleentry",
                data: $scope.automatedEmail
            }).then(function successCallback(response) {
                console.log("ss/sendStaleEntriesEmail >>>" + response.data + "<<<");
            }, function errorCallback(response) {
                console.log(response);
            });
        };

        $scope.sendRedFlagAlertEmail = function () {
            $scope.automatedEmail.listEmailAddresses = [$scope.email.toEmailAddress];
            console.log("sending red flag email", $scope.automatedEmail);
            $http({
                method: 'POST',
                url: "email/redflag",
                data: $scope.automatedEmail
            }).then(function successCallback(response) {
                console.log("ss/sendRedFlagAlertEmail >>>" + response.data + "<<<");
            }, function errorCallback(response) {
                console.log(response);
            });
        };

        $scope.sendFieldStaffEmail = function () {
            $scope.automatedEmail.listEmailAddresses = [$scope.email.toEmailAddress];
            console.log("sending field staff email", $scope.automatedEmail);
            $http({
                method: 'POST',
                url: "email/fieldstaff",
                data: $scope.automatedEmail
            }).then(function successCallback(response) {
                console.log("ss/sendFieldStaffEmail >>>" + response.data + "<<<");
            }, function errorCallback(response) {
                console.log(response);
            });
        };

        $scope.sendConditionalReleaseEmail = function () {
            $scope.automatedEmail.listEmailAddresses = [$scope.email.toEmailAddress];
            console.log("sending conditional release email", $scope.automatedEmail);
            $http({
                method: 'POST',
                url: "email/conditionalrelease",
                data: $scope.automatedEmail
            }).then(function successCallback(response) {
                console.log("ss/sendConditionalReleaseEmail >>>" + response.data + "<<<");
            }, function errorCallback(response) {
                console.log(response);
            });
        };

        $scope.sendClearHQREmail = function () {
            $scope.automatedEmail.listEmailAddresses = [$scope.email.toEmailAddress];
            console.log("sending clear hq referral email", $scope.automatedEmail);
            $http({
                method: 'POST',
                url: "email/clearhqr",
                data: $scope.automatedEmail
            }).then(function successCallback(response) {
                console.log("ss/sendClearHQREmail >>>" + response.data + "<<<");
            }, function errorCallback(response) {
                console.log(response);
            });
        };

        $scope.addLog = function () {
            console.log("adding log", $scope.log);
            var logsUrl = "logs/log";

            logService.addLog($scope.log);
        };

        $scope.addAudit = function () {
            console.log("adding audit", $scope.audit);

            $http({
                method: 'POST',
                url: "audit",
                data: $scope.audit
            }).then(function successCallback(response) {
                console.log("audit >>>" + response.data + "<<<");
                $scope.getAudits();
            }, function errorCallback(response) {
                console.log(response);
            });
        };

        $scope.getAudits = function () {
            $http({
                method: 'GET',
                url: 'audit',
                params: {
                    fromDate: $filter('date')(new Date(2015, 1, 1), 'yyyy-MM-dd'),
                    toDate: $filter('date')(new Date(2018, 1, 1), 'yyyy-MM-dd')
                }
            }).then(function successCallback(response) {
                console.log('ssController getAudits()', response);
                $scope.audits = response.data;
            }, function errorCallback(response) {
                console.log('Error: ssController getAudits()', response);
            });
        };

        $scope.getAudits();

        $scope.getPdf = function (pdfDto) {
            console.log("getting pdf");
            if (!pdfDto) {
                pdfDto = $scope.pdfDto;
            }
            $http({
                method: 'POST',
                url: "pdf/download",
                data: pdfDto,
                responseType: "arraybuffer"
            }).then(function successCallback(response) {
                    console.log("success get pdf");
                    var fileName = $scope.getFileNameFromHeader(response.headers('Content-Disposition'));
                    var file = new Blob([response.data], {type: 'application/pdf'});
                    if(window.navigator.msSaveOrOpenBlob) {
                        window.navigator.msSaveOrOpenBlob(file, fileName);
                    } else {
                        var fileURL = URL.createObjectURL(file);
                        var a = document.createElement('a');
                        a.href = fileURL; 
                        a.target = '_blank';
                        a.download = fileName;
                        document.body.appendChild(a);
                        a.click();
                        window.URL.revokeObjectURL(fileURL);
                    }
                }, function errorCallback(response) {
                    console.log(response);
                });
        };

        $scope.getFileNameFromHeader = function (header) {
            if (!header)
                return null;
            var result = header.split(";")[1].trim().split("=")[1];
            return result.replace(/"/g, '');
        };

    }]);

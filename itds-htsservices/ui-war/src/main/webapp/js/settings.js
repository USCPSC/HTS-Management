app.controller('settingsController', ['$scope', '$http', function ($scope, $http) {
        $scope.props = [];
        $scope.init = function () {
            console.log("settings::init()");
            $http({
                method: 'GET',
                url: "sysprops"
            }).then(function (response) {
                $scope.props = response.data;
                for (var i = 0; i < $scope.props.length; i++) {
                    $scope.props[i].isChanged = false;
                    $scope.props[i].origValue = $scope.props[i].value;
                }
                console.log("get sysprops success");
            }, function (response) {
                console.log("get sysprops error");
                console.dir(response);
            });
        };
        $scope.updateSysProp = function (prop) {
            console.log("settings::updateSysProp()");
            //console.dir(prop);
            $http({
                method: 'POST',
                url: "sysprops",
                data: prop
            }).then(function (response) {
                angular.copy(response.data, prop);
                prop.isChanged = false;
                prop.origValue = prop.value;
                console.log("post sysprops success");
                //console.dir(prop);
            }, function (response) {
                console.log("post sysprops error");
                console.dir(response);
            });
        };
        $scope.cancelSysProp = function (prop) {
            console.log("settings::cancelSysProp()");
            //console.dir(prop);
            prop.isChanged = false;
            prop.value = prop.origValue;
        };
        $scope.init();

    }]);

console.log("Done loading settings.js");
app.controller('userDashboardController', ['$scope', '$http', '$location', 'utilService', 'User', 'Session', 'authService', function($scope, $http, $location, utilService, User, Session, authService) {
    $scope.users=[];
    $scope.dashboard = {
        page: 0,
        pageSize: 10,
        columns: [
            {
                name: "username",
                display: "User ID",
                allowFilter: true,
                getValue: function(user) {
                    return utilService.truncate(user.username, 10);
            }},
            {
                name: "firstname",
                display: "First Name",
                allowFilter: true,
                getValue: function(user) {
                    return utilService.truncate(user.firstname, 10);
            }},
            {
                name: "lastname",
                display: "Last Name",
                allowFilter: true,
                getValue: function(user) {
                    return utilService.truncate(user.lastname, 10);
            }},
            {
                name: "email",
                display: "Email",
                allowFilter: true,
                getValue: function(user) {
                    return utilService.truncate(user.email, 15);
            }},
            {
                name: "orgcode",
                display: "Org Code",
                allowFilter: true,
                getValue: function(user) {
                    return utilService.truncate(user.orgcode, 10);
            }}
        ]
    };

    $scope.isAuthorized = function(authorizedRoles) {
        return authService.isAuthorized(authorizedRoles);
    }

    $scope.getUsers = function() {
        $http({
            method: 'GET',
            url: 'users',
            params: {
                userName: $scope.dashboard.columns[0].filter,
                firstName: $scope.dashboard.columns[1].filter,
                lastName: $scope.dashboard.columns[2].filter,
                email: $scope.dashboard.columns[3].filter,
                orgCode: $scope.dashboard.columns[4].filter,
                page: $scope.dashboard.page,
                pageSize: $scope.dashboard.pageSize,
                currentUser: Session.userId,
                password: Session.password
            }
        }).then(function successCallback(response) {
            console.log('userDashboardController getUsers()',response);
            $scope.users = response.data;
        }, function errorCallback(response) {
            console.log('Error: userDashboardController getUsers()',response);
        });
    };

    $scope.next = function() {
        if (!$scope.users.last) {
            $scope.dashboard.page+=1;
            $scope.getUsers();
        }
    };

    $scope.previous = function() {
        if (!$scope.users.first) {
            $scope.dashboard.page-=1;
            $scope.getUsers();
        }
    };

    $scope.selectUser = function (username) {
        $location.path("/users/" + username);
    };

    $scope.addUser = function () {
        $location.path("/users/new");
    };

    $scope.getUsers();
}]);

app.controller('userFormController', ['$scope', '$http', '$window', '$uibModal', 'growl', 'resolveUser', 'resolveData', 'Session', 'User', 'utilService', 'authService', function($scope, $http, $window, $uibModal, growl, resolveUser, resolveData, Session, User, utilService, authService) {
    $scope.pageTitle = resolveData.pageType + ' User';
    $scope.isEdit = resolveData.pageType == 'Edit';
    $scope.organizations = ['EXIS','CFIE','CFIW'];
    $scope.exisLocations = ['Baltimore, MD','Buffalo, NY','Champlain, NY','Charleston, SC','Chicago, IL','Dallas, TX',
    'Denver, CO','Detroit, MI','Houston, TX','JFK, NY','Laredo, TX','Los Angeles, CA','Miami/Port Everglades, FL',
    'Newark, NJ','Norfolk, VA','San Francisco, CA','San Juan, PR','Savanna, GA','Seattle, WA'];
    $scope.roles=[];
    $scope.ports=[];
    $scope.users=[];
    $scope.submitted = false;
    $scope.usernameIsUnique = true;
    $scope.uniqueUsernameError;
    $scope.isSelf = true;
    var modalInstance;
    
    $scope.utilService = utilService;

    $scope.isAuthorized = function(authorizedRoles) {
        return authService.isAuthorized(authorizedRoles);
    }

    $scope.populateForm = function() {
        $scope.getPorts();
    };

    $scope.getPortDisplayName = function(port) {
        var displayName = port.portCode + " (" + port.portName + ")";
        return displayName;
    };

    $scope.getPorts = function() {
        $http({
            method: 'GET',
            url: 'ports',
            params: {
                currentUser: Session.userId,
                password: Session.password
            }
        }).then(function successCallback(response) {
            console.log('userDashboardController getPorts()',response);
            $scope.ports = response.data;
            $scope.getRoles();
        }, function errorCallback(response) {
            console.log('Error: userDashboardController getPorts()',response);
        });
    };

    $scope.getRoles = function() {
        $http({
            method: 'GET',
            url: 'roles',
            params: {
                currentUser: Session.userId,
                password: Session.password
            }
        }).then(function successCallback(response) {
            console.log('userDashboardController getRoles()',response);
            $scope.roles = response.data;
            for (role in $scope.roles) {
                $scope.roles[role].selected=false;
            }
            $scope.getUsers();
        }, function errorCallback(response) {
            console.log('Error: userDashboardController getRoles()',response);
        });
    };

    $scope.getUsers = function() {
        $http({
        method: 'GET',
        url: 'users',
        params: {
            currentUser: Session.userId,
            password: Session.password
        }
        }).then(function successCallback(response) {
            console.log('userFormController getUsers()',response);
            $scope.users = response.data.content;
            $scope.getUser();
        }, function errorCallback(response) {
            console.log('Error: userFormController getUsers()',response);
        });
    };

    $scope.getUser = function() {
        $scope.user = resolveUser;
        $scope.user.refPorts.sort(function(a,b) {return (a.portCode > b.portCode) ? 1 : ((b.portCode > a.portCode) ? -1 : 0);} ); 
        utilService.removeSelectedOptions(resolveUser.refPorts, $scope.ports, 'portCode'); // remove current ports from source list

        for (i in $scope.roles) {
            for (j in $scope.user.refRoles) {
                if ($scope.roles[i].roleName == $scope.user.refRoles[j].roleName) {
                    $scope.roles[i].selected=true;
                }
            }
        }
        $scope.isSelf = $scope.user.username == Session.userId;
    };

    $scope.checkUsernameUniqueness = function() {
        return $http({
            method: 'GET',
            url: 'users/' + $scope.user.username + '/isunique',
            params: {
                currentUser: Session.userId,
                password: Session.password
            }
        }).then(function successCallback(response) {
            console.log('userFormController checkUsernameUniqueness()',response.data);
            $scope.usernameIsUnique = response.data;
            if ($scope.isEdit || response.data)
                $scope.uniqueUsernameError = undefined;
            else
                $scope.uniqueUsernameError = 'User ID must be unique';
        }, function errorCallback(response) {
            console.log('Error: userFormController checkUsernameUniqueness()',response);
        });
    };

    $scope.populateForm();

    $scope.save = function() {
        $scope.submitted = true;
        $scope.checkUsernameUniqueness().then(function (){
            if ($scope.user.isValid() && ($scope.isEdit || ($scope.usernameIsUnique && $scope.user.isPasswordValidForAdd()))) {
                var selectedRoles = [];
                var selectedSupervisors = [];
                for (role in $scope.roles) {
                    if ($scope.roles[role].selected){
                        selectedRoles.push($scope.roles[role]);
                    }
                }
                var url = $scope.isSelf ? 'account' : 'users'
                $scope.user.refRoles = selectedRoles;
                $http({
                    method : 'POST',
                    url : url,
                    data : $scope.user,
                    params: {
                        currentUser: Session.userId,
                        password: Session.password
                    }
                }).then( function successCallback(response) {
                    console.log("userFormController save()", response.data);
                    growl.addInfoMessage("Changes Saved", {ttl: 5000});
                    //$window.history.back();
                }, function errorCallback(response) {
                    console.log("ERROR: userFormController save()", response);
                    growl.addErrorMessage("Error Saving User", {ttl: 5000});
                });
            } else {
                growl.addErrorMessage("Please correct validation error(s)", {ttl: 5000});
            }
        })
    };

    $scope.cancel = function() {
        $window.history.back();
    };

    $scope.delete = function() {
        modalInstance = $uibModal.open({
            templateUrl: './user/deleteModal.html',
            controller: 'deleteController',
            resolve: {
                resolvedUser: function () {
                    return $scope.user;
                }
            }
        });
    };

    $scope.$on("$destroy", function() {
        if (modalInstance) {
            modalInstance.close();
            modalInstance = undefined;
        }
    });
}]);

//app.directive('multiSelect', ['$q', function($q) {
//  return {
//    restrict: 'E',
//    require: 'ngModel',
//    scope: {
//      selectedLabel: "@",
//      availableLabel: "@",
//      attrValue: "@",
//      displayValue: '&',
//      available: "=",
//      model: "=ngModel"
//    },
//    template: '<div class="multiSelect">' +
//                '<div class="select left-select col-xs-5">' +
//                  '<label class="control-label" for="multiSelectAvailable">{{ availableLabel }} ' +
//                      '({{ available.length }})</label>' +
//                  '<select id="multiSelectAvailable" class="form-control" ng-model="selected.available" multiple ' +
//                      'ng-options="e as displayValue({e:e}) for e in available"></select>' +
//                '</div>' +
//                '<div class="select buttons col-xs-1">' +
//                  '<button class="btn mover right" ng-click="add()" title="Add selected" ' +
//                      'ng-disabled="selected.available.length == 0">' +
//                    '<i class="fa fa-arrow-right"></i>' +
//                  '</button>' +
//                  '<button class="btn mover left" ng-click="remove()" title="Remove selected" ' +
//                      'ng-disabled="selected.current.length == 0">' +
//                    '<i class="fa fa-arrow-left"></i>' +
//                  '</button>' +
//                '</div>' +
//                '<div class="select right-select col-xs-5">' +
//                  '<label class="control-label" for="multiSelectSelected">{{ selectedLabel }} ' +
//                      '({{ model.length }})</label>' +
//                  '<select ng-model="selected.current" multiple ' +
//                      'class="form-control" ng-options="e as displayValue({e:e}) for e in model">' +
//                  '</select>' +
//                '</div>' +
//
//              '</div>',
//    link: function(scope, elm, attrs) {
//      scope.selected = {
//        available: [],
//        current: []
//      };
//
//      /* Handles cases where scope data hasn't been initialized yet */
//      var dataLoading = function(scopeAttr) {
//        var loading = $q.defer();
//        if(scope[scopeAttr]) {
//          loading.resolve(scope[scopeAttr]);
//        } else {
//          scope.$watch(scopeAttr, function(newValue, oldValue) {
//            if(newValue !== undefined)
//              loading.resolve(newValue);
//          });
//        }
//        return loading.promise;
//      };
//
//      /* Filters out items in original that are also in toFilter. Compares by reference. */
//      var filterOut = function(original, toFilter) {
//        var filtered = [];
//        angular.forEach(original, function(entity) {
//          var match = false;
//          for(var i = 0; i < toFilter.length; i++) {
//            if(toFilter[i][attrs.attrValue] == entity[attrs.attrValue]) {
//              match = true;
//              break;
//            }
//          }
//          if(!match) {
//            filtered.push(entity);
//          }
//        });
//        return filtered;
//      };
//
//      scope.refreshAvailable = function() {
//        scope.available = filterOut(scope.available, scope.model);
//        scope.selected.available = [];
//        scope.selected.current = [];
//      };
//
//      scope.add = function() {
//        scope.model = scope.model.concat(scope.selected.available);
//        scope.refreshAvailable();
//      };
//      scope.remove = function() {
//        scope.available = scope.available.concat(scope.selected.current);
//        scope.model = filterOut(scope.model, scope.selected.current);
//        scope.refreshAvailable();
//      };
//
//      $q.all([dataLoading("model"), dataLoading("available")]).then(function(results) {
//        scope.refreshAvailable();
//      });
//    }
//  };
//}]);

app.controller('deleteController', ['$scope', '$http', '$window', '$uibModalInstance', 'growl', 'resolvedUser', 'Session', function($scope, $http, $window, $uibModalInstance, growl, resolvedUser, Session) {
    $scope.delete = function () {
        $http({
            method : 'DELETE',
            url : "users/" + resolvedUser.username + "/delete",
            data : resolvedUser,
            params: {
                currentUser: Session.userId,
                password: Session.password
            }
        }).then( function successCallback(response) {
            console.log("deleteController delete()", response.data);
            growl.addInfoMessage("Item Deleted", {ttl: 5000});
            $window.history.back();
        }, function errorCallback(response) {
            console.log("ERROR: deleteController delete()", response);
            growl.addErrorMessage("Error Deleting User", {ttl: 5000});
        });
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}]);

app.factory('userRepository', ['$http','User','Session', function($http,User,Session) {
    var userRepository = {
        getUser: function (username) {
            return $http({
                method : 'GET',
                url : "users/" + username,
                params: {
                    currentUser: Session.userId,
                    password: Session.password
                }
            }).then(function successCallback(response) {
                console.log('userRepository getUser(username)',response);
                var user = new User(response.data);
                return user;
            }, function errorCallback(response) {
                console.log('Error: userRepository getUser(username)',response);
            });
        },
        getNew: function () {
            var user = new User();
            return user;
        }
    };

    return userRepository;
}]);

app.factory('User', function () {
    function User(user) {
        user = user ? user : {};
        this.username = user.username ? user.username : "";
        this.password = user.password ? user.password : "";
        this.confirmation = user.confirmation ? user.confirmation : "";
        this.passwordSalt = user.passwordSalt ? user.passwordSalt : "";
        this.firstname = user.firstname ? user.firstname : "";
        this.lastname = user.lastname ? user.lastname : "";
        this.active = user.active ? user.active : false;
        this.createTimestamp = user.createTimestamp ? user.createTimestamp : undefined;
        this.createuserid = user.createuserid ? user.createuserid : "";
        this.email = user.email ? user.email : "";
        this.exisLocation = user.exisLocation ? user.exisLocation : "";
        this.supervisors = user.supervisors ? user.supervisors : [];
        this.inboxrowsshown = user.inboxrowsshown ? user.inboxrowsshown : "";
//        this.inpoxrowshown = user.inpoxrowshown ? user.inpoxrowshown : "";
        this.lastlogintimestamp = user.lastlogintimestamp ? user.lastlogintimestamp : undefined;
        this.lastlogouttimestamp = user.lastlogouttimestamp ? user.lastlogouttimestamp : undefined;
        this.lastupdateTimestamp = user.lastupdateTimestamp ? user.lastupdateTimestamp : undefined;
        this.lastupdateuserid = user.lastupdateuserid ? user.lastupdateuserid : "";
        this.orgcode = user.orgcode ? user.orgcode : "";
        this.refRoles = user.refRoles ? user.refRoles : [];
        this.refPorts = user.refPorts ? user.refPorts : [];
        this.inboxrowsshown = user.inboxrowsshown ? user.inboxrowsshown : undefined;
    }

    User.prototype.isValid = function () {
        return (this.isUsernameValid() && this.isPasswordValidForEdit() && this.isFirstnameValid() &&
        this.isLastnameValid() && this.isExisSupervisorValid() && this.isEmailValid() && this.isExisLocationValid() &&
        this.isExisSupervisorValid() && this.isOrgcodeValid() && this.isInboxrowsshownValid());
    };

    User.prototype.isUsernameValid = function () {
        if (!this.username) {
            this.usernameError = "User ID is required";
            return false;
        } else if ('regex',/^([A-Za-z0-9]{26,})$/.test(this.username)) {
            this.usernameError = "User ID must contain 25 or fewer characters";
            return false;
        } else if ('regex',/^([A-Za-z0-9]{0,25})$/.test(this.username) && this.username == this.username.trim()) {
            this.usernameError = undefined;
            return true;
        } else {
            this.usernameError = "User ID may not contain spaces or special characters";
            return false;
        }
    };

    User.prototype.isPasswordValidForEdit = function () {
        if (!this.password) {
            this.passwordError = undefined;
            this.confirmationPasswordError = undefined;
            return true;
        } else if (!this.confirmation || this.password != this.confirmation) {
            this.passwordError = undefined;
            this.confirmationPasswordError = "Password and Confirmation Password must be identical";
            return false;
        } else {
            this.passwordError = undefined;
            this.confirmationPasswordError = undefined;
            return true;
        }
    };

    User.prototype.isPasswordValidForAdd = function () {
        if (!this.password) {
            this.passwordError = "Password is a required for new users";
            this.confirmationPasswordError = undefined;
            return false;
        } else if (!this.confirmation || this.password != this.confirmation) {
            this.passwordError = undefined;
            this.confirmationPasswordError = "Password and Confirmation Password must be identical";
            return false;
        } else {
            this.passwordError = undefined;
            this.confirmationPasswordError = undefined;
            return true;
        }
    };

    User.prototype.isFirstnameValid = function() {
        if (!this.firstname) {
            this.firstnameError = undefined;
            return true;
        } else if (this.firstname.length > 100) {
            this.firstnameError = "First Name must contain 100 or fewer characters";
            return false;
        } else {
            this.firstnameError = undefined;
            return true;
        }
    };

    User.prototype.isLastnameValid = function() {
        if (!this.lastname) {
            this.lastnameError = undefined;
            return true;
        } else if (this.lastname.length > 100) {
            this.lastnameError = "Last Name must contain 100 or fewer characters";
            return false;
        } else {
            this.lastnameError = undefined;
            return true;
        }
    };

    User.prototype.isEmailValid = function() {
        if (!this.email) {
            this.emailError = undefined;
            return true;
        } else if (this.email.length > 255) {
            this.emailError = "Email must contain 255 or fewer characters";
            return false;
        } else if (/^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i.test(this.email) && this.email == this.email.replace(/ /g,'')) {
            this.emailError = undefined;
            return true;
        } else {
            this.emailError = "Email must be valid";
            return false;
        }
    };

    User.prototype.isExisLocationValid = function() {
        if (!this.exisLocation) {
            this.exisLocationError = undefined;
            return true;
        } else if (this.exisLocation.length > 255) {
            this.exisLocationError = "Exis Location must contain 255 or fewer characters";
            return false;
        } else {
            this.exisLocationError = undefined;
            return true;
        }
    };

    User.prototype.isExisSupervisorValid = function() {
        if (!this.exisSupervisor) {
            this.exisSupervisorError = undefined;
            return true;
        } else if (this.exisSupervisor.length > 255) {
            this.exisSupervisorError = "Exis Supervisor must contain 255 or fewer characters";
            return false;
        } else {
            this.exisSupervisorError = undefined;
            return true;
        }
    };

    User.prototype.isOrgcodeValid = function() {
        if (!this.orgcode) {
            this.orgcodeError = undefined;
            return true;
        } else if (this.orgcode.length > 15) {
            this.orgcodeError = "Org Code must contain 15 or fewer characters";
            return false;
        } else {
            this.orgcodeError = undefined;
            return true;
        }
    };

    User.prototype.isInboxrowsshownValid = function() {
        var n = ~~Number(this.inboxrowsshown);
        if (String(n) === String(this.inboxrowsshown) && n > 0 && n <= 100) {
            this.inboxrowsshownError = undefined
            return true;
        } else {
            this.inboxrowsshownError = "Inbox Rows Shown must be a positive integer between 1 and 100";
            return false;
        }
    }

    return User;
});

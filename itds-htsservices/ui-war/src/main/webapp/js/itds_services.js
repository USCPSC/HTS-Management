/**
 * services that are used to support controllers
 *
 */
console.log("loading itds_services.js");

app.constant('AUTH_EVENTS', {
    loginSuccess: 'auth-login-success',
    loginFailed: 'auth-login-failed',
    logoutSuccess: 'auth-logout-success',
    notAuthenticated: 'auth-not-authenticated',
    notAuthorized: 'auth-not-authorized',
    keepAlive: 'keep-alive'
});

app.constant('USER_ACTIONS', {
    getUsers: 'ROLE_GCUSR',
    saveUser: 'ROLE_SCUSR',
    deleteUser: 'ROLE_DCUSR',
    saveEtl: 'ROLE_ETL',
    getMetrics: 'ROLE_MTRCS'
});

app.constant( 'SEARCH_COLLECTIONS', {
	'inbox': {
		'primary':'collectionInboxPrimary',
		'quick':'collectionInboxQuick',
		'public':'collectionInboxPublic',
		'portgroup':'collectionInboxPortgroup',
		'private':'collectionInboxPrivate'
	},
	'exam': {
		'primary':'collectionExamPrimary',
		'quick':'collectionExamQuick',
		'public':'collectionExamPublic',
		'portgroup':'collectionExamPortgroup',
		'private':'collectionExamPrivate'
	}
});

app.constant('APP_CONSTANTS', {
	EVENTS: {
		createExam: 'create-exam',
		openExamDetail: 'open-exam-detail',
		closeExamDetail: 'close-exam-detail'
		},
	MISC: {
	}
});

app.directive('sortIcons', function() {
	  return {
		    restrict: 'E',
		    scope: {
		      sortKey: "=key",
		      reverse: "=reverse",
		      myKey: '@value'
		    },
		    templateUrl: 'custom/sortIcons.html'
		  };
		});

/**
 * A generic confirmation for risky actions.
 * Usage: Add attributes: ng-really-message="Are you sure"? ng-really-click="takeAction()" function
 */
app.directive('ngReallyClick', [function() {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            element.bind('click', function() {
                var message = attrs.ngReallyMessage;
                if (message && confirm(message)) {
                    scope.$apply(attrs.ngReallyClick);
                }
            });
        }
    }
}]);

app.service('Session', ['searchService', function (searchService) {
//app.service('Session', ['searchService','SEARCH_COLLECTIONS', function (searchService,SEARCH_COLLECTIONS) {
    this.create = function (user) {
        this.userId = user.username;
        //this.authentication = user.authentication;
        this.userRoles = user.refRoles;
        this.lastActive = new Date();

        // get collections
   		searchService.resetSavedSearchCollection();
    };
    this.destroy = function () {
        this.userId = null;
        //this.authentication = null;
        this.userRoles = null;
        this.lastActive = null;
        
        if( searchService.sdIsEdited ) {
        	;
        }
    };
}]);

app.factory('propertyService', ['$http', function ($http) {
    var propertyService = {};

    propertyService.getProperties = function () {

        return $http({
            method : 'GET',
            url : 'properties'
        }).then(function (res) {
            var properties = res.data;
            return properties;
        });
    };

    return propertyService;
}]);

app.factory('authService', ['$http', 'Session', 'localStorageService', 'logService', '$q',
                            function ($http, Session, localStorageService, logService, $q) {
    var authService = {};

    authService.ssoLogin = function () {
        return $http({
            method : 'POST',
            url : 'login/sso'
        }).then(function (res) {
            console.log("sso login res", res);
            var username = res.data;
            return username;
        }, function errorCallback(response) {
            console.log("ERROR: authService ssoLogin()", response);
            return $q.reject(response);
        });
    };

    authService.login = function (credentials) {
        var headers = credentials ? {authorization : "Basic "
                + btoa(credentials.username + ":" + credentials.password)
            } : {};
        console.log("headers",headers);
        return $http({
            method : 'GET',
            url : 'login',
            headers : headers
        }).then(function (res) {
            console.log("login res", res);
            var user = {
                username: res.data.name,
                refRoles: res.data.authorities
            }
            Session.create(user);
            return user;
        }, function errorCallback(response) {
            console.log("ERROR: authService login()", response);
            return $q.reject(response);
        });
    };

    authService.logout = function () {
        if (Session.userId) {
            $http({
                method : 'GET',
                url : 'logout'
            }).then( function successCallback(response) {
                console.log("authService logout()", response);
            }, function errorCallback(response) {
                console.log("ERROR: authService logout()", response);
            });
        }

        localStorageService.remove('user');
        localStorageService.remove('lastActive');
        Session.destroy();
    };

    authService.continueSession = function (user, lastActive) {
        Session.lastActive = new Date(lastActive);
        Session.create(user);
        var session = authService.getSession();
        return true;
    };

    authService.isAuthenticated = function () {
        return !!Session.userId;
    };

    authService.isAuthorized = function (authorizedRoles) {
        var authorized = (authorizedRoles.length == 0);
        for (userRole in Session.userRoles) {
            if (authorizedRoles.indexOf(Session.userRoles[userRole].authority) !== -1){
                authorized = true;
            }
        }

        return authorized;
    };

    authService.getSession = function() {
        return $http({
            method : 'GET',
            url : 'session'
        }).then(function (res) {
            var session = res.data;
            return session;
        }, function (res) {
            console.log("ERROR: appController getSession()", res);
            return $q.reject(response);
        });
    }

    authService.getLandingPage = function() {
        var landingOnInbox = authService.isAuthorized(["ROLE_INBOX_READ"]);
        return landingOnInbox ? '/inbox' : '/examlog';
    }

    return authService;
}]);

app.factory('navigationService', [ '$window', function($window) {
	var service = {};

	service.currentTab = "home";

	service.setCurrentTab = function(tab) {
		this.currentTab = tab;
		console.log(this);
	};

	service.activeCss = function(tab) {
		return (tab == this.currentTab) ? "active" : "";
	};


	/*
	 * This sub-service in navigationService is created to allow life-cycle tracking of "pages" in the context of Single Page Application enabled by AngularJS.
	 * It provides a mechanism for each "page" to register a set of callback when an action causes the view to navigate away.
	 */
	service.pageNavigationService = {

		// current "page"
		currentPageName : "",

		// callback to check if page has a form which is dirty
		pageDirtyCallback: function() { return false;},

		// callback to ask for confirmation
		confirmCallback: function() { return true; },

		// callback to do cleanup
		CleanupCallback: function() {},

		/*
		 * set current page and its callback function
		 *
		 * Input:
		 * 		currentPageName : the name of current page. Example: "EDIT_EXAM"
		 *  	pageDirtyCallback: function():callback to check if page has a form which is dirty. return true if is dirty and needs user confirmation.
		 *  	confirmCallback: function(): callback to ask for confirmation. return true if ok, false if cancel
		 *  	CleanupCallback: function() - callback to the current page to do cleanup before navigate to new page
		 *
		 *  Output:
		 *  	none
		 *
		 *  Example:
		 *          navigationService.pageNavigationService.setCurrentPage({
		 *				currentPageName: "view_entry_detail_page",
		 *				CleanupCallback: function() {
		 *			   	$scope.detail.selectedEntryLine = null; // close entryLine detail
		 *			}
         *
        })

		 */
		setCurrentPage : function(pageObject) {

			//data: {currentPageName:string, pageDirtyCallback():true/false,confirmCallback():,CleanupCallback():void}

			this.currentPageName = pageObject.currentPageName;

			if(pageObject.pageDirtyCallback) {
				this.pageDirtyCallback = pageObject.pageDirtyCallback;
			} else {
				this.pageDirtyCallback = function() {return false;}
			};

			if(pageObject.confirmCallback) {
				this.confirmCallback = pageObject.confirmCallback;
			} else {
				this.confirmCallback = function() {
					//alert("Please save changes or cancel edit before proceed");
					//return false;
					var ok = confirm("Unsaved data will be lost! Press OK to discard changes, or press Cancel to stay on the same screen.");
					return ok;

					}
			};

			if(pageObject.CleanupCallback) {
				this.CleanupCallback = pageObject.CleanupCallback;
			} else {
				this.CleanupCallback = function() {}
			};

		},

		/*
		 * Invoke the callback functions for current page before navigate away
		 *
		 * input:
		 * 		event - the browser event object
		 *
		 * output:
		 * 		true/false - if navigation can proceed
		 *
		 * Example:
		 * 		if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
		 */
		beforeLeavingCurrentPage : function(event) {
			if(this.currentPageName) {
				// dirty form checking
				if(this.pageDirtyCallback(event)) {
					if (! this.confirmCallback(event)) {
						event.preventDefault();
						return false; // can not leave current page. Cancel action
					}
				}
				// current page does cleanup to be ready for new page
				this.CleanupCallback(event);

				// auto reset before go to new page
				this.currentPageName = undefined;
				return true; // continue action
			} else {
				return true; // continue action
			};


		},

	};

	return service;
} ]);

app.factory('utilService', ['$filter', function ($filter) {
    var utilService = {};

    utilService.truncate = function (value, length) {
        value = value ? value : "";
        return value.substring(0,length) == value ? value : value.substring(0,length) + "...";
    };

    utilService.filter = function (field, filter) {
        return field.toLowerCase().indexOf(filter.toLowerCase()) > -1;
    }

    utilService.getFormattedName = function(firstname, lastname) {
        var formattedName = lastname && firstname ? lastname + ', ' + firstname : lastname;
        formattedName = formattedName ? formattedName : firstname;
        formattedName = formattedName ? formattedName : "";
        return formattedName;
    }

    utilService.getFormattedDate = function(date) {
        var formattedDate = new Date(date);
        return $filter('date')(formattedDate, 'medium');
    };

    // add to facilitate sorting
    utilService.sortArrayBy = function( field, reverse, primer ){
    	var key = primer ?
    		function(x) { return primer( x[field] )} :
    		function(x) { return x[field] };
    	reverse = !reverse ? 1 : -1;
    	return function( a, b ){
    		return a = key(a), b = key(b), reverse * ((a > b) - (b > a));
    	}
    };
    
    // return the $scope of an AngularJS controller
    utilService.getControllerScope = function(controllerName) {
    	var elem = angular.element($('[ng-controller="' + controllerName + '"]'));
    	if(elem === undefined) {
    		return elem;
    	}
    	var scope = elem.scope();
    	return scope;
    };

    // return the $scope of an AngularJS controller
    utilService.getCurrentFiscalYear = function() {
    	var dt = new Date();
    	var currentFY = dt.getFullYear();
    	if(dt.getMonth()>=9) {
    		currentFY++;
    	}
    	return currentFY;
    };
    
    utilService.moveSelectedOptions = function(selected, from, to, idProperty){
    	console.log('move ', selected, ' from ', from, ' to ', to);
    	
    	selected.forEach(function(selectedId) {
    		console.log("selectedId="+selectedId);
    		$.each(from, function(i, opt) {
    			if(selectedId === opt[idProperty]) {
    				to.push( from[i] );
    				from.splice(i, 1);
    				return false;
    			}
    		});

    	});
    }
    
    utilService.removeSelectedOptions = function(selected, from, idProperty){
    	console.log('remove ', selected, ' from ', from);
    	
    	selected.forEach(function(selectedId) {
    		console.log("selectedId="+selectedId);
    		$.each(from, function(i, opt) {
    			if(selectedId[idProperty] === opt[idProperty]) {
    				from.splice(i, 1);
    				return false;
    			}
    		});

    	});
    }

return utilService;
}]);


app.factory('workflowService', [  function() {
	var service = {};

	service.getWorkflowNavigation = function(workflowStatus, hasExam, nationalOp) {
		var img = "";
		var menu = [];
		switch(workflowStatus) {
			case 'CBP CANCELLED':
				//img = "workflow_scored.png";
				img = "workflow_2_0.png";
				menu = [{header:"--final status--"}];
				break;
	
			case 'Scored':
			case 'NR':
				//img = "workflow_scored.png";
				img = "workflow_2_0.png";
					menu = ['CBP Hold Requested', 'Refer to Field', 'May Proceed'];
				break;

			case 'CBP Hold Requested':
			case 'CBPRQ':
//				img = "workflow_requested.png";
				img = "workflow_2_0.png";
				menu = ['CBP Hold Approved', {divider:true}, {header: 'CBP Released'}, 'Hold Rejected', 'Entry Released']; // {divider:true},  
				break;

			case 'CBP Hold Approved':
			case 'CBPHA':
//				img = "workflow_approved.png";
				img = "workflow_2_0.png";
				if(hasExam) {
					menu = [{header: 'Examined'}, 'Examined & Released', 'Sampled & Detained', 'Sampled & Conditionally Released'];
				} else {
					menu = [{header: 'Released'}, 'CR for Exam', 'CPSC Doc Review', 'CI Unavailable', 'CBP Released (Post Approval)'];
//					menu = [{header: 'Released'}, 'CR for Exam', 'CPSC Doc Review','CI Unavailable','CBP Released (Post Approval)','Release w/o Phys Exam (other)'];
				}
				//menu = [{header: 'Examined'}, 'Examined & Released','Sampled & Detained','Sampled & Conditionally Released',{divider:true},{header: 'Released'}, 'CPSC Doc Review','CI Unavailable','CBP Released (Post Approval)','Release w/o Phys Exam (other)'];
				break;

			case 'Hold Rejected':
			case 'CBPHR':
	  		case 'Entry Released':
			case 'PRPST':
//				img = "workflow_cbp_released.png";
				img = "workflow_2_0.png";
				menu = ['Request Redelivery', 'Refer to Field', {divider:true}, {header:"--final status--"}];
				break;

			case 'Request Redelivery':
			case 'RQRD':
//				img = "workflow_redelivery.png";
				img = "workflow_2_0.png";
				menu = [];
				if(hasExam) {
					menu = ['Refer to Field', {divider:true}, {header: 'Examined'}, 'Examined & Released', 'Sampled & Detained', 'Sampled & Conditionally Released'];
				} else {
					menu = ['Refer to Field', {divider:true}, {header: 'Released'}, 'CR for Exam', 'CPSC Doc Review', 'CI Unavailable', 'CBP Released (Post Approval)'];  // 'Release w/o Phys Exam (other)' not on chart
				}
//				menu = [{header: 'Examined'}, 'Examined & Released','Sampled & Detained','Sampled & Conditionally Released',{divider:true},{header: 'Released'}, 'CPSC Doc Review','CI Unavailable','CBP Released (Post Approval)','Release w/o Phys Exam (other)',{divider:true},'Refer to Field',{header:"--final status--"},'Redelivered for Seizure'];
				break;

	  		case 'Examined & Released':
			case 'ELRL':
	  		case 'Sampled & Detained':
			case 'IMRF':
//				img = "workflow_examined.png";
				img = "workflow_2_0.png";
				menu = [{header:"--final status--"}];
				break;
				
	  		case 'Sampled & Conditionally Released':
			case 'SCR':
//				img = "workflow_examined.png";
				img = "workflow_2_0.png";
				menu = [{header: '--CR Outcome--'}, 'CR Outcome - Released', 'Reconditioned', 'Redelivered for Seizure'];
				break;

			case 'CR for Exam':
			case 'CREXM':
//				img = "workflow_released.png";
				img = "workflow_2_0.png";
				menu = ['Request Redelivery', {divider:true}, {header:"--CR Outcome--"}, 'CR Outcome - Released', 'Reconditioned', 'Redelivered for Seizure'];
				break;

	  		case 'CPSC Doc Review':
			case 'DOCRV':
	  		case 'CI Unavailable':
			case 'CIUNV':
//				img = "workflow_released.png";
				img = "workflow_2_0.png";
				menu = ['Request Redelivery', {divider:true}, {header:"--final status--"}];
				break;
				
	  		case 'CBP Released (Post Approval)':
			case 'PRIRE':
//				img = "workflow_released.png";
				img = "workflow_2_0.png";
				menu = ['Request Redelivery', 'Refer to Field', {divider:true}, {header:"--final status--"}];
				break;
				
			case 'May Proceed':
			case 'MPROC':
//				img = "workflow_proceed.png";
				img = "workflow_2_0.png";
				menu = ["--final status--"];
				break;

			case 'Refer to Field':
			case 'RFF':
//				img = "workflow_field.png";
				img = "workflow_2_0.png";
				menu = [{header:"--final status--"}];
				break;

			case 'CR Outcome - Released':
			case 'CRORL':
			case 'Reconditioned':
			case 'RECND':
			case 'Redelivered for Seizure':
			case 'RDSEZ':
//				img = "workflow_released.png";
				img = "workflow_2_0.png";
				menu = [{header:"--final status--"}];
				break;
				
			default:
//				img = "workflow_field.png";
				img = "workflow_2_0.png";
				menu = [{header:"--final status--"}];
				console.log("invalid workflow status: " + workflowStatus);
				break;
				
//	  		case 'Release w/o Phys Exam (other)':
//			case 'NOPHY':
			
		}
//menu.push("Scored");
		console.log("workflowstatus:" + workflowStatus + ", img:" + img + ", action menu:" + menu);
		console.log(menu);
		return {imgUrl: img, menu: menu};
	}

	return service;
} ]);

// jQuery sortable and selectable
$(function () {
	$('.div-container').selectable({
	    cancel: '.div-sortablehandle',
	    filter: "li",
	    //tolerance: "fit"
	});
	$('.div-container').sortable({
		axis: "y",
		cursor:"move",
	    cursorAt: { top:10, left:25 },
	    placeholder: "li-placeHolder",
	    connectWith: ".srch-coll-ul-list",
	    items: "ul>li",
	    handle: '.div-sortablehandle',
	    revert: true,
	    scroll: true,
	    scrollSensitivity: 15, 
	    scrollSpeed: 20,
	    helper: function (e, item) {
	        if (!item.hasClass('ui-selected')) {
	            $('.div-container ul').find('.ui-selected').removeClass('ui-selected');
	            item.addClass('ui-selected');
	        }
	
	        var selected = $('.ui-selected').clone();
	        item.data('multidrag', selected);
	        $('.ui-selected').not(item).remove();
	        return $('<li class="transporter" />').append(selected);
	    },
	    stop: function (e, ui) {
	        var selected = ui.item.data('multidrag');
	        ui.item.after(selected);
	        ui.item.remove();
	        console.log("test test test test");
	        console.log($(this).sortable('serialize'));
	        console.log("test test test test done");
	    }
	}).disableSelection();
});

// confirm dialog leave un-saved search definition
$(function() {
  $( "#dialog-confirm" ).dialog({
    resizable: false,
    height:140,
    modal: true,
    buttons: {
      "Leave Search": function() {
        $( this ).dialog( "close" );
      },
      Cancel: function() {
        $( this ).dialog( "save" );
      }
    }
  });
});

console.log("Done loading itds_service.js");


/**
 * exam contactTable Controller. Control for the contact tab: the list of contacts
 */
console.log ("loading exam_contactTable.js");

app.controller('contactViewController', ['$scope', '$http', '$route','$uibModal','$uibModalInstance', 'navigationService', 'Session', 'utilService','workflowService', 'APP_CONSTANTS', function($scope,$http, $route, $uibModal,$uibModalInstance, navigationService, Session, utilService,workflowService, APP_CONSTANTS) {
	//console.log($scope);
	// this the the root controller for contact popup
	//alert("*********** Entering contactViewController()");

	$scope.ok = function () {
/*		if($scope.contact_edit_form.$dirty) {
			$scope.exam_form.$setDirty();
		}
*/
	    $uibModalInstance.close();
	    $scope.contactDetail.close();
	  };

	  $scope.cancel = function () {
	    $uibModalInstance.dismiss('cancel');
	    $scope.contactDetail.close();
	  };
	  
}]);

app.controller('contactTableController', ['$scope', '$http', '$route','$uibModal', 'navigationService', 'Session', 'utilService','workflowService', 'APP_CONSTANTS', function($scope,$http, $route, $uibModal, navigationService, Session, utilService,workflowService, APP_CONSTANTS) {
	
	/**
	 * initialize the controller: invoke this at end of this controller definition
	 */
	$scope.init = function() {
		console.log("build contactTableController");
	}

	$scope.contactAction = {
			contactIdSeq: 1,
			
			setupEditForm: function() {
				//$scope.contactCopy = $scope.contact;
				
				console.log($scope);
	    	},

		    getContactTypes: function() {
		        $http({
		            method: 'GET',
		            url: 'lookup/contactTypes',
		            params: {
		                currentUser: Session.userId,
		                password: Session.password
		            }
		        }).then(function successCallback(response) {
		            console.log('getContactTypes',response);
		            $scope.contactTypes =  response.data;
		            console.log($scope);
		        }, function errorCallback(response) {
		            console.log('Error: getContactTypes',response);
		            return [];
		        });
		    },
		    
		    updateContactName: function (contact, contactTypes) {
		    	for(var i=0; i<contactTypes.length; i++) {
		    		if(contact.contactType === contactTypes[i].contactType) {
		    			contact.contactTypeName = contactTypes[i].contactTypeName;
		    			break;
		    		}
		    	}
		    },

			performContactAction: function(action, contact, index) {
				
				
				console.log(action);
				this.getContactTypes();				
				
				switch(action) {
				case 'View':
					$scope.contactDetail.selectContact(contact,index);
					break;
				case 'Add':
					if(!$scope.examCopy.examContacts) {
						$scope.examCopy.examContacts = [];
					} 
					var contact = {
							_flag: "c",
							examId:$scope.examCopy.examId
					}
					
					$scope.examCopy.examContacts.push(contact);
					var index = $scope.examCopy.examContacts.length;
					$scope.contactDetail.selectContact(contact,index);
					$scope.exam_form.$setDirty();

					break;
				case 'Copy':
					var contact1 = angular.copy(contact);
					contact1.contactId = undefined;
					contact1._flag="c"; // create
					contact1.examId = $scope.examCopy.examId;
					
					$scope.examCopy.examContacts.push(contact1);
					var index = $scope.examCopy.examContacts.length;
					$scope.contactDetail.selectContact(contact1,index);
					$scope.exam_form.$setDirty();

					break;
				case 'CancelAdd':
					if($scope.examCopy.examContacts && $scope.examCopy.examContacts.length>0) {
						var lastIndex = $scope.examCopy.examContacts.length-1;
						for(var i=lastIndex; i>=0; i--) {
							if($scope.examCopy.examContacts[i]._flag == 'c' && $scope.examCopy.examContacts[i] == contact) {
								$scope.examCopy.examContacts.splice(i,1);
							}
						}
					}
					break;
				case 'Edit':
					$scope.contactDetail.selectContact(contact,index);
					break;
				case 'Delete':
					$scope.examCopy.examContacts[index]._flag = "d"; // deleted
					$scope.exam_form.$setDirty();

					break;
				default:
					break;
				}
					
			},
			
		};

	
	
	
    $scope.screen = {
    	    fullScreen: false,
    		
    	    displayFullScreen: function(isFullScreen) {
    			this.fullScreen = isFullScreen;
    		},

    		processEscapeKey: function(keyCode) {
    			if(keyCode == 27) { // process ESCAPE Key
    			  this.displayFullScreen(false);
    			}
    		}
    }

    
    $scope.contactDetail = {
    		selectedContact: null, // selected contact to display details, key is property 'contactNumber'
    	    selectedIndex: 0,
    	    isHideAboveRows: true,
    	    
    	    init: function() {
    		    $scope.$on(APP_CONSTANTS.EVENTS.closeExamDetail, function() {
    		    	$scope.contactDetail.selectedContact = null; // close exam detail
    		    })
    	    },
    	    
    	    isRowHidden: function (contact, index) {
    	    	if(contact._flag == 'd') {
    	    		return true;
    	    	}
    			return false;
    	    },

    	    isDetailRow: function (contact, index) {
    	    	return ($scope.contact && this.selectedContact && this.selectedContact.contactId == contact.contactId);
			},
			
			hideAboveRows: function (flag) {
				this.isHideAboveRows = flag;
			},    


		close: function() {
	    		$scope.contactDetail.selectedContact = null; // close exam detail
		},
		
		selectContact: function(contact,index) {
				var me = this;

	  		    if($scope.contactDetail.selectedContact && $scope.contactDetail.selectedContact.contactId == contact.contactId) {
	  	    		$scope.contactDetail.selectedContact = null; // close exam detail
	  	    		return;
	  	    	} 

	  	    	this.selectedContact = contact; // show detail for this contact
	  			this.selectedIndex = index; // the index in the loop
	  			//this.expandColumn = "EXAM";
	  			this.hideAboveRows(true);

	  	    	$scope.contactDetail.selectedContact = contact; // show detail for this exam
	  	    	$scope.contact = contact;
	  	    	
  				if($scope.exam._editMode_ == 'EDIT' || $scope.exam._editMode_ == 'CREATE') {
  	  				$scope.contactAction.setupEditForm();
	  				var modalInstance = $uibModal.open({
						templateUrl: './examlog/popup/contact_popup.html',
						controller: 'contactViewController',
						scope: $scope,
						backdrop: "static",
						appendTo: angular.element("#exam_contacts_edit_div")
						});	  	
  	  				}

  				if($scope.exam._editMode_ == 'VIEW') {
	  				var modalInstance = $uibModal.open({
						templateUrl: './examlog/popup/contact_popup.html',
						controller: 'contactViewController',
						scope: $scope,
						backdrop: "static"
						});	  	
	  				}
	  					
	              
	  	    	
	  	    	// fetch contact details if existing record
				//if($scope.contactDetail.selectedContact.contactId != undefined) {
				if(!$scope.contactDetail.selectedContact._flag || $scope.contactDetail.selectedContact._flag in ["k","s"]) {
		  	        var url = "exams/contacts/" + $scope.contactDetail.selectedContact.contactId;
		  	        $http({
		  	      	  method: 'POST',
		  	      	  url: url
		  	      	}).then(function successCallback(response) {
		  	  				console.log(response);
		  	  				$scope.examCopy.examContacts[index] = response.data;
		  	  				$scope.examCopy.examContacts[index]._flag = "e"; // detail loaded
		  	  				
		  	  				//$scope.contact = response.data;
		  	  				$scope.contact = $scope.examCopy.examContacts[index];
		  	  				
		  	  				if($scope.exam._editMode_ == 'EDIT') {
		  	  				  $scope.examCopy.examContacts[index]._flag = "u";
		  	  				  $scope.contactAction.setupEditForm();
		  	  				}
		  	      	  }, function errorCallback(response) {
		  	  				console.log(response);
		  	      	  });
				}
	  	    	
			}

	
    } // end of contactDetail object
    
    
    // call init()
    $scope.init();

}]);

console.log ("Done loading exam_contactTable.js");

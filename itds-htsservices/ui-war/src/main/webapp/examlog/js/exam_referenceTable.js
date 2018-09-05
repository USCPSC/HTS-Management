/**
 * exam referenceTable Controller. Control for the reference tab: the list of references
 */
console.log ("loading exam_referenceTable.js");

app.controller('referenceViewController', ['$scope', '$http', '$route','$uibModal','$uibModalInstance', 'navigationService', 'Session', 'utilService','workflowService', 'APP_CONSTANTS', function($scope,$http, $route, $uibModal,$uibModalInstance, navigationService, Session, utilService,workflowService, APP_CONSTANTS) {
	//console.log($scope);
	// this the the root controller for reference popup
	//alert("*********** Entering referenceViewController()");

	$scope.ok = function () {
/*		if($scope.reference_edit_form.$dirty) {
			$scope.exam_form.$setDirty();
		}
*/
	    $uibModalInstance.close();
	    $scope.referenceDetail.close();
	  };

	  $scope.cancel = function () {
	    $uibModalInstance.dismiss('cancel');
	    $scope.referenceDetail.close();
	  };
	  
}]);

app.controller('referenceTableController', ['$scope', '$http', '$route','$uibModal', 'navigationService', 'Session', 'utilService','workflowService', 'APP_CONSTANTS', function($scope,$http, $route, $uibModal, navigationService, Session, utilService,workflowService, APP_CONSTANTS) {
	
	/**
	 * initialize the controller: invoke this at end of this controller definition
	 */
	$scope.init = function() {
		console.log("build referenceTableController");
	}

	$scope.referenceAction = {
			referenceIdSeq: 1,
			
			setupEditForm: function() {
				//$scope.referenceCopy = $scope.reference;
				
				console.log($scope);
	    	},

		    getReferenceTypes: function() {
		        $http({
		            method: 'GET',
		            url: 'lookup/referenceTypes',
		            params: {
		                currentUser: Session.userId,
		                password: Session.password
		            }
		        }).then(function successCallback(response) {
		            console.log('getReferenceTypes',response);
		            $scope.referenceTypes =  response.data;
		            console.log($scope);
		        }, function errorCallback(response) {
		            console.log('Error: getReferenceTypes',response);
		            return [];
		        });
		    },
		    
		    updateReferenceName: function (reference, referenceTypes) {
		    	for(var i=0; i<referenceTypes.length; i++) {
		    		if(reference.referenceType === referenceTypes[i].referenceType) {
		    			reference.referenceTypeName = referenceTypes[i].referenceTypeName;
		    			break;
		    		}
		    	}
		    },

			performReferenceAction: function(action, reference, index) {
				
				
				console.log(action);
				this.getReferenceTypes();				
				
				switch(action) {
				case 'View':
					$scope.referenceDetail.selectReference(reference,index);
					break;
				case 'Add':
					if(!$scope.examCopy.examReferences) {
						$scope.examCopy.examReferences = [];
					} 
					var reference = {
							_flag: "c",
							examId:$scope.examCopy.examId
					}
					
					$scope.examCopy.examReferences.push(reference);
					var index = $scope.examCopy.examReferences.length;
					$scope.referenceDetail.selectReference(reference,index);
					$scope.exam_form.$setDirty();

					break;
				case 'Copy':
					var reference1 = angular.copy(reference);
					reference1.referenceId = undefined;
					reference1._flag="c"; // create
					reference1.examId = $scope.examCopy.examId;
					
					$scope.examCopy.examReferences.push(reference1);
					var index = $scope.examCopy.examReferences.length;
					$scope.referenceDetail.selectReference(reference1,index);
					$scope.exam_form.$setDirty();

					break;
				case 'CancelAdd':
					if($scope.examCopy.examReferences && $scope.examCopy.examReferences.length>0) {
						var lastIndex = $scope.examCopy.examReferences.length-1;
						for(var i=lastIndex; i>=0; i--) {
							if($scope.examCopy.examReferences[i]._flag == 'c' && $scope.examCopy.examReferences[i] == reference) {
								$scope.examCopy.examReferences.splice(i,1);
							}
						}
					}
					break;
				case 'Edit':
					$scope.referenceDetail.selectReference(reference,index);
					break;
				case 'Delete':
					$scope.examCopy.examReferences[index]._flag = "d"; // deleted
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

    
    $scope.referenceDetail = {
    		selectedReference: null, // selected reference to display details, key is property 'referenceNumber'
    	    selectedIndex: 0,
    	    isHideAboveRows: true,
    	    
    	    init: function() {
    		    $scope.$on(APP_CONSTANTS.EVENTS.closeExamDetail, function() {
    		    	$scope.referenceDetail.selectedReference = null; // close exam detail
    		    })
    	    },
    	    
    	    isRowHidden: function (reference, index) {
    	    	if(reference._flag == 'd') {
    	    		return true;
    	    	}
    			return false;
    	    },

    	    isDetailRow: function (reference, index) {
    	    	return ($scope.reference && this.selectedReference && this.selectedReference.referenceId == reference.referenceId);
			},
			
			hideAboveRows: function (flag) {
				this.isHideAboveRows = flag;
			},    


		close: function() {
	    		$scope.referenceDetail.selectedReference = null; // close exam detail
		},
		
		selectReference: function(reference,index) {
				var me = this;

	  		    if($scope.referenceDetail.selectedReference && $scope.referenceDetail.selectedReference.referenceId == reference.referenceId) {
	  	    		$scope.referenceDetail.selectedReference = null; // close exam detail
	  	    		return;
	  	    	} 

	  	    	this.selectedReference = reference; // show detail for this reference
	  			this.selectedIndex = index; // the index in the loop
	  			//this.expandColumn = "EXAM";
	  			this.hideAboveRows(true);

	  	    	$scope.referenceDetail.selectedReference = reference; // show detail for this exam
	  	    	$scope.reference = reference;
	  	    	
  				if($scope.exam._editMode_ == 'EDIT' || $scope.exam._editMode_ == 'CREATE') {
  	  				$scope.referenceAction.setupEditForm();
	  				var modalInstance = $uibModal.open({
						templateUrl: './examlog/popup/reference_popup.html',
						controller: 'referenceViewController',
						scope: $scope,
						backdrop: "static",
						appendTo: angular.element("#exam_references_edit_div")
						});	  	
  	  				}

  				if($scope.exam._editMode_ == 'VIEW') {
	  				var modalInstance = $uibModal.open({
						templateUrl: './examlog/popup/reference_popup.html',
						controller: 'referenceViewController',
						scope: $scope,
						backdrop: "static"
						});	  	
	  				}
	  					
	              
	  	    	
	  	    	// fetch reference details if existing record
				//if($scope.referenceDetail.selectedReference.referenceId != undefined) {
				if(!$scope.referenceDetail.selectedReference._flag || $scope.referenceDetail.selectedReference._flag in ["k","s"]) {
		  	        var url = "exams/references/" + $scope.referenceDetail.selectedReference.referenceId;
		  	        $http({
		  	      	  method: 'POST',
		  	      	  url: url
		  	      	}).then(function successCallback(response) {
		  	  				console.log(response);
		  	  				$scope.examCopy.examReferences[index] = response.data;
		  	  				$scope.examCopy.examReferences[index]._flag = "e"; // detail loaded
		  	  				
		  	  				//$scope.reference = response.data;
		  	  				$scope.reference = $scope.examCopy.examReferences[index];
		  	  				
		  	  				if($scope.exam._editMode_ == 'EDIT') {
		  	  				  $scope.examCopy.examReferences[index]._flag = "u";
		  	  				  $scope.referenceAction.setupEditForm();
		  	  				}
		  	      	  }, function errorCallback(response) {
		  	  				console.log(response);
		  	      	  });
				}
	  	    	
			}

	
    } // end of referenceDetail object
    
    
    // call init()
    $scope.init();

}]);

console.log ("Done loading exam_referenceTable.js");

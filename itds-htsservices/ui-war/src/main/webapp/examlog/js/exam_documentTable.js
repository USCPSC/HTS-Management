/**
 * exam documentTable Controller. Control for the document tab: the list of documents
 */
console.log ("loading exam_documentTable.js");

app.controller('documentViewController', ['$scope', '$http', '$route','$uibModal','$uibModalInstance', 'navigationService', 'Session', 'utilService','workflowService', 'APP_CONSTANTS', function($scope,$http, $route, $uibModal,$uibModalInstance, navigationService, Session, utilService,workflowService, APP_CONSTANTS) {
	//console.log($scope);
	// this the the root controller for document popup
	//alert("*********** Entering documentViewController()");

	$scope.ok = function () {
/*		if($scope.document_edit_form.$dirty) {
			$scope.exam_form.$setDirty();
		}
*/
	    $uibModalInstance.close();
	    $scope.documentDetail.close();
	  };

	  $scope.cancel = function () {
	    $uibModalInstance.dismiss('cancel');
	    $scope.documentDetail.close();
	  };
	  
}]);

app.controller('documentTableController', ['$scope', '$http', '$route','$uibModal', 'navigationService', 'examlogService', 'Session', 'utilService','workflowService', 'APP_CONSTANTS', function($scope,$http, $route, $uibModal, navigationService, examlogService, Session, utilService,workflowService, APP_CONSTANTS) {
	
	/**
	 * initialize the controller: invoke this at end of this controller definition
	 */
	$scope.init = function() {
		console.log("build documentTableController");
	}

	$scope.documentAction = {
			documentIdSeq: 1,
			
			setupEditForm: function() {
				//$scope.documentCopy = $scope.document;
				
				console.log($scope);
	    	},

		    getDocumentTypes: function() {
		        $http({
		            method: 'GET',
		            url: 'lookup/documentTypes',
		            params: {
		                currentUser: Session.userId,
		                password: Session.password
		            }
		        }).then(function successCallback(response) {
		            console.log('getDocumentTypes',response);
		            $scope.documentTypes =  response.data;
		            console.log($scope);
		        }, function errorCallback(response) {
		            console.log('Error: getDocumentTypes',response);
		            return [];
		        });
		    },
		    
		    updateDocumentName: function (document, documentTypes) {
		    	for(var i=0; i<documentTypes.length; i++) {
		    		if(document.documentType === documentTypes[i].documentType) {
		    			document.documentTypeName = documentTypes[i].documentTypeName;
		    			break;
		    		}
		    	}
		    },

			performDocumentAction: function(action, document, index) {
				
				
				console.log(action);
				this.getDocumentTypes();				
				
				switch(action) {
				case 'View':
					$scope.documentDetail.selectDocument(document,index);
					break;
				case 'Add':
					if(!$scope.examCopy.examDocuments) {
						$scope.examCopy.examDocuments = [];
					} 
					var document = {
							_flag: "c",
							examId:$scope.examCopy.examId
					}
					
					$scope.examCopy.examDocuments.push(document);
					var index = $scope.examCopy.examDocuments.length;
					$scope.documentDetail.selectDocument(document,index);
					$scope.exam_form.$setDirty();

					break;
				case 'Copy':
					var document1 = angular.copy(document);
					document1.documentId = undefined;
					document1._flag="c"; // create
					document1.examId = $scope.examCopy.examId;
					
					$scope.examCopy.examDocuments.push(document1);
					var index = $scope.examCopy.examDocuments.length;
					$scope.documentDetail.selectDocument(document1,index);
					$scope.exam_form.$setDirty();

					break;
				case 'CancelAdd':
					if($scope.examCopy.examDocuments && $scope.examCopy.examDocuments.length>0) {
						var lastIndex = $scope.examCopy.examDocuments.length-1;
						for(var i=lastIndex; i>=0; i--) {
							if($scope.examCopy.examDocuments[i]._flag == 'c' && $scope.examCopy.examDocuments[i] == document) {
								$scope.examCopy.examDocuments.splice(i,1);
							}
						}
					}
					break;
				case 'Edit':
					$scope.documentDetail.selectDocument(document,index);
					break;
				case 'Delete':
					$scope.examCopy.examDocuments[index]._flag = "d"; // deleted
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

    
    $scope.documentDetail = {
    		selectedDocument: null, // selected document to display details, key is property 'documentNumber'
    	    selectedIndex: 0,
    	    isHideAboveRows: true,
    	    
    	    init: function() {
    		    $scope.$on(APP_CONSTANTS.EVENTS.closeExamDetail, function() {
    		    	$scope.documentDetail.selectedDocument = null; // close exam detail
    		    })
    	    },
    	    
    	    isRowHidden: function (document, index) {
    	    	if(document._flag == 'd') {
    	    		return true;
    	    	}
    			return false;
    	    },

    	    isDetailRow: function (document, index) {
    	    	return ($scope.document && this.selectedDocument && this.selectedDocument.documentId == document.documentId);
			},
			
			hideAboveRows: function (flag) {
				this.isHideAboveRows = flag;
			},    


		close: function() {
	    		$scope.documentDetail.selectedDocument = null; // close exam detail
		},
		
		selectDocument: function(document,index) {
				var me = this;

	  		    if($scope.documentDetail.selectedDocument && $scope.documentDetail.selectedDocument.documentId == document.documentId) {
	  	    		$scope.documentDetail.selectedDocument = null; // close exam detail
	  	    		return;
	  	    	} 

	  	    	this.selectedDocument = document; // show detail for this document
	  			this.selectedIndex = index; // the index in the loop
	  			//this.expandColumn = "EXAM";
	  			this.hideAboveRows(true);

	  	    	$scope.documentDetail.selectedDocument = document; // show detail for this exam
	  	    	$scope.document = document;
	  	    	
  				if($scope.exam._editMode_ == 'EDIT' || $scope.exam._editMode_ == 'CREATE') {
  	  				$scope.documentAction.setupEditForm();
	  				var modalInstance = $uibModal.open({
						templateUrl: './examlog/popup/document_popup.html',
						controller: 'documentViewController',
						scope: $scope,
						backdrop: "static",
						appendTo: angular.element("#exam_documents_edit_div")
						});	  	
  	  				}

  				if($scope.exam._editMode_ == 'VIEW') {
	  				var modalInstance = $uibModal.open({
						templateUrl: './examlog/popup/document_popup.html',
						controller: 'documentViewController',
						scope: $scope,
						backdrop: "static"
						});	  	
	  				}
	  					
	              
	  	    	
	  	    	// fetch document details if existing record
				//if($scope.documentDetail.selectedDocument.documentId != undefined) {
				if(!$scope.documentDetail.selectedDocument._flag || $scope.documentDetail.selectedDocument._flag in ["k","s"]) {
		  	        var url = "exams/documents/" + $scope.documentDetail.selectedDocument.documentId;
		  	        $http({
		  	      	  method: 'POST',
		  	      	  url: url
		  	      	}).then(function successCallback(response) {
		  	  				console.log(response);
		  	  				$scope.examCopy.examDocuments[index] = response.data;
		  	  				$scope.examCopy.examDocuments[index]._flag = "e"; // detail loaded
		  	  				
		  	  				//$scope.document = response.data;
		  	  				$scope.document = $scope.examCopy.examDocuments[index];
		  	  				
		  	  				if($scope.exam._editMode_ == 'EDIT') {
		  	  				  $scope.examCopy.examDocuments[index]._flag = "u";
		  	  				  $scope.documentAction.setupEditForm();
		  	  				}
		  	      	  }, function errorCallback(response) {
		  	  				console.log(response);
		  	      	  });
				}
	  	    	
			}

	
    } // end of documentDetail object
    
    $scope.uploadFile = function() {
    	examlogService.uploadFile(document.forms.fileUploadForm.file.files[0]).then(function successCallback(data) {
    		$scope.document.documentName = document.forms.fileUploadForm.file.files[0].name;
    		$scope.document.documentNameOnServer = data;
    		console.log($scope.document);
      	  }, function errorCallback(response) {
  				console.log(response);
  				examCopy.examErrors = response.data;
  				examCopy.examErrors.message = "Please correct following error(s):";
      	  });
    }
    
    // call init()
    $scope.init();

}]);

console.log ("Done loading exam_documentTable.js");

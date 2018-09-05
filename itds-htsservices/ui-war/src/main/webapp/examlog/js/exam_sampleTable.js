/**
 * exam sampleTable Controller. Control for the sample tab: the list of products/samples
 */
console.log ("loading exam_sampleTable.js");

app.controller('sampleViewController', ['$scope', '$http', '$uibModalInstance', 'Session', 'APP_CONSTANTS', 'examlogService', 'utilService', function($scope, $http, $uibModalInstance, Session, APP_CONSTANTS, examlogService, utilService) {
	$scope.Math = window.Math;
	
	$scope.ok = function () {
//		console.log("********* exam_sampleTable.js::sampleViewController:$scope.ok()");		
	    $uibModalInstance.close();
	    $scope.sampleDetail.close();
	},

	$scope.cancel = function () {
//		console.log("********* exam_sampleTable.js::sampleViewController:$scope.cancel()");
		$uibModalInstance.dismiss('cancel');
	    $scope.sampleDetail.close();
	},
	  
	$scope.showReadings = function (show) {
//		console.log("********* exam_sampleTable.js::sampleViewController:$scope.showReadings(show) show="+show+"<<<");
		$scope.showContents = show?"READING":"SAMPLE";
	},
	
	$scope.perform = function( action, readingItem, index ) {
//		console.log("********* exam_sampleTable.js::sampleViewController:$scope.perform(action) action="+action+"<<<");
		
		switch(action) {
		case "DeleteReadingItem": // "Delete":
			if($scope.$parent.sample.readingItems[index] == readingItem) {
				$scope.$parent.sample.readingItems[index]._flag = 'd';
			}
			break;
		case "AddReadingItem": // "AddNewReadingItem":
//			console.log("********* exam_sampleTable.js::sampleViewController:$scope.perform() AddReadingItem");
			var readingItem =
			{
				id: null,
				productSampleId: $scope.$parent.sample.productSampleId,
				sampleNumber: $scope.$parent.sample.sampleNumber,
				readingNo: '',
				readingTime: new Date().getTime(),
				location: '',
				note: '',
				createTimestamp: new Date().getTime(),
				createUserId: Session.userId,
				lastUpdateTimestamp: new Date().getTime(),
				lastupdateUserId: Session.userId,
				readings: [{
					id: null,
					readingType: 'XRF_lead',
					readingValue: '0',
					indicator: '',
					readingTypeDescription: 'Highest XRF Reading - Lead',
					createTimestamp: new Date().getTime(),
					createUserId: Session.userId,
					lastUpdateTimestamp: new Date().getTime(),
					lastupdateUserId: Session.userId
				},{
					id: null,
					readingType: 'XRF_cadmium',
					readingValue: '0',
					indicator: '',
					readingTypeDescription: 'Highest XRF Reading - Cadmium',
					createTimestamp: new Date().getTime(),
					createUserId: Session.userId,
					lastUpdateTimestamp: new Date().getTime(),
					lastupdateUserId: Session.userId
				},{
					id: null,
					readingType: 'FTIR',
					readingValue: '0',
					indicator: '',
					readingTypeDescription: 'FTIR (Phthalates)',
					createTimestamp: new Date().getTime(),
					createUserId: Session.userId,
					lastUpdateTimestamp: new Date().getTime(),
					lastupdateUserId: Session.userId
				},{
					id: null,
					readingType: 'XRF_other',
					readingValue: '0',
					indicator: '',
					readingTypeDescription: 'Highest XRF Reading - Other',
					createTimestamp: new Date().getTime(),
					createUserId: Session.userId,
					lastUpdateTimestamp: new Date().getTime(),
					lastupdateUserId: Session.userId
				}]
			};
			if(!$scope.$parent.sample.readingItems) {
				$scope.$parent.sample.readingItems = [];
			}
			$scope.$parent.sample.readingItems.push( readingItem );
			
			if($scope.$parent.sample._flag!==undefined) {
				$scope.$parent.sample._flag = ($scope.$parent.sample._flag.length == 1) 
					? ($scope.$parent.sample._flag + 'c') : $scope.$parent.sample._flag;
			}
			else {
				$scope.$parent.sample._flag = 'uc';
			}
			break;
		case "SaveCloseReadingItem":
//			console.log("********* exam_sampleTable.js::sampleViewController:$scope.perform() SaveCloseReadingItem");
			//$scope.$parent.sample.reasons = $scope.$parent.sample.reasonTypesPicked;  ??? why ????
			
			if($scope.$parent.sample.readingItems) {
				for(var i=$scope.$parent.sample.readingItems.length-1; i>=0; i--) {
					if($scope.$parent.sample.readingItems[i].readings) {
						for(var j=$scope.$parent.sample.readingItems[i].readings.length-1; j>=0; j--) {
							if(!$scope.$parent.sample.readingItems[i].readings[j].readingValue) {
								$scope.$parent.sample.readingItems[i].readings.splice(j,1);
							}
						}
						if($scope.$parent.sample.readingItems[i].readings.length==0) {
							$scope.$parent.sample.readingItems[i]._flag = 'd';
						}
					}
				}
				if($scope.$parent.sample.readingItems) {
					examlogService.noReadingItem = ($scope.$parent.sample.readingItems.length) ? false : true;
				}
			}
			else {
				examlogService.noReadingItem = true;
			}
			
			if($scope.$parent.sample._flag!==undefined) {
				$scope.$parent.sample._flag = ($scope.$parent.sample._flag.length == 1) 
					? ($scope.$parent.sample._flag + 'c') : $scope.$parent.sample._flag;
			}
			else {
				$scope.$parent.sample._flag = 'uc';
			}
			// all the way to exam level to save
			var examDetailScope = utilService.getControllerScope('examDetailController');
			examDetailScope.$emit("saveSampleEdit", $scope.$parent); // examSamples
			break;
//		case "CancelAddReadingItem": // "Cancel":
//			console.log("********* exam_sampleTable.js::sampleViewController:$scope.perform() CancelAddReadingItem");
//			
//			if($scope.$parent.sample.readingItems && $scope.$parent.sample.readingItems.length>0) {
//				var lastIndex = $scope.$parent.sample.readingItems.length-1;
//				for(var i=lastIndex; i>=0; i--) {
//					if($scope.$parent.sample._flag.charAt(1) == 'c' && $scope.$parent.sample.readingItems[i] == readingItem) {
//						$scope.examCopy.examSamples.splice(i,1);
//					}
//				}
//			}
//			break;
//		case "Cancel":
//			console.log("********* exam_sampleTable.js::sampleViewController:$scope.perform() Cancel");
//			$uibModalInstance.dismiss('cancel');
//		    $scope.sampleDetail.close();
//			break;
		default:
			console("default");
		}
	},
	  
	$scope.isEnabledReading = function(reading) {
		
		switch (reading.readingType) {
		case "XRF_lead": 
		case "XRF_cadmium": 
		case "XRF_other": 
		case "FTIR": 
		case "PB":
		case "CD":
			return true;
		default:
			return false;
		}
	}
	  
}]);

app.controller('sampleTableController', ['$scope', '$http', '$uibModal',  'examlogService', 'Session',  'APP_CONSTANTS', 'utilService', function($scope, $http, $uibModal, examlogService, Session, APP_CONSTANTS, utilService) {
	
	/**
	 * initialize the controller: invoke this at end of this controller definition
	 */
	$scope.init = function() {
		console.log("build sampleTableController");
	}
	
	$scope.Math = window.Math;
	
	$scope.roundReadingValue = function(reading){
		if( reading && reading.readingValue ){
			reading.readingValue = $scope.Math.round(reading.readingValue);
		}
	}

	// filter the list of sampleReasons from screening reason list
	$scope.filterSampleReasons = function(list2) {
		var list3=[];
		if(!list2) {
			return list3;
		}
		
    	list2.forEach(function(reason) {
    			if('S' == reason.reasonType) {
    				list3.push( reason );
    			}
    	});
    	return list3;
	}  // List for sampling

	// prep for reasons for screening
	$scope.prepareReasons = function(sample) {
		//sample.reasonsForScreening = sample.reasons;  // dto
//		if(sample.reasonTypesPicked) {
//			sample.reasons = sample.reasonTypesPicked; 
//		}

		sample.reasonTypesAvailable = examlogService.prepReasonTypesAvailable( sample );  // excluded sample.reasons as 1st list
		// sample.reasons: as 2nd list (list of dto)
		sample.reasonsForSampling = $scope.filterSampleReasons(sample.reasons);  // populate list of reason for sampling as 3rd list
	
		
//		sample.reasonTypesPicked = angular.copy(sample.reasons);
//		sample.sampleReasonTypesPicked = sample.reasonsForSampling;

		if(sample.reasons !== undefined) {
			sample.reasons.sort( utilService.sortArrayBy( 'reasonName', false, function(a){return a.toUpperCase()}) );
		}
		else {
			sample.reasons = [];
		}
		
		// reasons for sampling
		if(sample.reasonsForSampling !== undefined) {
			sample.reasonsForSampling.sort( utilService.sortArrayBy( 'reasonName', false, function(a){return a.toUpperCase()}) );
		}
		else {
			sample.reasonsForSampling = [];
		}
}
	
	
	$scope.productSampleAction = {
			sampleIdSeq: 1,
			
			deepCopy: function (oldValue) { 
		    	  var newValue;
		    	  strValue = JSON.stringify(oldValue);
		    	  return newValue = JSON.parse(strValue);
		    },

		    prepopulateSampleNumber: function(sampleCopy) {
		    	//	<div class="form-group"  ng-class="{'required': sampleCopy.dispositionCode != null && sampleCopy.dispositionCode != 'RELEASE' && sampleCopy.dispositionCode != 'OTHER'}">
		    	if(sampleCopy.dispositionCode != null && sampleCopy.dispositionCode != 'RELEASE' && sampleCopy.dispositionCode != 'OTHER') {
			    	if(!sampleCopy.sampleNumber) {
			    		  sampleCopy.sampleNumber = (utilService.getCurrentFiscalYear()-2000)+"-304-";
			    	}
		    	}
		    },
		    
		    // $scope.productSampleAction
			setupEditForm: function() {
				//var sampleCopy = this.deepCopy($scope.sample);
				//$scope.sampleCopy = sampleCopy;
				$scope.sampleCopy = $scope.sample;  // not copy ???
				
				console.log($scope);
	    	},


		    // $scope.productSampleAction
			performProductSampleAction: function(action, sample, index) {
				console.log("/examlog/js/exam_sampleTable.js::sampleTableController::productSampleAction::performProductSampleAction() action="+action+"<<< sample="+sample+"<<< index="+index+"<<<");
				
				examlogService.currentSampleIndex = (index === undefined) ? $scope.sampleDetail.selectedIndex : index;
				if(sample === undefined || !sample) {
					if($scope.sample === undefined) {
						$scope.sample = {};
					}
					sample = $scope.sample;
				}

				
				// prep for reasons for screening

	  				
				// reasons for screening
				//$scope.prepareReasons(sample);
				
				if(sample.readingItems) {
					for(var i=sample.readingItems.length-1; i>=0; i--) {
						if(sample.readingItems[i].readings) {
							for(var j=sample.readingItems[i].readings.length-1; j>=0; j--) {
								if(!sample.readingItems[i].readings[j].readingValue) {
									sample.readingItems[i].readings.splice(j,1);
								}
							}
							if(sample.readingItems[i].readings.length==0) {
								sample.readingItems[i]._flag = 'd';
							}
						}
					}
					if(sample.readingItems) {
						examlogService.noReadingItem = (sample.readingItems.length) ? false : true;
					}
				}
				else {
					examlogService.noReadingItem = true;
				}
				
				//$scope.productSampleAction.performProductSampleAction
				switch(action) {
				case 'View':
					$scope.sampleDetail.selectSample(sample,index);
					break;
				case 'ViewIFS':
					var ifsViewSampleUrl = atob(examlogService.getParameters().ifs_view_sample_url_base64) + "?SampleNo="+sample.sampleNumber;
					window.open(ifsViewSampleUrl,"IFS");
					break;
				case 'AddIFS':
					examlogService.prepareIfsSample(sample);
					break;
				case 'Add':
					if(!$scope.examCopy.examSamples) {
						$scope.examCopy.examSamples = [];
					} 
					var sample = {
						_flag: "c",
						examId:$scope.examCopy.examId
						// prep for reasons for screening
						,reasonTypesAvailable: $scope.reasonTypes  	// List 1 options
						,reasonTypesPicked: []                     	// List 2 options
						,sampleReasonTypesPicked: []				// List 3 options
						,reasonTypesAvailableSelectedOptions: []	// List 1 user selection
						,reasonTypesPickedSelectedOptions: []		// List 2 user selection
						,sampleReasonTypesPickedSelectedOptions: [] // List 3 user selection
					}
					$scope.examCopy.examSamples.push(sample);
					var index = $scope.examCopy.examSamples.length;
					$scope.sampleDetail.selectSample(sample,index);
					$scope.exam_form.$setDirty();
					break;
				case 'Copy':
					var sample1 = this.deepCopy(sample);
					sample1.productSampleId = undefined;
					sample1._flag="c"; // create
					sample1.examId = $scope.examCopy.examId;
					
					// prep for reasons for screening
					console.log("reasonTypes", examlogService.reasonTypes);
					//$scope.prepareReasons(sample1);
					
					sample1.readingItems = null;
					$scope.examCopy.examSamples.push(sample1);
					var index = $scope.examCopy.examSamples.length;
					$scope.sampleDetail.selectSample(sample1,index);
					break;
				case 'Edit':
					//$scope.productSampleAction.performProductSampleAction
					
					$scope.sampleDetail.selectSample(sample,index);
					break;
				case 'SaveEdit':
					//$scope.productSampleAction.performProductSampleAction
					console.log("Save Edit");
					// reasons for screening
					// ???? sample.reasons = sample.reasonTypesPicked;  // dto
					$scope.sampleDetail.selectSample(sample,index);
					// all the way to exam level to save
					var examDetailScope = utilService.getControllerScope('examDetailController');
					examDetailScope.$emit("saveSampleEdit", $scope); // examSamples
					break;
//				case 'CancelClose':
//					console.log("Cancel Close");
//					// reasons for screening
//					$scope.sampleDetail.selectSample(sample,index);
//					// all the way to exam level to cancel
//					var examDetailScope = utilService.getControllerScope('examDetailController');
//					examDetailScope.$emit("cancelSampleEdit", $scope); // examSamples
//					break;
				case 'CancelAdd':
					if($scope.examCopy.examSamples && $scope.examCopy.examSamples.length>0) {
						var lastIndex = $scope.examCopy.examSamples.length-1;
						for(var i=lastIndex; i>=0; i--) {
							if($scope.examCopy.examSamples[i]._flag == 'c' && $scope.examCopy.examSamples[i] == sample) {
								$scope.examCopy.examSamples.splice(i,1);
							}
						}
					}
					break;
				case 'Delete':
					$scope.examCopy.examSamples[index]._flag = "d"; // deleted
//					$scope.sampleDetail.selectSample(sample,index);
					$scope.exam_form.$setDirty();
					break;
				default:
					break;
				}
					
			},
			
		    // begin - get drop down lists		
		    getDispositionTypes: function() {
		        $http({
		            method: 'GET',
		            url: 'lookup/dispositionTypes',
		            params: {
		                currentUser: Session.userId,
		                password: Session.password
		            }
		        }).then(function successCallback(response) {
		            console.log('getDispositionTypes',response);
		            $scope.dispositionTypes =  response.data;
		            console.log($scope);
		        }, function errorCallback(response) {
		            console.log('Error: getDispositionTypes',response);
		            return [];
		        });
		    },

		    getStatutes: function() {
		        $http({
		            method: 'GET',
		            url: 'lookup/statutes',
		            params: {
		                currentUser: Session.userId,
		                password: Session.password
		            }
		        }).then(function successCallback(response) {
		            console.log('getStatutes',response);
		            $scope.statutes =  response.data;
		            console.log($scope);
		        }, function errorCallback(response) {
		            console.log('Error: getStatutes',response);
		            return [];
		        });
		    },
	// end - get drop down lists

		    notifyExamCloseEvent: function () {
		    	$scope.$emit(APP_CONSTANTS.EVENTS.closeExamDetail);	    		
		    },
		    
		    createIfsSample: function(sample,exam,entry) {
		    	return {
		    		Sample_Number:	sample.sampleNumber,
		    		Collect_Date:	"9/2/2015",
		    		Product_Name:	"Imagination Generation Wooden Ambulance",
		    		Model:	sample.itemModel,
		    		Reason_Screening_Code:	"",
		    		Reason_Screening_Name:	"Small Parts (List)",
		    		Sample_Size:	sample.numberProductsScreened,
		    		Remarks:		"Wooden benches on the inside of ambulance fell out during drop test on 2nd drop. Bench completely fits into small parts cylinder.",
		  
		    		Entry_Number:	exam.entryNumber,
		    		Entry_Date:		exam.examDate,
		    		Port_of_Entry_Code:	exam.portCode,
		    		Port_of_Entry_Name:	"CHICAGO IL",
		    		Country_of_Origin:	"CN",
		    		Activity_Hours:	exam.activityTime,
		    		Travel_Hours:	exam.travelTime,
		    		Assignment_Number:	"",
		    		
		    		Importer_Name:	exam.importerName,
		    		Importer_Address: exam.importerAddressLine1,
		    		Importer_City: exam.importerAddressCity,
		    		Importer_State: exam.importerAddressState,
		    		Importer_Zip: exam.importerAddressZip,
		    		
		    		Foreign_Manufacturer_Name:	"GUANGDONG HONGTU TECHNOLOGY (HOLDING) CO",
		    		Foreign_Manufacturer_Address_Line1: "NO. 168 CENTURY RD",
		    		Foreign_Manufacturer_Address_Line2:"GAOYAO CITY GUANGDON",

		    		Importer_Broker:	"CARGO SERVICES INC",
		    		Importer_Broker_Address: "7640 MILES DR",
		    		Importer_Broker_City: "INDIANAPOLIS",
		    		Importer_Broker_State: "IN",
		    		Importer_Broker_Zip: "462313346",
		    			
		    		Product_Description:	sample.productDescription,
		    		Investigator_First_Name:	"Guadalupe",
		    		Investigator_Last_Name:	"Whyte"
		    	}
		    },
		    
		    createIfsTestSample: function() {
		    	return {
		    		Sample_Number:	"15-304-1647",
		    		Collect_Date:	"9/2/2015",
		    		Product_Name:	"Imagination Generation Wooden Ambulance",
		    		Model:	"L10099/TVEH-001",
		    		Reason_Screening_Code:	"",
		    		Reason_Screening_Name:	"Small Parts (List)",
		    		Sample_Size:	"1",
		    		Remarks:		"Wooden benches on the inside of ambulance fell out during drop test on 2nd drop. Bench completely fits into small parts cylinder.",
		  
		    		Entry_Number:	"BXW00176005",
		    		Entry_Date:		"8/17/2015",
		    		Port_of_Entry_Code:	"3901",
		    		Port_of_Entry_Name:	"CHICAGO IL",
		    		Country_of_Origin:	"CN",
		    		Activity_Hours:	"0.15",
		    		Travel_Hours:	"0.15",
		    		Assignment_Number:	"",
		    		
		    		Importer_Name:	"DOLLAR STORE",
		    		Importer_Address: "123 CHURCH ST",
		    		Importer_City: "RICHMOND",
		    		Importer_State: "KY",
		    		Importer_Zip:"404751469",
		    		
		    		Foreign_Manufacturer_Name:	"GUANGDONG HONGTU TECHNOLOGY (HOLDING) CO",
		    		Foreign_Manufacturer_Address_Line1: "NO. 168 CENTURY RD",
		    		Foreign_Manufacturer_Address_Line2:"GAOYAO CITY GUANGDON",

		    		Importer_Broker:	"CARGO SERVICES INC",
		    		Importer_Broker_Address: "7640 MILES DR",
		    		Importer_Broker_City: "INDIANAPOLIS",
		    		Importer_Broker_State: "IN",
		    		Importer_Broker_Zip: "462313346",
		    			
		    		Product_Description:	"can be a very long paragraph",
		    		Investigator_First_Name:	"Guadalupe",
		    		Investigator_Last_Name:	"Whyte"
		    	}
		    }
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

    
    $scope.sampleDetail = {
    	    //expandColumn: "", // ENTRY_LINE or EXAM
    		selectedSample: null, // selected product/sample to display details, key is property 'sampleNumber'
    	    //detailTabSelected: "Details",
    	    selectedIndex: 0,
    	    isHideAboveRows: true,
    	   //workflowUrl:"",
    	    
    	    init: function() {
    		    $scope.$on(APP_CONSTANTS.EVENTS.closeExamDetail, function() {
    		    	$scope.sampleDetail.selectedSample = null; // close exam detail
    		    })
    	    },
    	    
    	    isRowHidden: function (sample, index) {
//    			if($scope.sample && $scope.sampleDetail.selectedSample) {
//    				if (index < this.selectedIndex) {
//    					return this.isHideAboveRows;
//    				} 
//    			}
    	    	if(sample._flag == 'd') {
    	    		return true;
    	    	}
    			return false;
    	    },

    	    isDetailRow: function (sample, index) {
    	    	return ($scope.sample && this.selectedSample && this.selectedSample.productSampleId == sample.productSampleId);
			},
			
			hideAboveRows: function (flag) {
				this.isHideAboveRows = flag;
			},    


		close: function() {
	    		$scope.sampleDetail.selectedSample = null; // close exam detail
		},
		
		// $scope.sampleDetail
		selectSample: function(sample,index) {
				var me = this;

	  		    if($scope.sampleDetail.selectedSample && $scope.sampleDetail.selectedSample.productSampleId == sample.productSampleId) {
	  	    		$scope.sampleDetail.selectedSample = null; // close exam detail
	  	    		return;
	  	    	} 

	  	    	this.selectedSample = sample; // show detail for this sample
	  			this.selectedIndex = index; // the index in the loop
	  			//this.expandColumn = "EXAM";
	  			this.hideAboveRows(true);

	  	    	$scope.sampleDetail.selectedSample = sample; // show detail for this sample  ?? redundant???
	  	    	$scope.sample = sample;  // scope vs here ???
				// prep for reasons for screening
				$scope.prepareReasons($scope.sample);
	  	    	
  				if($scope.exam._editMode_ == 'EDIT' || $scope.exam._editMode_ == 'CREATE') {
  	  				$scope.productSampleAction.setupEditForm();
	  				var modalInstance = $uibModal.open({
						templateUrl: './examlog/popup/sample_popup.html',
						controller: 'sampleViewController',
						scope: $scope,
						backdrop: "static",
						appendTo: angular.element("#exam_product_samples_edit_div")
						});	  	
  	  				}

  				if($scope.exam._editMode_ == 'VIEW') {
	  				var modalInstance = $uibModal.open({
						templateUrl: './examlog/popup/sample_popup.html',
						controller: 'sampleViewController',
						scope: $scope,
						backdrop: "static"
						});	  	
	  				}
	  					
	              
	  	    	
	  	    	// fetch sample details if existing record
				//if($scope.sampleDetail.selectedSample.productSampleId != undefined) {
				if(!$scope.sampleDetail.selectedSample._flag || $scope.sampleDetail.selectedSample._flag in ["k","s"]) {
		  	        var url = "exams/samples/" + $scope.sampleDetail.selectedSample.productSampleId;
		  	        $http({
		  	      	  method: 'POST',
		  	      	  url: url
		  	      	}).then(function successCallback(response) {
		  	      		console.log(response);
		  	  			if($scope.exam._editMode_ != 'VIEW') {
		  	  				$scope.examCopy.examSamples[index] = response.data;
		  	  				$scope.examCopy.examSamples[index]._flag = "e"; // detail loaded
		  	  				
		  	  				$scope.sample = $scope.examCopy.examSamples[index];
		  	  				
		  					$scope.prepareReasons($scope.sample);

		  	  				if($scope.exam._editMode_ == 'EDIT') {
			  	  				$scope.examCopy.examSamples[index]._flag = "u";
			  	  				$scope.productSampleAction.setupEditForm();
		  	  				}
		  	  			} else {
		  	  				$scope.sample = response.data;
							// prep for reasons for screening
		  					$scope.prepareReasons($scope.sample);
		  	  			}
		  	      	}, function errorCallback(response) {
		  	  				console.log(response);
		  	      	});
				}
	  	    	
		}

	
    } // end of sampleDetail object
    

    $scope.orderReasonList = function(reasons) {
    	if(reasons) {
        	reasons.sort( utilService.sortArrayBy( 'reasonName', false, function(a){return a.toUpperCase()}) );
    	}
    }

    
    // move reasons for screening between left and right
    $scope.moveRightScreening = function(selected, from, to){
    	console.log('move ', selected, ' from ', from, ' to ', to);
    	
    	selected.forEach(function(selectedId) {
    		console.log("selectedId="+selectedId);
    		$.each(from, function(i, opt) {
    			if(selectedId === opt.reasonCode) {
    				to.push( from[i] );
    				from.splice(i, 1);
    				return false;
    			}
    		});

    	});
    	$scope.orderReasonList(to);
    }
    
    // between all reasons and screening
    $scope.moveLeftScreening = function(selected, samplePick, from, to){
    	console.log('move ', selected, ' from ', from, ' to ', to);
    	
    	selected.forEach(function(selectedId) {
    		console.log("selectedId="+selectedId);
    		$.each(from, function(i, opt) {
    			if(selectedId === opt.reasonCode) {
    				var reasonType = from[i].reasonType;
    				from[i].reasonType = null;
    				
    				to.push( from[i] );
    				from.splice(i, 1);
    				// remove from sample
    				if(reasonType == 'S') {
    		    		$.each(samplePick, function(j, optSample) {
    		    			if(selectedId === optSample.reasonCode) {
    		    				samplePick.splice(j, 1);
    		    				return false;
    		    			}
    		    		});
    				}
    				return false;
    			}
    		});

    	});
    	$scope.orderReasonList(to);
    }
    
    $scope.moveRightScreeningAll = function(from, to) {
    	console.log('move all from ', from, ' to ', to);
        angular.forEach(from, function(item) {
            to.push(item);
        });
        from.length = 0;
    	$scope.orderReasonList(to);
    }
    
    $scope.moveLeftScreeningAll = function(samplePick,from, to) {
    	console.log('move all from ', from, ' to ', to);

    	samplePick.splice(0,samplePick.length);

    	from.forEach(function(selectedId) {
			selectedId.reasonType=null;
  		});

    	
    	angular.forEach(from, function(item) {
            to.push(item);
        });
        from.length = 0;
    	$scope.orderReasonList(to);
    }
    
    // between screening and sampling
    $scope.moveLeftSample = function(selected, from, to){
    	console.log('move ', selected, ' from ', from, ' to ', to);
    	
    	selected.forEach(function(selectedId) {
    		console.log("selectedId="+selectedId);
    		$.each(from, function(i, opt) {
    			if(selectedId === opt.reasonCode && from[i].reasonType == 'S') {
    				from[i].reasonType=null;
    				//to.push( from[i] );
    				from.splice(i, 1);
    				return false;
    			}
    		});

    	});
    	$scope.orderReasonList(to);
    }
    
    $scope.moveRightSample = function(selected, from, to){
    	console.log('move ', selected, ' from ', from, ' to ', to);
    	
    	selected.forEach(function(selectedId) {
    		console.log("selectedId="+selectedId);
    		$.each(from, function(i, opt) {
    			if(selectedId === opt.reasonCode && from[i].reasonType != 'S') {
    				from[i].reasonType='S';
    				to.push( from[i] );
    				//from.splice(i, 1);
    				return false;
    			}
    		});

    	});
    	$scope.orderReasonList(to);
    }
    
    $scope.moveLeftSampleAll = function(selected, from, to){
    	console.log('move ', selected, ' from ', from, ' to ', to);
    	
    	selected.forEach(function(selectedId) {
    			selectedId.reasonType=null;
      		});
    	from.splice(0,from.length);
    	$scope.orderReasonList(to);
    }
    
    $scope.moveRightSampleAll = function(selected, from, to){
    	console.log('move ', selected, ' from ', from, ' to ', to);
    	
    	selected.forEach(function(selectedId) {
    		console.log("selectedId="+selectedId);
    		$.each(from, function(i, opt) {
    			if(selectedId.reasonCode === opt.reasonCode && from[i].reasonType != 'S') {
    				from[i].reasonType='S';
    				to.push( from[i] );
    			}
    		});

    	});
    	$scope.orderReasonList(to);
    }

    // call init()
    $scope.init();

}]);

console.log ("Done loading exam_sampleTable.js");
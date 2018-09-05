
console.log ("loading examlog.js");

app.controller('entrySearchController', ['$scope', '$http', '$route', '$uibModal','$uibModalInstance',
                                         'navigationService', 'spinnerService', 'Session', 'utilService','workflowService', 'APP_CONSTANTS', 
                                         'examlogService',
                                         function($scope,$http, $route, $uibModal,$uibModalInstance, 
                                        		 navigationService, spinnerService, Session, utilService,workflowService, APP_CONSTANTS,examlogService) {
	$scope.ok = function () {
	    $uibModalInstance.close();
	    //$scope.contactDetail.close();
	  };

	$scope.cancel = function () {
	    $uibModalInstance.dismiss('cancel');
	    //$scope.contactDetail.close();
	  };
	  
	  // Create Exam for existing Entry Number
		$scope.prePopulateExam = function (entryNumber) {
		   	if(entryNumber == undefined) {
		   		entryNumber = "";
		   	}
  			$scope.entryNumber_validation_error = null;
  			$scope.entryNumber2_validation_error = null;
  			if(entryNumber) { 
  				entryNumber = entryNumber.toUpperCase(); 
  			}
  			examlogService.validateExamEntryNumber(entryNumber,'0', true).then(function successCallback(data) {
  				utilService.getControllerScope('examLogController').detail.createExam(entryNumber);
  				$uibModalInstance.close();
  			}, function errorCallback(error) {
  				$scope.entryNumber_validation_error = error;
	      	})
	}	
	
	
	// Create Exam for Unkown "Entry Number"
	$scope.newExam = function (entryNumber) {
		   	if(entryNumber == undefined) {
		   		entryNumber = "";
		   	}
			$scope.entryNumber_validation_error = null;
  			$scope.entryNumber2_validation_error = null;
  			if(entryNumber) { 
  				entryNumber = entryNumber.toUpperCase(); 
  			}
  			examlogService.validateUnkownExamEntryNumber(entryNumber).then(function successCallback(data) {
  				utilService.getControllerScope('examLogController').detail.createExam(entryNumber);
  				$uibModalInstance.close();
  			}, function errorCallback(error) {
  				$scope.entryNumber2_validation_error = error;
	      	})
	}	  
	
	$scope.getFilteredEntryNumbers = function(filter) {
	        return $http({
	            method: 'GET',
	            url: 'lookup/filteredEntryNumbers',
	            params: {
	            	filter: filter,
	                currentUser: Session.userId,
	                password: Session.password
	            }
	        }).then(function successCallback(response) {
	            console.log('getFilteredEntryNumbers',response);
	            return response.data;
	        }, function errorCallback(response) {
	            console.log('Error: getFilteredEntryNumbers',response);
	            return [];
	        });
	    };	  
}]);

app.controller('examLogController', ['$scope', '$http', '$q', '$route', '$uibModal','$location',
                                     'navigationService', 'examlogService','paginationService','searchService',
                                     'APP_CONSTANTS','growl','utilService', 'spinnerService','Session','csvExportService',
                                     '$uibModal','$sce',
                                     function($scope,$http, $q, $route, $uibModal,$location,
                                    		 navigationService, examlogService, paginationService,searchService,
                                    		 APP_CONSTANTS,growl,utilService,spinnerService,Session,csvExportService,
                                    		 $uibModal,$sce) {
	console.log("*** Entering examLogController()");
//	alert("*********** Entering examLogController()");
//	$scope.$on("$destroy", function() {
//            alert("The scope for examLogController is being destroyed");
//    });	
	
	/**
	 * initialize the controller: invoke this at end of this controller definition
	 */
	$scope.init = function() {
		console.log("build examLogController");
		$scope.isChrome = navigator.userAgent.indexOf("Chrom")>0;

		navigationService.setCurrentTab("examlog");
		$scope.page = paginationService.createPage(25,500); // page size, buffer size

		$scope.exams = []; // exam list data
		//$scope.selectedExam = null; // selected exam to display details, key is property 'entryNumber'
		$scope.exam = null; // exam detail data
	    $scope.toggle = {};  // expand/collaps
	    
		$scope.detail.init();
	    
		//$scope.getExams(0,1000);
		//$scope.searchExam();
		
		
		// "#/examlog?action=importers_2yrs&impNo=xxx"
		
		var action;
		var search=$location.search();

		if(search && search.action == 'importers_2yrs') {
			console.log("search 2yrs exam for importer"+search.impNo);
			$scope.search.doAdhocSearch(examlogService.buildSd_importerLast2Yrs(search.impNo));
		} else 	if(search && search.action == 'importers_2wks') {
			console.log("search 2 weeks exam for importer"+search.impNo);
			$scope.search.doAdhocSearch(examlogService.buildSd_importerLast2Wks(search.impNo));
		} else 	if(search && search.action == 'importer_with_sample_2yrs') {
			console.log("search 2 weeks exam for importer"+search.impNo);
			$scope.search.doAdhocSearch(examlogService.buildSd_importerWithSampleLast2Yrs(search.impNo));
		} else 	if(search && search.action == 'importer_with_sample_2wks') {
			console.log("search 2 weeks exam for importer"+search.impNo);
			$scope.search.doAdhocSearch(examlogService.buildSd_importerWithSampleLast2Wks(search.impNo));
		} else {
			// default search
			console.log("default search");
			this.search.searchby(this.search.systemDefinedSearch[0],'system');
		}

		
//		var locHash = location.hash;
//		if(locHash != undefined && (locHash.indexOf("action=") > -1)) {
//			var idx = locHash.indexOf("action=" );
//			action = locHash.substring(idx+"action=".length);
//		}
//		
//		if(action) {
//			// special searches
//			
//		} else {
//			this.search.searchby(this.search.systemDefinedSearch[0],'system');
//		}
		
		$scope.csvExportColumnHeaders = 
			['Exam Id', 'Exam Date', 'Investigator1','Investigator1 First Name','Investigator1 Last Name',
			  'Investigator2','Investigator2 First Name','Investigator2 Last Name', 
			  'Importer Name', 'Importer Number', 'Entry', 'Country of Origin',
				'Port Name', 'Sample Number', 'Num of Products Screened', 'Product Description', 'Reasons for Screening',
				'Lead', 'Cadmium', 'FTIR Phthalates', 'CBP Action Requested', 'Disposition Date', 'Remarks',
				'Activity Time', 'Travel Time', 'Declined Indicator', 'Model/Item #', 'Label Missing/Incomplete',
				'Broker/Importer Notified'
			];

		$scope.csvExportColumnKeys = 
			['examId','examDate','investigator','investigatorFirstName','investigatorLastName',
			 'investigator2','investigator2FirstName','investigator2LastName',
			 'importerName','importerNumber','entryNumber','countryOfOrigin',
			 'portName','sampleNumber','numOfProductsScreened','productDescription','reasonsForScreening','lead','cadmium',
			 'ftirPhthalates','cbpActionRequested','dispositionDate','remarks','activityTime','travelTime',
			 'declinedIndicator','modelItemNumber','labelMissingIncomplete','brokerImporterNotified'
			 ];
		
		$scope.csvExportColumnMagic = 
			[
			 'importerNumber','entryNumber','sampleNumber','modelItemNumber'
			 ];

		$scope.exportCSV = function(){
			$scope.exportInProgressIndicator=true;
			csvExportService.executeDownload('examLog_export.csv',function(){
					console.log('Download complete!!');
				},function(){
					console.log('Download Failed!!');
					$scope.exportInProgressIndicator=false;
				},undefined); 
		};
		
		$scope.exportInProgressIndicator=false;
	}

	$scope.displayExamSampleTooltip = function(entryNumber){
		//$scope.currentHtmlTooltip = $sce.trustAsHtml('<i>Querying sample numbers for Entry ' + entryNumber + ' ...</i>');
		var tooltipHtml = '';
		tooltipHtml  = '<b>Entry Number: </b>'+entryNumber+'<br/>';
		tooltipHtml += '<b>Sample Numbers: Querying ...</b>'; 
		$scope.currentHtmlTooltip = $sce.trustAsHtml(tooltipHtml);
		
		$http({
	    	method: 'GET',
	    	url: 'tooltip/examlog/samplesTooltip/'+entryNumber
	    }).then(function successCallback(response) {
	    		var data = response.data;
	    		if(data.length>0){
	    				tooltipHtml  = '<b>Entry Number: </b>'+entryNumber+'<br/>';
	    				tooltipHtml += '<b>Sample Numbers: </b><br/>'; 
	    		           for(var i=0; i<data.length;i++) {
	    		        	   record = response.data[i];
	    		        	   tooltipHtml += record.sampleNumber + "<br/>";
	    		              	}
	    				tooltipHtml += '<br/>';
	    		}else{
	    				tooltipHtml = "No sample numbers found!!"
	    			}
	    		
	    		$scope.currentHtmlTooltip = $sce.trustAsHtml(tooltipHtml);
	    	},
	    function errorCallback(response) {
	    		$scope.currentHtmlTooltip = $sce.trustAsHtml('Error curred while fetching sample numbers!!');
	    });
	}

	$scope.searchExam = function() {
		var me = this;

		if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { 
			return false; 
		}
	    
		
		$scope.page.queryCallback = function (startIndex, numberOfRecords) {
			return examlogService.getExamList( startIndex, numberOfRecords).then(function successCallback(response) {
	            $scope.exams = response.data.data;
	            
	           //prepare data: initialize checkboxes
	           for(var i=0; i<$scope.exams.length;i++) {
	        	   	$scope.exams[i]._samples_Checkbox = ($scope.exams[i].sampleNumber && $scope.exams[i].sampleNumber.length >0);
	              	}
			
				return response;
			},
			function errorCallback(response) {
				this.message = response;
				console.log(response);
				$q.reject(response);
			});
		}
		$scope.page.reset();		
	}
	
    /**
     * get list of exams from server
     * 
     * input parameters:
     * 		startIndex
     *  	numberOfRecords
     *  
     *  output:
     * 		list of exam records 	
     */ 
    $scope.getExams = function(startIndex, numberOfRecords) {
		if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { 
			return false; 
		}
	    examlogService.getExamList($scope, startIndex, numberOfRecords);
    }

    /*
     * 2/19/2017 show sample checked only when there is at least one sample number in any product/sample record for an exam
     */
    $scope.hasSample = function(exam) {
    	if(exam.sampleNumber && exam.sampleNumber.length >0) {
    		// this is used to boost performance. It needs to be updated if user makes changes inside detail screen and removed all samples.
    		return true;
    	}
        if(exam.examSamples && exam.examSamples.length>0) {
        	var len = exam.examSamples.length;
			for(var i=0; i<len; i++) {
			  var sampleNumber = exam.examSamples[i].sampleNumber;
			  if(sampleNumber && sampleNumber.length >0) {
		    		return true;
		    	}			  
			}
        }
        return false;
    }


    /**
     * which tab to show for exam detail area
     * input:
     * 		detailTab: one from ['Details', 'Contacts', 'References','Documents']
     * output:
     * 		$scope.detailTabSelected: tab name
     */
//    $scope.showDetailTab = function(detailTab) {
//    	$scope.detailTabSelected = detailTab;
//    }
    
    /**
     * 
     */
    $scope.activeCss = function(tab) {
    	return navigationService.activeCss(tab);
	};


//	$scope.sort = function(keyname){
//		if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
//        $scope.sortKey = keyname;   //set the sortKey to the param passed
//        $scope.reverse = !$scope.reverse; //if true make it false and vice versa
//    }	
	$scope.sort = function(keyname) {
		//alert("sorting");
		if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }

		$scope.sortKey = keyname;   //set the sortKey to the param passed
        $scope.reverse = !$scope.reverse; //if true make it false and vice versa
        
        $scope.search.tableSort({
        	field: $scope.JpaFieldNameMap[$scope.sortKey],
        	sortDirection:$scope.reverse?"DESC":""
        })
    }	
	
	
	
    $scope.detail = {
    	    expandColumn: "", // ENTRY_LINE or EXAM
    		selectedEntryLine: null, // selected entry line to display details, key 'entryNumber' + 'entryLineNumber';
    		selectedExam: null, // selected exam to display details, key is property 'entryNumber'
    	    detailTabSelected: "Details",
    	    selectedIndex: 0,
    	    isHideAboveRows: true,
    	    workflowUrl:"",
    	    
    	    init: function() {
    		    $scope.$on(APP_CONSTANTS.EVENTS.closeExamDetail, function() {
    		    	$scope.detail.selectedExam = null; // close exam detail
    		    })
    	    },

    	    scrollToSummary: function() {
    	    	var y = $("#summaryTable").position().top;
    	    	window.scrollTo(0,y);
    	    },

    	    isRowHidden: function (entryLine, index) {
    			if($scope.exam && $scope.detail.selectedExam) {
    				if (index < this.selectedIndex) {
    					return this.isHideAboveRows;
    				} 
    			}
    			
    			return false;
    	    },

    	    isDetailRow: function (exam, index) {
    	    	var isDetail = (this.selectedExam && this.selectedExam.entryNumber == exam.entryNumber);
//    	    	if(isDetail) {
//    	    		console.log("isDetail for " + exam.entryNumber + ", index=" + index);
//    	    	}
    	    	return isDetail;
			},
			
			hideAboveRows: function (flag) {
				this.isHideAboveRows = flag;
			},    
			
		/*
		 * set $scope.exam to the selected exam
		 */
		setCurrentExam: function(exam) {
			var index = this.selectedIndex;
			var old_editMode = $scope.exams[index]._editMode_;

			angular.copy(exam, $scope.exams[index]);  // use copy to update value only; keep existing reference to same object 

			$scope.exams[index]._flag = "e"; // detail loaded
			$scope.exams[index]._editMode_ = old_editMode  || "VIEW"; // restore edit mode

			$scope.exam = $scope.exams[index]; // set as current exam
		},
    
	    /**
	     * Show/Hide Exam details for an entry line
	     * 
	     * Input:
	     * 		entryLine: an entry line object that has entry and line numbers property
	     * 		$selectedExam: the current shown exam details for an entry line
	     * Output:
	     * 		$scope.exam: exam details object if show
	     * 		this.selectedExam: null if previous show, exam object if previous hide/null
	     * 		this.expandColumn: ENTRY_LINE vs EXAM
	     */
		selectExam: function(exam,index) {
				var me = this;
	  		    if(this.selectedExam && this.selectedExam.entryNumber == exam.entryNumber) {
	  	    		if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
	  	    		$scope.detail.selectedExam = null; // close exam detail
	  	    		return;
	  	    	} 

	    		if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }


	  	    	// fetch exam details
	  			examlogService.getExamDetail(exam.entryNumber).then(function successCallback(data) {
		    		me.selectedExam = exam; // show detail for this exam
		  			me.selectedIndex = index; // the index in the loop
		  			me.expandColumn = "EXAM";
		  			me.hideAboveRows(true);
		  			
		  			me.setCurrentExam(data);
  			    	$scope.exam._editMode_ = "VIEW"; // reset edit mode 

		  	        navigationService.pageNavigationService.setCurrentPage({
		  				currentPageName: "view_exam_detail_page", 
		  				CleanupCallback: function() {
		  			    	$scope.detail.selectedExam = null; // close exam detail
		  				}
		  	        })
	  				
	  			});

	  	    	this.scrollToSummary();
	  			
			},

		createExam: function(entryNumber) {

			if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
			
			var exam = {
				_editMode_: "CREATE", // tell exam detail page to show in CREATE NEW exam mode
				_saveCallback_: function () { 
					  //entryLine.hasExam = true; 
					  },
				entryNumber: entryNumber,
				entryLineNumber: 0
			};
			
			$scope.exam = exam;
			$scope.exams.unshift(exam);

	    	this.selectedExam = exam; // show detail for this exam
			this.selectedIndex = 0; // the index in the loop
			this.expandColumn = "EXAM";
			this.hideAboveRows(true);
	    	$scope.$emit(APP_CONSTANTS.EVENTS.openExamDetail);	    		
		    this.scrollToSummary();

	        navigationService.pageNavigationService.setCurrentPage({
				currentPageName: "create_exam_page", 
				pageDirtyCallback: function() {
					var isDirty = ($scope.exam._editMode_ != "VIEW" && 
							utilService.getControllerScope('examDetailController')  && 
							utilService.getControllerScope('examDetailController').isExamFormDirty()); 
					return isDirty;
				},
				
				CleanupCallback: function(event) {
					if(event != "saveEdit") {
				    	$scope.detail.selectedExam = null; // close exam detail
				    	if($scope.exams[0]._editMode_ == "CREATE") {
				    		$scope.exams.shift();
				    	}
					}
				}
	        	
	        })
			
			}
	    
    } // end of detail object

	$scope.page =  {
			pageSize :  10,
			startIndex :  1,
			endIndex :  0,
			currentPage :  0,
			strCurrentPage: "0",
			totalResults :  0,
			totalPages :  0,
			showInput: false,
	
			mouseEnter: function() {
				this.showInput = true;
			},
			
			mouseLeave: function () {
				this.showInput = false;
			},
			
			setup:  function (lines) {
				console.log("page.setup()");
				console.log("lines:");
				console.log(lines);
				this.startIndex = 1;
				this.totalResults = lines;
				this.calculate();
				console.log(this);
				console.log("page.setup() completed");
			},
			
			calculate:	function() {
				this.endIndex = this.startIndex - 1 + this.pageSize;
				if(this.endIndex > this.totalResults) {
					this.endIndex = this.totalResults;
				}
				this.currentPage = Math.floor((this.startIndex-1)/this.pageSize) + 1;
				this.strCurrentPage = ""+this.currentPage;
				this.totalPages = Math.ceil(this.totalResults / this.pageSize);
				
			},
			
			gotoFirst:  function () {
				if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
				this.startIndex = 1;
				this.calculate();
			},
			
			gotoLast:  function () {
				if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
				this.startIndex = (this.totalPages-1)*this.pageSize + 1;
				this.calculate();
			},
			
			gotoPrev:  function () {
				if(this.startIndex > this.pageSize) {
					if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
					this.startIndex -= this.pageSize;
					this.calculate();
					}
			},

			gotoNext:  function () {
				if(this.startIndex + this.pageSize <= this.totalResults) {
					if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
					this.startIndex += this.pageSize;
					this.calculate();
				}
			},
			
			gotoPage:  function () {
				if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
				var pageNumber = this.currentPage;
				if(pageNumber > 0) {
					if(pageNumber <1) { 
						pageNumber = 1 };
					if(pageNumber > this.totalPages) { 
						pageNumber = this.totalPages };
					
					this.startIndex = (pageNumber-1) * this.pageSize + 1;
					console.log("gotoPage, set new startIndex");
					console.log(this);
					this.calculate();
				} 
			}
			
	}

	
	$scope.search = {
			state : false, // if show search panel
			message: "", // error messages
			searchErrors: '', // TBD
			orderByItem: {},

			systemDefinedSearch: [
			    	              {id:'0',qry:'top100',desc:'100 Recent Exams'}
			    	              ,{id:'1',qry:'all_exams',desc:'All Exams'}
			    	              ,{id:'2',qry:'exams_with_sample',desc:'Exams with Sample'}
			    	              //,{id:'3',qry:'sincelastlogout',desc:'New Since Last Logout'}
			    	        ],
			    	        
			userDefinedSearch: [],
	        portgroupSearch: [],

		    inboxQuickSearchMenuOptions: [
		       {id:'ENTRY_NUMBER', name:'Entry Number', type:'STRING'}
			  ,{id:'IMPORTER_NUMBER', name:'Importer Number', type:'STRING'}
			  ,{id:'IMPORTER_NAME', name:'Importer Name', type:'STRING'}
		      ,{id:'ENTRY_PORT_CODE', name:'Entry Port Code', type:'STRING'}
		      ,{id:'ENTRY_PORT_NAME', name:'Entry Port Name', type:'STRING'}
//		      ,{id:'EXAM_DATE_GT', name:'Exam Later Than', type:'DATE'}
//		      ,{id:'EXAM_DATE_LT', name:'Exam Earlier Than', type:'DATE'}
		      ,{id:'INVESTIGATOR_NAME', name:'Investigator Name', type:'STRING'}
		      ,{id:'HAS_SAMPLE', name:'Exam with Sample', type:'BOOLEAN'}
		    ],

	        
	        // todo: get these from user profile
		    selectedOption: {id:'ENTRY_NUMBER', name:'Entry Number'},
		    selectedOptionOp: {},
			searchValue: "",
			
			name: "",  // search name shown on inbox/examlog summary screen; 
			searchForItemList: [],  // where clause
			
			advSearchDisplayOn: false,
			selectedUserSearch:{},  // selected saved search. For new search, there will be no searchId
			new_search_name:"",
			
			clearSelectedItem: function() {
				this.selectedOption = {};
				this.selectedOptionOp = {};
				this.searchValue = null;
			},
			
			resetSearch: function() {
				this.searchForItemList = [];
				this.advSearchDisplayOn = false;
				this.clearSelectedItem();
			},
			
			toggle : function() {
				var me=this;
				this.state = !this.state;
				if(this.state) {
					//$scope.filter.state = false;
					
					// TODO:
					var username=Session.userId;
					searchService.getSavedSearchCollection("examlog", "private", username).then(function successCallback(response) {
						//svc[SEARCH_COLLECTIONS[target][avail]] = response.data;
						var tmp = me.advSearchDisplayOn;
						me.userDefinedSearch=response.data;
						if(!tmp) {
							me.displayQuickSearchForm("examlog");
						}
						
					}, function errorCallback(response) {
				    	this.message = response.status + " - " + response.statusText;
				    	console.log("ouch! >>>" + this.message + "<<<");
				});
					
					// portGroup search
					searchService.getSavedSearchCollection("examlog", "portgroup", username).then(function successCallback(response) {
						//svc[SEARCH_COLLECTIONS[target][avail]] = response.data;
						me.portgroupSearch=response.data;
//						if(me.portgroupSearch.length==0) {
//							me.portgroupSearch.push({
//								"searchName": "New Search",
//							    "searchAvailability":"PORTGROUP",
//							    "searchTarget":"EXAMLOG"
//							});
//						}
					}, function errorCallback(response) {
				    	this.message = response.status + " - " + response.statusText;
				    	console.log("ouch! >>>" + this.message + "<<<");
					});
					
				}
				this.resetSearch();
			},
			
			close: function() {
				this.state = false;
				this.resetSearch();
			},
			
			tableSort: function(orderByItem) {
				this.orderByItem = orderByItem;
				$scope.page.reset(); // refresh the current search
			},

			resetSearch: function() {
				$scope.search.orderByItem = {}; // remove previous table sort
				$scope.sortKey="";
				$scope.reverse=false;
			},
				
			save: function() {
				var me = this;
				console.log("save search");
				// save current search to the selected saved search
				//input: selectedUserSearch, searchForItemList[]
				if(me.selectedUserSearch != undefined /*&& me.selectedUserSearch.searchId != undefined*/) {
					var itemList = [];
					if(me.searchForItemList != undefined) {
						for(var i=0; i<me.searchForItemList.length;i++) {
							var item = {
									"type": me.searchForItemList[i].type,
									"field": me.searchForItemList[i].field,
									"fieldDisp": me.searchForItemList[i].fieldDisp,
									"op": me.searchForItemList[i].op,
									"opDisp": me.searchForItemList[i].opDisp,
									"value":me.searchForItemList[i].value,
									"concat": me.searchForItemList[i].concat
							}
							itemList.push(item);
						}  // i
					}  // if searchForItemList
					me.selectedUserSearch.searchCriteriaList = itemList;
					
					// call service to save to DB
					searchService.saveSearchDefinition(me.selectedUserSearch).then(function successCallback(response) {
						me.new_search_name="";
						me.close();
					});
				}  // if selectedUserSearch
				
			},
			
			saveAs: function() {
				var me = this;
				
				console.log("save search as new");

				var searchAvailability = "PRIVATE";
				if(me.selectedUserSearch != undefined && me.selectedUserSearch.searchAvailability != undefined) {
					searchAvailability = me.selectedUserSearch.searchAvailability;

				}
				
				//input:  searchForItemList[], new_search_name
				// generate new search name. Use search manager to rename
				var username=Session.userId;
				var sd = {
						"searchId": undefined,
						"searchName": me.new_search_name,
						"username":username,
						"searchTarget":"examlog",
						"searchAvailability":searchAvailability,
						"orderBys":undefined,
						"displayOrder":0,
						"searchCriteriaList":[],
						"orderBysDesc":undefined
				}
				
				me.selectedUserSearch = sd; // make this the current selected search
				me.save();
			},
			
			
			// by: 'system' for system defined, 'user' for user defined
			// qry: query name
			searchby: function( qry, by ) {
				
				if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
				
				var me = this;
				me.resetSearch();

				$scope.page.queryCallback = function (startIndex, numberOfRecords) {
					var params = {
							query: qry.qry, // qry: top100, nationop, ogcreferred, etc
							startIndex: startIndex,
							numberOfRecords: numberOfRecords  // | $scope.page.bufferSize,
							}; 
					
					spinnerService.reset();
					
					return $http({
						method: 'POST',
						url: "search/examlog/by/" + by,
						params: params,
						data: $scope.search.orderByItem
					}).then(function successCallback(response) {
							if(response.data.count > 0) {
			            $scope.exams = response.data.data;
			            console.log($scope.exams);
				           //prepare data: initialize checkboxes
				           for(var i=0; i<$scope.exams.length;i++) {
				        	   	$scope.exams[i]._samples_Checkbox = ($scope.exams[i].sampleNumber && $scope.exams[i].sampleNumber.length >0);
						              	}
							} else {
					         $scope.exams = [];
							}

							//for export
							csvExportService.setColumnHeaders($scope.csvExportColumnHeaders);
							csvExportService.setColumnKeys($scope.csvExportColumnKeys);
							csvExportService.setColumnMagic($scope.csvExportColumnMagic);
							
							csvExportService.registerDataSource("search/examlog/by/" + by, params, $scope.search.orderByItem);
							csvExportService.setDataTransformer(function(originalQueryResultList, afterDataReadyCallback){
										examIds = [];
										for(var i=0;i<originalQueryResultList.length;i++){
											examIds.push(originalQueryResultList[i]['examId']);
										}
										console.log(examIds);
										
										$http({
											method: 'POST',
											url: "search/examlog/by/ids",
											params: {},
											data: {"list": examIds},
											timeout: 600000 //10 mins
										}).then(function successCallback(response) {
												console.log(response.data);
												//params:  dataList,downloadCompleteCallback,downloadFailCallback,noDataCallback
												afterDataReadyCallback(response.data.data, function(){ 
														$scope.exportInProgressIndicator=false;
													}, function(){
															$scope.exportInProgressIndicator=false;
															colsole.log("Download failed, error occurred!!");
													});
										},function errorCallback(response) {
												$scope.exportInProgressIndicator=false;
												console.log(response);
										});
							});
							
							me.name=qry.desc;
							return response;
						},
						function errorCallback(response) {
							this.message = response;
							console.log(response);
							$q.reject(response);
						});

						return response;
					};
					
				$scope.page.reset();
				me.close();
				return false;
			},
			
			displayQuickSearchForm: function( targetView, currentList ) {
				this.selectUserSearch({}); // reset user saved search selection
				this.selectedUserSearch={}; 
				this.advSearchDisplayOn = false;
				
			},
			
			displayAdvSearchForm: function( targetView, currentList ) {
				console.log("into displayAdvSearchForm");
				this.advSearchDisplayOn = true;
		    	$scope.advancedSearchForm = searchService.createForm( targetView );

		    	// sort by field display name
		    	$scope.advancedSearchForm.comparisonOp.text.sort( utilService.sortArrayBy( 'name', false, function(a){return a.toUpperCase()}));
		    	$scope.advancedSearchForm.comparisonOp.number.sort( utilService.sortArrayBy( 'name', false, function(a){return a.toUpperCase()}));
		    	$scope.advancedSearchForm.comparisonOp.date.sort( utilService.sortArrayBy( 'name', false, function(a){return a.toUpperCase()}));
		    	$scope.advancedSearchForm.comparisonOp.boolean.sort( utilService.sortArrayBy( 'name', false, function(a){return a.toUpperCase()}));
		    	$scope.advancedSearchForm.concatnaterOp.sort( utilService.sortArrayBy( 'name', false, function(a){return a.toUpperCase()}));
		    	$scope.advancedSearchForm.searchingFieldsList.sort( utilService.sortArrayBy( 'name', false, function(a){return a.toUpperCase()}));
		    	
				return $scope.advancedSearchForm;
			},

			editSearchCriteria: function(criteria) {
				var me = this;
				for(var i=0; i<me.searchForItemList.length; i++) {
					var item = me.searchForItemList[i];
					if( item.fieldDisp == criteria.fieldDisp && item.opDisp == criteria.opDisp && 
							String(item.valueDisp) == String(criteria.valueDisp) ) {
						var edits = me.searchForItemList.splice(i,1);
						var edit = edits[0];
						// edit this criteria
						for(var i=0;i <$scope.advancedSearchForm.searchingFieldsList.length; i++) {
							if($scope.advancedSearchForm.searchingFieldsList[i].id == edit.field) {
								$scope.search.selectedOption = $scope.advancedSearchForm.searchingFieldsList[i];
								
								if( $scope.search.selectedOption.type == "DROP_DOWN_LIST" && $scope.search.selectedOption.name == "Disposition") {
									var opList = $scope.advancedSearchForm.dispositionList;
									if(opList != null) {
										for(var j=0;j<opList.length;j++) {
											if(opList[j].name.toLowerCase() == criteria.opDisp.toLowerCase()) {
												$scope.search.selectedOptionOp = opList[j];
												break;
											}
										}
									}
								} else {

								
								var opList = $scope.advancedSearchForm.comparisonOp[criteria.type.toLowerCase().replace("string","text")];
								if(opList != null) {
									for(var j=0;j<opList.length;j++) {
										if(opList[j].ql.toLowerCase() == criteria.op.toLowerCase()) {
											$scope.search.selectedOptionOp = opList[j];
											break;
										}
									}
								}
								
								$scope.search.searchValue = criteria.value;
								}
								break;
							}
						}
						return;
					}
				}
			},
			
			removeSearchCriteria: function(criteria) {
				var me = this;
				for(var i=0; i<me.searchForItemList.length; i++) {
					var item = me.searchForItemList[i];
					if( item.fieldDisp == criteria.fieldDisp && item.opDisp == criteria.opDisp && 
							String(item.valueDisp) == String(criteria.valueDisp) ) {
						me.searchForItemList.splice(i,1);
						return;
					}
				}
			},
			
			validateCriteria: function(item) {
				if(item.op == 'IS NULL' || item.op == 'IS NOT NULL') {
					item.value = null;
					item.valueDisp = '';
					return item;
				}
				if(item.field != null && item.field.length > 0 
				&& item.op != null && item.op.length > 0) {
					if(item.type!="BOOLEAN" && item.type!="OGC_REF_STATUS" && item.type!="WORKFLOW_STATUS" && item.type!="DROP_DOWN_LIST"){
						if(item.value != null && item.value.trim().length > 0) {
							return item;
						}
					}
					else {
						return item;
					}
				}
				return null;
			},
			
			addSearchCriteria: function(item) {
				var me = this;
				if(item == undefined) {
					item = {
						"type":me.selectedOption.type,
						"field":me.selectedOption.id,
						"fieldDisp":me.selectedOption.name,
						"op":me.selectedOptionOp.ql,
						"opDisp":me.selectedOptionOp.name,
						"value":me.searchValue,
						"valueDisp":me.searchValue,
						"concat":""};
				}
				
				// other than first in the list, all others are using "AND" condition 
				item.concat = (me.searchForItemList.length > 0) ? "AND" : "";
				if( me.validateCriteria(item) ) {
					if(item.value == null || (item.value.trim()).length == 0 ) {
						item.valueDisp = '';
					}
					if( item.type == "OGC_REF_STATUS" || item.type == "WORKFLOW_STATUS" ) {
						item.value = me.selectedOption.name;
						item.valueDisp = me.selectedOption.name;
					}
					if( item.type == "DROP_DOWN_LIST" ) {
						item.value = me.selectedOptionOp.value;
						item.op = "IN";
						item.opDisplay = "IN";
						
						item.type="STRING";
						item.valueDisp = "";
					}
					me.searchForItemList.push(item);
					me.clearSelectedItem();
				}
				else {
					me.clearSelectedItem();
					return false;
				}
			},
			
			selectUserSearch: function(searchFor) {
				console.log(searchFor);

				if(searchFor != undefined) {
//					this.resetSearch();
//					this.displayAdvSearchForm('Exam');
//					this.searchForItemList=[];
//					this.selectedUserSearch=searchFor;
//					this.new_search_name="";

//					https://docs.angularjs.org/api/ng/directive/ngOptions
//					Complex Models (objects or collections)
//					By default, ngModel watches the model by reference, not value. This is important to know when binding the select to a model that is an object or a collection.
//					One issue occurs if you want to preselect an option. For example, if you set the model to an object that is equal to an object in your collection, ngOptions won't be able to set the selection, because the objects are not identical. So by default, you should always reference the item in your collection for preselections, e.g.: $scope.selected = $scope.collection[3].
//					Another solution is to use a track by clause, because then ngOptions will track the identity of the item not by reference, but by the result of the track by expression. For example, if your collection items have an id property, you would track by item.id.
//					A different issue with objects or collections is that ngModel won't detect if an object property or a collection item changes. For that reason, ngOptions additionally watches the model using $watchCollection, when the expression contains a track by clause or the the select has the multiple attribute. This allows ngOptions to trigger a re-rendering of the options even if the actual object/collection has not changed identity, but only a property on the object or an item in the collection changes.

//					Note that $watchCollection does a ***shallow comparison of the properties of the object*** (or the items in the collection if the model is an array). This means that changing a property deeper than the first level inside the object/collection will not trigger a re-rendering.					
					var searchId = searchFor.searchId; // use this id to retrieve the selected option from userDefinedSearch[]
					if("PRIVATE" == searchFor.searchAvailability) {
						for(var i=0; i<this.userDefinedSearch.length; i++) {
							if(searchId == this.userDefinedSearch[i].searchId) {
								searchFor = this.userDefinedSearch[i];
								break;
							}
						}
					}
					
					if("PORTGROUP" == searchFor.searchAvailability) {
						for(var i=0; i<this.portgroupSearch.length; i++) {
							if(searchId == this.portgroupSearch[i].searchId) {
								searchFor = this.portgroupSearch[i];
								break;
							}
						}
					}
					
					//this.resetSearch();
					this.displayAdvSearchForm('Examlog');

					this.selectedUserSearch=searchFor;
					this.new_search_name="";

					this.searchForItemList=[]; // clear the search criteria
					
					if(searchFor.searchCriteriaList != undefined) {
						for(var i=0; i<searchFor.searchCriteriaList.length; i++) {
							var item = searchFor.searchCriteriaList[i];
							this.addSearchCriteria({
								"type":item.type,
								"field":item.field,
								"fieldDisp":item.fieldDisp?item.fieldDisp:item.field,
								"op":item.op,
								"opDisp":item.opDisp,
								"value":item.value,
								"valueDisp":item.value,
								"concat":""
							})
						}
					}
				}
				
				
			},
			
			// caller uses one based start index assuming normal user friendly displaying #
			doAdvSearch: function(startIndex, numberOfRecords) {
				if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
				var me = this;
				me.resetSearch();
				
				startIndex = (startIndex == null) ? DefaultStartIndex : startIndex;
				numberOfRecords = (numberOfRecords == null) ? DefaultNumberOfRecords : numberOfRecords;
				
				// assuring action: reset "concat" attribute in case original first element in original list was removed.
				me.searchForItemList[0].concat = "";
				var searchForItemListChecked = [];
				for(var i=0; i<me.searchForItemList.length; i++) {
					var item = me.searchForItemList[i];
					if( item.type == "WORKFLOW_STATUS" ) {
						item.value = me.selectedOptionOp.name;
						item.valueDisp = me.selectedOptionOp.name;
						//alert(item.value);
					}
					searchForItemListChecked.push( me.validateCriteria(me.searchForItemList[i]) );
				}
				me.searchForItemList = searchForItemListChecked;
				var searchFor = { 
						action:'SEARCH', 
						searchTarget: "EXAMLOG",
						searchCriteriaList:me.searchForItemList,
						adhocOrderBys: [$scope.search.orderByItem]
					};
				//var url = "search/examlog";

				// browser display
				var qryStrDisp = "";
				for(var i=0; i<me.searchForItemList.length; i++) {
					var it = me.searchForItemList[i];
					qryStrDisp += (it.concat != null && it.concat != "") ? " " + it.concat + " " : " ";
					qryStrDisp += it.fieldDisp + " " + it.opDisp;
					qryStrDisp += (it.valueDisp == "") ? ";" : " " + it.valueDisp + ";";
				}
				
				if(searchFor.searchName != undefined) {
					this.name = searchFor.searchName;
				}
				
				me.name = qryStrDisp;
				if(me.selectedUserSearch != undefined && me.selectedUserSearch.searchName != undefined) {
					me.name = me.selectedUserSearch.searchName + "(" + qryStrDisp + ")";
				}				
				
				me.searchFor = searchFor;			
				console.log("advSearch searchFor=");
				console.log(searchFor);
				
				$scope.page.queryCallback = function (startIndex, numberOfRecords) {
					spinnerService.reset();
					me.searchFor.adhocOrderBys = [$scope.search.orderByItem];

					return searchService.doAdvSearch(me.searchFor, startIndex, numberOfRecords).then(function successCallback(response) {
						if(response.data.count > 0) {
				            $scope.exams = response.data.data;
					           for(var i=0; i<$scope.exams.length;i++) {
					        	   	$scope.exams[i]._samples_Checkbox = ($scope.exams[i].sampleNumber && $scope.exams[i].sampleNumber.length >0);
					              	}
						} else {
				            $scope.exams = [];
						}

						//for export
						csvExportService.setColumnHeaders($scope.csvExportColumnHeaders);
						csvExportService.setColumnKeys($scope.csvExportColumnKeys);
						csvExportService.setColumnMagic($scope.csvExportColumnMagic);

						csvExportService.registerDataSource("search/" + me.searchFor.searchTarget.toLowerCase(), {}, me.searchFor);
						csvExportService.setDataTransformer(function(originalQueryResultList, afterDataReadyCallback){
									examIds = [];
									for(var i=0;i<originalQueryResultList.length;i++){
										examIds.push(originalQueryResultList[i]['examId']);
									}
									console.log(examIds);
									
									$http({
										method: 'POST',
										url: "search/examlog/by/ids",
										params: {},
										data: {"list": examIds},
										timeout: 600000 //10 mins
									}).then(function successCallback(response) {
											console.log(response.data);
											//params:  dataList,downloadCompleteCallback,downloadFailCallback,noDataCallback
											afterDataReadyCallback(response.data.data, function(){ 
													$scope.exportInProgressIndicator=false;
												}, function(){
														$scope.exportInProgressIndicator=false;
														colsole.log("Download failed, error occurred!!");
												});
									},function errorCallback(response) {
											$scope.exportInProgressIndicator=false;
											console.log(response);
									});
						});
					
						return response;
					},	function errorCallback(response) {
						this.message = response;
						console.log(response);
						$q.reject(response);
					});
				
				}
				$scope.page.reset();
				
				
				me.close();
				return false;
			},

			// adhoc search
			doAdhocSearch:  function(searchFor) {
				if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
				var me = this;
				me.resetSearch();
//				
//				me.searchForItemList = sd.searchCriteriaList;
//				//me.searchFor = sd.orderBys;
//				me.name = sd.searchName;
//
//				me.doAdvSearch(DefaultStartIndex, DefaultNumberOfRecords);
//			},
//
//			selectUserSearch: function(searchFor) {
				console.log(searchFor);

				if(searchFor != undefined) {
					//this.resetSearch();
					this.displayAdvSearchForm('Examlog');

					this.selectedUserSearch=searchFor;
					this.new_search_name="";

					this.searchForItemList=[]; // clear the search criteria
					
					if(searchFor.searchCriteriaList != undefined) {
						for(var i=0; i<searchFor.searchCriteriaList.length; i++) {
							var item = searchFor.searchCriteriaList[i];
							this.addSearchCriteria({
								"type":item.type,
								"field":item.field,
								"fieldDisp":item.fieldDisp?item.fieldDisp:item.field,
								"op":item.op,
								"opDisp":item.opDisp,
								"value":item.value,
								"valueDisp":item.value,
								"concat":""
							})
						}
					}
				}
				me.doAdvSearch(DefaultStartIndex, DefaultNumberOfRecords);
				
				
			},
			
			
			
			doUserSearch: function(searchFor) {
				if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
				var me = this;
				me.resetSearch();
				
//				startIndex = (startIndex == null) ? DefaultStartIndex : startIndex;
//				numberOfRecords = (numberOfRecords == null) ? DefaultNumberOfRecords : numberOfRecords;
//				
//				// assuring action: reset "concat" attribute in case original first element in original list was removed.
//				me.searchForItemList[0].concat = "";
//				var searchForItemListChecked = [];
//				for(var i=0; i<me.searchForItemList.length; i++) {
//					var item = me.searchForItemList[i];
//					if( item.type == "WORKFLOW_STATUS" ) {
//						item.value = me.selectedOptionOp.name;
//						item.valueDisp = me.selectedOptionOp.name;
//						//alert(item.value);
//					}
//					searchForItemListChecked.push( me.validateCriteria(me.searchForItemList[i]) );
//				}
//				me.searchForItemList = searchForItemListChecked;
//				var searchFor = { 
//						action:'SEARCH', 
//						searchCriteriaList:me.searchForItemList,
//						adhocOrderBys: [$scope.search.orderByItem]
//					};
//				//var url = "search/examlog";
//
//				// browser display
//				var qryStrDisp = "";
//				for(var i=0; i<me.searchForItemList.length; i++) {
//					var it = me.searchForItemList[i];
//					qryStrDisp += (it.concat != null && it.concat != "") ? " " + it.concat + " " : " ";
//					qryStrDisp += it.fieldDisp + " " + it.opDisp;
//					qryStrDisp += (it.valueDisp == "") ? ";" : " " + it.valueDisp + ";";
//				}
//				
//				
//				me.name = qryStrDisp;
//				
//				
				
				me.searchFor = searchFor;			
				searchFor.orderByItem = [$scope.search.orderByItem];
				console.log("advSearch searchFor=");
				console.log(searchFor);
				me.name = searchFor.searchName;
				$scope.page.queryCallback = function (startIndex, numberOfRecords) {
					spinnerService.reset();
					me.searchFor.adhocOrderBys = [$scope.search.orderByItem];

					return searchService.doAdvSearch(searchFor, startIndex, numberOfRecords).then(function successCallback(response) {
						if(response.data.count > 0) {
				            $scope.exams = response.data.data;
					           for(var i=0; i<$scope.exams.length;i++) {
					        	   	$scope.exams[i]._samples_Checkbox = ($scope.exams[i].sampleNumber && $scope.exams[i].sampleNumber.length >0);
					              	}
						} else {
				            $scope.exams = [];
						}

					
						return response;
					},	function errorCallback(response) {
						this.message = response;
						console.log(response);
						$q.reject(response);
					});
				
				}
				$scope.page.reset();
				
				
				me.close();
				return false;
			},

			// quick search
			doSearch:  function() {
				if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
				var me = this;
				me.resetSearch();
				
				me.searchForItemList = [];
				var item = {};
				switch( me.selectedOption.id ) {
				case "ENTRY_NUMBER":
					item = { "type":"STRING", "field":"entryNumber", "fieldDisp":"Entry Number",
							"op":"LIKE", "opDisp":"LIKE", 
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
					
				case "IMPORTER_NUMBER":
					item = { "type":"STRING", "field":"importer", "fieldDisp":"Importer Number",
							"op":"LIKE", "opDisp":"LIKE", 
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "IMPORTER_NAME":
					item = { "type":"STRING", "field":"importerName", "fieldDisp":"Importer Name",
							"op":"LIKE", "opDisp":"LIKE", 
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "ENTRY_PORT_NAME":
					item = { "type":"STRING", "field":"entryPortName", "fieldDisp":"Entry Port Name",
						"op":"LIKE", "opDisp":"LIKE", 
						"value":me.searchValue, "valueDisp":me.searchValue, 
						"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "ENTRY_PORT_CODE":
					item = { "type":"STRING", "field":"entryPortCode", "fieldDisp":"Entry Port Code",
							"op":"LIKE", "opDisp":"LIKE", 
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "INVESTIGATOR_NAME":
					item = { "type":"STRING", "field":"examiner", "fieldDisp":"Investigator Name",
							"op":"LIKE", "opDisp":"LIKE", 
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
//					item = { "type":"STRING", "field":"investigatorLastName", "fieldDisp":"Investigator Last Name",
//							"op":"LIKE", "opDisp":"LIKE", 
//							"value":me.searchValue, "valueDisp":me.searchValue, 
//							"concat":"OR" };
//					if( me.validateCriteria(item) ) {
//						me.searchForItemList.push(item);
//					}
					break;
					
				case "HAS_SAMPLE":
					item = { "type":"BOOLEAN", "field":"hasSample", "fieldDisp":"Has Sample",
							"op":"=", "opDisp":"", 
							"value":me.searchValue, "valueDisp":me.searchValue=="1"?"Yes":"No", 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "SCORE_GT":
					item = { "type":"FLOAT", "field":"ramScore", "fieldDisp":"RAM Score",
							"op":">", "opDisp":">", 
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "SCORE_LT":
					item = { "type":"FLOAT", "field":"ramScore", "fieldDisp":"RAM Score",
							"op":"<", "opDisp":"<", 
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "HTS":
					item = { "type":"STRING", "field":"hts", "fieldDisp":"HTS Number",
							"op":"LIKE", "opDisp":"LIKE", 
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "ENTRY_ARRIVAL_DATE_GT":
					item = { "type":"DATE", "field":"entryArrivalDate", "fieldDisp":"Arrive Later Than",
							"op":">", "opDisp":">", 
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "ENTRY_ARRIVAL_DATE_LT":
					item = { "type":"DATE", "field":"entryArrivalDate", "fieldDisp":"Arrive Earlier Than",
							"op":"<", "opDisp":"<", 
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "ENTRY_RELEASE_DATE_GT":
					item = { "type":"DATE", "field":"releaseDate", "fieldDisp":"Release Later Than",
						"op":">", "opDisp":">", 
						"value":me.searchValue, "valueDisp":me.searchValue, 
						"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "ENTRY_RELEASE_DATE_LT":
					item = { "type":"DATE", "field":"releaseDate", "fieldDisp":"Release Earlier Than",
						"op":"<", "opDisp":"<", 
						"value":me.searchValue, "valueDisp":me.searchValue, 
						"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				default:
					console.log("Quick Search menu selection id not found")
					break;
				}
				me.doAdvSearch(DefaultStartIndex, DefaultNumberOfRecords);
			},
	};		
	
	
	$scope.examlogAction = {
			examlogActionMenu: [],

			setupExamlogActions:  function() {
				this.examlogActionMenu = [ 'Create Exam','Decline Exam'];
				console.log(this.examlogActionMenu);
			},
		    performExamAction: function(action) {
				console.log(action);
				switch(action) {
				case 'Create Exam':
	  				var modalInstance = $uibModal.open({
						templateUrl: './examlog/popup/new_exam_popup.html',
						controller: 'entrySearchController',
						scope: $scope,
						backdrop: "static"
						});	  	
					break;
					
				default:
					console.log($scope);
					break;
				}
					
			}
				
	};

	// support adhoc sort by inbox summary table
	$scope.JpaFieldNameMap = {
//			<th ng-click="sort('entryNumber');">Entry<sort-icons key="sortKey" value="entryNumber"/></th>
//			<th ng-click="sort('importerNumber');">Importer #<sort-icons key="sortKey" value="importerNumber"/></th>
//			<th ng-click="sort('importerName');">Importer Name<sort-icons key="sortKey" value="importerName"/></th>
//			<th ng-click="sort('portName');">Port Name<sort-icons key="sortKey" value="portName"/></th>
//			<th ng-click="sort('examDate');">Exam Date<sort-icons key="sortKey" value="examDate"/></th>
//			<th ng-click="sort('investigatorFormatted');">Investigator<sort-icons key="sortKey" value="investigatorFormatted"/></th>
//			<th ng-click="sort('_samples_Checkbox');">Samples<sort-icons key="sortKey" value="_samples_Checkbox"/></th>
			//	dto.setExamId(""+exam.getId());
			//	dto.setEntryNumber(exam.getEntryNumber());
			//	dto.setExamDate(toString(exam.getActivityDate(), ApplicationConstants.DateFormats.mmddyyyy));
			//	dto.setInvestigatorFormatted(exam.getExaminer());
			//	dto.setImporterName(exam.getImporterName());
			//	dto.setImporterNumber(exam.getImporter());
			//	dto.setPortName(exam.getEntryPortName());
			//	dto.setSampleNumber((exam.getHasSample() == null || exam.getHasSample()==0)?"":"Y");
			examId: "id",
			entryNumber: "entryNumber",
			examDate: "activityDate",
			investigatorFormatted: "examiner",
			importerName: "importerName",
			importerNumber: "importer",
			portName: "entryPortName",
			_samples_Checkbox: "hasSample"
	}

	$scope.recentUserActionsList=[];
	$scope.recentUserActionsQuerying=true;
	$scope.recentUserAct=function(){
			$scope.recentUserActionsList=[];
			var modalInstance = $uibModal.open({
					templateUrl: './examlog/exam_recent_user_actions.html',
		      controller: 'eRecentUsrActModalCtrl',
		      resolve: {	
		    	  		user_id: function(){return Session.userId},
		    	  		src_scope: $scope
		      		 }
			});

			$scope.recentUserActionsQuerying=true;
			$http({
				method: 'GET',
				url: "history/examLog/top100UserActs/"+ Session.userId,
			}).then(function successCallback(response) {
				$scope.recentUserActionsList=response.data;
				//console.log("Got "+response.data.length+" records..");

				$scope.recentUserActionsQuerying=false;
			},
			function errorCallback(response) {
				this.message = response;
				console.log(response);
				me.name=qry.desc;
				$q.reject(response);
				
				$scope.recentUserActionsQuerying=false;
			});
	}
	
	
	/**
	 * debugging tools
	 */
    $scope.tbd = function(feature) {
    	console.log("feature " + feature + "TBD.");
    };

    $scope.showObject = function(feature) {
    	console.log(feature);
    };

    // call init()
    $scope.init();

}]);


app.controller('eRecentUsrActModalCtrl', ['$scope','$uibModalInstance','user_id','src_scope',function ($scope,$uibModalInstance,user_id,src_scope) {
	$scope.user_id = user_id;
	$scope.act_records = src_scope.recentUserActionsList;
	$scope.src_scope = src_scope;
	
	$scope.closeDialog=function(){
		$uibModalInstance.dismiss('cancel');
	};
	
$scope.examLogUserActClick=function(histRecord){
			src_scope.search.resetSearch();
		
			src_scope.search.searchForItemList = [];
			var item = {};
				item = { "type":"FLOAT", "field":"ID", "fieldDisp":"Exam ID",
						"op":"=", "opDisp":"=", 
						"value":histRecord.examId, "valueDisp":histRecord.examId, 
						"concat":"" };
				if( src_scope.search.validateCriteria(item) ) {
					src_scope.search.searchForItemList.push(item);
				}
				
				src_scope.search.doAdvSearch(0,500);
				
				$scope.closeDialog();
	};
}]);

console.log ("Done loading examlog.js");


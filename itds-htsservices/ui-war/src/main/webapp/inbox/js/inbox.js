

console.log ("loading inbox.js");

var DefaultNumberOfRecords = 32;
var DefaultStartIndex = 1; // user should see the start index as normal way: 1

app.controller('inboxController', ['$scope', '$http', '$q', '$route', '$filter', 
                                   'navigationService','paginationService', 'workflowService','searchService',
                                   'spinnerService','nationalOpService', 'Session','utilService', 'APP_CONSTANTS',
                                   'csvExportService','$sce', 'userRepository','$uibModal',
                                   function($scope,  $http, $q, $route, $filter, 
                                		   navigationService,  paginationService, workflowService, searchService, 
                                		   spinnerService,nationalOpService, Session,utilService, APP_CONSTANTS,
                                		   csvExportService, $sce, userRepository,$uibModal) {
	console.log("into inbox::controller()");
	var detailTabList = ['Details', 'Analysis', 'Importer', 'Workflow', 'Exam', 'Comments'];

    $scope.fetchUserDetails = function () {
        return userRepository.getUser(Session.userId);
    };

	/**
	 * initialize the controller: invoke this at end of this controller definition
	 */
	$scope.init = function() {
		console.log("inbox::init()");
		$scope.isChrome = navigator.userAgent.indexOf("Chrom")>0;
//		console.log($scope.isChrome);
//		alert($scope.isChrome);


        navigationService.setCurrentTab("inbox");
        
        var defaultPageSize = 16; // default is quicker for user profile
        $scope.page = paginationService.createPage(defaultPageSize,DefaultNumberOfRecords); // page size, buffer size
// The below is commented out to prevent the rest service from being called twice.
// The above change was needed to keep the page size at 16.
/*
        try {
            if(Session.userId) {
                userRepository.getUser(Session.userId).then(function successCallback(data) {
	                if(data && data.inboxrowsshown && data.inboxrowsshown>0 && data.inboxrowsshown<=100 && (data.inboxrowsshown != defaultPageSize)) {
	                                $scope.page.setPageSize(data.inboxrowsshown);
	                }
                }, function errorCallback(response) {
                	this.message = response.status + " - " + response.statusText;
                });
            }
        } catch(e) {
                        console.log("error in setting new page size");
        }		
	*/
		$scope.entryLines = []; // entry line list data
		this.selectedEntryLine = null; // selected entry line to display details, key 'entryNumber' + 'entryLineNumber';
		$scope.entryLine = null; // entry line detail data
	    $scope.detailTabSelected = "Details";
	    this.expandColumn = ""; // expand ENTRY_LINE or EXAM

		$scope.exams = []; // exam list data
		this.selectedExam = null; // selected exam to display details, key is property 'entryNumber'
		$scope.exam = null; // exam detail data

		$scope.workflowActions = null; // track workflow status action menu
		$scope.nationalOps = null; // action menu for national Ops


		$scope.nationalOpRules= undefined;
		nationalOpService.getNationalOpRules().then(function successCallback(data) {
			//$scope.nationalOpRules = data; // this can be used to map op to iconfile
			var map = [];
			for(var i=0;i<data.length;i++) {
				map[data[i].code] = data[i].iconfilename;
			}
//			var map = [];
//			for(var i=0;i<data.length;i++) {
//				map.push({
//					"code": data[i].code,
//					"iconfilename": data[i].iconfilename
//				})
//			}
			$scope.nationalOpIconFileNameMap = map;
		}, function errorCallback(response) {
	    	this.message = response.status + " - " + response.statusText;
		});

		$scope.counters.refresh();
		//$scope.getEntryLines(DefaultStartIndex, DefaultNumberOfRecords);
		// assign this as the current search definition (default)
		searchService.currentSearchDefinition = {
				action:"SEARCH",
				searchCriteriaList:[
					{type:"STRING",field:"entryNumber",fieldDisp:"Entry Number",op:"IS NOT NULL",opDisp:"HAS VALUE",value:null,valueDisp:null,concat:""}
				],
				orderBys:[
					{field:"entryArrivalDate",sortDirection:"DESC"},
					{field:"ramScore",sortDirection:"DESC"}
				]
		}
		console.log("entrylines count:" + $scope.entryLines.length);

		$scope.filterValue = "";
		$scope.checkboxCount = 0;
		$scope.$watch('filterValue', $scope.processFilterValueChange);

		$scope.detail.init();

		console.log("inbox::init() completed");

		$scope.search.selectedAdvField = {};
		$scope.search.selectedAdvFieldOp = {};
		$scope.search.advFieldCriteriaValue = '';
		$scope.search.advSearchDisplayOn = false;
		$scope.search.searchForItemList = [];
		$scope.search.orderByItem = {field:"", sortDirection:""};
//gz add Importer Number
		$scope.csvExportColumnHeaders =
			['Entry','Line','HTS','Importer Name','Importer Number','Manufacturer','MID','Filer','Port Code','Release Date','Arrival Date','Score','Op','Last Update User','Last Update Time','Workflow Status'];
		$scope.csvExportColumnKeys =
			['entryNumber','entryLineNumber','htsNumber','importerName','importerNumber','manufacturerName','mid','filerCode','portCode','releaseDate','arrivalDate','ramScore','nationalOp','uUser','uTime','workflowStatus'];
		$scope.csvExportColumnMagic = 
			['entryNumber','htsNumber','filerCode','portCode'];

		$scope.exportCSV = function(){
			if( $scope.exportInProgressIndicator ){
				return;
			}
			
			$scope.exportInProgressIndicator=true;
			csvExportService.executeDownload('inbox_export.csv',function(){
				console.log('Download complete!!');
				$scope.exportInProgressIndicator=false;
			},function(){
				console.log('Download Failed!!');
				$scope.exportInProgressIndicator=false;
			},undefined);
		};

		$scope.exportInProgressIndicator=false;


		//for tooltip
		$scope.currentHtmlTooltip= $sce.trustAsHtml('<i>Querying...</i>');
		$scope.displayHtsInfo = function(entryLine){
		$scope.currentHtmlTooltip = $sce.trustAsHtml('<i>Querying HTS '+entryLine.htsNumber+' ...</i>');
			$http({
		    	method: 'GET',
		    	url: 'tooltip/inbox/hts/'+entryLine.htsNumber
		    }).then(function successCallback(response) {
		    		data = response.data;
		    		tooltipHtml = '';
		    		if(data.length>0){
		    				tooltipHtml = response.data[0].description;
		    		}else{
		    				tooltipHtml = "No reference data found!!"
		    			}

		    		$scope.currentHtmlTooltip = $sce.trustAsHtml(tooltipHtml);
		    	},
		    function errorCallback(response) {
		    		$scope.currentHtmlTooltip = $sce.trustAsHtml('Error curred while fetching reference data!!');
		    });
		}

		$scope.displayImporterInfo = function(entryLine){
			$scope.currentHtmlTooltip = 
				  $sce.trustAsHtml('<i>Querying Importer '+entryLine.importerName+' ('+entryLine.importerNumber+') ...</i>');
			$http({
		    	method: 'GET',
		    	url: 'tooltip/inbox/importer/'+entryLine.importerNumber
		    }).then(function successCallback(response) {
		    		data = response.data;
		    		tooltipHtml = '';
		    		if(data.length>0){
		    				record = response.data[0];
		    				tooltipHtml = '<b>EIN: </b>'+entryLine.importerNumber+'<br/>'+
										    				'<b>Name: </b>'+entryLine.importerName+'<br/>'+
										    				'<b>Address: </b>'+(record.addressLine1?record.addressLine1:'')+(record.addressLine2?record.addressLine2:'')+'<br/>'+
										    				'<b>City: </b>'+record.city+'<br/>'+
										    				'<b>State: </b>'+record.state+'<br/>';
		    		}else{
		    				tooltipHtml = "No reference data found!!"
		    			}
		    		
		    		$scope.currentHtmlTooltip = $sce.trustAsHtml(tooltipHtml);
		    	},
		    function errorCallback(response) {
		    		$scope.currentHtmlTooltip = $sce.trustAsHtml('Error curred while fetching reference data!!');
		    });
		}

		/**
		 * Method used to format entry Line Number ==> insert a "-" before the last character of entry line number.
		 * 
		 * @param {String} entryLineNumber  
		 * @returns {String}
		 */
		$scope.formatEntryLineNumber = function(entryLineNumber){

			var formattedEntryLineNumber;
			if(entryLineNumber === null){
				formattedEntryLineNumber = "";
			} else {
				var entrySplit = entryLineNumber.split("");
			    entrySplit.splice(entrySplit.length-1, 0, '-');
			    formattedEntryLineNumber = entrySplit.join('');
			}

			return formattedEntryLineNumber;
		}
		
		
		$scope.displayManufacturerInfo = function(entryLine){
			$scope.currentHtmlTooltip = 
				  $sce.trustAsHtml('<i>Querying Manufacturer '+entryLine.manufacturerName+' ('+entryLine.mid+') ...</i>');
			$http({
		    	method: 'GET',
		    	url: 'tooltip/inbox/manufacturer/'+entryLine.mid
		    }).then(function successCallback(response) {
		    		data = response.data;
		    		tooltipHtml = '';
		    		if(data.length>0){
		    				record = response.data[0];
		    				tooltipHtml = '<b>MID: </b>'+entryLine.mid+'<br/>'+
										    				'<b>Name: </b>'+entryLine.manufacturerName+'<br/>'+
										    				'<b>Address: </b>'+(record.addressLine1?record.addressLine1:'')+(record.addressLine2?record.addressLine2:'')+'<br/>'+
										    				'<b>City: </b>'+record.city+'<br/>';;
		    		}else{
		    				tooltipHtml = "No reference data found!!"
		    			}
		    		
		    		$scope.currentHtmlTooltip = $sce.trustAsHtml(tooltipHtml);
		    	},
		    function errorCallback(response) {
		    		$scope.currentHtmlTooltip = $sce.trustAsHtml('Error curred while fetching reference data!!');
		    });
		}

		$scope.displayFilerInfo  = function(entryLine){
			$scope.currentHtmlTooltip = 
				  $sce.trustAsHtml('<i>Querying Filer '+entryLine.filerName+' ('+entryLine.filerCode+') ...</i>');
			$http({
		    	method: 'GET',
		    	url: 'tooltip/inbox/filer/'+entryLine.filerCode
		    }).then(function successCallback(response) {
		    		data = response.data;
		    		tooltipHtml = '';
		    		if(data.length>0){
		    				record = response.data[0];
		    				tooltipHtml = '<b>Filer Code: </b>'+entryLine.filerCode+'<br/>'+
		    											'<b>Name: </b>'+entryLine.filerName+'<br/>'+
									    				'<b>Address: </b>'+(record.addressLine1?record.addressLine1:'')+(record.addressLine2?record.addressLine2:'')+'<br/>'+
									    				'<b>City: </b>'+record.city+'<br/>'+
									    				'<b>State: </b>'+record.state+'<br/>';
		    		}else{
		    				tooltipHtml = "No reference data found!!"
		    			}
		    		
		    		$scope.currentHtmlTooltip = $sce.trustAsHtml(tooltipHtml);
		    	},
		    function errorCallback(response) {
		    		$scope.currentHtmlTooltip = $sce.trustAsHtml('Error curred while fetching reference data!!');
		    });
		};
		
		$scope.displayPortInfo = function(entryLine){
			$scope.currentHtmlTooltip = 
				  $sce.trustAsHtml('<i>Querying Port '+entryLine.portCode+' ...</i>');
			$http({
		    	method: 'GET',
		    	url: 'tooltip/inbox/port/'+entryLine.portCode
		    }).then(function successCallback(response) {
		    		data = response.data;
		    		tooltipHtml = '';
		    		if(data.length>0){
		    				record = response.data[0];
		    				tooltipHtml = record.portName;
		    		}else{
		    				tooltipHtml = "No reference data found!!"
		    			}
		    		
		    		$scope.currentHtmlTooltip = $sce.trustAsHtml(tooltipHtml);
		    	},
		    function errorCallback(response) {
		    		$scope.currentHtmlTooltip = $sce.trustAsHtml('Error curred while fetching reference data!!');
		    });
		};
		
		$scope.displayNationalOpInfo = function(entryLine){
			$scope.currentHtmlTooltip = 
				  $sce.trustAsHtml('<i>Querying ...</i>');
			$http({
		    	method: 'GET',
		    	url: 'tooltip/inbox/NationalOp/'+entryLine.entryNumber+'/'+entryLine.entryLineNumber
		    }).then(function successCallback(response) {
		    		data = response.data;
		    		tooltipHtml = '';
		    		if(data.length>0){
		    				record = response.data[0];
		    				tooltipHtml = '<b>Name: </b>' + record.name + '<br/>' +
		    					'<b>Action Message: </b>' + record.actionMessage;
		    		}else{
		    				tooltipHtml = "No reference data found!!"
		    			}
		    		
		    		$scope.currentHtmlTooltip = $sce.trustAsHtml(tooltipHtml);
		    	},
		    function errorCallback(response) {
		    		$scope.currentHtmlTooltip = $sce.trustAsHtml('Error curred while fetching reference data!!');
		    });
		};

	
		this.search.searchby(this.search.systemDefinedSearch[0],'system');
	
	}
	
	$scope.nationalOpIconFileName = function (opCode) {
		//$scope.nationalOpIconFileNameMap
		if(opCode && $scope.nationalOpIconFileNameMap != undefined) {
			var filename = $scope.nationalOpIconFileNameMap[opCode];
			if(filename) {
				return "./img/nationalOp/" + filename;
			}
		}
		return "./img/nationalOp/flag_yellow.png"; // default
	}
	
	$scope.setEntrylines = function(entryLines) {
		$scope.entryLines = entryLines;
		//$scope.page.setup(entryLines.length);
		$scope.detail.init();
		console.log(entryLines);
	} 
	
	$scope.counters = {
		redCount: "",
		newSinceLastLogout: "",
		refresh: function () {
			var me = this;
			var url = "inbox/counters/red";
			var params = {"color":"RED"};
			$http({
		    	method: 'POST',
		    	url: url,
		      	  byPassSpinner: true,
		    	params: params
		    }).then(function successCallback(response) {
		    	me.redCount = response.data;
		    	if(!me.redCount) {
		    		me.redCount = 0;
		    	}
		    	console.log(response.data);
		    },
		    function errorCallback(response) {
		    	this.message = response.status + " - " + response.statusText;
		    	console.log(response);
		    	$q.reject(response);
		    });
			
			url = "inbox/counters/lastlogout";
			params = {"query":"sincelastlogout"};
			$http({
		    	method: 'POST',
		    	url: url,
		      	  byPassSpinner: true,
		    	params: params
		    }).then(function successCallback(response) {
		    	me.newSinceLastLogout = response.data;
		    	if(!me.newSinceLastLogout) {
		    		me.newSinceLastLogout = 0;
		    	}
		    	console.log("since log out " + me.newSinceLastLogout + ">>>" + response.data);
		    },
		    function errorCallback(response) {
		    	this.message = response.status + " - " + response.statusText;
		    	console.log(response);
		    	$q.reject(response);
		    });
			
		}
		
	}
	

	$scope.recentUserActionsList=[];
	$scope.recentUserActionsQuerying=true;
	$scope.recentUserAct=function(){
			$scope.recentUserActionsList=[];
			var modalInstance = $uibModal.open({
					templateUrl: './inbox/inbox_recent_user_actions.html',
		      controller: 'iRecentUsrActModalCtrl',
		      resolve: {	
		    	  		user_id: function(){return Session.userId},
		    	  		src_scope: $scope
		      		 }
			});

			$scope.recentUserActionsQuerying=true;
			$http({
				method: 'GET',
				url: "history/inbox/top100UserActs/"+ Session.userId,
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
	
	$scope.search = {
			state : false, // if show search panel
			message: "", // error messages
			searchErrors: '', // TBD
			hideValueField: true,
			orderByItem: {},

		//  search data structure
			        // current search name and list of criteria
					name: "",  // search name shown on inbox/examlog summary screen; 
					searchForItemList: [],  // where clause - list of search criteria/item
					
					// Enter search area
					selectedOption: {id:'ENTRY_NUMBER', name:'Entry Number'},
				    selectedOptionOp: {},
					searchValue: "",
					
					// Quick search vs Advanced search
					advSearchDisplayOn: false,
					
					// selected search, new search
					selectedUserSearch:{},  // selected saved search. For new search, there will be no searchId
					new_search_name:"",

		// end

			
	        // todo: get these from server in future
		    inboxQuickSearchMenuOptions: [
		       {id:'ENTRY_NUMBER', name:'Entry Number', type:'STRING'}
			  ,{id:'HTS', name:'HTS Number', type:'STRING'}
		      ,{id:'INVESTIGATOR_NAME', name:'Investigator Name', type:'STRING'}
		      ,{id:'PORT_ENTRY_CODE', name:'Port Entry Code', type:'STRING'}
//		      ,{id:'PORT_ENTRY_NAME', name:'Port Entry Name', type:'STRING'}
		      ,{id:'SCORE_GT', name:'Score Greater Than', type:'FLOAT'}
		      ,{id:'SCORE_LT', name:'Score Less Than', type:'FLOAT'}
		      ,{id:'ENTRY_ARRIVAL_DATE_GT', name:'Arrive Later Than', type:'DATE'}
		      ,{id:'ENTRY_ARRIVAL_DATE_LT', name:'Arrive Earlier Than', type:'DATE'}
		      ,{id:'ENTRY_RELEASE_DATE_GT', name:'Release Later Than', type:'DATE'}
		      ,{id:'ENTRY_RELEASE_DATE_LT', name:'Release Earlier Than', type:'DATE'}
		    ],

			// todo: get these from server in future
			systemDefinedSearch: [
	              {id:'0',qry:'top100',desc:'Top Scored Entry Lines'}
	              ,{id:'1',qry:'nationalop',desc:'National OP Only'}
	              ,{id:'2',qry:'ogcreferred',desc:'OGC Referred Only'}
	              ,{id:'3',qry:'redcategoryonly',desc:'Red Category Only'}
	              ,{id:'4',qry:'sincelastlogout',desc:'New Since Last Logout'}
	        ],
	        
	        // todo: get these from user profile
			userDefinedSearch: [
//	              {id:'0',qry:'usrqry1',desc:'CI name like pan'}
//	              ,{id:'1',qry:'usrqry2',desc:'HTS like 0101'}
//	              ,{id:'2',qry:'usrqry3',desc:'HTS like 0101 and Score > 80'}
	        ],

	        portgroupSearch: [],
	        
	        checkOpChoice: function() {
	        	var me = this;
	        	if(me.selectedOptionOp.id == 'null' || me.selectedOptionOp.id == 'nnull') {
	        		$(".search-box-adv-span-li-input-value").attr('disabled', true);
	        		me.hideValueField = true;
	        	}
	        	else {
	        		$(".search-box-adv-span-li-input-value").attr('disabled', false);
	        		me.hideValueField = false;
	        	}
	        },
			
			clearSelectedItem: function() {
				this.selectedOption = {};
				this.selectedOptionOp = {};
				this.searchValue = null;
			},
			
//			resetSearch: function() {
//				this.searchForItemList = [];
//				this.advSearchDisplayOn = false;
//				this.clearSelectedItem();
//			},
			
			toggle : function() {
				var me=this;
				this.state = !this.state;
				if(this.state) {
					//$scope.filter.state = false;
					
					var username=Session.userId;
					
					// user defined search
					searchService.getSavedSearchCollection("inbox", "private", username).then(function successCallback(response) {
						//svc[SEARCH_COLLECTIONS[target][avail]] = response.data;
						var tmp = me.advSearchDisplayOn;
						me.userDefinedSearch=response.data;
						if(!tmp) {
							me.displayQuickSearchForm("inbox");
						}
					}, function errorCallback(response) {
				    	this.message = response.status + " - " + response.statusText;
				    	console.log("ouch! >>>" + this.message + "<<<");
					});
					
					// portGroup search
					searchService.getSavedSearchCollection("inbox", "portgroup", username).then(function successCallback(response) {
						//svc[SEARCH_COLLECTIONS[target][avail]] = response.data;
						me.portgroupSearch=response.data;
					}, function errorCallback(response) {
				    	this.message = response.status + " - " + response.statusText;
				    	console.log("ouch! >>>" + this.message + "<<<");
					});
				}
				//this.resetSearch();
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

			selectUserSearch: function(searchFor) {
				console.log(searchFor);
				if(searchFor != undefined) {
					var searchId = searchFor.searchId; // use this id to retrieve the selected option from userDefinedSearch[]
					// look for the selected search by comparing searchId. AngularJS does not seem to refresh the selected option even if the definition has changed
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
					this.displayAdvSearchForm('Inbox');

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

			save: function() {
				var x = $scope;
				
				var me = this;
				console.log("save search");
				// save current search to the selected saved search
				//input: selectedUserSearch, searchForItemList[]
				if(me.selectedUserSearch != undefined /*&& me.selectedUserSearch.searchId != undefined*/) {
					if(me.selectedUserSearch.searchAvailability != 'PRIVATE') {
						alert("PORT Group Search Update not allowed");
						return;
					}
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
						
						me.selectedUserSearch=undefined;
						me.close();
					});
				}  // if selectedUserSearch
				
			},
			
			delete: function() {
				var x = $scope;
				
				var me = this;
				console.log("delete search");

				if(me.selectedUserSearch) {
					if(me.selectedUserSearch.searchAvailability != 'PRIVATE') {
						alert("Only private search is allowed to be deleted.");
						return;
					}
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
					
					// call service remove from DB
					searchService.deleteSearchDefinition(me.selectedUserSearch).then(function successCallback(response) {
						me.new_search_name="";
						me.selectedUserSearch=undefined;
						me.close();
					});
				}  // if selectedUserSearch
				
			},

			saveAs: function() {
				var x = $scope;
				var me = this;
				
				console.log("save search as new");
				//input:  searchForItemList[], new_search_name
				// generate new search name. Use search manager to rename
				
				var searchAvailability = "PRIVATE";
//				if(me.selectedUserSearch != undefined && me.selectedUserSearch.searchAvailability != undefined) {
//					searchAvailability = me.selectedUserSearch.searchAvailability;
//
//				}
				
				var username=Session.userId;
				var sd = {
						"searchId": undefined,
						"searchName": me.new_search_name,
						"username":username,
						"searchTarget":"inbox",
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
						url: "inbox/entryline/by/" + by,
						params: params,
						data: $scope.search.orderByItem
					}).then(function successCallback(response) {
						//$scope.page.setup(response.data.count);
						$scope.setEntrylines(response.data.data);
						//for export
						csvExportService.setColumnHeaders($scope.csvExportColumnHeaders);
						csvExportService.setColumnKeys($scope.csvExportColumnKeys);
						csvExportService.setColumnMagic($scope.csvExportColumnMagic);
						csvExportService.registerDataSource("inbox/entryline/by/" + by, params, $scope.search.orderByItem);
						
						me.name=qry.desc;
						return response;
					},
					function errorCallback(response) {
						this.message = response;
						console.log(response);
						me.name=qry.desc;
						$q.reject(response);
					});
				}
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
		    	$scope.advancedSearchForm.comparisonOp.text_wo_in.sort( utilService.sortArrayBy( 'name', false, function(a){return a.toUpperCase()}));
		    	$scope.advancedSearchForm.comparisonOp.text_wo_ordering.sort( utilService.sortArrayBy( 'name', false, function(a){return a.toUpperCase()}));
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
					if( item.field == criteria.field && item.op == criteria.op && 
							String(item.value) == String(criteria.value) ) {
						var edits = me.searchForItemList.splice(i,1);
						var edit = edits[0];
						// edit this criteria
						for(var i=0;i <$scope.advancedSearchForm.searchingFieldsList.length; i++) {
							if($scope.advancedSearchForm.searchingFieldsList[i].id == edit.field) {
								me.clearSelectedItem();
								$scope.search.selectedOption = $scope.advancedSearchForm.searchingFieldsList[i];
								
								var item = $scope.search.selectedOption;
								if( item.type == "WORKFLOW_STATUS" || item.type == "OGC_REF_STATUS" || item.type == "DROP_DOWN_LIST" ) {
									var opList = null;
									var valueList = null;
									if( item.type == "OGC_REF_STATUS" ) {
										opList = $scope.advancedSearchForm.ogcStatusList;
									} else if (item.type == "WORKFLOW_STATUS" ) {
										opList = $scope.advancedSearchForm.comparisonOp.workflowStatus;
										valueList = $scope.advancedSearchForm.workflowStatusList;
									} else if( item.type == "DROP_DOWN_LIST" && item.name == "National Operation") {
										//var opList = $scope.advancedSearchForm.nationalOpList;
										opList = $scope.advancedSearchForm.comparisonOp.nationalOp;
										valueList = $scope.advancedSearchForm.nationalOpList;
									} else {
										opList = [];
									}
									
									if(opList != null) {
											for(var j=0;j<opList.length;j++) {
												if(opList[j].name.toLowerCase() == criteria.opDisp.toLowerCase()) {
													$scope.search.selectedOptionOp = opList[j];
													break;
												}
											}
										}
									if(valueList != null) {
										for(var j=0;j<valueList.length;j++) {
											if(valueList[j].value.toLowerCase() == criteria.value.toLowerCase()) {
												$scope.search.searchValue = valueList[j];
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
				// add a search criteria to current search conditions
				// item: a search criteria from saved search
				// if item is undefined, add user entered search selection/parameter to current search conditions
				var me = this;
				if(item == undefined) {
					item = {
						"type":me.selectedOption.type,
						"field":me.selectedOption.id,
						"fieldDisp":me.selectedOption.name,
						"op":me.selectedOptionOp.ql,
						"opDisp":me.selectedOptionOp.name,
						"value":(me.searchValue && me.searchValue.id)?me.searchValue.value:me.searchValue,
						"valueDisp":(me.searchValue && me.searchValue.id)?me.searchValue.name:me.searchValue,
						"concat":""};
				}
				
				// other than first in the list, all others are using "AND" condition 
				item.concat = (me.searchForItemList.length > 0) ? "AND" : "";
				if( me.validateCriteria(item) ) {
					if(item.value == null || (item.value.trim()).length == 0 ) {
						item.valueDisp = '';
					}
//					if( item.type == "WORKFLOW_STATUS" ) {
//					if( item.type == "xxxOGC_REF_STATUS" || item.type == "xxxxWORKFLOW_STATUS" ) {
//						item.value = me.selectedOption.name;
//						item.valueDisp = me.selectedOption.name;
//					}
//					if( item.type == "DROP_DOWN_LIST" ) {
//						item.value = me.selectedOptionOp.value;
//						item.op = "IN";
//						item.opDisplay = "IN";
//						
//						item.type="STRING";
//						item.valueDisp = "";
//					}
					me.searchForItemList.push(item);
					me.clearSelectedItem();
				}
				else {
					me.clearSelectedItem();
					return false;
				}
			},
			
			// caller uses one based start index assuming normal user friendly displaying #
			doAdvSearch: function() {
				if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
				var me = this;
				me.resetSearch();
				
				// assuring action: reset "concat" attribute in case original first element in original list was removed.
				me.searchForItemList[0].concat = "";
				var searchForItemListChecked = [];
				for(var i=0; i<me.searchForItemList.length; i++) {
					var item = me.searchForItemList[i];
//					if( item.type == "WORKFLOW_STATUS" ) {
					if( item.type == "OGC_REF_STATUS" || item.type == "xxxWORKFLOW_STATUS" ) {
						item.value = me.selectedOptionOp.name;
						item.valueDisp = me.selectedOptionOp.name;
						//alert(item.value);
					}
					searchForItemListChecked.push( me.validateCriteria(me.searchForItemList[i]) );
				}
				me.searchForItemList = searchForItemListChecked;
				var adhocOrderBys = [];
				var orderByItem = $scope.search.orderByItem;
				if($scope.search.orderByItem) {
					if(orderByItem.field == "entryNumber") {
						adhocOrderBys.push({
							field: "entryNumber",
							sortDirection: orderByItem.sortDirection
							});
						adhocOrderBys.push({
							field: "entryLineNumber",
							sortDirection: orderByItem.sortDirection
							});
					} else {
						adhocOrderBys.push({
							field: orderByItem.field,
							sortDirection: orderByItem.sortDirection
							});
					}
				}
				var searchFor = { 
						action:'SEARCH', 
						searchCriteriaList:me.searchForItemList,
						adhocOrderBys: adhocOrderBys
					};
				//var url = "inbox/entryline/search/advanced";
				var url = "search/inbox";
				
				// browser display
				var qryStrDisp = "";
				for(var i=0; i<me.searchForItemList.length; i++) {
					var it = me.searchForItemList[i];
					qryStrDisp += (it.concat) ? " " + it.concat + " " : " ";
//					qryStrDisp += (it.concat != null && it.concat != "") ? " " + it.concat + " " : " ";
					qryStrDisp += it.fieldDisp + " " + it.opDisp;
					qryStrDisp += (it.valueDisp) ? " " + it.valueDisp : "";
					qryStrDisp += ";";
//					qryStrDisp += (it.valueDisp == "") ? ";" : " " + it.valueDisp + ";";
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
					searchFor.adhocOrderBys = [$scope.search.orderByItem];

					return $http({
						method: 'POST',
			        	url: url,
			        	data: searchFor,
			        	params : {
			        		startIndex: startIndex, 
			        		numberOfRecords: numberOfRecords
			        		}
					}).then(function successCallback(response) {
						$scope.setEntrylines(response.data.data);
						//me.name = qryStrDisp;
						//for export
						csvExportService.setColumnHeaders($scope.csvExportColumnHeaders);
						csvExportService.setColumnKeys($scope.csvExportColumnKeys);
						csvExportService.setColumnMagic($scope.csvExportColumnMagic);
						csvExportService.registerDataSource(url, {}, searchFor);
						
						return response;
					}, function errorCallback(response) {
						console.log(response);
						//me.name = qryStrDisp;
						$q.reject(response);
					});
				
				}
				$scope.page.reset();
				
				
				me.close();
				return false;
			},
			
			doSearch:  function() {
				if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
				// search entry like searchValue
				var me = this;
				me.resetSearch();
				
				me.searchForItemList = [];
//				var valueStr = String( me.searchValue );
				var item = {};
				switch( me.selectedOption.id ) {
				case "ENTRY_NUMBER":
					item = { "type":"STRING", "field":"entryNumber", "fieldDisp":"Entry Number",
							"op":"LIKE", "opDisp":"LIKE", 
//							"value":valueStr, "valueDisp":valueStr,
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
//				case "PORT_ENTRY_NAME":
//					alert('to do, implement this search');
//					break;
				case "PORT_ENTRY_CODE":
					item = { "type":"STRING", "field":"portEntryCode", "fieldDisp":"Port Entry Code",
							"op":"LIKE", "opDisp":"LIKE", 
//							"value":valueStr, "valueDisp":valueStr,
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "INVESTIGATOR_NAME":
					item = { "type":"STRING", "field":"investigatorFirstName", "fieldDisp":"Investigator First Name",
							"op":"LIKE", "opDisp":"LIKE", 
//							"value":valueStr, "valueDisp":valueStr,
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					item = { "type":"STRING", "field":"investigatorLastName", "fieldDisp":"Investigator Last Name",
							"op":"LIKE", "opDisp":"LIKE", 
//							"value":valueStr, "valueDisp":valueStr,
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"OR" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "SCORE_GT":
					item = { "type":"FLOAT", "field":"ramScore", "fieldDisp":"RAM Score",
							"op":">", "opDisp":">", 
//							"value":valueStr, "valueDisp":valueStr,
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "SCORE_LT":
					item = { "type":"FLOAT", "field":"ramScore", "fieldDisp":"RAM Score",
							"op":"<", "opDisp":"<", 
//							"value":valueStr, "valueDisp":valueStr,
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "HTS":
					item = { "type":"STRING", "field":"hts", "fieldDisp":"HTS Number",
							"op":"LIKE", "opDisp":"LIKE", 
//							"value":valueStr, "valueDisp":valueStr,
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "ENTRY_ARRIVAL_DATE_GT":
					item = { "type":"DATE", "field":"entryArrivalDate", "fieldDisp":"Arrive Later Than",
							"op":">", "opDisp":">", 
//							"value":valueStr, "valueDisp":valueStr,
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "ENTRY_ARRIVAL_DATE_LT":
					item = { "type":"DATE", "field":"entryArrivalDate", "fieldDisp":"Arrive Earlier Than",
							"op":"<", "opDisp":"<", 
//							"value":valueStr, "valueDisp":valueStr,
							"value":me.searchValue, "valueDisp":me.searchValue, 
							"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "ENTRY_RELEASE_DATE_GT":
					item = { "type":"DATE", "field":"releaseDate", "fieldDisp":"Release Later Than",
						"op":">", "opDisp":">", 
//						"value":valueStr, "valueDisp":valueStr,
						"value":me.searchValue, "valueDisp":me.searchValue, 
						"concat":"" };
					if( me.validateCriteria(item) ) {
						me.searchForItemList.push(item);
					}
					break;
				case "ENTRY_RELEASE_DATE_LT":
					item = { "type":"DATE", "field":"releaseDate", "fieldDisp":"Release Earlier Than",
						"op":"<", "opDisp":"<", 
//						"value":valueStr, "valueDisp":valueStr,
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
				me.doAdvSearch();
			},
	};		
	
	$scope.filter = {
			state : false, // if show filter panel
			message: "", // error messages
		    inboxQuickSearchMenuOptions: [
		      {id: 'ENTRY', name: 'Entry #'},
		      {id: 'PORT', name: 'Port'},
		      {id: 'INVESTIGATOR', name: 'Investigator'},
		      {id: 'SCORE_G', name: 'Score greater than'},
		      {id: 'SCORE_L', name: 'Score less than'}
		    ],
		    selectedOption: {id: 'ENTRY', name: 'Entry #'}, //This sets the default value of the select in the ui
			filterValue: "",
			toggle : function() {
				this.state = !this.state;
				if(this.state) {
					$scope.search.state = false;
				}
			},
			
			filterby: function( qry, by ) {
				var me = this;
				// by: "system" for system predefined, "user" for user saved (need future add numbering)
				var params = {query: qry}; // qry: top100, nationop, ogcreferred, etc
				$http({
			    	method: 'POST',
			    	url: "inbox/entryline/by/" + by,
			    	params: params
			    }).then(function successCallback(response) {
			    	$scope.setEntrylines(response.data);
			    	//$scope.entryLines = response.data;
			    	//$scope.page.setup($scope.entryLines.length);
			    	me.toggle();
			    	console.log(response.data);
			    },
			    function errorCallback(response) {
			    	this.message = response;
			    	console.log(response);
			    });
			},
			
			dofilter:  function() {
				// filter entry like filterValue
				var url = "inbox/entryline/like/entry";
				var params = {pattern: this.filterValue};
				var me = this;
				switch(this.selectedOption.id) {
				  case "ENTRY":
						url = "inbox/entryline/like/entry";
						params = {pattern: this.filterValue};
					  break;
				  case "PORT":
					  
					  break;
				  case "INVESTIGATOR":
					  
					  break;
				  case "SCORE_G":
					  
					  break;
				  case "SCORE_L":
					  
					  break;
				  default:
					  
					  break;
				}

				$http({
			    	method: 'POST',
			    	url: url,
			    	params: params
			    }).then(function successCallback(response) {
			    	$scope.setEntrylines(response.data);
			    	//$scope.entryLines = response.data;
			    	//$scope.page.setup($scope.entryLines.length);
			    	me.toggle();
			    	console.log(response.data);
			    },
			    function errorCallback(response) {
			    	this.message = response;
			    	console.log(response);
			    });
				
				
			}
			
			
		    };		
	
	/**
     * when filter changed, reset checkboxes
     */
	$scope.processFilterValueChange = function() {
		console.log("processFilterValueChange()");
		$scope.entryLines.isChecked = false;	
		var len = $scope.entryLines.length;
    	for(var i=0; i<len; i++) {
    		$scope.entryLines[i].isChecked = false;
    	}
    	$scope.checkboxCount = 0;
	}


    /**
     * get list of entryLine from server
     * 
     * input parameters:
     * 		startIndex
     *  	numberOfRecords
     *  
     *  output:
     * 		list of entryLine records 	
     */ 
    $scope.getEntryLines = function(startIndex, numberOfRecords) {
		var url = "inbox/entryline";
		startIndex = (startIndex == null) ? DefaultStartIndex : startIndex;
		numberOfRecords = (numberOfRecords == null) ? DefaultNumberOfRecords : numberOfRecords;
		console.log("into inbox::getEntryLines() startIndex="+startIndex+"<<< numberOfRecords="+numberOfRecords+"<<<");
		
	    $http({
	    	method: 'POST',
	    	url: url,
	    	params: {startIndex: startIndex-1, numberOfRecords: numberOfRecords}
	    }).then(function successCallback(response) {
	    	//$scope.entryLines = response.data;
	    	//$scope.page.setup($scope.entryLines.length);
	    	$scope.setEntrylines(response.data);
	    	console.log("done getting inbox data");
	    	console.log(response.data);
	    },
	    function errorCallback(response) {
	    	console.log(response);
	    	$q.reject(response);
	    });
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
    	    	// listener for exam detail close events, sent from exam detail controller
    		    $scope.$on(APP_CONSTANTS.EVENTS.closeExamDetail, function() {
    		    	$scope.detail.selectedExam = null; // close exam detail
    		    });
    		    
    		    this.isHideAboveRows = false;
    		    this.selectedEntryLine = null;
    		    this.selectedExam = null;
    	    },

    	    scrollToSummary: function() {
    	    	var y = $("#summaryTableRow").position().top;
    	    	window.scrollTo(0,y);
    	    },
    	    
    	    isRowHidden: function (entryLine, index) {
    			if(($scope.exam && $scope.detail.selectedExam) || ($scope.entryLine && $scope.detail.selectedEntryLine)) {
    				if (index < this.selectedIndex) {
    					return this.isHideAboveRows;
    				} 
    			}
    			
    			return false;
    	    },

    	    isDetailRow: function (entryLine, index) {
    	    	var x = ($scope.exam && this.selectedExam && this.selectedExam.entryNumber == entryLine.entryNumber && this.selectedExam.entryLineNumber == entryLine.entryLineNumber) || 
    	    		   ($scope.entryLine && this.selectedEntryLine && this.selectedEntryLine.entryNumber == entryLine.entryNumber && this.selectedEntryLine.entryLineNumber == entryLine.entryLineNumber);
//    	    	if(x) {
//    	    		console.log("isDetail for " + entryLine.entryNumber + ", index=" + index);}

    	    	return x;
			},
			
			hideAboveRows: function (flag) {
				this.isHideAboveRows = flag;
			},    

    /**
     * Show/Hide entryLine details for an entry line
     * 
     * Input:
     * 		entryLine: an entry line object that has entry and line numbers property
     * 		this.selectedEntryLine: the current shown entry line detail
     * Output:
     * 		$scope.entryLine: entry line details object if show
     * 		this.selectedEntryLine: null if previous show, entry line object if previous hide/null
     * 		this.expandColumn: ENTRY_LINE vs EXAM
     */
    selectEntryLine:  function(entryLine, index) {
		if(this.selectedEntryLine 
		&& this.selectedEntryLine.entryNumber == entryLine.entryNumber
		&& this.selectedEntryLine.entryLineNumber == entryLine.entryLineNumber) {
			if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
			//this.selectedEntryLine = null; // close entryLine detail
			return;
		} 
		if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
		this.selectedExam = null; // close exam detail
		this.selectedEntryLine = entryLine; // show detail for this entryLine
		this.selectedIndex = index; // the index in the loop
		this.expandColumn = "ENTRY_LINE";
		this.hideAboveRows(true);
		//$("#summaryTable").scrollTop(300);
		this.scrollToSummary();
		
		var me = this;
		// fetch entryLine details
		var url = "inbox/entrysummary";
		$http({
			method: 'POST',
        	url: url,
        	params : {entryNumber: me.selectedEntryLine.entryNumber, lineNumber: me.selectedEntryLine.entryLineNumber}
		}).then(function successCallback(response) {
  			$scope.entryLine = response.data;
  			$scope.summary = response.data;
  			$scope.detail.workflowUrl = $scope.getWorkflowNavigation(me.selectedEntryLine.workflowStatus, me.selectedEntryLine.hasExam, me.selectedEntryLine.nationalOp).imgUrl;
  			me.entryLine = response.data;
  			if( me.entryLine[0].investigatorLastName != null && me.entryLine[0].investigatorLastName != '' ) {
  				me.investigatorFullNameString = me.entryLine[0].investigatorLastName + ', ' + me.entryLine[0].investigatorFirstName;
  			}
		}, function errorCallback(response) {
			console.log(response);
			$q.reject(response);
		});
		
    	// fetch tradeparties details
        var url = "inbox/tradeparties";
        $http({
      	  method: 'POST',
      	  url: url,
      	  params : {entryNumber: me.selectedEntryLine.entryNumber, lineNumber: me.selectedEntryLine.entryLineNumber}
      	}).then(function successCallback(response) {
      		$scope.parties = response.data;
//      		console.log("inbox::selectEntryLine::tradeparties $scope.parties=")+console.log($scope.parties)+console.log("<<<");
      	}, function errorCallback(response) {
      		console.log(response);
      		$q.reject(response);
      	});

        $scope.detailTabs = detailTabList;
        $scope.detailTabSelected = $scope.detailTabs[0];

        navigationService.pageNavigationService.setCurrentPage({
			currentPageName: "view_entry_detail_page", 
			CleanupCallback: function() {
		    	$scope.detail.selectedEntryLine = null; // close entryLine detail
			}
        	
        })

    },
    

    revertWorkflowStatus: function() {
    	var me = this;
    	var prevWorkstatus = $scope.entrylineHistory[1].workflowStatus;
    	var myEntryLineList = [];
    	myEntryLineList[0] = me.selectedEntryLine;
    	
    	if (confirm("Are you sure to revert back to previous status?")) {

    		var myEntryNumber = $scope.summary.entryNumber;
    		var myEntryLineNumber = $scope.summary.lineNumber;
    		
			var url = "inbox/entryline/revertWorkflowStatus";
			$http({
				method: 'POST',
	        	url: url,
	        	params : {entryNumber: myEntryLineList[0].entryNumber, entryLineNumber: myEntryLineList[0].entryLineNumber}
			}).then(function successCallback(response) {
	  			var retData = response.data;
	  			for(var i=0; i<$scope.entryLines.length;i++) {
	  				if( $scope.entryLines[i].isChecked ||
	  						($scope.entryLines[i].entryNumber == $scope.entryLine[0].entryNumber 
	  								&& $scope.entryLines[i].entryLineNumber == $scope.entryLine[0].lineNumber ) ) {
	  					$scope.entryLines[i].workflowStatus = retData[0].workflowStatus;
	  				} 
	  			}
		        var url = "inbox/entryline/workflow/history";
		        $http({
		        	method: 'GET',
		        	url: url,
			      	  byPassSpinner: true,
		        	params : {entryNumber: myEntryLineList[0].entryNumber, entryLineNumber: myEntryLineList[0].entryLineNumber}
		      	}).then(function successCallback(response) {
		      		$scope.entrylineHistory = response.data;
		      	}, function errorCallback(response) {
		      		console.log("check");
		      		$q.reject(response);
		      	});
			}, function errorCallback(response) {
				console.log("check");
				$q.reject(response);
			});
    	}
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
	selectExam: function(entryLine,index) {
    	if(this.selectedExam && this.selectedExam.entryNumber == entryLine.entryNumber && this.selectedExam.entryLineNumber == entryLine.entryLineNumber) {
    		if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
    		//this.selectedExam = null; // close exam detail
        	//$scope.$emit(APP_CONSTANTS.EVENTS.closeExamDetail);	    		
    		return;
    	} 
    	
		if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
		this.selectedEntryLine = null; // close entryLine detail
    	this.selectedExam = entryLine; // show detail for this exam
		this.selectedIndex = index; // the index in the loop
		this.expandColumn = "EXAM";
		this.hideAboveRows(true);
    	$scope.$emit(APP_CONSTANTS.EVENTS.openExamDetail);	    		
    	this.scrollToSummary();
    	
    	// fetch exam details
        var url = "exams/" + this.selectedExam.entryNumber;
        $http({
      	  method: 'POST',
      	  url: url
      	}).then(function successCallback(response) {
  				console.log(response);
  				$scope.exam = response.data;
  				$scope.exam._editMode_= "VIEW"; // tell exam detail page to show in view exam mode
      	  }, function errorCallback(response) {
  				console.log(response);
  				$q.reject(response);
      	  });

        navigationService.pageNavigationService.setCurrentPage({
			currentPageName: "view_exam_detail_page", 
			CleanupCallback: function() {
		    	$scope.detail.selectedExam = null; // close exam detail
			}
        })

	},
	

	/*
	 * set $scope.exam to the selected exam
	 */
	setCurrentExam: function(exam) {
		var index = this.selectedIndex;
		var old_editMode = $scope.exam._editMode_;

		angular.copy(exam, $scope.exam);  // use copy to update value only; keep existing reference to same object 

		$scope.exam._flag = "e"; // detail loaded
		$scope.exam._editMode_ = old_editMode  || "VIEW"; // restore edit mode

	
	},
	
	createExam: function(entryLine,index) {
    	if(this.selectedExam && this.selectedExam.entryNumber == entryLine.entryNumber && this.selectedExam.entryLineNumber == entryLine.entryLineNumber) {
    		if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
    		return;
    	} 
    	
		if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
		this.selectedEntryLine = null; // close entryLine detail
    	this.selectedExam = entryLine; // show detail for this exam
		this.selectedIndex = index; // the index in the loop
		this.expandColumn = "EXAM";
		this.hideAboveRows(true);
    	$scope.$emit(APP_CONSTANTS.EVENTS.openExamDetail);	    		
	    this.scrollToSummary();
		
		$scope.exam={
			_editMode_: "CREATE", // tell exam detail page to show in CREATE NEW exam mode
			_saveCallback_: function () { 
				  entryLine.hasExam = true; 
				  },
			entryNumber: entryLine.entryNumber,
			entryLineNumber: entryLine.entryLineNumber
		};

        navigationService.pageNavigationService.setCurrentPage({
			currentPageName: "create_exam_page", 
			pageDirtyCallback: function() {
				//var isDirty = ($scope.exam._editMode_ != "VIEW" && utilService.getControllerScope('examDetailController').exam_form.$dirty); 
				var isDirty = ($scope.exam._editMode_ != "VIEW" && 
						utilService.getControllerScope('examDetailController') &&
						utilService.getControllerScope('examDetailController').isExamFormDirty()); 
				return isDirty;
			},
			
			CleanupCallback: function(event) {
				if(event != "saveEdit") {
			    	$scope.detail.selectedExam = null; // close exam detail
				}
			}
        	
        })
		
		}
    
	
    }

    /**
     * which tab to show entry detail area
     * input:
     * 		detailTab: one from ['Details', 'Analysis', 'Importer', 'Workflow', 'Exam', 'Comments']
     * output:
     * 		$scope.detailTabSelected: tab name
     */
    $scope.showDetailTab = function(detailTab) {
    	$scope.detailTabSelected = detailTab;
    	
    	if(detailTab == "Workflow") {
        	// fetch workflow history
            var url = "inbox/entryline/workflow/history";
            $http({
          	  method: 'GET',
          	  url: url,
	      	  byPassSpinner: true,
          	  params : {entryNumber: $scope.detail.selectedEntryLine.entryNumber, entryLineNumber: $scope.detail.selectedEntryLine.entryLineNumber}
          	}).then(function successCallback(response) {
          		$scope.entrylineHistory = response.data;
          		console.log("inbox::selectEntryLine::getWorkflowHistory $scope.entrylineHistory"); console.log($scope.entrylineHistory);
          	}, function errorCallback(response) {
          		console.log(response);
          		$q.reject(response);
          	});
    	}
    	
//    	if(detailTab == "Importer") {
//			var url = "inbox/importer/counts";
//			$http({
//				method: 'GET',
//				url: url,
//				params : { importer: $scope.detail.selectedEntryLine.importerNumber }
//			}).then(function successCallback(response) {
//				$scope.detail.importerCounts = response.data;
//				var xticks = [[1,$scope.detail.importerCounts.fyqtrMinus7],[2,$scope.detail.importerCounts.fyqtrMinus6],
//					[3,$scope.detail.importerCounts.fyqtrMinus5],[4,$scope.detail.importerCounts.fyqtrMinus4],
//					[5,$scope.detail.importerCounts.fyqtrMinus3],[6,$scope.detail.importerCounts.fyqtrMinus2],
//					[7,$scope.detail.importerCounts.fyqtrMinus1],[8,$scope.detail.importerCounts.fyqtrCurrent]];
//				var examSer = [$scope.detail.importerCounts.noFyqtrMinus7,$scope.detail.importerCounts.noFyqtrMinus6,
//					$scope.detail.importerCounts.noFyqtrMinus5,$scope.detail.importerCounts.noFyqtrMinus4,
//					$scope.detail.importerCounts.noFyqtrMinus3,$scope.detail.importerCounts.noFyqtrMinus2,
//					$scope.detail.importerCounts.noFyqtrMinus1,$scope.detail.importerCounts.noFyqtrCurrent];
//				var sampleSer = [$scope.detail.importerCounts.noFyqtrMinus7Samples,$scope.detail.importerCounts.noFyqtrMinus6Samples,
//					$scope.detail.importerCounts.noFyqtrMinus5Samples,$scope.detail.importerCounts.noFyqtrMinus4Samples,
//					$scope.detail.importerCounts.noFyqtrMinus3Samples,$scope.detail.importerCounts.noFyqtrMinus2Samples,
//					$scope.detail.importerCounts.noFyqtrMinus1Samples,$scope.detail.importerCounts.noFyqtrCurrentSamples];
//				var maxY = Math.max.apply(null, [Math.max.apply(null, examSer), Math.max.apply(null, sampleSer)]);
//				var tickIntervalY = (maxY>500)? 50: ((maxY>200)? 20: ((maxY>100)? 10: ((maxY>50)? 5: ((maxY>10)? 2 : 1))));
//				var plot1 = $.jqplot( "chartdiv", [examSer,sampleSer], {
//					animate: true,
//					title: "Importer: " + $scope.detail.selectedEntryLine.importerName,
//					seriesDefaults: {
//						linePattern: 'solid',
//						showMarker: true,
//						shadow: true
//					},
//					axes: {
//						xaxis: {
//				            angle: -90,
//							ticks: xticks
//						},
//						yaxis: {
//							min: 0,
//							max: (maxY + 2),
//							tickInterval: tickIntervalY, 
//							tickOptions: { 
//								formatString: '%d' 
//							}
//						}
//					},
//				    legend: {
//				        renderer: $.jqplot.EnhancedLegendRenderer,
//				    	show: true,
//				    	labels: ["Exam","Samples"],
//				    	location: 'ne',
//				    	placement: "outside",
//				    	marginBottom: "1px",
//				    },
//				});
//				console.log($scope.detail.importerCounts);
//			}, function errorCallback(response) {
//				console.log(response);
//			});
//    	}
    	navigationService.setCurrentTab(detailTab);
    }
    
    /**
     * 
     */
    $scope.activeCss = function(tab) {
    	return navigationService.activeCss(tab);
	};
	
	$scope.clearCheckedAll = function(toggleAllModel) {
		toggleAllModel.isChecked = false;	

		var filtered = $filter('filter')(toggleAllModel, $scope.filterValue);
    	for(var i=0; i<toggleAllModel.length; i++) {
    		toggleAllModel[i].isChecked = false;
    	}
    	for(var i=0; i<filtered.length; i++) {
    		filtered[i].isChecked = toggleAllModel.isChecked;
    	}
    	$scope.checkboxCount = toggleAllModel.isChecked?filtered.length:0;
	}

	/**
	 * process SelectAll
	 */
	$scope.toggleCheckedAll = function(toggleAllModel) {
		toggleAllModel.isChecked = !toggleAllModel.isChecked;	

		var filtered = $filter('filter')(toggleAllModel, $scope.filterValue);
    	for(var i=0; i<toggleAllModel.length; i++) {
    		toggleAllModel[i].isChecked = false;
    	}
    	for(var i=0; i<filtered.length; i++) {
    		filtered[i].isChecked = toggleAllModel.isChecked;
    	}
    	$scope.checkboxCount = toggleAllModel.isChecked?filtered.length:0;
	}
	
	/**
	 * process entry line checkbox
	 */
	$scope.toggleChecked = function (toggleAllModel, toggleModel) {
		toggleModel.isChecked = !toggleModel.isChecked;
		if(toggleModel.isChecked) {
			$scope.checkboxCount ++;
		} else {
			$scope.checkboxCount --;
		}
		if(toggleModel.isChecked) {
			var filtered = $filter('filter')(toggleAllModel, $scope.filterValue);
	    	var flagAllChecked = true;
			for(var i=0; i<filtered.length; i++) {
	    		if(!filtered[i].isChecked) {
	    			flagAllChecked = false;
	    			break;
	    		}
	    	}
			toggleAllModel.isChecked = flagAllChecked;
		} else {
			toggleAllModel.isChecked = false;
		}
	}
	

	
//	$scope.sort = function(keyname){
//		if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
//        $scope.sortKey = keyname;   //set the sortKey to the param passed
//        $scope.reverse = !$scope.reverse; //if true make it false and vice versa
//    }	

	// setup both workflow and nationalOps
	$scope.setupBatchActions = function() {
    	/* only status=Scored allows batch operation */
    	var isScored = true;
    	for(var i=0;i<$scope.entryLines.length;i++) {
    		if($scope.entryLines[i].isChecked && $scope.entryLines[i].workflowStatus != 'Scored') {
    			isScored = false;
    			break;
    		}
    	}
    	if(isScored) {
			// add a header for workflow actions
			$scope.workflowActions = [{header: 'Set Workflow Status'}, 'May Proceed', 'CBP Hold Requested','Refer to Field', 'CBP Hold Approved'];

			nationalOpService.getNationalOpRules().then(function successCallback(response) {
    			// setup national ops
    			var menu = [];
    			menu.push({header: 'Set National Op'});
    			if(response) {
    				for(var i=0; i< response.length;i++) {
    					menu.push({"item": response[i]});
    				}
    			}
    			//menu = ['CBP Hold Approved',{divider:true},{header: 'CBP Released'}, 'Hold Rejected','Entry Released'];
    			menu.push({divider:true});
    			menu.push({header: 'Reset National Op'});
    			menu.push({
    				"item": {
    					"id": -1,
    					"name": "clear"
    					}
    				});
    			$scope.nationalOps = menu;
    			
    		}, function errorCallback(response) {
    	    	this.message = response.status + " - " + response.statusText;
    	});
    	}
    	else {
    		$scope.workflowActions = [{header: "Entries not of 'Scored' status: Workflow and National Ops are disabled"}];
    		$scope.nationalOps = [];
    	}
    },

	
	$scope.setupNationalOps = function() {
		// populate array nationalOps[]
		nationalOpService.getNationalOpRules().then(function successCallback(response) {
			var menu = [];
			menu.push({header: 'Set National Op'});
			if(response) {
				for(var i=0; i< response.length;i++) {
					menu.push({"item": response[i]});
				}
			}
			//example: menu = ['CBP Hold Approved',{divider:true},{header: 'CBP Released'}, 'Hold Rejected','Entry Released'];
			menu.push({divider:true});
			menu.push({header: 'Reset National Op'});
			menu.push({
				"item": {
					"id": -1,
					"name": "clear"
					}
				});
			$scope.nationalOps = menu;
			
		}, function errorCallback(response) {
	    	this.message = response.status + " - " + response.statusText;
	});
	}
	
    /*
     * process national op for current entry line
     */
    //	nationalOpService.processNationalOp = function(entryLines, nationalOpId, nationalOpName) {

    $scope.processNationalOp = function(entryLine, item) {
    	var entryLines = [ {
			"entryNumber" : entryLine.entryNumber,
			"entryLineNumber" : entryLine.entryLineNumber
    	}];
    	var nationalOpId = item.id;
        var nationalOpName=  item.name;
        
        nationalOpService.processNationalOp(entryLines, nationalOpId, nationalOpName).then(function successCallback(data) {
  			var retData = data;
  			// TODO: refresh inbox
  			$scope.page.refresh();
		}, function errorCallback(response) {
			console.log(response);
			$q.reject(response);
		});
    },
    $scope.processBatchNationalOp = function(item) {
    	var me = this;
    	var entryLines = [];
    	for(var i=0;i<$scope.entryLines.length;i++) {
    		if($scope.entryLines[i].isChecked && $scope.entryLines[i].workflowStatus == 'Scored') {
    			entryLines.push({
    				  "entryNumber" : $scope.entryLines[i].entryNumber,
    				  "entryLineNumber" : $scope.entryLines[i].entryLineNumber
    			})
    		}
    	}
    	var nationalOpId = item.id;
        var nationalOpName=  item.name;
        
        nationalOpService.processNationalOp(entryLines, nationalOpId, nationalOpName).then(function successCallback(data) {
  			var retData = data;
	    	$scope.clearCheckedAll($scope.entryLines);
  			$scope.page.refresh();
  			
		}, function errorCallback(response) {
			console.log(response);
			$q.reject(response);
		});
    },
///// working...
	
    $scope.setupWorkflowStatus = function(workflowStatus, hasExam, nationalOp) {
    	$scope.workflowActions = $scope.getWorkflowNavigation(workflowStatus, hasExam, nationalOp).menu;
    },
    

    /*
     * change entryline work flow status batch updates
     */
    $scope.processBatchWorkflowStatus = function(workflowStatus) {
    	var me = this;
    	var entryLines = [];
    	for(var i=0;i<$scope.entryLines.length;i++) {
    		if($scope.entryLines[i].isChecked && $scope.entryLines[i].workflowStatus == 'Scored') {
    			entryLines.push({
    				  "entryNumber" : $scope.entryLines[i].entryNumber,
    				  "entryLineNumber" : $scope.entryLines[i].entryLineNumber
    			})
    		}
    	}
		var url = "inbox/updateWorkflowStatus";
		$http({
			method: 'POST',
        	url: url,
        	params : {workflowStatus: workflowStatus},
	      	data: entryLines
		}).then(function successCallback(response) {
  			var retData = response.data;
  			if(true || retData.counter == $scope.checkboxCount) {
  				// success updates
  		    	for(var i=0;i<$scope.entryLines.length;i++) {
  		    		if($scope.entryLines[i].isChecked) {
//  		    		if($scope.entryLines[i].isChecked && $scope.entryLines[i].workflowStatus == 'Scored') {
  		    			$scope.entryLines[i].workflowStatus = workflowStatus;
  		    		}
  		    	}
  		    	$scope.clearCheckedAll($scope.entryLines);
  		    	$scope.page.refresh();

  			}
	        var url = "inbox/entryline/workflow/history";
	        $http({
	        	method: 'GET',
	        	url: url,
		      	  byPassSpinner: true,
	        	params : {entryNumber: $scope.detail.selectedEntryLine.entryNumber, entryLineNumber: $scope.detail.selectedEntryLine.entryLineNumber}
	        }).then(function successCallback(response) {
	        	$scope.entrylineHistory = response.data;
		    }, function errorCallback(response) {
		    	console.log("check");
		    });
		}, function errorCallback(response) {
			console.log(response);
			$q.reject(response);
		});
    },
    
    /*
     * change entryline work flow status update for current entry line
     */
    $scope.processWorkflowStatus = function(entryLine, workflowStatus) {
    	var entryLines = [ {
			"entryNumber" : entryLine.entryNumber,
			"entryLineNumber" : entryLine.entryLineNumber
    	}];
		var url = "inbox/updateWorkflowStatus";
		$http({
			method: 'POST',
        	url: url,
        	params : {workflowStatus: workflowStatus},
	      	data: entryLines
		}).then(function successCallback(response) {
  			var retData = response.data;
  			entryLine.workflowStatus = workflowStatus;
  			
 			$scope.page.refresh();

	        var url = "inbox/entryline/workflow/history";
	        $http({
	        	method: 'GET',
	        	url: url,
		      	  byPassSpinner: true,
	        	params : {entryNumber: entryLine.entryNumber, entryLineNumber: entryLine.entryLineNumber}
	        }).then(function successCallback(response) {
	        	$scope.entrylineHistory = response.data;
		    }, function errorCallback(response) {
		    	console.log("check");
		    });
		}, function errorCallback(response) {
			console.log(response);
			$q.reject(response);
		});
    },

    $scope.getWorkflowNavigation = workflowService.getWorkflowNavigation;
    
    $scope.investigatorAction = {
    		isEdit: false,
    		entryLine: {},
    		edit:  function(entryLine) {
    	    	console.log(entryLine);
    	    	this.getInvestigators();
    	    	this.isEdit = true;
    	    	this.entryLine = entryLine;
    		},
    		
    		isThisEntryLine: function (entryLine) {
    			if(this.entryLine === entryLine) {
    				return true;
    			} else {
    				return false;
    			}
    		},
    		
    		assign: function(option) {
    			// API call to save 
        	    this.updateInvestigator(this.entryLine.entryNumber, this.entryLine.entryLineNumber, this.entryLine.investigatorUsername);
    			
    			this.entryLine.investigatorLastName = this.entryLine.investigatorUsername;
    			for(var i=0;i< $scope.investigators.length; i++) {
    				if($scope.investigators[i].username === this.entryLine.investigatorUsername) {
    	    			this.entryLine.investigatorLastName = $scope.investigators[i].lastname;
    	    			this.entryLine.investigatorFirstName = $scope.investigators[i].firstname;
    					break;
    				}
    			}
    			this.isEdit = false;
    			this.entryLine = {};
    		},
    		
    		unAssign: function() {
    			this.entryLine.investigatorLastName = "";
    			this.entryLine.investigatorFirstName = "";
    			this.entryLine.investigatorUsername = "";

    			// API call to save 
        	    this.updateInvestigator(this.entryLine.entryNumber, this.entryLine.entryLineNumber, this.entryLine.investigatorUsername);

    			this.isEdit = false;
    			this.entryLine = {};
    		},
    		cancel: function() {
    			this.isEdit = false;
    			this.entryLine = {};
    		},
    		
    	    getInvestigators: function() {
    	        $http({
    	            method: 'POST',
    	            url: 'lookup/investigatorList',
    	            params: {
    	                currentUser: Session.userId,
    	                password: Session.password
    	            }
    	        }).then(function successCallback(response) {
    	            console.log('userFormController populateForm()',response);
    	            var users = response.data;
    	            var investigators = [];
    	            users.forEach(function(user, index, users) {
    	            	investigators.push({
    	            			username: user.username, 
    	            			firstname: user.firstname,
    	            			lastname: user.lastname,
    	            			formattedName: utilService.getFormattedName(user.firstname, user.lastname)
    	            	});
    	            });
    	            investigators.sort(function(u1,u2){return u1.formattedName.localeCompare(u2.formattedName)});
    	            $scope.investigators =  investigators;
    	            console.log($scope);
    	        }, function errorCallback(response) {
    	            console.log('Error: userFormController populateForm()',response);
    	            return [];
    	        });
    	    },
    		
    	    updateInvestigator: function(entryNumber, entryLineNumber, investigatorUsername) {
    	        $http({
    	            method: 'POST',
    	            url: 'inbox/entryline/assignInvestigator',
    	            params: {
    	                currentUser: Session.userId,
    	                password: Session.password,
    	    			entryNumber: entryNumber,
    	    			entryLineNumber: entryLineNumber,
    	    			investigator: investigatorUsername,
    	            }
    	        }).then(function successCallback(response) {
    	            console.log('inboxController updateInvestigator()',response);
    	  			growl.addInfoMessage("investigator updated.", {ttl: 5000});
    	  			exam.examErrors = null;
    				return response.data;
      	  	}, function errorCallback(response) {
    				console.log(response);
    	  			growl.addInfoMessage(response.statusText + ". " + response.data, {ttl: 10000});
                    return $q.reject(response);
          	  });
    	    },
    		
    }
    
	$scope.isEntryCancelled = function(entryLine){
    	if(entryLine.workflowStatus == 'CBP CANCELLED') {
    		return true;
    	} else {
    		return false;
    	}
    };
    
    /**
	 * debugging tools
	 */
    $scope.tbd = function(feature) {
    	console.log("feature " + feature + "TBD.");
    };

    $scope.showObject = function(feature) {
    	console.log(feature);
    };

	// support adhoc sort by inbox summary table
	$scope.JpaFieldNameMap = {
//		<th ng-click="sort('entryNumber');">Entry/Line
//		<th ng-click="sort('htsNumber'); "><div >HTS
//		<th ng-click="sort('importerName');">Importer
//		<th ng-click="sort('manufacturerName');">Manufacturer
//		<th ng-click="sort('portCode');">Port
//		<th ng-click="sort('releaseDate');">Release
//		<th ng-click="sort('arrivalDate');">Arrival
//		<th ng-click="sort('ramScore'); ">Score
//		<th ng-click="sort('nationalOp');">Op
//		<th ng-click="sort('workflowStatus'); ">Status
//		<th ng-click="sort('hasExam');">Exam
//		<th ng-click="sort('investigatorLastName');">Investigator
//		<th ng-click="sort('ogcReferral');">OGC
//			protected EntryLine populateEntryLineDto(InboxEntryLineEntity ety) throws IOException {
//					eln.setArrivalDate( DateFormatUtils.format(ety.getEntryArrivalDate(), ApplicationConstants.DateFormats.mmddyyyy) );
//					eln.setReleaseDate( DateFormatUtils.format(ety.getReleaseDate(), ApplicationConstants.DateFormats.mmddyyyy) );
//				eln.setEntryLineNumber( ety.getEntryLineNumber()!=null? ety.getEntryLineNumber().toString() : null );
//				eln.setEntryNumber( ety.getEntryNumber() );
//				eln.setRamScore( (int) Math.round(ety.getRamScore()) );
////			eln.setRamColor( ety.getRamColor() );
//				eln.setHtsNumber( ety.getHts() );
//				eln.setWorkflowStatus( workflowService.findStatusNameByCode(ety.getWorkflowStatusCode()) );
//				eln.setManufacturerName( ety.getManufacturerName() );
//				eln.setPortCode( ety.getPortEntryCode() );
//				eln.setImporterName( ety.getImporterName() );
//				eln.setInvestigatorLastName( ety.getInvestigatorLastName1st() );
//				eln.setInvestigatorFirstName( ety.getInvestigatorFirstName1st() );
//				eln.setFilerCode( ety.getFilerCode() );
//				eln.setNationalOp( ety.getNationalOp() );
//				eln.setOgcReferral(ety.getOgcReferral()!=null? ety.getOgcReferral().getId(): null );
//				eln.setOgcReferralStatus(ety.getOgcReferral()!=null? ety.getOgcReferral().getStatus(): null);
//				eln.setHasExam( (ety.getHasExam()!=null) ? true : false );
//				eln.setImporterNumber(ety.getImporterNumber());
//				eln.setMid(ety.getManufacturerNumber());
//				eln.setFilerName(ety.getFilerName());
//				eln.setConsigneeNumber(ety.getConsigneeNumber());
//				eln.setConsigneeName(ety.getConsigneeName());
//				eln.setInvestigatorUsername(ety.getInvestigatorUsername1st());
//				eln.setBillOfLading(ety.getBillOfLading());
//				eln.setInBondNumber(ety.getInBondNumber());
//				eln.setContainerNumber(ety.getContainerNumber());
			entryNumber: "entryNumber",
			htsNumber: "hts",
			investigatorFormatted: "examiner",
			importerName: "importerName",
			importerNumber: "importer",
			manufacturerName: "manufacturerName",
			portCode: "portEntryCode",
			releaseDate: "releaseDate",
			arrivalDate: "entryArrivalDate",
			ramScore: "ramScore",
			ramColor: "ramColor",
			nationalOp: "nationalOp",
			workflowStatus:"workflowStatus", 
			hasExam: "hasExam",
			investigatorLastName: "investigatorLastName1st",
			ogcReferral: "ogcReferral"
	}
	$scope.sort = function(keyname){
		if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
        $scope.sortKey = keyname;   //set the sortKey to the param passed
        $scope.reverse = !$scope.reverse; //if true make it false and vice versa
        $scope.search.tableSort({
        	field: $scope.JpaFieldNameMap[$scope.sortKey],
        	sortDirection:$scope.reverse?"DESC":""
        })
    }	
    
    // call init()
    $scope.init();

}]);

app.controller('iRecentUsrActModalCtrl', ['$scope','$uibModalInstance','user_id','src_scope',function ($scope,$uibModalInstance,user_id,src_scope) {
	$scope.user_id = user_id;
	$scope.act_records = src_scope.recentUserActionsList;
	$scope.src_scope = src_scope;
	
	$scope.closeDialog=function(){
		$uibModalInstance.dismiss('cancel');
	};
	
	$scope.inboxUserActClick=function(histRecord){
			src_scope.search.resetSearch();
			
			src_scope.search.searchForItemList = [];
			var item = {};
				item = { "type":"STRING", "field":"entryNumber", "fieldDisp":"Entry Number",
						"op":"=", "opDisp":"=", 
						"value":histRecord.entryNumber, "valueDisp":histRecord.entryNumber, 
						"concat":"" };
				if( src_scope.search.validateCriteria(item) ) {
					src_scope.search.searchForItemList.push(item);
				}
				
				item = { "type":"FLOAT", "field":"entryLineNumber", "fieldDisp":"Entry Line Number",
						"op":"=", "opDisp":"=", 
						"value":histRecord.entryLineNumber, "valueDisp":histRecord.entryLineNumber, 
						"concat":" and " };
				if( src_scope.search.validateCriteria(item) ) {
					src_scope.search.searchForItemList.push(item);
				}
				
				src_scope.search.doAdvSearch();
				
				$scope.closeDialog();
	};
}]);

//$(function () {
//	s1 = [[0, 0], [0, 0], [0, 0], [0, 0]];
//	try {
//		plot1 = $.jqplot("chartdiv", [s1], {
//			seriesDefaults:{
//				linePattern: 'dashed',
//				showMarker: false,
//				shadow: false
//			}
//		});
//	}
//	catch (e) {
////		console.log('Error creating plot1 on chartdiv.  Probably the element chartdiv has not been created yet.  This should be moved to the event that puts chartdiv into the dom');
////		console.log(e.message);
//	}
//});

console.log ("Done loading inbox.js");


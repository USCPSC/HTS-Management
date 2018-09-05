/**
 * 
 */

app.factory('searchService', ['$http','$q','SEARCH_COLLECTIONS','utilService', 'growl',
                              function ($http, $q, SEARCH_COLLECTIONS,utilService, growl) {
	
	console.log("loading searchService");
	
	var svc = {};
	svc.sdIsFromDb = true; // true when search is from saved search (e.g., default search when load UI)
	svc.sdIsEdited = false; // true when user clicks add or delete the search criteria
	svc.sdDisjunction = "AND"; // default all criteria is connected using AND
	svc.collections = {};

	// reset collections
	svc.resetSavedSearchCollection = function () {
		console.log("into resetSavedSearchCollection");
		svc.currentSearchDefinition = {};
		svc.sdDisjunction = "AND";
/*		
		for( tgt in SEARCH_COLLECTIONS ) {
			for( avail in SEARCH_COLLECTIONS[tgt] ) {
				console.log("reset collection >>>" + SEARCH_COLLECTIONS[tgt][avail] + "<<<");
				svc[SEARCH_COLLECTIONS[tgt][avail]] = {}; // nullify
				svc[SEARCH_COLLECTIONS[tgt][avail]] = svc.getSavedSearchCollection( tgt, avail );
			}
		}
*/		
	};

	svc.saveSearchDefinition = function( sd) {
		console.log("into saveSearchDefinition");
		var url = "search/save/searchdefinition";
		return $http({ 
				method: 'POST',
				url: url ,
		      	  byPassSpinner: true,
				data: sd
			}).then(function successCallback(response) {
	  			growl.addInfoMessage("Search saved", {ttl: 5000});
			    return response; 
			}, function errorCallback(response) {
	  			//growl.addInfoMessage("Failed to save search", {ttl: 5000});
	  			growl.addInfoMessage(response.data, {ttl: 5000});
                return $q.reject(response);
		});
	};
	
	svc.deleteSearchDefinition = function( sd) {
		console.log("into deleteSearchDefinition");
		var url = "search/delete/searchdefinition";
		return $http({ 
				method: 'POST',
				url: url ,
		      	  byPassSpinner: true,
				data: sd
			}).then(function successCallback(response) {
	  			growl.addInfoMessage("Search deleted", {ttl: 5000});
			    return response; 
			}, function errorCallback(response) {
	  			//growl.addInfoMessage("Failed to save search", {ttl: 5000});
	  			growl.addInfoMessage(response.data, {ttl: 5000});
                return $q.reject(response);
		});
	};

	// default getting INBOX PRIMARY collections
	svc.getSavedSearchCollection = function( target, availability, username) {
		console.log("into getSavedSearchCollection");
		target = target.toLowerCase() || "inbox";
		availability = availability.toLowerCase() || "primary";
		var url = "search/searchDefinitionList";
		return $http({ 
				method: 'POST',
				url: url ,
		    	params: {
		    		target: target, 
		    		availability: availability,
		    		username: username
		    		}
			}).then(function successCallback(response) {
				//svc[SEARCH_COLLECTIONS[target][avail]] = response.data;
			    return response; 
			}, function errorCallback(response) {
	  			growl.addInfoMessage(response.data, {ttl: 5000});
//		    	this.message = response.status + " - " + response.statusText;
//		    	console.log("ouch! >>>" + this.message + "<<<");
                return $q.reject(response);
		});
	};

    svc.doAdvSearch = function(sd, startIndex, numberOfRecords) {
		//var url = "exams";
		var url = "search/" + sd.searchTarget.toLowerCase();
//		var searchFor = {
//				action: "SEARCH",
//				searchCriteriaList:[]
//		}
		// {"action":"SEARCH","searchCriteriaList":[{"type":"STRING","field":"entryNumber","fieldDisp":"Entry Number","op":"LIKE","opDisp":"LIKE","value":"1","valueDisp":"1","concat":""}]}

	    return $http({
	    	  method: 'POST',
	    	  url: url,
	    	  params: {startIndex: startIndex, numberOfRecords: numberOfRecords},
	          data: sd
	    	}).then(function successCallback(response) {
				console.log(response);
				return response;
	    	  }, function errorCallback(response) {
	    		  	console.log(response);
	                return $q.reject(response);
	    		  	
	    	  });
    }
    	
	// name for visual display, ql for query language
	svc.createForm = function( targetedCollection ) {
		var searchForm = {};

		// common elements
		searchForm.comparisonOp = {};
		searchForm.comparisonOp.text = 
			[
			 {id:"eq",name:"=",ql:"="}
			,{id:"ne",name:"!=",ql:"<>"}
			,{id:"lt",name:"<",ql:"<"}
			,{id:"gt",name:">",ql:">"}
			,{id:"nnull",name:"Has Value",ql:"IS NOT NULL"}
			,{id:"in",name:"In",ql:"IN"}
			,{id:"null",name:"Is Empty",ql:"IS NULL"}
			,{id:"like",name:"Like",ql:"LIKE"}
			,{id:"nlike",name:"Not Like",ql:"NOT LIKE"}
			];
		searchForm.comparisonOp.text_wo_in = 
			[
			 {id:"eq",name:"=",ql:"="}
			,{id:"ne",name:"!=",ql:"<>"}
			,{id:"nnull",name:"Has Value",ql:"IS NOT NULL"}
			,{id:"null",name:"Is Empty",ql:"IS NULL"}
			,{id:"like",name:"Like",ql:"LIKE"}
			,{id:"nlike",name:"Not Like",ql:"NOT LIKE"}
			];
		searchForm.comparisonOp.text_wo_ordering = 
			[
			 {id:"eq",name:"=",ql:"="}
			,{id:"ne",name:"!=",ql:"<>"}
			,{id:"nnull",name:"Has Value",ql:"IS NOT NULL"}
			,{id:"in",name:"In",ql:"IN"}
			,{id:"null",name:"Is Empty",ql:"IS NULL"}
			,{id:"like",name:"Like",ql:"LIKE"}
			,{id:"nlike",name:"Not Like",ql:"NOT LIKE"}
			];
		searchForm.comparisonOp.text_has_ogc = 
			[
			{id:"nnull",name:"Has Value",ql:"IS NOT NULL"}
			,{id:"null",name:"Is Empty",ql:"IS NULL"}
			];
		searchForm.comparisonOp.number = 
			[
			 {id:"eq",name:"=",ql:"="}
			,{id:"ne",name:"!=",ql:"<>"}
			,{id:"lt",name:"<",ql:"<"}
			,{id:"gt",name:">",ql:">"}
			,{id:"nnull",name:"Has Value",ql:"IS NOT NULL"}
			,{id:"in",name:"In",ql:"IN"}
			,{id:"null",name:"Is Empty",ql:"IS NULL"}
			];
		searchForm.comparisonOp.float_ram_score = 
			[
			{id:"lt",name:"<",ql:"<"}
			,{id:"gt",name:">",ql:">"}
			,{id:"nnull",name:"Has Value",ql:"IS NOT NULL"}
			,{id:"null",name:"Is Empty",ql:"IS NULL"}
			];
		searchForm.comparisonOp.date = 
			[
			{id:"eq",name:"=",ql:"="}
			,{id:"ne",name:"!=",ql:"<>"}
			,{id:"lt",name:"<",ql:"<"}
			,{id:"gt",name:">",ql:">"}
			,{id:"nnull",name:"Has Value",ql:"IS NOT NULL"}
			,{id:"null",name:"Is Empty",ql:"IS NULL"}
			];
		// store true as 1 and false as 0 in data store
		searchForm.comparisonOp.boolean = 
			[
			{id:"yes",name:"Yes",ql:"= 1"} // "\u2713"
			,{id:"no",name:"No",ql:"= 0"} // "\u2718"
			];
		searchForm.comparisonOp.drop_down_list = 
			[
			 {id:"eq",name:"=",ql:"="}
			,{id:"ne",name:"!=",ql:"<>"}
			,{id:"nnull",name:"Has Value",ql:"IS NOT NULL"}
			,{id:"null",name:"Is Empty",ql:"IS NULL"}
			//,{id:"in",name:"In",ql:"IN"}
			//,{id:"in",name:"Not In",ql:"NOT IN"}
			];
		searchForm.comparisonOp.workflowStatus = 
			[
			 {id:"eq",name:"=",ql:"="}
			,{id:"ne",name:"!=",ql:"<>"}
			//,{id:"nnull",name:"Has Value",ql:"IS NOT NULL"}
			//,{id:"null",name:"Is Empty",ql:"IS NULL"}
			//,{id:"in",name:"In",ql:"IN"}
			//,{id:"in",name:"Not In",ql:"NOT IN"}
			];
		searchForm.comparisonOp.nationalOp = 
			[
			 {id:"eq",name:"=",ql:"="}
			,{id:"ne",name:"!=",ql:"<>"}
			,{id:"nnull",name:"Has Value",ql:"IS NOT NULL"}
			,{id:"null",name:"Is Empty",ql:"IS NULL"}
			//,{id:"in",name:"In",ql:"IN"}
			//,{id:"in",name:"Not In",ql:"NOT IN"}
			];
		searchForm.concatnaterOp = 
			[
			{id:"and",name:"and",ql:"and"}
			,{id:"or",name:"or",ql:"or"}
			];
		// work flow status list
		searchForm.workflowStatusList = 
			[
	         {id:"NR",name:"Scored",value:"Scored"}
			,{id:"CBPRQ",name:"CBP Hold Requested",value:"CBP Hold Requested"}
			,{id:"CBPHA",name:"CBP Hold Approved",value:"CBP Hold Approved"}
			,{id:"CBPHR",name:"Hold Rejected",value:"Hold Rejected"}
			,{id:"PRPST",name:"Entry Released",value:"Entry Released"}
			,{id:"ELRL",name:"Examined & Released",value:"Examined & Released"}
			,{id:"IMRF",name:"Sampled & Detained",value:"Sampled & Detained"}
			,{id:"SCR",name:"Sampled & Conditionally Released",value:"Sampled & Conditionally Released"}
			,{id:"MPROC",name:"May Proceed",value:"May Proceed"}
			,{id:"RFF",name:"Refer to Field",value:"Refer to Field"}
			,{id:"RQRD",name:"Request Redelivery",value:"Request Redelivery"}
			,{id:"NOPHY",name:"Release w/o Phys Exam (other)",value:"Release w/o Phys Exam (other)"}
			,{id:"PRIRE",name:"CBP Released (Post Approval)",value:"CBP Released (Post Approval)"}
			,{id:"CIUNV",name:"CI Unavailable",value:"CI Unavailable"}
			,{id:"DOCRV",name:"CPSC Doc Review",value:"CPSC Doc Review"}
			,{id:"CANCELLED",name:"CBP CANCELLED",value:"CBP CANCELLED"}
			];
		// national operation list
/*		code      name
		IS0500   Supply Chain Program (SI)
		IS1000   First Time Occurrences
		IS0588   Source Intelligence
		IS0023   Injunction Support
		IS0399   Importer Case Support
		IS1000 (SI)           First Time Occurrences (SI)
		IS0588 (SI)           Source Intelligence (SI)
	*/	
		searchForm.nationalOpList = 
			[
	         {id:"IS0500",name:"Supply Chain Program (SI)",value:"IS0500"}
			,{id:"IS1000",name:"First Time Occurrences",value:"IS1000"}
			,{id:"IS0588",name:"Source Intelligence",value:"IS0588"}
			,{id:"IS0023",name:"Injunction Support",value:"IS0023"}
			,{id:"IS0399",name:"Importer Case Support",value:"IS0399"}
			,{id:"IS1000 (SI)",name:"First Time Occurrences (SI)",value:"IS1000 (SI)"}
			,{id:"IS0588 (SI)",name:"Source Intelligence (SI)",value:"IS0588 (SI)"}
			];
		searchForm.ogcStatusList = 
			[
	         {id:"Closed",name:"Closed",ql:"="}
			,{id:"In Progress",name:"In Progress",ql:"="}
			,{id:"Monitoring",name:"Monitoring",ql:"="}
			,{id:"Referred",name:"Referred",ql:"="}
			];

		searchForm.dispositionList = 
			[
			 {id:"DESTRUCT",name:"Released for Destruction",ql:"IN",value:"DESTRUCT"}
			,{id:"EXPORT",name:"Released for Export",ql:"IN",value:"EXPORT"}
			,{id:"LOACFP",name:"Released with LOA-CFP",ql:"IN",value:"LOACFP"}
			,{id:"OTHER",name:"Other - See Investigator Remarks",ql:"IN",value:"OTHER"}
			,{id:"PENDING",name:"Sample Pending Evaluation",ql:"IN",value:"PENDING"}
			,{id:"RECONDIT",name:"Released for Reconditioning",ql:"IN",value:"RECONDIT"}
			,{id:"RELEASE",name:"Released",ql:"IN",value:"RELEASE"}
			,{id:"SEIZE",name:"Seize",ql:"IN",value:"SEIZE"}
			];
		
		// specific per target
		// id for tracking, name for display, type for op selection
		searchForm.searchingFieldsList = {};
		switch( targetedCollection.toUpperCase() ) {
			case 'EXAMLOG':
				searchForm.searchingFieldsList = [
				                   		       {id:'entryNumber', name:'Entry Number', type:'STRING'}
				                 			  ,{id:'IMPORTER_NUMBER', name:'Importer Number', type:'STRING'}
				                 			  ,{id:'IMPORTER_NAME', name:'Importer Name', type:'STRING'}
				          					  ,{id:"importerAddress",name:"Importer Address",type:"STRING"}
				                 		      ,{id:'ENTRY_PORT_CODE', name:'Entry Port Code', type:'STRING'}
				                 		      ,{id:'ENTRY_PORT_NAME', name:'Entry Port Name', type:'STRING'}
				                 		      ,{id:"activityDate",name:"Exam Date",type:"DATE"}
				                 		      ,{id:'EXAMINER', name:'Investigator Name', type:'STRING'}
				                 		      ,{id:'EXAMINER2', name:'Investigator (Secondary) Name', type:'STRING'}
				                 		      ,{id:'HAS_SAMPLE', name:'Exam with Sample', type:'BOOLEAN'}
				                   		      ,{id:'sampleNumber', name:'Sample Number', type:'STRING'}
				                   		      ,{id:'itemModel', name:'Model/Item Number', type:'STRING'}
				                   		      ,{id:'dispositionCode', name:'Disposition', type:'DROP_DOWN_LIST'}
					    					  ,{id:"trackLabelMissing",name:"Track Label Missing",type:"BOOLEAN"}
					       					  ,{id:"brokerNotified",name:"Broker Notified",type:"BOOLEAN"}
					       					  //  ,{id:"createTimestamp",name:"Exam Create Timestamp",type:"DATE"}
					       					  ,{id:"examCreateDate",name:"Exam Create Date",type:"DATE"} //gz+					    					 
				              				];
				
				break;
			case 'INBOX':
			default:
				searchForm.searchingFieldsList = [
					// entryline
					{id:"entryNumber",name:"Entry Number",type:"STRING"}
					,{id:"hts",name:"HTS Number",type:"STRING"}
//					,{id:"ramScore",name:"RAM Score",type:"FLOAT_RAM_SCORE"}
					,{id:"ramScore",name:"RAM Score",type:"FLOAT_RAM_SCORE"}
					,{id:"portEntryCode",name:"Port Entry Code",type:"STRING"}
//					,{id:"entryDate",name:"Entry Date",type:"DATE"}
//					,{id:"entryValue",name:"Entry Value",type:"FLOAT"}
					,{id:"entryCreateDate",name:"Entry Create Date",type:"DATE"}
					//,{id:"entryCreateTimestamp",name:"Entry Create Timestamp",type:"DATE"}
					,{id:"entryArrivalDate",name:"Entry Arrival Date",type:"DATE"}
					,{id:"releaseDate",name:"Entry Release Date",type:"DATE"}
					,{id:"hasExam",name:"Has Exam",type:"BOOLEAN"}
					,{id:"investigatorFirstName",name:"Investigator First Name",type:"STRING_WO_ORDERING"}
					,{id:"investigatorLastName",name:"Investigator Last Name",type:"STRING_WO_ORDERING"}
					,{id:"billOfLading",name:"Bill of Lading",type:"STRING"}
					,{id:"containerNumber",name:"Container Number",type:"STRING"}
					,{id:"ogcReferral",name:"OGC Referral",type:"STRING_HAS_OGC"} // was BOOLEAN
					// importer
					,{id:"importerNumber",name:"Importer Number",type:"STRING"}
					,{id:"importerName",name:"Importer Name",type:"STRING"}
					,{id:"importerAddress",name:"Importer Address",type:"STRING"}
//					,{id:"importerPhone",name:"Importer Phone",type:"STRING"}
//					,{id:"importerState",name:"Importer State",type:"STRING"}
//					,{id:"importerZip",name:"Importer Zip",type:"STRING"}
					// manufacturer
					,{id:"manufacturerNumber",name:"Manufacturer Number",type:"STRING"}
					,{id:"manufacturerName",name:"Manufacturer Name",type:"STRING_WO_IN"}
//					,{id:"manufacturerPhone",name:"Manufacturer Phone",type:"STRING"}
//					,{id:"manufacturerAddress",name:"Manufacturer Address",type:"STRING"}
//					,{id:"manufacturerState",name:"Manufacturer State",type:"STRING"}
//					,{id:"manufacturerZip",name:"Manufacturer Zip",type:"STRING"}
					// filer
//					,{id:"filerNumber",name:"Filer Number",type:"STRING"}
					,{id:"filerName",name:"Filer Name",type:"STRING_WO_IN"}
					,{id:"filerCode",name:"Filer Code",type:"STRING"}
//					,{id:"filerPhone",name:"Filer Phone",type:"STRING"}
//					,{id:"filerAddress",name:"Filer Address",type:"STRING"}
//					,{id:"filerState",name:"Filer State",type:"STRING"}
//					,{id:"filerZip",name:"Filer Zip",type:"STRING"}
					// consignee
					,{id:"consigneeNumber",name:"Consignee Number",type:"STRING"}
					,{id:"consigneeName",name:"Consignee Name",type:"STRING_WO_IN"}
//					,{id:"consigneePhone",name:"Consignee Phone",type:"STRING"}
//					,{id:"consigneeAddress",name:"Consignee Address",type:"STRING"}
//					,{id:"consigneeState",name:"Consignee State",type:"STRING"}
//					,{id:"consigneeZip",name:"Consignee Zip",type:"STRING"}
					
					//,{id:"nationalOp",name:"National Operation Code",type:"STRING"}
  					,{id:"nationalOp",name:"National Operation",type:"DROP_DOWN_LIST"}
  					,{id:"workflowStatus",name:"Work Flow Status",type:"WORKFLOW_STATUS"}
  					,{id:"ogcReferralStatus",name:"OGC Referral Status",type:"OGC_REF_STATUS"}
					,{id:"value",name:"VALUE",type:"STRING"}
					,{id:"lastUpdateUserId",name:"Last Update User ID",type:"STRING"}
				];
		};

    	searchForm.comparisonOp.text.sort( utilService.sortArrayBy( 'name', false, function(a){return a.toUpperCase()}));
    	searchForm.comparisonOp.number.sort( utilService.sortArrayBy( 'name', false, function(a){return a.toUpperCase()}));
    	searchForm.comparisonOp.date.sort( utilService.sortArrayBy( 'name', false, function(a){return a.toUpperCase()}));
    	searchForm.comparisonOp.boolean.sort( utilService.sortArrayBy( 'name', false, function(a){return a.toUpperCase()}));
    	searchForm.concatnaterOp.sort( utilService.sortArrayBy( 'name', false, function(a){return a.toUpperCase()}));
    	searchForm.searchingFieldsList.sort( utilService.sortArrayBy( 'name', false, function(a){return a.toUpperCase()}));
    	
		return searchForm;
	};

	return svc;
}]);

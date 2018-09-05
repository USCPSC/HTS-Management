/**
 *  Examlog Services
 */

app.factory('examlogService', ['$http','$q', 'growl', 'utilService', 'Session', function ($http, $q, growl, utilService, Session) {
    examlogService = {}
    ,examlogService = []
    ,currentExamIndex = null
    ,currentSampleIndex = null
    ,noReadingItem = true
    ,
    
    /*
     * call REST API to fetch examlog configuration parameters
     */
    examlogService.init = function($scope) {
		var url = "properties/ifs";
	    $http({
	    	  method: 'POST',
	    	  url: url,
	    	}).then(function successCallback(response) {
				console.log(response);
				examlogService.parameters = response.data;
	    	  }, function errorCallback(response) {
	    		  	console.log(response);
	                return $q.reject(response);
	    	  });
    },

    /*
     * examlog configuration parameters
     */
    examlogService.getParameters = function() {
    	return examlogService.parameters;
    },

    /*
     * call REST API to fetch exam list and store data at $scope.exams
     */
    examlogService.getExamList = function(startIndex, numberOfRecords) {
		//var url = "exams";
		var url = "search/examlog";
		var searchFor = {
				action: "SEARCH",
				searchCriteriaList:[]
		}
		// {"action":"SEARCH","searchCriteriaList":[{"type":"STRING","field":"entryNumber","fieldDisp":"Entry Number","op":"LIKE","opDisp":"LIKE","value":"1","valueDisp":"1","concat":""}]}

	    return $http({
	    	  method: 'POST',
	    	  url: url,
	    	  params: {startIndex: startIndex, numberOfRecords: numberOfRecords},
	          data: searchFor
	    	}).then(function successCallback(response) {
				console.log(response);
				return response;
	    	  }, function errorCallback(response) {
	    		  	console.log(response);
	                return $q.reject(response);
	    		  	
	    	  });
    },
    
	/*
	 * call REST API to fetch exam details and replace the version on the summary list
	 */
    examlogService.getExamDetail = function (entryNumber) {
  	        var url = "exams/" + entryNumber;
  	        return $http({
  	      	  method: 'POST',
  	      	  url: url
  	      	}).then(function successCallback(response) {
  	  				console.log(response);
  	  				return  response.data;
  	      	  }, function errorCallback(response) {
  	  				console.log(response);  //Object {data: "", status: 404, config: Object, statusText: "Not Found"}
                    if(response.status == 404) {
	  	  				growl.addInfoMessage("exam record for " + entryNumber + " not found.", {ttl: 5000});
                    } else {
	  	  				//growl.addInfoMessage(response.statusText, {ttl: 5000});
	  					//growl.addInfoMessage(response.statusText + ". " + response.data, {ttl: 10000});
	  					growl.addInfoMessage("Error: " + response.data, {ttl: 10000});
                    }
                    return $q.reject(response);
  	      	  });

    },

    /*
	 * call REST API to prepopulate exam details from entry line
	 */
    examlogService.prepopulateExamDetail = function (entryNumber, entryLineNumber) {
	        var url = "exams/prepopulateExam/" + encodeURIComponent(entryNumber) + "/" + encodeURIComponent(entryLineNumber);
	        return $http({
	      	  method: 'POST',
	      	  url: url
	      	}).then(function successCallback(response) {
	  				console.log(response);
	  				return  response.data;
	      	  }, function errorCallback(response) {
	  				console.log(response); 
                if(response.status == 404) {
  	  				//growl.addInfoMessage("entry line record for " + entryNumber + " not found.", {ttl: 5000});
  	  				return $q.reject("entry line record for " + entryNumber + " not found.");
                } else {
  					//growl.addInfoMessage("Error: " + response.data, {ttl: 10000});
  	  				return $q.reject("Error: " + response.data);
                }
                return $q.reject(response);
	      	  });

    },
    
    examlogService.validateExamEntryNumber = function (entryNumber, entryLineNumber, entryExists) {
  	        var url = "exams/validateExamEntryNumber?entryNumber=" + encodeURIComponent(entryNumber) + 
  	        		"&entryLineNumber=" + encodeURIComponent(entryLineNumber) + 
  	        		"&entryExists=" + encodeURIComponent(entryExists);
  	        return $http({
  	      	  method: 'POST',
  	      	  url: url
  	      	}).then(function successCallback(response) {
  	  				console.log(response);
  	  				return  response.data;
  	      	  }, function errorCallback(response) {
  	  				console.log(response); 
	  	  			return $q.reject("Error: " + response.data);
  	      	  });

    },
    
    examlogService.validateUnkownExamEntryNumber = function (entryNumber) {
	        var url = "exams/validateUnkownExamEntryNumber?entryNumber=" + encodeURIComponent(entryNumber);
	        return $http({
	      	  method: 'POST',
	      	  url: url
	      	}).then(function successCallback(response) {
	  				console.log(response);
	  				return  response.data;
	      	  }, function errorCallback(response) {
	  				console.log(response); 
  	  			return $q.reject("Error: " + response.data);
	      	  });

},
    
    
    examlogService.setupEditForm = function($scope) {
		var examCopy = angular.copy($scope.exam);
		$scope.examCopy = examCopy;
		
		examCopy.examDate_edit = examCopy.examDate;
		//examCopy.examDate_edit = new Date(examCopy.examDate);
		examCopy.investigator_edit = examCopy.investigator;
		examCopy.investigator2_edit = examCopy.investigator2;
		examCopy.remarks_edit = examCopy.remarks;
		examCopy.portCode_edit = examCopy.portCode;
		examCopy.activityTime_edit = Number(examCopy.activityTime);
		examCopy.travelTime_edit = Number(examCopy.travelTime);
		examCopy.ifsAssignmentId_edit = examCopy.ifsAssignmentId;
		examCopy.labelMissingIncomplete_edit = (examCopy.labelMissingIncomplete == "Yes")?true:false;
		examCopy.brokerImporterNotified_edit = (examCopy.brokerImporterNotified == "Yes")?true:false;

		examCopy.importerNumber_edit = examCopy.importerNumber;
		examCopy.importerName_edit = examCopy.importerName;
		
		$scope.exam_workflowStatus =""; // for new workflowstatus update
		

		console.log($scope);
	},
    
	/*
	 * change entryline work flow status batch updates
	 */
	examlogService.processBatchWorkflowStatus = function (examEntryLines, workflowStatus) {
		var entryLines = [];
		for (var i = 0; i < examEntryLines.length; i++) {
			if (examEntryLines[i].isChecked) {
				entryLines.push({
					"entryNumber": examEntryLines[i].entryNumber,
					"entryLineNumber": examEntryLines[i].entryLineNumber
				})
			}
		}
		var url = "inbox/updateWorkflowStatus";
		return $http({
			method: 'POST',
			url: url,
			params: {workflowStatus: workflowStatus},
			data: entryLines
		}).then(function successCallback(response) {
			var retData = response.data;
  			growl.addInfoMessage("Workflow status updated.", {ttl: 5000});
			return response.data;
		}, function errorCallback(response) {
  			//growl.addInfoMessage("Workflow status update failed.", {ttl: 5000});
				//growl.addInfoMessage(response.statusText, {ttl: 5000});
				growl.addInfoMessage("Workflow status update failed: " + response.data, {ttl: 10000});
			console.log(response);
            return $q.reject(response);
			
		});
	},
	
	// prep reasonTypesAvailable from all-n reasonTypes
	examlogService.prepReasonTypesAvailable = function( sample ) {
		
//		console.log("examlog_services.js::examlogService::prepReasonTypesAvailable");
//		console.log("this", this);
//		console.log("sample", sample);
//		console.log("reasonTypes", examlogService.reasonTypes);

		var available = angular.copy(examlogService.reasonTypes);
		
		if(sample.reasons) {
			for(var i=0; i<sample.reasons.length; i++) {
				for(var j=0; j<available.length; j++) {
					if( sample.reasons[i].reasonCode === available[j].reasonCode ) {
						available.splice(j, 1);
						continue;
					}
				}
			}
		}
//		else {
//			// prep reasonsForScreening as empty array
//			sample.reasonsForScreening = [];
//		}
		return available;
	},

	// update exam details
    examlogService.saveExam = function (exam) {

		var me = this;
		var newReasons = [];
		var sc = utilService.getControllerScope("sampleTableController");

		var url = "exams/save";
		return $http({
			method: 'POST',
			url: url,
			data: exam
		}).then(function successCallback(response) {
			console.log(response);
			growl.addInfoMessage("exam record saved.", {ttl: 5000});
			exam.examErrors = null;
			return response.data;
		}, function errorCallback(response) {
			console.log(response);
			if(response.status == 400) { // validation errors
				exam.examErrors = response.data;
				exam.examErrors.message = "Please correct following error(s):";
			} else if(response.status == 404) {
				growl.addInfoMessage("exam record for " + entryNumber + " not found", {ttl: 10000});
			} else {
				growl.addInfoMessage(response.statusText + ". " + response.data, {ttl: 10000});
			}
			return $q.reject(response);
		});
	},


	examlogService.prepareIfsSample = function (sample) {
		var url = 'exams/prepareIfsSample';
		var ifsUrl = atob(examlogService.getParameters().ifs_create_sample_url_base64);

		var form = document.createElement("form");
		form.method = "POST";
		form.action = url;   
		form.target = "IFS";
    	    
		var element = document.createElement("input"); 
		element.name="sampleId";
		element.value=sample.productSampleId;
		form.appendChild(element);  
		document.body.appendChild(form);

		element = document.createElement("input"); 
		element.name="ifsUrl";
		element.value=ifsUrl;
		form.appendChild(element);  
		document.body.appendChild(form);

		form.submit();
		form.parentNode.removeChild(form);
    	    
	},

	examlogService.addSampleIFS = function (ifsSample) {
		//var url = 'exams/echo';
		var url = examlogService.getParameters().ifs_create_sample_url;

		var form = document.createElement("form");
		form.method = "POST";
		//form.method = "GET";
		form.action = url;   
		form.target = "IFS";
		
		for (var property in ifsSample) {
			if (ifsSample.hasOwnProperty(property) && !property.startsWith('$$')) {
				var element1 = document.createElement("input"); 
				element1.value=ifsSample[property];
				element1.name=property;
				form.appendChild(element1);  
			}
		}    	    
		document.body.appendChild(form);
		
		form.submit();
		//document.removeChild(form);
		form.parentNode.removeChild(form);
		    
	},

	examlogService.uploadFile = function (file) {
		var formData = new FormData();
		formData.append("file", file);
 
		return $http({
			url: 'exams/uploadExamDocument',
			method: 'POST',
			data: formData,
			transformRequest: angular.identity,
			headers: {'Content-Type': undefined}
		}).then(function successCallback(response) {
			console.log(response);
			return response.data;
		}, function errorCallback(response) {
			console.log(response);
			return $q.reject(response);
		});

		return false;
	},

	// helper to build importer search
	
	examlogService.formatDate = function (date1) {
		var mm = date1.getMonth()+1;
		var dd = date1.getDate();
		var yyyy = date1.getFullYear();
		if(mm<10) {
			mm = "0" + mm;
		}
		if(dd<10) {
			dd = "0" + dd;
		}
		//return yyyy+"/" + mm +  "/" + dd;
		return mm +"/" + dd +  "/" + yyyy;
	},
	
	examlogService.buildSd_importer = function (impNo, date1, withSample) {
		date1 = examlogService.formatDate(date1);
		var sd = {
				action: "SEARCH",
				searchTarget: "EXAMLOG",
				searchName: "Importer exams in last 2 weeks",
				searchAvailability:"private",
				
				searchCriteriaList: [
	                 {
	                		type: "STRING",
	                		concat: "", // null if only one or first one in the searchFor list 
	                		field: "importer",// JPA attribute name
	                		fieldDisp: "Importer Number",// user friendly field name
	                		op:  "=", // LIKE, =, <, >, <>
	                		opDisp: " = ",// user friendly operator name
	                		value: impNo,
	                		valueDisp: impNo
	                 }
	                 ,
	                 
	                 {
	                		type: "DATE",
	                		concat: " AND ",  
	                		field: "activityDate",
	                		fieldDisp: "Exam Date",
	                		op:  ">", 
	                		opDisp: " > ",
	                		value: date1,
	                		valueDisp: date1.toString()
	                 }
				 ],
				 orderBys: [ {
			        	field: "activityDate", 
			        	fieldDisp: "Exam Date",
			        	sortRank: 1, 
			        	sortDirection: "DESC",
			        	sortDisp: "DESC"
				 		}
				        	
				 ]
					
				};
		
			if(withSample) {
				sd.searchCriteriaList.push({
	                		type: "STRING",
	                		concat: " AND ", // null if only one or first one in the searchFor list 
	                		field: "hasSample",// JPA attribute name
	                		fieldDisp: "Exam With Sample",// user friendly field name
	                		op:  "=", // LIKE, =, <, >, <>
	                		opDisp: " = ",// user friendly operator name
	                		value: "1",
	                		valueDisp: "YES"
				})
			}
		return sd;
	},
	
	examlogService.buildSd_importerLast2Yrs = function (impNo) {
		var date1=new Date();
		date1.setYear(date1.getFullYear()-2); // go back 2yrs
		var sd = examlogService.buildSd_importer(impNo,date1);
		sd.searchName = "Importer exams in last 2 years";
		console.log("sd=");
		console.log(sd);
		return sd;
	},

	examlogService.buildSd_importerLast2Wks = function (impNo) {
		var date1=new Date();
		date1.setDate(date1.getDate()-14); // go back 2 weeks
		var sd = examlogService.buildSd_importer(impNo,date1);
		sd.searchName = "Importer exams in last 2 weeks";
		console.log("sd=");
		console.log(sd);
		return sd;
	},

	examlogService.buildSd_importerWithSampleLast2Yrs = function (impNo) {
		var date1=new Date();
		date1.setYear(date1.getFullYear()-2); // go back 2yrs
		var sd = examlogService.buildSd_importer(impNo,date1,"sample");
		sd.searchName = "Importer exams with samples in last 2 years";
		console.log("sd=");
		console.log(sd);
		return sd;
	},

	examlogService.buildSd_importerWithSampleLast2Wks = function (impNo) {
		var date1=new Date();
		date1.setDate(date1.getDate()-14); // go back 2 weeks
		var sd = examlogService.buildSd_importer(impNo,date1,"sample");
		sd.searchName = "Importer exams with sample in last 2 weeks";
		console.log("sd=");
		console.log(sd);
		return sd;
	},
	
	examlogService.init();
	
    return examlogService;
}]);

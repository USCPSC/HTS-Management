/**
 *  Inbox Services
 */

app.factory('inboxService', ['$http','$q', 'growl',function ($http, $q, growl) {
    var inboxService = {};
    
    /*
     * call REST API to fetch Entryline score analysis rule info
     */
    inboxService.getRuleInfo = function (entryNumber, entryLineNumber) {
		var url = "inbox/analysis/ruleInfo";
	    return $http({
	    	  method: 'POST',
	    	  url: url,
	    	  params: {entryNumber: entryNumber, entryLineNumber: entryLineNumber}
	    	}).then(function successCallback(response) {
				console.log(response);
	            return response.data;
	    	  }, function errorCallback(response) {
				  if(response.status == 404) {
		  				growl.addInfoMessage("score analysis record for " + entryNumber + "/" + entryLineNumber + " not found", {ttl: 5000});
	              } else {
		  				growl.addInfoMessage(response.statusText, {ttl: 5000});
	              }
	              return $q.reject(response);
	    		  	
	    	  });
    }
    /*
     * call REST API to fetch Entryline score analysis rule info
     */
    inboxService.getScoreColorList = function (entryNumber, entryLineNumber) {
		var url = "inbox/analysis/scoreColorList";
	    return $http({
	    	  method: 'POST',
	    	  url: url,
	    	  params: {entryNumber: entryNumber, entryLineNumber: entryLineNumber}
	    	}).then(function successCallback(response) {
				console.log(response);
	            return response.data;
	    	  }, function errorCallback(response) {
				  if(response.status == 404) {
		  				growl.addInfoMessage("score analysis record for " + entryNumber + "/" + entryLineNumber + " not found", {ttl: 5000});
	              } else {
		  				growl.addInfoMessage(response.statusText, {ttl: 5000});
	              }
	              return $q.reject(response);
	    		  	
	    	  });
    }

    /*
     * call REST API to fetch Entryline comments list
     */
    inboxService.getCommentList = function (entryNumber, entryLineNumber) {
		var url = "inbox/comments";
	    return $http({
	    	  method: 'POST',
	    	  url: url,
	    	  params: {entryNumber: entryNumber, entryLineNumber: entryLineNumber}
	    	}).then(function successCallback(response) {
				console.log(response);
	            return response.data;
	    	  }, function errorCallback(response) {
				  if(response.status == 400) { // validation errors
						comment.commentErrors = response.data;
						comment.commentErrors.message = "Please correct following error(s):";
				  } else if(response.status == 404) {
		  				growl.addInfoMessage("comment record for " + entryNumber + "/" + entryLineNumber + " not found", {ttl: 5000});
	              } else {
		  				growl.addInfoMessage(response.statusText, {ttl: 5000});
	              }
	              return $q.reject(response);
	    		  	
	    	  });
    }


    /*
     * call REST API to fetch Entryline comments list
     */
    inboxService.prePopulateEntryLineComments = function (entryNumber, entryLineNumber) {
		var url = "inbox/comments/prePopulateEntryLineComments";
	    return $http({
	    	  method: 'POST',
	    	  url: url,
	    	  params: {entryNumber: entryNumber, entryLineNumber: entryLineNumber}
	    	}).then(function successCallback(response) {
				console.log(response);
	            return response.data;
	    	  }, function errorCallback(response) {
				  if(response.status == 400) { // validation errors
						comment.commentErrors = response.data;
						comment.commentErrors.message = "Please correct following error(s):";
				  } else if(response.status == 404) {
		  				growl.addInfoMessage("Entryline record for " + entryNumber + "/" + entryLineNumber + " not found", {ttl: 5000});
	              } else {
		  				growl.addInfoMessage(response.statusText, {ttl: 5000});
	              }
	              return $q.reject(response);
	    		  	
	    	  });
    }

    // update comment details
    inboxService.saveComment = function (comment) {
	    var url = "inbox/comments/create";
	    return $http({
	  	  method: 'POST',
	  	  url: url,
	  	  data: comment
	  	}).then(function successCallback(response) {
				console.log(response);
	  			growl.addInfoMessage("comment record saved.", {ttl: 5000});
				return response.data;
  	  	}, function errorCallback(response) {
				console.log(response);
			  if(response.status == 400) { // validation errors
					comment.commentErrors = response.data;
					comment.commentErrors.message = "Please correct following error(s):";
			  } else if(response.status == 404) {
	  				growl.addInfoMessage("comment record for " + entryNumber + " not found", {ttl: 5000});
              } else {
	  				growl.addInfoMessage(response.statusText, {ttl: 5000});
              }
              return $q.reject(response);
      	  });
    	};

    	
    	inboxService.getImporterSampleList = function(importerNumber, flag, dateAfter) {
        	// flag: VIO(lative), NON(violative), INP(rogress)
    		
    		var url = "inbox/importer/samples";
    		return $http({
    			method: 'POST',
            	url: url,
            	params : {
            		"importer": importerNumber,
            		"dateAfter": dateAfter,
            		"flag":flag
            		},
    		}).then(function successCallback(response) {
    			console.log(response);
                return response.data;
    		}, function errorCallback(response) {
    			console.log(response);
    			$q.reject(response);
    		});
        }
    	
    	
    	
    	
    	
    return inboxService;
}]);

app.factory('nationalOpService', ['$rootScope', '$http', '$q','growl', 
                                  function($rootScope, $http, $q,  growl) {
	var service = {};

	// call REST service to get the list of national operation rules
	service.getNationalOpRules = function() {
		// properties are retrieved by appController.init()
		//var url = $rootScope.ui_properties.ramApiUrlPrefix + $rootScope.ui_properties.ramApiUrlListNationalOpsPartial;
		var url = "inbox/listNationalOps";  // local proxy
	    return $http({
	    	  method: 'POST',
	    	  url: url,
	    	}).then(function successCallback(response) {
				console.log(response);
	            return response.data;
	    	  }, function errorCallback(response) {
		  		  growl.addInfoMessage("ITDS/RAM 2.0 Rule Engine is not reachable", {ttl: 5000});
	              return $q.reject(response);
	    		  	
	    	  });
	}

	service.processNationalOp = function(entryLines, nationalOpId, nationalOpName) {
		var url = "inbox/doNationalOp";
		return $http({
			method: 'POST',
        	url: url,
        	params : {
        		"nationalOpId": nationalOpId,
        		"nationalOpName": nationalOpName
        		},
	      	data: entryLines
		}).then(function successCallback(response) {
			console.log(response);
            return response.data;
		}, function errorCallback(response) {
			console.log(response);
			$q.reject(response);
		});
    }
	

	return service;
} ]);


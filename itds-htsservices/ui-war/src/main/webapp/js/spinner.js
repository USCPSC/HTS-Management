app.factory('spinnerService', [  function() {
	var service = {};

	service.count = 0;
	service.startDelay = 1000; // delay before show spinner
	service.stopDelay = 10; // show at lease this amount of time 
	
	service.reset = function() {
		service.count = 0;
		$('#spinnerDiv').hide();
	}
	
	service.start = function() {
		service.count ++;
    	setTimeout(function(){
    		if(service.count>0) {
    			$('#spinnerDiv').show();
    		}
    	},service.startDelay);

	}
	
	service.stop = function() {
		service.count --;
    	setTimeout(function(){
        	if(service.count <1) {
    			$('#spinnerDiv').hide();	
    		}
    	},service.stopDelay);
		
	}


	return service;
}]);  // app.factory('spinnerService'

//register the interceptor as a service
app.factory('spinnerInterceptor', ['$q','spinnerService',function($q,spinnerService) {
	var httpTimeout = 3*60*1000;
  return {
    'request': function(config) {
    	// do something on success
    	if(!config.byPassSpinner) {
        	config.timeout = httpTimeout;
        	spinnerService.start();
    	}
    	
    	return config;
    },

   'requestError': function(rejection) {
	   	// do something on error
  	if((rejection.config == undefined) || (!rejection.config.byPassSpinner)) {
  		spinnerService.reset();
	}
	   return $q.reject(rejection);
    },



    'response': function(response) {
    	// do something on success
    	if(!response.config.byPassSpinner) {
        	spinnerService.stop();
    	}
    	return response;
    },

   'responseError': function(rejection) {
	   	// do something on error
	   	console.log(rejection);
    	if((rejection.config == undefined) || (!rejection.config.byPassSpinner)) {
    	   	spinnerService.stop();
    	}
	   	return $q.reject(rejection);
    }
  };
}]);


app.config([ '$httpProvider', function($httpProvider) {
	$httpProvider.interceptors.push('spinnerInterceptor');
	$httpProvider.useApplyAsync(true);
}]);
	



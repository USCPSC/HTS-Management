/**
 * Inbox Comments tab
 */

console.log ("loading inbox_comments.js");

app.controller('inboxCommentsController', ['$scope', 'inboxService', 'Session', 'utilService', function($scope, inboxService, Session, utilService) {

	$scope.MAX_COMMENT_SIZE = 2048;
	$scope.commentList=[];
	
	$scope.init = function() {
		$scope.getEntryLineCommentList($scope.detail.selectedEntryLine);
		$scope.prePopulateEntryLineComments($scope.detail.selectedEntryLine);
	}
	
	$scope.getEntryLineCommentList = function(selectedEntryLine) {
		console.log("getEntryLineCommentList");
		console.log(selectedEntryLine);
		var entryNumber = selectedEntryLine.entryNumber;
		var entryLineNumber = selectedEntryLine.entryLineNumber;
		inboxService.getCommentList(entryNumber, entryLineNumber).then(function successCallback(data) {
			console.log(data);
			$scope.commentList = data;
		}, function errorCallback(response) {
			console.log(response);
		});
	}

	$scope.prePopulateEntryLineComments = function(selectedEntryLine) {
		console.log("prePopulateEntryLineComments");
		console.log(selectedEntryLine);
		var entryNumber = selectedEntryLine.entryNumber;
		var entryLineNumber = selectedEntryLine.entryLineNumber;
		inboxService.prePopulateEntryLineComments(entryNumber, entryLineNumber).then(function successCallback(data) {
			console.log(data);
			$scope.newComment = data;
		}, function errorCallback(response) {
			console.log(response);
		});
	}
	
	$scope.saveComment = function(selectedEntryLine, comment) {
		inboxService.saveComment(comment).then(function successCallback(data) {
			console.log(data);
			comment.note = "";
			comment.commentErrors = [];
			$scope.getEntryLineCommentList(selectedEntryLine);
			
  	  	}, function errorCallback(response) {
				console.log(response);
  	  	});
	}

    $scope.charRemaining = function(data, maxLength) {
    	if(data) {
    		var len = data.length;
    		return (maxLength - len>0)?(maxLength - len):0;
    	} 
    	return maxLength;
    };

    
	$scope.init();

}]);

console.log ("Done loading inbox_comments.js");

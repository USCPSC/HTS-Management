app.factory('paginationService', ['navigationService',  function(navigationService) {
	var service = {};

	service.createPage = function(pageSize, bufferSize) {
		var page = {
			maxBufferSize : 1000,
			
			bufferSize: 200,
			bufferStartIndex: -1,  //VM window start
			bufferEndIndex: 0, // VM window end
			queryCallback: function(startIndex, numberOfRecords) {
				//alert("code the callback to get more data!")
				return {
					data: {
						count: 0
					}
				}
				},
	
			pageSize :  10,
			startIndex :  1,
			endIndex :  0,
			currentPage :  0,
			strCurrentPage: "0",
			totalResults :  0,
			totalPages :  0,
			showInput: false,
			
	
			init: function(pageSize, bufferSize) {
				if(bufferSize > this.maxBufferSize) {
					console.log("bufferSize exceed max buffer size. Default to  " + this.maxBufferSize);
					this.bufferSize = this.maxBufferSize;
				} else {
					this.bufferSize = bufferSize;
				}
				
				if(pageSize > this.bufferSize) {
					console.log("page size exceeds buffer size. Default to " +  this.bufferSize);
					this.pageSize = this.bufferSize;
				} else {
					this.pageSize = pageSize;	
				}
				
				var nPages = Math.floor(this.bufferSize / this.pageSize);
				nPages = (nPages <1)?1:nPages;
				
				this.forwardPages = Math.floor((nPages-1)/2.618);
				this.backwardPages = nPages - 1 - this.forwardPages;
				this.backwardPages = (this.backwardPages<0)?0:this.backwardPages;
				if(this.forwardPages < this.backwardPages) {
					// swap
					var tmp = this.backwardPages;
					this.backwardPages = this.forwardPages;
					this.forwardPages = tmp;
				}
				console.log("bufferSize:" + this.bufferSize + ", pageSize:" + this.pageSize + ", forward:" + this.forwardPages + ", backward:" + this.backwardPages);
			},
			
			setPageSize: function(pageSize) {
				this.init(pageSize, this.bufferSize);
				this.calculate();
				this.checkCache();
			},
			
			reset:  function () {
				console.log("page.reset()");
				this.startIndex = 1;
				this.totalResults = 0;

				this.bufferStartIndex = -1;
				this.bufferEndIndex = 0;

				this.calculate();
				this.checkCache();

				console.log(this);
				console.log("page.reset() completed");
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
			
			checkCache: function() {
				// check cache miss
				var me = this;
				if( this.bufferStartIndex <0  || ! (this.startIndex-1 >= this.bufferStartIndex && this.endIndex<= this.bufferEndIndex) ) {
					var idx = this.startIndex - this.pageSize*this.backwardPages -1;
					idx = (idx <0)?0:idx;
					this.queryCallback (idx, this.bufferSize).then(function successCallback(response) {
						me.setup(idx, response.data.count);
					}, function errorCallback(response) {
						this.message = response;
						console.log(response);
					});
				}
			},

			refresh: function() {
				// check cache miss
				var me = this;
				//if( this.bufferStartIndex <0  || ! (this.startIndex-1 >= this.bufferStartIndex && this.endIndex<= this.bufferEndIndex) ) {
					var idx = this.startIndex - this.pageSize*this.backwardPages -1;
					idx = (idx <0)?0:idx;
					this.queryCallback (idx, this.bufferSize).then(function successCallback(response) {
						me.setup(idx, response.data.count);
					}, function errorCallback(response) {
						this.message = response;
						console.log(response);
					});
				//}
			},

			setup:  function (bufferStartIndex, totalResults) { // called by checkCache only
				console.log("page.setup()");
				console.log("totalResults:" + totalResults);
				this.totalResults = totalResults;

				this.bufferStartIndex = bufferStartIndex;
				this.bufferEndIndex = this.bufferStartIndex + this.bufferSize;
				if(this.bufferEndIndex > this.totalResults) {
					this.bufferEndIndex = this.totalResults;
				}

				this.calculate();

				console.log(this);
				console.log("page.setup() completed");
			},

			
			gotoFirst:  function () {
				if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
				this.startIndex = 1;
				this.calculate();
				this.checkCache();
			},
			
			gotoLast:  function () {
				if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
				this.startIndex = (this.totalPages-1)*this.pageSize + 1;
				this.calculate();
				this.checkCache();
			},
			
			gotoPrev:  function () {
				if(this.startIndex > this.pageSize) {
					if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
					this.startIndex -= this.pageSize;
					this.calculate();
					this.checkCache();
					}
			},

			gotoNext:  function () {
				if(this.startIndex + this.pageSize <= this.totalResults) {
					if( ! navigationService.pageNavigationService.beforeLeavingCurrentPage() ) { return false; }
					this.startIndex += this.pageSize;
					this.calculate();
					this.checkCache();
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
					this.checkCache();
				} 
			},
			
			mouseEnter: function() {
				this.showInput = true;
			},
			
			mouseLeave: function () {
				this.showInput = false;
			}
		
	}  // page
	
	page.init(pageSize, bufferSize);
	return page;
	}  // function createPage
	
	return service;
}]);  // app.factory('paginationService'


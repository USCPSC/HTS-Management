
app.service('csvExportService',['$http',function($http){
		var self=this;
		self.dataUrl = '';
		self.params = '';
		self.data = '';
		self.col_seprator=',';
		self.line_seprator='\r\n';
		self.col_surrounder='"';
		self.col_headers=[];
		self.output_file_name='download.csv';
		self.result_data_column_keys=[];
		self.result_data_column_magic=[];
		self.data_transformer=undefined;
		
		self.setDataTransformer=function(transformCallback){
			self.data_transformer=transformCallback;
		};
		
		self.setColumnSurrounder=
			function(surrounder){
				self.col_surrounder = surrounder;
			};
		
		self.setColumnKeys=
			function(keysArray){
				self.result_data_column_keys=keysArray;
			};
		
		self.setColumnMagic= // column affect by MS Excel magic conversion, and need escape
			function(magicArray){
				self.result_data_column_magic=magicArray;
			};
			
		self.setOutputFileName=
			function(fileName){
				self.output_file_name=fileName;
			};
		
		self.setColumnHeaders=
			function(headers){
				self.col_headers = headers;
			};
		
		self.setColumnSeprator=
			function(sep){
				if(sep)
					self.col_seprator = sep;
			};
		
		self.setLineSeprator=
			function(sep){
				if(sep)
					self.line_seprator = sep;
			};
		
		self.registerDataSource = 
			function(dataUrl,params,data){
				console.log("csvExportService.registerDataSource: dataUrl="+dataUrl);
			
				self.data_transformer = undefined; 
				
				self.dataUrl = dataUrl;
				self.params = params;
				if(!self.params){
					self.params={};
				}
				//eliminate pagination
				self.params.startIndex = 0;
				//self.params.numberOfRecords = 2147483647; //JAVA's Integer.MAX_VALUE
				self.params.numberOfRecords = 1048576; // max allowd by MS Excel
				
				self.data = data;
			};
			
		self.executeDownload = 
			function(outputFileName,downloadCompleteCallback,downloadFailCallback,noDataCallback){
					if(outputFileName)
						self.setOutputFileName(outputFileName);
					
					$http({
						method: 'POST',
						url: self.dataUrl,
						params: self.params,
						data: self.data,
						timeout: 600000 //10 mins
					}).then(function successCallback(response) {
						//console.log(response.data);
						if( response.data ){
							dataList=[];
							if( self.data_transformer ){
								dataList = self.data_transformer(response.data.data,self.resultDataToFile);
							}else{
								dataList = response.data.data;
								self.resultDataToFile(dataList,downloadCompleteCallback,downloadFailCallback,noDataCallback);
							}
						}
					},
					function errorCallback(response) {
						console.log(response);
						if(downloadFailCallback)
							downloadFailCallback();
					});
		};
		
		self.resultDataToFile=
			function(dataList,downloadCompleteCallback,downloadFailCallback,noDataCallback){
						count=0;
						if(dataList){
							count = dataList.length;
						}
						
						if( count<=0 ){
							if(noDataCallback)
								noDataCallback();
						}else{
							//prepare content
							csvContent = '';
							if(self.col_headers && self.col_headers.length>0){
								for( i=0;i<self.col_headers.length;i++){
									header = self.col_headers[i];
									if( self.col_surrounder ){
										csvContent += self.col_surrounder;
									}
									csvContent += header;
									if( self.col_surrounder ){
										csvContent += self.col_surrounder;
									}
									if( i< self.col_headers.length-1 ){
										csvContent += self.col_seprator;
									}
								}
								csvContent += self.line_seprator;
							}
							
							for (index = 0; index < dataList.length; ++index) {
								dataItem = dataList[index];
								//console.log(dataItem);
								line = '';
								if(self.result_data_column_keys){
									for( k=0; k<self.result_data_column_keys.length; k++){
										key = self.result_data_column_keys[k];
										if( dataItem[key] != null){
											if( self.col_surrounder ){
												line += self.col_surrounder;
											}
											
											if(dataItem[key] != null) {
												var columnData = '' + ((dataItem[key] == null)?'':dataItem[key]); // convert to string
												
												if(self.result_data_column_magic.indexOf(key) > -1) {
													columnData = '="' + columnData + '"'; // force to text by using MS spreadsheet formula
												}
												
												line += columnData.replace(/"/g, '""');
											}
							
											
											if( self.col_surrounder ){
												line += self.col_surrounder;
											}
										}
										
										if( k<self.result_data_column_keys.length-1)
											line += self.col_seprator;
									}
								}
								
								csvContent += (line+self.line_seprator);
							}
							
							self.downloadFile(self.output_file_name, csvContent, true);
						}
						
						if(downloadCompleteCallback)
							downloadCompleteCallback();
			}
		
		self.downloadFile = 
				function (fileName, csvContent, exporterOlderExcelCompatibility) {
	          var D = document;
	          var a = D.createElement('a');
	          var strMimeType = 'application/octet-stream;charset=utf-8';
	          var rawFile;
	          var ieVersion;

	          // IE10+
	          if (navigator.msSaveBlob) {
	            return navigator.msSaveOrOpenBlob(
	              new Blob(
						                [exporterOlderExcelCompatibility ? "\uFEFF" : '', csvContent],
						                { type: strMimeType } ),
						              fileName
						            );
						 }
	          
	          //IE older
	          ieVersion = self.isIE();
	          if (ieVersion && ieVersion < 10) {
		            var frame = D.createElement('iframe');
		            document.body.appendChild(frame);
	
		            frame.contentWindow.document.open('application/csv', "replace");
		            frame.contentWindow.document.write( 'sep=,\r\n' +  csvContent);
		            frame.contentWindow.document.close();
		            frame.contentWindow.focus();
		            frame.contentWindow.document.execCommand('SaveAs', true, fileName);
	
		            document.body.removeChild(frame);
		            return true;
		          		}
	          
	          //html5 A[download]
	          if ('download' in a) {
	            var blob = new Blob(
							              [exporterOlderExcelCompatibility ? "\uFEFF" : '', csvContent],
							              { type: strMimeType } );
	            rawFile = URL.createObjectURL(blob);
	            a.setAttribute('download', fileName);
	          } else {
	            rawFile = 'data:' + strMimeType + ',' + encodeURIComponent(csvContent);
	            a.setAttribute('target', '_blank');
	          			 }

	          a.href = rawFile;
	          a.setAttribute('style', 'display:none;');
	          D.body.appendChild(a);
	          setTimeout(function() {
							            if (a.click) {
							              a.click();
							              // Workaround for Safari 5
							            } else if (document.createEvent) {
							              var eventObj = document.createEvent('MouseEvents');
							              eventObj.initEvent('click', true, true);
							              a.dispatchEvent(eventObj);
							            			 }
							            D.body.removeChild(a);
							          		    }
	          			,this.delay);
		
	    };  // end of downloadFile()
	    
	    
	  self.isIE=
		  function(){
          var match = navigator.userAgent.search(/(?:Edge|MSIE|Trident\/.*; rv:)/);
          var isIE = false;

          if (match !== -1) {
            isIE = true;
          			  }

          return isIE;
	  		};
}]);
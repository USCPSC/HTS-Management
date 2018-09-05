/**
 * Inbox Analysis tab
 */

console.log ("loading inbox_analysis.js");

//{
//	  "color" : "Green",
//	  "rgb" : "00FF19",
//	  "textRgb" : "000000",
//	  "percentile" : 1,
//	  "minScore" : -2147483639,
//	  "description" : "Low",
//	  "ramScoreVersion" : "1_0_0"
//	},


	
app.controller('inboxAnalysisController', ['$scope', 'inboxService', 'Session', 'utilService', function($scope, inboxService, Session, utilService) {

	//$scope.commentList=[];
	
	
	$scope.init = function() {
		$scope.ruleInfoLoaded = false;
		$scope.ruleColorLoaded = false;
		
		//$scope.getScoreColorList($scope.detail.selectedEntryLine); // move this inside getRuleInfo to allow loading in sequence
		$scope.getRuleInfo($scope.detail.selectedEntryLine);
	}
	
	$scope.colorIndex = function (score) {
		if($scope.scoreColorList) {
			for(var i=0;i<4;i++) {
				if(score < $scope.scoreColorList[i+1].minScore) {
					return i;
				}
			}
			return 4;
		}
	}

	
	$scope.backgroundColor = function(index) {
		return $scope.scoreColorList[index].rgb;
	}
	
	$scope.textColor = function(index) {
		return $scope.scoreColorList[index].textRgb;
	}

	$scope.transformRuleInfo = function(ruleInfo) {
		var info=[];
		var getIntercept = function() {
			var x = jsonPath(ruleInfo,"$..ruleSourceObject.intercept");
			x = x?x[0]:0;
			return x;
		}
		var getProductCategory = function() {
			var x = jsonPath(ruleInfo,"$..ruleSourceObject.productCategory");
			x = x?x[0]:0;
			return x;
		}
		var getCoeff = function(termName) {
			var x = jsonPath(ruleInfo,"$..ruleSourceObject.funcTermList[?(@.termName == '" + termName + "')]");
			x = x?x[0].coefficient:0;
			return x;
		}
		var getItemScore = function(ruleTypeName) {
			var x = jsonPath(ruleInfo,"$..scoredItemList[?(@.ruleTypeName == '" + ruleTypeName + "')]");
			//x = x?x[0].scoreString:0;
			if(x) {
				x = (x[0].scoreString == undefined)?x[0].score: x[0].scoreString;
			} else {
				x = 0;
			}
				
				
			if(x == 'NaN') {
				x = 0;
			}
			return Number(x);
		}

		//		info.scoredItemList[5].ruleSourceObject.funcTermList[3].termName
//		"Filer Group"
//		info.scoredItemList[5].ruleSourceObject.funcTermList[3].termValue
//		"2"
//		<tr ng-repeat="item in ruleInfo.formatedScoredComponentList">
//		<td>{{item.dataCategory}}</td>
//		<td>{{item.expert}}</td>
//		<td>{{item.riskModel}}</td>
		i = 0;
		info[i++] = {"dataCategory": "INTERCEPT","expert": 0,"riskModel": 0};
		//info[i++] = {"dataCategory": "INTERCEPT","expert": 0,"riskModel": 0};
		info[i++] = {"dataCategory": "FILER","expert": 0,"riskModel": 0};
		info[i++] = {"dataCategory": "PRODUCT(ATV;FWRKS;LIGHTER;LIGHTER EXCLUSION)","expert": 0,"riskModel": 0};
		info[i++] = {"dataCategory": "IMPORTER(EXAM;COMPLY;ON THE MEND)","expert": 0,"riskModel": 0};
		info[i++] = {"dataCategory": "COUNTRY","expert": 0,"riskModel": 0};
		info[i++] = {"dataCategory": "SEASON","expert": 0,"riskModel": 0};
		info[i++] = {"dataCategory": "DECLARED VALUE","expert": 0,"riskModel": 0};
		//info[i++] = {"dataCategory": "RAW SCORE","expert": 0,"riskModel": 0};
		info[i++] = {"dataCategory": "WEIGHTED SCORE","expert":0,"riskModel": 0};

		info[0].dataCategory = getProductCategory(); // Show Toys, Fireworks, NTNF
		
//		// testing: dummy data
//		i = 0;
//		info[i++] = {"dataCategory": "INTERCEPT","expert": '--',"riskModel": 10};
//		info[i++] = {"dataCategory": "FILER","expert": '--',"riskModel": 0};
//		info[i++] = {"dataCategory": "PRODUCT(ATV;FWRKS;LIGHTER;LIGHTER EXCLUSION)","expert": 22,"riskModel": 10};
//		info[i++] = {"dataCategory": "IMPORTER(EXAM;COMPLY;ON THE MEND)","expert": 40,"riskModel": 25};
//		info[i++] = {"dataCategory": "COUNTRY","expert": '--',"riskModel": -0.6};
//		info[i++] = {"dataCategory": "SEASON","expert": '--',"riskModel": 0};
//		info[i++] = {"dataCategory": "DECLARED VALUE","expert": 18.5,"riskModel": 0};
//		info[i++] = {"dataCategory": "RAW SCORE","expert": 80.5,"riskModel": 44.4};
//		info[i++] = {"dataCategory": "WEIGHTED SCORE","expert":40.25,"riskModel": 22.2};

		// expert: sub-scores and total
		i = 0;
		info[i++].expert = 'n/a';
		info[i++].expert = 'n/a'; //getCoeff("Filer Group");
		info[i++].expert = getItemScore("AtvImporterInclusion") + getItemScore("FireworksExclusion") + getItemScore("LighterExclusion") + getItemScore("LighterInclusion") + getItemScore("HtsProduct") ;
		info[i++].expert = getItemScore("FieldExamResult") + getItemScore("LabAnalysisResult") + getItemScore("OnTheMend") ;
		info[i++].expert = 'n/a'; //getItemScore("COO Group");
		info[i++].expert = 'n/a'; //getItemScore("Shipment Month");
		info[i++].expert = getItemScore("DeclaredValue");

		var totalScore = 0;
		totalScore += info[2].expert;
		totalScore += info[3].expert;
		totalScore += info[6].expert;
		//info[i++].expert = totalScore * 2;
		info[i++].expert = totalScore;

		//risk model: coefficients, and scores
		i = 0;
		info[i++].riskModel = getIntercept().toString() + " (Intercept)"; // TODO: add intercept to func term list
		info[i++].riskModel = getCoeff("Filer Group").toString() + " (coefficient)";
		info[i++].riskModel = 'n/a'; //TODO: clarify what HTS code fit into coefficient? Inherent Risk?
		info[i++].riskModel = (getCoeff("Exam Rank") + getCoeff("Comply Rank")).toString()  + " (coefficient)";
		info[i++].riskModel = getCoeff("COO Group").toString()  + " (coefficient)";
		info[i++].riskModel = getCoeff("Shipment Month").toString()  + " (coefficient)";
		info[i++].riskModel = getCoeff("ValE").toString()  + " (coefficient)";

		//info[i++].riskModel = (getItemScore("ProductModel") ) * 2;
		info[i++].riskModel = (getItemScore("ProductModel")).toString()  + " (Weighted Score)";


		return info;
		
//		var funcTermList = jsonPath(info,"$..funcTermList");
//		if(funcTermList) {
//			funcTermList = funcTermList[0];
//			
//			for(var i=0;i<funcTermList.length;i++) {
//				
//			}
//		}
	}

	$scope.getRuleInfo = function(selectedEntryLine) {
		console.log("getRuleInfo");
		console.log(selectedEntryLine);
		var entryNumber = selectedEntryLine.entryNumber;
		var entryLineNumber = selectedEntryLine.entryLineNumber;
		inboxService.getRuleInfo(entryNumber, entryLineNumber).then(function successCallback(data) {
			console.log(data);
			$scope.ruleInfoLoaded = true;
			$scope.ruleInfo = data;
			$scope.ruleInfo.formatedScoredComponentList = $scope.transformRuleInfo($scope.ruleInfo) ;
			$scope.getScoreColorList(selectedEntryLine); // load in sequence

//			if("3407002000" == selectedEntryLine.hts) {
//				$scope.ruleInfo.formatedScoredComponentList = $scope.transformRuleInfo($scope.ruleInfo) ;
//			}
			
		}, function errorCallback(response) {
			console.log(response);
		});
	}

	$scope.getScoreColorList = function(selectedEntryLine) {
		console.log("getRuleInfo");
		console.log(selectedEntryLine);
		var entryNumber = selectedEntryLine.entryNumber;
		var entryLineNumber = selectedEntryLine.entryLineNumber;
		inboxService.getScoreColorList(entryNumber, entryLineNumber).then(function successCallback(data) {
			console.log(data);
			$scope.scoreColorList = data;
			$scope.ruleColorLoaded = true;
			for(var i=0;i<$scope.scoreColorList.length-1;i++) {
				$scope.scoreColorList[i].maxScore = Number($scope.scoreColorList[i+1].minScore) - 1;
			}
		}, function errorCallback(response) {
			console.log(response);
		});
	}

	
	// support ng-style to set text and background colors
	
//					<span style="padding: 5px 10px; : #{{}}; color: #{{textColor(colorIndex(ruleInfo.totalScore))}}">{{ruleInfo.totalScore | number: 0}}</span>
	
	$scope.totalScoreStyle = function(ruleInfo) {
		var style = {
				"background": '#' + $scope.backgroundColor($scope.colorIndex(ruleInfo.totalScore)),
				"color":		'#' + $scope.textColor($scope.colorIndex(ruleInfo.totalScore))
		}
		return style;
	}
//						<td ng-style="{'background': #color.rgb}" style = "text-align:center"><span ng-style="{'color': #color.textRgb} ">{{color.color}}</span></td>
	$scope.scoreColorStyle = function(color) {
		var style = {
				"background": '#' + color.rgb,
				"color":		'#' + color.textRgb
		}
		return style;
	}

	$scope.init();

}]);

console.log ("Done loading inbox_analysis.js");


//{
//	  "transactionId" : "06530941506_1_1468018474701",
//	  "requestTime" : "2016-07-08T22:54:34Z",
//	  "responseTime" : "2016-07-08T22:49:06Z",
//	  "totalScore" : 93.52374566395655,
//	  "scoredItemList" : [ {
//	    "score" : 500.0,
//	    "ruleCategoryName" : "RamExpert",
//	    "ruleTypeName" : "AtvImporterInclusion",
//	    "ruleSourceObject" : {
//	      "ruleCategoryName" : "RamExpert",
//	      "ruleTypeName" : "AtvImporterInclusion",
//	      "ruleInstanceName" : "RamExpert_AtvImporterInclusion",
//	      "ruleInstanceDisplayName" : "RamExpert_AtvImporterInclusion",
//	      "importerEIN" : "13-360964000",
//	      "htsCode" : "9613100000"
//	    }
//	  }, {
//	    "score" : 54.66553484858643,
//	    "ruleCategoryName" : "RamExpert",
//	    "ruleTypeName" : "DeclaredValue",
//	    "ruleSourceObject" : {
//	      "ruleCategoryName" : "RamExpert",
//	      "ruleTypeName" : "DeclaredValue",
//	      "ruleInstanceName" : "RamExpert_DeclaredValue",
//	      "ruleInstanceDisplayName" : "Declared Value at Import",
//	      "declaredValue" : 56000.0
//	    }
//	  }, {
//	    "score" : "NaN",
//	    "ruleCategoryName" : "RamExpert",
//	    "ruleTypeName" : "FieldExamResult",
//	    "ruleSourceObject" : {
//	      "ruleCategoryName" : "RamExpert",
//	      "ruleTypeName" : "FieldExamResult",
//	      "ruleInstanceName" : "RamExpert_FieldExamResult",
//	      "ruleInstanceDisplayName" : "RamExpert_FieldExamResult",
//	      "importerEIN" : "13-360964000",
//	      "sampleNumber" : null,
//	      "examDate" : null,
//	      "entryPort" : null
//	    }
//	  }, {
//	    "score" : 0.0,
//	    "ruleCategoryName" : "RamExpert",
//	    "ruleTypeName" : "FireworksExclusion",
//	    "ruleSourceObject" : {
//	      "ruleCategoryName" : "RamExpert",
//	      "ruleTypeName" : "FireworksExclusion",
//	      "ruleInstanceName" : "RamExpert_FireworkExclusions",
//	      "ruleInstanceDisplayName" : "RamExpert_FireworkExclusions",
//	      "htsCode" : "9613100000"
//	    }
//	  }, {
//	    "score" : "NaN",
//	    "ruleCategoryName" : "Matches, Lighters, Candles",
//	    "ruleTypeName" : "HtsProduct",
//	    "ruleSourceObject" : {
//	      "ruleCategoryName" : "RamExpert",
//	      "ruleTypeName" : "HtsProduct",
//	      "ruleInstanceName" : "RamExpert_HTSProduct",
//	      "ruleInstanceDisplayName" : "Addressability and Priority at Import",
//	      "htsCode" : "9613100000",
//	      "productCategory" : "Matches, Lighters, Candles",
//	      "score" : null
//	    }
//	  }, {
//	    "score" : "NaN",
//	    "ruleCategoryName" : "RamExpert",
//	    "ruleTypeName" : "LabAnalysisResult",
//	    "ruleSourceObject" : {
//	      "ruleCategoryName" : "RamExpert",
//	      "ruleTypeName" : "LabAnalysisResult",
//	      "ruleInstanceName" : "RamExpert_LabAnalysisResult",
//	      "ruleInstanceDisplayName" : "RamExpert_LabAnalysisResult",
//	      "importerEIN" : "13-360964000",
//	      "riskScore" : null,
//	      "screenInfo" : null
//	    }
//	  }, {
//	    "score" : 0.0,
//	    "ruleCategoryName" : "RamExpert",
//	    "ruleTypeName" : "LighterExclusion",
//	    "ruleSourceObject" : {
//	      "ruleCategoryName" : "RamExpert",
//	      "ruleTypeName" : "LighterExclusion",
//	      "ruleInstanceName" : "RamExpert_LighterExclusion",
//	      "ruleInstanceDisplayName" : "RamExpert_LighterExclusion",
//	      "importerEIN" : "13-360964000",
//	      "htsCode" : "9613100000"
//	    }
//	  }, {
//	    "score" : 100.0,
//	    "ruleCategoryName" : "RamExpert",
//	    "ruleTypeName" : "LighterInclusion",
//	    "ruleSourceObject" : {
//	      "ruleCategoryName" : "RamExpert",
//	      "ruleTypeName" : "LighterInclusion",
//	      "ruleInstanceName" : "RamExpert_LighterInclusion",
//	      "ruleInstanceDisplayName" : "RamExpert_LighterInclusion",
//	      "importerEIN" : "13-360964000",
//	      "htsCode" : "9613100000"
//	    }
//	  }, {
//	    "score" : 0.0,
//	    "ruleCategoryName" : "RamExpert",
//	    "ruleTypeName" : "OnTheMend",
//	    "ruleSourceObject" : {
//	      "ruleCategoryName" : "RamExpert",
//	      "ruleTypeName" : "OnTheMend",
//	      "ruleInstanceName" : "RamExpert_OnTheMend",
//	      "ruleInstanceDisplayName" : "RamExpert_OnTheMend",
//	      "importerEIN" : "13-360964000"
//	    }
//	  }, {
//	    "score" : 6.847991093807556E-4,
//	    "ruleCategoryName" : "PortModel",
//	    "ruleTypeName" : "PortModel",
//	    "ruleSourceObject" : {
//	      "ruleCategoryName" : "PortModel",
//	      "ruleTypeName" : "PortModel",
//	      "ruleInstanceName" : "PortModel_Large",
//	      "ruleInstanceDisplayName" : null,
//	      "funcTermList" : [ {
//	        "termName" : "Filer_Type",
//	        "termValue" : "Small",
//	        "coefficient" : 0.0,
//	        "variable" : 0.0
//	      }, {
//	        "termName" : "PortRegion",
//	        "termValue" : "SE",
//	        "coefficient" : -0.465,
//	        "variable" : 1.0
//	      }, {
//	        "termName" : "PortType",
//	        "termValue" : "Sea",
//	        "coefficient" : 0.0,
//	        "variable" : 0.0
//	      }, {
//	        "termName" : "Country_Type",
//	        "termValue" : "Large",
//	        "coefficient" : -0.0438,
//	        "variable" : 1.0
//	      }, {
//	        "termName" : "Product_Category",
//	        "termValue" : "Matches, Lighters, Candles",
//	        "coefficient" : 0.0,
//	        "variable" : 0.0
//	      }, {
//	        "termName" : "Filer_Type",
//	        "termValue" : "Small",
//	        "coefficient" : 0.0,
//	        "variable" : 0.0
//	      }, {
//	        "termName" : "PortRegion",
//	        "termValue" : "SE",
//	        "coefficient" : -0.465,
//	        "variable" : 1.0
//	      }, {
//	        "termName" : "Country_Type",
//	        "termValue" : "Large",
//	        "coefficient" : -0.0438,
//	        "variable" : 1.0
//	      } ]
//	    }
//	  }, {
//	    "score" : "NaN",
//	    "ruleCategoryName" : "ProductModel",
//	    "ruleTypeName" : "ProductModel",
//	    "ruleSourceObject" : {
//	      "ruleCategoryName" : "ProductModel",
//	      "ruleTypeName" : "ProductModel",
//	      "ruleInstanceName" : "ProductModel_Toys",
//	      "ruleInstanceDisplayName" : "ProductModel_Toys",
//	      "funcTermList" : null
//	    }
//	  } ]
//	}

/**
 * exam details controller, shared by inbox and examlog
 */
console.log("loading exam_details.js");
app.controller('examDetailController', ['$scope', '$rootScope', '$http', '$route', 'navigationService', 'examlogService', 'Session', 'utilService', 'workflowService', 'APP_CONSTANTS', '$uibModal', 'examCreateFormService', '$q', function ($scope, $rootScope, $http, $route, navigationService, examlogService, Session, utilService, workflowService, APP_CONSTANTS, $uibModal, examCreateFormService, $q) {
    $scope.statutes;
//	alert("*********** Entering examDetailController()"); // debugging why this controller is called twice except first time show exam detail
//	$scope.$on("$destroy", function() {
//            alert("The scope for examDetailController is being destroyed");
//    });
		/**
		 * initialize the controller: invoke this at end of this controller definition
		 */
		$scope.init = function () {
			console.log("build examDetailController");
			$scope.detailTabSelected = "Details";
			//$scope.editMode = "VIEW"; // VIEW/EDIT/CREATE mode

			$scope.examAction.initCreateExam();
			$scope.examAction.getReasonTypes(); // drop down list
			//	    // listen on event for new exam creation
//	    $scope.$on(APP_CONSTANTS.EVENTS.createExam, function() {
//	    	$scope.examAction.initCreateExam();
//	    })

		}

		/**
		 * which tab to show for exam detail area
		 * input:
		 * 		detailTab: one from ['Details', 'Contacts', 'References','Documents']
		 * output:
		 * 		$scope.detailTabSelected: tab name
		 */
		$scope.showDetailTab = function (detailTab) {
			$scope.detailTabSelected = detailTab;
			navigationService.setCurrentTab(detailTab);
		}

		$scope.isExamFormDirty = function () {
			var isDirty = $scope.exam_form.$dirty;
			return isDirty
		}

		$scope.examAction = {
			examActionMenu: [],
			sampleActionMenu: [],
			initCreateExam: function () {
				if ($scope.exam && $scope.exam._editMode_ == 'CREATE') {
                    // create new exam
                    examlogService.prepopulateExamDetail($scope.exam.entryNumber, $scope.exam.entryLineNumber).then(function successCallback(data) {
                        //$scope.exam = data;
                        //$scope.exam._editMode_= "CREATE";

                        $scope.exam.activityTime = data.activityTime;
                        $scope.exam.countryOfOrigin = data.countryOfOrigin;
                        if (!data.entryNumber) {
                            $scope.exam._entryUnknown_ = true;
                        } else {
                            $scope.exam.entryNumber = data.entryNumber;
                        }


                        $scope.exam.examDate = data.examDate;
                        $scope.exam.examEntryLines = data.examEntryLines;
                        $scope.exam.importerName = data.importerName;
                        $scope.exam.importerNumber = data.importerNumber;
                        $scope.exam.portCode = data.portCode;
                        $scope.exam.portName = data.portName;
                        $scope.exam.operation = data.operation;
                        $scope.exam.travelTime = data.travelTime;
                        $scope.exam.investigator = Session.userId;
                        $scope.examAction.setupEditForm();
                    }, function errorCallback(response) {
                        console.log(response);
                    });
				}

			},
			setupEditExam: function () {
				this.examActionMenu = [{header: 'Exam'},
					'Edit Exam', 'Delete Exam',
					{divider: true},
					{header: 'Create Form'},
					'Form 163 - Receipt for Samples',
					'Form 330 - Action Requested from CBP',
					'Form 352 - Notice of Sampling and Detention',
					'Form 353 - Notice of Sampling and Conditional Release',
					'Form 354 - Notice of Conditional Release',
					'Tracking Label Advisory'
					];
				this.sampleActionMenu = [{header: 'Products/Samples'}, 'Edit', 'Delete', 'Copy'];
				console.log(this.examActionMenu);
			},
			setupEditForm: function () {
				var examCopy = this.deepCopy($scope.exam);
				$scope.examCopy = examCopy;
				examCopy.examDate_edit = examCopy.examDate;
				//examCopy.examDate_edit = new Date(examCopy.examDate);
				examCopy.investigator_edit = examCopy.investigator;
				examCopy.investigator2_edit = examCopy.investigator2;
				examCopy.remarks_edit = examCopy.remarks;
				examCopy.portCode_edit = examCopy.portCode;
				examCopy.operation_edit = examCopy.operation;
				examCopy.activityTime_edit = Number(examCopy.activityTime);
				examCopy.travelTime_edit = Number(examCopy.travelTime);
				examCopy.ifsAssignmentId_edit = examCopy.ifsAssignmentId;
				examCopy.labelMissingIncomplete_edit = (examCopy.labelMissingIncomplete == "Yes") ? true : false;
				examCopy.brokerImporterNotified_edit = (examCopy.brokerImporterNotified == "Yes") ? true : false;
				examCopy.importerNumber_edit = examCopy.importerNumber;
				examCopy.importerName_edit = examCopy.importerName;
				$scope.exam_workflowStatus = ""; // for new workflowstatus update

				this.getInvestigators(); // drop down list
				this.getDispositionTypes(); // drop down list
				this.getStatutes(); // drop down list
				this.getReasonTypes(); // drop down list


				console.log($scope);
			},
// begin - get drop down lists
			getInvestigators: function () {
				$http({
					method: 'POST',
					url: 'lookup/investigatorList',
					params: {
						currentUser: Session.userId,
						password: Session.password
					}
				}).then(function successCallback(response) {
					console.log('userFormController populateForm()', response);
					var users = response.data;
					var investigators = [];
					users.forEach(function (user, index, users) {
						investigators.push({
							username: user.username,
							formattedName: utilService.getFormattedName(user.firstname, user.lastname)
						});
					});
					investigators.sort(function (u1, u2) {
						return u1.formattedName.localeCompare(u2.formattedName)
					});
					$scope.investigators = investigators;
					console.log($scope);
				}, function errorCallback(response) {
					console.log('Error: userFormController populateForm()', response);
					return [];
				});
			},
			getDispositionTypes: function () {
				$http({
					method: 'GET',
					url: 'lookup/dispositionTypes',
					params: {
						currentUser: Session.userId,
						password: Session.password
					}
				}).then(function successCallback(response) {
					console.log('getDispositionTypes', response);
					$scope.dispositionTypes = response.data;
					console.log($scope);
				}, function errorCallback(response) {
					console.log('Error: getDispositionTypes', response);
					return [];
				});
			},
			getReasonTypes: function () {
				$http({
					method: 'GET',
					url: 'lookup/reasonTypes',
					params: {
						currentUser: Session.userId,
						password: Session.password
					}
				}).then(function successCallback(response) {
					console.log('getReasonTypes', response);
					$scope.reasonTypes = response.data;
					examlogService.reasonTypes = response.data; 
					// sort by display name
					$scope.reasonTypes.sort( utilService.sortArrayBy( 'reasonScreeningName', false, function(a){return a}) );
//					$scope.reasonTypes.sort( utilService.sortArrayBy( 'reasonScreeningName', false, function(a){return a.toUpperCase()}) );
//					$scope.reasonTypesCopy = $scope.reasonTypes; 
					console.log($scope);
				}, function errorCallback(response) {
					console.log('Error: getReasonTypes', response);
					return [];
				});
			},
			getStatutes: function () {
				return $http({
					method: 'GET',
					url: 'lookup/statutes',
					params: {
						currentUser: Session.userId,
						password: Session.password
					}
				}).then(function successCallback(response) {
					console.log('getStatutes', response);
					$scope.statutes = response.data;
					console.log($scope);
				}, function errorCallback(response) {
					console.log('Error: getStatutes', response);
					return [];
				});
			},
			getPortCodes: function (filter) {
				return $http({
					method: 'GET',
					url: 'lookup/filteredPortCodes',
					params: {
						filter: filter,
						currentUser: Session.userId,
						password: Session.password
					}
				}).then(function successCallback(response) {
					console.log('getPorts', response);
					return response.data;
				}, function errorCallback(response) {
					console.log('Error: getPorts', response);
					return [];
				});
			},
			getFilteredImporterNumbers: function (filter) {
				return $http({
					method: 'GET',
					url: 'lookup/filteredImporterNumbers',
					params: {
						filter: filter,
						currentUser: Session.userId,
						password: Session.password
					}
				}).then(function successCallback(response) {
					console.log('getFilteredImporterNumbers', response);
					return response.data;
				}, function errorCallback(response) {
					console.log('Error: getFilteredImporterNumbers', response);
					return [];
				});
			},
			getFilteredImporterNames: function (filter) {
				return $http({
					method: 'GET',
					url: 'lookup/filteredImporterNames',
					params: {
						filter: filter,
						currentUser: Session.userId,
						password: Session.password
					}
				}).then(function successCallback(response) {
					console.log('getFilteredImporterNames', response);
					return response.data;
				}, function errorCallback(response) {
					console.log('Error: getFilteredImporterNames', response);
					return [];
				});
			},
//			getFilteredOpCode: function (filter) {
//				return $http({
//					method: 'GET',
//					url: 'lookup/filteredImporterNumbers',
//					params: {
//						filter: filter,
//						currentUser: Session.userId,
//						password: Session.password
//					}
//				}).then(function successCallback(response) {
//					console.log('getFilteredImporterNumbers', response);
//					return response.data;
//				}, function errorCallback(response) {
//					console.log('Error: getFilteredImporterNumbers', response);
//					return [];
//				});
//			},
			// end - get drop down lists

			deepCopy: function (oldValue) {
				var newValue;
				strValue = JSON.stringify(oldValue);
				return newValue = JSON.parse(strValue);
			},
			notifyExamCloseEvent: function () {
				if (!navigationService.pageNavigationService.beforeLeavingCurrentPage()) {
					return false;
				}
				//$scope.$emit(APP_CONSTANTS.EVENTS.closeExamDetail);
			},
			notifyExamOpenEvent: function () {
				$scope.$emit(APP_CONSTANTS.EVENTS.openExamDetail);
			},
			examCreateForm: function (formData) {
				$uibModal.open({
					templateUrl: './examlog/popup/create_form_popup.html',
					bindToController: true,
					controller: 'examCreateFormController',
					controllerAs: 'vm',
					scope: $scope,
					backdrop: "static",
					size: "pdf",
					resolve: {
						'formData': function () {
							return $scope.examAction.getStatutes().then(function () {
                                return examCreateFormService.prePopulatePdfDto(formData).then(function (pdfDto) {
                                    formData.pdfDto = pdfDto;
                                    return(formData);
                                });
                            });
						}
					}
				});
			},
			performExamAction: function (actionFullName) {
				console.log(actionFullName);
				var actionIndex = actionFullName.indexOf(" - ");
				var action = (actionIndex > 0) ? actionFullName.substring(0, actionIndex) : actionFullName;
				console.log("action:" + action);
				switch (action) {
					case 'Form 163':
						this.examCreateForm({formType: 'Form163', formName: 'Form 163', entryNumber: $scope.exam.entryNumber});
						break;
					case 'Form 330':
						this.examCreateForm({formType: 'Form330', formName: 'Form 330', entryNumber: $scope.exam.entryNumber});
						break;
					case 'Form 352':
						this.examCreateForm({formType: 'Form352', formName: 'Form 352', entryNumber: $scope.exam.entryNumber});
						break;
					case 'Form 353':
						this.examCreateForm({formType: 'Form353', formName: 'Form 353', entryNumber: $scope.exam.entryNumber});
						break;
					case 'Form 354':
						this.examCreateForm({formType: 'Form354', formName: 'Form 354', entryNumber: $scope.exam.entryNumber});
						break;
					case 'Tracking Label Advisory':
						this.examCreateForm({formType: 'FormTrackingLabel', formName: 'Tracking Label Advisory', entryNumber: $scope.exam.entryNumber});
						break;
							
					case 'Edit Exam':
						this.setupEditForm();
						//$scope.editMode = "EDIT";
						$scope.exam._editMode_ = "EDIT";
						navigationService.pageNavigationService.setCurrentPage({
							currentPageName: "edit_exam_page",
							pageDirtyCallback: function () {
								var isDirty = ($scope.exam._editMode_ != "VIEW" && 
										utilService.getControllerScope('examDetailController')  &&
										utilService.getControllerScope('examDetailController').isExamFormDirty());
								return isDirty;
							},
							CleanupCallback: function (event) {
								if (event != "cancelEdit" && event != "saveEdit") {
									$scope.detail.selectedExam = null; // close exam detail
								}
							}

						})

						break;
					case 'saveEdit':
						var me = this; // this is examAction {}
						var examCopy = $scope.examCopy;
						examCopy.examDate = examCopy.examDate_edit;
						//examCopy.examDate = examCopy.examDate_edit.toLocaleDateString("en-US");
						examCopy.investigator = examCopy.investigator_edit;
						examCopy.investigator2 = examCopy.investigator2_edit;
						examCopy.remarks = examCopy.remarks_edit;
						examCopy.portCode = examCopy.portCode_edit;
						examCopy.operation = examCopy.operation_edit;
						examCopy.activityTime = examCopy.activityTime_edit.toString();
						examCopy.travelTime = examCopy.travelTime_edit.toString();
						examCopy.ifsAssignmentId = examCopy.ifsAssignmentId_edit;
						examCopy.labelMissingIncomplete = (examCopy.labelMissingIncomplete_edit) ? "Yes" : "No";
						examCopy.brokerImporterNotified = (examCopy.brokerImporterNotified_edit) ? "Yes" : "No";
						examCopy.importerNumber = examCopy.importerNumber_edit;
						examCopy.importerName = examCopy.importerName_edit;
						
						// update exam details
						var myScope = $scope;
						if (myScope.exam_workflowStatus) {
							//examlogService.processBatchWorkflowStatus(examCopy.examEntryLines, myScope.exam_workflowStatus);
							for (var i = 0; i < examCopy.examEntryLines.length; i++) {
								if (examCopy.examEntryLines[i].isChecked) {
									examCopy.examEntryLines[i]._flag = 'u';
									examCopy.examEntryLines[i].workflowStatus = myScope.exam_workflowStatus;
								}
							}
							
						}
						examlogService.saveExam(examCopy).then(function successCallback(data) {
							console.log(data);
							// update inbox status for create exam from inbox
							if ($scope.exam._saveCallback_) {
								$scope.exam._saveCallback_();
							}

							$scope.detail.setCurrentExam(data);
							me.setupEditForm();
							$scope.exam_form.$setPristine();
							// update inbox entry lines
							if($scope.entryLines != undefined) {  //inbox
								for (var i = 0; i < data.examEntryLines.length; i++) {
										// update inbox entry lines
									for (var j = 0; j < $scope.entryLines.length; j++) {
										if (data.examEntryLines[i].entryNumber == $scope.entryLines[j].entryNumber && data.examEntryLines[i].entryLineNumber == $scope.entryLines[j].entryLineNumber) {
											$scope.entryLines[j].workflowStatus = data.examEntryLines[i].workflowStatus;
											break;
										}  // if
									}  // j
								}  //i
							}  // inbox
						}, function errorCallback(response) {
							console.log(response);
//		  				examCopy.examErrors = response.data;
//		  				examCopy.examErrors.message = "Please correct following error(s):";
						});
						console.log($scope);
						break;
					case 'cancelEdit':
						if (!navigationService.pageNavigationService.beforeLeavingCurrentPage("cancelEdit")) {
							return false;
						}
						console.log($scope);
						if ($scope.exam._editMode_ == "CREATE") {
							//$scope.detail.selectedExam = null; // close exam detail
							this.notifyExamCloseEvent();
						} else {
							$scope.exam._editMode_ = "VIEW";
							navigationService.pageNavigationService.setCurrentPage({
								currentPageName: "view_exam_detail_page",
								CleanupCallback: function () {
									$scope.detail.selectedExam = null; // close exam detail
								}

							});
						}
						$scope.exam_form.$setPristine();
						break;
					default:
						//$scope.editMode = "VIEW";
						//$scope.exam._editMode_= "EDIT";
						console.log($scope);
						break;
				}

			}

		};
		$scope.screen = {
			fullScreen: false,
			displayFullScreen: function (isFullScreen) {
				this.fullScreen = isFullScreen;
			},
			processEscapeKey: function (keyCode) {
				if (keyCode == 27) { // process ESCAPE Key
					this.displayFullScreen(false);
				}
			}
		}

		/**
		 * process SelectAll
		 */
		$scope.toggleCheckedAll = function (toggleAllModel) {
			toggleAllModel.isChecked = !toggleAllModel.isChecked;
			for (var i = 0; i < toggleAllModel.length; i++) {
				toggleAllModel[i].isChecked = false;
			}
			for (var i = 0; i < toggleAllModel.length; i++) {
				toggleAllModel[i].isChecked = toggleAllModel.isChecked;
			}
		}

		/**
		 * process entry line checkbox
		 */
		$scope.toggleChecked = function (toggleAllModel, toggleModel) {
			toggleModel.isChecked = !toggleModel.isChecked;
			if (toggleModel.isChecked) {
				var flagAllChecked = true;
				for (var i = 0; i < toggleAllModel.length; i++) {
					if (!toggleAllModel[i].isChecked) {
						flagAllChecked = false;
						break;
					}
				}
				toggleAllModel.isChecked = flagAllChecked;
			} else {
				toggleAllModel.isChecked = false;
			}
		}

		$scope.sort = function (keyname) {
			$scope.sortKey = keyname; //set the sortKey to the param passed
			$scope.reverse = !$scope.reverse; //if true make it false and vice versa
		}

		$scope.setupBatchWorkflowStatus = function (entryLines) {
			var statusSet = new Set();
			var status = "";
			var hasNationalOp = false;
			for (var i = 0; i < entryLines.length; i++) {
				if (entryLines[i].isChecked) {
					status = entryLines[i].workflowStatus;
					statusSet.add(status);
					hasNationalOp = (entryLines[i].nationalOp) ? true : hasNationalOp; 
				}
			}
			if (statusSet.size == 1) {
				$scope.workflowActions = workflowService.getWorkflowNavigation(status, true, hasNationalOp).menu;
			} else {
				$scope.workflowActions = [{header: "Selected entries do not have same status: Workflow disabled"}];
			}
		}


		$scope.setWorkflowStatus = function (workflowStatus) {
			$scope.exam_workflowStatus = workflowStatus;
			$scope.exam_form.$setDirty();
		}

		/*
		 * change entryline work flow status batch updates
		 */
		$scope.processBatchWorkflowStatus = function (examEntryLines, workflowStatus) {
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
			$http({
				method: 'POST',
				url: url,
				params: {workflowStatus: workflowStatus},
				data: entryLines
			}).then(function successCallback(response) {
				var retData = response.data;
	  			growl.addInfoMessage("Workflow status updated.", {ttl: 5000});
				
				// success updates
				for (var i = 0; i < examEntryLines.length; i++) {
					if (examEntryLines[i].isChecked) {
						$scope.exam.examEntryLines[i].workflowStatus = workflowStatus;
						// update inbox entry lines if in inbox view
						if($scope.entryLines != undefined) {
							for (var j = 0; j < $scope.entryLines.length; j++) {
								if (examEntryLines[i].entryNumber == $scope.entryLines[j].entryNumber && examEntryLines[i].entryLineNumber == $scope.entryLines[j].entryLineNumber) {
									$scope.entryLines[j].workflowStatus = workflowStatus;
								}
							}
						}
					}
				}



			}, function errorCallback(response) {
	  			growl.addInfoMessage("Workflow status update failed.", {ttl: 5000});
				console.log(response);
			});
		}
		$scope.sampleDetail = {
			//expandColumn: "", // ENTRY_LINE or EXAM
			selectedSample: null, // selected product/sample to display details, key is property 'sampleNumber'
			detailTabSelected: "Details",
			selectedIndex: 0,
			isHideAboveRows: true,
			workflowUrl: "",
			init: function () {
				$scope.$on(APP_CONSTANTS.EVENTS.closeExamDetail, function () {
					$scope.sampleDetail.selectedSample = null; // close exam detail
				})
			},
			isRowHidden: function (entryLine, index) {
				if ($scope.sample && $scope.sampleDetail.selectedSample) {
					if (index < this.selectedIndex) {
						return this.isHideAboveRows;
					}
				}

				return false;
			},
			isDetailRow: function (sample, index) {
				return ($scope.sample && this.selectedSample && this.selectedSample.productSampleId == sample.productSampleId);
			},
			hideAboveRows: function (flag) {
				this.isHideAboveRows = flag;
			},
			selectSample: function (sample, index) {
				if ($scope.sampleDetail.selectedSample && $scope.sampleDetail.selectedSample.productSampleId == sample.productSampleId) {
					$scope.sampleDetail.selectedSample = null; // close exam detail
					return;
				}

				this.selectedSample = sample; // show detail for this sample
				this.selectedIndex = index; // the index in the loop
				//this.expandColumn = "EXAM";
				this.hideAboveRows(true);
				$scope.sampleDetail.selectedSample = sample; // show detail for this exam

				// fetch sample details
				var url = "exams/samples/" + $scope.sampleDetail.selectedSample.productSampleId;
				$http({
					method: 'POST',
					url: url
				}).then(function successCallback(response) {
					console.log(response);
					$scope.sample = response.data;
					$scope.sample._editMode_ = "VIEW"; // tell exam detail page to show in view sample mode
					if(sample.readingItems) {
						examlogService.noReadingItem = (sample.readingItems.length) ? false : true;
					}

				}, function errorCallback(response) {
					console.log(response);
				});
//	  				$scope.sample = {
//	  	    			productSampleId: $scope.sampleDetail.selectedSample.productSampleId,
//	  	    			sampleNumber:12345
//						}

				$scope.sample._editMode_ = "VIEW"; // tell exam detail page to show in view sample mode


			}


		} // end of sampleDetail object


		$scope.$on("saveSampleEdit", function( examSamples ) {
			$scope.saveSampleEdit(examSamples);
		})
		
		$scope.saveSampleEdit = function (examSamples) {
			// make sure reading items are deleted
			var smpSetLen = examSamples.currentScope.examCopy.examSamples.length;
			if(smpSetLen) {
				for(var i=smpSetLen-1; i>=0; i--) {
					var smp = examSamples.currentScope.examCopy.examSamples[i];
					if(smp && smp.readingItems) {
						for(var j=smp.readingItems.length-1; j>=0; j--) {
							var rdit = smp.readingItems[j];
							if(rdit && rdit._flag=='d') {
								smp.readingItems.splice(j,1);
							}
						}
					}
				}
			}
			$scope.examCopy = examSamples.currentScope.examCopy;
			$scope.examAction.performExamAction('saveEdit');
		}
		
		$scope.$on("cancelSampleEdit", function( examSamples ) {
			$scope.cancelSampleEdit(examSamples);
		})
		
		$scope.cancelSampleEdit = function (examSamples) {
			$scope.examAction.performExamAction('cancelEdit');
		}
					
					
		// call init()
		$scope.init();
	}]);
console.log("Done loading exam_details.js");
//Junk:
//    /*
//	PATCH /contacts
//	[
//	    {
//	        "op": "add", "value": {
//	            "firstName": "my first name",
//	            "lastName": "my last name"
//	        }
//	    },
//	    {
//	        "op": "remove", "path": "/contacts/1"
//	    }
//	]
//	Res     */
//
//	    $scope.getDelta = function(objEdit, obj) {
//	    	// return the changes made to objEdit from obj, designed to detect updates. Not for add/remove
//			if(objEdit == undefined || objEdit == null || obj == undefined || obj == null || typeof(objEdit)!= 'object' || typeof(obj)!='object' ) {
//				  return undefined;
//				}
//
//				var objDelta = {};
//				var nullValuedNonStringFields = [];
//
//	    	    	for(var name in objEdit) {
//	    	    		if(objEdit.hasOwnProperty(name)) {
//	    	    			if(!obj.hasOwnProperty(name)) {
//	    	    				continue; // new property in objEdit: ignore
//	    	    			}
//	    	    		} else {
//	    	    			continue; // not own property: ignore
//	    	    		}
//
//	    	    		if(objEdit[name] == undefined || objEdit[name] == null) {
//							if(obj[name] != undefined && obj[name] != null) {
//	    	    			  objDelta[name]=objEdit[name];
//	    	    	    	  if( ['number','boolean'].indexOf(typeof(objEdit[name])) > -1) {
//	    	    	    		  nullValuedNonStringFields.push(name);
//	    	    	    	  }
//							}
//	    	    		}
//
//	    	    		if(obj[name] == undefined || obj[name] == null) {
//							if(objEdit[name] != undefined && objEdit[name] != null) {
//		    	    			  objDelta[name]=objEdit[name];
//							}
//	    	    		}
//	    	    		if(typeof(objEdit[name]) != typeof(obj[name])) {
//	    	    			//console.log(name + " not same type");
//	    	    			return undefined;
//	    	    		}
//	    	    		if( ['number','string','boolean'].indexOf(typeof(objEdit[name])) > -1) {
//	        	    		if(objEdit[name] != obj[name]) {
//	         	    		  // console.log(name + " not same");
//	     	    			  objDelta[name]=objEdit[name];
//	         	    		}
//
//	    	    		}
//	    	    	}
//
//	    	    	if(objDelta.length >0) {
//	        	    	return {
//	        	    		"objDelta": objDelta,
//	        	    		"nullValuedNonStringFields": nullValuedNonStringFields
//	        	    		};
//	    	    	} else {
//	    	    		return false;
//	    	    	}
//				}
//
//	    $scope.isModified = function(objEdit, obj) {
//			if(objEdit == undefined || objEdit == null) {
//				if(obj != undefined && obj != null) {
//				  return false;
//				}
//			}
//
//			if(obj == undefined || obj == null) {
//				if(objEdit != undefined && objEdit != null) {
//				  return false;
//				}
//			}
//	    	    	for(var name in objEdit) {
//	    	    		if(objEdit.hasOwnProperty(name)) {
//	    	    			if(!obj.hasOwnProperty(name)) {
//	    	    				return false;
//	    	    			}
//	    	    		} else {
//	    	    			continue;
//	    	    		}
//	    	    		if(objEdit[name] == undefined || objEdit[name] == null) {
//							if(obj[name] != undefined && obj[name] != null) {
//	    	    			  return false;
//							}
//	    	    		}
//
//	    	    		if(obj[name] == undefined || obj[name] == null) {
//							if(objEdit[name] != undefined && objEdit[name] != null) {
//	    	    			  return false;
//							}
//	    	    		}
//	    	    		if(typeof(objEdit[name]) != typeof(obj[name])) {
//	    	    			console.log(name + " not same type");
//	    	    			return false;
//	    	    		}
//	    	    		if( ['number','string','boolean'].indexOf(typeof(objEdit[name])) > -1) {
//	        	    		if(objEdit[name] != obj[name]) {
//	         	    		   console.log(name + " not same");
//	         	    		   return false;
//	         	    		}
//
//	    	    		}
//	    	    	}
//	    	    	return true;
//				}
////	    ,
////	    	    examEquals: function(examEdit, exam) {
////	    	    	return (examEdit.examDate == exam.examDate &&
////	    	    		examEdit.investigator == exam.investigator &&
////	    	    		examEdit.remarks == exam.remarks &&
////	    	    		examEdit.portCode == exam.portCode &&
////	    	    		examEdit.activityTime == exam.activityTime &&
////	    	    		examEdit.travelTime == exam.travelTime &&
////	    	    		examEdit.ifsAssignmentId == exam.ifsAssignmentId &&
////	    	    		examEdit.labelMissingIncomplete == exam.labelMissingIncomplete &&
////	    	    		examEdit.brokerImporterNotified == exam.brokerImporterNotified &&
////	    	    		examEdit.importerNumber == exam.importerNumber &&
////	    	    		examEdit.importerName == exam.importerName);
////	    	    }
////	    }
//	    $scope.examDiff = function (examEdit, exam) {
//	    	var dto = []; // add|remove|update exam,sample,contacts, references, documents, list|detail, sample readings, (file upload)
//	    	var addList=[];
//	    	var removeList=[];
//	    	var updateList=[];
//
//	    	var path = "/exams/" + exam.entryNumber;
//	    	var delta = null;
//
//	    	// check exam detail updates
//	    	delta = $scope.getDelta(examEdit, exam);
//	    	if(delta) {
//	    		updateList.push({
//	    			op: "update",
//	    			path: path,
//	    			value: delta.objDelta,
//	    			nullValuedNonStringFields:delta.nullValuedNonStringFields
//	    		})
//	    	}
//
//	    	// check ExamContact add
////	    	private String contactType;
////	    	private String name;
////	    	private String phoneNumber;
////	    	private String email;
////	    	private String fax;
//	    	for(var c in examEdit.examContacts) {
//	    		var d = undefined;
//	    		for(d in exam.examContacts) {
//	    			if (c.id == d.id) {
//	    				break;
//	    			}
//	    		}
//	    		if(d) {
//	    			// check update
//	    			delta = $scope.getDelta(c, d);
//	    	    	if(delta) {
//	    	    		updateList.push({
//	    	    			op: "update",
//	    	    			path: path+"/contacts/"+c.id,
//	    	    			value: delta.objDelta,
//	    	    			nullValuedNonStringFields:delta.nullValuedNonStringFields
//	    	    		})
//	    	    	}
//	    		} else {
//	    			// add
//		    		addList.push({
//		    			op: "add",
//		    			path: path+"/contacts/",
//		    			value: delta.objDelta
//		    		})
//
//	    		}
//
//	    	}
//	    	// remove
//	    	for(var c in exam.examContacts) {
//	    		var d = undefined;
//	    		for(d in examEdit.examContacts) {
//	    			if (c.id == d.id) {
//	    				break;
//	    			}
//	    		}
//	    		if(d) {
//	    			continue; // still there, not removed
//	    	    	}
//	    		 else {
//	    			// remove
//		    		removeList.push({
//		    			op: "remove",
//		    			path: path+"/contacts/",
//		    			value: c
//		    		});
//
//	    		}
//
//	    	}
//
//
//	    	console.log("updateList:");
//	    	console.log(updateList);
//
//	    }
//
//

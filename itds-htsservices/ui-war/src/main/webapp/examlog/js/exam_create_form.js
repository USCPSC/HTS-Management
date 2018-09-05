app.factory('examCreateFormService', ['$http', 'Session', '$q', '$timeout', function ($http, Session, $q, $timeout) {
		var examCreateFormService = {};

		examCreateFormService.prePopulatePdfDto = function (inputData) {
			url = "pdf/prePopulate/" + inputData.entryNumber + "/" + inputData.formType;
			return $http({
				method: 'GET',
				url: url
			}).then(function successCallback(response) {
				console.log("success: " + url);
				return response.data;
			}, function errorCallback(response) {
				console.log(response);
				return;
			});
		};

		examCreateFormService.getFileNameFromHeader = function (header) {
			if (!header)
				return null;
			var result = header.split(";")[1].trim().split("=")[1];
			return result.replace(/"/g, '');
		};

		examCreateFormService.getPdf = function (pdfDto) {
			console.log("getting pdf");
			if (!pdfDto) {
				console.log("no pdfDto!");
				return;
			}
			$http({
				method: 'POST',
				url: "pdf/download",
				data: pdfDto,
				responseType: "arraybuffer",
				params: {
					currentUser: Session.userId,
					password: Session.password
				}
			}).then(function successCallback(response) {
				console.log("success get pdf");
				var fileName = examCreateFormService.getFileNameFromHeader(response.headers('Content-Disposition'));
				var file = new Blob([response.data], {type: 'application/pdf'});
				if (window.navigator.msSaveOrOpenBlob) {
					window.navigator.msSaveOrOpenBlob(file, fileName);
				} else {
					var fileURL = URL.createObjectURL(file);
					var a = document.createElement('a');
					a.href = fileURL;
					a.target = '_blank';
					a.download = fileName;
					document.body.appendChild(a);
					$timeout(function () {
						a.click();
						setTimeout(function () {
							document.body.removeChild(a);
							window.URL.revokeObjectURL(fileURL);
						}, 100);
					});
				}
			}, function errorCallback(response) {
				console.log(response);
			});
		};

		return examCreateFormService;
	}]);

app.controller('examCreateFormController', ['formData', '$uibModalInstance', 'examCreateFormService', '$document', function (formData, $uibModalInstance, examCreateFormService, $document) {
		var vm = this;
		vm.formType = formData.formType;
		vm.formName = formData.formName;
		vm.pdfDto = formData.pdfDto;
		vm.getPdf = function () {
			examCreateFormService.getPdf(vm.pdfDto);
			$uibModalInstance.close();
		};
		vm.cancel = function () {
			$uibModalInstance.dismiss();
		};
		vm.selectableSamples = vm.pdfDto.listPdfSampleDto;
		vm.selectedSamples = [];
		vm.init = function () {
			console.log("examCreateFormController.init()");
			for (var i = 0; i < vm.selectableSamples.length; i++) {
				vm.selectableSamples[i].ticked = false;
			}
			var height = 900;
			var width = 900;
			switch (vm.formType) {
				case "Form163":
					break;
				case "Form330":
					break;
				case "Form352":
					break;
				case "Form353":
					break;
				case "Form354":
					break;
				case "FormTrackingLabel":
					break;
				default:
			}
			var styles = {
				width: "" + width + "px",
				height: "" + height + "px",
				right: "auto",
				bottom: "auto",
				left: "-317px",
				top: "-25px"
			};
			$document.find('#create_form_popup_div').css(styles);
			console.dir(styles);
		};
		vm.multiSelectSampleClose = function () {
			var samples = [];
			var modelItems = [];
			var products = [];
			vm.selectedSamples.forEach(function (selectedSample) {
				samples.push(selectedSample.sampleNo);
				if (selectedSample.itemModel) {
					modelItems.push(selectedSample.itemModel);
				}
				if (selectedSample.productDescription) {
					products.push(selectedSample.productDescription);
				}
			});
			vm.pdfDto.samples = samples.join();
			vm.pdfDto.modelItems = modelItems.join();
			vm.pdfDto.products = products.join();
		};
		function inAry(str, strAry) {
			for (var i = 0; i < strAry.length; i++)
				if (strAry[i] === str)
					return true;
			return false;
		}
		vm.showField = function (fieldName) {
			switch (vm.formType) {
				//gz case "Form163":
				//gz	return inAry(fieldName, ["fromAddress", "individualName", "individualTitle", 'collectionDate', 'firmName', 'firmAddress', 'entryNumber', 'importerName', 'importerAddress', 'officerName']);
				//gz	break;
				//gz+ 	
			   case "Form163":
					return inAry(fieldName, ["fromAreaOfficeAddress", "individualName", "individualTitle", 'collectionDate', 'firmName', 'firmStreetAndCity', 'entryNumber', 'importerName', 'importerAddress', 'officerName']);
					break;
				case "Form330":
					return inAry(fieldName, ["formDate", "actionRequested", "toPort", "toAttn", "fromOffice", "fromAddress", "products", "samples", "modelItems", "entryNumber", "entryDate", "importerName", "importerAddress", "consigneeName", "consigneeAddress", "invoiceNum", "invoiceDate", "portOfEntry", "examinationSite", "importerNumber", "programOperation", "customsBroker", "customsBrokerAddress", "manufacturerNumbers", "countryOfOrigin", "addlActionRequested", "statute", "commentsToCbp", "distributionCC", "officerTitle", "officerPhoneNumber", "officerFax", "officerEmail"]);
					break;
				case "Form352":
					return inAry(fieldName, ["formDate", "statute", "officerName", "officerTitle", "officerPhoneNumber", "officerEmail", "entryNumber", "entryDate", "products", "modelItems", "invoiceNum", "invoiceDate", "portOfEntry", "storageLocation", "importerNumber", "importerName", "importerAddress", "customsBroker", "samples"]);
					break;
				case "Form353":
					return inAry(fieldName, ["formDate", "statute", "officerName", "officerTitle", "officerPhoneNumber", "officerEmail", "entryNumber", "entryDate", "products", "modelItems", "invoiceNum", "invoiceDate", "portOfEntry", "storageLocation", "importerNumber", "importerName", "importerAddress", "customsBroker", "samples"]);
					break;
				case "Form354":
					return inAry(fieldName, ["formDate", "importerName", "importerNumber", "importerAddress", "importerEmail", "customsBroker", "customsBrokerPhone", "customsBrokerEmail", "portOfEntry", "cbpFax", "storageLocation", "entryNumber", "entryDate", "products", "modelItems", "htsNumbers", "statute", "officerName", "officerTitle", "officerPhoneNumber", "officerEmail"]);
					break;
				case "FormTrackingLabel":
					return inAry(fieldName, ["formDate", "importerName", "importerAddress", "customsBroker", "portOfEntry", "entryNumber", "invoiceNum", "products",/*"modelItems",*/"officerName", "officerTitle", "officerPhoneNumber", "officerEmail"]);
					break;
				default:
					return false;
			}
		};
		vm.init();
	}]);

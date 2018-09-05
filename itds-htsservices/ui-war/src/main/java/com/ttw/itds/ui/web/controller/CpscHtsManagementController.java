package com.ttw.itds.ui.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ttw.itds.ui.domain.entity.CpscHtsManagementEntity;
import com.ttw.itds.ui.domain.entity.CpscHtsMgmtHistEntity;
import com.ttw.itds.ui.domain.entity.User;
import com.ttw.itds.ui.service.exceptions.CpscHtsMgmtCodeEnum;
import com.ttw.itds.ui.service.impl.CpscHtsManagementService;
import com.ttw.itds.ui.service.impl.HtsGlobalStateEnum;
import com.ttw.itds.ui.service.impl.ServiceResponse;
import com.ttw.itds.ui.shared.codec.JsonCodec;

@SpringBootApplication
@RestController
@RolesAllowed("ROLE_SYS_PROP_SAVE")
public class CpscHtsManagementController extends BaseItdsController {
		
	private final Logger auditLog = Logger.getLogger("hts.serverside");
    private final Logger log = Logger.getLogger(CpscHtsManagementController.class);

	/*
	 * "Creating a local instance of the Service class does NOT allow 
	 * the method to run asynchronously. It must be created inside a 
	 * @Configuration class or picked up by @ComponentScan."
	 */
	
    @Inject
    private CpscHtsManagementService mgmtSvc;
    
    @RequestMapping(value = "/debug/{action}/{key}/{value}",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> debug(
    		@Context HttpServletRequest request, // ???
    		@PathVariable String action, @PathVariable String key, @PathVariable String value) {
    	ServiceResponse<String> response = new ServiceResponse<>();
    	action = action.toLowerCase();
        switch (action) {
            case "listproperties":
            	String username = "unknown";
        		try {
        			super.checkSession(request);
        			username = super.getSessionUserId(request);
        		}
        		catch (Exception e) {
        		}
            	String sessionUserIdSummary = "Session User Id: " + username;
            	response.setValue(sessionUserIdSummary + mgmtSvc.debugListProperties());
                break;
            case "putsourceusername": // key is source, value is username
            	response.setValue(mgmtSvc.debugPutSourceUsername(key, value));
                break;
            case "saveasis":
            	response.setValue(mgmtSvc.debugSaveAsIs(key));
                break;
            case "setstatetransition":
            	response.setValue(mgmtSvc.debugSetStateTransition(key));
                break;
            default: 
            	throw new RuntimeException("erroneous key=" + key);
        }
        return response.getHttpResponseEntity();
    }

    @RequestMapping(value = "/save",
		    method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> saveOne(@RequestBody CpscHtsManagementEntity rhme,HttpServletRequest request,
   		 HttpServletResponse res) {
    	auditLog.error("saveOne: rhme=" + rhme);
     	try {
			super.checkSession(request);
		}
		catch (Exception e) {
			res.setStatus(HttpServletResponse.SC_FORBIDDEN);
		      HttpHeaders responseHeaders = new HttpHeaders();
		        responseHeaders.set("Cache-Control", "no-cache");
		        responseHeaders.set("Pragma", "no-cache");
		        return new ResponseEntity<String>("Access Denied", responseHeaders, HttpStatus.UNAUTHORIZED);
		}
    	ServiceResponse<String> response = null;
    	mgmtSvc.postBeanInjectionTasks();
    	String errorCodeUsingStateTransition = mgmtSvc.errorCodeUsingStateTransition("save");
    	if (errorCodeUsingStateTransition.equals("")) {
    		response = mgmtSvc.saveOne(rhme);
        	mgmtSvc.postBeanInjectionTasks();
    	} else {
    		response = new ServiceResponse<>();
    		response.setValue("{\"valid\": \"false\", \"cause\": \"" + errorCodeUsingStateTransition + "\"}");
    	}
		return response.getHttpResponseEntity();
	}

    @RequestMapping(value = "/finalize",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> makeFinal(HttpServletRequest request,
    		 HttpServletResponse res) {
     	try {
			super.checkSession(request);
		}
		catch (Exception e) {
			res.setStatus(HttpServletResponse.SC_FORBIDDEN);
			HttpHeaders responseHeaders = new HttpHeaders();
	        responseHeaders.set("Cache-Control", "no-cache");
	        responseHeaders.set("Pragma", "no-cache");
	        return new ResponseEntity<String>("Access Denied", responseHeaders, HttpStatus.UNAUTHORIZED);
		}
    	String clientUsername = "client_unk";
		try {
			clientUsername = super.getSessionUserId(request);
		}
		catch (Exception e) {
		}
    	ServiceResponse<String> response = null;
    	mgmtSvc.postBeanInjectionTasks();
    	String errorCodeUsingStateTransition = mgmtSvc.errorCodeUsingStateTransition("finalize");
    	if (errorCodeUsingStateTransition.equals("")) {
    		response = mgmtSvc.finalizeOrRevert(true, clientUsername);
        	mgmtSvc.postBeanInjectionTasks();
    	} else {
    		response = new ServiceResponse<>();
    		response.setValue("{\"valid\": \"false\", \"cause\": \"" + errorCodeUsingStateTransition + "\"}");
    	}
        return response.getHttpResponseEntity();
    }
    
    @RequestMapping(value = "/revert",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> revert(HttpServletRequest request,
    		 HttpServletResponse res) {
      	try {
    			super.checkSession(request);
    		}
    		catch (Exception e) {
    			res.setStatus(HttpServletResponse.SC_FORBIDDEN);
    		      HttpHeaders responseHeaders = new HttpHeaders();
    		        responseHeaders.set("Cache-Control", "no-cache");
    		        responseHeaders.set("Pragma", "no-cache");
    		        return new ResponseEntity<String>("Access Denied", responseHeaders, HttpStatus.UNAUTHORIZED);
    		}
    	ServiceResponse<String> response = null;
    	mgmtSvc.postBeanInjectionTasks();
    	String errorCodeUsingStateTransition = mgmtSvc.errorCodeUsingStateTransition("revert");
    	if (errorCodeUsingStateTransition.equals("")) {
    		response = mgmtSvc.finalizeOrRevert(false, "unusedByRevert");
        	mgmtSvc.postBeanInjectionTasks();
    	} else {
    		response = new ServiceResponse<>();
    		response.setValue("{\"valid\": \"false\", \"cause\": \"" + errorCodeUsingStateTransition + "\"}");
    	}
        return response.getHttpResponseEntity();
    }
    
    @RequestMapping(value = "/reset",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> reset(HttpServletRequest request,
     		 HttpServletResponse res) {
      	try {
    			super.checkSession(request);
    		}
    		catch (Exception e) {
    			res.setStatus(HttpServletResponse.SC_FORBIDDEN);
    		      HttpHeaders responseHeaders = new HttpHeaders();
    		        responseHeaders.set("Cache-Control", "no-cache");
    		        responseHeaders.set("Pragma", "no-cache");
    		        return new ResponseEntity<String>("Access Denied", responseHeaders, HttpStatus.UNAUTHORIZED);
    		}
    	ServiceResponse<String> response = null;
    	mgmtSvc.postBeanInjectionTasks();
		response = mgmtSvc.resetIfNecessary();
    	mgmtSvc.postBeanInjectionTasks();
        return response.getHttpResponseEntity();
    }
    
    @RequestMapping(value = "/enableeditingcurrent",
    		method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> enableEditing() {
    	ServiceResponse<String> response = null;
    	mgmtSvc.postBeanInjectionTasks();
    	String errorCodeUsingStateTransition = mgmtSvc.errorCodeUsingStateTransition("enableeditingcurrent");
    	if (errorCodeUsingStateTransition.equals("")) {
    		response = mgmtSvc.enableEditing();
        	mgmtSvc.postBeanInjectionTasks();
    	} else {
    		response = new ServiceResponse<>();
    		response.setValue("{\"valid\": \"false\", \"cause\": \"" + errorCodeUsingStateTransition + "\"}");
    	}
    	return response.getHttpResponseEntity();
    }

    /**
     * Returns a nested JSON representation of the current state of the HTS Services application. The top-level key-value
     * pairs are as follows:
     * 
     * username
     * The name written to the USERNAME column of the scratch table. It has 4 possible values, each corresponding to 1 of 
     * the 4 global states. For any one global state, there can be only one username. The administrator can override these 
     * values at runtime. Their defaults are:
     *      For global state CPSC_CURRENT_WIP: TEST_CURR
     *      For global state ITC_UPLOAD_WIP:   TEST_UPLD
     *      For global state FINALIZED_NO_WIP: NO_FI_USR
     *      For global state IN_TRANSITION:    NO_TR_USR
     * Example: "username": "NO_FI_USR"
     * 
     * globalState
     * Which of the 4 possible global states the HTS Services application is currently in. These 4 values cannot be 
     * overridden. Their meanings are:
     *      CPSC_CURRENT_WIP: There is a work in progress, and it originated from editing the current production codes.
     *      ITC_UPLOAD_WIP:   There is a work in progress, and it originated from an upload of a USITC spreadsheet.
     *      FINALIZED_NO_WIP: There is no work in progress. 
     *      IN_TRANSITION:    Some endpoints of the HTS Services application are momentarily unavailable while the system
     *                        transitions from one global state to another.
     * Example: "globalState": "FINALIZED_NO_WIP"
     * 
     * stateTransition
     * Provides the most precise insight into the internal state of the HTS Services application. There are 12 possible
     * values, which cannot be overridden. They are: ENABLE_START, ENABLE_END, UPLOAD_START, UPLOAD_END, SAVE_START, 
     * SAVE_END, REVERT_START, REVERT_END, FINALIZE_START, FINALIZE_END, and FINALIZE_STALL.
     * Example: "stateTransition": "REVERT_END"
     * 
     * transitioningNow
     * A boolean, true IFF globalState == IN_TRANSITION. 
     * Example: "transitioningNow": "false"
     * 
     * persistenceStatus
     * This key-value pair is specific to tracking the progress of an ongoing upload. It has 4 possible values, which 
     * cannot be overridden. Their meanings are:
     *      INITIALIZED: No upload has occurred (or been in progress) since the last time the state endpoint was hit.
     *      SCANNING:    An upload is underway, and it is in the first of its 2 stages. In this stage, no data has yet been
     *                   written to the scratch table.
     *      UNDERWAY:    An upload is underway, and it is in the second of its 2 stages. In this stage, data is being
     *                   written to the scratch table.
     *      SUCCEEDED:   An upload has successfully completed since the last time the state endpoint was hit.
     * Example: "persistenceStatus": "INITIALIZED"
     * 
     * persistenceProgressRemark
     * This key-value pair is specific to tracking the progress of an ongoing upload. Its value is a human-readable String 
     * explaining the corresponding persistenceStatus.
     * Example: "persistenceProgressRemark": "Row scanning is underway. Rows scanned: 13000. Persistence will begin once 
     *          all rows are scanned."
     * 
     * percentage
     * This key-value pair is specific to tracking the progress of an ongoing upload. Its value is an integer from 0 to 
     * 100 (inclusive) showing what percentage of the HTS codes in the uploaded USITC spreadsheet have been written to the 
     * scratch table. Its value remains at 0 during the SCANNING stage (shown by "persistenceStatus": "SCANNING"), and later
     * is reset to 0 at the same time that the persistenceStatus is reset to INITIALIZED. (Therefore it is useful only during
     * the UNDERWAY stage.)
     * Example: "percentage": 42
     * 
     * progressInRecordsPersistedToScratchTable
     * This key-value pair is specific to tracking the progress of an ongoing upload. Its value is the numerator when 
     * calculating the percentage value. When percentage is 0 or 100, the special value "not applicable" is shown; otherwise, 
     * it is the number of records that have been inserted into the scratch table so far.
     * Example: "progressInRecordsPersistedToScratchTable": "not applicable"
     * 
     * progressGoalInRecords
     * This key-value pair is specific to tracking the progress of an ongoing upload. Its value is the denominator when 
     * calculating the percentage value. When percentage is 0 or 100, the special value "not applicable" is shown; otherwise, 
     * it is the total number of records that must be inserted into the scratch table during this upload.
     * Example: "progressGoalInRecords": "not applicable"
     * 
     * progressLastCalculated
     * This key-value pair is specific to tracking the progress of an ongoing upload. Its value is the time when the most recent 
     * upload process underwent its last progress calculation. In the special case where no upload has occurred since the time 
     * when the HTS Services application was restarted, the special value "Progress not yet calculated." is shown.
     * Example: "progressLastCalculated": "2018-07-13 20:18:24"
     * 
     * timestamp
     * The time when this request for state information was fulfilled.
     * NOTE: THIS KEY-VALUE PAIR IS UNRELATED TO ITS "NEPHEW" OF THE SAME NAME.
     * Example: "timestamp": "2018-07-13 20:18:24"
     * 
     * lastUpload
     * This key has a value consisting of nested key-value pairs, all of which are specific to the outcome of the most 
     * recent upload attempt. Their values are taken from the record in table CPSC_HTS_MGMT_HIST that was written at the 
     * conclusion of that attempt (regardless of whether it succeeded or failed).
     * 
     *           timestamp
     *           The time when the most recent upload attempt concluded.
     *           NOTE: THIS KEY-VALUE PAIR IS UNRELATED TO ITS "UNCLE" OF THE SAME NAME.
     *           
     *           result
     *           Has 2 possible values: "success" or "failure". Result is "success" IFF incidentCode is "OK".
     *           
     *           incidentCode
     *           Its value is selected from the enumerated type CpscHtsMgmtCodeEnum, but only a subset of those values are
     *           used in reporting upload outcomes. The values currently in use are OK, EXCEPTION_PARSING_LINE, 
     *           EXCEPTION_PARSING_INDENT, EXCEPTION_INSERTING_TO_SCRATCH_TABLE, SAW_RESET_FLAG, EXCEPTION_READING_FILE, 
     *           and EXCEPTION_WRITING_TO_FILE_TABLE. Of these, only "OK" represents success; all others are accompanied
     *           by the sibling key-value pair "result": "failure"
     *           
     *           username
     *           For now, this value is always "TBD". It will be the end-user login name.
     *           
     *           notes
     *           Its value is a human-readable String explaining the corresponding incidentCode.
     *           
     * Example:
     *           "lastUpload": {
     *                         "timestamp": "2018-07-13 15:48:37.113", 
     *                         "result": "failure", 
     *                         "incidentCode": "EXCEPTION_INSERTING_TO_SCRATCH_TABLE", 
     *                         "username": "TBD", 
     *                         "notes": "repository error: 1000 records, range 01 - 0304530015"
     *                         }
     *                        
     * @return an object of org.springframework.http.ResponseEntity whose body is in the JSON format specified above
     */
    @RequestMapping(value = "/state", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> obtainState() {
    	mgmtSvc.postBeanInjectionTasks();
    	ServiceResponse<String> response = mgmtSvc.obtainState();
        return response.getHttpResponseEntity();
    }
    
    @RequestMapping(value = "/counts",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> obtainCounts(@RequestParam(value="detailed", required=false) String detailed,
    		@RequestParam(value="usetree", required=false) String usetree, @RequestParam(value="codeofroot", required=false) String codeofroot, 
    		@RequestParam(value="minlength", required=false) String minlength, @RequestParam(value="maxdepth", required=false) String maxdepth,
    		HttpServletRequest request, HttpServletResponse res) {
     	try {
			super.checkSession(request);
		}
		catch (Exception e) {
			res.setStatus(HttpServletResponse.SC_FORBIDDEN);
		      HttpHeaders responseHeaders = new HttpHeaders();
		        responseHeaders.set("Cache-Control", "no-cache");
		        responseHeaders.set("Pragma", "no-cache");
		        return new ResponseEntity<String>("Access Denied", responseHeaders, HttpStatus.UNAUTHORIZED);
		}
    	ServiceResponse<String> response = null;
    	mgmtSvc.postBeanInjectionTasks();
    	String errorCodeUsingStateTransition = mgmtSvc.errorCodeUsingStateTransition("counts");
    	if (errorCodeUsingStateTransition.equals("")) {
    		response = mgmtSvc.obtainCounts(Boolean.parseBoolean(detailed), Boolean.parseBoolean(usetree), codeofroot, minlength, maxdepth);
        	mgmtSvc.postBeanInjectionTasks();
    	} else {
    		response = new ServiceResponse<>();
    		response.setValue("{\"valid\": \"false\", \"cause\": \"" + errorCodeUsingStateTransition + "\"}");
    	}
        return response.getHttpResponseEntity();
    }
    
    /**
     * legacy, to support /get/0/false/current/{filter}
     */
    @RequestMapping(value = "/get") //, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void get(
    		@RequestParam(value="page", required=false) String page,
    		@RequestParam(value="pagesize", required=false) String pagesize,
    		@RequestParam(value="root", required=false) String root,
    		@RequestParam(value="mincodelength", required=false) String mincodelength,
    		@RequestParam(value="maxdepth", required=false) String maxdepth,
    		@RequestParam(value="includelongdesc", required=false) String includelongdesc,
    		@RequestParam(value="source", required=false) String source,
    		@RequestParam(value="filter", required=false) String filter,
    		@RequestParam(value="searchterm", required=false) String searchterm,
    		@RequestParam(value="format", required=false) String format,
    		HttpServletRequest request, HttpServletResponse response
    		) {
		try {
			super.checkSession(request);
		}
		catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
    	if (format != null && (format.equalsIgnoreCase("csvflat") || format.equalsIgnoreCase("csvtree"))) {
    		response.setContentType("text/csv; charset=utf-8");
    		Date now1 = new Date();
    		SimpleDateFormat formatDt = new SimpleDateFormat("MMddyyyy");
    		String fileTimeStamp = formatDt.format(now1);
    		String csvFileNamePrefix = "";
    		if (source.equalsIgnoreCase("FINALIZED_NO_WIP")) {
    			csvFileNamePrefix = "CPSCLiveReadOnly";
    		}
    		else if (source.equalsIgnoreCase("CPSC_CURRENT_WIP")) {
    			csvFileNamePrefix = "CPSCOnlyEditMode"; 
    		}
    		else if (source.equalsIgnoreCase("ITC_UPLOAD_WIP")) {
    			csvFileNamePrefix = "ITCUploadEditMode"; 
    		}
    		else if (source.equalsIgnoreCase("DIFFS_FROM_UPLOAD")) {
    			csvFileNamePrefix = "DiffsFromUpload"; 
    		}
    		else if (source.equalsIgnoreCase("CURRENT")) {
    			csvFileNamePrefix = "Current"; 
    		} else {
    			csvFileNamePrefix = "CSV";
    		}
    		String fileName = "HTSReport_" + csvFileNamePrefix + "_" + filter + "_" + (searchterm == null ? "" : searchterm + "_") + fileTimeStamp+".csv";
    		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
    		page = "0";
    		pagesize = "0";
    		root = "";
    		maxdepth = "5";
    		includelongdesc = "true";
    	} else { // json
    		response.setContentType("application/json; charset=utf-8");
    	}
    	ServiceResponse<String> res = null;
    	mgmtSvc.postBeanInjectionTasks();
    	String errorCodeUsingStateTransition = mgmtSvc.errorCodeUsingStateTransition("get");
    	if (errorCodeUsingStateTransition.equals("")) {
	    	res = mgmtSvc.get(page, pagesize, root, mincodelength, maxdepth, Boolean.parseBoolean(includelongdesc), 
	        		source, filter, searchterm, format, "");
	    	mgmtSvc.postBeanInjectionTasks();
    	} else {
    		res = new ServiceResponse<>();
    		res.setValue("{\"valid\": \"false\", \"cause\": \"" + errorCodeUsingStateTransition + "\"}");
    	}
		try {
			response.getWriter().print(res.getValue());
		} catch (IOException e) {
			log.error(e);
		}
    }    
    
    @RequestMapping(value = "/upload",
            method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> simpleUploadAndImport(@RequestParam("file") MultipartFile file,HttpServletRequest request,
      		 HttpServletResponse res) {   
    	ServiceResponse<String> response = new ServiceResponse<>();
    	mgmtSvc.postBeanInjectionTasks();
    	String errorCodeUsingStateTransition = mgmtSvc.errorCodeUsingStateTransition("upload");
    	if (errorCodeUsingStateTransition.equals("")) {
	    	String multipartFileMetadata = "(ContentType: " + file.getContentType() + ", Name: " + file.getName() +
	    			", OriginalFilename: " + file.getOriginalFilename() + ", Size: " + file.getSize() + ")";
	    	DateTime jodaDT = new DateTime(DateTimeZone.UTC);
	    	DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd-HHmmss");
	    	String strOutputDateTime = fmt.print(jodaDT);
	    	String serverSideName = "TEMP_HTS_CSV_" + strOutputDateTime;
	    	auditLog.error("in simpleUploadAndImport: : multipartFileMetadata=" + multipartFileMetadata + 
	    			", serverSideName=" + serverSideName);
	    	File serverSideFile = null;
	    	if (!file.isEmpty()) {
	    		serverSideFile = new File(serverSideName);
	    		try {
					file.transferTo(serverSideFile);
				} catch (IllegalStateException e2) {
			    	auditLog.error("in simpleUploadAndImport: IllegalStateException. serverSideName: " + serverSideName 
			    			+ ", serverSideFile.length(): " + serverSideFile.length());
				} catch (IOException e2) {
			    	auditLog.error("in simpleUploadAndImport: IOException. serverSideName=" + serverSideName 
			    			+ ", serverSideFile.length(): " + serverSideFile.length());
				}
	    	}
	    	final File serverSideFileAsFinal = serverSideFile;
	        new Thread(() -> {
	      	    	mgmtSvc.simpleUploadAndImport(serverSideName, serverSideFileAsFinal);
	        }).start();
	    	mgmtSvc.postBeanInjectionTasks();
			response.setValue("{\"result\": \"Upload and import now underway\"}");
    	} else {
    		response.setValue("{\"valid\": \"false\", \"cause\": \"" + errorCodeUsingStateTransition + "\"}");
    	}
    	return response.getHttpResponseEntity();
    }
    
    @RequestMapping(value = "/uploadprogress",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> progress(	 HttpServletRequest request,
   		 HttpServletResponse res) {
    	try {
			super.checkSession(request);
		}
		catch (Exception e) {
			res.setStatus(HttpServletResponse.SC_FORBIDDEN);
		      HttpHeaders responseHeaders = new HttpHeaders();
		        responseHeaders.set("Cache-Control", "no-cache");
		        responseHeaders.set("Pragma", "no-cache");
		        return new ResponseEntity<String>("Access Denied", responseHeaders, HttpStatus.UNAUTHORIZED);
		}
    	ServiceResponse<String> response = null;
		mgmtSvc.postBeanInjectionTasks();
    	String errorCodeUsingStateTransition = mgmtSvc.errorCodeUsingStateTransition("uploadprogress");
    	if (errorCodeUsingStateTransition.equals("")) {
    		response = mgmtSvc.serviceResponseProgress();
    		mgmtSvc.postBeanInjectionTasks();
    	} else {
    		response = new ServiceResponse<>();
    		response.setValue("{\"valid\": \"false\", \"cause\": \"" + errorCodeUsingStateTransition + "\"}");
    	}
        return response.getHttpResponseEntity();
    }
    
	@RequestMapping(value = "/appendixbsummary")
	public void getappendixbsummaryreport(@RequestParam(value="source", required=false) String source,
	 HttpServletRequest request,
	 HttpServletResponse response) {
		try {
			super.checkSession(request);
		}
		catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		ServiceResponse<String> res = null;
		mgmtSvc.postBeanInjectionTasks();
    	String errorCodeUsingStateTransition = mgmtSvc.errorCodeUsingStateTransition("appendixbsummarycsv");
		if (errorCodeUsingStateTransition.equals("")) {
			response.setContentType("text/csv; charset=utf-8");
			Date now1 = new Date();
			SimpleDateFormat formatDt = new SimpleDateFormat("yyyyMMdd");
			String fileTimeStamp = formatDt.format(now1);
			String fileName = "AppendixBSummary"+fileTimeStamp+".csv";
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			res = mgmtSvc.getSpecialReport(source, "no_filter", "", "csvflat", "appendixb");
			mgmtSvc.postBeanInjectionTasks();
		} else {
			res = new ServiceResponse<>();
    		res.setValue("{\"valid\": \"false\", \"cause\": \"" + errorCodeUsingStateTransition + "\"}");
		}
		try {
			response.getWriter().print(res.getValue());
		} catch (IOException e) {
			log.error(e);
		}
	}
	

	@RequestMapping(value = "/appendixbjurisdiction")
	public void getappendixbjurisdiction 
		(@RequestParam(value="source", required=false) String source,
		 @RequestParam(value="searchterm", required=false) String searchterm,
		 HttpServletRequest request,
		 HttpServletResponse response){
		try {
			super.checkSession(request);
		}
		catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		ServiceResponse<String> res = null;
		mgmtSvc.postBeanInjectionTasks();
		
		if(source.equalsIgnoreCase("current")|| source.equalsIgnoreCase("FINALIZED_NO_WIP") ||  source == null ) {
			searchterm = "abj";
		}
    	String errorCodeUsingStateTransition = mgmtSvc.errorCodeUsingStateTransition("appendixbjurisdiction");
		if (errorCodeUsingStateTransition.equals("")) {
			String reportType = "specialreport";
			response.setContentType("text/csv; charset=utf-8");
			Date now1 = new Date();
			SimpleDateFormat formatDt = new SimpleDateFormat("yyyyMMdd");
			String fileTimeStamp = formatDt.format(now1);
			String fileName = "AppendixBJurisdiction"+fileTimeStamp+".csv";
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			res = mgmtSvc.getSpecialReport(source, "jurisdiction",searchterm, "csvflat",reportType);
			mgmtSvc.postBeanInjectionTasks();
		} else {
			res = new ServiceResponse<>();
    		res.setValue("{\"valid\": \"false\", \"cause\": \"" + errorCodeUsingStateTransition + "\"}");
		}
		try {
			response.getWriter().print(res.getValue());
		} catch (IOException e) {
			log.error(e);
		}

	}
	
	@RequestMapping(value = "/cbpcpscjurisdiction")
	public void getcbpcpscjurisdiction 
		(@RequestParam(value="source", required=false) String source,
		 @RequestParam(value="searchterm", required=false) String searchterm,
		 HttpServletRequest request,
		 HttpServletResponse response){
		try {
			super.checkSession(request);
			User user = super.getSessionUser(request);
		}
		catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		ServiceResponse<String> res = null;
		mgmtSvc.postBeanInjectionTasks();
    	String errorCodeUsingStateTransition = mgmtSvc.errorCodeUsingStateTransition("appendixbjurisdiction");
		if (errorCodeUsingStateTransition.equals("")) {
			if(source == null) source = "current";
			response.setContentType("text/csv; charset=utf-8");
			Date now1 = new Date();
			SimpleDateFormat formatDt = new SimpleDateFormat("yyyyMMdd");
			String fileTimeStamp = formatDt.format(now1);
			String fileName = "CBP-CPSC-Jurisdiction"+fileTimeStamp+".csv";
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			res = mgmtSvc.getSpecialReport(source, "jurisdiction",searchterm, "csvflat","specialreport");
			mgmtSvc.postBeanInjectionTasks();
		} else {
			res = new ServiceResponse<>();
    		res.setValue("{\"valid\": \"false\", \"cause\": \"" + errorCodeUsingStateTransition + "\"}");
		}
		try {
			response.getWriter().print(res.getValue());
		} catch (IOException e) {
			log.error(e);
		}

	}
	@RequestMapping(value = "/cbpcpscentryfilter")
	public void getcbpcpscentryfilter 
		(@RequestParam(value="source", required=false) String source,
		 @RequestParam(value="searchterm", required=false) String searchterm,
		 HttpServletRequest request,
		 HttpServletResponse response){
		try {
			super.checkSession(request);
			User user = super.getSessionUser(request);
		}
		catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		ServiceResponse<String> res = null;
		mgmtSvc.postBeanInjectionTasks();
    	String errorCodeUsingStateTransition = mgmtSvc.errorCodeUsingStateTransition("appendixbjurisdiction");
		if (errorCodeUsingStateTransition.equals("")) {
			if(source == null) source = "current";
			response.setContentType("text/csv; charset=utf-8");
			Date now1 = new Date();
			SimpleDateFormat formatDt = new SimpleDateFormat("yyyyMMdd");
			String fileTimeStamp = formatDt.format(now1);
			String fileName = "CBP-CPSC-EntryFilter"+fileTimeStamp+".csv";
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			res = mgmtSvc.getSpecialReport(source, "targeted",searchterm, "csvflat","specialreport");
			mgmtSvc.postBeanInjectionTasks();
		} else {
			res = new ServiceResponse<>();
    		res.setValue("{\"valid\": \"false\", \"cause\": \"" + errorCodeUsingStateTransition + "\"}");
		}
		try {
			response.getWriter().print(res.getValue());
		} catch (IOException e) {
			log.error(e);
		}

	}
	
}

package com.ttw.itds.ui.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.inject.Inject;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;

//import org.springframework.web.context.Interface WebApplicationContext;

import com.ttw.itds.ui.domain.entity.CpscHtsManagementEntity;
import com.ttw.itds.ui.domain.entity.HtsMgmtLookupEntity;
import com.ttw.itds.ui.domain.repository.CpscHtsManagementRepository;
import com.ttw.itds.ui.domain.repository.HtsMgmtLookupRepository;
import com.ttw.itds.ui.service.exceptions.CpscHtsMgmtCodeEnum;
import com.ttw.itds.ui.service.exceptions.CpscHtsMgmtException;

@Service
//@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class CpscHtsManagementService {

    private final Logger auditLog = Logger.getLogger("hts.serverside");
    //private final Logger log = Logger.getLogger(CpscHtsManagementService.class);
    
    @Inject
    private SysPropertyService sysPropertyService;
    @Inject
    private HtsMgmtLookupRepository htsMgmtLookupRepository;
    @Inject
    private CpscHtsManagementRepository cpscHtsManagementRepository;
    // If we use a constructor instead of @Inject, then we'll get a "downstream" NPE as soon as 
    // HtsStateTransitionService tries to use one of its own injected repositories. See details at
    // https://stackoverflow.com/questions/21067559/spring-data-injected-repository-is-null
    @Inject
    private HtsStateTransitionService transition;
    
	private static final int sizeOfDimA = 5;
	private static final int sizeOfDimB = 4;
	private static final int sizeOfDimC = 9;
	private static final int sizeOfDimAForTriTreeCounts = 5; // code type (depth)
	private static final int sizeOfDimBForTriTreeCounts = 3; // All/J/T
	private static final int sizeOfDimCForTriTreeCounts = 7; // All/nones/adds/removes/novelties/retirees/OTHER
    private static final String SYS_PROPERTY_PREFIX = "HTS.SVC.";
    
    private boolean watchCodePrefix;
    private String codePrefixToWatch;
    private HashMap<String, String> sourceUsernameMap;
    private HtsCsvLineMap inputLines;
	private int[][][] treeCounts = new int[sizeOfDimA][sizeOfDimB][sizeOfDimC];
	private int[][][] triTreeCounts = new int[sizeOfDimAForTriTreeCounts][sizeOfDimBForTriTreeCounts][sizeOfDimCForTriTreeCounts];
    private boolean haltForReset = false;
	private int progressInRowsReadFromImportFile = 0;
	private int progressInRecordsPersistedToScratchTable = 0;
	private int progressGoalInRecords = 0;
	private String progressLastCalculated = "Progress not yet calculated.";
	private int booleanViolationsUnjustifiedParent = 0;
	private int booleanViolationsSilentParentJ = 0;
	private int booleanViolationsSilentParentT = 0;
	
    public CpscHtsManagementService() {
    	inputLines = new HtsCsvLineMap();
    	// re-initializing the following two per-instance fields here in the constructor for safety:
    	treeCounts = new int[sizeOfDimA][sizeOfDimB][sizeOfDimC];
    	triTreeCounts = new int[sizeOfDimAForTriTreeCounts][sizeOfDimBForTriTreeCounts][sizeOfDimCForTriTreeCounts];
    }
    
    private String prefixedPropertyKey(HtsSysPropertyEnum hspe) {
    	return SYS_PROPERTY_PREFIX + hspe.name();
    }

    /**
     * "Because if you want to use injected properties at initialization time 
     * it won't work, because beans won't yet be injected."
     * 
     * "If you want to call injected properties, it won't work in a constructor"
     */
    public void postBeanInjectionTasks() {
    	resetCounters();
		watchCodePrefix = sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.AUDIT_CODE_PREFIX_WATCH));
		codePrefixToWatch = sysPropertyService.getSysPropertyStringByName(prefixedPropertyKey(HtsSysPropertyEnum.AUDIT_CODE_PREFIX_TO_WATCH));
		sourceUsernameMap = new HashMap<>();
    	sourceUsernameMap.put(HtsGlobalStateEnum.CPSC_CURRENT_WIP.name(), sysPropertyService.getSysPropertyStringByName(prefixedPropertyKey(HtsSysPropertyEnum.SYS_USER_FOR_CPSC_CURRENT_WIP)));
    	sourceUsernameMap.put(HtsGlobalStateEnum.ITC_UPLOAD_WIP.name(),   sysPropertyService.getSysPropertyStringByName(prefixedPropertyKey(HtsSysPropertyEnum.SYS_USER_FOR_ITC_UPLOAD_WIP)));
    	sourceUsernameMap.put(HtsGlobalStateEnum.FINALIZED_NO_WIP.name(), sysPropertyService.getSysPropertyStringByName(prefixedPropertyKey(HtsSysPropertyEnum.SYS_USER_FOR_FINALIZED_NO_WIP)));
    	sourceUsernameMap.put(HtsGlobalStateEnum.IN_TRANSITION.name(),    sysPropertyService.getSysPropertyStringByName(prefixedPropertyKey(HtsSysPropertyEnum.SYS_USER_FOR_IN_TRANSITION)));
    	if (transition.findLatestStateTransition() != HtsStateTransitionEnum.UPLOAD_START) {
       		progressInRowsReadFromImportFile = 0;
    		progressInRecordsPersistedToScratchTable = 0;
    		progressGoalInRecords = 0;
    	}
    }
    
// START OF DEBUG METHODS ------------------------------------------------------	
    
	public String debugListProperties() {
    	String result = "Source Username Map: \n";
    	for (String k : sourceUsernameMap.keySet()) {
    		String v = sourceUsernameMap.get(k);
        	result += "   " + k + ": " + v + "\n";
    	}
        return result;
    }
	
    public String debugPutSourceUsername(String source, String username) {
    	if (       source.equals(HtsGlobalStateEnum.CPSC_CURRENT_WIP.name()) 
    			|| source.equals(HtsGlobalStateEnum.ITC_UPLOAD_WIP.name()) 
    			|| source.equals(HtsGlobalStateEnum.FINALIZED_NO_WIP.name())
    			|| source.equals(HtsGlobalStateEnum.IN_TRANSITION.name())) {
    		return sourceUsernameMap.put(source, username);
    	}
        return "(requested source was invalid)";
    }

	public String debugSetStateTransition(String st) {
		HtsStateTransitionEnum hste = HtsStateTransitionEnum.valueOf(st);
		transition.requestTransition(hste, sysPropertyService.getSysPropertyStringByName(prefixedPropertyKey(HtsSysPropertyEnum.TRANSITION_DESC_FOR_INIT)));
		return "set to " + st;
	}
	
// END OF DEBUG METHODS ------------------------------------------------------	
    
	public ServiceResponse<String> resetIfNecessary() {
		ServiceResponse<String> response = new ServiceResponse<>();
		boolean conclusivelyConsistent = false;
		boolean resetOccurred = false;
		int countFromClean = 0;
		long numOfRecordsInScratchBelongingToTheEnableWip = 0;
		long numOfRecordsInScratchBelongingToTheUploadWip = 0;
		HtsStateTransitionEnum hste = transition.findLatestStateTransition();
		switch (hste) {
		case ENABLE_START:
			flagTheResetAndIdle();
			countFromClean = cleanScratchTable(getUsernameFor(HtsGlobalStateEnum.CPSC_CURRENT_WIP));
			resetOccurred = true;
			break;
		case UPLOAD_START:
			flagTheResetAndIdle();
			countFromClean = cleanScratchTable(getUsernameFor(HtsGlobalStateEnum.ITC_UPLOAD_WIP));
			resetOccurred = true;
			break;
		case FINALIZE_START:
		case REVERT_START:
			flagTheResetAndIdle();
			countFromClean = cleanScratchTable(getUsernameFor(HtsGlobalStateEnum.CPSC_CURRENT_WIP));
			countFromClean += cleanScratchTable(getUsernameFor(HtsGlobalStateEnum.ITC_UPLOAD_WIP));
			resetOccurred = true;
			break;
		case ENABLE_END:
	    	numOfRecordsInScratchBelongingToTheEnableWip = cpscHtsManagementRepository.countByUsername(
	    			getUsernameFor(HtsGlobalStateEnum.CPSC_CURRENT_WIP));
	    	conclusivelyConsistent = numOfRecordsInScratchBelongingToTheEnableWip > 0;
	    	if (!conclusivelyConsistent) {
				flagTheResetAndIdle();
				countFromClean = cleanScratchTable(getUsernameFor(HtsGlobalStateEnum.ITC_UPLOAD_WIP));
				resetOccurred = true;
	    	}
			break;
		case UPLOAD_END:
	    	numOfRecordsInScratchBelongingToTheUploadWip = cpscHtsManagementRepository.countByUsername(
	    			getUsernameFor(HtsGlobalStateEnum.ITC_UPLOAD_WIP));
	    	conclusivelyConsistent = numOfRecordsInScratchBelongingToTheUploadWip > 0;
	    	if (!conclusivelyConsistent) {
				flagTheResetAndIdle();
				countFromClean = cleanScratchTable(getUsernameFor(HtsGlobalStateEnum.CPSC_CURRENT_WIP));
				resetOccurred = true;
	    	}
			break;
		case FINALIZE_END:
		case REVERT_END:
	    	numOfRecordsInScratchBelongingToTheEnableWip = cpscHtsManagementRepository.countByUsername(
	    			getUsernameFor(HtsGlobalStateEnum.CPSC_CURRENT_WIP));
	    	numOfRecordsInScratchBelongingToTheUploadWip = cpscHtsManagementRepository.countByUsername(
	    			getUsernameFor(HtsGlobalStateEnum.ITC_UPLOAD_WIP));
	    	conclusivelyConsistent = numOfRecordsInScratchBelongingToTheEnableWip + numOfRecordsInScratchBelongingToTheUploadWip == 0;
	    	if (!conclusivelyConsistent) {
				flagTheResetAndIdle();
				countFromClean = cleanScratchTable(getUsernameFor(HtsGlobalStateEnum.CPSC_CURRENT_WIP));
				countFromClean += cleanScratchTable(getUsernameFor(HtsGlobalStateEnum.ITC_UPLOAD_WIP));
				resetOccurred = true;
	    	}
			break;
		case FINALIZE_STALL:
			break;
		default: // no action required for other state transitions
			break;
		}
		String result = "{\"resetOccurred\": \"" + (resetOccurred ? "true" : "false") + "\", \"recordsDeletedFromScratch\": \"" + countFromClean 
				+ "\", \"haltForReset\": \"" + haltForReset + "\", \"timestamp\": \"" + finalizationTimestampAsString(false) + "\"}";
		response.setValue(result);
		if (resetOccurred) {
			transition.requestTransition(HtsStateTransitionEnum.REVERT_END, sysPropertyService.getSysPropertyStringByName(prefixedPropertyKey(HtsSysPropertyEnum.TRANSITION_DESC_FOR_RESET)));
			haltForReset = false;
		}
    	return response;
    }

	private void flagTheResetAndIdle() {
    	try {
    	    Thread.sleep(3000);
    	} catch(InterruptedException ex) {
    	    Thread.currentThread().interrupt();
    	}    	
    }
    
	private void checkForReset() throws CpscHtsMgmtException {
		if (haltForReset) {
			throw new CpscHtsMgmtException(CpscHtsMgmtCodeEnum.SAW_RESET_FLAG, 245, "saw reset flag");
		}
	}
	
	public String errorCodeUsingStateTransition(String endpoint) {
		String errorCode = "TRANSITIONING";
		HtsStateTransitionEnum hste = transition.findLatestStateTransition();
		endpoint = endpoint.toLowerCase();
		switch (endpoint) {
		case "counts": case "get": case "appendixbsummarycsv": case "appendixbjurisdiction":
			if (hste == HtsStateTransitionEnum.ENABLE_END || hste == HtsStateTransitionEnum.UPLOAD_END || hste == HtsStateTransitionEnum.SAVE_END
					|| hste == HtsStateTransitionEnum.FINALIZE_END || hste == HtsStateTransitionEnum.REVERT_END) {
				errorCode = "";
			}
		break;
		case "enableeditingcurrent": case "upload":
			if (hste == HtsStateTransitionEnum.FINALIZE_END || hste == HtsStateTransitionEnum.REVERT_END) {
				errorCode = "";
			} else if (hste == HtsStateTransitionEnum.ENABLE_END || hste == HtsStateTransitionEnum.UPLOAD_END || hste == HtsStateTransitionEnum.SAVE_END) {
				errorCode = "WIP";
			}
		break;
		case "uploadprogress":
			if (hste == HtsStateTransitionEnum.UPLOAD_START || hste == HtsStateTransitionEnum.UPLOAD_END) {
				errorCode = "";
			} else if (hste == HtsStateTransitionEnum.ENABLE_START || hste == HtsStateTransitionEnum.SAVE_START
					|| hste == HtsStateTransitionEnum.FINALIZE_START || hste == HtsStateTransitionEnum.REVERT_START) {
				errorCode = "TRANSITION_IS_NOT_AN_UPLOAD";
			} else {
				errorCode = "NO_TRANSITION";
			}
		break;
		case "save": case "finalize": case "revert":
			if (hste == HtsStateTransitionEnum.ENABLE_END || hste == HtsStateTransitionEnum.UPLOAD_END || hste == HtsStateTransitionEnum.SAVE_END) {
				errorCode = "";
			} else if (hste == HtsStateTransitionEnum.FINALIZE_END || hste == HtsStateTransitionEnum.REVERT_END) {
				errorCode = "NO_WIP";
			}
		break;
		}
		return errorCode;
	}
	
	public ServiceResponse<String> enableEditing() {
        ServiceResponse<String> response = new ServiceResponse<>();
        boolean editingCurrentIsEnabled = false;
        if (transition.getGlobalStateEnum() == HtsGlobalStateEnum.CPSC_CURRENT_WIP) {
        	// editing of current CPSC codes as a work-in-progress is already underway, therefore nothing to do here:
        	editingCurrentIsEnabled = true;
        } else if (transition.getGlobalStateEnum() == HtsGlobalStateEnum.FINALIZED_NO_WIP) {
        	String username = getUsernameFor(HtsGlobalStateEnum.CPSC_CURRENT_WIP);
        	int sequenceId = sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.SLICE_ID));
    		transition.requestTransition(HtsStateTransitionEnum.ENABLE_START, "");
    		if (sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.USE_SLICE))) {
        		cpscHtsManagementRepository.copyActivesFromLookupIntoScratchViaSeq(username, sequenceId);
    		} else {
        		cpscHtsManagementRepository.copyActivesFromLookupIntoScratch(username);
    		}
			transition.requestTransition(HtsStateTransitionEnum.ENABLE_END, "");
	        postBeanInjectionTasks();
	        editingCurrentIsEnabled = (transition.getGlobalStateEnum() == HtsGlobalStateEnum.CPSC_CURRENT_WIP);
        } else { // transition.getGlobalStateEnum() == HtsGlobalStateEnum.ITC_UPLOAD_WIP
        	// request to enable editing of current CPSC codes must be denied:
        	editingCurrentIsEnabled = false;
        }
        response.setValue("{ \"editingCurrentIsEnabled\": \"" + editingCurrentIsEnabled + "\", \"globalState\": \"" + transition.getGlobalStateEnum().name() + "\" }");
        return response;
    }

// START OF UPLOAD FEATURE ------------------------------------------------------	
    
    public void simpleUploadAndImport(String serverSideName, File serverSideFile) {
    	String username = getUsernameFor(HtsGlobalStateEnum.ITC_UPLOAD_WIP);
    	transition.requestTransition(HtsStateTransitionEnum.UPLOAD_START, serverSideName, null, serverSideFile, username);
		transition.recordIncident(HtsStateTransitionEnum.UPLOAD_START, CpscHtsMgmtCodeEnum.OK, serverSideName);
    	progressLastCalculated = finalizationTimestampAsString(false);
    	progressInRecordsPersistedToScratchTable = 0;
		List<CpscHtsManagementEntity> listAwaitingPersistence = new ArrayList<CpscHtsManagementEntity>();
		String causeForImmediateReversion = null;
		boolean scratchTableIsDirty = false;
		try {
			listAwaitingPersistence = importUploadedCsvFile(serverSideFile, username);
		} catch (CpscHtsMgmtException htsException) {
			switch (htsException.getErrorCode()) {
			case SAW_RESET_FLAG: // passed from checkForReset only
				causeForImmediateReversion = htsException.getMessage();
				break;
			case EXCEPTION_PARSING_LINE: // passed from HtsCsvLine.parseRawInput only
				causeForImmediateReversion = htsException.getMessage() + " " + htsException.getTag();
				break;
			case EXCEPTION_PARSING_INDENT: // passed from HtsCsvLine.parseRawInput only
				causeForImmediateReversion = "unparsable indent value [" + htsException.getMessage() + "] in row " + htsException.getTag();
				break;
			default:
				causeForImmediateReversion = "unknown 342";
				break;
			}
			transition.recordIncident(HtsStateTransitionEnum.UPLOAD_START, htsException.getErrorCode(), causeForImmediateReversion);
		}
		if (causeForImmediateReversion == null) {
			int desiredBatchSize = sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.DESIRED_BATCH_SIZE));
			try {
				if (desiredBatchSize == 0) {
					try {
						scratchTableIsDirty = true;
			    		cpscHtsManagementRepository.save(listAwaitingPersistence);
					} catch (Exception e) {
						throw new CpscHtsMgmtException(CpscHtsMgmtCodeEnum.EXCEPTION_INSERTING_TO_SCRATCH_TABLE, "repository error: all records", e);
					}
				} else {
					scratchTableIsDirty = true;
	    			persistScratchMapViaBatching(listAwaitingPersistence, desiredBatchSize); // CpscHtsMgmtExceptions SAW_RESET_FLAG, EXCEPTION_INSERTING_TO_SCRATCH_TABLE
				}
			} catch (CpscHtsMgmtException htsException) {
				switch (htsException.getErrorCode()) {
				case SAW_RESET_FLAG: // passed from checkForReset only
				case EXCEPTION_INSERTING_TO_SCRATCH_TABLE: // message includes HTS range if applicable
					causeForImmediateReversion = htsException.getMessage();
					break;
				default:
					causeForImmediateReversion = "unknown 369";
					break;
				}
				transition.recordIncident(HtsStateTransitionEnum.UPLOAD_START, htsException.getErrorCode(), causeForImmediateReversion);
			}
		}
    	transition.requestTransition(HtsStateTransitionEnum.UPLOAD_END, serverSideName);
    	if (causeForImmediateReversion != null) {
    		transition.requestTransition(HtsStateTransitionEnum.REVERT_START, causeForImmediateReversion);
    		int countFromClean = 0;
    		if (scratchTableIsDirty) {
    			countFromClean = cleanScratchTable();
    		}
    		transition.requestTransition(HtsStateTransitionEnum.REVERT_END, "cleaned " + countFromClean);
    	}
    }
    
    private void persistScratchMapViaBatching(Collection<CpscHtsManagementEntity> crhme, int desiredBatchSize) throws CpscHtsMgmtException {
    	progressLastCalculated = finalizationTimestampAsString(false);
    	progressInRecordsPersistedToScratchTable = 0;
    	progressGoalInRecords = crhme.size();
    	ArrayList<CpscHtsManagementEntity> currentBatch = new ArrayList<CpscHtsManagementEntity>();
    	int inThisBatchSoFar = 0;
    	for (CpscHtsManagementEntity rhme : crhme) {
			currentBatch.add(rhme);
			if (++inThisBatchSoFar >= desiredBatchSize) {
    			saveBatchAndUpdateProgress(currentBatch); // CpscHtsMgmtExceptions SAW_RESET_FLAG, EXCEPTION_INSERTING_TO_SCRATCH_TABLE
    			inThisBatchSoFar = 0;
    			currentBatch = new ArrayList<CpscHtsManagementEntity>();
    		}
    	}
    	if (!currentBatch.isEmpty()) {
    		saveBatchAndUpdateProgress(currentBatch); // CpscHtsMgmtExceptions SAW_RESET_FLAG, EXCEPTION_INSERTING_TO_SCRATCH_TABLE
    	}
    }
    
    private void saveBatchAndUpdateProgress(ArrayList<CpscHtsManagementEntity> currentBatch) throws CpscHtsMgmtException {
    	checkForReset(); // CpscHtsMgmtException iff SAW_RESET_FLAG
    	if (currentBatch != null && !currentBatch.isEmpty()) {
			try {
				cpscHtsManagementRepository.save(currentBatch);
			} catch (Exception e) {
	    		String details = "repository error: " + currentBatch.size() + " records, range " + currentBatch.get(0).getHtsCode() 
	    				+ " - " + currentBatch.get(currentBatch.size() - 1).getHtsCode();
				throw new CpscHtsMgmtException(CpscHtsMgmtCodeEnum.EXCEPTION_INSERTING_TO_SCRATCH_TABLE, details, e);
			}
	    	progressLastCalculated = finalizationTimestampAsString(false);
	    	progressInRecordsPersistedToScratchTable += currentBatch.size();
    	}
    }
    
    List<CpscHtsManagementEntity> importUploadedCsvFile(File serverSideFile, String username) throws CpscHtsMgmtException {
    	progressInRowsReadFromImportFile = 0;
		HashMap<String, CpscHtsManagementEntity> finalizedOmniStatus = lookupAllFinalizedOmniStatus(username);
		List<CpscHtsManagementEntity> result = new ArrayList<CpscHtsManagementEntity>();
		List<String> codesForTrueAddsNotEncounteredBefore = new ArrayList<String>(20000);
		List<String> codesForTrueAddsExcludedAsDuplicateLines = new ArrayList<String>(100);
		int spreadsheetRowNum = 0; // will be incremented before first row (row #1 in spreadsheet lingo) is read
		try {    	
			int NUMBER_OF_PARTS = sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.LENGTH_OF_DESCRIPTION_ARRAY));
	    	String[] descriptionParts = new String[NUMBER_OF_PARTS];
	    	for (int i = 0; i < descriptionParts.length; i++) {
	    		descriptionParts[i] = "";                                          
	    	}
			Scanner scanner = new Scanner(serverSideFile, "UTF-8");
			while (scanner.hasNext()) {
				checkForReset(); // CpscHtsMgmtException iff SAW_RESET_FLAG
				spreadsheetRowNum++;
				progressInRowsReadFromImportFile = spreadsheetRowNum;
				String potentialLine = scanner.nextLine();
				HtsCsvLine hcl = new HtsCsvLine(potentialLine, spreadsheetRowNum); // CpscHtsMgmtException iff EXCEPTION_PARSING_{LINE|INDENT}
				// detect USITC header row and skip it if found:
				if (spreadsheetRowNum == 1 && scanner.hasNext() && hcl.isHeader()) {
					spreadsheetRowNum++;
					potentialLine = scanner.nextLine();
					hcl = new HtsCsvLine(potentialLine, spreadsheetRowNum); // CpscHtsMgmtException iff EXCEPTION_PARSING_{LINE|INDENT}
				}
				// work around the problem of quoted strings continuing on the next line:
				while (!potentialLine.endsWith("\"") && scanner.hasNext()) {
					if (sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.COUNT_SPILLOVER_LINES_AS_SPREADSHEET_ROWS))) {
						spreadsheetRowNum++;
					}
					potentialLine = scanner.nextLine();
					// must not inline the nextLine call, or else loop won't exit:
					hcl.appendSpilloverLine(potentialLine);
				}
				inputLines.admit(hcl); // must precede hcl.getDescriptionFragment() for floaters to be applied
				if (!hcl.isFloater()) {
					CpscHtsManagementEntity rhmeSave = new CpscHtsManagementEntity();
					rhmeSave.setUsername(username);
					int MAX_DIGITS_OF_SPREADSHEET_ROW = 5;
					rhmeSave.setSource(String.format("%1$0" + MAX_DIGITS_OF_SPREADSHEET_ROW + "d", spreadsheetRowNum));
					String htsc = (hcl.getHtsCode() != null ? hcl.getHtsCode() : "");
					rhmeSave.setHtsCode(htsc);
					rhmeSave.setCodeType(hcl.getCodeType());
					String shortDesc = handleSpecialCharactersBeforeComparison(hcl.getDescriptionFragment());
			    	String[] allShortDescsInclusive = new String[htsc.length() / 2];
			    	for (int i = 0; i < allShortDescsInclusive.length; i++) {
			    		if (i == allShortDescsInclusive.length - 1) { // i.e. (htsc.length() / 2) == 1 + i
			    			allShortDescsInclusive[i] = hcl.getDescriptionFragment();
			    		} else {
			    			String ancCode = htsc.substring(0, (i + 1) * 2); // (i + 1) * 2 is length of HTS code for this ancestor
			    			String ancDesc = (inputLines.get(ancCode) != null ? inputLines.get(ancCode).getDescriptionFragment() : "");
			    			// get description for this ancestor from lookup table too, to compare "new" desc from
			    			// the uploaded spreadsheet to the existing ITC short description in lookup table:
		    				CpscHtsManagementEntity ancFromLookup = finalizedOmniStatus.get(ancCode);
		    				if (ancFromLookup != null) { // if missing from lookup table, use ancDesc from inputLines
		    					String cpscOverrideFromLookup = ancFromLookup.getCpscShortDescription();
			    				String oldItcDesc = ancFromLookup.getDescription();
			    				// Get the old ITC Description from lookup. Compare it to the new. If they're the same then it hasn't changed
			    				// in spreadsheet. In this case, if there was a CPSC short description override, retain it for concatenation
			    				// If we will not use CPSC short description, then we will use the old ITC description if spreadsheet had no value
		    					if (ancDesc != null && !ancDesc.isEmpty()) {
				    				if (ancDesc.equals(oldItcDesc) && cpscOverrideFromLookup != null && 
				    						!cpscOverrideFromLookup.isEmpty()) {
				    					ancDesc = cpscOverrideFromLookup;
				    				}
				    				else if (ancDesc == null || ancDesc.isEmpty()) {
				    					ancDesc = oldItcDesc;
				    				}
		    					} else { // this ancestor is not present in the spreadsheet (e.g. a chapter)
		    						if (cpscOverrideFromLookup != null && !cpscOverrideFromLookup.isEmpty()) {
		    							ancDesc = cpscOverrideFromLookup;
		    						} else {
		    							ancDesc = (oldItcDesc != null ? oldItcDesc : "");
		    						}
		    					}
		    				}
			    			allShortDescsInclusive[i] = ancDesc;
			    		}
			    	}
					setMultiDescription(shortDesc, buildConcatDescription(allShortDescsInclusive), rhmeSave);
					CpscHtsManagementEntity incumbentOrNull = finalizedOmniStatus.get(htsc);
					if (incumbentOrNull != null) { // changeStatus will be a none, or else an add-and-remove pair
						rhmeSave.setJurisdiction(incumbentOrNull.getJurisdiction());
						rhmeSave.setTargeted(incumbentOrNull.getTargeted());
						rhmeSave.setSunset(false); // this is an upload
						rhmeSave.setNotes((incumbentOrNull.getNotes() != null ? incumbentOrNull.getNotes() : ""));
						boolean itcDescsAreCloseEnough = false;
						if (sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.ITC_DESCRIPTION_SIMILARITY_THRESHHOLD)) == 19) {
							// 19 is the strictest standard, so use simplified technique:
							if (rhmeSave.getDescription().trim().equals(incumbentOrNull.getDescription().trim())) {
								itcDescsAreCloseEnough = true;
							}
						} else {
							// description_similarity_threshhold is <19, so must call utility method:
							int itcDescriptionSimilarity = itcDescriptionMatch(rhmeSave.getDescription(), incumbentOrNull.getDescription());
							if (itcDescriptionSimilarity >= sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.ITC_DESCRIPTION_SIMILARITY_THRESHHOLD))) {
								itcDescsAreCloseEnough = true;
							}
						}
						if (sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.FORCE_ITC_DESCS_TO_BE_CONSIDERED_UNEQUAL))) {
							itcDescsAreCloseEnough = false;
						}
						if (itcDescsAreCloseEnough) { // it's a none
							rhmeSave.setChangeStatus("none");
							boolean concatDescsAreCloseEnough = false;
							if (sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.ITC_DESCRIPTION_SIMILARITY_THRESHHOLD)) == 19) {
								// 19 is the strictest standard, so use simplified technique:
								if (rhmeSave.getCdescription().trim().equals(incumbentOrNull.getCdescription().trim())) {
									concatDescsAreCloseEnough = true;
								}
							} else {
								// description_similarity_threshhold is <19, so must call utility method:
								int concatDescriptionSimilarity = itcDescriptionMatch(rhmeSave.getCdescription(), incumbentOrNull.getCdescription());
								if (concatDescriptionSimilarity >= sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.ITC_DESCRIPTION_SIMILARITY_THRESHHOLD))) {
									concatDescsAreCloseEnough = true;
								}
							}
							rhmeSave.setInheritsChange(!concatDescsAreCloseEnough); // business rule: a concatenated change suffices to be considered an inherited change
							result.add(rhmeSave);
							incumbentOrNull.setReviewStatus("covered_by_a_none");
							rhmeSave.setReviewStatus("d_unchanged");
						} else { // they're too different: it's an update, therefore use an add-and-remove pair
							rhmeSave.setChangeStatus("add");
							incumbentOrNull.setChangeStatus("remove");
							incumbentOrNull.setUsername(username);
							incumbentOrNull.setReviewStatus("paired_remove");
							rhmeSave.setReviewStatus("d_paired_add");
							result.add(incumbentOrNull);
							result.add(rhmeSave);
						}
					} else { // incumbentOrNull is null, so changeStatus will be an add
						rhmeSave.setChangeStatus("add");
						rhmeSave.setJurisdiction(false);
						rhmeSave.setTargeted(false);
						rhmeSave.setSunset(false);
						rhmeSave.setNotes("");
						rhmeSave.setReviewStatus("d_true_add");
						String copyOfCodeForFilteringDuplicateLines = rhmeSave.getHtsCode();
						if (!codesForTrueAddsNotEncounteredBefore.contains(copyOfCodeForFilteringDuplicateLines)) {
							codesForTrueAddsNotEncounteredBefore.add(copyOfCodeForFilteringDuplicateLines);
							result.add(rhmeSave);
						} else {
							codesForTrueAddsExcludedAsDuplicateLines.add(copyOfCodeForFilteringDuplicateLines);
						}
					}
				}
			}
			scanner.close();
			checkForReset(); // CpscHtsMgmtException iff SAW_RESET_FLAG
			// Of the current (i.e. most-recent unexpired) REF_HTS_ALL records that lack reviewStatus of "covered_by_a_none" or "paired_remove",
			// some (a few) are true removals (i.e. retirees), with the remainder (vast majority) being previously-finalized placeholders. Must  
			// identify the previously-finalized placeholders and mark them so that they're not treated as retirees:
			HashSet<String> allCodesInImport = new HashSet<String>(20000);
			for (CpscHtsManagementEntity ur : result) {
				allCodesInImport.add(ur.getHtsCode());
			}
			// allCodesInImport has all codes present in uploaded CSV
			HashSet<String> allCodesForAncestorsMissingFromImport = calculateCodesForPlaceholders(allCodesInImport);
			// allCodesForAncestorsMissingFromImport has codes only for missing ancestors. Each missing ancestor is either present in 
			// REF_HTS_ALL, or else must be generated as a placeholder. Must convert to a list to sort:
			List<String> listOfAllCodesForAncestorsMissingFromImport = new ArrayList<String>(allCodesForAncestorsMissingFromImport);
			listOfAllCodesForAncestorsMissingFromImport.sort(null);
			for (String cfamfi : listOfAllCodesForAncestorsMissingFromImport) {
				CpscHtsManagementEntity incumbentOrNull = finalizedOmniStatus.get(cfamfi);
				if (incumbentOrNull != null) { // present in REF_HTS_ALL
					incumbentOrNull.setReviewStatus("necessary_ancestor");
					incumbentOrNull.setChangeStatus("none");
					if (cfamfi.length() > 2) {
						// because listOfAllCodesForAncestorsMissingFromImport is sorted, the parent is guaranteed to be in result:
						List<CpscHtsManagementEntity> withParentCode = new ArrayList<CpscHtsManagementEntity>();
						for (CpscHtsManagementEntity chme : result) {
							if (chme.getHtsCode().equals(cfamfi.substring(0, cfamfi.length() - 2))) {
								withParentCode.add(chme);
							}
						}
						String longDescOfParent = "";
						for (CpscHtsManagementEntity parentCandidate : withParentCode) {
							if (parentCandidate.getChangeStatus() != null && 
									(parentCandidate.getChangeStatus().equalsIgnoreCase("none") || parentCandidate.getChangeStatus().equalsIgnoreCase("add"))) {
								if (parentCandidate.getCdescription() != null) {
									longDescOfParent = parentCandidate.getCdescription();
								}
							}
						}
						if (sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.MAKING_SNAPBACK_SO_IGNORE_LOOKUP_DESCS_FOR_MISSING_ANCESTORS))) {
							setMultiDescription("", "", longDescOfParent, incumbentOrNull);
						} else {
							setMultiDescription(incumbentOrNull.getDescription(), incumbentOrNull.getCpscShortDescription(), longDescOfParent, incumbentOrNull);
						}
					} else { // is a chapter
						setMultiDescription(incumbentOrNull.getDescription(), incumbentOrNull.getCpscShortDescription(), incumbentOrNull.getCdescription(), incumbentOrNull);
					}
					result.add(incumbentOrNull);
				} // else must be generated as a placeholder
			}
			Collection<CpscHtsManagementEntity> fosv = finalizedOmniStatus.values();
			for (CpscHtsManagementEntity rhmeFromLookup : fosv) {
				checkForReset(); // CpscHtsMgmtException iff SAW_RESET_FLAG
				if (!rhmeFromLookup.getReviewStatus().equalsIgnoreCase("paired_remove")
						&& !rhmeFromLookup.getReviewStatus().equalsIgnoreCase("covered_by_a_none")
						&& !rhmeFromLookup.getReviewStatus().equalsIgnoreCase("necessary_ancestor")) {
					// "" shows unchanged from the default, so put a corresponding remove into result:
					CpscHtsManagementEntity rhmeRepresentingRemoval = new CpscHtsManagementEntity();
					rhmeRepresentingRemoval.setUsername(username);
					rhmeRepresentingRemoval.setSource("REF_HTS_ALL");
					rhmeRepresentingRemoval.setHtsCode(rhmeFromLookup.getHtsCode());
					rhmeRepresentingRemoval.setCodeType(rhmeFromLookup.getHtsCode().length());
					setMultiDescription(rhmeFromLookup, rhmeRepresentingRemoval);
					boolean protectChapters = sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.PROTECT_CHAPTERS_FROM_REMOVAL));
					if (protectChapters && rhmeRepresentingRemoval.getCodeType() == 2) {
						rhmeRepresentingRemoval.setChangeStatus("none");
						rhmeRepresentingRemoval.setReviewStatus("chapter_is_leaf");
					} else {
						rhmeRepresentingRemoval.setChangeStatus("remove");
						rhmeRepresentingRemoval.setReviewStatus("d_true_remove");
					}
					rhmeRepresentingRemoval.setJurisdiction(rhmeFromLookup.getJurisdiction());
					rhmeRepresentingRemoval.setTargeted(rhmeFromLookup.getTargeted());
					rhmeRepresentingRemoval.setSunset(false); // this is an upload
					rhmeRepresentingRemoval.setNotes(rhmeFromLookup.getNotes());
					result.add(rhmeRepresentingRemoval);
				}
			}
			HashSet<String> allNonPlaceholderCodes = new HashSet<String>(20000); // TODO all initial capacities in method
			for (CpscHtsManagementEntity ur : result) {
				allNonPlaceholderCodes.add(ur.getHtsCode());
			}
			// allNonPlaceholderCodes has (1) all codes present in uploaded CSV and, (2) from REF_HTS_ALL, (2.1) all paired removals
			// and (2.2) all true removals (retirees) and (2.3) all necessary ancestors
			HashSet<String> allCodesForPlaceholders = calculateCodesForPlaceholders(allNonPlaceholderCodes);
			// allCodesForPlaceholders has codes only for those missing ancestors that are not already in REF_HTS_ALL
			List<CpscHtsManagementEntity> entitiesForPlaceholders = generatePlaceholderEntitiesWithSpecifiedCodes(
					allCodesForPlaceholders, result, username);
			result.addAll(entitiesForPlaceholders);
			// result is now free of missing ancestors: for each entity, all ancestors are present
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (CpscHtsManagementEntity chme : result) {
			checkForReset(); // CpscHtsMgmtException iff SAW_RESET_FLAG
				chme.setModified(false); // Nothing in upload process should ever result in the modified bit being true
		}
		result.sort(null);
		return result;
    }
    
	private String buildConcatDescription(String[] allShortDescsInclusive) {
		String result = "";
		for (int i = 0; i < allShortDescsInclusive.length; i++) {
			if (!allShortDescsInclusive[i].isEmpty()) {
				if (!result.isEmpty()) {
					result += sysPropertyService.getSysPropertyStringByName(prefixedPropertyKey(HtsSysPropertyEnum.DESC_DELIMITER));
				}
				result += allShortDescsInclusive[i];
			}
		}
		return handleSpecialCharactersBeforeComparison(result);
	}
	
    private HashMap<String, CpscHtsManagementEntity> lookupAllFinalizedOmniStatus(String username) {
        int initialCapacity = sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.HASHMAP_INITIAL_CAPACITY));
        float loadFactor = 0.75F;
        HashMap<String, CpscHtsManagementEntity> finalizedOmniStatus = new HashMap<String, CpscHtsManagementEntity>(initialCapacity, loadFactor);
        List<HtsMgmtLookupEntity> lrha2e = null;
    	boolean sliced = sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.USE_SLICE));
		Date today = finalizationTimestamp(false);
    	if (sliced) {
    		lrha2e = cpscHtsManagementRepository.findAllFinalizedOmniStatusDatefulViaSeq(today, sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.SLICE_ID)));
    	} else {
    		lrha2e = cpscHtsManagementRepository.findAllFinalizedOmniStatusDateful(today);
    	}
    	lrha2e = debugScreen(lrha2e);
        lrha2e.sort(null);
    	for (HtsMgmtLookupEntity rha2e : lrha2e) {
    		CpscHtsManagementEntity rhme = new CpscHtsManagementEntity();
    		rhme.setUsername(username);
    		String code = rha2e.getHtsCode(); // non-null
    		rhme.setHtsCode(code);
    		rhme.setCodeType(code.length());
    		setMultiDescription(rha2e, rhme);
    		rhme.setJurisdiction(rha2e.getJurisdiction());
    		rhme.setTargeted(rha2e.getTargeted());
    		rhme.setChangeStatus("to_overwrite"); // will be overwritten
    		rhme.setReviewStatus("initial"); // field will be used to track lookups when setting changeStatus in new list 
    		rhme.setSource((rha2e.getSource() != null ? rha2e.getSource() : ""));
    		rhme.setSunset(rha2e.getEndDate() != null);
    		rhme.setNotes((rha2e.getNotes() != null ? rha2e.getNotes() : ""));
    		rhme.setChildren(new ArrayList<CpscHtsManagementEntity>()); // no concept of kids in ref table
    		finalizedOmniStatus.put(rhme.getHtsCode(), rhme);
    	}
    	return finalizedOmniStatus;
    }
    
    public ServiceResponse<String> serviceResponseProgress() {
    	String result = "{";
    	result += progressAsInfix();
    	result += "}";
    	ServiceResponse<String> response = new ServiceResponse<>();
    	response.setValue(result);
    	return response;    	
    }
    
    private String progressAsInfix() {
    	String persistenceStatus = ""; // will be overwritten
    	String persistenceProgressRemark = ""; // will be overwritten
		int percentage = 0; // will be overwritten unless division by zero required
    	if (progressGoalInRecords == 0) {
    		if (progressInRowsReadFromImportFile == 0) {
    			persistenceStatus = "INITIALIZED";
    			persistenceProgressRemark = "No persistence has occurred since last system restart.";
    		} else {
    			persistenceStatus = "SCANNING";
    			persistenceProgressRemark = "Row scanning is underway. Rows scanned: " + progressInRowsReadFromImportFile 
    					+ ". Persistence will begin once all rows are scanned.";
    		}
    		percentage = 0;
    	} else if (progressInRecordsPersistedToScratchTable == progressGoalInRecords) {
			persistenceStatus = "SUCCEEDED";
			persistenceProgressRemark = "Persistence has completed.";
    		percentage = 100;
    	} else {
			persistenceStatus = "UNDERWAY";
			persistenceProgressRemark = "Persistence is underway.";
    		if (progressGoalInRecords > 0) {
    			percentage = (100 * progressInRecordsPersistedToScratchTable) / progressGoalInRecords;
    		}
    	}
    	String summary = "\"persistenceStatus\": \"" + persistenceStatus + "\"";
    	summary += ", \"persistenceProgressRemark\": \"" + persistenceProgressRemark + "\"";
    	summary += ", \"percentage\": " + percentage;
    	if (percentage == 0 || percentage == 100) {
    		summary += ", \"progressInRecordsPersistedToScratchTable\": \"not applicable\"";
    		summary += ", \"progressGoalInRecords\": \"not applicable\"";
    	} else {
    		summary += ", \"progressInRecordsPersistedToScratchTable\": \"" + progressInRecordsPersistedToScratchTable + "\"";
    		summary += ", \"progressGoalInRecords\": \"" + progressGoalInRecords + "\"";
    	}
		summary += ", \"progressLastCalculated\": \"" + progressLastCalculated + "\"";
    	String strOutputDateTime = finalizationTimestampAsString(false);
    	summary += ", \"timestamp\": \"" + strOutputDateTime + "\"";
    	return summary;
    }
        
	/**
	 * 
	 * 
	 * @param previousDescriptionParts an array of short descriptions that will be used to (1) assemble the 
	 * concatenated description for this node, and possibly also (2) to prefix a floater or floaters to the 
	 * short description for this node (in the case where this node's short description should be not only the
	 * text found on the spreadsheet line where this HTS code was found, but also one or more floaters from higher 
	 * in the spreadsheet).
	 * @param indentAsInt the indentation level of this node, i.e. the value in the 2nd column of the spreadsheet
	 * on the line containing this HTS code
	 * @param theDesc the text found in the 3rd column of the spreadsheet on this HTS code's line
	 * @return
	 */
    private String[] updateDescriptionParts(String[] previousDescriptionParts, int indentAsInt, String theDesc) {
    	if (indentAsInt >= 0 && indentAsInt < previousDescriptionParts.length) {
    		String[] currentDescriptionParts = new String[previousDescriptionParts.length];    				
	    	for (int i = 0; i < currentDescriptionParts.length; i++) {
	    		currentDescriptionParts[i] = previousDescriptionParts[i];
	    	}
	    	currentDescriptionParts[indentAsInt] = theDesc;
    		for (int j = indentAsInt + 1; j < currentDescriptionParts.length; j++) {
    			currentDescriptionParts[j] = "";
    		}
    		return currentDescriptionParts;
    	} else {
    		return previousDescriptionParts;
    	}
    }			

	private String snippet(String part, int widthOfDescriptionPart) {
		if (part.equalsIgnoreCase("")) {
			return String.format("%1$" + widthOfDescriptionPart + "s", "^");
		}
		if (part.length() < widthOfDescriptionPart) {
			return String.format("%1$-" + widthOfDescriptionPart + "s", part + "_");
		}
		return String.format("%1$-" + widthOfDescriptionPart + "s", part.substring(0, (widthOfDescriptionPart/2) - 1) 
				+ "~" + part.substring(part.length() - ((widthOfDescriptionPart/2) - 1), part.length()) 
				+ "_");
	}	
	
	private String handleSpecialCharactersBeforeComparison(String s) {
		int bits = ((sysPropertyService.getSysPropertyStringByName(prefixedPropertyKey(HtsSysPropertyEnum.UNICODE_REPLACEMENTS)) == null || sysPropertyService.getSysPropertyStringByName(prefixedPropertyKey(HtsSysPropertyEnum.UNICODE_REPLACEMENTS)).equals("")) 
				? 0 : sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.UNICODE_REPLACEMENTS)));
		String result = s.replace('\n', ' '); // 1 occurrence, same desc as ...
		result = result.replace('\r', ' '); // ... this one (code=6110301560)
		if (((bits >> 0) & 1) == 1) {
			result = result.replace('\u02BA', '"'); // MODIFIER LETTER DOUBLE PRIME
		}
		if (((bits >> 1) & 1) == 1) {
			result = result.replace('\u02BC', '\''); // MODIFIER LETTER APOSTROPHE
		}
		// there are 4 occurrences of 02BB (MODIFIER LETTER TURNED COMMA), most likely typos where 02B9 (MODIFIER LETTER PRIME) was intended. In any event,
		// SQL Server is not currently preserving it (and is replacing it with '?' instead):
		if (((bits >> 2) & 1) == 1) {
			result = result.replace('\u02BB', '\'');
		}
		// there are 43 occurrences of U+2010 (HYPHEN) in the latest USITC spreadsheet. This seems to be a typo, because everywhere else the ASCII character
		// U+002D (HYPHEN-MINUS {hyphen or minus sign}) is used:
		if (((bits >> 3) & 1) == 1) {
			result = result.replace('\u2010', '-');
		}
		// must replace 02DA (RING ABOVE) with 00B0 (DEGREE SIGN), because SQL Server is not currently distinguishing between them:
		if (((bits >> 4) & 1) == 1) {
			result = result.replace('\u02DA', '\u00B0');
		}
		// must replace 00DF (LATIN SMALL LETTER SHARP S, Eszett) with 03B2 (GREEK SMALL LETTER BETA), because SQL Server is not currently distinguishing between them:
		if (((bits >> 5) & 1) == 1) {
			result = result.replace('\u00DF', '\u03B2');
		}
		// to let testing begin, replace 02B9 (MODIFIER LETTER PRIME) with single quote. However, this character appears only 
		// in chemical expressions, so it can be safely left untouched once baseline records have been re-generated, UNLESS SQL Server replaces it with '?':
		if (((bits >> 6) & 1) == 1) {
			result = result.replace('\u02B9', '\'');
		}		
		// must replace 03B3 (GREEK SMALL LETTER GAMMA) because SQL Server is not currently preserving it (and is replacing it with '?' instead):
		if (((bits >> 7) & 1) == 1) {
			result = result.replace('\u03B3', 'g');
		}
		// must replace 03C9 (GREEK SMALL LETTER OMEGA) because SQL Server is not currently preserving it (and is replacing it with '?' instead):
		if (((bits >> 8) & 1) == 1) {
			result = result.replace('\u03C9', 'o');
		}
		// must replace 0394 (GREEK CAPITAL LETTER DELTA). If SQL Server is not preserving it, then replacement must remain until DB is reconfigured:
		if (((bits >> 9) & 1) == 1) {
			result = result.replace('\u0394', 'D');
		}
		// there are 116 occurrences of 03B1 (GREEK SMALL LETTER ALPHA), all in chemical expressions. Treat similarly to 02B9 (MODIFIER LETTER PRIME):
		if (((bits >> 10) & 1) == 1) {
			result = result.replace('\u03B1', 'a');
		}
		return result;
	}
	
	private int itcDescriptionMatch(String s1, String s2) {
		// remove leading and trailing whitespace:
		s1 = s1.trim();
		s2 = s2.trim();
		// replace each whitespace region with single space:
		s1 = s1.replaceAll("\\s+"," ");
		s2 = s2.replaceAll("\\s+"," ");
		if (!sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.IGNORE_QN_MARK_WHEN_COMPARING)) || !(s1.length() == s2.length()) || (!s1.contains("?") && !s2.contains("?"))) {
			return itcDescriptionMatchOfNonWildcardSubstring(s1, s2);
		} else {
			if (!s1.contains("?")) {
				String tmp = s1;
				s1 = s2;
				s2 = tmp;
			} // assert s1.contains("?")
			int lowestRegionScore = 19;
			int beginIndexOfCurrentRegion = 12345; // doesn't matter
			int endIndexOfCurrentRegion = -1;
			String[] simpleRegions = s1.split("\\?", 0);
			for (String region : simpleRegions) {
				beginIndexOfCurrentRegion = endIndexOfCurrentRegion + 1;
				endIndexOfCurrentRegion = beginIndexOfCurrentRegion + region.length();
				lowestRegionScore = Math.min(lowestRegionScore, itcDescriptionMatchOfNonWildcardSubstring(region, s2.substring(beginIndexOfCurrentRegion, endIndexOfCurrentRegion)));
			}
			return lowestRegionScore;
		}
	}
	
	private int itcDescriptionMatchOfNonWildcardSubstring(String s1, String s2) {
		// remove leading and trailing whitespace:
		s1 = s1.trim();
		s2 = s2.trim();
		// replace each whitespace region with single space:
		s1 = s1.replaceAll("\\s+"," ");
		s2 = s2.replaceAll("\\s+"," ");
		if (s1.equals(s2)) {
			return 19;
		}
		if (s1.equalsIgnoreCase(s2)) {
			return 18;
		}
		// remove each space:
		s1 = s1.replaceAll("\\s","");
		s2 = s2.replaceAll("\\s","");
		if (s1.equals(s2)) {
			return 15;
		}
		if (s1.equalsIgnoreCase(s2)) {
			return 14;
		}
		// remove each non-alphanumeric:
		s1 = s1.replaceAll("\\P{Alnum}","");
		s2 = s2.replaceAll("\\P{Alnum}","");
		if (s1.equals(s2)) {
			return 13;
		}
		if (s1.equalsIgnoreCase(s2)) {
			return 12;
		}
		// remove each non-alphabetic:
		s1 = s1.replaceAll("\\P{Alpha}","");
		s2 = s2.replaceAll("\\P{Alpha}","");
		if (s1.equals(s2)) {
			return 11;
		}
		if (s1.equalsIgnoreCase(s2)) {
			return 10;
		}
		// truncate:
		s1 = s1.substring(0, Math.min(998, s1.length()));
		s2 = s2.substring(0, Math.min(998, s2.length()));
		if (s1.equals(s2)) {
			return 9;
		}
		if (s1.equalsIgnoreCase(s2)) {
			return 8;
		}
		// truncate:
		s1 = s1.substring(0, Math.min(222, s1.length()));
		s2 = s2.substring(0, Math.min(222, s2.length()));
		if (s1.equals(s2)) {
			return 7;
		}
		if (s1.equalsIgnoreCase(s2)) {
			return 6;
		}
		// truncate:
		s1 = s1.substring(0, Math.min(77, s1.length()));
		s2 = s2.substring(0, Math.min(77, s2.length()));
		if (s1.equals(s2)) {
			return 5;
		}
		if (s1.equalsIgnoreCase(s2)) {
			return 4;
		}
		// truncate:
		s1 = s1.substring(0, Math.min(22, s1.length()));
		s2 = s2.substring(0, Math.min(22, s2.length()));
		if (s1.equals(s2)) {
			return 3;
		}
		if (s1.equalsIgnoreCase(s2)) {
			return 2;
		}
		// truncate:
		s1 = s1.substring(0, Math.min(7, s1.length()));
		s2 = s2.substring(0, Math.min(7, s2.length()));
		if (s1.equals(s2)) {
			return 1;
		}
		if (s1.equalsIgnoreCase(s2)) {
			return 0;
		}
		return -1;
	}
	
// END OF UPLOAD FEATURE ------------------------------------------------------	

// START OF METHODS COMMON TO THE UPLOAD AND GET FEATURES ------------------------------------------------------
	
	private void setMultiDescription(String shortDesc, String longDesc, CpscHtsManagementEntity chme) {
		chme.setCdescription((longDesc == null ? "" : longDesc));
		chme.setCpscShortDescription("");
		chme.setDescription((shortDesc == null ? "" : shortDesc));
	}

	private void setMultiDescription(String shortItcDesc, String shortCpscDesc, String longDesc, CpscHtsManagementEntity chme) {
		chme.setCdescription((longDesc == null ? "" : longDesc));
		chme.setCpscShortDescription((shortCpscDesc == null ? "" : shortCpscDesc));
		chme.setDescription((shortItcDesc == null ? "" : shortItcDesc));
	}

	private void setMultiDescription(CpscHtsManagementEntity sourceChme, CpscHtsManagementEntity targetChme) {
		targetChme.setCdescription((sourceChme.getCdescription() == null ? "" : sourceChme.getCdescription()));
		targetChme.setCpscShortDescription((sourceChme.getCpscShortDescription() == null ? "" : sourceChme.getCpscShortDescription()));
		targetChme.setDescription((sourceChme.getDescription() == null ? "" : sourceChme.getDescription()));
	}
	
	private void setMultiDescription(HtsMgmtLookupEntity sourceRha2e, CpscHtsManagementEntity targetChme) {
		targetChme.setCdescription((sourceRha2e.getCpscDescription() == null ? "" : sourceRha2e.getCpscDescription()));
		targetChme.setCpscShortDescription((sourceRha2e.getCpscShortDescription() == null ? "" : sourceRha2e.getCpscShortDescription()));
		targetChme.setDescription((sourceRha2e.getItcDescription() == null ? "" : sourceRha2e.getItcDescription()));
	}

// END OF METHODS COMMON TO THE UPLOAD AND GET FEATURES ------------------------------------------------------

// START OF THE GET FEATURE ------------------------------------------------------	

	private String getUsernameForGlobalState() {
		return sourceUsernameMap.get(transition.getGlobalStateEnum().name());
	}
	
	public String getUsernameFor(HtsGlobalStateEnum enumRepresentingAnyState) {
		return sourceUsernameMap.get(enumRepresentingAnyState.name());
	}

	public ServiceResponse<String> obtainState() {
        ServiceResponse<String> response = new ServiceResponse<>();
        response.setValue("{ \"username\": \"" + getUsernameForGlobalState() 
        		+ "\", \"globalState\": \"" + transition.getGlobalStateEnum().name() + transition.stateTransitionAsInfix() 
        		+ "\", " + progressAsInfix() + transition.summarizeLastUpload() + "}");
        return response;
    }
	
    private List<CpscHtsManagementEntity> toListOfCpscHtsManagementEntities(List<HtsMgmtLookupEntity> listOfReferenceEntities,
    		String usernameForDisplay, String changeStatusForDisplay, String reviewStatusForDisplay) {
    	ArrayList<CpscHtsManagementEntity> afterConversion = new ArrayList<CpscHtsManagementEntity>();
    	if (listOfReferenceEntities != null) {
    		listOfReferenceEntities.sort(null);
        	for (HtsMgmtLookupEntity rha2e : listOfReferenceEntities) {
        		CpscHtsManagementEntity rhme = new CpscHtsManagementEntity();
        		rhme.setUsername(usernameForDisplay);
        		String code = rha2e.getHtsCode(); // non-null
        		rhme.setHtsCode(code);
        		rhme.setCodeType(code.length());
        		setMultiDescription(rha2e, rhme);
        		rhme.setJurisdiction(rha2e.getJurisdiction());
        		rhme.setTargeted(rha2e.getTargeted());
        		rhme.setChangeStatus(changeStatusForDisplay);
        		rhme.setReviewStatus(reviewStatusForDisplay);
        		rhme.setSource(rha2e.getSource());
        		rhme.setSunset(rha2e.getEndDate() != null);
        		rhme.setNotes(rha2e.getNotes());
        		// repository returns only endDates that are null or in the future:
        		rhme.setSunset(rha2e.getEndDate() != null);
        		rhme.setChildren(new ArrayList<CpscHtsManagementEntity>());
        		afterConversion.add(rhme);        		
        	}
        	afterConversion.sort(null);
    	}
    	return afterConversion;
    }
    
    private List<HtsMgmtLookupEntity> debugScreen(List<HtsMgmtLookupEntity> someRefs) {
    	List<HtsMgmtLookupEntity> result = new ArrayList<HtsMgmtLookupEntity>();
    	if (someRefs != null && someRefs.size() > 0) {
        	result = someRefs;
    	}
    	return result;
    }
    
    private List<HtsMgmtLookupEntity> obtainRefsUsingCodeList(HashSet<String> theMissings, int sequenceId) {
    	ArrayList<String> codelistAL = new ArrayList<String>(theMissings);
    	List<HtsMgmtLookupEntity> supplementRecords = new ArrayList<HtsMgmtLookupEntity>();
    	if (codelistAL.size() > 0) {
	    	boolean sliced = sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.USE_SLICE));
        	ArrayList<String> currentBatch = new ArrayList<String>();
        	List<HtsMgmtLookupEntity> batchOfSupplementRecords = new ArrayList<HtsMgmtLookupEntity>();
        	int inThisBatchSoFar = 0;
			Date today = finalizationTimestamp(false);
        	for (String code : codelistAL) {
        		if (++inThisBatchSoFar < sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.MAX_SAFE_CODELIST_SIZE_FOR_SELECTS))) {
        			currentBatch.add(code);
        		} else {
        			currentBatch.add(code);
            		currentBatch.sort(null);
            		if (sliced) {
        				batchOfSupplementRecords = cpscHtsManagementRepository.obtainRefsUsingALCodeListOrderedDatefulViaSeq(today, currentBatch, sequenceId);
            		} else {
        				batchOfSupplementRecords = cpscHtsManagementRepository.obtainRefsUsingALCodeListOrderedDateful(today, currentBatch);
            		}
            		batchOfSupplementRecords = debugScreen(batchOfSupplementRecords);
        			supplementRecords.addAll(batchOfSupplementRecords);
        			inThisBatchSoFar = 0;
        			currentBatch = new ArrayList<String>();
        		}
        	}
        	if (!currentBatch.isEmpty()) {
        		currentBatch.sort(null);
        		if (sliced) {
        			batchOfSupplementRecords = cpscHtsManagementRepository.obtainRefsUsingALCodeListOrderedDatefulViaSeq(today, currentBatch, sequenceId);
        		} else {
        			batchOfSupplementRecords = cpscHtsManagementRepository.obtainRefsUsingALCodeListOrderedDateful(today, currentBatch);
        		}
        		batchOfSupplementRecords = debugScreen(batchOfSupplementRecords);
    			supplementRecords.addAll(batchOfSupplementRecords);
        	}
    	}
    	return supplementRecords;
    }
    
    private List<CpscHtsManagementEntity> obtainMissingAncestorsFromScratchTableUsingCodeList(HashSet<String> theMissings, 
    		String username) {
    	ArrayList<String> codelistAL = new ArrayList<String>(theMissings);
    	List<CpscHtsManagementEntity> strainByUsernameToProduceSupplementRecords = new ArrayList<CpscHtsManagementEntity>();
    	if (codelistAL.size() > 0) {
        	ArrayList<String> currentBatch = new ArrayList<String>();
        	List<CpscHtsManagementEntity> batchOfStrainables = new ArrayList<CpscHtsManagementEntity>();
        	int inThisBatchSoFar = 0;
        	for (String code : codelistAL) {
        		if (++inThisBatchSoFar < sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.MAX_SAFE_CODELIST_SIZE_FOR_SELECTS))) {
        			currentBatch.add(code);
        		} else {
        			currentBatch.add(code);
            		currentBatch.sort(null);
            		batchOfStrainables = cpscHtsManagementRepository.obtainUsingALCodeListOrdered(currentBatch);
            		strainByUsernameToProduceSupplementRecords.addAll(batchOfStrainables);
        			inThisBatchSoFar = 0;
        			currentBatch = new ArrayList<String>();
        		}
        	}
        	if (!currentBatch.isEmpty()) {
        		currentBatch.sort(null);
        		batchOfStrainables = cpscHtsManagementRepository.obtainUsingALCodeListOrdered(currentBatch);
        		strainByUsernameToProduceSupplementRecords.addAll(batchOfStrainables);
        	}
    	}
    	List<CpscHtsManagementEntity> supplement = new ArrayList<CpscHtsManagementEntity>();
		for (CpscHtsManagementEntity ent : strainByUsernameToProduceSupplementRecords) {
			if (ent.getUsername().equalsIgnoreCase(username)) {
				supplement.add(ent);
			}
		}
		return supplement;
    }
    
    private HashSet<String> calculateCodesForPlaceholders(HashSet<String> allRealCodes) {
    	HashSet<String> theTens = new HashSet<String>(20000); // TODO replace all initial capacities based on input size
    	HashSet<String> theEights = new HashSet<String>(20000);
    	HashSet<String> theSixes = new HashSet<String>(6000);
    	HashSet<String> theFours = new HashSet<String>(2000);
    	HashSet<String> theTwos = new HashSet<String>(100);
    	for (String code : allRealCodes) {
    		if (code.length() == 10) {
    			theTens.add(code);
    		} else if (code.length() == 8) {
    			theEights.add(code);
    		} else if (code.length() == 6) {
    			theSixes.add(code);
    		} else if (code.length() == 4) {
    			theFours.add(code);
    		} else { // code.length() == 2
    			theTwos.add(code);
    		}
    	}
    	for (String aTen : theTens) {
    		theEights.add(aTen.substring(0, 8));
    	}
    	for (String anEight : theEights) {
    		theSixes.add(anEight.substring(0, 6));
    	}
    	for (String aSix : theSixes) {
    		theFours.add(aSix.substring(0, 4));
    	}
    	for (String aFour : theFours) {
    		theTwos.add(aFour.substring(0, 2));
    	}
    	HashSet<String> result = new HashSet<String>(50000);
    	result.addAll(theTens);
    	result.addAll(theEights);
    	result.addAll(theSixes);
    	result.addAll(theFours);
    	result.addAll(theTwos);
    	result.removeAll(allRealCodes);
    	return result;
    }
    
    private List<CpscHtsManagementEntity> generatePlaceholderEntitiesWithSpecifiedCodes(HashSet<String> allCodesForPlaceholders, 
    		List<CpscHtsManagementEntity> listForInheritingDescriptions, String username) {
    	List<CpscHtsManagementEntity> result = new ArrayList<CpscHtsManagementEntity>(10000);
        int initialCapacity = sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.HASHMAP_INITIAL_CAPACITY));
        float loadFactor = 0.75F;
        listForInheritingDescriptions.sort(null);
    	HashMap<String, CpscHtsManagementEntity> hashMapForInheritingDescriptions = populateScratchMap(
    			listForInheritingDescriptions, initialCapacity, loadFactor);
    	for (String code : allCodesForPlaceholders) {    		
    		CpscHtsManagementEntity placeholder = new CpscHtsManagementEntity();
    		// descriptions shall be inherited, so inherit them now:
			CpscHtsManagementEntity donor = hashMapForInheritingDescriptions.get(code);
			String debugAid = "x"; // so far in tests, is always overwritten
			if (donor == null && code.length() >= 4) {
				donor = hashMapForInheritingDescriptions.get(code.substring(0, code.length() - 2));
				debugAid = "a" + code.substring(0, code.length() - 2);
			}
			if (donor == null && code.length() >= 6) {
				donor = hashMapForInheritingDescriptions.get(code.substring(0, code.length() - 4));
				debugAid = "b" + code.substring(0, code.length() - 4);
			}
			if (donor == null && code.length() >= 8) {
				donor = hashMapForInheritingDescriptions.get(code.substring(0, code.length() - 6));
				debugAid = "c" + code.substring(0, code.length() - 6);
			}
			if (donor == null && code.length() == 10) { // so far in tests, is not reached
				donor = hashMapForInheritingDescriptions.get(code.substring(0, code.length() - 8));
				debugAid = "d" + code.substring(0, code.length() - 8);
			}
			if (donor != null) {
				setMultiDescription(donor, placeholder);
	    		placeholder.setReviewStatus("log_" + debugAid);
			} else {
				setMultiDescription("GENERATED_PLACEHOLDER_NO_ANCESTOR_FOUND", "GENERATED_PLACEHOLDER_NO_ANCESTOR_FOUND", placeholder);
	    		placeholder.setReviewStatus("log_GPNAF");
			}
    		placeholder.setUsername(username);
    		placeholder.setHtsCode(code);
    		placeholder.setCodeType(code.length());
    		placeholder.setJurisdiction(false);
    		placeholder.setTargeted(false);
    		placeholder.setChangeStatus("placeholder");
    		placeholder.setSource("");
    		placeholder.setSunset(false);
    		placeholder.setNotes("");
    		placeholder.setChildren(new ArrayList<CpscHtsManagementEntity>());
    		result.add(placeholder);
    	}
    	result.sort(null);
    	return result;
    }

    private List<CpscHtsManagementEntity> select(boolean selectFromLookupTable, String username, String filter, String searchterm, int sequenceId) {
    	List<CpscHtsManagementEntity> result = null;
    	if (selectFromLookupTable) {
	    	List<HtsMgmtLookupEntity> listOfRefEntities = null;
	    	boolean sliced = sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.USE_SLICE));
			Date today = finalizationTimestamp(false);
			if (filter.equalsIgnoreCase("no_filter") || filter.equalsIgnoreCase("search")) {
				if (searchterm == null || searchterm.equalsIgnoreCase("")) {
					if (sliced) {
						listOfRefEntities = cpscHtsManagementRepository.obtainRefsOrderedDatefulViaSeq(today, sequenceId);
					} else {
						listOfRefEntities = cpscHtsManagementRepository.obtainRefsOrderedDateful(today);
					}
				} else {
					if (sliced) {
						listOfRefEntities = cpscHtsManagementRepository.obtainRefsBySearchtermDatefulViaSeq(today, searchterm, searchterm, sequenceId); 
					} else {
						listOfRefEntities = cpscHtsManagementRepository.obtainRefsBySearchtermDateful(today, searchterm, searchterm); 
					}
				}
			} else if (filter.equalsIgnoreCase("jurisdiction")) {
				if (searchterm == null || searchterm.equalsIgnoreCase("")) {
					if (sliced) {
						listOfRefEntities = cpscHtsManagementRepository.obtainRefsByJurisdictionOrderedDatefulViaSeq(today, true, sequenceId); 
					} else {
						listOfRefEntities = cpscHtsManagementRepository.obtainRefsByJurisdictionOrderedDateful(today, true); 
					}
				} else {
					if (sliced) {
						listOfRefEntities = cpscHtsManagementRepository.obtainRefsByJurisdictionAndSearchtermDatefulViaSeq(today, true, searchterm, searchterm, sequenceId); 
					} else {
						listOfRefEntities = cpscHtsManagementRepository.obtainRefsByJurisdictionAndSearchtermDateful(today, true, searchterm, searchterm); 
					}
				}
			} else { // filter.equalsIgnoreCase("targeted")
				if (searchterm == null || searchterm.equalsIgnoreCase("")) {
					if (sliced) {
						listOfRefEntities = cpscHtsManagementRepository.obtainRefsByTargetedOrderedDatefulViaSeq(today, true, sequenceId); 
					} else {
						listOfRefEntities = cpscHtsManagementRepository.obtainRefsByTargetedOrderedDateful(today, true);
					}
				} else {
					if (sliced) {
						listOfRefEntities = cpscHtsManagementRepository.obtainRefsByTargetedAndSearchtermDatefulViaSeq(today, true, searchterm, searchterm, sequenceId); 
					} else {
						listOfRefEntities = cpscHtsManagementRepository.obtainRefsByTargetedAndSearchtermDateful(today, true, searchterm, searchterm); 
					}
				}
			}
			listOfRefEntities = debugScreen(listOfRefEntities);
			listOfRefEntities.sort(null);
			String changeStatusForDisplay = "none"; // (filter.equalsIgnoreCase("no_filter") || filter.equalsIgnoreCase("search") ? "none" : "placeholder" );
			String reviewStatusForDisplay = "selected";
			result = toListOfCpscHtsManagementEntities(listOfRefEntities, username, changeStatusForDisplay, reviewStatusForDisplay);
    	} else {
    		String codesearchterm = searchterm;
			if (filter.equalsIgnoreCase("no_filter") || filter.equalsIgnoreCase("search")) {
				if (searchterm == null || searchterm.equalsIgnoreCase("")) {
					result = cpscHtsManagementRepository.findByUsernameOrderByHtsCodeAsc(username);
				} else {
					result = cpscHtsManagementRepository.obtainByUsernameAndSearchterm(username, searchterm, codesearchterm);
				}
			} else if (filter.equalsIgnoreCase("jurisdiction")) {
				if (searchterm == null || searchterm.equalsIgnoreCase("")) {
					result = cpscHtsManagementRepository.findByUsernameAndJurisdictionOrderByHtsCodeAsc(username, true);
				} else {
					result = cpscHtsManagementRepository.obtainByUsernameAndJurisdictionAndSearchterm(username, true, searchterm, codesearchterm);
				}
			} else { // filter.equalsIgnoreCase("targeted")
				if (searchterm == null || searchterm.equalsIgnoreCase("")) {
					result = cpscHtsManagementRepository.findByUsernameAndTargetedOrderByHtsCodeAsc(username, true);
				} else {
					result = cpscHtsManagementRepository.obtainByUsernameAndTargetedAndSearchterm(username, true, searchterm, codesearchterm);
				}
			}
    	}
		result.sort(null);
    	return result;
    }
    
    private List<CpscHtsManagementEntity> getListUsingArbitraryState(HtsGlobalStateEnum enumRepresentingAnyState, 
    		String filter, String searchterm, boolean sourceWasDiffsFromUpload, int sequenceId) {
    	String username = getUsernameFor(enumRepresentingAnyState);
		boolean selectFromLookupTable = (enumRepresentingAnyState == HtsGlobalStateEnum.FINALIZED_NO_WIP);
		List<CpscHtsManagementEntity> selection = select(selectFromLookupTable, username, filter, searchterm, sequenceId);
		if (sourceWasDiffsFromUpload) {
			List<CpscHtsManagementEntity> allTrueDiffs = new ArrayList<CpscHtsManagementEntity>(20000);
			for (CpscHtsManagementEntity ur : selection) {
				if (ur.getChangeStatus().equalsIgnoreCase("add") || ur.getChangeStatus().equalsIgnoreCase("remove")) {
					ur.setReviewStatus("diff");
					allTrueDiffs.add(ur);
				} else if (ur.getChangeStatus().equalsIgnoreCase("none")) {
					ur.setReviewStatus("no_diff");
				} else { // placeholder, shouldn't yet be present
					ur.setReviewStatus("error");
				}
			}
			selection = allTrueDiffs; // alternative to removing the unworthies from selection
		}
		List<CpscHtsManagementEntity> sufficientEntitiesForTree = new ArrayList<CpscHtsManagementEntity>(50000);
		//
		// 1ST CONTRIBUTION TO RESULT: result set from initial DB query
		//
		sufficientEntitiesForTree.addAll(selection);
		HashSet<String> selectionCodes = new HashSet<String>(20000);
		for (CpscHtsManagementEntity ur : selection) {
			selectionCodes.add(ur.getHtsCode());
		}
		HashSet<String> allCodesForPlaceholders = calculateCodesForPlaceholders(selectionCodes);
		if (allCodesForPlaceholders.size() > 0) {
			//
			// 2ND CONTRIBUTION TO RESULT: missing ancestors will be required
			//
			// can't replace w/selection, because allUploadRecords may have been restricted by filter and/or searchterm:
			List<CpscHtsManagementEntity> missingAncestorsFoundInScratchTable = obtainMissingAncestorsFromScratchTableUsingCodeList(allCodesForPlaceholders, username);
			//
			// 2ND CONTRIBUTION TO RESULT, GROUP A: missing ancestors that were found in scratch table:
			//
			sufficientEntitiesForTree.addAll(missingAncestorsFoundInScratchTable);
			HashSet<String> codesOfMissingAncestorsFoundInScratchTable = new HashSet<String>(20000);
			for (CpscHtsManagementEntity missAncFromScratch : missingAncestorsFoundInScratchTable) {
				codesOfMissingAncestorsFoundInScratchTable.add(missAncFromScratch.getHtsCode());
			}
			// assert: allCodesForPlaceholders has codes for both (a) scratch table records that are not diffs and 
			// (b) necessary ancestors for which no scratch table record exists
			allCodesForPlaceholders.removeAll(codesOfMissingAncestorsFoundInScratchTable);
			// assert: allCodesForPlaceholders has codes only for (b) placeholders for which no scratch table record exists, i.e.
			// codes for both (b.1) necessary ancestors existing only in the lookup table and (b.2) necessary ancestors for which no record exists
			List<HtsMgmtLookupEntity> missingAncestorsFoundInRefTable = obtainRefsUsingCodeList(allCodesForPlaceholders, sequenceId);
			List<CpscHtsManagementEntity> missingAncestorsFoundInRefTableAsMgmtEntities = toListOfCpscHtsManagementEntities(
					missingAncestorsFoundInRefTable, username, "none", "placeholder_from_ref_A");
			//
			// 2ND CONTRIBUTION TO RESULT, GROUP B: missing ancestors that were not found in scratch table, but were found in ref table:
			//
			sufficientEntitiesForTree.addAll(missingAncestorsFoundInRefTableAsMgmtEntities);
			HashSet<String> codesOfMissingAncestorsFoundInRefTable = new HashSet<String>(20000);
			for (CpscHtsManagementEntity missAncFromRef : missingAncestorsFoundInRefTableAsMgmtEntities) {
				codesOfMissingAncestorsFoundInRefTable.add(missAncFromRef.getHtsCode());
			}
			allCodesForPlaceholders.removeAll(codesOfMissingAncestorsFoundInRefTable);
			// assert: allCodesForPlaceholders has codes only for necessary ancestors for which no record exists
			List<CpscHtsManagementEntity> entitiesForPlaceholders = generatePlaceholderEntitiesWithSpecifiedCodes(allCodesForPlaceholders, 
					sufficientEntitiesForTree, username);
			//
			// 2ND CONTRIBUTION TO RESULT, GROUP C: missing ancestors found in neither table:
			//
			sufficientEntitiesForTree.addAll(entitiesForPlaceholders);
		}
		sufficientEntitiesForTree.sort(null);
    	for (CpscHtsManagementEntity ent : sufficientEntitiesForTree) {
    		ent.setChildren(new ArrayList<CpscHtsManagementEntity>());
    	}
    	sufficientEntitiesForTree.sort(null);
		return sufficientEntitiesForTree;
    }
    
    private CpscHtsManagementEntity generateRoot() {
    	resetCounters();
    	CpscHtsManagementEntity root = new CpscHtsManagementEntity();
    	root.setHtsCode("");
    	root.setCodeType(0);
    	root.setChildren(new ArrayList<CpscHtsManagementEntity>());
		return root;
    }
    
    private void resetCounters() {
		treeCounts = new int[sizeOfDimA][sizeOfDimB][sizeOfDimC];
		for (int a = 0; a < sizeOfDimA; a++) {
			for (int b = 0; b < sizeOfDimB; b++) {
				for (int c = 0; c < sizeOfDimC; c++) {
					treeCounts[a][b][c] = 0;
				}
			}
		}
		triTreeCounts = new int[sizeOfDimAForTriTreeCounts][sizeOfDimBForTriTreeCounts][sizeOfDimCForTriTreeCounts];
		for (int a = 0; a < sizeOfDimAForTriTreeCounts; a++) {
			for (int b = 0; b < sizeOfDimBForTriTreeCounts; b++) {
				for (int c = 0; c < sizeOfDimCForTriTreeCounts; c++) {
					triTreeCounts[a][b][c] = 0;
				}
			}
		}
		booleanViolationsUnjustifiedParent = 0;
		booleanViolationsSilentParentJ = 0;
		booleanViolationsSilentParentT = 0;
    }

    private CpscHtsManagementEntity toNodeInSelfSufficient(CpscHtsManagementEntity root, String codeOfRoot) {
    	CpscHtsManagementEntity result = root;
    	if (codeOfRoot != null && (codeOfRoot.length() == 2 || codeOfRoot.length() == 4 || codeOfRoot.length() == 6 || codeOfRoot.length() == 8 || codeOfRoot.length() == 10)) {
    		result = obtainInTree(root, codeOfRoot);
    	}
    	return result;
    }
    
    private String getAsJson(CpscHtsManagementEntity root, String deFactoSource, String filter, String searchterm, String format, 
    		int sequenceId, String codeOfRoot, int minCodeLength, boolean includeLongDescFromJsonOutput, int maxDepth, String reportType) {
		String result = "error 1090: must overwrite";
		HtsGlobalStateEnum globalStateOrQueryLookup = transition.getGlobalStateEnum();
		if (deFactoSource.equalsIgnoreCase("current")) {
			globalStateOrQueryLookup = HtsGlobalStateEnum.FINALIZED_NO_WIP;
		}
		
		if(reportType.equalsIgnoreCase("appendixb") ) {
			minCodeLength =10;
		}
		if (minCodeLength == 10) {
			
			if(reportType.equalsIgnoreCase("appendixb") ) {
				result=formatCSVForAppendixBSummary(globalStateOrQueryLookup,deFactoSource, includeLongDescFromJsonOutput, sequenceId);
			}
		
			else {
				result = formatJsonOfFlatList(globalStateOrQueryLookup, includeLongDescFromJsonOutput, sequenceId);
			}
		
		} else if (maxDepth == 0) {
			result = formatJsonOfOneNode(globalStateOrQueryLookup, codeOfRoot, includeLongDescFromJsonOutput, sequenceId);
		} else {
			String tempsearchterm = "";
			boolean sourceWasDiffsFromUpload = false;
			if (deFactoSource.equalsIgnoreCase("diffs_from_upload")) {
	        	// assert transition.getGlobalStateEnum() == HtsGlobalStateEnum.ITC_UPLOAD_WIP
				sourceWasDiffsFromUpload = true;
	    	}
			if ( (filter !=null &&filter.equalsIgnoreCase("jurisdiction")) && (searchterm != null && searchterm.equalsIgnoreCase("abj") ) ) { tempsearchterm = "abj"; searchterm = "";}
	    	List<CpscHtsManagementEntity> selfSufficientList = getListUsingArbitraryState(globalStateOrQueryLookup, 
	    			filter, searchterm, sourceWasDiffsFromUpload, sequenceId);
			populateWholeHtsTreeFromSelfSufficient(root, 0, 0, selfSufficientList);
			if (format.equalsIgnoreCase("json")) {
				result = formatJsonFromSelfSufficient(toNodeInSelfSufficient(root, codeOfRoot), deFactoSource, filter, 
						searchterm, getUsernameForGlobalState(), selfSufficientList.size(), includeLongDescFromJsonOutput, maxDepth);
			} else if (format.equalsIgnoreCase("csvflat")) { 
				
				if(reportType.equalsIgnoreCase("specialreport")) {
					result = formatCSVFromSelfSufficient(root, deFactoSource, filter, tempsearchterm,
							getUsernameForGlobalState(), selfSufficientList.size(), false,true);
				}
				
				else {
					result = formatCSVFromSelfSufficient(root, deFactoSource, filter, searchterm,
							getUsernameForGlobalState(), selfSufficientList.size(), false,false);
				}
			} else if (format.equalsIgnoreCase("csvtree")) {
					result = formatCSVFromSelfSufficient(root, deFactoSource, filter, searchterm,
							getUsernameForGlobalState(), selfSufficientList.size(), true,false);
			}
		}
    	return result;
    }
    
    private String getPaginatedJson(CpscHtsManagementEntity root, int pageOrZero, int pagesizeOrZero, String deFactoSource, 
    		String filter, String searchterm, int sequenceId, String codeOfRoot, int minCodeLength, int maxDepth, boolean includeLongDescFromJsonOutput) {
		String result = "error 1274: must overwrite";
		HtsGlobalStateEnum globalStateOrQueryLookup = transition.getGlobalStateEnum();
		if (deFactoSource.equalsIgnoreCase("current")) {
			globalStateOrQueryLookup = HtsGlobalStateEnum.FINALIZED_NO_WIP;
		}
		if (minCodeLength == 10) {
			result = formatJsonOfFlatList(globalStateOrQueryLookup, includeLongDescFromJsonOutput, sequenceId);
		} else if (maxDepth == 0) {
			result = formatJsonOfOneNode(globalStateOrQueryLookup, codeOfRoot, includeLongDescFromJsonOutput, sequenceId);
		} else {
	    	boolean sourceWasDiffsFromUpload = false;
			if (deFactoSource.equalsIgnoreCase("diffs_from_upload")) {
	        	// assert transition.getGlobalStateEnum() == HtsGlobalStateEnum.ITC_UPLOAD_WIP
				sourceWasDiffsFromUpload = true;
	    	}
	    	List<CpscHtsManagementEntity> selfSufficientList = getListUsingArbitraryState(globalStateOrQueryLookup, 
	    			filter, searchterm, sourceWasDiffsFromUpload, sequenceId);
			populateWholeHtsTreeFromSelfSufficient(root, pageOrZero, pagesizeOrZero, selfSufficientList);
	    	result = formatJsonFromSelfSufficient(toNodeInSelfSufficient(root, codeOfRoot), deFactoSource, filter, 
	    			searchterm, getUsernameForGlobalState(), selfSufficientList.size(), includeLongDescFromJsonOutput, maxDepth);
		}
    	return result;
    }
    
    /**
     * allow "current" to select from REF_HTS_ALL regardless of current global state
     * allow "all_from_upload" as alias for "ITC_UPLOAD_WIP"
     * filter has 4 possible values: jurisdiction, targeted, no_filter, search
     */
    public ServiceResponse<String> getSpecialReport(String source, String filter, String searchterm, String format, String reportType) {
    	return get("0", "0", "", "0", "5", true, source, filter, searchterm, format, reportType);
    }
    		
    public ServiceResponse<String> get(String pageAsString, String pagesizeAsString, String codeOfRoot, String minCodeLengthAsString, String maxDepthAsString,
    		boolean includeLongDescFromJsonOutput, String source, String filter, String searchterm, String format, String reportType) {
        ServiceResponse<String> response = new ServiceResponse<>();
    	if (source == null) {
    		source = "current";
    	}
    	if (filter == null) {
    		filter = "no_filter";
    	}
    	if (format == null) {
    		format = "json";
    	}
    	if (searchterm == null) {
    		searchterm = "";
    	}
    	
		int minCodeLength = 0;
		if (minCodeLengthAsString != null) {
			try {
				minCodeLength = Integer.parseInt(minCodeLengthAsString);
			} catch (NumberFormatException nfe) {
			}
		}
        int maxDepth = 5;
		if (maxDepthAsString != null) {
			try {
				maxDepth = Integer.parseInt(maxDepthAsString);
			} catch (NumberFormatException nfe) {
			}
		}
		if (maxDepth == 0) {
			pageAsString = "0";
			pagesizeAsString = "0";
			includeLongDescFromJsonOutput = true;
			filter = "no_filter";
			searchterm = null;
		}
        boolean validSourceGivenPresentState = false;
        if (source.equalsIgnoreCase("current")) {
        	validSourceGivenPresentState = true;
        } else if (transition.getGlobalStateEnum() == HtsGlobalStateEnum.FINALIZED_NO_WIP) {
        	if (source.equalsIgnoreCase("FINALIZED_NO_WIP")) {
        		validSourceGivenPresentState = true;
        	}
        } else if (transition.getGlobalStateEnum() == HtsGlobalStateEnum.CPSC_CURRENT_WIP) {
        	if (source.equalsIgnoreCase("CPSC_CURRENT_WIP")) {
        		validSourceGivenPresentState = true;
        	}
        } else { // transition.getGlobalStateEnum() == HtsGlobalStateEnum.ITC_UPLOAD_WIP
        	if (source.equalsIgnoreCase("ITC_UPLOAD_WIP") || source.equalsIgnoreCase("all_from_upload") 
        			|| source.equalsIgnoreCase("diffs_from_upload")) {
        		validSourceGivenPresentState = true;
        	}
        }
        if (!validSourceGivenPresentState) {
        	response.setResponseCode(ServiceResponseCodeEnum.SOURCE_INVALID_FOR_STATE);
        	return response;
        }
        if (!(filter.equalsIgnoreCase("jurisdiction") 
        		|| filter.equalsIgnoreCase("targeted")
        		|| filter.equalsIgnoreCase("no_filter") 
        		|| filter.equalsIgnoreCase("search"))) {
        	response.setResponseCode(ServiceResponseCodeEnum.UNDEFINED_FILTER);
        	return response;
        }
        if (filter.equalsIgnoreCase("search") && (searchterm == null || searchterm.equalsIgnoreCase(""))) {
        	response.setResponseCode(ServiceResponseCodeEnum.UNSUPPORTED_SEARCH);
        	return response;
        }
        // resolve the grandfathered alias:
        if (source.equalsIgnoreCase("all_from_upload")) { // allow "all_from_upload" as alias for "ITC_UPLOAD_WIP"
        	source = "ITC_UPLOAD_WIP";
        }
        int pageOrZero = 0;
        int pagesizeOrZero = 0;
		if (pageAsString != null && pagesizeAsString != null) {
			try {
				pageOrZero = Integer.parseInt(pageAsString);
				pagesizeOrZero = Integer.parseInt(pagesizeAsString);
			} catch (NumberFormatException nfe) {
			}
		}
    	int sequenceId = sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.SLICE_ID));
    	if (pageOrZero > 0 && pagesizeOrZero > 0) {
        	response.setValue(getPaginatedJson(generateRoot(), pageOrZero, pagesizeOrZero, source, filter, searchterm, sequenceId, codeOfRoot, minCodeLength, maxDepth, includeLongDescFromJsonOutput));
    	} else {
    		response.setValue(getAsJson(generateRoot(), source, filter, searchterm, format, sequenceId, codeOfRoot, minCodeLength, includeLongDescFromJsonOutput, maxDepth, reportType));
    	}
    	return response;
    }
    
    private HashMap<String, CpscHtsManagementEntity> populateScratchMap(Collection<CpscHtsManagementEntity> crhme, 
    		int initialCapacity, float loadFactor) {
    	HashMap<String, CpscHtsManagementEntity> scratchMap = new HashMap<String, CpscHtsManagementEntity>(initialCapacity, loadFactor);
    	if (crhme != null) {
	    	for (CpscHtsManagementEntity rhme : crhme) { // precondition: crhme is sorted
	    		if (rhme.getChildren() == null) {
	    			rhme.setChildren(new ArrayList<CpscHtsManagementEntity>());
	    		}
	    		scratchMap.put(rhme.getHtsCode(), rhme);
	    	}
    	}
    	return scratchMap;
    }

    private String formatJsonOfFlatList(HtsGlobalStateEnum enumRepresentingAnyState, boolean includeLongDescFromJsonOutput, int sequenceId) {
    	String username = getUsernameFor(enumRepresentingAnyState);
    	List<CpscHtsManagementEntity> chmeList = null;
    	if (enumRepresentingAnyState == HtsGlobalStateEnum.FINALIZED_NO_WIP) { // select from lookup table
	    	List<HtsMgmtLookupEntity> rhaeList = null;
			Date today = finalizationTimestamp(false);
			if (sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.USE_SLICE))) {
				rhaeList = cpscHtsManagementRepository.obtainRefsByLengthOfCodeOrderedDatefulViaSeq(today, 10, sequenceId);
			} else {
				rhaeList = cpscHtsManagementRepository.obtainRefsByLengthOfCodeOrderedDateful(today, 10);
			}
			chmeList = toListOfCpscHtsManagementEntities(rhaeList, username, "none", "selected");
    	} else {
    		chmeList = cpscHtsManagementRepository.findByUsernameAndCodeTypeOrderByHtsCodeAsc(username, 10);
    	}
    	chmeList.sort(null);
    	StringBuilder sb = new StringBuilder();
    	sb.append("[ ");
    	boolean hasPredecessor = false;
    	for (CpscHtsManagementEntity chme : chmeList) {
    		if (hasPredecessor) {
    	    	sb.append(", ");
    		}
			sb.append(formatJsonOfNodeDisregardingChildren(chme, includeLongDescFromJsonOutput, enumRepresentingAnyState == HtsGlobalStateEnum.FINALIZED_NO_WIP));
			hasPredecessor = true;
    	}
    	sb.append(" ]");
		return sb.toString();
    }
    
    private String formatJsonOfOneNode(HtsGlobalStateEnum enumRepresentingAnyState, String code, boolean includeLongDescFromJsonOutput, int sequenceId) {
    	String username = getUsernameFor(enumRepresentingAnyState);
    	CpscHtsManagementEntity rhme = null;
    	if (enumRepresentingAnyState == HtsGlobalStateEnum.FINALIZED_NO_WIP) { // select from lookup table
	    	List<HtsMgmtLookupEntity> listOfOneRefEntity = null;
			Date today = finalizationTimestamp(false);
			if (sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.USE_SLICE))) {
				listOfOneRefEntity = cpscHtsManagementRepository.obtainRefByCodeOrderedDatefulViaSeq(today, code, sequenceId);
			} else {
				listOfOneRefEntity = cpscHtsManagementRepository.obtainRefByCodeOrderedDateful(today, code);
			}
			List<CpscHtsManagementEntity> listOfOne = toListOfCpscHtsManagementEntities(listOfOneRefEntity, username, "none", "selected");
			rhme = listOfOne.get(0);
    	} else {
    		rhme = cpscHtsManagementRepository.findOneByUsernameAndHtsCodeAndChangeStatus(username, code, "none");
    		if (rhme == null) {
    			rhme = cpscHtsManagementRepository.findOneByUsernameAndHtsCodeAndChangeStatus(username, code, "add");
    		}
    		if (rhme == null) {
    			rhme = cpscHtsManagementRepository.findOneByUsernameAndHtsCodeAndChangeStatus(username, code, "remove");
    		}
    	}
		return formatJsonOfNodeDisregardingChildren(rhme, includeLongDescFromJsonOutput, enumRepresentingAnyState == HtsGlobalStateEnum.FINALIZED_NO_WIP);
    }
    
    private String formatJsonFromSelfSufficient(CpscHtsManagementEntity rootOfTreeOrSubtree,
			String source, String wasFilter, String wasSearchterm, String username, int recordCount, 
			boolean includeLongDescFromJsonOutput, int maxDepth) {
		StringBuilder sb = new StringBuilder();
		sb.append("{ \"username\": \"" + username + "\", ");
		sb.append("\"globalState\": \"" + transition.getGlobalStateEnum().name() + "\", ");
		sb.append("\"source\": \"" + escapeForJson(source) + "\", ");
		sb.append("\"filter\": \"" + escapeForJson(wasFilter) + "\", ");
		sb.append("\"searchterm\": \"" + escapeForJson(wasSearchterm) + "\", ");
		sb.append("\"recordCount\": \"" + recordCount + "\", ");
		sb.append("\"htsView\": [ ");
		sb.append(recurseSubtree(rootOfTreeOrSubtree, true, includeLongDescFromJsonOutput, 0, false, maxDepth, false, false,
				(source.equalsIgnoreCase("current") || source.equalsIgnoreCase("FINALIZED_NO_WIP"))));
		sb.append("] }");
		return sb.toString();
    }

	private CpscHtsManagementEntity obtainAmongKidsCreatingIfNecessary(CpscHtsManagementEntity parent, String codeOfNeededChild,
			String username, String source, String ultimateCause) {
		CpscHtsManagementEntity result = null;
		List<CpscHtsManagementEntity> kids = getSortedChildrenOrCreateEmpty(parent);
		List<CpscHtsManagementEntity> kidsWithMatchingCode = new ArrayList<CpscHtsManagementEntity>();
		for (CpscHtsManagementEntity kid : kids) {
			if (kid.getHtsCode().equalsIgnoreCase(codeOfNeededChild)) {
				kidsWithMatchingCode.add(kid);
			}
		}
		if (kidsWithMatchingCode.size() == 1) {
			String theChangeStatusOfOnlyMatch = kidsWithMatchingCode.get(0).getChangeStatus();
			if (theChangeStatusOfOnlyMatch.equalsIgnoreCase("none") 
					|| theChangeStatusOfOnlyMatch.equalsIgnoreCase("add") 
					|| theChangeStatusOfOnlyMatch.equalsIgnoreCase("placeholder")) {
				result = kidsWithMatchingCode.get(0);
			} // else is a remove, so must generate sibling
		} else if (kidsWithMatchingCode.size() == 2) {
			String theChangeStatusOfFirstMatch = kidsWithMatchingCode.get(0).getChangeStatus();
			String theChangeStatusOfSecondMatch = kidsWithMatchingCode.get(1).getChangeStatus();
			if (theChangeStatusOfFirstMatch.equalsIgnoreCase("add")) {
				result = kidsWithMatchingCode.get(0);
			} else if (theChangeStatusOfSecondMatch.equalsIgnoreCase("add")) {
				result = kidsWithMatchingCode.get(1);
			}
		} // else either (a) too many matches for code (kidsWithMatchingCode.size() > 2) 
		  // or (b) kidsWithMatchingCode empty, so must generate, so leave needToAdd unchanged (i.e. true)
		if (result == null) {
			CpscHtsManagementEntity generatedChild = new CpscHtsManagementEntity();
			generatedChild.setUsername(username);
			generatedChild.setSource(source);
			generatedChild.setHtsCode(codeOfNeededChild);
			generatedChild.setCodeType(codeOfNeededChild.length());
			setMultiDescription("GENERATED_PLACEHOLDER_A", "GENERATED_PLACEHOLDER_A", generatedChild);
			generatedChild.setChangeStatus("placeholder");
			generatedChild.setJurisdiction(false);
			generatedChild.setTargeted(false);
			generatedChild.setReviewStatus("GENERATED_FOR " + ultimateCause);
			generatedChild.setSunset(false);
			generatedChild.setNotes("");
			generatedChild.setChildren(new ArrayList<CpscHtsManagementEntity>());
			parent.getChildren().add(generatedChild);
			parent.getChildren().sort(null);
			result = generatedChild;
		}
		return result;
	}
	
	private CpscHtsManagementEntity obtainCertainFromTreeOrSubtree(CpscHtsManagementEntity root, CpscHtsManagementEntity rootOfTreeOrSubtree, String code) {
		// precondition: the node with the code IS in the subtree
		CpscHtsManagementEntity result = null;
		if (code.equalsIgnoreCase("")) {
			result = root;
		} else if (rootOfTreeOrSubtree.getHtsCode().equalsIgnoreCase(code)) {
			result = rootOfTreeOrSubtree;
		} else {
			CpscHtsManagementEntity goalOrElseToRecurseOn = null;
			List<CpscHtsManagementEntity> kids = rootOfTreeOrSubtree.getChildren(); // has kids by precondition
			// we need one of these kids, either to return or to recurse on
			List<CpscHtsManagementEntity> kidsWithAppropriateCodes = new ArrayList<CpscHtsManagementEntity>();
			for (CpscHtsManagementEntity kid : kids) {
				if (code.toLowerCase().startsWith(kid.getHtsCode().toLowerCase())) {
					kidsWithAppropriateCodes.add(kid);
				}
			} // it'll be, or be under, one of the kids. There may be an add/remove pair to choose from.
			if (kidsWithAppropriateCodes.size() == 2) {
				if (kidsWithAppropriateCodes.get(0).getChangeStatus().equalsIgnoreCase("add")) {
					goalOrElseToRecurseOn = kidsWithAppropriateCodes.get(0);
				} else { // the remove is at index 0 and the add is at index 1
					goalOrElseToRecurseOn = kidsWithAppropriateCodes.get(1);
				}
			} else if (kidsWithAppropriateCodes.size() == 1) {
				goalOrElseToRecurseOn = kidsWithAppropriateCodes.get(0);
			} // else kidsWithAppropriateCodes.size() == 0 ... or > 2 ???
			if (goalOrElseToRecurseOn != null && goalOrElseToRecurseOn.getHtsCode().length() == code.length()) { // they must be equal
				result = goalOrElseToRecurseOn;
			} else { // must descend
				result = obtainCertainFromTreeOrSubtree(root, goalOrElseToRecurseOn, code);
			}
		}
		return result;
	}
	
	private void pruneTreeToPage(CpscHtsManagementEntity root, int page, int pagesize) {
		if (page <= 0 || pagesize <= 0 || root.getChildren() == null || root.getChildren().size() == 0) {
			return;
		}
		int chaptersToLeftOfPage = (page - 1) * pagesize;
		int chaptersNotToRightOfPage = chaptersToLeftOfPage + pagesize;
		List<CpscHtsManagementEntity> chaptersToKeep = new ArrayList<CpscHtsManagementEntity>();
		int index = 0;
		for (CpscHtsManagementEntity chme : root.getChildren()) {
			index++;
			if (index > chaptersToLeftOfPage && index <= chaptersNotToRightOfPage) {
				chaptersToKeep.add(chme);
			}
		}
		root.setChildren(chaptersToKeep);
	}
	
	private void pruneTreeByChapterRange(CpscHtsManagementEntity root, int startingChapter, int endingChapter) {
		if (startingChapter >= 0 && endingChapter <= 99 && startingChapter <= endingChapter 
				&& root.getChildren() != null && root.getChildren().size() > 0) {
			List<CpscHtsManagementEntity> chaptersToKeep = new ArrayList<CpscHtsManagementEntity>();
			int chap = 0;
			for (CpscHtsManagementEntity chme : root.getChildren()) {
				chap++;
				if (chap >= startingChapter && chap <= endingChapter) {
					chaptersToKeep.add(chme);
				}
			}
			root.setChildren(chaptersToKeep);
		}
	}
	
	private void populateWholeHtsTreeFromSelfSufficient(CpscHtsManagementEntity root, int pageOrZero, int pagesizeOrZero, List<CpscHtsManagementEntity> selfSufficientList) {
		// precondition: root has been (re-)initialized
		// precondition: selfSufficientList is not null
		selfSufficientList.sort(null);
		for (CpscHtsManagementEntity rhme : selfSufficientList) {
			String code = rhme.getHtsCode();
			CpscHtsManagementEntity rightfulGuaranteedParent = null;
			if (code.length() == 2) {
				rightfulGuaranteedParent = root;
			} else { 
				// the required parent is guaranteed to be in list AND to be present in tree under root
				String codeOfParent = code.substring(0, code.length() - 2);
				rightfulGuaranteedParent = obtainCertainFromTreeOrSubtree(root, root, codeOfParent);
			}
			rightfulGuaranteedParent.getChildren().add(rhme);
		}
		if (pageOrZero > 0 && pagesizeOrZero > 0) {
			pruneTreeToPage(root, pageOrZero, pagesizeOrZero);
		}
	}

	private void populateHtsTree(CpscHtsManagementEntity root, List<CpscHtsManagementEntity> listRefHtsMid) {
		listRefHtsMid.sort(null);
		for (CpscHtsManagementEntity rhme : listRefHtsMid) {
			String code = rhme.getHtsCode();
			String myCS = rhme.getChangeStatus();
			CpscHtsManagementEntity rightfulGuaranteedParent = null;
			if (code.length() == 2) {
				rightfulGuaranteedParent = root;
			} else { // parent may not yet exist
				if (code.length() == 4) {
					String codeOf2DigitAncestor = code.substring(0, 2);
					rightfulGuaranteedParent = obtainAmongKidsCreatingIfNecessary(root, 
							codeOf2DigitAncestor, rhme.getUsername(), rhme.getSource(), code);
				} else if (code.length() == 6) {
					String codeOf2DigitAncestor = code.substring(0, 2);
					CpscHtsManagementEntity rightfulGuaranteed2DigitAncestor = obtainAmongKidsCreatingIfNecessary(root, 
							codeOf2DigitAncestor, rhme.getUsername(), rhme.getSource(), code);
					String codeOf4DigitAncestor = code.substring(0, 4);
					rightfulGuaranteedParent = obtainAmongKidsCreatingIfNecessary(rightfulGuaranteed2DigitAncestor, 
							codeOf4DigitAncestor, rhme.getUsername(), rhme.getSource(), code);
				} else if (code.length() == 8) {
					String codeOf2DigitAncestor = code.substring(0, 2);
					CpscHtsManagementEntity rightfulGuaranteed2DigitAncestor = obtainAmongKidsCreatingIfNecessary(root, 
							codeOf2DigitAncestor, rhme.getUsername(), rhme.getSource(), code);
					String codeOf4DigitAncestor = code.substring(0, 4);
					CpscHtsManagementEntity rightfulGuaranteed4DigitAncestor = obtainAmongKidsCreatingIfNecessary(rightfulGuaranteed2DigitAncestor, 
							codeOf4DigitAncestor, rhme.getUsername(), rhme.getSource(), code);
					String codeOf6DigitAncestor = code.substring(0, 6);
					rightfulGuaranteedParent = obtainAmongKidsCreatingIfNecessary(rightfulGuaranteed4DigitAncestor, 
							codeOf6DigitAncestor, rhme.getUsername(), rhme.getSource(), code);
				} else { // code.length() == 10
					String codeOf2DigitAncestor = code.substring(0, 2);
					CpscHtsManagementEntity rightfulGuaranteed2DigitAncestor = obtainAmongKidsCreatingIfNecessary(root, 
							codeOf2DigitAncestor, rhme.getUsername(), rhme.getSource(), code);
					String codeOf4DigitAncestor = code.substring(0, 4);
					CpscHtsManagementEntity rightfulGuaranteed4DigitAncestor = obtainAmongKidsCreatingIfNecessary(rightfulGuaranteed2DigitAncestor, 
							codeOf4DigitAncestor, rhme.getUsername(), rhme.getSource(), code);
					String codeOf6DigitAncestor = code.substring(0, 6);
					CpscHtsManagementEntity rightfulGuaranteed6DigitAncestor = obtainAmongKidsCreatingIfNecessary(rightfulGuaranteed4DigitAncestor, 
							codeOf6DigitAncestor, rhme.getUsername(), rhme.getSource(), code);
					String codeOf8DigitAncestor = code.substring(0, 8);
					rightfulGuaranteedParent = obtainAmongKidsCreatingIfNecessary(rightfulGuaranteed6DigitAncestor, 
							codeOf8DigitAncestor, rhme.getUsername(), rhme.getSource(), code);
				}
			}
			List<CpscHtsManagementEntity> siblings = getSortedChildrenOrCreateEmpty(rightfulGuaranteedParent);
			List<CpscHtsManagementEntity> siblingsWithMatchingCode = new ArrayList<CpscHtsManagementEntity>();
			for (CpscHtsManagementEntity sibling : siblings) {
				if (sibling.getHtsCode().equalsIgnoreCase(code)) {
					siblingsWithMatchingCode.add(sibling);
				}
			}
			if (siblingsWithMatchingCode.size() == 0) {
				rightfulGuaranteedParent.getChildren().add(rhme);
			} else if (siblingsWithMatchingCode.size() == 1) {
				String otherCS = siblingsWithMatchingCode.get(0).getChangeStatus();
				if (otherCS.equalsIgnoreCase("add") && myCS.equalsIgnoreCase("remove")) {
					rightfulGuaranteedParent.getChildren().add(rhme);
				} else if (otherCS.equalsIgnoreCase("remove") && myCS.equalsIgnoreCase("add")) {
					rightfulGuaranteedParent.getChildren().add(rhme);
				}
			} // else siblingsWithMatchingCode.size() > 1
			rightfulGuaranteedParent.getChildren().sort(null);
		}		
	}
	
	private void populateHtsTree(CpscHtsManagementEntity root, Map<String, CpscHtsManagementEntity> scratchMap, String username) {
		for (CpscHtsManagementEntity rhme : scratchMap.values()) {
			List<String> lineageCodes = generateLineageCodes(rhme.getHtsCode());
			for (String code : lineageCodes) {
				CpscHtsManagementEntity ancestorOrSelf = scratchMap.get(code);
				if (ancestorOrSelf == null) { 
					// it's either a nonexistent ancestor, or an ancestor that exists in the scratch 
					// table but that isn't in this search result set (i.e. it wasn't returned by the 
					// SELECT). This is common when the scratchMap was populated
					// by a search, rather than by getting all codes. It shouldn't happen quite so much when getting
					// all codes EXCEPT for the fact that chapters (2-digit codes) are always missing (and lots of others)
					CpscHtsManagementEntity fromElsewhereInScratchTable = cpscHtsManagementRepository.findOneByUsernameAndHtsCode(username, code);
					// TODO what if there is more than 1 such record? Are children necessarily being added to the correct one?
					// may want to use findOneByUsernameAndHtsCodeAndChangeStatus instead
					if (fromElsewhereInScratchTable == null) {
						CpscHtsManagementEntity missingAncestor = new CpscHtsManagementEntity();
						missingAncestor.setUsername(rhme.getUsername());
						missingAncestor.setSource(rhme.getSource());
						missingAncestor.setHtsCode(code);
						missingAncestor.setCodeType(code.length());
						setMultiDescription("GENERATED_PLACEHOLDER_X", "GENERATED_PLACEHOLDER_X", missingAncestor);
						missingAncestor.setChangeStatus("none");
						missingAncestor.setJurisdiction(rhme.getJurisdiction());
						missingAncestor.setTargeted(rhme.getTargeted());
						missingAncestor.setReviewStatus("GNRTD_FOR " + rhme.getHtsCode());
						missingAncestor.setSunset(false);
						missingAncestor.setNotes("");
						missingAncestor.setChildren(new ArrayList<CpscHtsManagementEntity>());
						ancestorOrSelf = missingAncestor;						
					} else {
						if (fromElsewhereInScratchTable.getChildren() == null) {
							fromElsewhereInScratchTable.setChildren(new ArrayList<CpscHtsManagementEntity>());
						}
						ancestorOrSelf = fromElsewhereInScratchTable;
					}					
				}
				String codeOfNewChild = ancestorOrSelf.getHtsCode();
				CpscHtsManagementEntity rightfulGuaranteedParent = null;
				if (codeOfNewChild.length() == 2) {
					rightfulGuaranteedParent = root;
				} else {
					String codeOfKnownParent= codeOfNewChild.substring(0, codeOfNewChild.length() - 2);
					rightfulGuaranteedParent = obtainInTree(root, codeOfKnownParent);
				}
				CpscHtsManagementEntity parent = rightfulGuaranteedParent;
				boolean needToAdd = true;
				List<CpscHtsManagementEntity> kids = getSortedChildrenOrCreateEmpty(parent);
				for (CpscHtsManagementEntity kid : kids) {
					if (kid.getHtsCode().equalsIgnoreCase(ancestorOrSelf.getHtsCode())) { // TODO add vs. remove
						needToAdd = false;
					}
				}
				if (needToAdd) {
					parent.getChildren().add(ancestorOrSelf);
					parent.getChildren().sort(null);
				}
			}
		}
	}
	
	private List<String> generateLineageCodes(String code) {
		List<String> result = new ArrayList<>();
		if (code.length() > 2) {
			result.add(code.substring(0, 2));
		}
		if (code.length() > 4) {
			result.add(code.substring(0, 4));
		}
		if (code.length() > 6) {
			result.add(code.substring(0, 6));
		}
		if (code.length() > 8) {
			result.add(code.substring(0, 8));
		}
		result.add(code);
		return result;
	}
	
	private CpscHtsManagementEntity obtainInTree(CpscHtsManagementEntity rootOfSubtree, String codeCertainToFind) {
		CpscHtsManagementEntity result = null;
		boolean found = false;
		int debugCounter = 0;
		while (!found) {
			debugCounter++;
			if (rootOfSubtree.getHtsCode().equals(codeCertainToFind)) {
				result = rootOfSubtree;
				found = true;
			} else {
				List<CpscHtsManagementEntity> children = getSortedChildrenOrCreateEmpty(rootOfSubtree);
				List<CpscHtsManagementEntity> plausibleChildren = new ArrayList<CpscHtsManagementEntity>();
				for (CpscHtsManagementEntity child : children) {
					if (codeCertainToFind.startsWith(child.getHtsCode())) {
						plausibleChildren.add(child);
						plausibleChildren.sort(null);
					}
				}
				boolean decided = false;
				CpscHtsManagementEntity decidedUpon = null;
				if (plausibleChildren.size() == 1) {
					decided = true;
					decidedUpon = plausibleChildren.get(0);
				} else {
					int indexIntoPlausibleChildren = -1;
					while (++indexIntoPlausibleChildren < plausibleChildren.size() && !decided) {
						if (plausibleChildren.get(indexIntoPlausibleChildren).getChangeStatus() != null
								&&
								plausibleChildren.get(indexIntoPlausibleChildren).getChangeStatus().equalsIgnoreCase("add")) {
							decided = true;
							decidedUpon = plausibleChildren.get(indexIntoPlausibleChildren);
						}
					}
					if (!decided) {
						decided = true;
						decidedUpon = plausibleChildren.get(0);
					}
				}
				result = obtainInTree(decidedUpon, codeCertainToFind);
				found = true;
			}
		}
		// assert result not null
		return result;
	}
		
	private List<CpscHtsManagementEntity> getSortedChildrenOrCreateEmpty(CpscHtsManagementEntity rhme) {
		if (rhme.getChildren() == null) {
			List<CpscHtsManagementEntity> emptyList = new ArrayList<CpscHtsManagementEntity>();
			rhme.setChildren(emptyList);
			return rhme.getChildren();
		} else {
			List<CpscHtsManagementEntity> preexistingList = rhme.getChildren();
			if (preexistingList.size() > 1) {
				preexistingList.sort(null); // TODO unnecessary slowdown???
			}
			return preexistingList;
		}
	}
	
	private String escapeForJson(String s) {
		return (s == null ? "" : s.replace("\"", "\\\""));
	}
	
	private String formatJsonOfNodeDisregardingChildren(CpscHtsManagementEntity rhme, boolean includeLongDescFromJsonOutput, boolean showSunsets) {
		StringBuilder sb = new StringBuilder();
		sb.append("{ \"htsCode\": \"" + rhme.getHtsCode() + "\", ");
		sb.append("\"htsCodeType\": \"" + rhme.getCodeType() + "\", ");
		String descriptionToShow = rhme.getDescription();
		boolean isItcDescription = true;
		if (rhme.getCpscShortDescription() != null 
				&& rhme.getCpscShortDescription().length() > 0
				&& !rhme.getCpscShortDescription().equalsIgnoreCase(rhme.getDescription())) {
			descriptionToShow = rhme.getCpscShortDescription();
			isItcDescription = false;
		}
		sb.append("\"htsDescription\": \"" 
				+ escapeForJson(truncateIfDebug(descriptionToShow == null ? "" : descriptionToShow)) + "\", ");
		sb.append("\"isItcDescription\": \"" + isItcDescription + "\", ");
		if (includeLongDescFromJsonOutput) {
			sb.append("\"cpscDescription\": \"" + escapeForJson(rhme.getCdescription()) + "\", ");
		}
		sb.append("\"jurisdiction\": \"" + rhme.getJurisdiction() + "\", ");
		sb.append("\"targeted\": \"" + rhme.getTargeted() + "\", ");
		sb.append("\"modified\": \"" + (rhme.getModified() == null ? "false" : rhme.getModified()) + "\", ");
		sb.append("\"sunset\": \"" + ((rhme.getSunset() == null || !showSunsets) ? "false" : rhme.getSunset()) + "\", ");
		sb.append("\"changeStatus\": \"" + rhme.getChangeStatus() + "\", ");
        if (sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.INCLUDE_REVIEW_STATUS_IN_JSON))) {
        	sb.append("\"reviewStatus\": \"a " + rhme.getReviewStatus() + "\", ");
        }
		sb.append("\"notes\": \"" + escapeForJson((rhme.getNotes() == null ? "" : rhme.getNotes())) + "\"}");			
		return sb.toString();
	}
	
	private int booleanPairToInt(Boolean j, Boolean t) {
		int result = 0;
		if (j != null) {
			result = (j ? 1 : 2);
		}
		if (t != null) {
			result += (t ? 3 : 6);
		}
		return result;
	}
	
	private int statusToInt(String htsCode, boolean tallyAsPaired, String changeStatus) {
		int result = 1; // not zero: zeroth element tallies all
		if (changeStatus.equalsIgnoreCase("none")) {
			if (tallyAsPaired) {
				result = 6; // ERROR
			} else {
				result = 1;
			}
		} else {
			if (tallyAsPaired) {
				if (changeStatus.equalsIgnoreCase("add")) {
					result = 2;
				} else if (changeStatus.equalsIgnoreCase("remove")) {
					result = 3;
				} else {
					result = 6; // ERROR
				}
			} else {
				if (changeStatus.equalsIgnoreCase("add")) {
					result = 4;
				} else if (changeStatus.equalsIgnoreCase("remove")) {
					result = 5;
				} else {
					result = 6; // ERROR
				}
			}
		}
		return result;
	}
	
	private String summarizeBooleans(Boolean j, Boolean t, int type) {
		StringBuilder sb = new StringBuilder();
		int pairAsInt = booleanPairToInt(j, t);
		switch (pairAsInt) {
		case 0: sb.append("\u25CF"); // BLACK CIRCLE
		break;
		case 1: sb.append("\u25E0"); // UPPER HALF CIRCLE
		break;
		case 2: sb.append("\u25E1"); // LOWER HALF CIRCLE
		break;
		case 3: sb.append("\u25D6"); // LEFT HALF BLACK CIRCLE
		break;
		case 4: sb.append((type == 0 ? ":" : "\u2982")); // Z NOTATION TYPE COLON
		break;
		case 5: sb.append((type == 0 ? "!" : "\u00A1")); // INVERTED EXCLAMATION MARK
		break;
		case 6: sb.append("\u25D7"); // RIGHT HALF BLACK CIRCLE
		break;
		case 7: sb.append((type == 0 ? "." : "\u3002")); // IDEOGRAPHIC FULL STOP
		break;
		case 8: sb.append((type == 0 ? " " : "\u25AD")); // WHITE RECTANGLE
		break;
		}
		return sb.toString();
	}
	
	private void tallyViolators(String ofParent, String ofChild) {
		String a = ofParent.substring(0, 1);
		String b = ofChild.substring(0, 1);
		if ((a.equalsIgnoreCase(":") && !b.equalsIgnoreCase(":")) || (a.equalsIgnoreCase(".") && b.equalsIgnoreCase(" "))) {
			booleanViolationsUnjustifiedParent++;
		}
	}
	
	private String recurseSubtree(CpscHtsManagementEntity rhme, boolean outputAsJson, boolean includeLongDescIfJsonOutput, int guard, boolean hasSiblingsToRight, 
			int maxDepth, boolean isKnownToBeOnlyChild, boolean tallyAsPaired, boolean showSunsets) {
		StringBuilder sb = new StringBuilder();
		if (outputAsJson && rhme.getCodeType() > 0) {
			sb.append("{ \"data\" : ");
			sb.append(formatJsonOfNodeDisregardingChildren(rhme, includeLongDescIfJsonOutput, showSunsets));
			sb.append(", \"children\": [ ");
		}
		if (maxDepth > 0) {
			int numOfKids = getSortedChildrenOrCreateEmpty(rhme).size();
			if (rhme.getCodeType() > 0) {
				int indexForDimA = (rhme.getCodeType() - 2) / 2;
				int indexForDimB = (numOfKids < 3 ? numOfKids : 3);
				int indexForDimC = booleanPairToInt(rhme.getJurisdiction(), rhme.getTargeted());
				treeCounts[indexForDimA][indexForDimB][indexForDimC]++;
			}
			if (rhme.getCodeType() > 0) {
				int indexForDimAForTriTreeCounts = (rhme.getCodeType() - 2) / 2;
				int indexForDimBForTriTreeCounts = (rhme.getTargeted() ? 2 : (rhme.getJurisdiction() ? 1 : 0));
				int indexForDimCForTriTreeCounts = statusToInt(rhme.getHtsCode(), tallyAsPaired, rhme.getChangeStatus());
				triTreeCounts[indexForDimAForTriTreeCounts][indexForDimBForTriTreeCounts][indexForDimCForTriTreeCounts]++;
				// incr the tracker for "all" too:
				triTreeCounts[indexForDimAForTriTreeCounts][indexForDimBForTriTreeCounts][0]++;
			}			
			if (guard < 10 && numOfKids > 0) { // replace 10 with better safety
				int whichKidIsCurrent = 0;
				List<CpscHtsManagementEntity> sortedList = rhme.getChildren();
				sortedList.sort(null);
				HashSet<String> codesOfPairs = new HashSet<String>();
				String prevCode = "";
				for (CpscHtsManagementEntity childForPairSpotting : sortedList) {
					if (childForPairSpotting.getHtsCode().equalsIgnoreCase(prevCode)) {
						codesOfPairs.add(childForPairSpotting.getHtsCode());
					} else {
						prevCode = childForPairSpotting.getHtsCode();
					}
				}
				String fromRecursion = "";
				String myBooleans = summarizeBooleans(rhme.getJurisdiction(), rhme.getTargeted(), 0);
				int numOfKidsSeenWithJurisdictionTrue = 0;
				int numOfKidsSeenWithTargetedTrue = 0;
				for (CpscHtsManagementEntity child : sortedList) {
					boolean regardAsPaired = false;
					if (codesOfPairs.contains(child.getHtsCode())) {
						regardAsPaired = true;
					}
					if (outputAsJson) {
						sb.append(recurseSubtree(child, outputAsJson, includeLongDescIfJsonOutput, guard + 1, (++whichKidIsCurrent < numOfKids), 
								maxDepth - 1, numOfKids == 1, regardAsPaired, showSunsets));
					} else {
						String currChildBooleansAndDescendants = recurseSubtree(child, outputAsJson, includeLongDescIfJsonOutput, guard + 1, (++whichKidIsCurrent < numOfKids), 
								maxDepth - 1, numOfKids == 1, regardAsPaired, showSunsets);
						fromRecursion += currChildBooleansAndDescendants;
						String booleansOfThisChildSpecifically = currChildBooleansAndDescendants.substring(0, 1);
						if (booleansOfThisChildSpecifically.equalsIgnoreCase(":") || booleansOfThisChildSpecifically.equalsIgnoreCase(".")) {
							numOfKidsSeenWithJurisdictionTrue++;
							if (booleansOfThisChildSpecifically.equalsIgnoreCase(":")) {
								numOfKidsSeenWithTargetedTrue++;
							}
						}
						tallyViolators(myBooleans, booleansOfThisChildSpecifically);
					}
				}
				if (!outputAsJson) {
					if (sortedList.size() == numOfKidsSeenWithJurisdictionTrue && (rhme.getJurisdiction() == null || !rhme.getJurisdiction())) {
						booleanViolationsSilentParentJ++;
					}
					if (sortedList.size() == numOfKidsSeenWithTargetedTrue && (rhme.getTargeted() == null || !rhme.getTargeted())) {
						booleanViolationsSilentParentT++;
					}
					if (fromRecursion.contains("<") || fromRecursion.contains(">") || fromRecursion.contains("\u2039") || fromRecursion.contains("\u203A")) {
						sb.append(myBooleans);
						sb.append("<");
						sb.append(fromRecursion);
						sb.append(">");
					} else if (fromRecursion.contains("(") || fromRecursion.contains(")")) {
						sb.append(myBooleans);
						sb.append("\u2039"); // SINGLE LEFT-POINTING ANGLE QUOTATION MARK
						sb.append(fromRecursion);
						sb.append("\u203A"); // SINGLE RIGHT-POINTING ANGLE QUOTATION MARK
					} else if (fromRecursion.length() > 1) {
						sb.append(myBooleans);
						sb.append("(");
						sb.append(fromRecursion);
						sb.append(")");
					} else if (fromRecursion.length() == 1) { // booleansOfMyOnlyChild
						if (myBooleans.equals(fromRecursion)) {
							sb.append(myBooleans);
							sb.append("-");
							sb.append(fromRecursion);
						} else {
							sb.append(myBooleans);
							sb.append("|");
							sb.append(fromRecursion);
						}
					} else {
						sb.append("{" + fromRecursion + "}");
					}
				}
			} else { // Is a leaf.
				if (!outputAsJson && rhme.getCodeType() > 0) {
					sb.append(summarizeBooleans(rhme.getJurisdiction(), rhme.getTargeted(), 0));
				}
			}
		} else if (!outputAsJson) {
			sb.append(summarizeBooleans(rhme.getJurisdiction(), rhme.getTargeted(), 0));
		}
		if (outputAsJson && rhme.getCodeType() > 0) {
			if (hasSiblingsToRight) {
				sb.append("] }, ");
			} else {
				sb.append("] } ");
			}
		}
		return sb.toString();
	}
	
	private String truncateIfDebug(String descPossiblyWithQuotes) {
		String descMadeSafe = descPossiblyWithQuotes.replace('\n', ' '); // 1 occurrence, same desc as ...
		descMadeSafe = descMadeSafe.replace('\r', ' '); // ... this one (code=6110301560)
		String result = descMadeSafe;
		return result;
	}
	
// END OF THE GET FEATURE ------------------------------------------------------	

// START OF THE FINALIZE FEATURE -----------------------------------------------	

	private boolean flippedJurisdictionOrTargetedFromLookup(CpscHtsManagementEntity fromScratchTable, 
			HashMap<String, CpscHtsManagementEntity> finalizedOmniStatus) {
		boolean result = false;
		CpscHtsManagementEntity incumbentOrNull = finalizedOmniStatus.get(fromScratchTable.getHtsCode());
		if (incumbentOrNull != null) {
			if ((incumbentOrNull.getJurisdiction() != fromScratchTable.getJurisdiction())
					||
					(incumbentOrNull.getTargeted() != fromScratchTable.getTargeted())) {
				result = true;
			}
		} // else code is a novelty (true add), so return false
		return result;
	}
	
	private boolean alteredNotesFromLookup(CpscHtsManagementEntity fromScratchTable, 
			HashMap<String, CpscHtsManagementEntity> finalizedOmniStatus) {
		boolean result = false;
		CpscHtsManagementEntity incumbentOrNull = finalizedOmniStatus.get(fromScratchTable.getHtsCode());
		if (incumbentOrNull != null) {
			if (!incumbentOrNull.getNotes().equalsIgnoreCase(fromScratchTable.getNotes())) {
				result = true;
			}
		} // else code is a novelty (true add), so return false
		return result;
	}
	
	private int finalizePlaceholders(String userForScratch, Date startDateTime, Date endDateTime, ArrayList<String> codesOfAllPlaceholders) {
		int result = 0;
		if (codesOfAllPlaceholders.size() > 0) {
        	ArrayList<String> currentBatch = new ArrayList<String>();
        	int inThisBatchSoFar = 0;
        	for (String code : codesOfAllPlaceholders) {
        		if (++inThisBatchSoFar < sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.MAX_SAFE_CODELIST_SIZE_FOR_SELECTS))) {
        			currentBatch.add(code);
        		} else {
        			currentBatch.add(code);
            		currentBatch.sort(null);
					result += cpscHtsManagementRepository.insertPlaceholdersFromScratchIntoLookup("plcehldr", userForScratch, startDateTime, endDateTime, currentBatch);
        			inThisBatchSoFar = 0;
        			currentBatch = new ArrayList<String>();
        		}
        	}
        	if (!currentBatch.isEmpty()) {
        		currentBatch.sort(null);
				result += cpscHtsManagementRepository.insertPlaceholdersFromScratchIntoLookup("plcehldr", userForScratch, startDateTime, endDateTime, currentBatch);
        	}
		}
		return result;
	}
	
	private int finalizeTrueAdds(String userForScratch, Date startDateTime, Date endDateTime, ArrayList<String> codesOfTrueAdds) {
		int result = 0;
		if (codesOfTrueAdds.size() > 0) {
        	ArrayList<String> currentBatch = new ArrayList<String>();
        	int inThisBatchSoFar = 0;
        	for (String code : codesOfTrueAdds) {
        		if (++inThisBatchSoFar < sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.MAX_SAFE_CODELIST_SIZE_FOR_SELECTS))) {
        			currentBatch.add(code);
        		} else {
        			currentBatch.add(code);
            		currentBatch.sort(null);
					result += cpscHtsManagementRepository.insertNoveltiesFromScratchIntoLookup(
							"novelty", userForScratch, startDateTime, endDateTime, currentBatch);
        			inThisBatchSoFar = 0;
        			currentBatch = new ArrayList<String>();
        		}
        	}
        	if (!currentBatch.isEmpty()) {
        		currentBatch.sort(null);
				result += cpscHtsManagementRepository.insertNoveltiesFromScratchIntoLookup(
						"novelty", userForScratch, startDateTime, endDateTime, currentBatch);
        	}
		}
		return result;
	}
	
	private int finalizeTrueRemoves(Date endDateTime, ArrayList<String> codesOfTrueRemoves) {
		int result = 0;
		if (codesOfTrueRemoves.size() > 0) {
        	ArrayList<String> currentBatch = new ArrayList<String>();
        	int inThisBatchSoFar = 0;
        	for (String code : codesOfTrueRemoves) {
        		if (++inThisBatchSoFar < sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.MAX_SAFE_CODELIST_SIZE_FOR_SELECTS))) {
        			currentBatch.add(code);
        		} else {
        			currentBatch.add(code);
            		currentBatch.sort(null);
					result += cpscHtsManagementRepository.updateRetireesFromScratchIntoLookup("retiree", endDateTime, currentBatch);
        			inThisBatchSoFar = 0;
        			currentBatch = new ArrayList<String>();
        		}
        	}
        	if (!currentBatch.isEmpty()) {
        		currentBatch.sort(null);
        		result += cpscHtsManagementRepository.updateRetireesFromScratchIntoLookup("retiree", endDateTime, currentBatch);
        	}
		}
		return result;
	}
	
	private Date finalizationTimestamp(boolean applyOffsetIntoPast) {
		DateTime jodaDT = new DateTime(DateTimeZone.UTC); // i.e. now
		if (applyOffsetIntoPast) {
	    	int offsetMinutesIntoPast = sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.OFFSET_MINUTES_INTO_PAST));
	    	jodaDT = jodaDT.minusMinutes(offsetMinutesIntoPast);
		}
		return jodaDT.toDate();
	}

	private String finalizationTimestampAsString(boolean applyOffsetIntoPast) {
		DateTime jodaDT = new DateTime(DateTimeZone.UTC); // i.e. now
		if (applyOffsetIntoPast) {
	    	int offsetMinutesIntoPast = sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.OFFSET_MINUTES_INTO_PAST));
	    	jodaDT = jodaDT.minusMinutes(offsetMinutesIntoPast);
		}
    	DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		return fmt.print(jodaDT);
	}

	public ServiceResponse<String> finalizeOrRevert(boolean writeToLookupTable, String clientUsername) {
		ServiceResponse<String> response = new ServiceResponse<>();
		String scratchUserForSelection = getUsernameForGlobalState();
		transition.requestTransition((writeToLookupTable ? HtsStateTransitionEnum.FINALIZE_START : HtsStateTransitionEnum.REVERT_START), "");
		if (transition.getGlobalStateEnum() == HtsGlobalStateEnum.FINALIZED_NO_WIP) {
			response.setResponseCode(ServiceResponseCodeEnum.FINALIZATION_INVALID_WITHOUT_WIP);
			return response;
		}
		String result = "{\"remark\": ";
		String causeForScratchTablePreservation = null;
		if (writeToLookupTable) { // this is a finalization into the lookup table
			try {
				finalizeIntoLookupTable(scratchUserForSelection, clientUsername);
				result += "\"Finalization into lookup table is complete.\"";
			} catch (CpscHtsMgmtException htsException) {
				switch (htsException.getErrorCode()) {
				case SAW_RESET_FLAG: // passed from checkForReset only
					response.setResponseCode(ServiceResponseCodeEnum.RESET_OCCURRED);
					causeForScratchTablePreservation = htsException.getMessage();
					break;
				case EXCEPTION_WRITING_TO_LOOKUP_TABLE: // raised in body of finalizeIntoLookupTable only
					response.setResponseCode(ServiceResponseCodeEnum.FINALIZATION_FAILED);
					causeForScratchTablePreservation = htsException.getMessage();
					break;
				default:
					causeForScratchTablePreservation = "unknown 2686";
					break;
				}
				transition.recordIncident(HtsStateTransitionEnum.FINALIZE_START, htsException.getErrorCode(), causeForScratchTablePreservation);
				result += "\"Finalization into lookup table has stalled.\"";
			}
		} else { // this is a revert to last finalized
			result += "\"Reversion to previous finalization is complete. Work in progress has been discarded.\"";
		}
		int countFromClean = 0;
		if (causeForScratchTablePreservation == null) { // either finalization or revert
			countFromClean = cleanScratchTable();
			transition.requestTransition((writeToLookupTable ? HtsStateTransitionEnum.FINALIZE_END : HtsStateTransitionEnum.REVERT_END), "" + countFromClean);
		} else { // this was a finalization, not a revert
			transition.requestTransition(HtsStateTransitionEnum.FINALIZE_STALL, causeForScratchTablePreservation);
		}
		result += ", \"recordCount\": \"" + countFromClean + "\"}";
		response.setValue(result);
    	return response;
    }
	
	private int cleanScratchTable() {
		return cleanScratchTable(getUsernameFor(HtsGlobalStateEnum.ITC_UPLOAD_WIP))
				+ cleanScratchTable(getUsernameFor(HtsGlobalStateEnum.CPSC_CURRENT_WIP));
	}
	
	private int cleanScratchTable(String scratchUserForSelection) {
		int countFromClean = 0;
		if (sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.AUDIT_PRESERVE_SCRATCH_RECORDS_UNDER_SPECIFIED_USERNAME))) {
			countFromClean = cpscHtsManagementRepository.makeFinalCleanByChangingUsername(scratchUserForSelection, 
					sysPropertyService.getSysPropertyStringByName(prefixedPropertyKey(HtsSysPropertyEnum.SCRATCH_USER_INDICATING_FINALIZED)));
		} else { 
			// must delete in small batches to avoid "java.sql.SQLException: The transaction 
			// log for database ... is full due to 'ACTIVE_TRANSACTION'."
			for (int i = 0; i < 10; i++) {
				String digitInArbitraryPlace = "" + i;
				countFromClean += cpscHtsManagementRepository.makeFinalCleanByDeletingBatchwise(scratchUserForSelection, digitInArbitraryPlace);
			}
			// erroneous records under username may still exist if this was triggered by a reset after a failed upload:
			countFromClean += cpscHtsManagementRepository.makeFinalCleanByDeleting(scratchUserForSelection);
		}
		return countFromClean;
	}
	
	private void finalizeIntoLookupTable(String userForScratch, String clientUsername) throws CpscHtsMgmtException {
		checkForReset(); // CpscHtsMgmtException iff SAW_RESET_FLAG
		Date startDateTime = finalizationTimestamp(false);
		Date endDateTime = finalizationTimestamp(true);
		List<CpscHtsManagementEntity> allFinalizationCandidates = collectFinalizationCandidates(userForScratch);
		ArrayList<CpscHtsManagementEntity> allAdds = new ArrayList<CpscHtsManagementEntity>();
		ArrayList<CpscHtsManagementEntity> allRemoves = new ArrayList<CpscHtsManagementEntity>();
		ArrayList<String> codesOfAllRemoves = new ArrayList<String>();
		ArrayList<CpscHtsManagementEntity> allNones = new ArrayList<CpscHtsManagementEntity>();
		ArrayList<String> codesOfAllPlaceholders = new ArrayList<String>();
		for (CpscHtsManagementEntity chme : allFinalizationCandidates) {
			if (chme.getChangeStatus().equalsIgnoreCase("add")) {
				allAdds.add(chme);
			} else if (chme.getChangeStatus().equalsIgnoreCase("remove")) {
				allRemoves.add(chme);
				codesOfAllRemoves.add(chme.getHtsCode());
			} else if (chme.getChangeStatus().equalsIgnoreCase("none")) {
				// Includes reviewStatus values of "necessary_ancestor" and "chapter_is_leaf".
				// Normally, only chapter 77 has reviewStatus of "chapter_is_leaf".
				allNones.add(chme);
			} else if (chme.getChangeStatus().equalsIgnoreCase("placeholder")) {
				codesOfAllPlaceholders.add(chme.getHtsCode());
			}
		}
		ArrayList<CpscHtsManagementEntity> partneredAdds = new ArrayList<CpscHtsManagementEntity>();
		ArrayList<String> codesOfPartneredRemoves = new ArrayList<String>();
		ArrayList<String> codesOfTrueAdds = new ArrayList<String>();
		for (CpscHtsManagementEntity chme : allAdds) {
			if (codesOfAllRemoves.contains(chme.getHtsCode())) { // it's half of an add-remove pair
				partneredAdds.add(chme);
				codesOfPartneredRemoves.add(chme.getHtsCode());
			} else { // it's a true add
				codesOfTrueAdds.add(chme.getHtsCode());
			}
		}
		ArrayList<CpscHtsManagementEntity> partneredRemoves = new ArrayList<CpscHtsManagementEntity>();
		ArrayList<CpscHtsManagementEntity> trueRemoves = new ArrayList<CpscHtsManagementEntity>();
		for (CpscHtsManagementEntity chme : allRemoves) {
			if (codesOfPartneredRemoves.contains(chme.getHtsCode())) { // it's half of an add-remove pair
				partneredRemoves.add(chme);
			} else { // it's a true remove
				trueRemoves.add(chme);
			}
		}
		ArrayList<CpscHtsManagementEntity> tweakedIncumbents = new ArrayList<CpscHtsManagementEntity>();
		ArrayList<String> codesOfTweakedIncumbents = new ArrayList<String>();
		ArrayList<CpscHtsManagementEntity> untweakedIncumbentsWithNewNotes = new ArrayList<CpscHtsManagementEntity>();
		ArrayList<String> codesOfUntweakedIncumbentsWithNewNotes = new ArrayList<String>();
		HashMap<String, CpscHtsManagementEntity> finalizedOmniStatus = lookupAllFinalizedOmniStatus(userForScratch);
		for (CpscHtsManagementEntity chme : allNones) {
			boolean wasModifiedViaSave = (chme.getModified() != null ? chme.getModified() : false);
			if (flippedJurisdictionOrTargetedFromLookup(chme, finalizedOmniStatus) || wasModifiedViaSave) {
				tweakedIncumbents.add(chme);
				codesOfTweakedIncumbents.add(chme.getHtsCode());
			} else if (alteredNotesFromLookup(chme, finalizedOmniStatus)) {
				untweakedIncumbentsWithNewNotes.add(chme);
				codesOfUntweakedIncumbentsWithNewNotes.add(chme.getHtsCode());
			} else if (chme.getInheritsChange() != null && chme.getInheritsChange()) {
				tweakedIncumbents.add(chme);
				codesOfTweakedIncumbents.add(chme.getHtsCode());
			} // else don't update in REF_HTS_ALL
		}
		finalizationHandlerForUnpairedChanges(trueRemoves, codesOfTrueAdds, startDateTime, endDateTime, userForScratch);
		try {
			finalizationHandlerForPairs(partneredRemoves, partneredAdds, startDateTime, endDateTime, userForScratch, clientUsername);
			finalizationHandlerForIncumbents(tweakedIncumbents, untweakedIncumbentsWithNewNotes, startDateTime, endDateTime, 
					userForScratch, clientUsername);
		} catch (DataAccessException dae) {
			throw new CpscHtsMgmtException(CpscHtsMgmtCodeEnum.EXCEPTION_WRITING_TO_LOOKUP_TABLE, 2871, "repository write failed", dae);
		}
		if (!codesOfAllPlaceholders.isEmpty()) {
			finalizePlaceholders(userForScratch, startDateTime, endDateTime, codesOfAllPlaceholders);
		}
	}
	
	private List<CpscHtsManagementEntity> collectFinalizationCandidates(String userForScratch) {
		// records from scratch table eligible to be finalized - group 1 of 4: modifieds
		List<CpscHtsManagementEntity> allFinalizationCandidates = 
				cpscHtsManagementRepository.findByUsernameAndModifiedOrderByHtsCodeAsc(userForScratch, true);
		// records from scratch table eligible to be finalized - group 2 of 4: unmodified and changeStatus of add
		List<CpscHtsManagementEntity> unmodifiedAdds = 
				cpscHtsManagementRepository.findByUsernameAndModifiedAndChangeStatusOrderByHtsCodeAsc(userForScratch, false, "add");
		for (CpscHtsManagementEntity chme : unmodifiedAdds) {
			allFinalizationCandidates.add(chme);
		}
		// records from scratch table eligible to be finalized - group 3 of 4: unmodified and changeStatus of remove
		List<CpscHtsManagementEntity> unmodifiedRemoves = 
				cpscHtsManagementRepository.findByUsernameAndModifiedAndChangeStatusOrderByHtsCodeAsc(userForScratch, false, "remove");
		for (CpscHtsManagementEntity chme : unmodifiedRemoves) {
			allFinalizationCandidates.add(chme);
		}
		// records from scratch table eligible to be finalized - group 4 of 4: unmodified and inheritsChange and changeStatus of none
		List<CpscHtsManagementEntity> unmodifiedNonesInheritingChange = 
				cpscHtsManagementRepository.findByUsernameAndModifiedAndInheritsChangeAndChangeStatusOrderByHtsCodeAsc(userForScratch, false, true, "none");
		for (CpscHtsManagementEntity chme : unmodifiedNonesInheritingChange) {
			allFinalizationCandidates.add(chme);
		}
		allFinalizationCandidates.sort(null);
		return allFinalizationCandidates;
	}
	
	private void finalizationHandlerForUnpairedChanges(ArrayList<CpscHtsManagementEntity> trueRemoves, ArrayList<String> codesOfTrueAdds, 
			Date startDateTime, Date endDateTime, String userForScratch) {
		if (!codesOfTrueAdds.isEmpty()) {
			finalizeTrueAdds(userForScratch, startDateTime, null, codesOfTrueAdds);
		}
		if (!trueRemoves.isEmpty()) {
			ArrayList<String> codesOfTrueRemovesToBeSunsetted = new ArrayList<String>();
			ArrayList<String> codesOfTrueRemovesNotToBeSunsetted = new ArrayList<String>();
			for (CpscHtsManagementEntity chme : trueRemoves) {
				if (chme.getTargeted()) {
					codesOfTrueRemovesToBeSunsetted.add(chme.getHtsCode());
				} else {
					codesOfTrueRemovesNotToBeSunsetted.add(chme.getHtsCode());
				}
			}
			if (!codesOfTrueRemovesToBeSunsetted.isEmpty()) {
				finalizeTrueRemoves(add3MonthsToDate(endDateTime), codesOfTrueRemovesToBeSunsetted);
			}
			if (!codesOfTrueRemovesNotToBeSunsetted.isEmpty()) {
				finalizeTrueRemoves(endDateTime, codesOfTrueRemovesNotToBeSunsetted);
			}			
		}
	}

	private void finalizationHandlerForPairs(ArrayList<CpscHtsManagementEntity> partneredRemoves, 
			ArrayList<CpscHtsManagementEntity> partneredAdds, Date startDateTime, Date endDateTime, 
			String userForScratch, String clientUsername) {
		Iterator<CpscHtsManagementEntity> iterOverPartneredRemoves = partneredRemoves.iterator();
		Iterator<CpscHtsManagementEntity> iterOverPartneredAdds = partneredAdds.iterator();
		int tmpDebugCounter = 0;
		while (iterOverPartneredRemoves.hasNext() && iterOverPartneredAdds.hasNext()) {
			checkForReset(); // CpscHtsMgmtException iff SAW_RESET_FLAG
			tmpDebugCounter++;
			CpscHtsManagementEntity nextRemove = iterOverPartneredRemoves.next();
			CpscHtsManagementEntity nextAdd = iterOverPartneredAdds.next();
			cpscHtsManagementRepository.updateOnePairedRemovalFromScratchIntoLookup(
					"pairedRemoval_" + tmpDebugCounter, endDateTime, nextRemove.getHtsCode(),
					startDateTime, clientUsername);
		    // must exclude records in scratch table where reviewStatus == "paired_remove", because those are already in REF_HTS_ALL:
			cpscHtsManagementRepository.insertOnePairedAddFromScratchIntoLookup(
					"pairedAdd_" + tmpDebugCounter, userForScratch, startDateTime, null, nextAdd.getHtsCode(), "paired_remove",
					startDateTime, clientUsername);
		}
	}
	
	private void finalizationHandlerForIncumbents(ArrayList<CpscHtsManagementEntity> tweakedIncumbents,
			ArrayList<CpscHtsManagementEntity> untweakedIncumbentsWithNewNotes, Date startDateTime, 
			Date endDateTime, String userForScratch, String clientUsername) {
		int tmpDebugCounter = 0;
		for (CpscHtsManagementEntity tweakedIncumbent : tweakedIncumbents) {
			tmpDebugCounter++;
			cpscHtsManagementRepository.updateTheClosingHalfOfOneTweakedIncumbentFromScratchIntoLookup(
					"tweakedCloser_" + tmpDebugCounter, endDateTime, tweakedIncumbent.getHtsCode(),
					startDateTime, clientUsername);
			cpscHtsManagementRepository.insertTheOpeningHalfOfOneTweakedIncumbentFromScratchIntoLookup(
					"tweakedOpener_" + tmpDebugCounter, userForScratch, startDateTime, null, tweakedIncumbent.getHtsCode(),
					startDateTime, clientUsername);
		}
		for (CpscHtsManagementEntity chme : untweakedIncumbentsWithNewNotes) {
			cpscHtsManagementRepository.updateUntweakedIncumbentWithNewNotesFromScratchIntoLookup(
					"notesOnly", chme.getNotes(), chme.getHtsCode(),
					startDateTime, clientUsername);
		}
	}
		
// END OF THE FINALIZE FEATURE ----------------------------------------------------	
		
// START OF THE SAVE FEATURE ------------------------------------------------------	
	
	public String debugSaveAsIs(String ignoredForNow) {
		int countJ = 0;
		int countT = 0;
		String username = getUsernameForGlobalState();
		List<CpscHtsManagementEntity> all10DigitJs = cpscHtsManagementRepository.findByUsernameAndJurisdictionAndCodeTypeOrderByHtsCodeAsc(username, true, 10);
		for (CpscHtsManagementEntity chme : all10DigitJs) {
			saveFlex(username, chme, false, true);
			countJ++;
		}
		List<CpscHtsManagementEntity> all10DigitTs = cpscHtsManagementRepository.findByUsernameAndTargetedAndCodeTypeOrderByHtsCodeAsc(username, true, 10);
		for (CpscHtsManagementEntity chme : all10DigitTs) {
			saveFlex(username, chme, false, true);
			countT++;
		}
		return "verify J:" + countJ + " T: " + countT;
	}
	
	public ServiceResponse<String> saveOne(CpscHtsManagementEntity rhme) {
		ServiceResponse<String> response = new ServiceResponse<>();
		String usernameForPreviousGlobalState = getUsernameForGlobalState();
		transition.requestTransition(HtsStateTransitionEnum.SAVE_START, rhme.getHtsCode());
		int count = saveFlex(usernameForPreviousGlobalState, rhme, true, true);
		transition.requestTransition(HtsStateTransitionEnum.SAVE_END, rhme.getHtsCode() + "_" + count);
		response.setValue("{\"count\": " + count + "}");
    	return response;
	}

	private int saveFlex(String usernameForPreviousGlobalState, CpscHtsManagementEntity rhme, boolean allowCascade, boolean allowPercolation) {
		// only the following 3 fields are used in select:
		String username = usernameForPreviousGlobalState; // getUsernameForGlobalState();
		String code = rhme.getHtsCode(); // precondition: non-null and non-empty
		String changeStatus = (rhme.getChangeStatus() != null ? rhme.getChangeStatus() : "");
		boolean jurisdiction = (rhme.getJurisdiction() != null ? rhme.getJurisdiction() : false);
		boolean targeted = (rhme.getTargeted() != null ? rhme.getTargeted() : false);
		if (sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.TARGETED_DRIVES_JURISDICTION))) {
			jurisdiction = jurisdiction || targeted;
		} else {
			targeted = jurisdiction && targeted;
		}
		String reviewStatus = (rhme.getReviewStatus() != null ? rhme.getReviewStatus() : "");
		String notes = (rhme.getNotes() != null ? rhme.getNotes() : "");
		CpscHtsManagementEntity incumbent = null;
		incumbent = cpscHtsManagementRepository.findOneByUsernameAndHtsCodeAndChangeStatus(username, code, "none");
		changeStatus = "none";
		if (incumbent == null) {
			incumbent = cpscHtsManagementRepository.findOneByUsernameAndHtsCodeAndChangeStatus(username, code, "add");
			changeStatus = "add";
		}
		String shortDescFromClient = (rhme.getDescription() != null ? rhme.getDescription() : ""); // to avoid changing client JS
		String previousCpscShortDescription = (incumbent.getCpscShortDescription() != null ? incumbent.getCpscShortDescription() : "");
		String cpscShortDescToSave = "";
		// ITC_SHORT_DESCRIPTION:
		String previousItcShortDescription = (incumbent.getDescription() != null ? incumbent.getDescription() : "");
		if (!shortDescFromClient.isEmpty()) {
			if (previousCpscShortDescription.isEmpty()) {
				if (!shortDescFromClient.equals(previousItcShortDescription)) {
					cpscShortDescToSave = shortDescFromClient;
				} // else do nothing
			} else {
				if (shortDescFromClient.equals(previousCpscShortDescription)) {
					cpscShortDescToSave = shortDescFromClient; // == previousCpscShortDescription
				} else {
					if (shortDescFromClient.equals(previousItcShortDescription)) {
						// must clear out the cpsc short when saving, so do nothing
					} else {
						cpscShortDescToSave = shortDescFromClient;
					}
				}
			}
		} // else do nothing
		String cdescriptionToSave = "";
		String previousCdescription = (incumbent.getCdescription() != null ? incumbent.getCdescription() : "");
		if (cpscShortDescToSave.equals(previousCpscShortDescription)) {
			cdescriptionToSave = previousCdescription;
		} else { // necessitates a recalculation of the concatenated description
			String previousShortDescUsed = (previousCpscShortDescription.isEmpty() ? previousItcShortDescription : previousCpscShortDescription);
			cdescriptionToSave = refreshLongDescription(previousCdescription, previousShortDescUsed, cpscShortDescToSave);
		}
		int result = cpscHtsManagementRepository.uniqueUpdate(cpscShortDescToSave, cdescriptionToSave, jurisdiction, targeted, reviewStatus, notes, 
				username, code, changeStatus, true);
		boolean previousJurisdiction = (incumbent.getJurisdiction() != null ? incumbent.getJurisdiction() : false);
		boolean previousTargeted = (incumbent.getTargeted() != null ? incumbent.getTargeted() : false);
		boolean forcePercolation = sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.FORCE_PERCOLATION));
		if (jurisdiction != previousJurisdiction || targeted != previousTargeted || forcePercolation) {
			if (allowPercolation && code.length() > 2) {
				maybeSaveParentWithPercolatingUpdate(username, code);
			}
		}
		if (jurisdiction != previousJurisdiction || targeted != previousTargeted || !cpscShortDescToSave.equals(previousCpscShortDescription)) {
			if (allowCascade && code.length() < 10) {
				saveChildrenWithCascadingUpdates(username, code, !cpscShortDescToSave.equals(previousCpscShortDescription), cdescriptionToSave,
						jurisdiction != previousJurisdiction, jurisdiction, targeted != previousTargeted, targeted);
			}
		}
		return result;
	}
	
	private String trimTrailingCruft(String s) {
		String delimiter = sysPropertyService.getSysPropertyStringByName(prefixedPropertyKey(HtsSysPropertyEnum.DESC_DELIMITER));
		String oneSpace = " ";
		while (s.endsWith(delimiter) || s.endsWith(oneSpace)) {
			if (s.endsWith(delimiter)) {
				s = s.substring(0, s.length() - delimiter.length());
			} else { // s.endsWith(oneSpace)
				s = s.substring(0, s.length() - oneSpace.length());
			}
		}
		return s;
	}

	private String refreshLongDescription(String previousCdescriptionWithTrailingCruft, String previousShortDescUsedWithTrailingCruft, 
			String shortDescToUseWithTrailingCruft) {
		String previousCdescription = trimTrailingCruft(previousCdescriptionWithTrailingCruft);
		String previousShortDescUsed = trimTrailingCruft(previousShortDescUsedWithTrailingCruft);
		String shortDescToUse = trimTrailingCruft(shortDescToUseWithTrailingCruft);
		int numOfCharsToTruncate = Math.min(previousShortDescUsed.length(), previousCdescription.length());
		auditLog.error("previousCdescription=" + previousCdescription + ", previousShortDescUsed=" + previousShortDescUsed
				+ ", shortDescToUse=" + shortDescToUse + ", numOfCharsToTruncate=" + numOfCharsToTruncate);
		return previousCdescription.substring(0, previousCdescription.length() - numOfCharsToTruncate) + shortDescToUse;
	}
	
	private String obtainDescriptionPart(String username, String code) {
		CpscHtsManagementEntity ancestorOrNull = cpscHtsManagementRepository.findOneByUsernameAndHtsCodeAndChangeStatus(username, code, "none");
		if (ancestorOrNull == null) {
			ancestorOrNull = cpscHtsManagementRepository.findOneByUsernameAndHtsCodeAndChangeStatus(username, code, "add");
		}
		String result = "";
		if (ancestorOrNull != null) {
			result = ancestorOrNull.getCpscShortDescription();
			if (result == null || result.equalsIgnoreCase("")) {
				result = ancestorOrNull.getDescription();
				if (result == null) {
					result = "";
				}
			}
		}
		return result;
	}
	
	private void saveChildrenWithCascadingUpdates(String username, String code, boolean updateLongDesc, String newCdescriptionOfParent,
			boolean updateJurisdiction, boolean newJurisdiction, boolean updateTargeted, boolean newTargeted) {
		if (code.length() < 10) {
			List<CpscHtsManagementEntity> kids = cpscHtsManagementRepository.obtainDescendantsOfSpecificLevelByCodePrefix(username, code.length() + 2, code);
			kids.sort(null);
			for (CpscHtsManagementEntity chme : kids) {
				if (!chme.getChangeStatus().equalsIgnoreCase("remove")) {
					boolean mustPersist = false;
					boolean mustRecurse = false;
					String newCdescriptionForRecursion = newCdescriptionOfParent;
					if (updateLongDesc) {
						if (chme.getDescription() != null && !chme.getDescription().isEmpty()) {
							// existence of an ITC description indicates this child is not a 
							// placeholder (necessary ancestor), so process its descriptions:
							String appropriateShortDescription = ((chme.getCpscShortDescription() == null || chme.getCpscShortDescription().isEmpty()) 
									? (chme.getDescription() != null ? chme.getDescription() : "")
									: chme.getCpscShortDescription());
							String newCdescription = newCdescriptionOfParent;
							if (newCdescriptionOfParent != null && !newCdescriptionOfParent.isEmpty() && !appropriateShortDescription.isEmpty()) {
								newCdescription += sysPropertyService.getSysPropertyStringByName(prefixedPropertyKey(HtsSysPropertyEnum.DESC_DELIMITER)) + appropriateShortDescription;
							} else {
								newCdescription = appropriateShortDescription;
							}
							if (!newCdescription.equals(chme.getCdescription())) {
								chme.setCdescription(newCdescription);
								mustPersist = true;
								mustRecurse = true;
							}
							newCdescriptionForRecursion = newCdescription;
						} else {
							// it lacks an ITC description, so this child is a placeholder (necessary ancestor), so skip description 
							// processing. Must recurse (whether or not persisting), because descendants may need description changes:
							mustRecurse = true;
						}
					}
					if (updateJurisdiction && (newJurisdiction != chme.getJurisdiction())) {
						chme.setJurisdiction(newJurisdiction);
						mustPersist = true;
						mustRecurse = true;
					}
					if (updateTargeted && (newTargeted != chme.getTargeted())) {
						chme.setTargeted(newTargeted);
						mustPersist = true;
						mustRecurse = true;
					}
					if (mustPersist) {
						cpscHtsManagementRepository.uniqueUpdate(chme.getCpscShortDescription(), chme.getCdescription(), chme.getJurisdiction(), chme.getTargeted(), 
								chme.getReviewStatus(), chme.getNotes(), chme.getUsername(), chme.getHtsCode(), chme.getChangeStatus(), true);
					}
					if (mustRecurse && chme.getCodeType() < 10) {
						saveChildrenWithCascadingUpdates(chme.getUsername(), chme.getHtsCode(), updateLongDesc, newCdescriptionForRecursion,
								updateJurisdiction, chme.getJurisdiction(), updateTargeted, chme.getTargeted());
					}
				}
			}
		}
	}
	
	private void maybeSaveParentWithPercolatingUpdate(String username, String code) {
		if (code.length() > 2) {
			CpscHtsManagementEntity parent = cpscHtsManagementRepository.findOneByUsernameAndHtsCodeAndChangeStatus(username, 
					code.substring(0, code.length() - 2), "none");
			if (parent == null) {
				parent = cpscHtsManagementRepository.findOneByUsernameAndHtsCodeAndChangeStatus(username, 
						code.substring(0, code.length() - 2), "add");
			}
			if (parent == null) {
				parent = cpscHtsManagementRepository.findOneByUsernameAndHtsCodeAndChangeStatus(username, 
						code.substring(0, code.length() - 2), "placeholder");
			}
			if (parent != null) {
				List<CpscHtsManagementEntity> childrenOfParent = cpscHtsManagementRepository.obtainDescendantsOfSpecificLevelByCodePrefix(username, 
						code.length(), code.substring(0, code.length() - 2));
				boolean atLeastOneChildWithNonTrueJurisdictionSeen = false;
				boolean atLeastOneChildWithNonTrueTargetedSeen = false;
				for (CpscHtsManagementEntity chme : childrenOfParent) {
					if (chme.getJurisdiction() == null || !chme.getJurisdiction()) {
						atLeastOneChildWithNonTrueJurisdictionSeen = true;
					}
					if (chme.getTargeted() == null || !chme.getTargeted()) {
						atLeastOneChildWithNonTrueTargetedSeen = true;
					}
				}
				boolean mustPersist = false;
				if (atLeastOneChildWithNonTrueJurisdictionSeen) {
					if (parent.getJurisdiction() != null && parent.getJurisdiction()) {
						parent.setJurisdiction(false);
						mustPersist = true;
					}
				} else {
					if (parent.getJurisdiction() == null || !parent.getJurisdiction()) {
						parent.setJurisdiction(true);
						mustPersist = true;
					}
				}
				if (atLeastOneChildWithNonTrueTargetedSeen) {
					if (parent.getTargeted() != null && parent.getTargeted()) {
						parent.setTargeted(false);
						mustPersist = true;
					}
				} else {
					if (parent.getTargeted() == null || !parent.getTargeted()) {
						parent.setTargeted(true);
						mustPersist = true;
					}
				}
				if (mustPersist) {
					cpscHtsManagementRepository.uniqueUpdate(parent.getCpscShortDescription(), parent.getCdescription(), 
							parent.getJurisdiction(), parent.getTargeted(), parent.getReviewStatus(), parent.getNotes(), parent.getUsername(), parent.getHtsCode(), parent.getChangeStatus(), true);
					if (parent.getHtsCode().length() > 2) {
						maybeSaveParentWithPercolatingUpdate(parent.getUsername(), parent.getHtsCode());
					}
				}
			} // else do nothing, although currently this can't arise
		}
	}

	// END OF THE SAVE FEATURE ------------------------------------------------------	

	// START OF THE COUNTS FEATURE --------------------------------------------------
	
	private long countLookupsByQuery(Integer sequenceId, Date today, int minLengthOfCode, Boolean... triValuedFlags) {
		long result = -1L;
		if (triValuedFlags == null || triValuedFlags.length == 0) {
			result = (sequenceId == null ? htsMgmtLookupRepository.obtainNum(today, minLengthOfCode) 
					: htsMgmtLookupRepository.obtainNumViaSeq(today, sequenceId, minLengthOfCode));
		} else if (triValuedFlags.length == 1) {
			// when sunset flag is present, the sunset records are selected regardless of sunset boolean value
			result = (sequenceId == null ? htsMgmtLookupRepository.obtainNumOfSunsets(today, minLengthOfCode) 
					: htsMgmtLookupRepository.obtainNumOfSunsetsViaSeq(today, sequenceId, minLengthOfCode));
		} else if (triValuedFlags.length == 2) {
			// sunset is ignored when additional flags are present
			Boolean jurisdiction = triValuedFlags[1];
			result = (sequenceId == null ? htsMgmtLookupRepository.obtainNumUsingJurisdiction(today, jurisdiction, minLengthOfCode) 
					: htsMgmtLookupRepository.obtainNumUsingJurisdictionViaSeq(today, jurisdiction, sequenceId, minLengthOfCode));
		} else { 
			// triValuedFlags.length >= 3, but we'll ignore flags (if any) after the third
			Boolean jurisdiction = triValuedFlags[1];
			Boolean targeted = triValuedFlags[2];
			result = (sequenceId == null ? htsMgmtLookupRepository.obtainNumUsingJurisdictionAndTargeted(today, jurisdiction, targeted, minLengthOfCode) 
					: htsMgmtLookupRepository.obtainNumUsingJurisdictionAndTargetedViaSeq(today, jurisdiction, targeted, sequenceId, minLengthOfCode));
		}
		return result;
	}
	
	public ServiceResponse<String> obtainCounts(boolean debug, boolean commandeerTree, String codeOfRoot, 
			String minLengthOfCodeAsString, String maxDepthAsString) {
        ServiceResponse<String> response = new ServiceResponse<>();
		if (!debug) {
    		response.setValue(obtainCountsSummary());
    	} else {
			response.setValue(obtainCountsDetailed(generateRoot(), commandeerTree, codeOfRoot, minLengthOfCodeAsString, maxDepthAsString));
    	}
        return response;
	}
	
	private String obtainCountsSummary() {
		Date today = finalizationTimestamp(false);
		Integer sequenceId = (sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.USE_SLICE)) ? sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.SLICE_ID)) : null);
		String result = "{ \"refActiveTotal\": \"" + countLookupsByQuery(sequenceId, today, 10)
				+ "\", \"refActiveTotalJurisdictionTrue\": \"" + countLookupsByQuery(sequenceId, today, 10, false, true)
				+ "\", \"refActiveTotalTargetedTrue\": \"" + countLookupsByQuery(sequenceId, today, 10, false, true, true) + "\"";
		result += ", \"refSunsetTotal\": \"" + countLookupsByQuery(sequenceId, today, 10, true) + "\"";
		if (transition.getGlobalStateEnum() == HtsGlobalStateEnum.CPSC_CURRENT_WIP
				|| transition.getGlobalStateEnum() == HtsGlobalStateEnum.ITC_UPLOAD_WIP) {
			String username = getUsernameForGlobalState();
			long tensThatAreAdds = cpscHtsManagementRepository.countByUsernameAndChangeStatusAndCodeType(username, "add", 10);
			long tensThatAreNones = cpscHtsManagementRepository.countByUsernameAndChangeStatusAndCodeType(username, "none", 10);
			long distinctNonRetireeTensInScratch = tensThatAreAdds + tensThatAreNones;
			long jurisTensThatAreAdds = cpscHtsManagementRepository.countByUsernameAndChangeStatusAndCodeTypeAndJurisdiction(username, "add", 10, true);
			long jurisTensThatAreNones = cpscHtsManagementRepository.countByUsernameAndChangeStatusAndCodeTypeAndJurisdiction(username, "none", 10, true);
			long distinctNonRetireeJurisTensInScratch = jurisTensThatAreAdds + jurisTensThatAreNones;
			long targetedTensThatAreAdds = cpscHtsManagementRepository.countByUsernameAndChangeStatusAndCodeTypeAndTargeted(username, "add", 10, true);
			long targetedTensThatAreNones = cpscHtsManagementRepository.countByUsernameAndChangeStatusAndCodeTypeAndTargeted(username, "none", 10, true);
			long distinctNonRetireeTargetedTensInScratch = targetedTensThatAreAdds + targetedTensThatAreNones;
			result += ", \"distinctNonRetireeTensInScratch\": \"" + distinctNonRetireeTensInScratch + "\""
					+ ", \"distinctNonRetireeJurisTensInScratch\": \"" + distinctNonRetireeJurisTensInScratch + "\""
					+ ", \"distinctNonRetireeTargetedTensInScratch\": \"" + distinctNonRetireeTargetedTensInScratch + "\"";
		}
		result += " }";
    	return result;
	}
	
	private String obtainCountsDetailed(CpscHtsManagementEntity root, boolean commandeerTree, String codeOfRoot, String minLengthOfCodeAsString, String maxDepthAsString) {
		Date today = finalizationTimestamp(false);
		Integer sequenceId = (sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.USE_SLICE)) ? sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.SLICE_ID)) : null);
		String result = "Scratch Table Counts: \n";
		result += countScratch();
    	result += "\nLookup Table Counts:";
    	result += countLookupBooleans(today, sequenceId, minLengthOfCodeAsString);
    	result += countLookupCodeLengths(today, sequenceId);
    	result += "\n";
    	if (commandeerTree) {
	    	result += countScratchUsingTree(root, codeOfRoot, maxDepthAsString);
    	}
    	return result;
	}
	
	private String countScratch() {
		String result = "";
    	List<String> allUsernames = cpscHtsManagementRepository.obtainDistinctUsernames();
    	if (allUsernames == null || allUsernames.isEmpty()) {
    		result = "   Scratch Table is empty or has no records with usernames.\n";
    	} else {
	    	allUsernames.sort(null);
	    	long count = 0;
	    	for (String username : allUsernames) {
	    		count = cpscHtsManagementRepository.countByUsername(username);
	    		result += "   " + username + ": " + count;
	    		if (username.equalsIgnoreCase(getUsernameFor(HtsGlobalStateEnum.CPSC_CURRENT_WIP))) {
	    			result += " [this USERNAME corresponds to state CPSC_CURRENT_WIP]";
	    		} else if (username.equalsIgnoreCase(getUsernameFor(HtsGlobalStateEnum.ITC_UPLOAD_WIP))) {
	    			result += " [this USERNAME corresponds to state ITC_UPLOAD_WIP]";
	    		} 
	    		StringBuilder detailsUnderUsername = new StringBuilder();
	    		String[] changeStatuses = {"add","remove","none", "placeholder"};
	    		for (String changeStatus : changeStatuses) {
	    			detailsUnderUsername.append("\n      " + String.format("%1$11s", changeStatus) + ": ");
	    			detailsUnderUsername.append(String.format("%1$8s", cpscHtsManagementRepository.countByUsernameAndChangeStatus(username, changeStatus)));
	    			for (int codeType = 2; codeType <= 10; codeType += 2) {
	    				detailsUnderUsername.append("\n         " + String.format("%1$2s", codeType) + ": ");
	    				detailsUnderUsername.append(String.format("%1$8s", cpscHtsManagementRepository.countByUsernameAndChangeStatusAndCodeType(username, changeStatus, codeType)));
	    			}
	    		}
	    		Boolean[] triValues = {true, false, null};
	    		for (Boolean triValueJ : triValues) {
	    			detailsUnderUsername.append("\n   jurisdiction " + String.format("%1$5s", triValueJ) + ": ");
	    			detailsUnderUsername.append(String.format("%1$8s", cpscHtsManagementRepository.countByJurisdiction(triValueJ)));
	    			for (Boolean triValueT : triValues) {
		    			detailsUnderUsername.append("\n       targeted " + String.format("%1$5s", triValueT) + ": ");
		    			detailsUnderUsername.append(String.format("%1$8s", cpscHtsManagementRepository.countByJurisdictionAndTargeted(triValueJ, triValueT)));
		    			for (int codeType = 2; codeType <= 10; codeType += 2) {
		    				detailsUnderUsername.append("\n         " + String.format("%1$2s", codeType) + ":           ");
		    				detailsUnderUsername.append(String.format("%1$8s", cpscHtsManagementRepository.countByJurisdictionAndTargetedAndCodeType(triValueJ, triValueT, codeType)));
		    			}
	    			}
	    		}
		    	result += detailsUnderUsername.toString() + "\n";
	    	}
    	}
		return result;
	}
	
	private String countScratchUsingTree(CpscHtsManagementEntity root, String codeOfRoot, String maxDepthAsString) {
		resetCounters();
		String nameToUse = getUsernameForGlobalState();
		List<CpscHtsManagementEntity> allInScratch = cpscHtsManagementRepository.findByUsernameOrderByHtsCodeAsc(nameToUse);
    	for (CpscHtsManagementEntity ent : allInScratch) {
    		ent.setChildren(new ArrayList<CpscHtsManagementEntity>());
    	}
    	allInScratch.sort(null);
		List<CpscHtsManagementEntity> sufficientEntitiesForTree = new ArrayList<CpscHtsManagementEntity>(50000);
		sufficientEntitiesForTree.addAll(allInScratch);
		HashSet<String> selectionCodes = new HashSet<String>(20000);
		for (CpscHtsManagementEntity ur : allInScratch) {
			selectionCodes.add(ur.getHtsCode());
		}
		HashSet<String> allCodesForPlaceholders = calculateCodesForPlaceholders(selectionCodes);
		if (allCodesForPlaceholders.size() > 0) {
			List<HtsMgmtLookupEntity> missingAncestorsFoundInRefTable = obtainRefsUsingCodeList(allCodesForPlaceholders, 
					sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.SLICE_ID)));
			List<CpscHtsManagementEntity> missingAncestorsFoundInRefTableAsMgmtEntities = toListOfCpscHtsManagementEntities(
					missingAncestorsFoundInRefTable, nameToUse, "none", "placeholder_from_ref");
			sufficientEntitiesForTree.addAll(missingAncestorsFoundInRefTableAsMgmtEntities);
		} // list is now self-sufficient
		populateWholeHtsTreeFromSelfSufficient(root, 0, 0, sufficientEntitiesForTree);
        int maxDepth = 6;
		if (maxDepthAsString != null) {
			try {
				maxDepth = Integer.parseInt(maxDepthAsString);
			} catch (NumberFormatException nfe) {
			}
		}
		String fromTraversal = recurseSubtree(toNodeInSelfSufficient(root, codeOfRoot), false, false, 0, false, maxDepth, false, false, false);
		String result = "\n\nGraph Of Tree:" + countScratchBooleansUsingTree();
		result += "\n\n" + fromTraversal + "\n\n";
		resetCounters();
		return result;
	}
	
	private String countScratchBooleansUsingTree() {
		String graphOfTree = "\n\n   triTree:\n                           all   none  add   rem   new   term  other";
		for (int a = 0; a < sizeOfDimAForTriTreeCounts; a++) {
			graphOfTree += "\n   code length " + ((a * 2) + 2) + ":";
			for (int b = 0; b < sizeOfDimBForTriTreeCounts; b++) {
				switch (b) {
				case 0: graphOfTree +=  "\n      all               : ";
            		break;
				case 1: graphOfTree +=  "\n      jurisdiction true : ";
            		break;
				case 2: graphOfTree +=  "\n      targeted true     : ";
            		break;
				}
				for (int c = 0; c < sizeOfDimCForTriTreeCounts; c++) {
					graphOfTree += String.format("%1$6s", triTreeCounts[a][b][c]);
				}
			}
		}
		graphOfTree = "\n\n   booleanViolationsUnjustifiedParent: " + booleanViolationsUnjustifiedParent;
		graphOfTree += "\n   booleanViolationsSilentParentJ:     " + booleanViolationsSilentParentJ;
		graphOfTree += "\n   booleanViolationsSilentParentT:     " + booleanViolationsSilentParentT + "\n";	    			
		for (int a = 0; a < sizeOfDimA; a++) {
			graphOfTree += "\n   code length " + ((a * 2) + 2) + ":";
			for (int b = 0; b < sizeOfDimB; b++) {
				switch (b) {
				case 0: graphOfTree +=  "\n      leaf        : ";
            		break;
				case 1: graphOfTree +=  "\n      parent of 1 : ";
            		break;
				case 2: graphOfTree +=  "\n      parent of 2 : ";
            		break;
				default: graphOfTree += "\n      parent of 3+: ";
                	break;
				}
				for (int c = 0; c < sizeOfDimC; c++) {
					graphOfTree += String.format("%1$6s", treeCounts[a][b][c]);
				}
			}
		}
		return graphOfTree;
	}

	private String countLookupBooleans(Date today, Integer sequenceId, String minLengthOfCodeAsString) {
		String result = "";
    	int minLengthOfCode = 2;
		if (minLengthOfCodeAsString != null) {
			try {
				minLengthOfCode = Integer.parseInt(minLengthOfCodeAsString);
			} catch (NumberFormatException nfe) {
			}
		}
		long jurisTrue = countLookupsByQuery(sequenceId, today, minLengthOfCode, false, true);
		long jurisFalse = countLookupsByQuery(sequenceId, today, minLengthOfCode, false, false);
    	long jurisNullAndTargTrue = countLookupsByQuery(sequenceId, today, minLengthOfCode, false, null, true);
    	long jurisNullAndTargFalse = countLookupsByQuery(sequenceId, today, minLengthOfCode, false, null, false);
    	long jurisNullAndTargNull = countLookupsByQuery(sequenceId, today, minLengthOfCode, false, null, null);
    	long jurisNull = jurisNullAndTargTrue + jurisNullAndTargFalse + jurisNullAndTargNull;
		result += "\n   (slice ID: " + sequenceId + ")";
    	result += "\n   total (via sum):       " + (jurisTrue + jurisFalse + jurisNull);
    	result += "\n   total (direct):        " + countLookupsByQuery(sequenceId, today, minLengthOfCode);
    	result += "\n      jurisdiction true:  " + jurisTrue;
    	result += "\n         targeted true:   " + countLookupsByQuery(sequenceId, today, minLengthOfCode, false, true, true);
    	result += "\n         targeted false:  " + countLookupsByQuery(sequenceId, today, minLengthOfCode, false, true, false);
    	result += "\n         targeted null:   " + countLookupsByQuery(sequenceId, today, minLengthOfCode, false, true, null);
    	result += "\n      jurisdiction false: " + jurisFalse;
    	result += "\n         targeted true:   " + countLookupsByQuery(sequenceId, today, minLengthOfCode, false, false, true);
    	result += "\n         targeted false:  " + countLookupsByQuery(sequenceId, today, minLengthOfCode, false, false, false);
    	result += "\n         targeted null:   " + countLookupsByQuery(sequenceId, today, minLengthOfCode, false, false, null);
    	result += "\n      jurisdiction null:  " + jurisNull;
    	result += "\n         targeted true:   " + jurisNullAndTargTrue;
    	result += "\n         targeted false:  " + jurisNullAndTargFalse;
    	result += "\n         targeted null:   " + jurisNullAndTargNull;
    	return result;
	}
	
	private String countLookupCodeLengths(Date today, Integer sequenceId) {
    	// switch to following for speed in this debug method specifically:      
        // @Query("select distinct htsCode from HtsMgmtLookupEntity")
    	// public List<String> obtainDistinctHtsCodes();
    	List<String> inclDupes = new ArrayList<String>();
    	if (sequenceId != null) {
        	inclDupes = htsMgmtLookupRepository.obtainHtsCodesIncludingDuplicatesViaSeq(today, sequenceId);
    	} else {
        	inclDupes = htsMgmtLookupRepository.obtainHtsCodesIncludingDuplicates(today);
    	}
    	ArrayList<String> theTens = new ArrayList<String>(20000);
    	ArrayList<String> theEights = new ArrayList<String>(20000);
    	ArrayList<String> theSixes = new ArrayList<String>(6000);
    	ArrayList<String> theFours = new ArrayList<String>(2000);
    	ArrayList<String> theTwos = new ArrayList<String>(100);
    	HashSet<String> theDistinctTens = new HashSet<String>(20000);
    	HashSet<String> theDistinctEights = new HashSet<String>(20000);
    	HashSet<String> theDistinctSixes = new HashSet<String>(6000);
    	HashSet<String> theDistinctFours = new HashSet<String>(2000);
    	HashSet<String> theDistinctTwos = new HashSet<String>(100);
    	int countOfCodesOfInvalidLengths = 0;
    	for (String code : inclDupes) {
    		if (code.length() == 10) {
    			theTens.add(code);
    			theDistinctTens.add(code);
    		} else if (code.length() == 8) {
    			theEights.add(code);
    			theDistinctEights.add(code);
    		} else if (code.length() == 6) {
    			theSixes.add(code);
    			theDistinctSixes.add(code);
    		} else if (code.length() == 4) {
    			theFours.add(code);
    			theDistinctFours.add(code);
    		} else if (code.length() == 2) {
	    		theTwos.add(code);
	    		theDistinctTwos.add(code);
    		} else { // error
    			countOfCodesOfInvalidLengths++;
    		}
    	}
    	// to find duplicates in REF_HTS_ALL (example shows search for duplicate codes of length 8):
    	// select hts, count(*) FROM [YOUR_DB_NAME_HERE].[YOUR_SCHEMA].[LOOKUP_TABLE_NAME] where len(hts) = 8 group by hts having count(*) > 1;
    	String result = "\n   code length  2: " + theTwos.size() + " (" + theDistinctTwos.size() + " distinct)";
    	result += "\n   code length  4: " + theFours.size() + " (" + theDistinctFours.size() + " distinct)";
    	result += "\n   code length  6: " + theSixes.size() + " (" + theDistinctSixes.size() + " distinct)";
    	result += "\n   code length  8: " + theEights.size() + " (" + theDistinctEights.size() + " distinct)";
    	result += "\n   code length 10: " + theTens.size() + " (" + theDistinctTens.size() + " distinct)";
    	result += "\n   countOfCodesOfInvalidLengths: " + countOfCodesOfInvalidLengths;
		return result;
	}
	
	// END OF THE COUNTS FEATURE ----------------------------------------------------
		
	private String buildIncompleteDescriptionToTest(String[] descriptionParts) {
		String result = "";
		if (sysPropertyService.getSysPropertyBooleanByName(prefixedPropertyKey(HtsSysPropertyEnum.LIMIT_LENGTHS_OF_IMPORTED_DESCRIPTIONS))) {
			int widthOfDescription = 140;
			try {
				widthOfDescription = sysPropertyService.getSysPropertyIntegerByName(prefixedPropertyKey(HtsSysPropertyEnum.IMPORTED_DESCRIPTION_MAX_LENGTH));
			} catch (NumberFormatException nfe) {}
			int widthOfDescriptionPart = widthOfDescription / 7;
			result = snippet(descriptionParts[0], widthOfDescriptionPart);
			for (int i = 1; i < descriptionParts.length; i++) {
				result = result + snippet(descriptionParts[i], widthOfDescriptionPart);
			}
		} else {
			result = descriptionParts[0];
			for (int i = 1; i < descriptionParts.length; i++) {
				result = result + descriptionParts[i];
			}
		}
		return result;
	}
	
	 private String formatCSVFromSelfSufficient(CpscHtsManagementEntity rootOfTreeOrSubtree,
	 			String source, String wasFilter, String wasSearchterm, String username, int recordCount,boolean isTree,boolean isSpecialReport) {
	 		StringBuilder sb = new StringBuilder();
	 		String s = ",";
	 		  sb.append("HTS Code");
	 		  if(!isSpecialReport ) {
	 		  sb.append(s);
	 		  sb.append(" HTS CodeType");
	 		  sb.append(s);
	 		  sb.append("HTS Description");
	 		  sb.append(s);
	 		  sb.append("Jurisdiction");
	 		  sb.append(s);
	 		  sb.append("Target");
	 		  sb.append(s);
	 		  sb.append("Change Status");
	 		  sb.append(s);
	 		  sb.append("Start Date");
	 		  sb.append(s);
	 		  sb.append("End Date");
	 		  sb.append(s);
	 		  sb.append("Notes");
	 		  }
	 		  sb.append("\n");
	 		  
	 		 if(!isTree){
	 			 sb.append(formatCSVFlatOfHtsTreeOrSubtree(rootOfTreeOrSubtree, 0, false,isSpecialReport,wasFilter,source,wasSearchterm));
	 		  }
	 		 else {
	 		  sb.append(formatCSVOfHtsTreeOrSubtree(rootOfTreeOrSubtree, 0, false));
	 		 }
	 		return sb.toString();
	     }
	 private String formatCSVFlatOfHtsTreeOrSubtree(CpscHtsManagementEntity rhme, int guard, boolean hasSiblingsToRight,boolean isSpecialreport,String filter,String source,String searchterm) {
		 StringBuilder sb = new StringBuilder();
			String s = ",";
			if (rhme.getHtsCode().length() == 10) {
				if( ! ( ( filter.equalsIgnoreCase("jurisdiction") && source.equalsIgnoreCase("itc_upload_wip") ) && rhme.getReviewStatus().equalsIgnoreCase("d_true_remove"))) {
					
				if(!( searchterm.equalsIgnoreCase("abj") && rhme.getSunset() ) ) {
				sb.append("\"");
				sb.append(rhme.getHtsCode());
				sb.append("\"");
				if(!isSpecialreport ) {
				sb.append(s);
				String htsCodeType = "\""+rhme.getCodeType()+"\"";
				sb.append(htsCodeType);
				sb.append(s);
				String htsDescription ="\"" +truncateIfDebug(rhme.getDescription() == null ? "" : rhme.getDescription())+ "\"";
				sb.append(htsDescription);
				sb.append(s);
				String jurisdiction = "\""+rhme.getJurisdiction()+"\"";
				sb.append(jurisdiction);
				sb.append(s);
				String targeted = "\""+rhme.getTargeted()+"\"";
				sb.append(targeted);
				sb.append(s);
				String changeStatus = "\""+rhme.getChangeStatus()+"\"";
				String none = "\""+"None"+"\"";
				if (rhme.getChangeStatus().trim().equalsIgnoreCase("placeholder")) {
					sb.append(none);
				} else {
					sb.append(changeStatus);
				}
				sb.append(s);
				String startDateAsTruncatedStr = "";
				String htsStartDate = "\""+startDateAsTruncatedStr+"\"";
				sb.append(htsStartDate);
				sb.append(s);
				String endDateAsTruncatedStr = "";
				String htsEndDate = "\""+endDateAsTruncatedStr+"\"";
				sb.append(htsEndDate);
				sb.append(s);
				String notes ="\"" +(rhme.getNotes() == null ? "" : rhme.getNotes())+ "\"";
				sb.append(notes);
				}
				sb.append("\n");
				}
				}
			}
			int numOfKids = getSortedChildrenOrCreateEmpty(rhme).size();
			if (guard < 10 && numOfKids > 0) { // replace 10 with better safety
				int whichKidIsCurrent = 0;
				List<CpscHtsManagementEntity> sortedList = rhme.getChildren();
				sortedList.sort(null);
				for (CpscHtsManagementEntity child : sortedList) {
					sb.append(formatCSVFlatOfHtsTreeOrSubtree(child, guard + 1, (++whichKidIsCurrent < numOfKids),isSpecialreport,filter,source,searchterm));
				}
			}
			return sb.toString();
}	

	private String formatCSVOfHtsTreeOrSubtree(CpscHtsManagementEntity rhme, int guard, boolean hasSiblingsToRight) {
		StringBuilder sb = new StringBuilder();
		String s = ",";
		if (rhme.getHtsCode().length() >0) {
			String htsCode = "\"" + rhme.getHtsCode() + "\"";
			sb.append(htsCode);
			sb.append(s);
			String htsCodeType = "\"" + rhme.getCodeType() + "\"";
			sb.append(htsCodeType);
			sb.append(s);
			String htsDescription = "\"" + truncateIfDebug(rhme.getDescription() == null ? "" : rhme.getDescription())
					+ "\"";
			sb.append(htsDescription);
			sb.append(s);
			String jurisdiction = "\"" + rhme.getJurisdiction() + "\"";
			sb.append(jurisdiction);
			sb.append(s);
			String targeted = "\"" + rhme.getTargeted() + "\"";
			sb.append(targeted);
			sb.append(s);
			String changeStatus = "\"" + rhme.getChangeStatus() + "\"";
			String none = "\"" + "None" + "\"";
			if (rhme.getChangeStatus().trim().equalsIgnoreCase("placeholder")) {
				sb.append(none);
			} else {
				sb.append(changeStatus);
			}
			sb.append(s);
			String startDateAsTruncatedStr = "";
			String htsStartDate = "\"" + startDateAsTruncatedStr + "\"";
			sb.append(htsStartDate);
			sb.append(s);
			String endDateAsTruncatedStr = "";
			String htsEndDate = "\"" + endDateAsTruncatedStr + "\"";
			sb.append(htsEndDate);
			sb.append(s);
			String notes = "\"" + (rhme.getNotes() == null ? "" : rhme.getNotes()) + "\"";
			sb.append(notes);
			sb.append("\n");
		}
		int numOfKids = getSortedChildrenOrCreateEmpty(rhme).size();
		if (guard < 10 && numOfKids > 0) { // replace 10 with better safety
			int whichKidIsCurrent = 0;
			List<CpscHtsManagementEntity> sortedList = rhme.getChildren();
			sortedList.sort(null);
			for (CpscHtsManagementEntity child : sortedList) {
				sb.append(formatCSVOfHtsTreeOrSubtree(child, guard + 1, (++whichKidIsCurrent < numOfKids)));
			}
		}
		return sb.toString();

	}
    
	/**
	 * to format the CSV for AppendixBsummary special report HTS = 10 source is
	 * passed in as a parameter defaults to current. for current go against the
	 * lookup table and for all others scratch table.
	 * 
	 * @param enumRepresentingAnyState
	 * @param includeLongDescFromJsonOutput
	 * @param sequenceId
	 * @return
	 */
	private String formatCSVForAppendixBSummary(HtsGlobalStateEnum enumRepresentingAnyState, String source,
			boolean includeLongDescFromJsonOutput, int sequenceId) {
		String username = getUsernameFor(enumRepresentingAnyState);
		List<CpscHtsManagementEntity> chmeList = null;
		Date today = (new DateTime(DateTimeZone.UTC)).toDate();
		StringBuilder sb = new StringBuilder();
		sb.append("HTSReport_AppendixB_Summary");
		sb.append("\n");
		String s = ",";
		if (source.equalsIgnoreCase("CPSC_CURRENT_WIP")) {
			chmeList = cpscHtsManagementRepository.obtainRefsForSourceCPSCcurrentWIP(username, today);
			return formatCPSCcurrentStrFromChmeList(chmeList, sb);

		} else if (source.equalsIgnoreCase("ITC_UPLOAD_WIP")) {
			List<CpscHtsManagementEntity> chmeAdds = cpscHtsManagementRepository
					.obtainChmeForStateITCUploadWIPAddsAppendixBSummary(username);
			List<CpscHtsManagementEntity> chmeRemoves = cpscHtsManagementRepository
					.obtainChmeForStateITCUploadWIPRemovesAppendixBSummary(username);
			sb.append("Added HTS Codes");
			for (CpscHtsManagementEntity addChme : chmeAdds) {
				sb.append(s);
				sb.append("\"");
				sb.append(addChme.getHtsCode());
				sb.append("\"");
			}
			sb.append("\n");
			sb.append("Removed HTS Codes");
			for (CpscHtsManagementEntity removeChme : chmeRemoves) {
				sb.append(s);
				sb.append("\"");
				sb.append(removeChme.getHtsCode());
				sb.append("\"");
			}

			return sb.toString();
		} else {

			return formatCPSCdefaultStrFromLookupList();
		}
	}

	private String formatCPSCcurrentStrFromChmeList(List<CpscHtsManagementEntity> chmeList, StringBuilder sb) {
		String s = ",";
		sb.append("Added HTS Codes");
		for (CpscHtsManagementEntity chme : chmeList) {
			if (chme.getJurisdiction()) {	
				sb.append(s);
				sb.append("\"");
				sb.append(chme.getHtsCode());
				sb.append("\"");
			}
		}
		sb.append("\n");
		sb.append("Removed HTS Codes");
		for (CpscHtsManagementEntity removeChme : chmeList) {
			if (!removeChme.getJurisdiction()) {
				sb.append(s);
				sb.append("\"");
				sb.append(removeChme.getHtsCode());
				sb.append("\"");
			}
		}

		return sb.toString();

	}

	private String formatCPSCdefaultStrFromLookupList() {
		StringBuilder sb = new StringBuilder();
		String s = ",";
		sb.append("HTSReport_AppendixB_Summary");
		sb.append("\n");
		Date latestFinalizeTS = transition.getLatestFinalizeEventTimeStamp();
		Date startTs = addMinutesToDate(latestFinalizeTS, -10);
		Date endTs = addMinutesToDate(latestFinalizeTS, 10);
		Date startsunSetTs = add3MonthsToDate(startTs);
		Date endsunSetTs = add3MonthsToDate(endTs);
		List<HtsMgmtLookupEntity> rhaeAllAddList = null;
		List<HtsMgmtLookupEntity> rhaesunSetList = null;
		List<HtsMgmtLookupEntity> rhaeAllRemoveList = null;
		Date today = (new DateTime(DateTimeZone.UTC)).toDate();
		rhaesunSetList = cpscHtsManagementRepository.obtainRefsunsetRemovesCurrentforSpecialReport(startsunSetTs, endsunSetTs);
		rhaeAllAddList = cpscHtsManagementRepository.obtainRefAllAddsforSpecialReport(startTs, endTs, today);
		rhaeAllRemoveList = cpscHtsManagementRepository.obtainRefAllRemovesforSpecialReport(startTs, endTs, today);
		sb.append("Added HTS Codes");
		for (HtsMgmtLookupEntity addRef : rhaeAllAddList) {
		
			sb.append(s);
			sb.append("\"");
			sb.append(addRef.getHtsCode());
			sb.append("\"");
		}
		sb.append("\n");
		sb.append("Removed HTS Codes");
		for (HtsMgmtLookupEntity removeRef : rhaeAllRemoveList) {
			sb.append(s);
			sb.append("\"");
			sb.append(removeRef.getHtsCode());
			sb.append("\"");
		}
		for (HtsMgmtLookupEntity removeRef : rhaesunSetList) {
			sb.append(s);
			sb.append("\"");
			sb.append(removeRef.getHtsCode());
			sb.append("\"");
		}
		return sb.toString();
	}

	private Date addMinutesToDate(Date origDate, int numOfMins) {
		long modifiedDateNum = origDate.getTime() + numOfMins * 60000;
		return new Date(modifiedDateNum);
	}
	private Date add3MonthsToDate(Date origDate) {
		return DateUtils.addDays(origDate, 100);
	}

}

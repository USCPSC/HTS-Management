package gov.cpsc.hts.itds.ui.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.cpsc.hts.itds.ui.domain.entity.CpscHtsManagementFileEntity;
import gov.cpsc.hts.itds.ui.domain.entity.CpscHtsMgmtHistEntity;
import gov.cpsc.hts.itds.ui.domain.repository.CpscHtsManagementFileRepository;
import gov.cpsc.hts.itds.ui.domain.repository.CpscHtsManagementRepository;
import gov.cpsc.hts.itds.ui.domain.repository.CpscHtsMgmtHistRepository;

import gov.cpsc.hts.itds.ui.service.exceptions.CpscHtsMgmtCodeEnum;
import gov.cpsc.hts.itds.ui.service.exceptions.CpscHtsMgmtException;

@Service
public class HtsStateTransitionService {
	
	@Autowired
    private SysPropertyService sysPropertyService;
	@Autowired
    private CpscHtsManagementRepository cpscHtsManagementRepository;
	@Autowired
    private CpscHtsManagementFileRepository cpscHtsManagementFileRepository;
	@Autowired
    private CpscHtsMgmtHistRepository cpscHtsMgmtHistRepository;
    
    private static final String usernameForHistTable = "TBD";
    private static final String categoryForTransitions = "TRANSITION";
    private static final String categoryForIncidents = "INCIDENT";
    private static final String categorySuffixForUpload = "_UPLD";
    private static final String categorySuffixForFinalize = "_FNLZ";
    private static final String SYS_PROPERTY_PREFIX = "HTS.SVC.";
    
	private class StateTuple {
    	
    	public final HtsStateTransitionEnum transition;
    	public final HtsGlobalStateEnum peel;
    	public final HtsGlobalStateEnum inference;
    	public final Date eventDate;
    	
    	public StateTuple(HtsStateTransitionEnum transition, HtsGlobalStateEnum peel, HtsGlobalStateEnum inference, Date eventDate) {
    		this.transition = transition;
    		this.peel = peel;
    		this.inference = inference;
    		this.eventDate = eventDate;
    	}
    }

    private StateTuple findLatestStateTuple() {
		HtsStateTransitionEnum transition = HtsStateTransitionEnum.REVERT_END;
		HtsGlobalStateEnum peel = HtsGlobalStateEnum.FINALIZED_NO_WIP;
		Date eventDate = new Date();
    	CpscHtsMgmtHistEntity chmhe = cpscHtsMgmtHistRepository.findFirstByCategoryOrderByEventDateDesc(categoryForTransitions);
		if (chmhe != null) {
			transition = HtsStateTransitionEnum.valueOf(chmhe.getStateTransition());
			peel = HtsGlobalStateEnum.valueOf(chmhe.getStateRestoration());
			eventDate = chmhe.getEventDate();
		}
		HtsGlobalStateEnum inference = null;
		switch (transition) {
		case ENABLE_START: 
		case UPLOAD_START: 
		case SAVE_START: 
		case FINALIZE_START: 
		case REVERT_START: 
			inference = HtsGlobalStateEnum.IN_TRANSITION;
			break; 
		case ENABLE_END: 
			inference = HtsGlobalStateEnum.CPSC_CURRENT_WIP;
			break; 
		case UPLOAD_END: 
			inference = HtsGlobalStateEnum.ITC_UPLOAD_WIP;
			break; 
		case SAVE_END: 
		case FINALIZE_STALL: 
			inference = peel; // CPSC_CURRENT_WIP or ITC_UPLOAD_WIP
			break; 
		case FINALIZE_END: 
			inference = HtsGlobalStateEnum.FINALIZED_NO_WIP;
			break; 
		case REVERT_END:
			inference = HtsGlobalStateEnum.FINALIZED_NO_WIP;
			break;
		default:
			// Unreachable (UNLESS HtsStateTransitionEnum is redefined), because valueOf would have thrown an exception.
			// See https://docs.oracle.com/javase/8/docs/api/java/lang/Enum.html#valueOf-java.lang.Class-java.lang.String-
			break;
		}
		return new StateTuple(transition, peel, inference, eventDate);
	}
	
	public HtsGlobalStateEnum getGlobalStateEnum() {
		return findLatestStateTuple().inference;
	}
	
    public HtsStateTransitionEnum findLatestStateTransition() {
    	return findLatestStateTuple().transition;
	}
    
    public Date getLatestFinalizeEventTimeStamp() {
		Date result = (new DateTime(DateTimeZone.UTC)).toDate(); // i.e. now
		List<Date> timestampList = cpscHtsMgmtHistRepository.obtainLatestFinalizeEventTimeStamp(categoryForTransitions);
		if (timestampList != null && !timestampList.isEmpty() && timestampList.get(0) != null) {
			result = timestampList.get(0); // all elements in timestampList have same value
		}
		return result;
	}
	
	private HtsGlobalStateEnum calculatePeelIfValidRequest(HtsStateTransitionEnum requestedTransition) throws CpscHtsMgmtException {
		if (requestedTransition == null) {
			throw new IllegalArgumentException("argument of enumerated type HtsStateTransitionEnum was null");
		}
		HtsGlobalStateEnum correspondingPeel = null;
		StateTuple currStateTuple = findLatestStateTuple();
		HtsStateTransitionEnum currTrans = currStateTuple.transition;
		switch (requestedTransition) {
		case ENABLE_START: 
		case UPLOAD_START: 
			if (currTrans == HtsStateTransitionEnum.FINALIZE_END || currTrans == HtsStateTransitionEnum.REVERT_END) {
				correspondingPeel = HtsGlobalStateEnum.FINALIZED_NO_WIP;
			}
			break;
		case FINALIZE_START: 
		case REVERT_START: 
		case SAVE_START:
			if (currTrans == HtsStateTransitionEnum.SAVE_END || currTrans == HtsStateTransitionEnum.ENABLE_END 
					|| currTrans == HtsStateTransitionEnum.UPLOAD_END || currTrans == HtsStateTransitionEnum.FINALIZE_STALL) {
				correspondingPeel = currStateTuple.inference; // CPSC_CURRENT_WIP or ITC_UPLOAD_WIP
			}
			break; 
		case SAVE_END:
			if (currTrans == HtsStateTransitionEnum.SAVE_START) {
				correspondingPeel = currStateTuple.peel; // CPSC_CURRENT_WIP or ITC_UPLOAD_WIP
			}
			break;
		case ENABLE_END: 
			if (currTrans == HtsStateTransitionEnum.ENABLE_START) {
				correspondingPeel = HtsGlobalStateEnum.CPSC_CURRENT_WIP;
			}
			break; 
		case UPLOAD_END: 
			if (currTrans == HtsStateTransitionEnum.UPLOAD_START) {
				correspondingPeel = HtsGlobalStateEnum.ITC_UPLOAD_WIP;
			}
			break; 
		case REVERT_END:
			if (currTrans == HtsStateTransitionEnum.REVERT_START) {
				correspondingPeel = HtsGlobalStateEnum.FINALIZED_NO_WIP;
			}
			break; 
		case FINALIZE_END: 
			if (currTrans == HtsStateTransitionEnum.FINALIZE_START) {
				correspondingPeel = HtsGlobalStateEnum.FINALIZED_NO_WIP;
			}
			break; 
		case FINALIZE_STALL:
			if (currTrans == HtsStateTransitionEnum.FINALIZE_START) {
				correspondingPeel = currStateTuple.peel; // CPSC_CURRENT_WIP or ITC_UPLOAD_WIP
			}
			break;
		default:
			// Unreachable (UNLESS HtsStateTransitionEnum is redefined), because this method would have thrown IllegalArgumentException.
			// See https://docs.oracle.com/javase/8/docs/api/java/lang/Enum.html#valueOf-java.lang.Class-java.lang.String-
			break;
		}
		if (correspondingPeel == null) {
			throw new CpscHtsMgmtException(CpscHtsMgmtCodeEnum.EXCEPTION_INVALID_TRANSITION_REQUEST, 159, 
					"request for " + requestedTransition + " invalid for current transition " + currTrans);
		}
		return correspondingPeel;
	}

	public void requestTransition(HtsStateTransitionEnum requestedTransition, String notes) throws CpscHtsMgmtException {
		if (requestedTransition == HtsStateTransitionEnum.UPLOAD_START) {
			throw new CpscHtsMgmtException(CpscHtsMgmtCodeEnum.EXCEPTION_INVALID_TRANSITION_REQUEST, 166, "UPLOAD_START requires file");
		}
		requestTransition(requestedTransition, null, notes, null, null);
	}
	
	public void requestTransition(HtsStateTransitionEnum requestedTransition, String source, String notes, File uploadedFile,
			String usernameAssociatedWithUploadedFile) throws CpscHtsMgmtException {
		if ((requestedTransition == HtsStateTransitionEnum.UPLOAD_START) == (uploadedFile == null)) {
			throw new CpscHtsMgmtException(CpscHtsMgmtCodeEnum.EXCEPTION_INVALID_TRANSITION_REQUEST, 173, "UPLOAD_START IFF file provided");
		}
		source = (source != null ? source.substring(0, Math.min(99, source.length())) : "none");
		notes = (notes != null ? notes.substring(0, Math.min(99, notes.length())) : "none");
        CpscHtsMgmtHistEntity chmhe = new CpscHtsMgmtHistEntity();
		chmhe.setStateTransition(requestedTransition.name());
    	// refrain from catching EXCEPTION_INVALID_TRANSITION_REQUEST if thrown by calculatePeelIfValidRequest:
    	chmhe.setStateRestoration(calculatePeelIfValidRequest(requestedTransition).name());
        chmhe.setUsername(usernameForHistTable);
        chmhe.setCategory(categoryForTransitions);
        chmhe.setNotes(notes);
		Date timestamp = generateUniqueTimestamp();
        chmhe.setEventDate(timestamp);
        if (uploadedFile != null) {
        	// UPLOAD_START is requested, or else this method would already have thrown CpscHtsMgmtException
	        CpscHtsManagementFileEntity mfe = new CpscHtsManagementFileEntity();
	        // no need to set mfe's reviewStatus, notes, rowCount or codeCount 
	        mfe.setUsername(usernameAssociatedWithUploadedFile);
	        mfe.setSource(source); // is serverSideName
	        int size = (int)(uploadedFile.length());
	        chmhe.setSize(size);
	        mfe.setSize(size);
	        mfe.setCreationDate(timestamp);
	        if (sysPropertyService.getSysPropertyBooleanByName(SYS_PROPERTY_PREFIX + HtsSysPropertyEnum.SAVE_FILE_AS_BLOB.name())) {
	        	try {
	            	byte[] data = Files.readAllBytes(uploadedFile.toPath());
	            	mfe.setCsv(data);
	        	} catch (Exception e) {
					throw new CpscHtsMgmtException(CpscHtsMgmtCodeEnum.EXCEPTION_READING_FILE, 197, source, e);
				}
	        }
	        try {
	        	cpscHtsManagementFileRepository.save(mfe);
	        } catch (Exception e) {
				throw new CpscHtsMgmtException(CpscHtsMgmtCodeEnum.EXCEPTION_WRITING_TO_FILE_TABLE, 206, source, e);
	        }
        }
        try {
        	cpscHtsMgmtHistRepository.save(chmhe);
        } catch (Exception e) {
        	// Iff UPLOAD_START was requested, a record has already been inserted into the file table, but there will
        	// now be no corresponding record in the history table. TODO If so, rollback the most recent record in the file table.
			throw new CpscHtsMgmtException(CpscHtsMgmtCodeEnum.EXCEPTION_RECORDING_HISTORY, 224, chmhe.toString(), e);
        }
	}
	
	private Date generateUniqueTimestamp() {
        try {
			Thread.sleep(5); // pause for 5 MILLIseconds to ensure each timestamp in history table is unique
		} catch (InterruptedException ie) {
		} // if interrupted, proceed
		return (new DateTime(DateTimeZone.UTC)).toDate(); // i.e. now		
	}

	public void recordIncident(HtsStateTransitionEnum context, CpscHtsMgmtCodeEnum codeForException, String notes) throws CpscHtsMgmtException {
        CpscHtsMgmtHistEntity chmhe = new CpscHtsMgmtHistEntity();
        StateTuple currStateTuple = findLatestStateTuple();
		chmhe.setStateTransition(currStateTuple.transition.name());
    	chmhe.setStateRestoration(currStateTuple.peel.name());
    	chmhe.setUsername(usernameForHistTable);
    	String category = categoryForIncidents;
    	switch (context) {
    	case UPLOAD_START:
    		category += categorySuffixForUpload;
    		break;
    	case FINALIZE_START:
    		category += categorySuffixForFinalize;
    		break;
    	default: // no need to modify category
    		break;
    	}
        chmhe.setCategory(category);
        chmhe.setIncident(codeForException.name());
        chmhe.setNotes(notes != null ? notes.substring(0, Math.min(99, notes.length())) : "none");
        chmhe.setEventDate(generateUniqueTimestamp());
        try {
        	cpscHtsMgmtHistRepository.save(chmhe);
        } catch (Exception e) {
			throw new CpscHtsMgmtException(CpscHtsMgmtCodeEnum.EXCEPTION_RECORDING_HISTORY, 183, chmhe.toString(), e);
        }
	}
	
	private String summarizeLastUpload() {
		String result = "";
    	CpscHtsMgmtHistEntity chmhe = cpscHtsMgmtHistRepository.findFirstByCategoryOrderByEventDateDesc(categoryForIncidents + categorySuffixForUpload);
    	if (chmhe != null) {
    		result = ", \"lastUpload\": {\"timestamp\": \"" + chmhe.getEventDate() + "\", \"result\": \"";
    		result += (chmhe.getIncident().equalsIgnoreCase(CpscHtsMgmtCodeEnum.OK.name()) ? "success" : "failure");
    		result += "\", \"incidentCode\": \"" + chmhe.getIncident();
    		result += "\", \"username\": \"" + chmhe.getUsername();
    		result += "\", \"notes\": \"" + chmhe.getNotes().replace("\"", "\\\"") + "\"}";
    	}
    	return result;
	}
	
	public HtsGlobalStateSummary generateHtsGlobalStateSummary(String progressAsInfix) {
		StateTuple stateTuple = findLatestStateTuple();
		String stateTransitionAsInfix = "\", \"stateTransition\": \"" + stateTuple.transition + "\", \"transitioningNow\": \"" 
				+ (stateTuple.inference == HtsGlobalStateEnum.IN_TRANSITION) + "\", \"stateTransitionTimestamp\": \"" + stateTuple.eventDate;
		String summary = "\"globalState\": \"" + stateTuple.inference.name() + stateTransitionAsInfix + "\", " + progressAsInfix + summarizeLastUpload();
		return new HtsGlobalStateSummary(stateTuple.inference, summary);
	}
	
	private HtsGlobalStateEnum emergencyRecovery() {
		CpscHtsManagementService chms = new CpscHtsManagementService();
		String usernameForCpscCurrentWip = chms.getUsernameFor(HtsGlobalStateEnum.CPSC_CURRENT_WIP);
		String usernameForItcUploadWip = chms.getUsernameFor(HtsGlobalStateEnum.ITC_UPLOAD_WIP);
    	long persistedCurrentCount = cpscHtsManagementRepository.countByUsername(usernameForCpscCurrentWip);
    	long persistedUploadCount = cpscHtsManagementRepository.countByUsername(usernameForItcUploadWip);
    	if (persistedCurrentCount > 0) {
    		return HtsGlobalStateEnum.CPSC_CURRENT_WIP;
    	} else if (persistedUploadCount > 0) {
    		return HtsGlobalStateEnum.ITC_UPLOAD_WIP;
    	} else {
    		return HtsGlobalStateEnum.FINALIZED_NO_WIP;
    	}
	}
	
}

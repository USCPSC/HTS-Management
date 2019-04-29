package gov.cpsc.hts.itds.ui.web.controller;

import gov.cpsc.hts.itds.sharedservice.dto.SysEvent;
import gov.cpsc.hts.itds.sharedservice.dto.audit.AuditRecord;
import gov.cpsc.hts.itds.ui.domain.entity.SysEventEntity;
import gov.cpsc.hts.itds.ui.service.SharedService;
import gov.cpsc.hts.itds.ui.shared.codec.JsonCodec;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shared")
public class SharedServiceController {

	private static final Logger logger = LoggerFactory.getLogger(SharedServiceController.class);

	public SharedServiceController() {
	}

	@Resource
	private SharedService sharedService;

	@RequestMapping(value = "/sysevent/collect", method = {RequestMethod.POST})
	public Object collectSysEvent(HttpServletRequest req, HttpServletResponse res, @RequestBody String jsonPayload) {
		logger.info(req.getPathInfo() + " is called");
		SysEvent dto = null;
		try {
			dto = JsonCodec.unmarshal(SysEvent.class, jsonPayload);
			System.out.println(ToStringBuilder.reflectionToString(dto));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		if (dto == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		try {
			sharedService.addSysEvent(dto);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("SysEvent'persistance error");
		}

		return ResponseEntity.ok("OK");
	}

	@RequestMapping(value = "/auditrecord/collect", method = {RequestMethod.POST})
	public Object collectAuditrecord(HttpServletRequest req, HttpServletResponse res, @RequestBody String jsonPayload) {
		logger.info(req.getPathInfo() + " is called");
		AuditRecord dto = null;
		try {
			dto = JsonCodec.unmarshal(AuditRecord.class, jsonPayload);
			System.out.println(ToStringBuilder.reflectionToString(dto));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		if (dto == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		try {
			sharedService.addAuditRecord(dto);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AuditRecord'persistance error");
		}

		return ResponseEntity.ok("OK");
	}

	/**
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/sysevents",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@RolesAllowed("ROLE_SYS_EVENT_READ")
	public Page<SysEventEntity> getSysEventList(
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "25") Integer pageSize
	) {
		logger.debug("REST call to getSysEventList");
		Pageable pageable = new PageRequest(
				page,
				pageSize,
				new Sort(
						new Order(Sort.Direction.DESC, "id")
				)
		);
		return sharedService.getSysEvents(pageable);
	}

	/**
	 * @param event
	 * @return
	 */
	@RequestMapping(value = "/sysevents/run",
			method = RequestMethod.GET,
			produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> runSystemEvent(
			@RequestParam(value = "event", required = true) String event) {
		logger.debug("REST call to runSystemEvent: " + event);
		try {
			sharedService.runSysEvent(event);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.ok("OK");
	}

}

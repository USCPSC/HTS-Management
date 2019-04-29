package gov.cpsc.hts.itds.ui.service.impl;

import gov.cpsc.hts.itds.sharedservice.dto.SysEvent;
import gov.cpsc.hts.itds.sharedservice.dto.audit.AuditRecord;
import gov.cpsc.hts.itds.ui.domain.entity.AuditRecordEntity;
import gov.cpsc.hts.itds.ui.domain.entity.SysEventEntity;
import gov.cpsc.hts.itds.ui.domain.repository.AuditRecordRepository;
import gov.cpsc.hts.itds.ui.domain.repository.SysEventRepository;
import gov.cpsc.hts.itds.ui.shared.codec.UtcDateTimeCodec;

import gov.cpsc.hts.itds.ui.service.SharedService;

import java.util.Date;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Qualifier("sharedService")
public class SharedServiceImpl implements SharedService {
	private static final Logger logger = LoggerFactory.getLogger(SharedServiceImpl.class);

	private String buildLogText(Object obj){
		return ToStringBuilder.reflectionToString(obj, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	public SharedServiceImpl() {
	}

	@Resource
	private SysEventRepository sysEventRepo;
	
	@Resource
	private AuditRecordRepository auditRecordRepo;

//	@Autowired
//	@Lazy
//	private TimerService timerService;
	

	private SysEventEntity converte(SysEvent dto) {
		SysEventEntity entity = new SysEventEntity();
		entity.setUserIdentifier(dto.getUserIdentifier());
		entity.setAppName(dto.getAppName());
		entity.setAppVersion(dto.getAppVersion());
		entity.setAction(dto.getAction());
		entity.setEventName(dto.getEventName());
		entity.setEventDescription(dto.getEventDescription());

		try {
			if (StringUtils.isEmpty(dto.getEventTimeStamp())) {
				logger.warn("Event's time stamp is not set");
			} else {
				Date eventTS = UtcDateTimeCodec.decode(dto.getEventTimeStamp());
				entity.setEventTimeStamp(eventTS);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		return entity;
	}
	
	
	private AuditRecordEntity converte(AuditRecord dto) {
		AuditRecordEntity entity = new AuditRecordEntity();
		entity.setUserIdentifier(dto.getUserId());
		entity.setAppName(dto.getGeneratorAppName());
		entity.setAppVersion(dto.getGeneratorAppVersion());
		entity.setEventAction( dto.getEventAction().toString() );
		entity.setNetworkAccessPoint( dto.getNetworkAccessPointEnum().toString() );
		entity.setNetworkAccessAddress(dto.getNetworkAccessAddress());
		
		entity.setQueryContextPath(dto.getQueryContextPath());
		entity.setQueryParameter(dto.getQueryParameter());

		try {
			if (StringUtils.isEmpty(dto.getEventTimeStamp())) {
				logger.warn("Event's time stamp is not set, which is required");
			} else {
				Date eventTS = UtcDateTimeCodec.decode(dto.getEventTimeStamp());
				entity.setEventTimeStamp(eventTS);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		return entity;
	}

	@Override
	public void addSysEvent(SysEvent dto) {
		if( dto == null ){
			logger.info("Input data objetc of SysEvent is null, ignored");
		}
		SysEventEntity entity = converte(dto);
		if( logger.isInfoEnabled() ){
			logger.info("Received an SysEvent = " + buildLogText(dto) );
			logger.info("Converted to SysEventEntity = " + buildLogText(entity) );
		}

		sysEventRepo.saveAndFlush(entity);
		
	}

	@Override
	public void addAuditRecord(AuditRecord dto) {
		if( dto == null ){
			logger.info("Input data objetc of SysEvent is null, ignored");
		}
		AuditRecordEntity entity = converte(dto);
		if( logger.isInfoEnabled() ){
			logger.info("Received an AuditRecord = " + buildLogText(dto) );
			logger.info("Converted to AuditRecordEntity = " + buildLogText(entity) );
		}

		auditRecordRepo.saveAndFlush(entity);
	}

    @Override
    public Page<SysEventEntity> getSysEvents(Pageable pageable) {
        return sysEventRepo.findAll(pageable);
    }

	@Override
	public void runSysEvent(String event) {
				
	}

}

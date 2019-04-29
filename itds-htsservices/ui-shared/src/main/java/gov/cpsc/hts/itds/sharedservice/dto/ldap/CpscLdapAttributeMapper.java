package gov.cpsc.hts.itds.sharedservice.dto.ldap;

import java.util.List;
import java.util.Vector;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.cpsc.hts.itds.ui.shared.codec.JsonCodec;

public class CpscLdapAttributeMapper extends ObjectMapper implements AttributesMapper {
	private static final Logger logger = LoggerFactory.getLogger(CpscLdapAttributeMapper.class);

	@Override
	public Object mapFromAttributes(Attributes attrs) throws NamingException {
//		logger.info("CpscLdapAttributeMapper::mapFromAttributes(): attrs=" + attrs + "<<<");

		String retStr = null;
		try {
//			logger.info("TEST GET LDAP mail" + attrs.get("mail"));
//			logger.info("TEST GET LDAP sn" + attrs.get("sn"));
			
			NamingEnumeration<String> idList = attrs.getIDs();
//			logger.info("********************************** TEST GET IDs **********************************");
//			while (idList.hasMore()) {
//				String ne = idList.next();
//				logger.info("      ne =" + ne + "<<<<<<");
//			}
			
			retStr = JsonCodec.marshal(idList);
		} catch (Exception e) {
//			logger.info(
//					"APPLICATION::DEBUG::40001::CpscLdapAttributeMapper::mapFromAttributes(): exception=" + e.getMessage());
			e.printStackTrace();
		}
		return retStr;
	}

}
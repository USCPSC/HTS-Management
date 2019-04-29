package gov.cpsc.hts.itds.ui.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.SerializationFeature;
import gov.cpsc.hts.itds.sharedservice.dto.ldap.CpscLdapAttributeMapper;
import gov.cpsc.hts.itds.ui.domain.entity.User;
import gov.cpsc.hts.itds.ui.domain.repository.UserRepository;

import gov.cpsc.hts.itds.ui.service.LdapService;

@Service("LdapService")
@Transactional
public class LdapServiceImpl extends BaseItdsServiceImpl implements LdapService {

	private static final Logger logger = LoggerFactory.getLogger(LdapServiceImpl.class);

	/*
	 * use LDAP to verify this user is indeed a user or not and if yes return DB username
	 */
	@Autowired
	UserRepository userRepository; 
	
	@Value("${hts.ldap.url}")
	private String ldapUrl;

	@Value("${hts.ldap.base}")
	private String ldapBase;

	public String getRamUsernameByLdapInfo (String email, String fn, String sn, String cn) throws IOException {
		logger.info("LdapServiceImpl::getRamUsernameByLdapInfo(): email="+email+"<<< first="+fn+
				"<<< last="+fn+"<<< cn="+cn+"<<<");
		
		// lookup LDAP
		String url = ldapUrl;
		String base = ldapBase;
		LdapContextSource ctxsrc = new LdapContextSource();
		ctxsrc.setUrl(url);
		ctxsrc.setBase(base);
		ctxsrc.setUserDn("");
		ctxsrc.setPassword("");
		ctxsrc.afterPropertiesSet();
		LdapTemplate ldapTemplate = new LdapTemplate(ctxsrc);
		AndFilter andFilter = new AndFilter();
		andFilter.and(new EqualsFilter("cn", cn));
		andFilter.and(new EqualsFilter("mail", email));
		andFilter.and(new EqualsFilter("sn", sn));
		andFilter.and(new LikeFilter("objectclass", "*Person*"));
		
		CpscLdapAttributeMapper attrMapper = new CpscLdapAttributeMapper();
		attrMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		
		List<String> list = null;
		try {
			list = ldapTemplate.search("", andFilter.encode(), attrMapper);
		}
		catch (Exception e) {
			logger.info("APPLICATION::50000::LdapServiceImpl::getRamUsernameByLdapInfo(): exception="+e.getMessage()+"<<<");
			// temp not thrown 
		}

		// search RAM DB, if any record from RAM DB then is found
		String username = null;
		if (list != null && list.size() > 0) {
			logger.info("APPLICATION::50001::LdapServiceImpl::list  size="+list.size()+"<<< email="+email+"<<< last name="+sn+"<<<");
			List<User> users = userRepository.findByEmailAndLastnameOrderByCreateTimestampDesc(email, sn);
			logger.info("APPLICATION::50002::LdapServiceImpl::users size="+users.size()+"<<<");
			if (users != null && users.size() > 0) {
				for (User user : users) {
					if (user.getUsername().equalsIgnoreCase(cn)) {
						logger.info("APPLICATION::50003::LdapServiceImpl::username match RAM username");
						username = user.getUsername();
						break;
					}
					logger.info("APPLICATION::50004::LdapServiceImpl:: check next matching email and sn in RAM DB");
				}
			}
			logger.info("APPLICATION::50005::LdapServiceImpl::username ="+username+"<<<");
		}
		return username;
	}
}

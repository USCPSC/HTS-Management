package gov.cpsc.hts.itds.ui.service;

import java.io.IOException;

public interface LdapService {

	public String getRamUsernameByLdapInfo (String email, String fn, String ln, String cn) throws IOException;
	
}

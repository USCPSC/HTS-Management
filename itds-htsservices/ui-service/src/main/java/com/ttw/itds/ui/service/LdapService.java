package com.ttw.itds.ui.service;

import java.io.IOException;

public interface LdapService {

	public String getRamUsernameByLdapInfo (String email, String fn, String ln, String cn) throws IOException;
	
}

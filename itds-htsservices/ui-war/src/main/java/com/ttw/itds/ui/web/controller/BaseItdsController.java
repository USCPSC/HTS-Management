package com.ttw.itds.ui.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ttw.itds.ui.domain.entity.User;
import com.ttw.itds.ui.shared.ApplicationConstants;

public class BaseItdsController {
	public void checkSession(HttpServletRequest request) throws Exception 
	{
		if(this.getSessionUser(request) == null) {
			throw new Exception("Login required");
		}
	}
	
	public User getSessionUser(HttpServletRequest request) {
		HttpSession session =request.getSession(false); 
		if(session != null) {
			return (User)session.getAttribute("user");
		}
		return null;
	}

	public String getSessionUserId(HttpServletRequest request) {
		HttpSession session =request.getSession(false); 
		if(session != null) {
			User user = (User)session.getAttribute("user");
			if(user!= null)
				return user.getUsername();
		}
		return null;
	}
	
	private static final SimpleDateFormat formatter = new SimpleDateFormat(ApplicationConstants.DateFormats.mmddyyyy);
	public static Date toDate(String val) throws ParseException {return (val == null)?null:formatter.parse(val);}
	
}

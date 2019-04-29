package gov.cpsc.itds.common.email;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.cpsc.itds.common.properties.PropertiesHandler;

public class RAMEmailer {

  private static final Logger logger = LoggerFactory.getLogger(RAMEmailer.class);

  public String sendEmail(String toEmailAddress, String subject, String body, boolean sendHtml)
	      throws EmailException {
	    logger.debug("toEmailAddress={}, subject={}", toEmailAddress, subject);
	    String rtn;
	    Email email;
	    if (sendHtml) {
	      email = new HtmlEmail();
	    } else {
	      email = new SimpleEmail();
	    }
		String[] toEmails = toEmailAddress.split(",");
	    email.setHostName(PropertiesHandler.getProperty(RAMEmailPropertyKeys.EMAIL_HOSTNAME));
	    email.setSmtpPort(PropertiesHandler.getIntProperty(RAMEmailPropertyKeys.EMAIL_PORT));
	    email.setFrom(PropertiesHandler.getProperty(RAMEmailPropertyKeys.FROM_EMAIL));
	    email.setSubject(subject);
	    email.setMsg(body);
	    email.addTo(toEmails);

	    String msgId = null;

	    msgId = email.send();

	    logger.info("Email sent. msgId={}", msgId);
	    rtn =
	        "{\"MessageId\":\""
	            + msgId
	            + "\",\"toEmailAddress\":\""
	            + toEmailAddress
	            + "\",\"subject\":\""
	            + subject
	            + "\"}";
	    return rtn;
	  }
  
}

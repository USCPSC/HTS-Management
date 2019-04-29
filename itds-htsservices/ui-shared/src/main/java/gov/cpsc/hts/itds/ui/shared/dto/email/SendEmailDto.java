package gov.cpsc.hts.itds.ui.shared.dto.email;

import gov.cpsc.hts.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 *
 * @author rzauel
 */
public class SendEmailDto extends ItdsUiBaseDto {

    private static final long serialVersionUID = 8466901897701708842L;
    
    private String toEmailAddress;
    private String subject;
    private String body;
    private Boolean sendHtml;

    public SendEmailDto() {
    }

    public SendEmailDto(String toEmailAddress, String subject, String body, Boolean sendHtml) {
        this.toEmailAddress = toEmailAddress;
        this.subject = subject;
        this.body = body;
        this.sendHtml = sendHtml;
    }
    
    public String getToEmailAddress() {
        return toEmailAddress;
    }

    public void setToEmailAddress(String toEmailAddress) {
        this.toEmailAddress = toEmailAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getSendHtml() {
        return sendHtml;
    }

    public void setSendHtml(Boolean sendHtml) {
        this.sendHtml = sendHtml;
    }

    @Override
    public String toString() {
        return "SendEmailDto{" + "toEmailAddress=" + toEmailAddress + ", subject=" + subject + ", body=" + body + ", sendHtml=" + sendHtml + '}';
    }

}

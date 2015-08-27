package edu.franklin.eac.model.pmp;

import java.util.Date;

@SuppressWarnings("serial")
public class PasswordResetTemp implements java.io.Serializable{
	private Integer passwordResetTempId;
	private String username;
	private Date effectiveDate;
	private String effectiveDateStr = "";
	private Date auditStamp;
		
	/** default constructor */
	public PasswordResetTemp() {
	}
		/** constructor with id */
	public PasswordResetTemp(Integer passwordResetTempId) {
		this.passwordResetTempId = passwordResetTempId;
	}
	public Integer getPasswordResetTempId() {
		return this.passwordResetTempId;
	}
	public void setPasswordResetTempId(Integer passwordResetTempId) {
		this.passwordResetTempId = passwordResetTempId;
	}
	public Date getAuditStamp() {
		return this.auditStamp;
	}
	public void setAuditStamp(Date auditStamp) {
		this.auditStamp = auditStamp;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEffectiveDateStr() {
		return effectiveDateStr;
	}
    public Date getEffectiveDate() {
        return this.effectiveDate;
    }
    
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}		
}

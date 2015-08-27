package edu.franklin.eac.model.pmp;
import java.util.Date;

@SuppressWarnings("serial")
public class Phonenumbers implements java.io.Serializable {

	private int phoneId;

	private Employee employee;

	private Department department;

	private String extension;

	private boolean faxFlag;

	private Date auditStamp;

	private String exchange;

	/*
	public Phonenumbers() {
	}

	public Phonenumbers(int phoneId, String extension, boolean faxFlag,
			Date auditStamp) {
		this.phoneId = phoneId;
		this.extension = extension;
		this.faxFlag = faxFlag;
		this.auditStamp = auditStamp;
	}

	public Phonenumbers(int phoneId, Employee employee, Department department,
			String extension, boolean faxFlag, Date auditStamp, String exchange) {
		this.phoneId = phoneId;
		this.employee = employee;
		this.department = department;
		this.extension = extension;
		this.faxFlag = faxFlag;
		this.auditStamp = auditStamp;
		this.exchange = exchange;
	}
*/
	public int getPhoneId() {
		return this.phoneId;
	}

	public void setPhoneId(int phoneId) {
		this.phoneId = phoneId;
	}

	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getExtension() {
		return this.extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public boolean isFaxFlag() {
		return this.faxFlag;
	}

	public void setFaxFlag(boolean faxFlag) {
		this.faxFlag = faxFlag;
	}

	public Date getAuditStamp() {
		return this.auditStamp;
	}

	public void setAuditStamp(Date auditStamp) {
		this.auditStamp = auditStamp;
	}

	public String getExchange() {
		return this.exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

}

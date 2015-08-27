package edu.franklin.eac.model.pmp;

import java.util.Date;

@SuppressWarnings("serial")
public class OrgChartTemp implements java.io.Serializable {

	private Integer orgChartTempId;
	private Employee employee;
	private Department department;
	private Position position;
	private Employee supervisor;
	private Integer supervisorId;
	private Integer employeeId;
	private Date effectiveDate;
	private String effectiveDateStr = "";
	private Short transferOrTerminate;
	private Location location;	
	private Date auditStamp;
	private Short infoUpdated;
	
	/** default constructor */
	public OrgChartTemp() {
	}

	/** constructor with id */
	public OrgChartTemp(Integer orgChartTempId) {
		this.orgChartTempId = orgChartTempId;
	}

	public Integer getOrgChartTempId() {
		return this.orgChartTempId;
	}

	public void setOrgChartTempId(Integer orgChartTempId) {
		this.orgChartTempId = orgChartTempId;
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

	public Position getPosition() {
		return this.position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Employee getSupervisor() {
		return this.supervisor;
	}

	public void setSupervisor(Employee supervisor) {
		this.supervisor = supervisor;
	}

	public Date getAuditStamp() {
		return this.auditStamp;
	}

	public void setAuditStamp(Date auditStamp) {
		this.auditStamp = auditStamp;
	}

	public Integer getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(Integer supervisorId) {
		this.supervisorId = supervisorId;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
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

	public void setTransferOrTerminate(Short transferOrTerminate) {
		this.transferOrTerminate = transferOrTerminate;
	}

	public Short getTransferOrTerminate() {
		return transferOrTerminate;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}	

	public void setInfoUpdated(Short infoUpdated) {
		this.infoUpdated = infoUpdated;
	}

	public Short getInfoUpdated() {
		return infoUpdated;
	} 
	
}

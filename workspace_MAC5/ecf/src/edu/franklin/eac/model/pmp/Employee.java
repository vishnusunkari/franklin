package edu.franklin.eac.model.pmp;

import java.util.Date;
import java.util.Set;

@SuppressWarnings({"serial", "unchecked"})
public class Employee extends BaseObject {

	private Integer employeeId;
	private String firstName;
	private String lastName;
	private String email;
	private String username;
	private String password;
	private Date hireDate;
	private Date transferDate;
    private Date auditStamp;	
	private short status;
	private Date terminationDate;
	private Location location;
	private Set orgCharts;
	private Set employees;
    private String hireDateStr = "";
    private String transferDateStr = "";   
    private String terminationDateStr = "";	
	private Short review30day;
	private Short review60day;
	private Short review90day;
	private Short infoUpdated;
	
    /** default constructor */
    public Employee() {
    }
    
    /** constructor with id */
    public Employee(Integer employeeId) {
        this.employeeId = employeeId;
    }

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public Date getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}

    public Date getAuditStamp() {
        return this.auditStamp;
    }
    
    public void setAuditStamp(Date auditStamp) {
        this.auditStamp = auditStamp;
    }
    
	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public Date getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationDate(Date terminationDate) {
		this.terminationDate = terminationDate;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}	

	public void setOrgCharts(Set orgCharts) {
		this.orgCharts = orgCharts;
	}

	public Set getOrgCharts() {
		return orgCharts;
	}	

	public void setEmployees(Set employees) {
		this.employees = employees;
	}

	public Set <Employee> getEmployees() {
		return employees;
	}   	
	
	public String getHireDateStr() {
		return hireDateStr;
	}

	public void setHireDateStr(String hireDateStr) {
		this.hireDateStr = hireDateStr;
	}

	public String getTransferDateStr() {
		return transferDateStr;
	}

	public void setTransferDateStr(String transferDateStr) {
		this.transferDateStr = transferDateStr;
	}

	public String getTerminationDateStr() {
		return terminationDateStr;
	}

	public void setTerminationDateStr(String terminationDateStr) {
		this.terminationDateStr = terminationDateStr;
	}

	public Short getReview30day() {
		return review30day;
	}

	public void setReview30day(Short review30day) {
		this.review30day = review30day;
	}

	public Short getReview60day() {
		return review60day;
	}

	public void setReview60day(Short review60day) {
		this.review60day = review60day;
	}

	public Short getReview90day() {
		return review90day;
	}

	public void setReview90day(Short review90day) {
		this.review90day = review90day;
	}

	public void setInfoUpdated(Short infoUpdated) {
		this.infoUpdated = infoUpdated;
	}

	public Short getInfoUpdated() {
		return infoUpdated;
	} 
    
}

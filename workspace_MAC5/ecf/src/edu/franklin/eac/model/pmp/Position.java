package edu.franklin.eac.model.pmp;

import java.util.Date;
import java.util.Set;

@SuppressWarnings({"serial", "unchecked"})
public class Position  implements java.io.Serializable {

	private Integer positionId;
    private String title;
    private Date auditStamp;
    private Set employees;
    
    /** default constructor */
    public Position() {
    }
    
    /** constructor with id */
    public Position(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getPositionId() {
        return this.positionId;
    }
    
    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public Date getAuditStamp() {
        return this.auditStamp;
    }
    
    public void setAuditStamp(Date auditStamp) {
        this.auditStamp = auditStamp;
    }

	public Set getEmployees() {
		return employees;
	}

	public void setEmployees(Set employees) {
		this.employees = employees;
	}
}
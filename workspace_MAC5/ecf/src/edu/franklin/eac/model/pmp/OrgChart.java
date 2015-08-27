package edu.franklin.eac.model.pmp;

import java.util.Date;

@SuppressWarnings("serial")
public class OrgChart  implements java.io.Serializable {

	private Integer orgChartId;
    private Employee employee;
    private Department department;
    private Position position;
    private Employee supervisor;
    private Integer supervisorId;
    private Integer employeeId;
    private Date auditStamp;

    /** default constructor */
    public OrgChart() {
  	
    }
    
    /** constructor with id */
    public OrgChart(Integer orgChartId) {
        this.orgChartId = orgChartId;
    }

    public Integer getOrgChartId() {
        return this.orgChartId;
    }
    
    public void setOrgChartId(Integer orgChartId) {
        this.orgChartId = orgChartId;
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
	
}

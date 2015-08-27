package edu.franklin.eac.model.pmp;

import java.util.Date;
import java.util.Set;

@SuppressWarnings({"serial", "unchecked"})
public class Department  implements java.io.Serializable {

	private Integer departmentId;
    private String name;
    private String email;
    private Building building;
    private Employee supervisor;
    private Date auditStamp;
    private Set employees;
    
    /** default constructor */
    public Department() {
    }
    
    /** constructor with id */
    public Department(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getDepartmentId() {
        return this.departmentId;
    }
    
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
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

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public Employee getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Employee supervisor) {
		this.supervisor = supervisor;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
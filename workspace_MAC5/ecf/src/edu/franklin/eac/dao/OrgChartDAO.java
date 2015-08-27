package edu.franklin.eac.dao;

import java.util.List;

import edu.franklin.eac.model.pmp.Department;
import edu.franklin.eac.model.pmp.Employee;
import edu.franklin.eac.model.pmp.OrgChart;
import edu.franklin.eac.model.pmp.OrgChartTemp;
import edu.franklin.eac.model.pmp.PasswordResetTemp;

@SuppressWarnings("unchecked")
public interface OrgChartDAO extends DAO {

    public void saveOrgChart(OrgChart orgChart);
	public OrgChart getOrgChart(Integer employeeId);
	public List getEmployees();
	public Employee getEmployee(Integer empId);
	public void saveEmployee(Employee employee);
    public void saveDepartment(Department dept);   
    public void updateDepartment(Department dept);  
    public OrgChart getOrgChartByEmployeeId(Integer empId);
	public void updateOrgChart(OrgChart orgChart);
	public void removeEmployeeFromOrgChart(Integer orgChartId);
	public OrgChartTemp getOrgChartTempByEmployeeId(Integer empId);
	public PasswordResetTemp getPasswordResetTempByUsername(String username);
	public void savePasswordResetTemp(PasswordResetTemp passwordResetTemp);
}
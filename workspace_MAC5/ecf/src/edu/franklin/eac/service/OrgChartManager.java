package edu.franklin.eac.service;

import java.util.List;

import edu.franklin.eac.model.pmp.Employee;
import edu.franklin.eac.model.pmp.OrgChart;

@SuppressWarnings("unchecked")
public interface OrgChartManager {
	public OrgChart getOrgChart(String orgChartId);
    public List getEmployees();    
    public Employee getEmployee(String employeeId);
    public Employee saveEmployee(Employee employee);
    public OrgChart saveOrgChart(OrgChart orgChart);
}

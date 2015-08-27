package edu.franklin.eac.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.franklin.eac.dao.OrgChartDAO;
import edu.franklin.eac.model.pmp.Employee;
import edu.franklin.eac.model.pmp.OrgChart;
import edu.franklin.eac.service.OrgChartManager;

@SuppressWarnings("unchecked")
public class OrgChartManagerImpl implements OrgChartManager {
    
	private static Log log = LogFactory.getLog(OrgChartManagerImpl.class);
    private OrgChartDAO orgChartDAO;
    Employee employee;
    
	public OrgChart getOrgChart(String orgChartId){
		OrgChart org = orgChartDAO.getOrgChart(Integer.valueOf(orgChartId));
		
		if(org == null){
			log.warn("OrgChartId '" + orgChartId + "' not found in database.");
		}
		return org;		
	}
    
    public void setOrgChartDAO(OrgChartDAO orgChartDAO) {
        this.orgChartDAO = orgChartDAO;
    }
	//@Override
	public Employee getEmployee(String employeeId) {
		Employee employee = orgChartDAO.getEmployee(Integer.valueOf(employeeId));
		
		if(employee == null){
			log.warn("EmployeeId '" + employeeId + "' not found in database.");
		}
		return employee;
	}

	//@Override
	public List getEmployees() {
		return orgChartDAO.getEmployees();
	}

	//@Override
	public Employee saveEmployee(Employee employee) {
		orgChartDAO.saveEmployee(employee);
		return employee;
	}
	

	//@Override
	public OrgChart saveOrgChart(OrgChart orgChart) {
		// TODO Auto-generated method stub
		orgChartDAO.saveOrgChart(orgChart);
		return orgChart;
	}

}

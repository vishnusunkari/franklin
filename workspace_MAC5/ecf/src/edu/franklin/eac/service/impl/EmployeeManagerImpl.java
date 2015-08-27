package edu.franklin.eac.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.franklin.eac.dao.EmployeeDAO;
import edu.franklin.eac.dao.OrgChartDAO;
import edu.franklin.eac.service.EmployeeManager;
import edu.franklin.eac.model.pmp.Department;
import edu.franklin.eac.model.pmp.Employee;
import edu.franklin.eac.model.pmp.OrgChart;
import edu.franklin.eac.model.pmp.OrgChartTemp;
import edu.franklin.eac.model.pmp.PasswordResetTemp;
import edu.franklin.eac.model.pmp.Phonenumbers;
import edu.franklin.eac.model.pmp.Position;


@SuppressWarnings("unchecked")
public class EmployeeManagerImpl implements EmployeeManager {
    
	private static Log log = LogFactory.getLog(EmployeeManagerImpl.class);
    private EmployeeDAO employeeDAO;
    private OrgChartDAO orgDAO;

    public void setEmployeeDAO(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }
    
    public void setOrgChartDAO(OrgChartDAO orgDAO){
    	this.orgDAO = orgDAO;
    }
    
	//@Override
	public Employee getEmployee(String employeeId) {
		Employee employee = employeeDAO.getEmployee(Integer.valueOf(employeeId));
		
		if(employee == null){
			log.warn("EmployeeId '" + employeeId + "' not found in database.");
		}
		return employee;
	}

    
    //@Override
	public Employee getEmployeeByEmail(String email) {
		Employee employee = employeeDAO.getEmployeeByEmail(email);
		
		if(employee == null){
			log.warn("Email '" + email+ "' not found in database.");
		}
		return employee;
	}
    
	//@Override
	public List getEmployees() {
		return employeeDAO.getEmployees();
	}

	//@Override
	public Employee saveEmployee(Employee employee) {
		employeeDAO.saveEmployee(employee);
		return employee;
	}
	
	//@Override
	public OrgChart saveOrgChart(OrgChart orgChart) {
		orgDAO.saveOrgChart(orgChart);
		return orgChart;
	}

	//@Override
	public Position  getPositionByTitle(String title) {
		return employeeDAO.getPositionByTitle(title);
	}

	//@Override
	public Department getDepartmentByDepartId(Integer departId) {
		return employeeDAO.getDepartmentByDepartId(departId);
	}

	//@Override
	public OrgChart getOrgChartByEmployeeId(String employeeId) {
		return orgDAO.getOrgChartByEmployeeId(Integer.valueOf(employeeId));
	}

	//@Override
	public void updateOrgChart(OrgChart orgChart) {
		orgDAO.updateOrgChart(orgChart);

	}

	//@Override
	public void removeEmployeeFromOrgChart(Integer orgChartId) {
		orgDAO.removeEmployeeFromOrgChart(orgChartId);
		
	}

	//@Override
	public List getRoomByRoomName(String roomName) {
		return employeeDAO.getRoomByRoomName(roomName);
	}

	//@Override
	public List getLocationByRoomIdAndBuildingId(Integer employeeRoomId,
			Integer buildingId) {
		return employeeDAO.getLocationByRoomIdAndBuildingId(employeeRoomId, buildingId);
	}

	public List getAllBuildings() {		
		return employeeDAO.getAllBuildings();
	}

	public List getAllRoomsFromLocationByBuildingId(Integer buildingId) {
		return employeeDAO.getAllRoomsFromLocationByBuildingId(buildingId);
	}

	public List getPhoneByPhoneExtension(String phoneExtension) {
		return employeeDAO.getPhoneByPhoneExtension(phoneExtension);
	}

	public Phonenumbers savePhonenumbers(Phonenumbers employeePhone) {
		employeeDAO.savePhonenumbers(employeePhone);
		return employeePhone;
		
	}

	public Position getPositionById(Integer posId) {
		Position position = employeeDAO.getPosition(posId);
		
		if(position == null){
			log.warn("PositionId '" + posId + "' not found in database.");
		}
		return position;
	}

	public void saveOrgChartTemp(OrgChartTemp orgChartTemp) {
		employeeDAO.saveOrgChartTemp(orgChartTemp);
		
	}

	public void saveNewPosition(Position pos) {
		employeeDAO.saveNewPosition(pos);		
	}

	public OrgChartTemp getOrgChartTempByEmployeeId(String employeeID) {
		return orgDAO.getOrgChartTempByEmployeeId(Integer.valueOf(employeeID));
	}

	@Override
	public List getAllDepartments() {
		return employeeDAO.getAllDepartments();
	}

	@Override
	public PasswordResetTemp getPasswordResetTempByUsername(String username) {
		return orgDAO.getPasswordResetTempByUsername(username);
	}

	@Override
	public void savePasswordResetTemp(PasswordResetTemp passwordResetTemp) {
		orgDAO.savePasswordResetTemp(passwordResetTemp);
		
	}

}

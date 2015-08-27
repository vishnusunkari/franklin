package edu.franklin.eac.service;

import java.util.List;

import edu.franklin.eac.model.pmp.Department;
import edu.franklin.eac.model.pmp.Employee;
import edu.franklin.eac.model.pmp.OrgChart;
import edu.franklin.eac.model.pmp.OrgChartTemp;
import edu.franklin.eac.model.pmp.PasswordResetTemp;
import edu.franklin.eac.model.pmp.Phonenumbers;
import edu.franklin.eac.model.pmp.Position;


@SuppressWarnings("unchecked")
public interface EmployeeManager {
    public List<Employee> getEmployees();
    public List getAllDepartments();
	public List getAllBuildings();    
	public List getAllRoomsFromLocationByBuildingId(Integer buildingId);	
    public Employee getEmployee(String employeeId);
    public Employee getEmployeeByEmail(String email);
    public Employee saveEmployee(Employee employee);
	public void updateOrgChart(OrgChart orgChart);    
    public OrgChart saveOrgChart(OrgChart orgChart);
	public Position getPositionById(Integer posId);    
    public Position getPositionByTitle(String title);
	public void saveNewPosition(Position pos);    
	public Department getDepartmentByDepartId(Integer departId);
	public OrgChart getOrgChartByEmployeeId(String employeeId);
	public void removeEmployeeFromOrgChart(Integer orgChartId);
	public List getRoomByRoomName(String roomName);
	public List getLocationByRoomIdAndBuildingId(Integer employeeRoomId, Integer buildingId);
	public List getPhoneByPhoneExtension(String phoneExtension);
	public Phonenumbers savePhonenumbers(Phonenumbers employeePhone);
	public OrgChartTemp getOrgChartTempByEmployeeId(String employeeID);
	public PasswordResetTemp getPasswordResetTempByUsername(String username);
	public void saveOrgChartTemp(OrgChartTemp orgChartTemp);
	public void savePasswordResetTemp(PasswordResetTemp passwordResetTemp);
		
}

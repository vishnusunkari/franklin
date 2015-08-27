package edu.franklin.eac.dao;

import java.util.List;

import edu.franklin.eac.model.pmp.Department;
import edu.franklin.eac.model.pmp.Employee;
import edu.franklin.eac.model.pmp.OrgChartTemp;
import edu.franklin.eac.model.pmp.Phonenumbers;
import edu.franklin.eac.model.pmp.Position;

@SuppressWarnings("unchecked")
public interface EmployeeDAO extends DAO {

	public List getEmployees();
	public List getAllDepartments();	
	public Employee getEmployee(Integer empId);
	public Employee getEmployeeByEmail(String email);	
	public void saveEmployee(Employee employee);
	public Position getPositionByTitle(String title);
	public Department getDepartmentByDepartId(Integer departId);
	public List getRoomByRoomName(String roomName);
	public List getLocationByRoomIdAndBuildingId(Integer employeeRoomId,
			Integer buildingId);
	public List getAllBuildings();
	public List getAllRoomsFromLocationByBuildingId(Integer buildingId);
	public List getPhoneByPhoneExtension(String phoneExtension);
	public void savePhonenumbers(Phonenumbers employeePhone);
	public Position getPosition(Integer posId);
	public void saveOrgChartTemp(OrgChartTemp orgChartTemp);
	public void saveNewPosition(Position pos);

	
}

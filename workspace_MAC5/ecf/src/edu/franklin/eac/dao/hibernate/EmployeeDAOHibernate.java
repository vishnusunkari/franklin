package edu.franklin.eac.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import edu.franklin.eac.dao.EmployeeDAO;
import edu.franklin.eac.model.pmp.Department;
import edu.franklin.eac.model.pmp.Employee;
import edu.franklin.eac.model.pmp.OrgChartTemp;
import edu.franklin.eac.model.pmp.Phonenumbers;
import edu.franklin.eac.model.pmp.Position;

public class EmployeeDAOHibernate extends HibernateDaoSupport implements EmployeeDAO {
	
	private static Log log = LogFactory.getLog(EmployeeDAOHibernate.class);

	public Employee getEmployee(Integer employeeId) {
		return (Employee) getHibernateTemplate().get(Employee.class, employeeId);
	}
	
	public Employee getEmployeeByEmail(String email) {
		return (Employee) getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("from Employee as emp where emp.email = ?").setString(0, email).list().get(0);
	}

	@SuppressWarnings("unchecked")	
	public List getEmployees() {
		return getHibernateTemplate().find("from Employee");
	}

	public void saveEmployee(Employee employee) {
		getHibernateTemplate().saveOrUpdate(employee);
		if(log.isDebugEnabled()) {
			log.debug("userId set to: " + employee.getEmployeeId());
		}
	}
	
	public Position getPositionByTitle(String title) {		
		return (Position) getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("from Position as pos where pos.title = ?").setString(0, title).list().get(0);
	}

	public Department getDepartmentByDepartId(Integer departId) {
		return (Department) getHibernateTemplate().get(Department.class, departId);
		//return getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("from Department as depart where depart.departmentId = ?").setString(0, departId).list();	
	}

	@SuppressWarnings("unchecked")
	public List getRoomByRoomName(String roomName) {
		return getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("from Room as room where room.name = ?").setString(0, roomName).list();
	}
	
	@SuppressWarnings("unchecked")
	public List getLocationByRoomIdAndBuildingId(Integer employeeRoomId,
			Integer buildingId) {
		return getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("from Location as loc where loc.room = ? and loc.building = ?").setInteger(0, employeeRoomId).setInteger(1, buildingId).list();
	}

	@SuppressWarnings("unchecked")
	public List getAllBuildings() {
		//return getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("from Building").list();
		return getHibernateTemplate().find("from Building");
	}

	@SuppressWarnings("unchecked")
	public List getAllRoomsFromLocationByBuildingId(Integer buildingId) {
		return getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("from Location as loc where loc.building = ?").setInteger(0, buildingId).list();	
	}

	@SuppressWarnings("unchecked")
	public List getPhoneByPhoneExtension(String phoneExtension) {
		return getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("from Phonenumbers as phone where phone.extension = ?").setString(0, phoneExtension).list();
	}

	public void savePhonenumbers(Phonenumbers employeePhone) {
		getHibernateTemplate().saveOrUpdate(employeePhone);
		if(log.isDebugEnabled()) {
			log.debug("phoneId set to: " + employeePhone.getPhoneId());
		}		
	}
	public Position getPosition(Integer posId) {
		return (Position) getHibernateTemplate().get(Position.class, posId);
	}

	public void saveOrgChartTemp(OrgChartTemp orgChartTemp) {
		getHibernateTemplate().saveOrUpdate(orgChartTemp);
		if(log.isDebugEnabled()) {
			log.debug("orgChartTempId set to: " + orgChartTemp.getOrgChartTempId());
		}		
	}

	public void saveNewPosition(Position pos) {
		getHibernateTemplate().saveOrUpdate(pos);		
	}

	@SuppressWarnings("unchecked")
	public List getAllDepartments() {
		return getHibernateTemplate().find("from Department as dept order by dept.name");
	}
	
}

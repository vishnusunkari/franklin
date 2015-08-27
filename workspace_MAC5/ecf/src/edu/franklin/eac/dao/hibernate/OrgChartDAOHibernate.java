package edu.franklin.eac.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import edu.franklin.eac.dao.OrgChartDAO;
import edu.franklin.eac.model.pmp.Department;
import edu.franklin.eac.model.pmp.Employee;
import edu.franklin.eac.model.pmp.OrgChart;
import edu.franklin.eac.model.pmp.OrgChartTemp;
import edu.franklin.eac.model.pmp.PasswordResetTemp;


public class OrgChartDAOHibernate extends HibernateDaoSupport implements OrgChartDAO {
	
	private static Log log = LogFactory.getLog(OrgChartDAOHibernate.class);
	Employee employee;

	public OrgChart getOrgChart(Integer employeeId){
		return (OrgChart) getHibernateTemplate().get(OrgChart.class, employeeId);
	}
	
	public Employee getEmployee(Integer employeeId) {
		return (Employee) getHibernateTemplate().get(Employee.class, employeeId);
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
	
	public void saveOrgChart(OrgChart orgChart) {
		getHibernateTemplate().saveOrUpdate(orgChart);
		if(log.isDebugEnabled()) {
			log.debug("orgChartId set to: " + orgChart.getOrgChartId());
		}
	}

	public void saveDepartment(Department dept) {
        getHibernateTemplate().save(dept);		
	}

	public void updateDepartment(Department dept) {
        getHibernateTemplate().update(dept);		
	}

	public OrgChart getOrgChartByEmployeeId(Integer employeeId) {
		return (OrgChart) getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("from OrgChart as org where org.employeeId = ?").setInteger(0, employeeId).list().get(0);
			
	}

	public void updateOrgChart(OrgChart orgChart) {
		getHibernateTemplate().update(orgChart);		
	}

	public void removeEmployeeFromOrgChart(Integer orgChartId) {
		Object orgChart = getHibernateTemplate().load(OrgChart.class, orgChartId);
		getHibernateTemplate().delete(orgChart);		
	}

	public OrgChartTemp getOrgChartTempByEmployeeId(Integer employeeId) {
		return (OrgChartTemp) getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("from OrgChartTemp as orgTemp where orgTemp.employeeId = ?").setInteger(0, employeeId).list().get(0);
	}

	@Override
	public PasswordResetTemp getPasswordResetTempByUsername(String username) {
		return (PasswordResetTemp) getHibernateTemplate().get(PasswordResetTemp.class, username);
	}

	@Override
	public void savePasswordResetTemp(PasswordResetTemp passwordResetTemp) {
		getHibernateTemplate().saveOrUpdate(passwordResetTemp);
		
		if(log.isDebugEnabled()) {
			log.debug("Id set to: " + passwordResetTemp.getPasswordResetTempId());
		}		
	}

}

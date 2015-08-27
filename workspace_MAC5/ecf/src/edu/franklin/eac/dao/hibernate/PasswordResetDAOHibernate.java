package edu.franklin.eac.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.franklin.eac.dao.PasswordResetDAO;
import edu.franklin.eac.model.passwordreset.ResetQuestions;

public class PasswordResetDAOHibernate extends HibernateDaoSupport implements PasswordResetDAO {

	private static Log log = LogFactory.getLog(PasswordResetDAOHibernate.class);
	
	@SuppressWarnings("unchecked")
	public void removeEmployeesFromResetQuestions(String userName) {
		 List empList = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("from ResetQuestions as resetQuest where resetQuest.username = ?").setString(0, userName).list();
		 Iterator empListItr = null;
		 if(empList!=null && !empList.isEmpty()){
			 empListItr = empList.iterator();
			while (empListItr.hasNext()){
				ResetQuestions resetQuest = (ResetQuestions) empListItr.next();
				log.info("Employee being removed: " + resetQuest.getUsername());
				getHibernateTemplate().delete(resetQuest);
			}
			log.info(userName + " is removed from password_reset database");
		 } else {
			 log.info(userName + " NOT FOUND in the password_reset database.");
		 }
		 
		 log.info("List size of employees removed: " + empList.size());
	}

	@SuppressWarnings("unchecked")
	public List getPasswordResetDAO(String userName) {
		return getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("from ResetQuestions as resetQuest where resetQuest.username = ?").setString(0, userName).list();
	}

}

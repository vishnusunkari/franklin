package edu.franklin.eac.service.impl;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

import edu.franklin.eac.dao.PasswordResetDAO;
import edu.franklin.eac.service.PasswordResetManager;

public class PasswordResetManagerImpl implements PasswordResetManager {

	//private static Log log = LogFactory.getLog(PasswordResetManagerImpl.class);
	private PasswordResetDAO dao;
    
    public void setPasswordResetDAO(PasswordResetDAO dao) {
        this.dao = dao;
    }
    
	public void removeEmployeesFromResetQuestions(String userName) {
		dao.removeEmployeesFromResetQuestions(userName);		
	}

}

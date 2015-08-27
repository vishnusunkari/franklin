package edu.franklin.eac.dao;

import java.util.List;

@SuppressWarnings("unchecked")
public interface PasswordResetDAO extends DAO{
	public List getPasswordResetDAO(String userName);
	public void removeEmployeesFromResetQuestions(String userName);
}

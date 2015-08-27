package edu.franklin.eac.dao;

import java.util.List;

public interface ContactDAO {
	public List <Person> getAllStaff();
	public List <Person> getAllFaculty();
	public List <Person> getAllAdjunct();
}

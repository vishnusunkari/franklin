package edu.franklin.eac.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import edu.franklin.eac.service.EmployeeManager;


import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class EmployeeController implements Controller {
	
	private static Log log = LogFactory.getLog(EmployeeController.class);
	private EmployeeManager mgr = null;
	
	public void setEmployeeManager(EmployeeManager employeeManager) { 
		this.mgr = employeeManager;
	}
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'handleRequest' method...");
		}
		return new ModelAndView("empList", "employees", mgr.getEmployees());
	}

}

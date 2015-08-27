package edu.franklin.eac.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import edu.franklin.eac.ldap.LDAPClient;
import edu.franklin.eac.model.pmp.Department;
import edu.franklin.eac.model.pmp.Employee;
import edu.franklin.eac.model.pmp.OrgChart;
import edu.franklin.eac.model.pmp.OrgChartTemp;
import edu.franklin.eac.model.pmp.PasswordResetTemp;
import edu.franklin.eac.model.pmp.Position;
import edu.franklin.eac.service.EmployeeManager;
import edu.franklin.eac.service.PasswordResetManager;
import edu.franklin.eac.util.BuildEmail;
import edu.franklin.eac.util.DateConversion;
import edu.franklin.eac.util.IOUtil;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.RequestUtils;
import org.springframework.web.bind.ServletRequestDataBinder;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


@SuppressWarnings("deprecation")
public class EmployeeFormController extends SimpleFormController {
	private static Log log = LogFactory.getLog(EmployeeFormController.class);
	private EmployeeManager employeeManager = null;
	private PasswordResetManager passwordResetManager = null;

	static String appResourceFilename = "application";
	static ResourceBundle appResource = ResourceBundle.getBundle(appResourceFilename);
	private static String macHostAddress = appResource.getString("macHostAddress");
	private static String ldapCGI = appResource.getString("ldapCGI");
	private String[] ldapFailEmail = appResource.getString("email.ldapfail").split("[\\s,;]+");
	private static String fromAddressForLDAPAccount = appResource.getString("email.ldapaccount.from");
	private static String helpdeskTerminateQueue = appResource.getString("email.helpdesk.terminate.queue");
	private static String helpdeskLinuxQueue = appResource.getString("email.helpdesk.linux.queue");
	private static String franklinEmailTail = appResource.getString("franklin.email.tail");

	public void setEmployeeManager(EmployeeManager employeeManager) {
		this.employeeManager = employeeManager;
	}
	
	public void setPasswordResetManager(PasswordResetManager passwordResetManager) {
		this.passwordResetManager = passwordResetManager;
	}

	public EmployeeFormController() {
		super();
		setCommandClass(Employee.class);
	}

	// Method that executes before getting to Jsp
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request) throws Exception {
		log.info("Method that executes before getting to Jsp");
		Map reference = new HashMap();
		List<String> departmentList = null;
		    	
    	/*
    	 * Update local.js by calling LDAPClient Thread    	
    	 */
		//log.info("Start updating local.js in all the 3 places");
		//Thread LDAPThread = new Thread(new LDAPClient());
    	//LDAPThread.start();		
		
		log.info("Getting all the Department names to fill in the Department drop-down field");
		try {
			log.info("Start getAllDepartments");
			departmentList = employeeManager.getAllDepartments();
			log.info("getAllDepartments Success");
		} catch (DataAccessException dae) {
			log.error("     DataAccessException: "
					+ dae.getStackTrace().toString());
		}
		request.setAttribute("departmentList", departmentList);
		return reference;
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws ServletException {
		String id = RequestUtils.getStringParameter(request, "id", "");
		Employee command = new Employee();

		if (!"".equals(id)) {
			command = employeeManager.getEmployee(id);
		}
		return command;
	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		// convert java.lang.Long
		NumberFormat nf = NumberFormat.getNumberInstance();
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, nf, true));
	}

	/**
	 * Redirect to the successView when the cancel button has been pressed.
	 */
	/*public ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if (request.getParameter("cancel") != null) {
			return new ModelAndView(getSuccessView());
		}

		return super.processFormSubmission(request, response, command, errors);
	}*/

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		log.info("begin 'onSubmit' method of EmployeeFormController");
		
		Employee employee = (Employee) command;
		log.info("employee object's employeeId from command object of spring bind: " + employee.getEmployeeId());
		
		OrgChart orgChart = new OrgChart();
		OrgChartTemp orgChartTemp = new OrgChartTemp();

		final String requestType = request.getParameter("requestType");
		String cc = (String) request.getParameter("cc");
		String ccUsername = (String) request.getParameter("ccUsername");
		log.info("ccUsername: " + ccUsername);
		String ccEmail = null;
		if(ccUsername!=null && !ccUsername.isEmpty()){
			ccEmail = ccUsername+franklinEmailTail;
		}
		log.info("ccEmail: " + ccEmail);
		String effectiveDateWithDayName = (String) request.getParameter("effectiveDate");
		// Convert form page's date text to Date object
		DateConversion dateConvertObj = new DateConversion();
		Date hireTransferTerminationDate = dateConvertObj.formatStringToDate(effectiveDateWithDayName);
		String hireTransferTerminationDateStr = effectiveDateWithDayName;
		Date currentDate = Calendar.getInstance().getTime();
		log.info("requestType: " + requestType);
		log.info("macHostAddress: " + macHostAddress);	
	
		/*//for testing purpose
		 * int i = 1; if(i==1){ return new ModelAndView(getSuccessView()); }
		 */

		String employeeType = null;
		String employeeFirstName = null;
		String employeeLastName = null;
		String type = null;
		String employeeName = null;

		// New Employee
		String employeeUsername = null;
		String employeeEmail = null;

		String supervisorName = null;
		String supervisorEmail = null;
		String title = null;
		String supervisorUsername = null;
		Integer positionId = null;
		Integer departId = null;
		Integer employeeId = null;
		Integer supervisorId = null;
		Date hireDate = null;
		String departmentName = null;
		Department department = null;
		Employee supervisor = null;
		Position position = null;
		BuildEmail email = new BuildEmail();
		StringBuffer macURL = new StringBuffer();
		ArrayList <String> toAddress = new ArrayList<String>();
		String subject = null;
		String fromAddress = null;

		//List of emails successfully sent
		ArrayList<String> emailServiceDeskSent = new ArrayList<String>();
		//List of emails Failed
		ArrayList<String> emailServiceDeskFailed = new ArrayList<String>();
		
		//No need of any request.getSession attributes;
		//request.getSession().setAttribute(arg0, arg1);

		/*
		 * After Manual LDAP Account Creation Form submission (requestType = 0)
		 * Update local.js Manual LDAP Account Creation (requestType = 0) New
		 * Hire (requestType = 1) Transfer (requestType = 2) Termination
		 * (requestType = 3)
		 */

		/*
		 * When employeeType={Staff, Faculty, Student}->{Create LDAP Account,
		 * Access PMP and Send SupervisorEmail} When employeeType={Adjunct
		 * Faculty, Tutor, Work-Study, Other}->{Create LDAP Account} requestType
		 * = 0 is only for employeeType = {Staff, Faculty, Student}
		 */

		if (requestType.equals("0")) {
			Map<String, Object> ldapModel = new Hashtable<String, Object>();
			ldapModel.put("requestType", requestType);
			ldapModel.put("effectiveDate", hireTransferTerminationDateStr);
			log.info("Inside if requestType = " + requestType);

			employeeType = (String) request.getParameter("employeeType");
			employeeUsername = (String) request.getParameter("userName");
			employeeEmail = (String) request.getParameter("userName") + franklinEmailTail;
			employeeFirstName = request.getParameter("firstName");
			employeeLastName = request.getParameter("lastName");
			employeeName = employeeFirstName + " " + employeeLastName;
			title = request.getParameter("title");
			supervisorName = request.getParameter("supervisor");
			departmentName = request.getParameter("department");
			employeeType = request.getParameter("employeeType");
			supervisorUsername = request.getParameter("supervisorUserName");
			supervisorEmail = supervisorUsername + franklinEmailTail;

			if (employeeType != null) {
				if (employeeType.equals("staff")
						|| employeeType.equals("faculty")
						|| employeeType.equals("student")) {
					log.info("Inside if employeeType = " + employeeType);	
					try{
						supervisorId = Integer.parseInt(request.getParameter("supervisorId"));
					} catch (NumberFormatException nfe) {
						log.info("supervisorId may be null: " + nfe.getMessage());
					}
					try{
						positionId = Integer.parseInt(request.getParameter("positionId"));						
					} catch (NumberFormatException nfe) {
						log.info("positionId may be null: " + nfe.getMessage());
					}
					try{
						departId = Integer.parseInt(request.getParameter("departId"));
					} catch (NumberFormatException nfe) {
						log.info("departId may be null: " + nfe.getMessage());
					}
		
					hireDate = dateConvertObj.formatStringToDate(effectiveDateWithDayName);
					Employee existsEmployee = null;
					try {
						
						/*
						 *  Check whether an employee exists with the same email in the Employee table
						 *  If exists look whether that employee is a terminated employee.  If yes append that
						 *	employee's email with old_ and then save this new Employee in the db, else if 
						 *  not a terminated employee return error page
						 */
						
						existsEmployee = employeeManager.getEmployeeByEmail(employeeEmail);
						log.info("getEmployeeByEmail Success");
						log.info("The New employeeEmail: " + employeeEmail
									+ " already exists in the pmp db." 
									+ "\n Looking whether this employee is a re-hiree..");
						if (existsEmployee.getTerminationDate() != null) {
							log.info("Employee is a re-hiree so appending old_ " +
										"to his previous email in the db");
							existsEmployee.setEmail("old_" + employeeEmail);
							existsEmployee.setUsername("old_" + employeeUsername);
							employeeManager.saveEmployee(existsEmployee);
							log.info("New employee successfully saved into db");
						} else {
							log.info("Employee is not a re-hiree, there exists an active employee "
											+ "with same email " + employeeEmail 
											+ " in the db, thus returning error");
							ldapModel.put("existsActiveEmployee", true);
							return new ModelAndView(getSuccessView(), ldapModel);
						}
					} catch (IndexOutOfBoundsException iob) {
						log.info("New employeeEmail: " + employeeEmail
								+ " is a fresh email. So its good to save into db");						
					} catch (DataAccessException dae) {						
						log.error("Error saving data into Database: " + dae.getClass()
								+ "  " + dae.getCause() + "  " + dae.getMessage());
						log.error("StackTrace: " + IOUtil.StackTraceToString(dae));
						ldapModel.put("dataAccessException", true);
						return new ModelAndView(getSuccessView(), ldapModel);
					} catch (Exception e) {
						log.error("Error: " + e.getClass()
								+ "  " + e.getCause() + "  " + e.getMessage());
						log.error("StackTrace: " + IOUtil.StackTraceToString(e));
						ldapModel.put("exception", true);
						return new ModelAndView(getSuccessView(), ldapModel);
					}
					try {
		
						log.info("Data embedded in the URL for delayedLDAP form: \n");
						log.info("employeeType: " + employeeType							
								+ "\n employeeEmail: " + employeeEmail
								+ "\n employeeFirstName: " + employeeFirstName
								+ "\n employeeLastName: " + employeeLastName
								+ "\n positionId: " + positionId 
								+ "\n departId: " + departId 
								+ "\n employeetype: " + employeeType
								+ "\n supervisorEmail: " + supervisorEmail
								+ "\n supervisorId: " + supervisorId
								+ "\n ccEmail: " + ccEmail
								+ "\n effectiveDateWithDayName: " + effectiveDateWithDayName 
								+ "\n hireDate: " + hireDate);
		
						employee.setEmail(employeeEmail);
						employee.setUsername(employeeUsername);
						employee.setPassword(employeeFirstName.toLowerCase());
						employee.setHireDate(hireDate);
						employee.setStatus(new Short((short) 1));
						employee.setReview30day(new Short((short) 0));
						employee.setReview60day(new Short((short) 0));
						employee.setReview90day(new Short((short) 0));
						employee.setInfoUpdated(new Short((short) 0));
		
						// Update PMP Database
						log.info("Updating PMP Database\n");
						employeeManager.saveEmployee(employee);
						orgChart.setEmployee(employee);
						supervisor = employeeManager.getEmployee(supervisorId.toString());
						orgChart.setSupervisor(supervisor);
						log.info("Start getPositionById\n");
						position = employeeManager.getPositionById(positionId);
						log.info("getPositionById successful\n");
						orgChart.setPosition(position);
						log.info("Start getDepartmentByDepartId\n");
						department = employeeManager.getDepartmentByDepartId(departId);
						log.info("getDepartmentByDepartId is successful\n");
						orgChart.setDepartment(department);
						log.info("department set in orgChart");
						employeeManager.saveOrgChart(orgChart);
						log.info("orgChart saved in db");
						
						
						// Change Date to String object
						//String hireDateStr = dateConvertObj.formatDateToString(hireDate);
						// log.info("Date String is: " + hireDateStr);
		
						/*
						 * Make sure within the macURL ie sent to supervisor email, the
						 * requestType is changed to requestType=1 as it is New Hire Not
						 * changing the original requestType variable value as it
						 * effects the part of code down below ie the reason it was
						 * declared as final.
						 */
						 String requestTypeChanged = "1";
						 log.info("requestType being Changed to embed in the url, requestTypeChaged= "
						  			+ requestTypeChanged);
						 macURL.append(macHostAddress + "/ecf/mac/updateEmployee.html?"
								 + "requestType=1" + "&employeeID=" + employeeUsername 
								 + "&employeeName=" + employeeName								  
								 + "&employeeTitle=" + title.replace("&", "%26") 
								 + "&employeeDepartment=" + departmentName.replace("&", "%26") 
								 + "&supervisorID=" + supervisorUsername 
								 + "&supervisorName=" + supervisorName
								 + "&supervisorDepartment=" + departmentName.replace("&", "%26")
								 + "&effectiveDate=" + hireTransferTerminationDateStr
								 + "&employeeType=" + employeeType);
						 log.info("macLink is :" + macURL.toString());
						toAddress.add(supervisorEmail);
						if(ccEmail!=null){
							toAddress.add(ccEmail);
						}
						 // Send an email to Supervisor
						 log.info("Sending an email to supervisor " + supervisorEmail);
						 if (email.supervisorReminderEmail(toAddress.toArray(),
								 							employeeName, macURL,
								 							requestTypeChanged, effectiveDateWithDayName)) {
							 ldapModel.put("emailSupervisorSent", supervisorEmail);
							 log.info("Sending an email to supervisor SUCCESS");
						 } else {
							 ldapModel.put("emailSupervisorFailed", supervisorEmail);
							 log.info("Sending an email to supervisor FAILED");
						 }		
						ldapModel.put("updateDatabase", true);
						
						/*
						 * Run the LDAPThread to update local.js file in all 3 places
						 */
				    	//Thread LDAPThread = new Thread(new LDAPClient());
				    	//LDAPThread.start();
				    	
						
						return new ModelAndView(getSuccessView(), ldapModel);
		
					} catch (DataAccessException dae) {
						log.error("Error saving data into db: " + dae.getClass()
								+ "  " + dae.getCause() + "  " + dae.getMessage());
						log.error("StackTrace: " + IOUtil.StackTraceToString(dae));
						ldapModel.put("dataAccessException", true);
						return new ModelAndView(getSuccessView(), ldapModel);
					} catch (Exception e) {				
						log.error("Error while saving data into Database: " + e.getClass()
								+ "  " + e.getCause() + "  " + e.getMessage());
						log.error("StackTrace: " + IOUtil.StackTraceToString(e));
						ldapModel.put("exception", true);
						return new ModelAndView(getSuccessView(), ldapModel);
					}
					
				} else if((employeeType.equals("workstudy")) 
							|| employeeType.equals("tutor")
							|| employeeType.equals("other")) {
					log.info("Inside if employeeType= workstudy or tutor or other.  No saving into database, just supervisor email");
					// Change Date to String object
					hireDate = dateConvertObj.formatStringToDate(effectiveDateWithDayName);
					/*
					 * Make sure within the macURL ie sent to supervisor email, the
					 * requestType is changed to requestType=1 as it is New Hire Not
					 * changing the original requestType variable value as it
					 * effects the part of code down below ie the reason it was
					 * declared as final.
					 */
					 String requestTypeChanged = "1";
					macURL.append(macHostAddress + "/ecf/mac/updateEmployee.html?"
							+ "requestType=1"
							+ "&employeeID=" + employeeUsername
							+ "&employeeName=" + employeeName							 
							+ "&employeeTitle=" + title.replace("&", "%26")
							+ "&employeeDepartment=" + departmentName.replace("&", "%26")
							+ "&supervisorID=" + supervisorUsername
							+ "&supervisorName=" + supervisorName
							+ "&supervisorDepartment=" + departmentName.replace("&", "%26")
							+ "&effectiveDate=" + hireTransferTerminationDateStr
							+ "&employeeType=" + employeeType);
					log.info("macLink is : \n" + macURL.toString());
					toAddress.add(supervisorEmail);
					if(ccEmail!=null){
						toAddress.add(ccEmail);
					}
					log.info("Sending email to Supervisor email: " + supervisorEmail);
					if (email.supervisorReminderEmail(toAddress.toArray(),
								employeeName, macURL, requestTypeChanged,
								hireTransferTerminationDateStr)) {
						ldapModel.put("emailSupervisorSent", supervisorEmail);
						log.info("Sending an email to supervisor SUCCESS");
					} else {
						ldapModel.put("emailSupervisorFailed", supervisorEmail);
						log.info("Sending an email to supervisor FAILED");
					}
					ldapModel.put("updateDatabase", false);	
					if(supervisorName != null){
						ldapModel.put("supervisorName", supervisorName);	
					}
					return new ModelAndView(getSuccessView(), ldapModel);
				} else {
					log.info("Inside else part. ie employeeType = adjunct");
					log.info("No Saving into Database and No Supervisor Email since employeeType: "
									+ employeeType);
					ldapModel.put("exception", true);
					return new ModelAndView(getSuccessView(), ldapModel);
				}
			} else {
				log.info("employeeType is: " + employeeType);
				ldapModel.put("exception", true);
				return new ModelAndView(getSuccessView(), ldapModel);
			}

		}

		/*
		 * Manual LDAP Account Creation (requestType = 0) New Hire (requestType
		 * = 1) Transfer (requestType = 2) Termination (requestType = 3)
		 */
		if (requestType.equals("1")) {			
			log.info("Inside if requestType = 1");
			Map<String, Object> ecfModel = new HashMap<String, Object>();
			ecfModel.put("requestType", requestType);
			ecfModel.put("effectiveDate", hireTransferTerminationDateStr);

			try {
				employeeType = (String) request.getParameter("employeeType");
				employeeFirstName = employee.getFirstName();
				// Need to remove the comma ie added to the last name (spring bind)
				employeeLastName = employee.getLastName().replace(',', ' ');
				employeeName = employeeFirstName + " " + employeeLastName;
				title = (String) request.getParameter("title");
				supervisorName = (String) request.getParameter("supervisor");
				supervisorUsername = (String) request.getParameter("sUsername");
				departId = Integer.parseInt(request.getParameter("department"));
				departmentName = (String) request.getParameter("departmentName");
				log.info("departmentId: " + departId);
				log.info("departmentName: " + departmentName);

				ecfModel.put("employeeType", employeeType);
				ecfModel.put("employeeName", employeeName);
				ecfModel.put("title", title);
				ecfModel.put("supervisorName", supervisorName);
				ecfModel.put("departmentName", departmentName);
								
				if (supervisorUsername != null) {
					supervisorEmail = supervisorUsername + franklinEmailTail;
				}

				/*
				 * When employeeType={Staff, Faculty, Student}->{Create LDAP
				 * Account, Access PMP and Send SupervisorEmail} When
				 * employeeType={Adjunct Faculty, Tutor, Work-Study,
				 * Other}->{Create LDAP Account}
				 */
				if (employeeType != null && (employeeType.equals("staff")
												|| employeeType.equals("faculty") 
													|| employeeType.equals("student"))) {
					log.info("Inside if employeeType = staff or faculty or student");
					log.info("employeeType: " + employeeType);
					
					// Retrieve department of the employee
					department = retrieveDepartmentFromDB(departId);
					departmentName = department.getName();
					
					// Retrieve supervisor for the employee from PMP database
					if((supervisor = retrieveSupervisorFromDB(supervisorEmail))== null){
						ecfModel.put("empTypeQueryException", true);
						return new ModelAndView(getSuccessView(), ecfModel);
					}
					supervisorId = supervisor.getEmployeeId();
					log.info("supervisor Id : " + supervisor.getEmployeeId().toString());

					// Retrieve position of the employee from PMP database
					if((position = retrievePositionFromDB(title)) == null){
						ecfModel.put("positionQueryException", true);
						return new ModelAndView(getSuccessView(), ecfModel);
					}
					positionId = position.getPositionId();
					log.info("Position Id : " + position.getPositionId());

				}

				/*
				 * When employeeType={staff, faculty, student}->{Create LDAP
				 * Account, Access PMP and Send SupervisorEmail} When
				 * employeeType={adjunct, tutor, workstudy,
				 * other}->{Create LDAP Account}
				 */
				if (employeeType != null) {
					if (employeeType.equals("faculty")) {
						type = "Faculty";
					} else if (employeeType.equals("adjunct")) {
						type = "Adjunct";
					} else if (employeeType.equals("staff")
							|| employeeType.equals("tutor")
							|| employeeType.equals("workstudy")
							|| employeeType.equals("student")
							|| employeeType.equals("other")) {
						type = "Staff";
					}
				}
				log.info("type: " + type);
				
				// Connection to LDAP Server using LDAP API
				// String theCGI = "http://ldap-devel.dev.mc.franklin.edu/cgi-bin/ssoapi.cgi?";
				// look in properties file
				String msg = ldapAccountCreationMsg(employeeFirstName, employeeLastName, type);
				
				// Retrieve Newly created employee's username, password and
				// email from LDAP's success message
				if (msg.contains("SUCCESS")) {
					log.info("LDAP Account Creation SUCCESS.  "
									+ "\n Sending an email to Supervisor to fill out mac form \n");
					log.info("Updating local.js in all 3 locations\n");

					/*
					 * Run the LDAPThread to update local.js file in all 3 places
					 */
					
			    	//Thread LDAPThread = new Thread(new LDAPClient());
			    	//LDAPThread.start();

					String[] info = msg.split(" ");
					employeeUsername = info[1];
					//String employeePassword = info[2];

					log.info("employeeUsername: " + employeeUsername);
					//System.out.println("employeePassword: " + employeePassword);

					// input model attribute for ldap success output
					ecfModel.put("ldapAccount", true);
					/*
					 * When employeeType={Staff, Faculty, Student}->{Create LDAP
					 * Account, Access PMP and Send SupervisorEmail} When
					 * employeeType={Adjunct Faculty, Tutor, Work-Study,
					 * Other}->{Create LDAP Account}
					 */
					if (employeeType != null) {
						if (employeeType.equals("staff")
								|| employeeType.equals("faculty")
								|| employeeType.equals("student")) {
							log.info("Inside IF employeeType = staff or faculty or student");
							log.info("employeeType = " + employeeType);
							employeeEmail = employeeUsername + franklinEmailTail;
							Employee existsEmployee = checkExistsEmployeeInDB(employeeEmail, employeeUsername);
							if(existsEmployee != null){
								/* There exists an active employee within DB 
								   with the given employeeEmail so return error page. */								 
								ecfModel.put("existsActiveEmployee", true);
								return new ModelAndView(getSuccessView(), ecfModel);
							}
								
							employee.setEmail(employeeEmail);
							employee.setUsername(employeeUsername);
							employee.setPassword(employeeFirstName.toLowerCase());
							employee.setHireDate(hireTransferTerminationDate);
							employee.setStatus(new Short((short) 1));
							// employee.setLocation(location)
							employee.setReview30day(new Short((short) 0));
							employee.setReview60day(new Short((short) 0));
							employee.setReview90day(new Short((short) 0));

							/*
							 * Update this field in the Employee table only for
							 * new hires ie requestType=1. For transfers update
							 * infoUpdated field for OrgChartTemp table. Cronjob
							 * will send emails depending on this field from
							 * Employee table and OrgChartTemp table. When the
							 * OrgChartTemp table field is updated in the
							 * OrgChart table on the Effective Date then the
							 * field infoUpdated is updated from OrgChartTemp
							 * table to infoUpdated of Employee table so that
							 * the recurring emails continue until mac form is
							 * submitted.
							 */
							employee.setInfoUpdated(new Short((short) 0));

							orgChart.setEmployee(employee);
							orgChart.setDepartment(department);
							orgChart.setPosition(position);
							orgChart.setSupervisor(supervisor);
							orgChart.setDepartment(department);

							// Update PMP Database
							log.info("Updating PMP Database ie saving employee and orgChart into db");
							employeeManager.saveEmployee(employee);
							employeeManager.saveOrgChart(orgChart);
							log.info("Updating PMP Database ie saving employee and orgChart into db SUCCESS");
							macURL.append(macHostAddress
									+ "/ecf/mac/updateEmployee.html?"
									+ "requestType=" + requestType
									+ "&employeeID=" + employeeUsername
									+ "&employeeName=" + employeeName									 
									+ "&employeeTitle=" + title.replace("&", "%26")
									+ "&employeeDepartment=" + departmentName.replace("&", "%26")
									+ "&supervisorID=" + supervisor.getUsername()
									+ "&supervisorName=" + supervisorName
									+ "&supervisorDepartment=" + departmentName.replace("&", "%26")
									+ "&effectiveDate=" + hireTransferTerminationDateStr
									+ "&employeeType=" + employeeType);
							log.info("macLink is : \n" + macURL.toString());
							toAddress.add(supervisorEmail);
							if(ccEmail!=null){
								toAddress.add(ccEmail);
							}
							log.info("Sending email to Supervisor email: " + supervisorEmail);
							if (email.supervisorReminderEmail(toAddress.toArray(), employeeName, 
																	macURL, requestType, 
																	hireTransferTerminationDateStr)) {
								ecfModel.put("emailSupervisorSent", supervisorEmail);
								log.info("Sending an email to supervisor SUCCESS");
							} else {
								ecfModel.put("emailSupervisorFailed", supervisorEmail);
								log.info("Sending an email to supervisor FAILED");
							}
							return new ModelAndView(getSuccessView(), ecfModel);
						} else if (employeeType.equals("workstudy") || 
									employeeType.equals("tutor") || employeeType.equals("other")) {
							// Change Date to String object
							hireTransferTerminationDateStr = dateConvertObj.formatDateToString(hireTransferTerminationDate);
							// log.info("Date String is: " + hireTransferTerminationDateStr);
							macURL.append(macHostAddress
									+ "/ecf/mac/updateEmployee.html?"
									+ "requestType=" + requestType
									+ "&employeeID=" + employeeUsername
									+ "&employeeName=" + employeeName									 
									+ "&employeeTitle=" + title.replace("&", "%26")
									+ "&employeeDepartment=" + departmentName.replace("&", "%26")
									+ "&supervisorID=" + supervisorUsername
									+ "&supervisorName=" + supervisorName
									+ "&supervisorDepartment=" + departmentName.replace("&", "%26")
									+ "&effectiveDate=" + hireTransferTerminationDateStr
									+ "&employeeType=" + employeeType);
							log.info("macLink is : \n" + macURL.toString());						
							log.info("Sending email to Supervisor email: " + supervisorEmail);
							toAddress.add(supervisorEmail);
							if(ccEmail!=null){
								toAddress.add(ccEmail);
							}
							if (email.supervisorReminderEmail(toAddress.toArray(), employeeName,
										macURL, requestType, hireTransferTerminationDateStr)) {
								ecfModel.put("emailSupervisorSent", supervisorEmail);
								log.info("Sending an email to supervisor SUCCESS");
							} else {
								ecfModel.put("emailSupervisorFailed", supervisorEmail);
								log.info("Sending an email to supervisor FAILED");
							}						
							return new ModelAndView(getSuccessView(), ecfModel);
						} else {
							log.info("Inside else part of if employeeType = staff or faculty or student or workstudy");
							log.info("No Saving into Database and No Supervisor Email since employeeType: "
											+ employeeType);
							return new ModelAndView(getSuccessView(), ecfModel);
						}
					} else {
						log.info("employeeType is: " + employeeType);
						ecfModel.put("exception", true);
						return new ModelAndView(getSuccessView(), ecfModel);
					}
				} else {
					log.info("LDAP Account Creation Failed. \n"
							+ "Sending a HelpDesk Ticket \n");
					ecfModel.put("ldapAccount", false);
					/*
					 * Make sure within the macURL ie sent to helpdesk, the
					 * requestType is changed to requestType=0 as it will be
					 * delayedLDAP submission. Do Not change requestType
					 * variable value as it effects the part of code down below.
					 */
					macURL.append(macHostAddress
							+ "/ecf/delayedldap/delayedLDAP.html?"
							+ "requestType=0" 
							+ "&firstName=" + employeeFirstName 
							+ "&lastName=" + employeeLastName 
							+ "&title=" + title.replace("&", "%26")
							+ "&positionId=" + positionId 							 
							+ "&department=" + departmentName.replace("&", "%26") 
							+ "&departId=" + departId 
							+ "&supervisor=" + supervisorName
							+ "&supervisorId=" + supervisorId
							+ "&supervisorUserName=" + supervisorUsername
							+ "&cc=" + cc
							+ "&ccUsername=" + ccUsername
							+ "&effectiveDate=" + effectiveDateWithDayName
							+ "&hireDate=" + hireTransferTerminationDate.toString()
							+ "&employeeType=" + employeeType);
					log.info("macURL: " + macURL.toString() + "\n");
					StringBuffer content = new StringBuffer();
					content.append(	"The LDAP account creation script encountered an error when trying "
											+ "to generate a user name for ")
							.append(employeeFirstName)
							.append(" ")
							.append(employeeLastName)
							.append(".  Please manually create an LDAP account for this user.\n\n")
							.append("First Name: ").append(employeeFirstName).append("\n")
							.append("LastName: ").append(employeeLastName).append("\n\n")
							.append("Employee Type: ").append(employeeType)
							.append("\n\n");

					if (employeeType != null
							&& (employeeType.equals("staff")
									|| employeeType.equals("faculty") 
									|| employeeType.equals("student")
									|| employeeType.equals("workstudy")
									|| employeeType.equals("tutor")
									|| employeeType.equals("other"))) {
						content.append(	"\n\nAdditionally, click the link below and enter the user name in the provided form.  " +
										"PLEASE NOTE: You must submit this form to initiate the MAC process for this employee.\n\n")
								.append(macURL.toString().replace(" ", "%20"));
					}
					log.info("Sending an email to helpdesk for manual LDAP Account Creation\n");
					fromAddress = fromAddressForLDAPAccount;
					subject = helpdeskLinuxQueue + employeeName;
					if (email.generateHelpDeskEmail(ldapFailEmail, fromAddress,	subject, content)) {
						emailServiceDeskSent.add(subject);
						log.info("Sending a helpdesk ticket for manual LDAP Account Creation SUCCESS");
					} else {
						emailServiceDeskFailed.add(subject);
						log.info("Sending a helpdesk ticket for manual LDAP Account Creation FAILED");
					}
					if (emailServiceDeskSent != null){
						ecfModel.put("emailServiceDeskSent", emailServiceDeskSent);
					}
					if (emailServiceDeskFailed != null) {
						ecfModel.put("emailServiceDeskFailed", emailServiceDeskFailed);
					}
					return new ModelAndView(getSuccessView(), ecfModel);
				}
			} catch (DataAccessException dae) {				
				log.error("DataAccessException: " + dae.getMessage() + "  "
						+ dae.getCause() + "  " + dae.getClass());
				log.error("StackTrace: " + IOUtil.StackTraceToString(dae));
				ecfModel.put("dataAccessException", true);
				return new ModelAndView(getSuccessView(), ecfModel);
			} catch (Exception e) {				
				log.error("Exception: " + e.getClass() + "  " + e.getCause()
						+ "  " + e.getMessage());
				log.error("StackTrace: " + IOUtil.StackTraceToString(e));
				ecfModel.put("exception", true);
				return new ModelAndView(getSuccessView(), ecfModel);
			}
		}
		/*
		 * (requestType = 0)- Manual LDAP Account Creation 
		 * (requestType = 1)- New Hire 
		 * (requestType = 2)- Transfer 
		 * (requestType = 3)- Termination
		 */
		else if (requestType.equals("2")) {
			log.info("Inside if requestType = 2");
			Map <String, Object> ecfModel = new HashMap<String, Object>(); 		
			ecfModel.put("requestType", requestType);
			ecfModel.put("effectiveDate", hireTransferTerminationDateStr);

			try {				
				employeeName = (String) request.getParameter("employee");
				title = (String) request.getParameter("newTitle");
				supervisorName = (String) request.getParameter("newSupervisor");
				supervisorUsername = (String) request.getParameter("nsUsername");
				if (supervisorUsername != null) {
					supervisorEmail = supervisorUsername + franklinEmailTail;
				}

				departId = Integer.parseInt(request.getParameter("newDepartment"));
				//employeeType = newEmployeeType
				employeeType = (String) request.getParameter("newEmployeeType");								
				ecfModel.put("employeeType", employeeType);
				
				// String[] employeeNameArray = employeeName.split(" ");
				// employeeFirstName = employeeNameArray[0];
				// employeeLastName = employeeNameArray[1];
				employeeUsername = (String) request.getParameter("eUsername");
				employeeEmail = employeeUsername + franklinEmailTail;			
				
				ecfModel.put("employeeName", employeeName);
				ecfModel.put("title", title);
				ecfModel.put("supervisorName", supervisorName);

				Employee emp = null;
				if (employeeEmail != null) {
					try {
						log.info("Start getEmployeeByEmail\n");
						// Retrieve employee by email
						emp = employeeManager.getEmployeeByEmail(employeeEmail);
						log.info("getEmployeeByEmail Success\n");
					} catch (IndexOutOfBoundsException e) {
						log.info("IndexOutOfBoundsException: Employee Name: " + employeeName
								+ " with employeeEmail: " + employeeEmail
								+ " not found in the pmp database");
						log.info(employeeName + " is of employeeType adjunct/workstudy/tutor/other");
						//ecfModel.put("empTypeQueryException", true);
						//return new ModelAndView(getSuccessView(), ecfModel);
						
						
					} catch (DataAccessException dae) {						
						log.info("DataAccessException: Error retrieving employeebyEmail from pmp database");
						log.error("DataAccessError : " + dae.getClass() + "  "
								+ dae.getCause() + "  " + dae.getMessage());
						log.error("StackTrace: " + IOUtil.StackTraceToString(dae));
						ecfModel.put("dataAccessException", true);
						return new ModelAndView(getSuccessView(), ecfModel);
					}
				}
				
				// Retrieve department of the employee
				department = retrieveDepartmentFromDB(departId);
				departmentName = department.getName();				
				
				//(emp == null) implies oldEmployeeType is nonPMP type
				if (emp == null){					
					//newEmployeeType is nonPMP type
					if (employeeType.equals("adjunct")){	
						log.info(employeeName + " is of employeeType adjunct");
						ecfModel.put("empTypeQueryException", true);
						return new ModelAndView(getSuccessView(), ecfModel);
					} 
					//newEmployeeType is nonPMP type
					else if (employeeType.equals("workstudy") ||
							employeeType.equals("tutor") ||
								employeeType.equals("other")){
						
						//send email to old and new supervisors with the mac link
						
						// log.info("Date String is: " + hireTransferTerminationDateStr);
						macURL.append(macHostAddress
								+ "/ecf/mac/updateEmployee.html?"
								+ "requestType=" + requestType
								+ "&employeeID=" + employeeUsername
								+ "&employeeName=" + employeeName								 
								+ "&employeeTitle=" + title.replace("&", "%26")
								+ "&employeeDepartment=" + departmentName.replace("&", "%26")
								+ "&supervisorID=" + supervisorUsername
								+ "&supervisorName=" + supervisorName
								+ "&supervisorDepartment=" + departmentName.replace("&", "%26")
								+ "&effectiveDate="	+ hireTransferTerminationDateStr
								+ "&employeeType=" + employeeType);
						log.info("macLink is : \n" + macURL.toString());						
						log.info("Sending email to Supervisor email: " + supervisorEmail);
						toAddress.add(supervisorEmail);
						if(ccEmail!=null){
							toAddress.add(ccEmail);
						}
						if (email.supervisorReminderEmail(toAddress.toArray(), employeeName,
									macURL, requestType, hireTransferTerminationDateStr)) {
							ecfModel.put("emailSupervisorSent", supervisorEmail);
							log.info("Sending an email to new supervisor SUCCESS");
						} else {
							ecfModel.put("emailSupervisorFailed", supervisorEmail);
							log.info("Sending an email to new supervisor FAILED");
						}						
						return new ModelAndView(getSuccessView(), ecfModel);
	
					} else { 
						/*
						 * oldEmployeetype is nonPMP type
						 * newEmployeeType is PMP type
						 */
						
						// Retrieve supervisor for the employee from PMP database
						if((supervisor = retrieveSupervisorFromDB(supervisorEmail))== null){
							ecfModel.put("empTypeQueryException", true);
							return new ModelAndView(getSuccessView(), ecfModel);
						}
						supervisorId = supervisor.getEmployeeId();
						log.info("supervisor Id : " + supervisor.getEmployeeId().toString());

						// Retrieve position of the employee from PMP database
						if((position = retrievePositionFromDB(title)) == null){
							ecfModel.put("positionQueryException", true);
							return new ModelAndView(getSuccessView(), ecfModel);
						}
						positionId = position.getPositionId();
						log.info("Position Id : " + position.getPositionId());
						
						/*
						 * Split employeeName into employeeFirstName and employeeLastName
						 * The first word is assigned to firstName and the rest is 
						 * assigned to lastName
						 */
						String [] empName = employeeName.split("\\s");
						StringBuilder empLastName = new StringBuilder();
						if(empName != null){
							employeeFirstName = empName[0];
							for(int i=1; i< empName.length;i++){
								empLastName.append(empName[i]);
							}							
						} 
						if (empLastName != null){
							employeeLastName = empLastName.toString();
						}
						
						employee.setFirstName(employeeFirstName);
						employee.setLastName(employeeLastName);
						employee.setEmail(employeeEmail);
						employee.setUsername(employeeUsername);
						employee.setPassword(empName[0].toLowerCase());
						employee.setHireDate(hireTransferTerminationDate);
						employee.setStatus(new Short((short) 1));
						// employee.setLocation(location)
						employee.setReview30day(new Short((short) 0));
						employee.setReview60day(new Short((short) 0));
						employee.setReview90day(new Short((short) 0));

						/*
						 * Update this field in the Employee table only for
						 * new hires ie requestType=1. For transfers update
						 * infoUpdated field for OrgChartTemp table. Cronjob
						 * will send emails depending on this field from
						 * Employee table and OrgChartTemp table. When the
						 * OrgChartTemp table field is updated in the
						 * OrgChart table on the Effective Date then the
						 * field infoUpdated is updated from OrgChartTemp
						 * table to infoUpdated of Employee table so that
						 * the recurring emails continue until mac form is
						 * submitted.
						 */
						employee.setInfoUpdated(new Short((short) 0));

						orgChart.setEmployee(employee);
						orgChart.setDepartment(department);
						orgChart.setPosition(position);
						orgChart.setSupervisor(supervisor);
						orgChart.setDepartment(department);

						// Update PMP Database
						log.info("Updating PMP Database ie saving employee and orgChart into db");
						employeeManager.saveEmployee(employee);
						employeeManager.saveOrgChart(orgChart);
						log.info("Updating PMP Database ie saving employee and orgChart into db SUCCESS");
						macURL.append(macHostAddress
								+ "/ecf/mac/updateEmployee.html?"
								+ "requestType=" + requestType
								+ "&employeeID=" + employeeUsername
								+ "&employeeName=" + employeeName							 
								+ "&employeeTitle=" + title.replace("&", "%26")
								+ "&employeeDepartment=" + departmentName.replace("&", "%26")
								+ "&supervisorID=" + supervisor.getUsername()
								+ "&supervisorName=" + supervisorName
								+ "&supervisorDepartment=" + departmentName.replace("&", "%26")
								+ "&effectiveDate=" + hireTransferTerminationDateStr
								+ "&employeeType=" + employeeType);
						log.info("macLink is : \n" + macURL.toString());

						toAddress.add(supervisorEmail);
						if(ccEmail!=null){
							toAddress.add(ccEmail);
						}
						log.info("Send email to new Supervisor and cc: " + supervisorEmail);						
						if (email.supervisorReminderEmail(toAddress.toArray(), employeeName, 
																macURL, requestType, 
																hireTransferTerminationDateStr)) {
							ecfModel.put("emailSupervisorSent", supervisorEmail);
							log.info("Sending an email to supervisor SUCCESS");
						} else {
							ecfModel.put("emailSupervisorFailed", supervisorEmail);
							log.info("Sending an email to supervisor FAILED");
						}
												
						return new ModelAndView(getSuccessView(), ecfModel);						
					}
															
				} else {
					/*
					 * (emp !=null) implies 
					 *  oldEmployeeType = PMP type
					 *  newEmployeeType = PMP type (this is assumed)
					 */
					
					employeeId = emp.getEmployeeId();
					
					//Retrieve oldSupervisor info
					OrgChart org = employeeManager.getOrgChartByEmployeeId(employeeId.toString());
					String oldSupervisorName = org.getSupervisor().getFirstName() + " " + org.getSupervisor().getLastName();
					String oldSupervisorEmail = org.getSupervisor().getEmail();					
					log.info("oldSupervisorName: "+ oldSupervisorName);
					log.info("oldSupervisorEmail: "+ oldSupervisorEmail);
					
					ecfModel.put("oldSupervisorName", oldSupervisorName);
					
					// Retrieve supervisor for the employee from PMP database
					if((supervisor = retrieveSupervisorFromDB(supervisorEmail))== null){
						ecfModel.put("empTypeQueryException", true);
						return new ModelAndView(getSuccessView(), ecfModel);
					}
					supervisorId = supervisor.getEmployeeId();
					log.info("supervisor Id : " + supervisor.getEmployeeId().toString());

					// Retrieve position of the employee from PMP database
					if((position = retrievePositionFromDB(title)) == null){
						ecfModel.put("positionQueryException", true);
						return new ModelAndView(getSuccessView(), ecfModel);
					}
					positionId = position.getPositionId();
					log.info("Position Id : " + position.getPositionId());
					
					
					log.info("Form values are :\n");
					log.info("employeeName : " + employeeName 
							+ "\n title : "	+ title 
							+ "\n employeeEmail : " + employeeEmail
							+ "\n supervisorName: " + supervisorName
							+ "\n supervisorUsername: " + supervisorUsername
							+ "\n departId: " + departId);								
	
					log.info("Setting new values for Transferred employee in OrgChart table");
					if (currentDate.compareTo(hireTransferTerminationDate) >= 0) {
						log.info("Effective Date for Transfer is Today or already Passed - ie "
										+ hireTransferTerminationDate
										+ "\nUpdating the employee "
										+ employeeName
										+ " in PMP Database\n");
	
						orgChart = employeeManager.getOrgChartByEmployeeId(employeeId.toString());
						orgChart.setDepartment(department);
						orgChart.setPosition(position);
						orgChart.setSupervisor(supervisor);
	
						// location is updated from MAC form
						emp.setTransferDate(hireTransferTerminationDate);
						emp.setReview30day(new Short((short) 0));
						emp.setReview60day(new Short((short) 0));
						emp.setReview90day(new Short((short) 0));
						emp.setInfoUpdated(new Short((short) 0));
						log.info("Saving in Employee and OrgChart  table");
						try {
							employeeManager.saveEmployee(emp);
							employeeManager.saveOrgChart(orgChart);
						} catch (DataAccessException dae) {						
							log.error("DataAccessException: Error saving emp and orgChart into db: "
									+ dae.getClass() + "  " + dae.getCause() + "  "
									+ dae.getMessage());
							log.error("StackTrace: "
									+ IOUtil.StackTraceToString(dae));
							ecfModel.put("dataAccessException", true);
							return new ModelAndView(getSuccessView(), ecfModel);
						}
					} else {
						// set new values for transferred employee in orgChartTemp
						orgChartTemp.setEmployee(emp);
						orgChartTemp.setDepartment(department);
	
						// All new titles to save in the Position table
						orgChartTemp.setPosition(position);
						orgChartTemp.setSupervisor(supervisor);
						orgChartTemp.setDepartment(department);
						orgChartTemp.setEffectiveDate(hireTransferTerminationDate);
						// setTransferOrTerminate to 1 for Transfer and 0 for
						// Terminate
						orgChartTemp.setTransferOrTerminate(new Short((short) 1));
	
						/*
						 * Field which is used by the cronjob to send recurring
						 * emails for supervisors to fill in the mac form
						 */
						orgChartTemp.setInfoUpdated(new Short((short) 0));
						log.info("Saving in orgChartTemp table");
						try {
							// update the orgChartTemp table in PMP Database
							employeeManager.saveOrgChartTemp(orgChartTemp);
							log.info("Success saving in orgChartTemp table");
						} catch (DataAccessException dae) {						
							log.error("DataAccessException: Error saving orgChartTemp into Database: "
									+ dae.getClass() + "  " + dae.getCause() + "  "
									+ dae.getMessage());
							log.error("StackTrace: " + IOUtil.StackTraceToString(dae));
							ecfModel.put("dataAccessException", true);
							return new ModelAndView(getSuccessView(), ecfModel);
						}
					}
	
					macURL.append(macHostAddress + "/ecf/mac/updateEmployee.html?"
							+ "requestType=" + requestType 
							+ "&employeeID=" + employeeUsername 
							+ "&employeeName=" + employeeName							 
							+ "&employeeTitle=" + title.replace("&", "%26") 
							+ "&employeeDepartment=" + departmentName.replace("&", "%26") 
							+ "&supervisorID=" + supervisor.getUsername()
							+ "&supervisorName=" + supervisorName
							+ "&supervisorDepartment=" + departmentName.replace("&", "%26")
							+ "&effectiveDate="	+ hireTransferTerminationDateStr
							+ "&employeeType=" + employeeType);
					log.info("macURL: " + macURL.toString() + "\n");
					toAddress.add(supervisorEmail);
					if(ccEmail!=null){
						toAddress.add(ccEmail);
					}
					// Send an email to New Supervisor
					log.info("Sending email to new supervisor: " + supervisorEmail);
					if (email.supervisorReminderEmail(toAddress.toArray(), employeeName, 
									macURL, requestType, hireTransferTerminationDateStr)) {
						ecfModel.put("emailSupervisorSent", supervisorEmail);
						log.info("Sending an email to Supervisor SUCCESS");
					} else {
						ecfModel.put("emailSupervisorFailed", supervisorEmail);
						log.info("Sending an email to Supervisor FAILED");
					}
					//Send an email to Old Supervisor
					log.info("Sending email to old supervisor: " + oldSupervisorEmail);
					if (email.oldSupervisorNotificationEmail(oldSupervisorName, oldSupervisorEmail, 
							employeeName, departmentName, hireTransferTerminationDateStr)) {
						ecfModel.put("emailOldSupervisorSent", oldSupervisorEmail);
						log.info("Sending an email to Old Supervisor SUCCESS");
					} else {
						ecfModel.put("emailOldSupervisorFailed", oldSupervisorEmail);
						log.info("Sending an email to Old Supervisor FAILED");
					}
					ecfModel.put("departmentName", departmentName);				
					return new ModelAndView(getSuccessView(), ecfModel);
				}//end of else part of if(emp == null) 

			} catch (DataAccessException dae) {
				log.error("DataAccessException: Error retrieving data from db: "
						+ dae.getClass() + "  " + dae.getCause() + "  "
						+ dae.getMessage());
				log.error("StackTrace: " + IOUtil.StackTraceToString(dae));
				ecfModel.put("dataAccessException", true);
				return new ModelAndView(getSuccessView(), ecfModel);
			} catch (Exception e) {
				log.error("Exception : " + e.getClass() + "  " + e.getCause()
						+ "  " + e.getMessage());
				log.error("StackTrace: " + IOUtil.StackTraceToString(e));
				ecfModel.put("exception", true);
				return new ModelAndView(getSuccessView(), ecfModel);
			}
		}
		/*
		 * Manual LDAP Account Creation (requestType = 0) New Hire (requestType= 1)
		 * Transfer (requestType = 2) Termination (requestType = 3)
		 */
		else if (requestType.equals("3")) {
			Map<String, Object> ecfModel = new HashMap<String, Object>();
			ecfModel.put("requestType", requestType);
			ecfModel.put("effectiveDate", hireTransferTerminationDateStr);
			String comments = (String) request.getParameter("comments")+"";
			ecfModel.put("comments", comments);
			
			log.info("Inside if requestType = 3");
			employeeName = (String) request.getParameter("employee");
			employeeUsername = request.getParameter("eUsername");
			employeeEmail = employeeUsername + franklinEmailTail;
			String employeeBuildingName = null;
			String employeeRoomNumberName = null;

			Boolean existsInDB = false;
			log.info("employeeName: " + employeeName);
			log.info("employeeUsername: " + employeeUsername);
			log.info("employeeEmail: " + employeeEmail);

			ecfModel.put("employeeName", employeeName);
			/*
			 * When employeeType={Staff, Faculty, Student}->{Create LDAP
			 * Account, Access PMP and Send SupervisorEmail} When
			 * employeeType={Adjunct Faculty, Tutor, Work-Study, Other}->{Create
			 * LDAP Account}
			 */

			/*
			 * int i = 1; if(i==1){ return new ModelAndView(getSuccessView()); }
			 */

			log.info("Form values are :\n");
			log.info("employeeName: " + employeeName 
					+ "\n employeeUsername: " + employeeUsername 
					+ "\n employeeEmail: " + employeeEmail);
			try {
				if (employeeEmail != null) {
					log.info("Start getEmployeeByEmail \n");
					try {
						employee = employeeManager.getEmployeeByEmail(employeeEmail);
						// As the employee is in DB make the variable existsInDB
						// as true
						existsInDB = true;
						log.info("getEmployeeByEmail Success\n");
					} catch (IndexOutOfBoundsException e) {
						log.info("Employee Name: " + employeeName
								+ " with employeeEmail: " + employeeEmail
								+ " not found in pmp database");
						// As the employee is NOT in db make the variable existsInDB=false
						existsInDB = false;
					}
				} else {
					log.info("employeeEmail is null");
					ecfModel.put("exception", true);
					return new ModelAndView(getSuccessView(), ecfModel);
				}
				if (existsInDB == true) {
					log.info("existsInDB = " + existsInDB);
					ecfModel.put("existsInDB", existsInDB);
					employeeId = employee.getEmployeeId();
					if (employeeId != null) {
						log.info("Start getOrgChartByEmployeeId \n");
						orgChart = employeeManager.getOrgChartByEmployeeId(employeeId.toString());
						log.info("getOrgChartByEmployeeId Success\n");
					}
					supervisor = orgChart.getSupervisor();
					supervisorName = supervisor.getFirstName() + " "
							+ supervisor.getLastName();
					supervisorEmail = orgChart.getSupervisor().getEmail();
					position = orgChart.getPosition();
					department = orgChart.getDepartment();

					try {
						employeeBuildingName = employee.getLocation().getBuilding().getName();
						employeeRoomNumberName = employee.getLocation().getRoom().getName();
					} catch (Exception e) {
						log.error("Exception: Error while retrieving Employee Building and Employee Room "
										+ e.getMessage() + e.getCause() + e.getClass());
					}
					log.info("employeeName: "+ employeeName
							+ "\n employeeUsername: " + employeeUsername
							+ "\n supervisorName: " + supervisorName
							+ "\n supervisorEmail: " + supervisorEmail
							+ "\n employeeBuildingName: " + employeeBuildingName
							+ "\n employeeRoomNumberName: " + employeeRoomNumberName);
					log.info("Setting new values for Terminating employee in OrgChartTemp table \n");

					hireTransferTerminationDateStr = dateConvertObj.formatDateToString(hireTransferTerminationDate);

					if (currentDate.compareTo(hireTransferTerminationDate) >= 0) {
						log.info("Effective Date for Termination is Today or already Passed- ie "
										+ hireTransferTerminationDate
										+ "\nUpdating the employee "
										+ employeeName + " in PMP Database\n");
						// You cannot do the following two lines
						// orgChart.getEmployee().setTerminationDate(hireTransferTerminationDate);
						// orgChart.getEmployee().setStatus(new Short((short)
						// 2));
						try{
							employee.setTerminationDate(hireTransferTerminationDate);
							employee.setStatus(new Short((short) 2));
							employee.setInfoUpdated(null);
							employeeManager.saveEmployee(employee);
							Integer orgChartId = orgChart.getOrgChartId();
							employeeManager.removeEmployeeFromOrgChart(orgChartId);						
							log.info("Employee status updated to 2 in Employee table and is deleted from OrgChart table");
						} catch(DataAccessException dae){
							log.error("DataAccessException while saving employee's termination info in PMP DB" + dae.getMessage());
						} catch(Exception e){
							log.error("Exception while saving employee's termination info in PMP DB" + e.getMessage());
						}
						/* Checks for employee inside password_reset database and 
						 * removes the employee's secret questions if found.
						 */
						try{
							passwordResetManager.removeEmployeesFromResetQuestions(employeeUsername);
							log.info(employeeUsername + "'s reset questions removed from PasswordReset DB");
						}catch(DataAccessException dae){
							log.error("Data Access Exception while removing employee's reset questions from " +
										"PasswordReset DB" + dae.getMessage());
						}catch(Exception e){
							log.error("Error while removing employee's reset questions from PasswordReset DB");
						}
							
						
					} else {
						// set new values for terminated employee in
						// orgChartTemp table
						orgChartTemp.setEmployee(employee);
						orgChartTemp.setSupervisor(supervisor);
						orgChartTemp.setPosition(position);
						orgChartTemp.setDepartment(department);
						orgChartTemp.setEffectiveDate(hireTransferTerminationDate);
						// setTransferOrTerminate to 1 for Transfer and 2 for
						// Terminate
						orgChartTemp.setTransferOrTerminate(new Short((short) 2));

						// save into the orgChartTemp table in PMP Database
						employeeManager.saveOrgChartTemp(orgChartTemp);
					}										
					
					toAddress.add(supervisorEmail);
					if(ccEmail!=null){
						toAddress.add(ccEmail);
					}
					log.info("Sending email to supervisor email: " + supervisorEmail);
					// Send an email to Supervisor
					if (email.supervisorReminderEmail(toAddress.toArray(), employeeName,
															null, requestType,
															hireTransferTerminationDateStr)) {
						ecfModel.put("emailSupervisorSent", supervisorEmail);
						log.info("Sending an email to supervisor " + supervisorEmail + " : SUCCESS");
					} else {
						ecfModel.put("emailSupervisorFailed", supervisorEmail);
						log.info("Sending an email to supervisor " + supervisorEmail + " : FAILED");

					}
					log.info("Sending email to helpdesk : "
							+ helpdeskTerminateQueue + "\n");
					// Create Termination HelpDesk Tickets
					subject = helpdeskTerminateQueue;
					if (email.generateTerminationHelpDeskEmail(subject,	employeeName, employeeUsername,
												employeeBuildingName, employeeRoomNumberName, 
												hireTransferTerminationDateStr, comments)) {
						emailServiceDeskSent.add(subject);
						log.info("Sending an email to helpdeskTerminateQueue SUCCESS");
					} else {
						emailServiceDeskFailed.add(subject);
						log.info("Sending an email to helpdeskTerminateQueue FAILED");
					}
					/*
					  No need of all the individual helpdesk tickets
					
						log.info("Sending email to helpdesk : "
								+ helpdeskFacilitiesQueue + "\n");
						subject = helpdeskFacilitiesQueue;
						if (email.generateTerminationHelpDeskEmail(subject, employeeName, employeeUsername,
																	employeeBuildingName, employeeRoomNumberName,
																		hireTransferTerminationDateStr, comments)) {
							emailServiceDeskSent.add(subject);
							log.info("Sending an email to helpdeskFacilitiesQueue SUCCESS");
						} else {
							emailServiceDeskFailed.add(subject);
							log.info("Sending an email to helpdeskFacilitiesQueue FAILED");
						}
						log.info("Sending email to helpdesk : "	+ helpdeskLinuxQueue + "\n");
						subject = helpdeskLinuxQueue;
						if (email.generateTerminationHelpDeskEmail(subject, employeeName, employeeUsername, "", "",
																	hireTransferTerminationDateStr, comments)) {
							emailServiceDeskSent.add(subject);
							log.info("Sending an email to helpdeskLinuxQueue SUCCESS");
						} else {
							emailServiceDeskFailed.add(subject);
							log.info("Sending an email to helpdeskLinuxQueue FAILED");
						}
						log.info("Sending email to helpdesk : " + helpdeskUISQueue
								+ "\n");
						subject = helpdeskUISQueue;
						if (email.generateTerminationHelpDeskEmail(subject,
								employeeName, employeeUsername, "", "",
								hireTransferTerminationDateStr, comments)) {
							emailServiceDeskSent.add(subject);
							log.info("Sending an email to helpdeskUISQueue SUCCESS");
						} else {
							emailServiceDeskFailed.add(subject);
							log.info("Sending an email to helpdeskUISQueue FAILED");
						}
	
						log.info("Sending email to helpdesk : " + helpdeskPhoneQueue + "\n");
						subject = helpdeskPhoneQueue;
						if (email.generateTerminationHelpDeskEmail(subject, employeeName, employeeUsername, "", "",
																		hireTransferTerminationDateStr, comments)) {
							emailServiceDeskSent.add(subject);
							log.info("Sending an email to helpdeskPhoneQueue SUCCESS");
						} else {
							emailServiceDeskFailed.add(subject);
							log.info("Sending an email to helpdeskPhoneQueue FAILED");
						}
					*/
					if (emailServiceDeskSent != null){
						ecfModel.put("emailServiceDeskSent", emailServiceDeskSent);
					}
					if (emailServiceDeskFailed != null){
						ecfModel.put("emailServiceDeskFailed", emailServiceDeskFailed);
					}		
					ecfModel.put("supervisorName", supervisorName);			
					return new ModelAndView(getSuccessView(), ecfModel);
				} else {
					/*
					 * existsInDB = false. Send helpdesk email. 
					 * Terminate queue.
					 */

					log.info("existsInDB = " + existsInDB);
					ecfModel.put("existsInDB", existsInDB);
					
					if (currentDate.compareTo(hireTransferTerminationDate) >= 0) {
						log.info("Effective Date for Termination is Today or already Passed- ie "
										+ hireTransferTerminationDate 
										+ "\nRemoving "	+ employeeName + "'s reset questions" 
										+ " from PasswordReset Database\n");
						
						/* Checks for employee inside password_reset database and 
						 * removes the employee's secret questions if found.
						 */
						
						try{
							passwordResetManager.removeEmployeesFromResetQuestions(employeeUsername);
							log.info(employeeName + "'s reset questions deleted from PasswordReset DB");	
						} catch(DataAccessException dae){
							log.error("Data Access Exception while removing employee's reset questions from " +
										"PasswordReset DB" + dae.getMessage());
						} catch(Exception e){
							log.error("Exception while removing employee's reset questions from PasswordReset DB");
						}						
						
					} else {
						
						try{
							// save into the PasswordResetTemp table in PMP Database
							//PasswordResetTemp passwordResetTemp = employeeManager.getPasswordResetTempByUsername(employeeUsername);
							PasswordResetTemp passwordResetTemp = new PasswordResetTemp();
							passwordResetTemp.setUsername(employeeUsername);
							passwordResetTemp.setEffectiveDate(hireTransferTerminationDate);
							employeeManager.savePasswordResetTemp(passwordResetTemp);
							log.info(employeeUsername + "'s info saved into PasswordResetTemp table of PMP DB");
						} catch(DataAccessException dae){
							log.error("Data Access Exception while saving employee's info " +
										"in PasswordResetTemp table of PMP DB: " + dae.getMessage());
						} catch(Exception e){
							log.error("Exception while saving employee into PasswordResetTemp table of PMP DB");
						}
					}										
															
					
					/* Checks for employee inside password_reset database and 
					 * removes the employee's secret questions if found.
					 */
					//passwordResetManager.removeEmployeesFromResetQuestions(employeeUsername);	
					
					log.info("Sending email to helpdesk : "	+ helpdeskTerminateQueue + "\n");
					// Create Termination HelpDesk Tickets
					subject = helpdeskTerminateQueue;
					if (email.generateTerminationHelpDeskEmail(subject, employeeName, employeeUsername,
								employeeBuildingName, employeeRoomNumberName, 
								hireTransferTerminationDateStr, comments)) {
						emailServiceDeskSent.add(subject);
						log.info("Sending an email to helpdeskTerminateQueue SUCCESS");
					} else {
						emailServiceDeskFailed.add(subject);
						log.info("Sending an email to helpdeskTerminateQueue FAILED");
					}
					/*
					  No need of all the individual helpdesk tickets
					
						log.info("Sending email to helpdesk : "
								+ helpdeskLinuxQueue + "\n");
						subject = helpdeskLinuxQueue;
						if (email.generateTerminationHelpDeskEmail(subject, employeeName, 
										employeeUsername, "", "",
										hireTransferTerminationDateStr, comments)) {
							emailServiceDeskSent.add(subject);
							log.info("Sending an email to helpdeskLinuxQueue SUCCESS");
						} else {
							emailServiceDeskFailed.add(subject);
							log.info("Sending an email to helpdeskLinuxQueue FAILED");
						}
					*/
					if (emailServiceDeskSent != null){
						ecfModel.put("emailServiceDeskSent", emailServiceDeskSent);
					}
					if (emailServiceDeskFailed != null){
						ecfModel.put("emailServiceDeskFailed", emailServiceDeskFailed);
					}
					return new ModelAndView(getSuccessView(), ecfModel);

				}
			} catch (DataAccessException dae) {
				log.error("DataAccessException: " + dae.getClass() + "  "
						+ dae.getCause() + "  " + dae.getMessage());
				log.error("StackTrace: " + IOUtil.StackTraceToString(dae));
				ecfModel.put("dataAccessException", true);
				return new ModelAndView(getSuccessView(), ecfModel);
			} catch (Exception e) {
				log.error("Exception: " + e.getClass() + "  " + e.getCause()
						+ "  " + e.getMessage());
				log.error("StackTrace: " + IOUtil.StackTraceToString(e));
				ecfModel.put("exception", true);
				return new ModelAndView(getSuccessView(), ecfModel);
			}
		}

		return new ModelAndView(getSuccessView());
	}
	
	/*
	 *  Check whether an employee exists with the same email in the Employee table
	 *  If exists look whether that employee is a terminated employee.  If yes append that
	 *	employee's email with old_ and then save this new Employee in the db, else if 
	 *  not a terminated employee return error page
	 */
	private Employee checkExistsEmployeeInDB(String employeeEmail, String employeeUsername) {

		Employee existsEmployee = null;
		
		try {
			existsEmployee = employeeManager.getEmployeeByEmail(employeeEmail);
			log.info("getEmployeeByEmail Success");
			log.info("The New employeeEmail: " + employeeEmail
						+ " already exists in the pmp db." 
						+ "\n Looking whether this employee is a re-hiree..");
			if (existsEmployee.getTerminationDate() != null) {
				log.info("Employee is a re-hiree so appending old_ to his previous email in the db");
				existsEmployee.setEmail("old_" + employeeEmail);
				existsEmployee.setUsername("old_" + employeeUsername);
				employeeManager.saveEmployee(existsEmployee);
				log.info("New employee's old info is successfully saved into db as old_");
				
				/*return null because there doesn't exist an 
				active employee within DB with the employeeEmail */
				return null;
			} else {
				log.info("Employee is not a re-hiree, there exists an active employee "
						+ "with same email " + employeeEmail 
						+ " in the db, thus returning error");
				 
				return existsEmployee;
			}
		} catch (IndexOutOfBoundsException iob) {								
			log.info("New employeeEmail: "
						+ employeeEmail
						+ " does not exists in the pmp database.  "
						+ "So its good to save into the db");
			
			/*return null because there doesn't exist an 
			active employee within DB with the employeeEmail */
			return null;
		}
	}

	private String ldapAccountCreationMsg(String employeeFirstName,
			String employeeLastName, String type) {
		String encoded = "action=createuser" + "&firstname="
		+ URLEncoder.encode(employeeFirstName) + "&lastname="
		+ URLEncoder.encode(employeeLastName) + "&type="
		+ URLEncoder.encode(type);
		URL CGIurl = null;
		String msg = "";
		try {
			CGIurl = new URL(ldapCGI + encoded);
			log.info("CGIurl: " + CGIurl + "\n");
		} catch (MalformedURLException e) {
			log.error("MalformedURLException: " + e.getClass() + "  "
					+ e.getCause() + "  " + e.getMessage());
		}
		try {
			URLConnection c = CGIurl.openConnection();
			c.setDoOutput(true);
			c.setUseCaches(false);
			c.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			DataOutputStream out = new DataOutputStream(c.getOutputStream());
			out.writeBytes(encoded);
			log.info("CGIurl Connection Output: " + out.toString());
			out.flush();
			out.close();
		
			BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
		
			String aLine;
			while ((aLine = in.readLine()) != null) {
				// data from the CGI
				msg = msg + aLine + " ";
				// log.info(aLine);
			}
			log.info("LDAP msg from CGI output inside try block: " + msg);
		} catch (IOException ioe) {
			log.error("IOException while Connecting to LDAP server to create user: "
						+ ioe.getClass()
						+ "  "
						+ ioe.getCause()
						+ "  " + ioe.getMessage());
			log.error("StackTrace: " + IOUtil.StackTraceToString(ioe));
		} catch (Exception e) {
			log.error("Exception while Connecting to LDAP server to create user:  "
							+ e.getClass()
							+ "  "
							+ e.getCause()
							+ "  "
							+ e.getMessage());
			log.error("StackTrace: " + IOUtil.StackTraceToString(e));
		}
		return msg;
		
	}

	private Position retrievePositionFromDB(String title) {
		Position position = null;
		if (title != null) {
			try {
				log.info("Start getPositionByTitle");
				position = employeeManager.getPositionByTitle(title);
				log.info("getPositionByTitle Success");
			} catch (IndexOutOfBoundsException iob) {
				log.info("New Title not found in the database, so creating a new position: "
								+ title + "  in the Position table");
				Position pos = new Position();
				pos.setTitle(title);
				employeeManager.saveNewPosition(pos);
				log.info("New position is successfully saved in the database \n");
				log.info("Start getPositionByTitle \n");
				/*
				 * If the position title doesn't exist in db, then
				 * the catch block above creates a new position. So
				 * retrieve the position again to get the new
				 * positionId.
				 */
				try {
					position = employeeManager.getPositionByTitle(title);
					log.info("getPositionByTitle Success \n");
				} catch (IndexOutOfBoundsException iob2) {								
					log.info("Unable to retrieve new position: "
							+ title + "  from the Position table");
					return null;
				}
			}
		}
		return position;
	}

	private Employee retrieveSupervisorFromDB(String supervisorEmail) {
		Employee supervisor = null;
		if (supervisorEmail != null) {
			log.info("Start getEmployeeByEmail");
			try {
				supervisor = employeeManager.getEmployeeByEmail(supervisorEmail);
				log.info("getEmployeeByEmail Success");
			} catch (IndexOutOfBoundsException iob) {
				log.info("Supervisor Email: " + supervisorEmail
						+ " not found in the pmp database");
				return null;
			}
		}
		return supervisor;
	}

	private Department retrieveDepartmentFromDB(Integer departId) {
		Department department = null;
		
		if (departId != null) {
			log.info("Start getDepartmentByDepartId");
			department = employeeManager.getDepartmentByDepartId(departId);
			log.info("getDepartmentByDepartId Success");
		}
		return department;
	}
}

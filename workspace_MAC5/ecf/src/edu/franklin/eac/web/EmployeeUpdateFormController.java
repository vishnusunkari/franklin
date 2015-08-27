package edu.franklin.eac.web;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.RequestUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import edu.franklin.eac.ldap.LDAPClient;
import edu.franklin.eac.model.pmp.Building;
import edu.franklin.eac.model.pmp.Employee;
import edu.franklin.eac.model.pmp.Location;
import edu.franklin.eac.model.pmp.OrgChartTemp;
import edu.franklin.eac.model.pmp.Room;
import edu.franklin.eac.service.EmployeeManager;
import edu.franklin.eac.util.BuildEmail;
import edu.franklin.eac.util.DateConversion;
import edu.franklin.eac.util.IOUtil;


@SuppressWarnings("deprecation")
public class EmployeeUpdateFormController extends SimpleFormController {
    private static Log log = LogFactory.getLog(EmployeeUpdateFormController.class);
    static String appResourceFilename ="application";
	static ResourceBundle appResource = ResourceBundle.getBundle(appResourceFilename);
	//private String [] helpdeskEmail  =  appResource.getString("email.helpdesk").split("[\\s,;]+");
	private String [] macNewHireEmail  = appResource.getString("email.macnewhire").split("[\\s,;]+");	
	private String [] nameplateEmail  = appResource.getString("email.nameplate").split("[\\s,;]+");
	private String [] securityEmail  = appResource.getString("email.security").split("[\\s,;]+");
	private static String helpdeskNewHireQueue = appResource.getString("email.helpdesk.newhire.queue");
	private static String nameplateQueue = appResource.getString("email.nameplate.queue");
	private static String securityQueue = appResource.getString("email.security.queue");	
	/*private static String helpdeskGeneralQueue = appResource.getString("email.helpdesk.general.queue");	
	private static String helpdeskFacilitiesQueue = appResource.getString("email.helpdesk.facilities.queue");
	private static String helpdeskWindowsQueue = appResource.getString("email.helpdesk.windows.queue");
	private static String helpdeskLinuxQueue = appResource.getString("email.helpdesk.linux.queue");
	private static String helpdeskUISQueue = appResource.getString("email.helpdesk.uis.queue");
	private static String helpdeskPhoneQueue = appResource.getString("email.helpdesk.phone.queue");	
	*/

	private static String franklinEmailTail = appResource.getString("franklin.email.tail");	

    private EmployeeManager employeeManager = null;
        
    public void setEmployeeManager(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    public EmployeeUpdateFormController() {
        super();
        setCommandClass(Building.class);        
    }
	
    //Method that executes before getting to Jsp
    @SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request) throws Exception {
    	log.info("Method that executes before getting to Jsp");
    	
    	log.info("Start updating local.js in all the 3 places");
    	//Update local.js by calling LDAPClient Thread    	
    	Thread LDAPThread = new Thread(new LDAPClient());
    	LDAPThread.start();
    	    	    
    	log.info("Getting all the building and their corresponding rooms information to fill in the form");
    	Map reference = new HashMap(); 
    	List  buildingList = null;
    	Building buildingObj = null;
    	List locationsList = null;

    	Integer buildingId = null;
    	String buildingName = null;
    	String roomId = null;
    	String roomName = null;				    	
    	Iterator locationsListItr;
    	Location locationObj = null;
    	Room roomObj = null;
    	int i=0,j=0,k=0;
    	
    	try {
    		log.info("Start getAllBuildings");
    		buildingList  =  employeeManager.getAllBuildings();
    		log.info("getAllBuildings Success");
    	} catch(DataAccessException dae){
    		log.error("     DataAccessException: "+ dae.getStackTrace().toString());
    	}
    	int buildingListSize = buildingList.size();
    	Iterator buildingListItr = buildingList.iterator();    	
    	/*
    	 * Create 2-dimensional Array for holding buidlingIds and corresponding roomIds
    	 * Create 2-dimensional Array for holding buildingNames and corresponding roomNames
    	 * Assuming max no. of rooms per building as 200
    	*/ 
    	String [][] buildingRoomIdsArray =  new String [buildingListSize][200];
    	String [][] buildingRoomNamesArray = new String [buildingListSize][200];
    	//Array holds the List of no. of rooms per building
    	int [] numOfRoomsPerBuilding = new int [buildingListSize];
    	log.info("Start of while loop for getAllRoomsFromLocationByBuildingId for each building");
    	while(buildingListItr.hasNext()){
    		j=0;
        	buildingObj =  (Building) buildingListItr.next();        	
        	//log.info("Building Id: " + buildingObj.getBuildingId());
        	//log.info("Building Name: " + buildingObj.getName());				       
        	buildingId = buildingObj.getBuildingId();
        	buildingName = buildingObj.getName();							        	
        	//Insert buildingIds in Array's (i,0) position
        	buildingRoomIdsArray[i][j] = (String) buildingId.toString();
        	buildingRoomNamesArray[i][j] = buildingName;
        	j++;
        	try{
        		//log.info("Start getAllRoomsFromLocationByBuildingId");
        		locationsList = employeeManager.getAllRoomsFromLocationByBuildingId(buildingId);
        		//log.info("getAllRoomsFromLocationByBuildingId Success");
        	}catch(DataAccessException dae){
        		log.error("     DataAccessException: "+ dae.getStackTrace().toString());
        	}
        	numOfRoomsPerBuilding[k] = locationsList.size();
        	k++;
        	locationsListItr = locationsList.iterator();	
	        while(locationsListItr.hasNext()){
	        	locationObj = (Location) locationsListItr.next();
	        	roomObj = (Room) locationObj.getRoom();
	        	roomId = roomObj.getRoomId().toString();
	        	roomName = roomObj.getName();
	           	//log.info("Building: " + buildingObj.getName() + "; Room: " + roomObj.getName());
	           	//Insert RoomIds in Array's (i, j) position
	           	buildingRoomIdsArray[i][j] = roomId;
	           	buildingRoomNamesArray[i][j] = roomName;
	           	//log.info("buildingRoomIdsArray[" + i + "][" + j + "]: " + buildingRoomIdsArray[i][j]);
	           	//log.info("buildingRoomNamesArray[" + i + "][" + j + "]: " + buildingRoomNamesArray[i][j] + "\n\n");
	           	j++;
	        }
	        i++;						
	    }	
    	log.info("End of while loop for getAllRoomsFromLocationByBuildingId for each building");    	
    	//reference.put("employeeManager", employeeManager);
    	request.setAttribute("buildingRoomIdsArray", buildingRoomIdsArray);
    	request.setAttribute("buildingRoomNamesArray", buildingRoomNamesArray);
    	request.setAttribute("numOfRoomsPerBuilding", numOfRoomsPerBuilding);
    	
    	String employeeUsername = (String) request.getParameter("employeeUsername");    	
    	log.info("employeeUsername: " + employeeUsername);
    	String supervisorUsername = (String) request.getParameter("supervisorUsername");  
    	log.info("supervisorUsername: " + supervisorUsername);	
    	/*
    	 * The following code is needed when you want to 
    	 * read data from javascript file local.js
    	 * Right now index.js of mac form handles.  So there is no
    	 * need of the following code.  Hence it is commented out.  
    	 */
    	/*if(employeeUsername != null){
	    	log.info("Inside if null condition ");
		   	Get employee data from local.js using JSON object
		   	FileReader fr = new FileReader("C:/Documents and Settings/sunkariv/workspace_MAC2/ECF/WebContent/mac/js/autocomplete/local.js");
		   	BufferedReader br = new BufferedReader(fr);
		   	String line;
		   	StringBuffer result = new StringBuffer();
		   	JSONTokener json = null;
		   	int numOfLines = 0;
		   	JSONObject jsonObject = new JSONObject();
		   	try{
		    	while ((line = br.readLine()) != null) {
		    		numOfLines++;
		    		if(numOfLines>0){
		    			json = new JSONTokener(br.readLine());
			    		jsonObject = (JSONObject) json.nextValue();
						if (jsonObject.get("u").equals(employeeUsername)){
				    		log.info("Employee Details: ");
				    		log.info("n: "+ jsonObject.get("n"));
				    		log.info("t: "+ jsonObject.get("t"));
				    		log.info("d: "+ jsonObject.get("d"));
				    		log.info("s: "+ jsonObject.get("s"));
				    		log.info("b: "+ jsonObject.get("b"));
				    		log.info("r: "+ jsonObject.get("r"));
				    		log.info("e: "+ jsonObject.get("e"));
				    		log.info("u: "+ jsonObject.get("u"));		    							    					
				   		}		    				
				    	if (jsonObject.get("u").equals(supervisorUsername)){
				    		log.info("Supervisor Details: ");
				    		log.info("n: "+ jsonObject.get("n"));
				    		log.info("t: "+ jsonObject.get("t"));
				    		log.info("d: "+ jsonObject.get("d"));
				    		log.info("s: "+ jsonObject.get("s"));
				    		log.info("b: "+ jsonObject.get("b"));
				    		log.info("r: "+ jsonObject.get("r"));
				    		log.info("e: "+ jsonObject.get("e"));
				    		log.info("u: "+ jsonObject.get("u"));		    							    					
				    	}
			    	}
		    		result.append(line);  		
		    	}
		    }catch(Exception e){
    			e.getMessage();
	    	}
		}*/
    	return reference;
    }
    
    protected Object formBackingObject(HttpServletRequest request)
    throws ServletException {
        String id = RequestUtils.getStringParameter(request, "id", "");        
        Building command = new Building();        

        if (!"".equals(id)) {
            command = (Building) employeeManager.getAllBuildings().get(0);                       
        }
        return command;
    }
    
    protected void initBinder(HttpServletRequest request,
            ServletRequestDataBinder binder) {
		// convert java.lang.Long
		NumberFormat nf = NumberFormat.getNumberInstance();
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, nf, true));
		//binder.registerCustomEditor(Employee.class, "employee", new EmployeeEditor());
	}
    
    /*
     * Redirect to the successView when the cancel button has been pressed.
     */
    /*public ModelAndView processFormSubmission(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Object command,
                                              BindException errors)
    	throws Exception {
        	if (request.getParameter("cancel") != null) {
        		return new ModelAndView(getSuccessView());
        	}

        	return super.processFormSubmission(request, response, command, errors);
    }*/

	@SuppressWarnings("unchecked")
    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
     throws Exception {

	    	if (log.isDebugEnabled()) {
	            log.debug("entering 'onSubmit' method...");
	        }        
	    	
	    	//model data
	    	Map macModel = new HashMap();
	        
			log.info("Start Form onSubmit method");
			// Just to verify the email addresses from properties file
	        for(int i=0; i<macNewHireEmail.length; i++){
	        	log.info("macNewHireEmails are: " + macNewHireEmail[i]);
	        }
	        for(int i=0; i<nameplateEmail.length; i++){
	        	log.info("nameplateEmails are: " + nameplateEmail[i]);
	        }
	        for(int i=0; i<securityEmail.length; i++){
	        	log.info("securityEmails are: " + securityEmail[i]);
	        }
	    	
	        //Employee's Information
	        String requestType = request.getParameter("requestType");
	        log.info("requestType=" + requestType);
	        String employeeType = request.getParameter("employeeType");
	        log.info("employeeType is: " + employeeType);
	        String employeeUsername = request.getParameter("employeeID"); 
	        String employeeEmail = employeeUsername + "@franklin.edu";
	        log.info("employeeEmail: " + employeeEmail);        
	        String employeeName = request.getParameter("employeeName");      
	        String employeeTitle = request.getParameter("employeeTitle");   
	        String employeeDepartment = request.getParameter("employeeDepartment");         
	        String effectiveDateStr = request.getParameter("effectiveDate");
	
	        //Employee's Current BuildingName and RoomName Information 
	        String currentEmployeeBuilding = request.getParameter("currentEmployeeBuilding");
	        String currentEmployeeRoomNumber = request.getParameter("currentEmployeeRoomNumber");
	        log.info("currentEmployeeBuilding: " + currentEmployeeBuilding);
	        log.info("currentEmployeeRoomNumber: " + currentEmployeeRoomNumber);
	        
	        //Employee's Modified BuildingName and RoomName Information 
	        String changedEmployeeBuilding = request.getParameter("changedEmployeeBuilding");
	        String changedEmployeeRoomNumber = request.getParameter("changedEmployeeRoomNumber");
	        log.info("changedEmployeeBuilding: " + changedEmployeeBuilding);
	        log.info("changedEmployeeRoomNumber: " + changedEmployeeRoomNumber);
	        
	        String employeePhone = request.getParameter("employeePhone");                 
	
	        //Supervisor's Information
	        String supervisorUsername = request.getParameter("supervisorID");  
	        String supervisorName = request.getParameter("supervisorName");         
	        String supervisorTitle = request.getParameter("supervisorTitle");         
	        String supervisorDepartment = request.getParameter("supervisorDepartment");         
	        String supervisorBuilding = request.getParameter("supervisorBuilding");         
	        String supervisorRoomNumber = request.getParameter("supervisorRoomNumber");         
	        String supervisorPhone = request.getParameter("supervisorPhone");         
	        String supervisorEmail = supervisorUsername + franklinEmailTail;
	        
			//No need of request.getSession attributes
	        //request.getSession().setAttribute(arg0, arg1);
			
	        //model data
			macModel.put("employeeType", employeeType);
			/*macModel.put("updateDatabase", false);
			macModel.put("macFormAlreadySubmitted", false);
			macModel.put("dataAccessException", null);
			macModel.put("exception", null);
			macModel.put("emailServiceDeskSent", null);
			macModel.put("emailServiceDeskFailed", null);
			*/
			
			
			//List of emails successfully sent
			List emailServiceDeskSent = new ArrayList();
			//List of emails Failed
			List emailServiceDeskFailed = new ArrayList();
	        /*
	         * The database is updated for only employeeType=staff or faculty or student
	         * employeeType=default means employeeType is either staff || faculty || student
	         * This is set by the MACEmailReminder as it has no information of employeeType 
	         * when generating reminder emails as employeeType is not stored anywhere in 
	         * database.  
	         */ 
	    	try{
	    		if (employeeType == null || employeeType.isEmpty()) {
	    			log.info("employeeType is null or empty");
		        	macModel.put("employeeTypeNull", true);
 			        return new ModelAndView(getSuccessView(), macModel);
		        } else if (employeeType.equals("staff")
		        				|| employeeType.equals("faculty")
		        				|| employeeType.equals("student")
		        				|| employeeType.equals("default")) {
		        	
		        	log.info("Inside if employeeType=staff, faculty, student");
		        	//Update Database
		        	macModel.put("updateDatabase", true);
		        
			        /*
			         ****Start**** To Update Employee Location in PMP Database, get the buildingId and roomId 
			         *and then get the 
			         */        		
			        
			        //Set Employee's Location
			        Location employeeLocation = null;
			        if(!currentEmployeeBuilding.equals(changedEmployeeBuilding) 
			        		|| !currentEmployeeRoomNumber.equals(changedEmployeeRoomNumber)){
				        /*
				         * Get Employee buildingId.  The option values of employeeBuilding
				         * selectbox are the primary keys of LocationBuilding database table                 
				         */
			        				        	
				        String employeeBuilding = request.getParameter("employeeBuilding");
				        Integer buildingId = null;
				        if(employeeBuilding != null){
				        	buildingId = Integer.parseInt(request.getParameter("employeeBuilding"));				        	
				        }
				        log.info("buildingId: " + buildingId);             
				        /*
				         * Get Employee roomId  
				         * First get the selectedIndex of employeeBuilding which is 
				         * appended to the employeeRoomNumber selectboxes
				         * 
				         * Note:  buildingSelectedIndex is not always equal to buildingId,
				         * buildingSelectedIndex is the hidden value grabbed from javascript
				         * which is assigned only when the building is changed otherwise it is null.
				         * So within this if condition, buildingSelectedIndex is guaranteed to have a value.
				         * buildingId is the option value of employeeBuilding selectbox
				         * which is also the primary key of LocationBuilding database table.
				         * There is no field called employeeRoomNumber in the macForm, all we have is
				         * employeeRoomNumber(i) where 'i' is the buildingSelectedIndex value
				         */
				        String buildingSelectedIndex = request.getParameter("buildingSelectedIndex"); 
				        log.info("buildingSelectedIndex: " + buildingSelectedIndex);
				        /*
				         * Grabbing the room ie selected for the selected building.
				         * All the option values of employeeRoomNumber selectboxes contain 
				         * the primary keys of LocationRooms database table
				         */
				        Integer employeeRoomId = Integer.parseInt(request.getParameter("employeeRoomNumber"+buildingSelectedIndex));                       
				        log.info("employeeRoomId: " + employeeRoomId);

			        	if(buildingId != null && employeeRoomId != null ){
			        		try{
			        			log.info("Start getLocationByRoomIdAndBuildingId");
			        			employeeLocation = (Location) employeeManager.getLocationByRoomIdAndBuildingId(employeeRoomId, buildingId).get(0);
			        			log.info("getLocationByRoomIdAndBuildingId Success");
			        		} catch(IndexOutOfBoundsException iob){
			        			log.error("IndexOutOfBoundsException: No Location by given RoomId and buildingId in database, employeeRoomId: " + 
			        						employeeRoomId + "; buildingId: "+ buildingId);
			        			log.error("StackTrace: "+IOUtil.StackTraceToString(iob));
			        			macModel.put("dataAccessException", true);
			        			return new ModelAndView(getSuccessView(), macModel);
			        		} catch(DataAccessException dae){
			        			log.error("DataAccessException: getLocationByRoomIdAndBuildingId" + 
			        						dae.getClass()+"  "+dae.getCause()+"  "+ dae.getMessage());
			        			log.error("StackTrace: " + IOUtil.StackTraceToString(dae));
			        			macModel.put("dataAccessException", true);
			        			return new ModelAndView(getSuccessView(), macModel);
			        		} 
			        	}
			        }          
			        
			        /*
			         * Get the employee from PMP by employee's Email as it is unique field
			         */
			                      
			        //Get Employee 
			        Employee employee = null;
			        if(employeeEmail != null){
		        		log.info("Inside if employeeEmail != null");
			        	try{
			        		log.info("Start getEmployeeByEmail");
			        		employee = employeeManager.getEmployeeByEmail(employeeEmail);
			        		log.info("getEmployeeByEmail Success");
			        	} catch(IndexOutOfBoundsException iob){			        		
		        			log.error("IndexOutOfBoundsException: No employee with given employeeEmail: " +  employeeEmail 
		        						+ " in database");
		        			log.error("StackTrace: "+IOUtil.StackTraceToString(iob));
		        			macModel.put("dataAccessException", true);
		        			return new ModelAndView(getSuccessView(), macModel);
			        	} catch(DataAccessException dae){
			        		log.error("DataAccessException, getEmployeeByEmail " + 
		    						dae.getClass()+"  "+dae.getCause()+"  "+ dae.getMessage());
			        		log.error("StackTrace: " + IOUtil.StackTraceToString(dae));
			        		macModel.put("dataAccessException", true);
			        		return new ModelAndView(getSuccessView(), macModel);
			        	}
			        }
			
			        /*
			         * Set Employee's InfoUpdated field to 1 for 
			         * all employeeTypes(staff, faculty and student) within Database
			         */
			        employee.setInfoUpdated(new Short((short) 1));
			        log.info("info_updated field is updated to 1.  Supervisor will no longer receive reminder emails.");
			        Integer employeeId = employee.getEmployeeId();      
			        DateConversion dateConvertObj = new DateConversion();
			        Date effectiveDate = dateConvertObj.formatStringToDate(effectiveDateStr);
			        Calendar  currentDateInstance = Calendar.getInstance();
			        Calendar effectiveDateInstance = Calendar.getInstance();
			        effectiveDateInstance.setTime(effectiveDate);  
			        
			        log.info("effectiveDateInstance: " + effectiveDateInstance);
			        log.info("currentDateInstance: " + currentDateInstance);			                
			
				    if (requestType.equals("2")){
				    	
				    	log.info("Inside requestType=2");
				     	/*
				       	 * Compare the effectiveDate with today's Date.  If effectiveDate is already passed
				      	 * then update the Employee Info in Employee table else update the Employee Info in
				       	 * OrgChartTemp table.
				       	 */				    	
					    	
			           	OrgChartTemp orgChartTemp = null;
				    	//if(effectiveDateInstance.before(currentDateInstance)){
			           	if(currentDateInstance.compareTo(effectiveDateInstance) < 0){
			           		log.info("effectiveDate is yet to come");
				           	//Update in OrgChartTemp table
				           try{
				           		log.info("Start getOrgChartTempByEmployeeId");
				           		orgChartTemp = employeeManager.getOrgChartTempByEmployeeId(employeeId.toString());
				           		log.info("getOrgChartTempByEmployeeId Success");
				           	} catch(IndexOutOfBoundsException iob){
				           		log.error("IndexOutOfBoundsException: No orgChartTemp with given employeeId: " +  employeeId);
				           		log.error("StackTrace: "+IOUtil.StackTraceToString(iob));
				           		macModel.put("dataAccessException", true);
				           		return new ModelAndView(getSuccessView(), macModel);
				           	} catch(DataAccessException dae){
				           		log.error("DataAccessException: getOrgChartTempByEmployeeId " + 
				           					dae.getClass()+"  "+dae.getCause()+"  "+ dae.getMessage());
			        			log.error("StackTrace: " + IOUtil.StackTraceToString(dae));
				           		macModel.put("dataAccessException", true);
			        			return new ModelAndView(getSuccessView(), macModel);
				           	}
				           	if(employeeLocation != null){
				               	orgChartTemp.setLocation(employeeLocation);
				            } else {
				            	log.info("employeeLocation is null");
				            }
				           	orgChartTemp.setInfoUpdated(new Short((short) 1));
				           	log.info("Start saveOrgChartTemp");
				           	employeeManager.saveOrgChartTemp(orgChartTemp);
				           	log.info("saveOrgChartTemp Success");
				    	} else {
				           	log.info("effectiveDate is either today or has already passed the currentDate");				           	
				           	//Update in Employee table as 
				           	/*
				           	 * It will only be present in Employee table.
				           	 * So update in Employee table
				           	 */
				           	System.out.println("Update in Employee table");
					        try{
					        	log.info("Start getEmployee");
					           	employee = (Employee) employeeManager.getEmployee(employeeId.toString());
					           	log.info("getEmployee Success");
					        } catch(NullPointerException npe){					        	
					        	log.error("NullPointerException: employeeManager.getEmployee, " 
					        				+ "No employee with given employeeId: " +  employeeId 
					        				+ " in Employee table.");
					           	log.error("StackTrace: "+IOUtil.StackTraceToString(npe));
				           		macModel.put("dataAccessException", true);
					           	return new ModelAndView(getSuccessView(), macModel);
					        } catch(DataAccessException dae){
					        	log.error("DataAccessException: getOrgChartTempByEmployeeId " + 
					           				dae.getClass()+"  "+dae.getCause()+"  "+ dae.getMessage());
				        		log.error("StackTrace: " + IOUtil.StackTraceToString(dae));
				           		macModel.put("dataAccessException", true);
				        		return new ModelAndView(getSuccessView(), macModel);
					        }
					        if(employeeLocation != null){
					        	employee.setLocation(employeeLocation);					        						        	
					        } else {
					        	log.info("employeeLocation is null");
					        }
				          	employee.setInfoUpdated(new Short((short) 1));				          					          	
						    log.info("Start saveEmployee");
						    employeeManager.saveEmployee(employee);
						    log.info("saveEmployee Success");
				        }   
					} else {
						//for requestType!=2 ie for new Hirees
				       	if(employeeLocation != null){
				       		employee.setLocation(employeeLocation);
				       	} else {
				       		log.info("employeeLocation is null");
				       	}
					    log.info("Start saveEmployee");
					    employeeManager.saveEmployee(employee);
					    log.info("saveEmployee Success");
				    }                

		        } else {
		        	macModel.put("employeeType", false);	        	
		        	if(employeeType.equals("workstudy") || 		        
		        			employeeType.equals("tutor") ||
		        				employeeType.equals("other")){
		        		log.info("Inside if employeeType = workstudy or tutor or other");
		        		macModel.put("updateDatabase", false);
		        	}
		        } 
		         
		        //Get Phone By its Extension and assign employeeId into the Phone table
		        //String phoneExtension = request.getParameter("employeePhone");
		        //Phonenumbers employeePhone = (Phonenumbers) employeeManager.getPhoneByPhoneExtension(phoneExtension).get(0);
		        //employeePhone.setEmployee(employee);
		        
		        //Update the employeePhone with the employeeId in PMP
		        //employeeManager.savePhonenumbers(employeePhone);
		        
		        
		        //IT Services 
		        String existingPC = request.getParameter("existingPC");
		        String existingPhone = request.getParameter("existingPhone");
		        
		        //hardware or software reqts
		        String hardwareSoftware = request.getParameter("hardwareSoftware");
		        String hardwareSoftwareRequirements = request.getParameter("hardwareSoftwareRequirements");
		        
		        //Access or account reqts
		        String access = request.getParameter("access");
		        String networkAccess = request.getParameter("networkAccess");
		        String specifyNetworkAccess = request.getParameter("specifyNetworkAccess");
		        String datatelAccess = request.getParameter("datatelAccess");
		        String specifyDatatelAccess = request.getParameter("specifyDatatelAccess");
		        String starrsAccess = request.getParameter("starrsAccess");
		        String specifyStarrsAccess = request.getParameter("specifyStarrsAccess");
		        String printerAccess = request.getParameter("printerAccess");
		        String specifyPrinterAccess = request.getParameter("specifyPrinterAccess");
		        String fileAccess = request.getParameter("fileAccess");
		        String specifyFileAccess = request.getParameter("specifyFileAccess");
		        String accessRequirements = request.getParameter("accessRequirements");
		        
		        //Facilities Services name plate
		        String nameplate = request.getParameter("nameplate");
		        String nameplateText = request.getParameter("nameplateText");
		        String nameplateType = request.getParameter("nameplateType");
		        String specialNameplate = request.getParameter("specialNameplate");
		        String nameplateHolder = request.getParameter("nameplateHolder");
		        String nameplateColor = request.getParameter("nameplateColor");
		        //String nameplateSize = request.getParameter("nameplateSize");
		        String sizeText = request.getParameter("sizeText");
		        //String nameplateTitle = request.getParameter("nameplateTitle");
		        String titleText = request.getParameter("titleText");
		        
		        //Facilities Services furniture
		        String furniture = request.getParameter("furniture");
		        String furnitureRequirements = request.getParameter("furnitureRequirements");
		        
		        //Facilities Services move to new location
		        String move = request.getParameter("move");
		        String moveRequirements = request.getParameter("moveRequirements");
		        
		        //Facilities Services keys access cards
		        String accessCard = request.getParameter("accessCard");
		        String accessCardType = request.getParameter("accessCardType");
		        String keys = request.getParameter("keys");
		        String accessCardRequirements = request.getParameter("accessCardRequirements");
		        
		        //HelpDesk Email 
		        BuildEmail helpDeskEmail = new BuildEmail();
		        /*
		         * To send email to more than one email address
		         * add them into the toAddress[]. 
		         */        
		        String [] toAddress;
		        String fromAddress = supervisorEmail;
		        String subject = null;
		        StringBuffer emailHeaderContent = new StringBuffer();
		        if (requestType.equals("1")){
		        	emailHeaderContent.append("Request Type: New Hire \n");		        				
		        } else if (requestType.equals("2")){
		        	emailHeaderContent.append("Request Type: Transfer \n");		        				
		        }
		        emailHeaderContent.append("--------------------------------------------------------\n\n")
		        			.append("EFFECTIVE DATE \n")
							.append("--------------------------------------------------------\n")
							.append(effectiveDateStr).append("\n\n")
							
		        			.append("EMPLOYEE \n")
		        			.append("--------------------------------------------------------\n")
		        			.append(employeeName).append(" (").append(employeeUsername).append(")\n")
		        			.append(employeeTitle).append(", ").append(employeeDepartment).append("\n")
		        			.append(changedEmployeeBuilding).append(" ").append(changedEmployeeRoomNumber).append(", ")
		        			.append(employeePhone).append("\n\n")
		        			
		        			.append("SUPERVISOR \n")
		        			.append("--------------------------------------------------------\n")
		        			.append(supervisorName).append(" (").append(supervisorUsername).append(")\n")
		        			.append(supervisorTitle).append(", ").append(supervisorDepartment).append("\n")        		
		        			.append(supervisorBuilding).append(" ").append(supervisorRoomNumber).append(", ")
		        			.append(supervisorPhone).append("\n\n");
		        
		        /*
		         * Get all the email content to send it to New Hire Queue
		         */
		        StringBuffer  emailAllContent = new StringBuffer();
		        emailAllContent.append(emailHeaderContent);			
		       if (existingPC.equals("no")){
		    	   /*subject = helpdeskGeneralQueue + employeeName;
		           StringBuffer  emailContent = new StringBuffer();
		           emailContent.append(emailHeaderContent);		    	   
		    	   emailContent.append("PC  \n")		    	   
					.append("--------------------------------------------------------\n");
				   */
		    	   emailAllContent.append("PC  \n")
					.append("--------------------------------------------------------\n");               
		    	   //emailContent.append(employeeName).append(" requires a PC\n\n");
		    	   emailAllContent.append(employeeName).append(" requires a PC\n\n");
		    	  /* if(!helpDeskEmail.generateHelpDeskEmail(toAddress, fromAddress, subject, emailContent)){
		    		   emailServiceDeskFailed.add(subject);
		    	   } else {
		    		   emailServiceDeskSent.add(subject);
		    	   }
		    	   */
		       }
		       if(hardwareSoftware != null && hardwareSoftware.equals("on") && hardwareSoftwareRequirements != null){
		    	   /*subject  = helpdeskGeneralQueue + employeeName;
		    	   StringBuffer  emailContent = new StringBuffer();
		    	   emailContent.append(emailHeaderContent);
		    	   emailContent.append("HARDWARE/SOFTWARE  \n")
					.append("--------------------------------------------------------\n");
				  */
		    	   emailAllContent.append("HARDWARE/SOFTWARE  \n")
					.append("--------------------------------------------------------\n");    	   
		    	   //emailContent.append(hardwareSoftwareRequirements).append("\n\n");
		    	   emailAllContent.append(hardwareSoftwareRequirements).append("\n\n");
		    	 /*  if(!helpDeskEmail.generateHelpDeskEmail(toAddress, fromAddress, subject, emailContent)){
		    		   emailServiceDeskFailed.add(subject);
		    	   } else {
		    		   emailServiceDeskSent.add(subject);
		    	   }
		    	   */
		       }
		
		       if (existingPhone.equals("no")){    	   
		    	   /*subject  = helpdeskPhoneQueue + employeeName;
		    	   StringBuffer  emailContent = new StringBuffer();
		    	   emailContent.append(emailHeaderContent);
		    	   emailContent.append("PHONE  \n")
					.append("--------------------------------------------------------\n");
		    	   */
		    	   emailAllContent.append("PHONE  \n")
					.append("--------------------------------------------------------\n");    
		    	   //emailContent.append(employeeName).append(" requires a Phone\n\n");
		    	   emailAllContent.append(employeeName).append(" requires a Phone\n\n");
		    	 /*  if(!helpDeskEmail.generateHelpDeskEmail(toAddress, fromAddress, subject, emailContent)){
		    		   emailServiceDeskFailed.add(subject);
		    	   } else {
		    		   emailServiceDeskSent.add(subject);
		    	   }
		    	   */
		       } 
		
		       if(access != null && access.equals("on")){
		    	   /*subject = helpdeskWindowsQueue + employeeName;
		    	   StringBuffer  emailContent = new StringBuffer();
		    	   emailContent.append(emailHeaderContent);
		    	   emailContent.append("ACCOUNTS ACCESS  \n")
					.append("--------------------------------------------------------\n");
		    	   */
		    	   emailAllContent.append("ACCOUNTS ACCESS  \n")
					.append("--------------------------------------------------------\n");
		           if(networkAccess != null && networkAccess.equals("on") && (specifyNetworkAccess != null)){
		        	   //emailContent.append("Network Access: Same permissions as ").append(specifyNetworkAccess).append("\n");
		        	   emailAllContent.append("Network Access: Same permissions as ").append(specifyNetworkAccess).append("\n");
		           }           
		           if(starrsAccess != null && starrsAccess.equals("on") && (specifyStarrsAccess != null)){
		        	   //emailContent.append("STARRS Access: Same permissions as ").append(specifyStarrsAccess).append("\n");
		        	   emailAllContent.append("STARRS Access: Same permissions as ").append(specifyStarrsAccess).append("\n");
		           }
		           if(printerAccess != null && printerAccess.equals("on") && (specifyPrinterAccess != null)){
		        	   //emailContent.append("Printer Access: Same permissions as ").append(specifyPrinterAccess).append("\n"); 
		        	   emailAllContent.append("Printer Access: Same permissions as ").append(specifyPrinterAccess).append("\n"); 
		           }           
		           if(fileAccess != null && fileAccess.equals("on") && (specifyFileAccess != null)){
		        	   //emailContent.append("File Access: ").append(specifyFileAccess).append("\n");
		        	   emailAllContent.append("File Access: ").append(specifyFileAccess).append("\n");
		           } 
		           if(accessRequirements!= null){
		        	   //emailContent.append("Other Access Requirements: ").append(accessRequirements).append("\n\n");
		        	   emailAllContent.append("Other Access Requirements: ").append(accessRequirements).append("\n\n");
		           }
		         /*  if(!helpDeskEmail.generateHelpDeskEmail(toAddress, fromAddress, subject, emailContent)){
		    		   emailServiceDeskFailed.add(subject);
		           } else {
		    		   emailServiceDeskSent.add(subject);
		           }
		           */
		       }
		       
		       if(access != null && access.equals("on")){
		    	   if(datatelAccess != null && datatelAccess.equals("on") && (specifyDatatelAccess != null)){
		    		   /*subject  = helpdeskUISQueue + employeeName;
		    		   StringBuffer  emailContent = new StringBuffer();
		        	   emailContent.append(emailHeaderContent);
		        	   emailContent.append("DATATEL  \n")
		   				.append("--------------------------------------------------------\n");
		        	   */
		        	   emailAllContent.append("DATATEL  \n")
		   				.append("--------------------------------------------------------\n");
		        	   //emailContent.append("Datatel Access: Same permissions as ").append(specifyDatatelAccess).append("\n\n");
		        	   emailAllContent.append("Datatel Access: Same permissions as ").append(specifyDatatelAccess).append("\n\n");
		        	  /* if(!helpDeskEmail.generateHelpDeskEmail(toAddress, fromAddress, subject, emailContent)){
		        		   emailServiceDeskFailed.add(subject);        	   
		        	   } else {
		        		   emailServiceDeskSent.add(subject);
		        	   }
		        	   */
		    	   }
		       }
		               
		       if(nameplate != null && nameplate.equals("on") && (nameplateText != null)){
		    	   subject = nameplateQueue + employeeName;
		    	   StringBuffer  emailContent = new StringBuffer();
		    	   emailContent.append(emailHeaderContent);
		    	   emailContent.append("NAMEPLATE  \n")
					.append("--------------------------------------------------------\n");
		    	   
		    	   emailAllContent.append("NAMEPLATE  \n")
					.append("--------------------------------------------------------\n");
		    	   emailContent.append("Text: ").append(nameplateText).append("\n")
						.append("Type: ").append(nameplateType).append("\n")
						.append("Special: ").append(specialNameplate).append("\n")
						.append("Holder: ").append(nameplateHolder).append("\n")
						.append("Color: ").append(nameplateColor).append("\n")
						.append("Size: ").append(sizeText).append("\n")
						.append("Title: ").append(titleText).append("\n\n");
		    	    
		    	   emailAllContent.append("Text: ").append(nameplateText).append("\n")
						.append("Type: ").append(nameplateType).append("\n")
						.append("Special: ").append(specialNameplate).append("\n")
						.append("Holder: ").append(nameplateHolder).append("\n")
						.append("Color: ").append(nameplateColor).append("\n")
						.append("Size: ").append(sizeText).append("\n")
						.append("Title: ").append(titleText).append("\n\n");
		    	   toAddress = nameplateEmail;
		    	   if(!helpDeskEmail.generateHelpDeskEmail(toAddress, fromAddress, subject, emailContent)){
		    		   emailServiceDeskFailed.add(subject);
		    	   } else {
		    		   emailServiceDeskSent.add(subject);
		    	   }
		    	   
		       }
		       
		        
		       if((furniture !=null && furniture.equals("on") && (furnitureRequirements != null))){
		    	   /*subject = helpdeskFacilitiesQueue + employeeName;
		    	   toAddress = helpdeskEmail;
		    	   StringBuffer  emailContent = new StringBuffer();
		    	   emailContent.append(emailHeaderContent);
		    	   emailContent.append("FURNITURE  \n")
					.append("--------------------------------------------------------\n");
		    	   */
		    	   emailAllContent.append("FURNITURE  \n")
					.append("--------------------------------------------------------\n");
		    	   //emailContent.append(furnitureRequirements).append("\n\n");
		    	   emailAllContent.append(furnitureRequirements).append("\n\n");
		    	   /*if(!helpDeskEmail.generateHelpDeskEmail(toAddress, fromAddress, subject, emailContent)){
		    		   emailServiceDeskFailed.add(subject);
		    	   } else {
		    		   emailServiceDeskSent.add(subject);
		    	   }
		    	   */
		       }
		       
		       if(move != null && move.equals("on") && (moveRequirements != null)){
		    	   /*subject = helpdeskFacilitiesQueue + employeeName;
		    	   toAddress = helpdeskEmail;
		    	   StringBuffer  emailContent = new StringBuffer();
		    	   emailContent.append(emailHeaderContent);
		    	   emailContent.append("RELOCATION  \n")
					.append("--------------------------------------------------------\n");
		    	   */
		    	   emailAllContent.append("RELOCATION  \n")
					.append("--------------------------------------------------------\n");
		    	   //emailContent.append(moveRequirements).append("\n\n");
		    	   emailAllContent.append(moveRequirements).append("\n\n");
		    	  /* if(!helpDeskEmail.generateHelpDeskEmail(toAddress, fromAddress, subject, emailContent)){
		    		   emailServiceDeskFailed.add(subject);
		    	   } else {
		    		   emailServiceDeskSent.add(subject);
		    	   }
		    	   */
		       }
		        
		       if((accessCard != null && accessCard.equals("on")) && (accessCardType != null || (keys != null && keys.equals("on")) || accessCardRequirements != null)){
		    	   subject = securityQueue + employeeName;
		    	    toAddress = securityEmail; 
		    	    StringBuffer  emailContent = new StringBuffer();
		    	    emailContent.append(emailHeaderContent);
		    	    emailContent.append("BUILDING ACCESS  \n")
					.append("--------------------------------------------------------\n");
					
		    	   emailAllContent.append("BUILDING ACCESS  \n")
					.append("--------------------------------------------------------\n");
		    	   if(accessCardType != null){
		    		   //emailContent.append("Access Card Required: ").append(accessCardType).append("\n");
		    		   emailAllContent.append("Access Card Required: ").append(accessCardType).append("\n");
		    	   }
		    	   if(keys != null){
		    		   emailContent.append("Keys Required: ").append(keys).append("\n");
		    		   emailAllContent.append("Keys Required: ").append(keys).append("\n");
		    	   }
		    	   if(accessCardRequirements != null){
		    		   emailContent.append("Other Access Card Requirements: ").append(accessCardRequirements).append("\n\n");
		    		   emailAllContent.append("Other Access Card Requirements: ").append(accessCardRequirements).append("\n\n");
		    	   }
		    	   if(!helpDeskEmail.generateHelpDeskEmail(toAddress, fromAddress, subject, emailContent)){
		    		   emailServiceDeskFailed.add(subject);
		    	   } else {
		    		   emailServiceDeskSent.add(subject);
		    	   }
		    	   
		       }
		       subject = helpdeskNewHireQueue + employeeName; 
		       //toAddress = helpdeskEmail;
		       toAddress = macNewHireEmail;
		       if(!helpDeskEmail.generateHelpDeskEmail(toAddress, fromAddress, subject, emailAllContent)){
				   emailServiceDeskFailed.add(subject);
		       } else {
				   emailServiceDeskSent.add(subject);
		       }
		       macModel.put("emailServiceDeskSent", emailServiceDeskSent);
		       macModel.put("emailServiceDeskFailed", emailServiceDeskFailed);
		       return new ModelAndView(getSuccessView(), macModel);
	    	}catch(DataAccessException dae) {
		       	log.error("DataAccessException: " + dae.getMessage());
				log.error("StackTrace: "+IOUtil.StackTraceToString(dae));
			    macModel.put("dataAccessException", true);
	 		    return new ModelAndView(getSuccessView(), macModel);
	    	}catch(Exception e) {
	    		log.error("Exception: " + e.getMessage());
				log.error("StackTrace: "+IOUtil.StackTraceToString(e));
	    	    macModel.put("exception", true);
	 		    return new ModelAndView(getSuccessView(), macModel);
	    	}
		    
    	} 

}


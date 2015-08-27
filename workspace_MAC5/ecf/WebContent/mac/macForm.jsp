<%@ include file="/WEB-INF/taglibs.jsp"%>

<%@page import="java.io.*" %>
<%@page import="java.util.*" %>
<%@page import="edu.franklin.eac.model.pmp.Building" %>
<%@page import="edu.franklin.eac.model.pmp.Location" %>
<%@page import="edu.franklin.eac.model.pmp.Room" %>
<%@page import="edu.franklin.eac.service.EmployeeManager" %>


<script language="javascript">
var ctlRoom;
	function buildingChanged(selectedIndex, optionsLength){
		var empRoomSelected = selectedIndex;
		document.forms[0].buildingSelectedIndex.value = selectedIndex;
		document.forms[0].changedEmployeeBuilding.value = document.getElementById("employeeBuilding").options[selectedIndex].text;
		document.forms[0].changedEmployeeRoomNumber.value = document.getElementById("employeeRoomNumber"+selectedIndex).options[0].text;								
		//Make all room select boxes display none
		for(var i=0;i<optionsLength;i++){
			if(i != selectedIndex) {
				document.getElementById("employeeRoomNumber"+i).style.display='none';
			}
		}
		var ctlRoom = document.getElementById("employeeRoomNumber"+ selectedIndex);
		var ctlBuilding = document.getElementById("employeeBuilding");
		if(selectedIndex != null ){
			document.getElementById("employeeRoomNumber"+selectedIndex).style.display='block';			
		} else {
			alert("Current browser does not support javascript or DOM2, please use the supported browser.");
		}
	}
	function roomChanged(buildingSelectedIndex, selectedIndex, optionsLength){
		document.forms[0].changedEmployeeRoomNumber.value = document.getElementById("employeeRoomNumber"+buildingSelectedIndex).options[selectedIndex].text;				
	}
</script>


<html>
<head>
	<script src="js/jquery/jquery-1.3.2.min.js"></script>
	<script src="js/jquery/jquery-ui-1.7.custom.min.js"></script>
	<script src="js/index.js"></script>
	<script src="js/validation/validation.js"></script>
	<script src="js/autocomplete/local.js"></script>
	<script src="js/autocomplete/jquery.autocomplete.min.js"></script>
	<link rel="stylesheet" type="text/css" href="css/index.css" />
	<link rel="stylesheet" type="text/css" href="js/jquery/theme/ui.all.css" />
	<link rel="stylesheet" type="text/css" href="css/buttons/buttons.css" />
	<link rel="stylesheet" type="text/css" href="js/autocomplete/css/jquery.autocomplete.css" />
</head>
<body>
	<spring:bind path="building.*">
    	<c:if test="${not empty status.errorMessages}">
    	<div class="error">	
        	<c:forEach var="error" items="${status.errorMessages}">
            	<c:out value="${error}" escapeXml="false"/><br/>
        	</c:forEach>
    	</div>
    	</c:if>
	</spring:bind>
	<div id="container">
		<div id="header">
			<h1>Employee Move/Add/Change Request</h1>
		</div>
		<div style="margin: 4px 20px; text-align: right;">
			<a href='../<c:url value="j_spring_security_logout"/>' style='font-size: 11px; margin:0; text-decoration:none; color: #396e8e;'>Logout</a>
		</div>
		<form class="validate" style="margin-top: 0px" action="<c:url value="/mac/updateEmployee.html"/>" method="post" onsubmit="return validateBuilding(this)">
			<fieldset id="requestInfo">
				<h2>Request Information</h2>
				<table>
					<tr>
						<td><label for="employeeName">Employee Name</label></td>
						<td>								
							<input id="employeeName" name="employeeName" type="text" />
							<input id="employeeID" name="employeeID" type="hidden" />
							<input id="employeeType" name="employeeType" type="hidden" />											
							<input id="employeeTitle" name="employeeTitle" type="hidden" />
							<input id="employeeDepartment" name="employeeDepartment" type="hidden" />
						</td>
					</tr>
					<tr>
						<td><label for="supervisorName">Supervisor Name</label></td>
						<td>
							<input id="supervisorName" name="supervisorName" type="text" />
							<input id="supervisorID" name="supervisorID" type="hidden" />
							<input id="supervisorTitle" name="supervisorTitle" type="hidden" />
							<input id="supervisorDepartment" name="supervisorDepartment" type="hidden" />
							<input id="supervisorBuilding" name="supervisorBuilding" type="hidden" />
							<input id="supervisorRoomNumber" name="supervisorRoomNumber" type="hidden" />
							<input id="supervisorPhone" name="supervisorPhone" type="hidden" />
						</td>
					</tr>
					<tr>
						<td><label for="effectiveDate">Effective Date</label></td>
						<td>
							<input id="effectiveDate" name="effectiveDate" readonly="readonly" type="text" />
							<input id="date" name="date" type="hidden" />
							<input id="requestType" name="requestType" type="hidden" value="standard" />
						</td>
					</tr>
				</table>
			</fieldset>
								<%									
								/*	EmployeeManager employeeManager = (EmployeeManager) request.getAttribute("employeeManager");					
							    	List  buildingList  =  employeeManager.getAllBuildings();
							    	int buildingListSize = buildingList.size();
							    	System.out.println("Inside Jsp");
							    	Building buildingObj = null;
							    	List locationsList = null;
							    	Iterator buildingListItr = buildingList.iterator();    	
							    	Integer buildingId = null;
							    	String buildingName = null;
							    	String roomId = null;
							    	String roomName = null;				    	
							    	Iterator locationsListItr;
							    	Location locationObj = null;
							    	Room roomObj  = null;
							    	int i=0, j=0,k=0;
							    	//Assuming max no. of rooms per building as 200 
							    	String [][] buildingRoomIdsArray =  new String [buildingListSize][200];
							    	String [][] buildingRoomNamesArray = new String [buildingListSize][200];
							    	//Array holds the List of no. of rooms per building
							    	int [] numOfRoomsPerBuilding = new int [buildingListSize];
							    	while(buildingListItr.hasNext()){
							    		j=0;
							        	buildingObj =  (Building) buildingListItr.next();        	
							        	//System.out.println("Building Id: " + buildingObj.getBuildingId());
							        	//System.out.println("Building Name: " + buildingObj.getName());				       
							        	buildingId = buildingObj.getBuildingId();
							        	buildingName = buildingObj.getName();							        	
							        	//Insert buildingIds in Array's (i,0) position
							        	buildingRoomIdsArray[i][j] = (String) buildingId.toString();
							        	buildingRoomNamesArray[i][j] = buildingName;
							        	j++;
							        	locationsList = employeeManager.getAllRoomsFromLocationByBuildingId(buildingId);
							        	numOfRoomsPerBuilding[k] = locationsList.size();
							        	k++;
							        	locationsListItr = locationsList.iterator();	
								        	while(locationsListItr.hasNext()){
								        		locationObj = (Location) locationsListItr.next();
								        		roomObj = (Room) locationObj.getRoom();
								        		roomId = roomObj.getRoomId().toString();
								        		roomName = roomObj.getName();
								            	//System.out.println("Building: " + buildingObj.getName() + "; Room: " + roomObj.getName());
								            	//Insert RoomIds in Array's (i, j) position
								            	buildingRoomIdsArray[i][j] = roomId;
								            	buildingRoomNamesArray[i][j] = roomName;
								            	System.out.println("buildingRoomIdsArray[" + i + "][" + j + "]: " + buildingRoomIdsArray[i][j]);
								            	System.out.println("buildingRoomNamesArray[" + i + "][" + j + "]: " + buildingRoomNamesArray[i][j] + "\n\n");
								            	j++;
								        	}
								        	i++;						
								    	}
							    	*/
							    	
							    	String [][] buildingRoomIdsArray =  (String[][]) request.getAttribute("buildingRoomIdsArray");
							    	String [][] buildingRoomNamesArray = (String[][]) request.getAttribute("buildingRoomNamesArray");
							    	int [] numOfRoomsPerBuilding = (int[]) request.getAttribute("numOfRoomsPerBuilding");
							    	int numOfBuildings = numOfRoomsPerBuilding.length;
									%>
			<fieldset id="directoryInfo">
				<h2>Employee Directory Information</h2>									
				<table>
					<tr>
						<td><label for="employeeBuilding">Building</label></td>
						<td>							
							<select id="employeeBuilding" name="employeeBuilding" onchange="buildingChanged(this.selectedIndex, this.options.length);" >
							
							<% for(int l=0;l<numOfBuildings;l++){  %>							
								<option value="<%=buildingRoomIdsArray[l][0]%>"><%=buildingRoomNamesArray[l][0]%></option>
							<%} %>														
							</select>
							<!-- currentEmployeeBuilding is always null -->										
							<input id="currentEmployeeBuilding" name="currentEmployeeBuilding" type="hidden" />
							<!-- The following variables are assigned values within javascript -->	
							<input id="buildingSelectedIndex" name="buildingSelectedIndex" type="hidden" />
							<input id="changedEmployeeBuilding" name="changedEmployeeBuilding" type="hidden" />							
						</td>
					</tr>

					<tr>
						<td><label for="employeeRoomNumber">Room</label></td>
						<td>
							<%for(int l=0;l<numOfBuildings;l++){  %>
									<select id="employeeRoomNumber<%=l%>" name="employeeRoomNumber<%=l%>" onchange="roomChanged(<%=l%>, this.selectedIndex, this.options.length);" style="display:none">
								<% 
								 for(int m=1;m<=numOfRoomsPerBuilding[l];m++){%>							
									<option value="<%=buildingRoomIdsArray[l][m]%>"><%=buildingRoomNamesArray[l][m]%></option>
								 <%	} %>														
									</select>									
							<%}%>
							<!-- currentEmployeeRoomNumber is always null -->	
							<input id="currentEmployeeRoomNumber" name="currentEmployeeRoomNumber" type="hidden" />
							<!-- The following variable is assigned value within javascript -->	
							<input id="changedEmployeeRoomNumber" name="changedEmployeeRoomNumber" type="hidden" />

						</td>
					</tr>
					<tr>
						<td><label for="employeePhone">Extension</label></td>
						<td>
							<input id="employeePhone" name="employeePhone" type="text" />
							<input id="currentEmployeePhone" name="currentEmployeePhone" type="hidden" />
						</td>
					</tr>
				</table>
			</fieldset>
			<fieldset id="it">
				<h2>IT Services</h2>
				<table>
					<tr>
						<td><label for="existingPC">Is there an existing PC for this employee?</label></td>
						<td>
							<select id="existingPC" name="existingPC">
								<option value="yes">Yes</option>
								<option value="no">No</option>
							</select>
						</td>
					</tr>
					<tr>
						<td><label for="existingPhone">Is there an existing phone for this employee?</label></td>
						<td>
							<select id="existingPhone" name="existingPhone">
								<option value="yes">Yes</option>
								<option value="no">No</option>
							</select>
						</td>
					</tr>
				</table>
				<br />
				<div>
					<input type="checkbox" id="hardwareSoftware" name="hardwareSoftware" />
					<label for="hardwareSoftware">This employee has hardware and/or software requirements</label>
					<div id="hardwareSoftwareSubMenu" class="subMenu">
						<div>
							<strong>Please note, the following software comes standard on every PC:</strong> Windows Operating System, Internet Explorer, Zimbra, Microsoft Office, Adobe Acrobat Reader, wIntegrate, WinZip, RealPlayer, QuickTime, and McAfee Virus Scan.
						</div>
						<br />
						<label for="hardwareSoftwareRequirements">Please enter a description of the user's additional hardware and/or software requirements</label>
						<textarea id="hardwareSoftwareRequirements" name="hardwareSoftwareRequirements"></textarea>				
					</div>
				</div>
				<div>
					<input type="checkbox" id="access" name="access" />
					<label for="access">This employee has access and/or account requirements</label>
					<div id="accessSubMenu" class="subMenu">
						<label>This user requires:</label>
						<br /><br />
						<div>
							<input type="checkbox" id="networkAccess" name="networkAccess" />
							<label for="networkAccess">Franklin network access</label>
							<div id="networkAccessSubMenu">
								<label for="specifyNetworkAccess">Same permissions as</label>
								<input type="text" id="specifyNetworkAccess" name="specifyNetworkAccess" />
							</div>
						</div>
						<div>
							<input type="checkbox" id="datatelAccess" name="datatelAccess" />
							<label for="datatelAccess">Datatel access</label>
							<div id="datatelAccessSubMenu">
								<label for="specifyDatatelAccess">Same permissions as</label>
								<input type="text" id="specifyDatatelAccess" name="specifyDatatelAccess" />
							</div>
						</div>
						<div>
							<input type="checkbox" id="starrsAccess" name="starrsAccess" />
							<label for="starrsAccess">STARRS access</label>
							<div id="starrsAccessSubMenu">
								<label for="specifyStarrsAccess">Same permissions as</label>
								<input type="text" id="specifyStarrsAccess" name="specifyStarrsAccess" />
							</div>
						</div>
						<div>
							<input type="checkbox" id="printerAccess" name="printerAccess" />
							<label for="printerAccess">Printer access</label>
							<div id="printerAccessSubMenu">
								<label for="specifyPrinterAccess">Same permissions as</label>
								<input type="text" id="specifyPrinterAccess" name="specifyPrinterAccess" />
							</div>
						</div>
						<div>
							<input type="checkbox" id="fileAccess" name="fileAccess" />
							<label for="fileAccess">File access</label>
							<div id="fileAccessSubMenu">
								<label for="specifyFileAccess">Specify folder name(s)</label>
								<input type="text" id="specifyFileAccess" name="specifyFileAccess" />
							</div>
						</div>
						<br />
						<label for="accessRequirements">Please enter a description of any additional account and/or access requirements</label>
						<textarea id="accessRequirements" name="accessRequirements"></textarea>
					</div>	
				</div>
			</fieldset>
			<fieldset id="facilities">
				<h2>Facilities Services</h2>
				<div>
					<input type="checkbox" id="nameplate" name="nameplate" />
					<label for="nameplate">This employee requires a new name plate</label>
					<div id="nameplateSubMenu" class="subMenu">
						<table>
							<tr>
								<td><label for="nameplateText">Text</label></td>
								<td><input type="text" id="nameplateText" name="nameplateText" /></td>
							</tr>
							<tr>
								<td><label for="nameplateType">Type</label></td>
								<td>
									<select id="nameplateType" name="nameplateType">
										<option value="metal">Door - Metal</option>
										<option value="wooden">Door - Wooden</option>
										<option value="cubicle">Cubicle Wall</option>
										<option value="desk">Desk</option>
										<option value="special">Special</option>
									</select>
									<span id="specialDiv">
										<label for="specialNameplate">Specify</label>
										<input id="specialNameplate" name="specialNameplate" type="text" />
									</span>
								</td>
							</tr>
							<tr>
								<td><label for="nameplateHolder">Holder Needed</label></td>
								<td>
									<select id="nameplateHolder" name="nameplateHolder">
										<option value="no">No</option>
										<option value="yes">Yes</option>
									</select>
								</td>
							</tr>
							<tr>
								<td><label for="nameplateColor">Color</label></td>
								<td>
									<select id="nameplateColor" name="nameplateColor">
										<option value="black">Black</option>
										<option value="blue">Blue</option>
										<option value="woodgrain">Woodgrain</option>
									</select>
								</td>
							</tr>
							<tr>
								<td><label for="nameplateSize">Custom Size</label></td>
								<td>
									<select id="nameplateSize" name="nameplateSize">
										<option value="no">No</option>
										<option value="yes">Yes</option>
									</select>
									<span id="sizeDiv">
										<label for="sizeText">Specify</label>
										<input id="sizeText" name="sizeText" type="text" />
									</span>
								</td>
							</tr>
							<tr>
								<td><label for="nameplateTitle">Display Title</label></td>
								<td>
									<select id="nameplateTitle" name="nameplateTitle">
										<option value="no">No</option>
										<option value="yes">Yes</option>
									</select>
									<span id="titleDiv">
										<label for="titleText">Specify</label>
										<input id="titleText" name="titleText" type="text" />
									</span>
								</td>
							</tr>
						</table>
					</div>
				</div>
				<div>
					<input type="checkbox" id="furniture" name="furniture" />
					<label for="furniture">This employee requires new furniture</label>
					<div id="furnitureSubMenu" class="subMenu">
						<label for="furnitureRequirements">Please enter a description of the employee's furniture requirements</label>
						<textarea id="furnitureRequirements" name="furnitureRequirements"></textarea>
					</div>
				</div>
				<div>
					<input type="checkbox" id="move" name="move" />
					<label for="move">This employee needs to move to a new location</label>
					<div id="moveSubMenu" class="subMenu">
						<label for="moveRequirements">Please enter a description of the employee's moving requirements</label>
						<textarea id="moveRequirements" name="moveRequirements"></textarea>
					</div>
				</div>
				<div>
					<input type="checkbox" id="accessCard" name="accessCard" />
					<label for="accessCard">This employee requires new keys and/or access cards</label>
					<div id="accessCardSubMenu" class="subMenu">
						<table>
							<tr>
								<td><label for="accessCardType">Access Card Required</label></td>
								<td><input id="accessCardType" name="accessCardType" type="text" /></td>
							</tr>
							<tr>
								<td><label for="keys">Keys Required</label></td>
								<td><input id="keys" name="keys" type="text" /></td>
							</tr>
						</table>
						<br />
						<label for="accessCardRequirements">Please enter any comments or additional requirements</label>
						<textarea id="accessCardRequirements" name="accessCardRequirements"></textarea>
					</div>
				</div>
			</fieldset>
			<button type="submit" class="custom"><span class="submit">Submit Request</span></button>
		</form>
	</div>
</body>
</html>
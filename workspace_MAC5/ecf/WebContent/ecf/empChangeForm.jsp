<%@ include file="/WEB-INF/taglibs.jsp"%>

<%@page import="java.io.*" %>
<%@page import="java.util.*" %>
<%@page import="edu.franklin.eac.model.pmp.Employee" %>
<%@page import="edu.franklin.eac.model.pmp.Department" %>  
<script language="javascript">
	function departmentChanged(selectedIndex){
		document.forms[0].departmentName.value = document.getElementById("department").options[selectedIndex].text;
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
	<spring:bind path="employee.*">
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
			<h1>Employee Change</h1>
		</div>
		<div style="margin: 4px 20px; text-align: right;">
			<a href='../<c:url value="j_spring_security_logout"/>' style='font-size: 11px; margin:0; text-decoration:none; color: #396e8e;'>Logout</a>
		</div>
		<form class="validate" style="margin-top: 0px" action="<c:url value="/ecf/addEmployee.html"/>" method="post" onsubmit="return validateEmployee(this)">
			<fieldset id="typeSection">
				<h2>Request</h2>
				<table>
					<tr>
						<td><label for="requestType">Request Type</label></td>
						<td>
							<select id="requestType" name="requestType">
								<option value=""></option>
								<option value="1">New Hire</option>
								<option value="2">Transfer</option>
								<option value="3">Termination</option>
							</select>
						</td>
					</tr>
				</table>
			</fieldset>
			<fieldset id="requestSection">
				<h2 id="requestHeading">New Hire</h2>
				<table id="general">
					
					<spring:bind path="employee.employeeId" >
						<input type="hidden" name="${status.expression}" value="${status.value}"/> 
					</spring:bind>				
					<tr>
						<td><label for="employeeFirstName">First Name</label></td>
						<td>
					<%
						System.out.println("Hello \n");
					%>
							<spring:bind path="employee.firstName">
								<input id="employeeFirstName" name="${status.expression}" type="text" />
							</spring:bind>
							<!-- input id="employeeFirstName" name="employeeFirstName" type="text" / -->							
						</td>
					</tr>
					<%
						System.out.println("Bye \n");
					%>
					<tr>
						<td><label for="employeeLastName">Last Name</label></td>
						<td>
							<spring:bind path="employee.lastName">
								<input id="employeeLastName" name="${status.expression}" type="text" />
							</spring:bind>
							<!-- spring:bind path="employee.email">
								<input id="employeeEmail" name="${status.expression}" type="hidden" />
							< / spring:bind -->
							<!-- input id="employeeLastName" name="employeeLastName" type="text" / -->
						</td>
					</tr>
					
					<tr>
						<td><label for="employee">Employee Name</label></td>
						<td>
							<input id="employee" name="employee" type="text" />
							<input id="eUsername" name="eUsername" type="hidden" />														
						</td>
					</tr>
					
					<tr>
						<td><label for="title">Title</label></td>
						<td><input id="title" name="title" type="text" /></td>
					</tr>
					<tr>
						<td><label for="supervisor">Supervisor</label></td>
						<td>
							<input id="supervisor" name="supervisor" type="text" />
							<input id="sUsername" name="sUsername" type="hidden" />
						</td>
					<%
			    	Department dept = null;
					List departmentList = (List) request.getAttribute("departmentList");
					%>
					<tr>
						<td><label for="department">Department</label></td>
						<td>
							<select id="department" name="department" onchange="departmentChanged(this.selectedIndex);">
								<option></option>
						<%
				    		for(Object d : departmentList){
				    			dept = (Department) d;  
						%>
								<option value="<%=dept.getDepartmentId()%>"><%=dept.getName()%></option>								
						<%
							} 
						%>
							</select>
						</td>						
					</tr>	
					<input id="departmentName" name="departmentName" type="hidden" />	
					<tr>
						<td><label for="newTitle">New Title</label></td>
						<td><input id="newTitle" name="newTitle" type="text" /></td>
					</tr>
					<tr>
						<td><label for="newSupervisor">New Supervisor</label></td>
						<td>
							<input id="newSupervisor" name="newSupervisor" type="text" />
							<input id="nsUsername" name="nsUsername" type="hidden" />
						</td>
					</tr>
					<tr>
						<td><label for="newDepartment">New Department</label></td>
						<td>
							<select id="newDepartment" name="newDepartment">
								<option></option>
								<%
						    		for(Object d : departmentList){
						    			dept = (Department) d;  
								%>
										<option value="<%=dept.getDepartmentId()%>"><%=dept.getName()%></option>								
								<%
									} 
								%>							
							</select>
						</td>
					</tr>
					<tr>
						<td><label for="newEmployeeType">New Employee Type</label></td>
						<td>
							<select id="newEmployeeType" name="newEmployeeType">
								<option value=""></option>
								<option value="staff">Staff</option>
								<option value="faculty">Faculty</option>
								<option value="adjunct">Adjunct</option>
								<option value="tutor">Tutor</option>
								<option value="workstudy">Work Study</option>
								<option value="student">Student</option>
								<option value="other">Other</option>
							</select>
						</td>
					</tr>	
					<tr>
						<td><label for="employeeType">Employee Type</label></td>
						<td>
							<select id="employeeType" name="employeeType">
								<option value=""></option>
								<option value="staff">Staff</option>
								<option value="faculty">Faculty</option>
								<option value="adjunct">Adjunct</option>
								<option value="tutor">Tutor</option>
								<option value="workstudy">Work Study</option>
								<option value="student">Student</option>
								<option value="other">Other</option>
							</select>
						</td>
					</tr>					
					<tr>
						<td><label for="effectiveDate">Effective Date</label></td>
						<spring:bind path="employee.transferDate">
							<td><input id="effectiveDate" readonly="readonly" name="effectiveDate" type="text" /></td>
						</spring:bind>
					</tr>
					<tr>
						<td><label for="cc">CC</label></td>
						<td>
							<input id="cc" name="cc" type="text" />
							<input id="ccUsername" name="ccUsername" type="hidden" />
						</td>
					</tr>
					<tr>
						<td><label for="comments">Comments</label></td>
						<spring:bind path="employee.password">
							<td><textarea id="comments" name="comments" type="text"></textarea></td>
						</spring:bind>
					</tr>

				</table>
			</fieldset>
			<!-- <input type="submit" class="submit" name="save" value="Submit Request"> -->
			<button type="submit" class="custom" name="save"><span class="submit">Submit Request</span></button>
		</form>
	</div>
</body>
</html>
<%@ page contentType="text/html" %>

<!-- following uri is for webapp2.5 that needs to be modified in web.xml 
     Need when you upgrade to tomcat6-->
<!-- %@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %  -->

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

  
<html>
<head>
	<link rel="stylesheet" type="text/css" href="css/index.css" />
</head>
<body>
	<div id="container">
		<div id="header">
			<h1>Employee Change</h1>
		</div>
		<div style="margin: 4px 20px; text-align: right;">
			<a href='../<c:url value="j_spring_security_logout"/>' style='font-size: 11px; margin:0; text-decoration:none; color: #396e8e;'>Logout</a>
		</div>
		<form>
			<fieldset>

			<c:choose>
				<c:when test="${param.exception==true}">
					<h2>Error</h2>
					<p>The form has encountered an error and is unable to complete your request.</p>
					<p>Please contact the Help Desk at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a> or 947-6222 for assitance.</p>							
				</c:when>
				<c:when test="${param.dataAccessException==true}">		
					<h2>Error Database</h2>
					<p>The form has encountered an error while trying to access the <span style="font-weight:bold">Database</span> and is unable to complete your request.</p>
					<p>Please contact the Help Desk at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a> or 947-6222 for assitance.</p>
				</c:when>				
				<c:when test="${param.empTypeQueryException==true}">				
					<h2>Error Query</h2>
					<p>The Employee/Supervisor is of type Adjunct Faculty.  This form cannot process your request.</p>
					<p>Please contact the Help Desk at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a> or 947-6222 for assitance.</p>
				</c:when>
				<c:when test="${param.positionQueryException==true}">				
					<h2>Error Query</h2>
					<p>The form has encountered an error while trying to find the <span style="font-weight:bold">New Job Title</span>  within Database and is unable to complete your request.</p>
					<p>Please contact the Help Desk at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a> or 947-6222 for assitance.</p>
				</c:when>												
				<c:when test="${param.existsActiveEmployee==true}">							
					<h2>Error - New Hire</h2>
					<p><font color="red">FAILED to add employee to Database.  There exists an active employee with same email address in the Database.</font></p>				
					<p>Please contact the Service Desk at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a> or 947-6222 for assitance.</p>													
				</c:when>
				<c:otherwise>												
		        
					<c:if test="${param.requestType=='1'}">
						<h2>Success - New Hire</h2>
						<table>
							<tr>
								<td><label>Name: </label>${param.employeeName}</td>
								<td></td>
							</tr>
							<tr>
								<td><label>Title: </label>${param.title}</td>
								<td></td>
							</tr>
							<tr>
								<td><label>Supervisor: </label>${param.supervisorName}</td>
								<td></td>
							</tr>
							<tr>
								<td><label>Department: </label>${param.departmentName}</td>
								<td></td>
							</tr>
							<tr>
								<td><label>Employee Type: </label>${param.employeeType}</td>
								<td></td>
							</tr>
							<tr>
								<td><label>Effective Date: </label>${param.effectiveDate}</td>
								<td></td>
							</tr>
						</table>
						<ul>
						<c:choose>
							<c:when test="${param.ldapAccount==true}">
								<li>An LDAP account has been created for the new employee.</li>						

								<c:if test="${param.employeeType == 'workstudy' || param.employeeType == 'tutor' || param.employeeType=='other'}">
									<c:if test="${not empty param.emailSupervisorSent}">
										<li>An email has been sent to the employee's supervisor ${param.supervisorName} (${param.emailSupervisorSent}) with instructions to fill out the Move/Add/Change form.</li>
									</c:if>
									<c:if test="${not empty param.emailSupervisorFailed}">	
										<li><font color="red">FAILED to send an email to employee's supervisor ${param.supervisorName} (${param.emailSupervisorFailed}) with instructions to fill out the Move/Add/Change form.</font></li>
										<p>Please contact the Service Desk at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a> or 947-6222 for assitance.</p>													
									</c:if>	
								</c:if>
								
							
								<c:if test= "${param.employeeType == 'staff'  || param.employeeType == 'faculty' || param.employeeType=='student'}"> 
									
									<li>The employee's information has been added to the PMP database and will be reflected in the Phone Roster within one business day.</li>
				
									<c:if test="${not empty param.emailSupervisorSent}">	
										<li>An email has been sent to the employee's supervisor ${param.supervisorName} (${param.emailSupervisorSent}) with instructions to fill out the Move/Add/Change form.</li>
									</c:if>
									<c:if test="${not empty param.emailSupervisorFailed}">	
										<li><font color="red">FAILED to send an email to employee's supervisor ${param.supervisorName} (${param.emailSupervisorFailed}) with instructions to fill out the Move/Add/Change form.</font></li>
										<p>Please contact the Service Desk at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a> or 947-6222 for assitance.</p>						
									</c:if>
								</c:if>
								
							</c:when>
							
							
							<c:when test = "${param.ldapAccount==false}">
								<p><span style="font-weight:bold">Note: </span>An LDAP account could not be automatically generated for the new employee.  The most likely cause is that the intended user name has already been assigned to another employee.</p>
								<c:if test = "${not empty param.emailServiceDeskSent && fn:length(param.emailServiceDeskSent) > 2}">
									<p>A Service Desk ticket ${param.emailServiceDeskSent} has been generated notifying IT that the account must be manually created.</p>													
									<c:if test="${param.employeeType == 'workstudy'}">				
										<p>Upon account creation, the standard supervisor email will be generated.  <span style="font-weight:bold">No further action is required from HR regarding this request.</span></p>						
									</c:if>
									<!-- The following is output only when the employee data is stored in pmp db ie for the employeeTypes staff, faculty and student -->
									<c:if test="${param.employeeType == 'staff' || param.employeeType == 'faculty' || param.employeeType == 'student'}">											
										<p>Upon account creation, the submitted user information will be updated in the PMP database and the standard supervisor email will be generated.  <span style="font-weight:bold">No further action is required from HR regarding this request.</span></p>
									</c:if>
								</c:if>
								<c:if test="${not empty param.emailServiceDeskFailed && fn:length(param.emailServiceDeskFailed) > 2}">		
									 <li><font color="red">FAILED to generate the ServiceDesk ticket ${param.emailServiceDeskFailed} notifying IT that the account must be manually created.</font></li>
									 <p>Please contact the Service Desk at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a> or 947-6222 for assitance.</p>						
								</c:if>	
							</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
						
						</ul>
						</c:if>
						
						
						<c:if test="${param.requestType=='2'}">
							<h2>Success - Transfer</h2>
							<table>
								<tr>
									<td><label>Name: </label>${param.employeeName}</td>
									<td></td>
								</tr>
								<tr>
									<td><label>New Title: </label>${param.title}</td>
									<td></td>
								</tr>
								<tr>
									<td><label>New Supervisor: </label>${param.supervisorName}</td>
									<td></td>
								</tr>
								<tr>
									<td><label>New Department: </label>${param.departmentName}</td>
									<td></td>
								</tr>
								<tr>
									<td><label>Effective Date: </label>${param.effectiveDate}</td>
									<td></td>
								</tr>
							</table>
							<ul>
								<c:if test= "${param.employeeType == 'staff'  || param.employeeType == 'faculty' || param.employeeType=='student'}"> 
									<li>The employee's information will be updated in the PMP database on the submitted effective date.</li>								 
								</c:if>
								<c:if test="${not empty param.emailSupervisorSent}">								
									<li>An email has been sent to the employee's new supervisor ${param.supervisorName} (${param.emailSupervisorSent}) with instructions to fill out the Move/Add/Change form.</li>
								</c:if>
								<c:if test="${not empty param.emailSupervisorFailed}">									
									<li><font color="red">FAILED to send an email to employee's new supervisor ${param.supervisorName} (${param.emailSupervisorFailed}) with instructions to fill out the Move/Add/Change form.</font></li>
									<p>Please contact the Service Desk at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a> or 947-6222 for assitance.</p>													
								</c:if>
								<c:if test="${not empty param.emailOldSupervisorSent}">								
									<li>An email has been sent to the employee's Old supervisor ${param.oldSupervisorName} (${param.emailOldSupervisorSent}).</li>
								</c:if>
								<c:if test="${not empty param.emailOldSupervisorFailed}">									
									<li><font color="red">FAILED to send an email to employee's Old supervisor ${param.oldSupervisorName} (${param.emailOldSupervisorFailed}).</font></li>
									<p>Please contact the Service Desk at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a> or 947-6222 for assitance.</p>													
								</c:if>
							</ul>											
						</c:if>		
						
						<c:if test="${param.requestType=='3'}">
		
							<h2>Success - Termination</h2>
							<table>
								<tr>
									<td><label>Name: </label>${param.employeeName}</td>
									<td></td>
								</tr>
								<tr>
									<td><label>Effective Date: </label>${param.effectiveDate}</td>
									<td></td>
								</tr>
								<tr>
									<td><label>Comments: </label>${param.comments}</td>
									<td></td>
								</tr>
							</table>
							<ul>
								<c:if test="${param.existsInDB == true}">
									<li>The employee's status will be changed to inactive in the PMP database on the submitted effective date.</li>
										<c:if test="${not empty param.emailSupervisorSent}">
											<li>An email has been sent to the employee's supervisor ${param.supervisorName} (${param.emailSupervisorSent}) containing notification of the termination.</li>
										</c:if>
										<c:if test="${not empty param.emailSupervisorFailed}">
											<li><font color="red">FAILED to send an email to employee's supervisor ${param.supervisorName} (${param.emailSupervisorFailed}) containing notification of the termination.</font></li>
											<p>Please contact the Service Desk at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a> or 947-6222 for assitance.</p>																	
										</c:if>
								</c:if>
								<c:if test="${not empty param.emailServiceDeskSent && fn:length(param.emailServiceDeskSent) > 2}"> 
									<li>Following Service Desk tickets have been generated notifying IT of the employee's termination.
										<ul>								
										<c:forTokens var="emailSent" items="${param.emailServiceDeskSent}" delims=",[]-">
											<li>${emailSent}</li>
										</c:forTokens>									
										</ul>	
									</li>
								</c:if>
								<c:if test="${not empty param.emailServiceDeskFailed && fn:length(param.emailServiceDeskFailed) > 2}"> 
									<li><font color="red">FAILED to generate the Service Desk tickets:
									<ul>
									<c:forTokens var="emailFailed" items="${param.emailServiceDeskFailed}" delims=",[]-">
										<li>${emailFailed}</li>
									</c:forTokens>
									</ul>
									</font></li>
									 <p>Please contact the Service Desk at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a> or 947-6222 for assitance.</p>	
								</c:if>			
							</ul>				
						</c:if>
						</c:otherwise>
						</c:choose>
				</fieldset>
			<div style="clear: both"></div>
		</form>
	</div>
</body>
</html>
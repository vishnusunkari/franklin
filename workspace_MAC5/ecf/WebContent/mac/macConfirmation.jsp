<%@ page contentType="text/html" %>

<!-- following uri is for webapp2.5 that needs to be modified in web.xml 
     Need when you upgrade to tomcat6 -->
<!-- % @ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" % -->

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
	<head>
		<link rel="stylesheet" type="text/css" href="css/index.css" />
		<link rel="stylesheet" type="text/css" href="js/jquery/theme/ui.all.css" />		
		<title>MAC Confirmation Page</title>
	</head>
	<body>
		<div id="container">
			<div id="header">
				<h1>Employee Move/Add/Change Request</h1>
			</div>
			<div style="margin: 4px 20px; text-align: right;">
				<a href='../<c:url value="j_spring_security_logout"/>' style='font-size: 11px; margin:0; text-decoration:none; color: #396e8e;'>Logout</a>
			</div>
		<form>
			<fieldset id="typeSection">	
			<c:choose>
				<c:when test="${param.employeeTypeNull == true || param.exception == true || param.dataAccessException == true}">
					<c:if test="${param.employeeTypeNull == true}">
						<h2>Error</h2>
						<font color="red"><p>The form has FAILED to submit.</p></font>  
							<p>Please make sure the link within your email is not broken.  If it is broken, try to copy
							   the entire link manually from your email onto your browser and then submit the form.
							  If the problem still persists please contact the Help Desk 
							  at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a>
							   or 947-6222 for assitance.</p>				
					</c:if>
					<c:if test="${param.exception == true}">
						<h2>Error</h2>
						<p>The form has encountered an error and is unable to complete your request.</p>
						<p>Please contact the Help Desk at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a> or 947-6222 for assitance.</p>							
					</c:if>
					<c:if test="${param.dataAccessException == true}">										
						<h2>Error Database</h2>
						<p>The form has encountered an error while trying to access the <span style="font-weight:bold">Database</span> and is unable to complete your request.</p>
						<p>Please contact the Help Desk at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a> or 947-6222 for assitance.</p>
					</c:if>
				</c:when>
				<c:otherwise>									
					<h2>Success</h2>
					<ul>
					<c:if test="${param.updateDatabase == true}">
						<li>The employee's information has been updated in the PMP database and will be reflected in the Phone Roster within one business day.</li>
					</c:if>						
					<c:if test="${not empty param.emailServiceDeskSent && fn:length(param.emailServiceDeskSent) > 2}"> 																			
						<li>Following Service Desk tickets have been generated notifying the appropriate departments of the employee's requirements.
							<ul>
							<c:forTokens var="emailSent" items="${param.emailServiceDeskSent}" delims=",[]">
								<li>${emailSent}</li>
							</c:forTokens>
							</ul>
						</li>
					</c:if>
					<c:if test="${not empty param.emailServiceDeskFailed && fn:length(param.emailServiceDeskFailed) > 2}"> 
						<li><font color="red">FAILED to generate the ServiceDesk tickets: 
							<ul>							
							<c:forTokens var="emailFailed" items="${param.emailServiceDeskFailed}" delims=",[]">
								<li>${emailFailed}</li>
							</c:forTokens>																		
							</ul>
							</font>
						</li>	
						<p>Please contact the Service Desk at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a> or 947-6222 for assitance.</p>						
					</c:if>
					</ul>
				</c:otherwise>
			</c:choose>									
			</fieldset>
			</form>
			<table align="center" cellpadding="0" cellspacing="10" border="0"> 
				<tr> 
					<td><a href="http://www.franklin.edu/"><img src="images/franklinlogo.jpg" alt="Franklin University" border="0" width="120" height="47"></a></td> 
	        		<td><font size="-2">201 S.Grant Avenue, Columbus, Ohio 43215<br> 
	                        614-797-4700, toll free 1-877-341-6300<br> 
	                        &copy; 2006, Franklin University</font></td> 
					</tr> 
			</table> 
		</div>
	</body>
</html>


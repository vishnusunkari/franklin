<%@ page contentType="text/html" %>

<!-- following uri is for webapp2.5 that needs to be modified in web.xml 
     Need when you upgrade to tomcat6 -->
<!-- % @ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" % -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
  
<html>
<head>
	<link rel="stylesheet" type="text/css" href="css/index.css" />
</head>
<body>
	<div id="container">
		<div id="header">
			<h1>LDAP Account Creation</h1>
		</div>
		<div style="margin: 4px 20px; text-align: right;">
			<a href='../<c:url value="j_spring_security_logout"/>' style='font-size: 11px; margin:0; text-decoration:none; color: #396e8e;'>Logout</a>
		</div>
		<form>
			<fieldset>
						<!-- c:forEach items="${param.emailServiceDeskSent}" var="entry">
								<li>An email has been sent to the employee's supervisor <%//pageContext.getAttribute("entry").toString().replace("[","").replace("]","") %> with instructions to fill out the <br>Move/Add/Change form.</li>
							< / c:forEach -->
			<c:choose>
				<c:when test="${param.exception==true}">
					<h2>Error</h2>
					<p>The form has encountered an error and is unable to complete your request.</p>
					<p>Please contact the Help Desk at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a> or 947-6222 for assitance.</p>										
				</c:when>				
				<c:when test="${param.dataAccessException==true}">	
					<h2>Error</h2>
					<p>The form has encountered an error while trying to access the <span style="font-weight:bold">Database</span> and is unable to complete your request.</p>
					<p>Please contact the Help Desk at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a> or 947-6222 for assitance.</p>
				</c:when>	
				<c:when test="${param.existsActiveEmployee==true}">			
					<h2>Error - New Hire</h2>
					<p><font color="red">FAILED to add employee to Database.  There exists an active employee with the same email address in the Database.</font></p>				
					<p>Please contact the Service Desk at <a href="mailto:servicedesk@franklin.edu">servicedesk@franklin.edu</a> or 947-6222 for assitance.</p>																						
				</c:when>
				<c:otherwise>
					<c:if test="${param.requestType=='0'}">
						<h2>Success - New Hire</h2>
						<ul>
						<c:if test="${param.updateDatabase == true}">
							<li>The employee's information has been added to the PMP database and will be reflected in the Phone Roster within one business day.</li>
						</c:if>
						<c:if test="${not empty param.emailSupervisorSent}">										
							<li>An email has been sent to the employee's supervisor ${param.supervisorName} (${param.emailSupervisorSent}) with instructions to fill out the <br>Move/Add/Change form.</li>
						</c:if>		
						<c:if test="${not empty param.emailSupervisorFailed}">
							<li><font color="red">FAILED to send an email to employee's supervisor  ${param.supervisorName} (${param.emailSupervisorFailed}) with instructions to fill out the <br>Move/Add/Change form.</font></li>
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
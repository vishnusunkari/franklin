<%@ include file="/WEB-INF/taglibs.jsp"%>
<html>
<head>
	<script src="js/jquery/jquery-1.3.2.min.js"></script>
	<script src="js/jquery/jquery-ui-1.7.custom.min.js"></script>
	<script src="js/index.js"></script>
	<script src="js/validation/validation.js"></script>
	<link rel="stylesheet" type="text/css" href="css/index.css" />
	<link rel="stylesheet" type="text/css" href="js/jquery/theme/ui.all.css" />
	<link rel="stylesheet" type="text/css" href="css/buttons/buttons.css" />
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
			<h1>LDAP Account Creation</h1>
		</div>
		<div style="margin: 4px 20px; text-align: right;">
			<a href='<c:url value="j_spring_security_logout"/>' style='font-size: 11px; margin:0; text-decoration:none; color: #396e8e;'>Logout</a>
		</div>
		<form class="validate" style="margin-top: 0px" action="<c:url value="/delayedldap/delayedLDAP.html"/>" method="post" onsubmit="return validateEmployee(this)">
			<fieldset id="userInfo">			
				<h2>User Information</h2>
				<table>
					<tr>
						<input name="requestType" id="requestType" type="hidden"/>
						<td><label for="userName">User Name</label></td>
						<td><input type="text" name="userName" id="userName" /><br /><br /></td>
					</tr>
					<tr>
						<td><label for="firstName">First Name</label></td>
						<td><input type="text" name="firstName" id="firstName" readonly="readonly" /></td>
					</tr>
					<tr>
						<td><label for="lastName">Last Name</label></td>
						<td><input type="text" name="lastName" id="lastName" readonly="readonly" /></td>
					</tr>
					<tr>
						<td><label for="title">Title</label></td>
						<td><input type="text" name="title" id="title" readonly="readonly" /></td>
							<input id="positionId" name="positionId" type="hidden" />
					</tr>
					<tr>
						<td><label for="department">Department</label></td>
						<td><input type="text" name="department" id="department" readonly="readonly" /></td>
							<input id="departId" name="departId" type="hidden" />
					</tr>
					<tr>
						<td><label for="employeeType">Employee Type</label></td>
						<td><input type="text" name="employeeType" id="employeeType" readonly="readonly" /></td>
					</tr>	
					<tr>
						<td><label for="supervisor">Supervisor</label></td>
						<td><input type="text" name="supervisor" id="supervisor" readonly="readonly" /></td>
							<input id="supervisorId" name="supervisorId" type="hidden" />
							<input id="supervisorUserName" name="supervisorUserName" type="hidden" />
					</tr>
					<tr>
						<td><label for="cc">CC</label></td>
						<td>
							<input type="text" name="cc" id="cc" readonly="readonly" />
							<input id="ccUsername" name="ccUsername" type="hidden" />
						</td>
					</tr>
					<tr>
						<td><label for="effectiveDate">Effective Date</label></td>
						<td><input type="text" name="effectiveDate" id="effectiveDate" readonly="readonly" /></td>
						<input name="comments" id="comments" type="hidden"/>
					</tr>
				</table>
			</fieldset>
			<button type="submit" class="custom"><span class="submit">Submit Request</span></button>
		</form>
	</div>
</body>
</html>
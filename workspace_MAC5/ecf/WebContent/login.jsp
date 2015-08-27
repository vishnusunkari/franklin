<%@ page import="org.springframework.security.ui.webapp.AuthenticationProcessingFilter" %>
<%@ page import="org.springframework.security.ui.AbstractProcessingFilter" %>
<%@ page import="org.springframework.security.AuthenticationException" %>
<%@ taglib uri = "http://java.sun.com/jstl/core_rt" prefix = "c"%> 
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="css/index.css" />
		<link rel="stylesheet" type="text/css" href="js/jquery/theme/ui.all.css" />
		<link rel="stylesheet" type="text/css" href="css/buttons/buttons.css" />	
		<title>Login Page</title>
	</head>
	<body onload='document.f.j_username.focus();'>
		<div id="container">
			<div id="header">
				<h1>Move/Add/Change</h1>
			</div>	
			
			<c:if test="${not empty param.error}">
       			<font color="red">
         			<CENTER>Incorrect Username/Password<br/>
         			Please try again!</CENTER>  
         		</font>
     		</c:if>
			
		<form name="loginForm" action="j_spring_security_check" method="POST">
			<fieldset id="typeSection">		
			<h2>Login</h2>
			<table width="230px">
			<tr>
				<td><label for="j_username">Username:</label></td>
				<td><input type="text" name="j_username" id="j_username" /></td>
			</tr>
			<tr>
				<td><label for="j_password">Password:</label></td>
				<td><input type="password" name="j_password" id="j_password"/></td>
			</tr>				
			<br/>
			<tr>
				<td colspan="2" style="padding: 5px 0px 8px 0px">
					<input type='checkbox' id='_spring_security_remember_me' name='_spring_security_remember_me'/> <label for="_spring_security_remember_me" style="font-size: 11px; font-weight: normal;">Remember me on this computer.</label>
				</td>
			</tr>					
			<br/>
			<tr>
				<td colspan="2"><button type="submit" class="custom"><span class="login">Login</span></button></td>
			</tr>
			</table>
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
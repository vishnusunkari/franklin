<%@ page import="org.springframework.security.ui.webapp.AuthenticationProcessingFilter" %>
<%@ page import="org.springframework.security.ui.AbstractProcessingFilter" %>
<%@ page import="org.springframework.security.AuthenticationException" %>
 
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="css/index.css" />
		<link rel="stylesheet" type="text/css" href="js/jquery/theme/ui.all.css" />		
		<title>Logout Page</title>
	</head>
	<body>
		<div id="container">
			<div id="header">
				<h1>Move/Add/Change</h1>
			</div>
		<form>	
			<fieldset id="typeSection">	
				<h2>Logout</h2>
				<p>You have successfully logged out.</p>
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
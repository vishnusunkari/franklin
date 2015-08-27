<%@ include file="/WEB-INF/taglibs.jsp"%>

<title>MyUsers ~ User List</title>
<button onclick="location.href='admin/editEmployee.html'">Add User</button>
<table class="list">
<thead>
  <tr>
	<th>Employee Id</th>
	<th>First Name</th>
	<th>Last Name</th>
  </tr>
</thead>
<tbody>
<c:forEach var="employee" items="${employees}" varStatus="status">
<tr>
	<td>${employee.employeeId}</td>
	<td>${employee.firstName}</td>
	<td>${employee.lastName}</td>
</tr>
</c:forEach>
</tbody>
</table>
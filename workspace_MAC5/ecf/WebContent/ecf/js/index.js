//Stephen B. Schreiber 11/18/08//
//JQuery 1.2.6//

$(function(){
	//Assign datepicker contained in datePicker.js
	$("#effectiveDate").datepicker({
		nextText: '>',
		prevText: '<',
		dateFormat: 'DD, m/d/yy',
		showOn: "both",
    	buttonImage: "images/calendar.gif",
    	buttonImageOnly: true
	});
	
	//highlight field label on focus
	$("input:text,select,textarea").focus(function(){
		$(this).css("border-color","black");
	}).blur(function(){
		$(this).css("border-color","");
	});
	
	//show/hide sections
	$("#requestSection").css("display","none");
	$("#requestType").change(function(){
		$("#requestSection").fadeOut("normal",function(){
			if($("#requestType").val() == '1') {
				$("#newTitle, #newDepartment, #newEmployeeType, #employee, #newSupervisor, #comments").parent().parent().css("display","none");
				$("#title, #department, #supervisor, #employeeFirstName, #employeeLastName, #employeeType").parent().parent().css("display","");
				$("#requestHeading").text("New Hire");
			}
			if($("#requestType").val() == '2') {
				$("#title, #department, #employeeFirstName, #employeeLastName, #supervisor, #employeeType, #comments").parent().parent().css("display","none");
				$("#newTitle, #newDepartment, #newEmployeeType, #employee, #newSupervisor").parent().parent().css("display","");
				$("#requestHeading").text("Transfer");
			}
			if($("#requestType").val() == '3') {
				$("#newTitle, #newDepartment, #newEmployeeType, #title, #department, #supervisor, #newSupervisor, #employeeFirstName, #employeeLastName, #employeeType").parent().parent().css("display","none");
				$("#employee, #comments").parent().parent().css("display","");
				$("#requestHeading").text("Termination");
			}
			if($("#requestType").val()) {
				$("#requestSection").fadeIn("normal");
			}
		});
	});
	
	//show/hide/populate supervisor info
	var acVars = {
		minChars: 1,
		width: 600,
		matchContains: true,
		max: 7,
		highlight: false,
		formatItem: function(row, i, max) {
			return "<img src='images/user.png' style='margin-right:2px;' /> <span style='font-weight: bold;'>" + row.n + "</span>&nbsp;&nbsp;--&nbsp;&nbsp;" + row.t + ", " + row.d + "&nbsp;&nbsp;<span class='gray'>(" + row.u + "@franklin.edu)</span>";
		},
		formatMatch: function(row, i, max) {
			return row.n;
		},
		formatResult: function(row) {
			return row.n;
		}
	};
	function verifySelection(e) {
		var d = $(this);
		if($("div.ac_results").css("display") == 'none') {
			if(d.val() != '') {
				d.search(function(result){
					if(!result) d.val("");
					d.change();
				});
			}
		}
	}
	$("#supervisor").autocomplete(employees, acVars).result(function(event, data, formatted){
		$(this).blur().focus();
		$("#sUsername").val(data.u);
		$("#department").val(data.d).change();
	}).blur(verifySelection);
	$("#newSupervisor").autocomplete(employees, acVars).result(function(event, data, formatted){
		$(this).blur().focus();
		$("#nsUsername").val(data.u);
		$("#newDepartment").val(data.d).change();
	}).blur(verifySelection);
	$("#employee").autocomplete(employees, acVars).result(function(event, data, formatted){
		$(this).blur().focus();
		$("#eUsername").val(data.u);
	}).blur(verifySelection);
	$("#cc").autocomplete(employees, acVars).result(function(event, data, formatted){
		$(this).blur().focus();
		$("#ccUsername").val(data.u);
	}).blur(verifySelection);
	
	var sup = new Array();
	for(var i = 0; i < employees.length; i++) {
		sup[i] = employees[i].t;
	}
	sup = sup.sort();
	for(var i = 0; i < sup.length; i++) {
		if(typeof sup[i] == "undefined" || sup[i] == sup[i-1]) {
			sup.splice(i,1);
			i = i-1;
		}
	}
	var sup2 = new Array();
	for(var i = 0; i < sup.length; i++) {
		sup2[i] = { t: sup[i] };
	}
	var acVars2 = {
		minChars: 1,
		width: 600,
		matchContains: true,
		max: 7,
		highlight: false,
		formatItem: function(row, i, max) {
			return row.t;
		},
		formatMatch: function(row, i, max) {
			return row.t;
		},
		formatResult: function(row) {
			return row.t;
		}
	};
	$("#title").autocomplete(sup2, acVars2).result(function(event, data, formatted){
		$(this).blur().focus();
		$("#title").val(data.t);
		$(this).change();
	});
	$("#newTitle").autocomplete(sup2, acVars2).result(function(event, data, formatted){
		$(this).blur().focus();
		$("#title").val(data.t);
		$(this).change();
	});
	
	//setInfo
	setInfo($("label[for='requestType']"),"<p style='margin-top:10px'>Submitting a <u>New Hire</u> request will: <ul><li>Generate an LDAP account for the new hire.</li><li>Create a new user record in the PMP database, and populate it with the new hire's information.</li><li>Generate an email containing the new hire's start date and a link to the Employee Move/Add/Change Form, and deliver it to the new hire's supervisor.</li></ul></p><p>Submitting a <u>Transfer</u> request will: <ul><li>Schedule the PMP database to update with the employee's new position information on the submitted effective date.</li><li>Generate an email containing the employee's transfer date and a link to the Employee Move/Add/Change Form, and deliver it to the employee's new supervisor.</li></ul></p><p>Submitting a <u>Termination</u> request will: <ul><li>Schedule the PMP database to update the employee's employment status to 'inactive' on the submitted effective date.</li><li>Generate and submit help desk tickets to the appropriate departments in IT.</li><li>Generate an email containing notification of the employee's termination, and deliver it to the employee's supervisor.</li></ul></p>",true);
	setInfo($("label[for='cc']"),"Upon form submission, an email will automatically be sent to the employee's current and/or future supervisor.  Please enter a name into this field ONLY if you would like to add an additional recipient to the email already being generated (in most cases this field will be left blank).",true);
	//validate
	setValidation("requestType","Please select a request type.");
	setValidation("effectiveDate","Please enter the effective date.");
	setValidation("employeeFirstName","Please enter the employee's first name.",false,"$('#requestType').val() == '1'");
	setValidation("employeeLastName","Please enter the employee's last name.",false,"$('#requestType').val() == '1'");
	setValidation("department","Please enter the employee's department.",false,"$('#requestType').val() == '1'");
	setValidation("employeeType","Please select the employee type.",false,"$('#requestType').val() == '1'");
	setValidation("title","Please enter the employee's title.",false,"$('#requestType').val() == '1'");
	setValidation("supervisor","Please select a supervisor from the drop-down list.",false,"$('#requestType').val() == '1'");
	setValidation("employee","Please select an employee from the drop-down list.",false,"$('#requestType').val() != '1'");
	setValidation("newDepartment","Please enter the employee's new department.",false,"$('#requestType').val() == '2'");
	setValidation("newEmployeeType","Please select the employee type.",false,"$('#requestType').val() == '2'");
	setValidation("newSupervisor","Please select a new supervisor from the drop-down list.",false,"$('#requestType').val() == '2'");
	setValidation("newTitle","Please enter the employee's new title.",false,"$('#requestType').val() == '2'");
});
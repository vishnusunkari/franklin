//Stephen B. Schreiber 11/18/08//
//JQuery 1.2.6//

$(function(){
	
	//populate fields from URL params
	var url = window.location.href;
	if(url.indexOf("?") > -1) {
		var params = url.substr(url.indexOf("?") + 1).split("&");
		for(var i = 0; i < params.length; i++) {
			$("#" + params[i].split("=")[0]).val(decodeURI(params[i].split("=")[1]).replace(/%26/,"&"));
			if(params[i].split("=")[0] == "supervisorID") sID = decodeURI(params[i].split("=")[1]).replace(/%26/,"&");
			if(params[i].split("=")[0] == "employeeTitle") eTitle = decodeURI(params[i].split("=")[1]).replace(/%26/,"&");
			if(params[i].split("=")[0] == "employeeDepartment") eDepartment = decodeURI(params[i].split("=")[1]).replace(/%26/,"&");
		}
	}
	
	//DEBUG -- remove for final version
	if($("#requestType").val().match(/test/)) $("input:hidden").each(function(){
		this.type = 'text';
	});
	
	//Assign datepicker contained in datePicker.js
	var year = $("#date").val().substr(4,4);
	var month = $("#date").val().substr(0,2);
	var day = $("#date").val().substr(2,2);
	$("#effectiveDate").datepicker({
		nextText: '>',
		prevText: '<',
		dateFormat: 'DD, m/d/yy',
		showOn: "both",
    	buttonImage: "images/calendar.gif",
    	buttonImageOnly: true,
    	altField: '#date',
    	altFormat: 'mmddyy'
	})
	if($("#date").val().match(/^\d{8}$/)) $("#effectiveDate").datepicker('setDate', new Date(year,month-1,day));
	
	//highlight input on focus
	$("input:text,select,textarea").focus(function(){
		$(this).css("border-color","black");
	}).blur(function(){
		$(this).css("border-color","");
	});
	
	//show/hide form sections
	$("div[id*='SubMenu']").filter(function(){
		return !$("#" + this.id.replace(/SubMenu/,''))[0].checked
	}).css("display","none");
	$(":checkbox").click(function(){
		this.checked ? $("#" + this.id + "SubMenu").slideDown("fast") : $("#" + this.id + "SubMenu").slideUp("fast");
	});
	$("#specialDiv, #titleDiv, #sizeDiv").css("margin-left","10px").hide();
	$("#employeeRoomNumber0").css("display","");
	
	//equalize and float boxes
	var s = $("#requestInfo");
	var e = $("#directoryInfo");
	s.height() > e.height() ? e.height(s.height()) : s.height(e.height());
	s.width(s.parent().width() / 2 - 32).css({"float":"left","margin-right":"20px"})
	e.width(e.parent().width() / 2 - 32);
	$("#changeRequest").css("clear","both");
	
	//conditional, New Employee
	if($("#requestType").val().match(/new/)) {
		$("#access").attr("checked","checked")
		$("#accessSubMenu").css("display","");
		$("#nameplate").attr("checked","checked")
		$("#nameplateSubMenu").css("display","");
		$("#accessCard").attr("checked","checked")
		$("#accessCardSubMenu").css("display","");
	}
	
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
	$("#employeeName").autocomplete(employees, acVars).result(function(event, data, formatted){
		if(typeof data != 'undefined') {
			$(this).blur().focus();
			$("#employeeID").val(data.u).change();
			if(typeof eTitle != "undefined") $("#employeeTitle").val(eTitle).change();
			else if(typeof data.t != "undefined") $("#employeeTitle").val(data.t).change();
			if(typeof eDepartment != "undefined") $("#employeeDepartment").val(eDepartment).change();
			else if(typeof data.d != "undefined") $("#employeeDepartment").val(data.d).change();
			if(typeof data.b != "undefined") $("#employeeBuilding, #currentEmployeeBuilding, #changedEmployeeBuilding").val(data.b).change();
			if(typeof data.r != "undefined") $("#employeeRoomNumber" + $("#employeeBuilding")[0].selectedIndex).val(data.r).change();
			if(typeof data.r != "undefined") $("#currentEmployeeRoomNumber, #changedEmployeeRoomNumber").val(data.r).change();
			if(typeof data.e != "undefined") $("#employeePhone, #currentEmployeePhone").val(data.e.replace(/^.*-/,"")).change();
			if(typeof sID != "undefined") {
				$.each(employees, function(i,val) {
					if(val.u.toLowerCase() == sID.toLowerCase()) $("#supervisorName").val(val.n).search();
				});
			}
			else if(typeof data.s != "undefined") $("#supervisorName").val(data.s).change().search();
		}
		else {
			setTimeout("$('#employeeName').search()",50);
		}
		
	}).blur(verifySelection);
	$("#supervisorName").autocomplete(employees, acVars).result(function(event, data, formatted){
		if(typeof data != 'undefined') {
			if(typeof data.u != "undefined") $("#supervisorID").val(data.u).change();
			if(typeof data.t != "undefined") $("#supervisorTitle").val(data.t).change();
			if(typeof data.d != "undefined") $("#supervisorDepartment").val(data.d).change();
			if(typeof data.b != "undefined") $("#supervisorBuilding").val(data.b).change();
			if(typeof data.r != "undefined") $("#supervisorRoomNumber").val(data.r).change();
			if(typeof data.e != "undefined") $("#supervisorPhone").val(data.e.replace(/^.*-/,"")).change();
		}
		else {
			$("#supervisorID").val('').change();
			$("#supervisorTitle").val('').change();
			$("#supervisorDepartment").val('').change();
			$("#supervisorBuilding").val('').change();
			$("#supervisorRoomNumber").val('').change();
			$("#supervisorPhone").val('').change();
		}
	}).blur(verifySelection);
	
	//populate access
	var acVars2 = {
			minChars: 1,
			width: 600,
			matchContains: true,
			max: 7,
			highlight: false,
			formatItem: function(row, i, max) {
				return "<img src='images/user.png' style='margin-right:2px;' /> <span style='font-weight: bold;'>" + row.n + "</span>&nbsp;&nbsp;--&nbsp;&nbsp;" + row.t + ", " + row.d + "&nbsp;&nbsp;<span class='gray'>(" + row.u + "@franklin.edu)</span>";
			},
			formatMatch: function(row, i, max) {
				return row.n + row.u;
			},
			formatResult: function(row) {
				return row.n + " (" + row.u + ")";
			}
		};
	$("#specifyNetworkAccess").autocomplete(employees, acVars2);
	$("#specifyDatatelAccess").autocomplete(employees, acVars2);
	$("#specifyStarrsAccess").autocomplete(employees, acVars2);
	$("#specifyPrinterAccess").autocomplete(employees, acVars2);
	
	//if employeeID is defined, populate based on that
	$.each(employees, function(i,val) {
		if(val.u.toLowerCase() == $("#employeeID").val().toLowerCase()) $("#employeeName").attr("readonly","readonly").val(val.n).search();
	});
	
	//populate nameplate text based on employee name
	$("#employeeName").change(function(e){
		$("#nameplateText").val($("#employeeName").val()).change();
	});
	$("#nameplateType").change(function(e){
		if($(this).val() == 'desk') $("#nameplateColor").val("woodgrain").attr("disabled","disabled");
		else $("#nameplateColor").val("woodgrain").removeAttr("disabled");
		if($(this).val() != 'special') $("#specialDiv").fadeOut("fast");
		else $("#specialDiv").fadeIn("fast");
		if($(this).val() == 'metal' || $(this).val() == 'wooden' || $(this).val() == 'cubicle') $("#nameplateSize").removeAttr("disabled");
		else $("#nameplateSize").val("no").attr("disabled","disabled").change();
	});
	$("#nameplateTitle").change(function(e){
		if($(this).val() != 'yes') $("#titleDiv").fadeOut("fast");
		else $("#titleDiv").fadeIn("fast");
	});
	$("#nameplateSize").change(function(e){
		if($(this).val() != 'yes') $("#sizeDiv").fadeOut("fast");
		else $("#sizeDiv").fadeIn("fast");
	});
	$("#employeeTitle").change(function(e){
		$("#titleText").val($(this).val());
	});
	
	//populate moving instructions based on directory info
	$("#moveRequirements").focus(function(e){ $(this).addClass("focused") });
	$("#employeeBuilding, select[id*='employeeRoomNumber']").change(function(e){
		if($("#moveRequirements").hasClass("focused") == false && $("#currentEmployeeBuilding").val() != '' && $("#currentEmployeeRoomNumber").val() != '') {
			if($("#employeeBuilding").val() != $("#currentEmployeeBuilding").val() || $("#employeeRoomNumber").val() != $("#currentEmployeeRoomNumber").val()) {
				$("#moveRequirements").val("Please move the following items from " + $('#currentEmployeeBuilding').val() + " " + $('#currentEmployeeRoomNumber').val() + " to " + $('#employeeBuilding option:selected').text() + " " + $('#employeeRoomNumber' + $('#employeeBuilding')[0].selectedIndex + ' option:selected').text() + ":")
			}
		}
	});
	
	//focus first field on page ready
	$("input:first").focus();
	
	//validate
	setValidation("employeeName","Please enter the employee's name.");
	setValidation("supervisorName","Please enter the supervisor's name.");
	setValidation("employeeBuilding","Please enter the employee's building.");
	setValidation("employeeRoomNumber","Please enter the employee's room number.",false,"$('#employeeBuilding').val() != 'Unknown'");
	$('#employeeBuilding').change(function(e){
		if($("#employeeRoomNumber" + $("#employeeBuilding")[0].selectedIndex)[0].wasFocused) $("#employeeRoomNumber" + $("#employeeBuilding")[0].selectedIndex).change();
	});
	setValidation("effectiveDate","Please enter the date by which the request must be implemented.");
	setValidation("employeePhone","If there is an existing phone for this employee, please enter the 4-digit extension.",/^\d{4}$/,"$('#existingPhone').val() == 'yes'");
	$('#existingPhone').change(function(e){
		if($("#employeePhone")[0].wasFocused) $("#employeePhone").change();
	});
	setValidation("specifyNetworkAccess","Please specify the required network access.",false,"$('#networkAccess')[0].checked")
	setValidation("specifyDatatelAccess","Please specify the required Datatel access.",false,"$('#datatelAccess')[0].checked")
	setValidation("specifyStarrsAccess","Please specify the required STARRS access.",false,"$('#starrsAccess')[0].checked")
	setValidation("specifyPrinterAccess","Please specify the required printer access.",false,"$('#printerAccess')[0].checked")
	setValidation("specifyFileAccess","Please specify the required file access.",false,"$('#fileAccess')[0].checked")
	setValidation("nameplateText","Please specify the nameplate text.",false,"$('#nameplate')[0].checked")
	//set info
	setInfo($("label[for='effectiveDate']"),"The request should be implemented by this date (frequently the date of hire or transfer).",true);
	setInfo($("label[for='hardwareSoftware']"),"The employee needs equipment such as a computer or monitor added, removed, or replaced, or needs software such as Microsoft Office or Adobe Professional installed or uninstalled.",true);
	setInfo($("label[for='access']"),"The employee needs access to resources such as Datatel and network drives granted or denied.",true);
	setInfo($("label[for='networkAccess']"),"This will allow the user to view shared drives and access network printers.",true);
	setInfo($("label[for='fileAccess']"),"e.g. 'Z:\MyData, read', 'Z:\MyData2, modify(delete)', 'X:\MyApp, read/write'",true);
	setInfo($("label[for='accessCardType']"),"e.g. 'Earlybird access to Fisher 169'",true);
	setInfo($("label[for='keys']"),"e.g. 'Phillips Hall, Room 221'",true);
	setInfo($("label[for='nameplateSize']"),"Maximum: 9.5 x 14 inches",true);
});
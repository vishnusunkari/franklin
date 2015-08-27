//Stephen B. Schreiber 11/18/08//
//JQuery 1.2.6//

$(function(){
	//populate fields from URL params
	var url = window.location.href;
	if(url.indexOf("?") > -1) {
		var params = url.substr(url.indexOf("?") + 1).split("&");
		for(var i = 0; i < params.length; i++) {
			$("#" + params[i].split("=")[0]).val(decodeURI(params[i].split("=")[1]).replace(/%26/,"&"));
		}
	}
	
	//validate
	setValidation("userName","Please enter the Username of the employee.");
});



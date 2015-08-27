/*

FORM VALIDATOR
Stephen B. Schreiber, 8/5/08
Requires JQuery 1.2.6+ (jquery.com)

1) Assign any forms to be validated class="validate"
2) To run a custom function onsubmit, assign it to <myForm>.customFunction
3) Set each field to validate with setValidation(node,msg,validator,required):
	node - the form element to be validated.  Accepts DOM node or ID string
	msg (optional, default null) - message to be displayed on field focus after failed validation.  Accepts string
	validator (optional, default false) - function to run validation against.  Accepts user defined function, regExp, or false (false validates against null value)
	required (optional, default true) - indicates whether field is required.  Accepts boolean or conditional function (which returns true/false) or code snippet (which evals to true/false)
		Example: setValidation("myField","Must start with a digit",/$\d/);
			--assigns validation to DOM element with id "myField"
			--validation function is a regular expression that requires the first character to be a digit
			--if the value does not validate onchange, the field is set to invalid and the message "Must start with a digit" is displayed on field focus
			--since the 'required' boolean was left out, required defaults to true and a blank value does not validate

All validatable fields are initially set to isValid=false and validated live onchange.  Focused invalid fields show absolutely positioned error message.
On form submission, if any form elements are !isValid they are highlighted and the first invalid field is focused.

*/

//get folder path, needed to pull images
var path = $("script").filter(function(){ return this.src.match(/.*validation\/validation.js/)}).attr("src").replace(/validation.js/,"");

$(function() {
	$("form.validate").each(function(){
		this.isValid = false;
		$(this).submit(validateForm);
	});
});

function setValidation(node,msg,validator,required) {
	var field = (typeof node == "string") ? document.getElementById(node) : node;
	if(!field) return;
	//if validator is not set to false, determine whether it's a function or a regular expression and validate accordingly
	if(typeof validator != "undefined" && validator) var validationFunction = (String(validator).match(/^\//)) ? function (e) { e.target.value.match(validator) ? makeValid(e.target) : makeInvalid(e.target); } : validator;
	//if validator is not defined or set to false, set isRequired and validate against null value
	else var validationFunction = function(e) { (e.target.value == '') ? makeInvalid(e.target) : makeValid(e.target);};
	//set isRequired to true, false, or conditional
	field.isRequired = (typeof required != "undefined" && !required) ? false : (typeof required == "boolean" || typeof required == "undefined") ? true : required;
	//set error message if field does not validate
	field.msg = (typeof msg != 'undefined') ? msg : null;
	//set isValid to false if field has no content (if it has preloaded content it is assumed to be valid)
	field.isValid = (field.value == '') ? false : true;
	field.mustValidate = true;
	$(field).focus(isFocused);
	$(field).blur(isNotFocused).change(validationFunction);
}

function setInfo(node,msg,icon) {
	var field = (typeof node == "string") ? document.getElementById(node) : node;
	if(!field) return;
	//set icon
	if(typeof icon != "undefined" && icon) {
		$(field).after("<img src='" + path + "images/information.png' />");
		var field = $(field).next();
		$(field).css({"cursor":"pointer","padding":"0 5px 0 0"});
	}
	//set message
	field.msg = (typeof msg != 'undefined') ? msg : null;
	$(field).mouseover(function(){
		showInfo(field,"info");
	});
	$(field).mouseout(function(){
		hideInfo("info");
	});
}

function showInfo(node,type) {
	var pos = $(node).offset();
	$("<div id='" + type + "Message'>"+node.msg+"</div>").css({
		"position":"absolute",
		"top":pos.top-2,
		"left":pos.left+$(node).outerWidth()+10,
		"display":"none",
		"padding":"4px 8px",
		"background-color":"#ffffee",
		"background-image":"url(" + path + "images/bgr-box-trans.png)",
		"background-repeat":"repeat-x",
		"border":"1px solid black",
		"color":"black",
		"font-size":"11px",
		"font-weight":"bold",
		"max-width":($(window).width() - pos.left - 100) < 500 ? $(window).width() - pos.left - 100 : 500,
		"text-align":"left"
	}).appendTo("body").fadeIn("fast");
	$("<img id='" + type + "Image' src='" + path + "images/nub.gif' />").css({
		"position":"absolute",
		"top":pos.top+5,
		"left":pos.left+$(node).outerWidth()+1,
		"display":"none"
	}).appendTo("body").fadeIn("fast");
}

function hideInfo(type) {
	$("#" + type + "Message").remove();
	$("#" + type + "Image").remove();
}

function makeValid(node) {
	node.isValid = true;
	$(node).css("background-color","").removeClass("error errorFocused");
}

function makeInvalid(node) {
	node.isValid = false;
	if(node.isFocused) $(node).css("background-color","#ffff66").addClass("errorFocused").removeClass("error");
	else $(node).css("background-color","#f9f7ba").addClass("error").removeClass("errorFocused");
}

function isFocused(e) {
	var node = e.target;
	node.isFocused = true;
	tempVal = node.value;
	if($(node).hasClass("error")) {
		$(node).css("background-color","#ffff66").addClass("errorFocused").removeClass("error");
		if(node.msg) showInfo(node,"validation");
	}
}

function isNotFocused(e) {
	var node = e.target;
	node.isFocused = false;
	if(typeof tempVal != "undefined" && node.value != tempVal && !node.tagName.match(/select/i)) $(node).change();
	if($(node).hasClass("errorFocused")) $(node).css("background-color","#f9f7ba").addClass("error").removeClass("errorFocused");
	hideInfo("validation");
	if(node.isRequired == false && node.value == '') makeValid(node);
}

function validateForm(e) {
	//prevent form from submitting
	e.preventDefault();
	var fm = e.target;
	var el = fm.elements;
	var errorFields = [];
	//loop through form elements and validate one by one
	for(var i = 0; i < el.length; i++) {
		//if field is blank and not required, skip it
		if(!el[i].isRequired && el[i].value == '') {
			makeValid(el[i]);
			el[i].isValid = false;
			continue;
		}
		//if field is required conditionally, check condition--if false, skip it
		if(typeof el[i].isRequired != 'boolean' && typeof el[i].isRequired != 'undefined') {
			var result = (typeof el[i].isRequired == 'function') ? el[i].isRequired() : eval(el[i].isRequired);
			if(!result) {
				makeValid(el[i]);
				el[i].isValid = false;
				continue;
			}
		}
		//otherwise, if field is set to mustValidate, check validity and add invalid fields to errorFields array
		if(el[i].mustValidate) {
			if(!el[i].isValid) {
				makeInvalid(el[i]);
				errorFields.push(el[i]);
			}
		}
	}
	//if errorFields has any kids, highlight them all as errors and focus/select the first one
	if(errorFields[0]) {
		errorFields[0].focus();
		if(errorFields[0].value != '') errorFields[0].select();
	}
	//if a custom function has been defined for the form, run it, otherwise submit
	else (fm.customFunction) ? fm.customFunction(e) : fm.submit();
}
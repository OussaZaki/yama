function AjaxCall(DivName,path,form_name) {
var xmlhttp=null;
	if (window.XMLHttpRequest)
	{
		xmlhttp = new XMLHttpRequest( );
	} else if (window.ActiveXObject) {
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
xmlhttp.open("GET",path,true);
xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");	
var data = getFormParameters(form_name);
data = encodeURI(data);
xmlhttp.send(data);
if(document.getElementById(DivName) != null){
		document.getElementById(DivName).innerHTML=xmlhttp.responseText;
	}
}
function getFormParameters(form_name) {
      var getstr = "";
      for (var i = 0; i < document.forms[form_name].elements.length; i++) {
         if (document.forms[form_name].elements[i].tagName == "INPUT" || document.forms[form_name].elements[i].tagName == "HIDDENINPUT" ) {
            if (document.forms[form_name].elements[i].type == "text") {
               getstr += document.forms[form_name].elements[i].name + "=" + document.forms[form_name].elements[i].value + "&";
            }
            if (document.forms[form_name].elements[i].type == "password") {
               getstr += document.forms[form_name].elements[i].name + "=" + document.forms[form_name].elements[i].value + "&";
            }			
            if (document.forms[form_name].elements[i].type == "hidden") {
               getstr += document.forms[form_name].elements[i].name + "=" + document.forms[form_name].elements[i].value + "&";
            }
            if (document.forms[form_name].elements[i].type == "checkbox") {
               if (document.forms[form_name].elements[i].checked) {
                  getstr += document.forms[form_name].elements[i].name + "=" + document.forms[form_name].elements[i].value + "&";
               } else {
                  getstr += document.forms[form_name].elements[i].name + "=&";
               }
            }
            if (document.forms[form_name].elements[i].type == "radio") {
               if (document.forms[form_name].elements[i].checked) {
                  getstr += document.forms[form_name].elements[i].name + "=" + document.forms[form_name].elements[i].value + "&";
               }
            }
         }   
         if (document.forms[form_name].elements[i].tagName == "SELECT") {
            var sel = document.forms[form_name].elements[i];
		if (sel.selectedIndex != -1){
                getstr += sel.name + "=" + sel.options[sel.selectedIndex].value + "&";
            }

         }
         
      }
      return getstr;
}
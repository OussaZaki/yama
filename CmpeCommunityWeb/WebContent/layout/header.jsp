<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="Tables.UserTable" %>
<!DOCTYPE HTML>
<html lang="en-US">
<head>
<%@ page import="drivers.SearchDriver"%>
<meta charset="UTF-8">
	<title>Cmpe Community</title>
	<link rel="stylesheet" href="/CmpeCommunityWeb/css/bootstrap.min.css">
	<link rel="stylesheet" href="/CmpeCommunityWeb/css/bootstrap-tagmanager.css">
	<link rel="stylesheet" href="/CmpeCommunityWeb/css/main.css">
	<style type="text/css">
	body { padding-top: 60px; }
	.nav-tabs > li > a {
    border: 1px solid transparent;
    border-radius: 4px 4px 0 0;
    cursor: pointer;
    line-height: 20px;
    padding-bottom: 8px;
    padding-top: 8px;
	}
	.nav-tabs > .active > a, .nav-tabs > .active > a:hover {
    -moz-border-bottom-colors: none;
    -moz-border-left-colors: none;
    -moz-border-right-colors: none;
    -moz-border-top-colors: none;
    background-color: #FFFFFF;
    border-color: #DDDDDD #DDDDDD transparent;
    border-image: none;
    border-style: solid;
    border-width: 1px;
    color: #555555;
    cursor: pointer;
}
.item-category {
  font-weight:italic;
  color:#0088CC;
  float:right;
} 
	</style>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="/CmpeCommunityWeb/css/bootstrap-responsive.min.css">
	<script type="text/javascript" src="/CmpeCommunityWeb/js/jquery.min.js"></script>
	<script type="text/javascript" src="/CmpeCommunityWeb/js/main.js"></script>

    <title>Cmpe Community</title>
    <link rel="stylesheet" href="/CmpeCommunityWeb/css/bootstrap.min.css">
    <link rel="stylesheet" href="/CmpeCommunityWeb/css/bootstrap-tagmanager.css">
    <style type="text/css">
    body { padding-top: 60px; }
    
    </style>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/CmpeCommunityWeb/css/bootstrap-responsive.min.css">
    <script type="text/javascript" src="/CmpeCommunityWeb/js/jquery.min.js"></script>
    <script type="text/javascript" src="/CmpeCommunityWeb/js/main.js"></script>
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.9.2/themes/base/jquery-ui.css" />
    <script src="http://code.jquery.com/jquery-1.8.3.js"></script>
    <script src="http://code.jquery.com/ui/1.9.2/jquery-ui.js"></script>
	<script src="/CmpeCommunityWeb/js/jquery-ui-timepicker-addon.js"></script>
	<script src="/CmpeCommunityWeb/js/bootstrap.js"></script>
    <style>
    .ui-autocomplete-category {
        font-weight: bold;
        padding: .2em .4em;
        margin: .8em 0 .2em;
        line-height: 1.5;
    }
    input.span3, textarea.span3, .uneditable-input.span3 {
    width: 290px;
    }
    </style>
    <script>
    $.widget( "custom.catcomplete", $.ui.autocomplete, {
    	  _renderMenu: function( ul, items ) {
    	   var self = this,
    	    currentCategory = "";
    	   $.each( items, function( index, item ) {
    		   if (item.category != currentCategory) {
    	     //ul.append( "<i>" + item.category + "</i>" );
    	     currentCategory = item.category;
    		   }
    	    self._renderItem( ul, item );
    	   });
    	  }
    	 });
    $(function() {
        var data = [
            <%=SearchDriver.getSearchAutoComplete()%>
            { value: "",label: " ", category: " " }
        ];
 
        $( "#search" ).catcomplete({
            delay: 0,
            source: data,
            select: function(event,ui) {
            	window.location.href=ui.item.value;
            	$("#search").val(ui.item.label);
            	return false;
            }
        }).data("catcomplete")._renderItem = function (ul, item) {
        	var MyHtml = "<a>" + "<div class='ac' >" +
            "<div>" +
            "<span>" + item.label + "</span>" +
            "<span class='item-category'><i>" + item.category + "</i></span>" +
            "</div>" +
            "</div>" + "</a>";
return $("<li></li>").data("item.autocomplete", item).append(MyHtml).appendTo(ul);
};
    });
    </script>
</head>
<body>
<%UserTable user=(UserTable)request.getAttribute("user");%>
<%UserTable[] recommendedUsers=(UserTable[])request.getAttribute("recommendUsers");%>

    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container">
                <a href="/CmpeCommunityWeb/Profile/cloud" class="brand">Cmpe Community</a>
                <div class="nav-collapse">
                    <ul class="nav">
                        <li><a href="/CmpeCommunityWeb/Profile">Home</a></li>
                        <li><a href="/CmpeCommunityWeb/Forum">Forum</a></li>
                    </ul>

                    <form action="#" class="navbar-search">
                        <input id="search" type="text" placeholder="Search" class="search-query span3"
                            name="q" value="" /> <input type="hidden" name="scope"
                            id="autocomplete" value="posts">
                    </form>
                    <ul class="nav pull-right">
                        <li><a href="/CmpeCommunityWeb/Profile/edit">Account</a></li>
                        <li><a href="/CmpeCommunityWeb/User/logout">Logout</a></li>
                    </ul>
                </div>
                
            </div>
        </div>
    </div>
    <div class="container offset1">
    <div class="span3">
        <table class="table table-hover">
            <thead>
                <tr>
                    <th><img src="/CmpeCommunityWeb/img/minions.jpg"></th>
                </tr>
                <tr>
                    <th class="well"><%=user.getName()%></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>
                        <a href="/CmpeCommunityWeb/Profile/tags/<%=user.getId()%>">Tags</a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <a href="/CmpeCommunityWeb/Profile/event/<%=user.getId()%>">Events</a>
                    </td>
                </tr>
                <tr class="text-info">
                    <td><a href="/CmpeCommunityWeb/Profile/survey/<%=user.getId()%>">Surveys</a></td>
                </tr>
            
            </tbody>
        </table>
        
        <table class="table table-hover" id='recommendationTable'>
            <thead>
                <tr class="success">
                    <th>Recommended Users</th>
                </tr>
            </thead>
            <tbody>
            <%if(recommendedUsers != null){ %>
            <%for(UserTable u: recommendedUsers){ %>
                <tr class="text-info">
                    <td><a class="text-success" href="/CmpeCommunityWeb/Profile/survey/<%=user.getId()%>">Surveys</a></td>
                </tr>
            <%} %>
            <%} %>
            </tbody>
        </table>        
        </div>
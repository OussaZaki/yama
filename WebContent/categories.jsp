<%@page import="drivers.SearchDriver"%>
<%@page import="drivers.ForumsDriver"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Tables.ForumPostTable"%>
<%@page import="Tables.UserTable"%>
<%@page import="Tables.ForumTopicTable"%>
<%@page import="java.util.Map"%>
<%@page import="Tables.ForumsTable"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>YAMA - You Ask, Moroccans Answer Forums</title>
<link rel="stylesheet" href="/YamaWeb/css/bootstrap.min.css">
<style>
body {
	padding-top: 60px;
	/* 60px to make the container go all the way to the bottom of the topbar */
}

.center-text{
	text-align: center !important;
}
</style>

    <link rel="stylesheet" href="http://code.jquery.com/ui/1.9.2/themes/base/jquery-ui.css" />
    <script src="http://code.jquery.com/jquery-1.8.3.js"></script>
    <script src="http://code.jquery.com/ui/1.9.2/jquery-ui.js"></script>
    <style>
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
  color:#dd6600;
  float:right;
} 
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
	<%
		ForumsTable[] categories = (ForumsTable[])request.getAttribute("categories");
		Map<Integer, ForumsTable[]> subForums = (Map<Integer, ForumsTable[]>)request.getAttribute("subForums");
		Map<Integer, ForumPostTable> posts = (Map<Integer, ForumPostTable>)request.getAttribute("posts");
		Map<Integer, UserTable> users = (Map<Integer, UserTable>)request.getAttribute("users");
	%>
		
    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container">
                <a href="/YamaWeb/Profile/cloud" class="brand">YAMA</a>
                <div class="nav-collapse">
                    <ul class="nav">
                        <li><a href="/YamaWeb/Profile">Home</a></li>
                        <li class="active"><a href="/YamaWeb/Forum">Forum</a></li>
                    </ul>

                    <form action="#" class="navbar-search">
                        <input id="search" type="text" placeholder="Search" class="search-query span3"
                            name="q" value="" /> <input type="hidden" name="scope"
                            id="autocomplete" value="posts">
                    </form>
                    <ul class="nav pull-right">
                        <li><a href="/YamaWeb/Profile/edit">Account</a></li>
                        <li><a href="/YamaWeb/User/logout">Logout</a></li>
                    </ul>
                </div>
                
            </div>
        </div>
    </div>


	<div class="container">
		<ul class="breadcrumb">
			<li><i class="icon-home"></i><a
				href="/YamaWeb/Forum"> Board Index</a></li>
		</ul>

		<%
			for(ForumsTable category: categories) {
		%>
		<div style="margin-left: 8px">
			<h4>
				<a href="<%= "Forum/index/" + category.getId()%>"><%= category.getName() %></a>
			</h4>
		</div>

		<table class="table table-hover table-bordered">
			<thead>
				<tr class="well">
					<th>Forums</th>
					<th class="span1 center-text">Topics</th>
					<th class="span1 center-text">Posts</th>
					<th class="span3">Last Post</th>
				</tr>
			</thead>
			<tbody>
			<% for(ForumsTable forum: subForums.get(category.getId())) { /*for(int j = 0; j < forums[i]; j++){*/ %>
			<tr>
				<td>
					<div><a href="<%= "Forum/index/" + forum.getId()%>"><strong><%= forum.getName() %></strong></a></div>
					<div><%= forum.getDescription() %></div>
					<% if(subForums.get(forum.getId())!=null && subForums.get(forum.getId()).length > 0){
						ForumsTable[] subForumsObjects = subForums.get(forum.getId());
					%>
						<div>
							<b>Subforums:</b>
							<% for(int i = 0; i < subForumsObjects.length; i++) {%> 
								<a href="<%= "Forum/index/" + subForumsObjects[i].getId()%>"><%= subForumsObjects[i].getName() %></a><%= (i == subForumsObjects.length - 1) ? "" : "," %>
							<% } %>
						</div>
					<% } %>
				</td>
				<td class="center-text"><%= forum.getTopicsCount() %></td>
				<td class="center-text"><%= forum.getPostsCount() %></td>
				<td class="span3">
				<% ForumPostTable p = posts.get(forum.getLastPostId());
 					if(p != null){
						UserTable u = users.get(p.getUserId());%>
					<div><small><a href="/YamaWeb/Profile/details/<%=u.getId()%>">
					<%=u.getName() %></a></small> <a href="/YamaWeb/Forum/topic/<%= p.getTopicId()%>"><i class="icon-play-circle" title="View the latest post"></i></a></div>
					<div><small><%=ForumsDriver.niceTime(p.getPostTime()) %></small></div>
					<%}else{%>
						No posts
					<% } %>
				</td>
			</tr>
			<% } %>
			</tbody>
		</table>
		<br />
		<%
			}
		%>

	</div>

	<script type="text/javascript" src="/YamaWeb/js/bootstrap.js"></script>
</body>
</html>
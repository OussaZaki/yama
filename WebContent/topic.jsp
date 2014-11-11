<%@page import="drivers.SearchDriver"%>
<%@page import="Tables.UserTable"%>
<%@page import="drivers.ForumsDriver"%>
<%@page import="Tables.ForumPostTable"%>
<%@page import="com.sun.org.apache.xpath.internal.FoundIndex"%>
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

.center-text {
	text-align: center !important;
}
</style>
<script type="text/javascript">
function stoppedTyping(text)
  {
    if (text.length > 0)
      document.getElementById("replybutton").disabled = false;
    else
      document.getElementById("replybutton").disabled = true;
  }
</script>

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
		<%
			ForumsTable forum = (ForumsTable) request
					.getAttribute("forum");
			ForumsTable[] parents = (ForumsTable[]) request.getAttribute("parents");
			ForumTopicTable topic = (ForumTopicTable)request.getAttribute("topic");
			ForumPostTable[] posts = (ForumPostTable[])request.getAttribute("posts");
			Map<Integer, UserTable> users = (Map<Integer, UserTable>)request.getAttribute("users");
			UserTable user = (UserTable)request.getAttribute("user");
		%>

		<ul class="breadcrumb">
			<li><i class="icon-home"></i><a href="/YamaWeb/Forum">
					Board Index</a></li>
			<%
				for (int i = parents.length - 1; i > 0; i--) {
			%>
			<li><span class="divider">/</span><a
				href=<%="/YamaWeb/Forum/index/" + parents[i].getId()%>>
					<%=parents[i].getName()%></a></li>
			<%
				}
			%>
			<li><span class="divider">/</span><a
				href=<%="/YamaWeb/Forum/index/" + forum.getId()%>>
					<%=forum.getName()%></a></li>
					
			<li class="active"><span class="divider">/</span><%=topic.getTitle() %></li>		
		</ul>

		<div style="margin-left: 8px">
			<h2><%=topic.getTitle() %></h2>
		</div>

		<br />

		<div class="row">
			<div class="span1">
				<img src="/YamaWeb/img/minions.jpg"/>
			</div>
			<div class="span10">
				<div style="margin: -10px 0px 5px 10px">
					<a href="/YamaWeb/Profile/details/<%=users.get(posts[0].getUserId()).getId()%>"><strong><%= users.get(posts[0].getUserId()).getName() %></strong></a> &raquo; <%= ForumsDriver.niceTime(posts[0].getPostTime()) %>
				</div>
				<p class="well"><%= posts[0].getContent() %></p>
			</div>
		</div>

		<h4>Replies</h4>
		<hr />
		<%for(int i = 1; i < posts.length; i++){ %>
		<div class="row">
			<div class="span1">
				<img src="/YamaWeb/img/minions.jpg" />
			</div>
			<div class="span10">
				<div style="margin: -10px 0px 5px 10px">
					<a href="/YamaWeb/Profile/details/<%=users.get(posts[0].getUserId()).getId()%>"><strong><%= users.get(posts[i].getUserId()).getName() %></strong></a> &raquo; <%= ForumsDriver.niceTime(posts[i].getPostTime()) %>
				</div>
				<p class="well"><%= posts[i].getContent() %></p>
			</div>
		</div>
		<br />
		<%} %>
		<%if(posts.length == 1){ %>
			<div>No replies yet</div>
		<%} %>
 		<br />
 		
		<div class="row">
			<form action="/YamaWeb/Forum/reply/<%= topic.getId() %>" method="post" class="span11">
				<fieldset>
					<legend>Post a new Reply</legend>

					<div class="span1" style="margin-left: 0px">
						<img src="/YamaWeb/img/minions.jpg" />
					</div>

					<div class="controls span10">
						<a style="margin-left: 10px" href="/YamaWeb/Profile/details/<%=user.getId()%>"><strong><%= user.getName() %></strong></a>
						<textarea onkeyup="stoppedTyping(this.value)" name="content" rows="5" class="span10"></textarea>
					</div>
					<div class="controls">
						<button id="replybutton" disabled="true" class="btn btn-info pull-right">
							Send Reply <i class="icon-chevron-right icon-white"></i>
						</button>
					</div>
				</fieldset>
			</form>
		</div>
	</div>

	<script type="text/javascript" src="/YamaWeb/js/bootstrap.js"></script>
</body>
</html>
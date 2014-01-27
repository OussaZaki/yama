<%@page import="Tables.UserTable"%>
<%
	UserTable user = (UserTable)request.getAttribute("user");
%>
<div style="margin-left: 30px">
			<ul class="nav span6 nav-tabs">
				<li><a onclick="Posts.loadNewsFeed(<%=user.getId()%>, this)" data-toggle="tab">News Feed</a></li>
				<li><a onclick="Posts.loadWall(<%=user.getId()%>, this)" data-toggle="tab">Wall</a></li>
			</ul>
	<div id="contentBody" class="span6"></div>
</div>
<script type="text/javascript">
<!--
	$(document).ready(function(){
		Posts.loadNewsFeed(<%=user.getId()%>, $('.nav-tabs li:first a'));
	});
//-->
</script>

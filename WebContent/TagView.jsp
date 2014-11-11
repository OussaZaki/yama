<%@page import="Tables.TagsTable"%>
<%@page import="Tables.UserTable"%>
<%@page import="Tables.TagsTable"%>
<%
	UserTable user = (UserTable)request.getAttribute("user");
	TagsTable tag = (TagsTable)request.getAttribute("tag");
%>
<div style="margin-left: 30px">
			<div >Tag Name:<%=tag.getTag()%></div>
			<ul class="nav span6 nav-tabs">
				<li><a href="#" onclick="Tags.loadWall(<%=tag.getId()%>)" data-toggle="tab">Wall</a></li>
				<li><a href="#" onclick="Tags.loadUsers(<%=tag.getId()%>)" data-toggle="tab">Users</a></li>
			</ul>
	<div id="contentBody" class="span6"></div>
</div>
<script type="text/javascript">
<!--
	$(document).ready(function(){
		Tags.loadWall(<%=tag.getId()%>);
	});
//-->
</script>

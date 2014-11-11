<%@page import="Tables.UserTable"%>
<%
	UserTable user = (UserTable)request.getAttribute("user");
%>
<div style="margin-left: 30px">
		<ul class="nav span6 nav-tabs">
			<li><a onclick="Events.loadMyEvents(<%=user.getId()%>)" data-toggle="tab">My events</a></li>
			<li class='active'><a onclick="Events.loadAttendedEvents(<%=user.getId()%>)" data-toggle="tab">Attended Events</a></li>
		</ul>
	<div id="contentBody" class="span6"></div>
</div>
<script type="text/javascript">
	$(document).ready(function(){
		Events.loadAttendedEvents('<%=user.getId()%>');
	});
</script>
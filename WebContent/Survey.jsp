<%@page import="Tables.UserTable"%>
<%
	UserTable user = (UserTable)request.getAttribute("user");
%>
<div style="margin-left: 30px">
			<ul class="nav span6 nav-tabs">
				<li><a onclick="Surveys.loadMySurveys(<%=user.getId()%>)" data-toggle="tab">My surveys</a></li>
				<li><a onclick="Surveys.loadCompletedSurveys(<%=user.getId()%>)" data-toggle="tab">Completed surveys</a></li>
			</ul>
	<div id="contentBody" class="span6"></div>
</div>
<script type="text/javascript">
	$(document).ready(function(){
		Surveys.loadMySurveys(<%=user.getId()%>);
	});
</script>
<%@page import="Tables.UserTable"%>
<%@page import="Tables.EventTable"%>

<% EventTable event = (EventTable)request.getAttribute("event"); %>
<% UserTable[] users = (UserTable[])request.getAttribute("users"); %>
<% UserTable creator = (UserTable)request.getAttribute("creator"); %>
<% boolean attending = (Boolean)request.getAttribute("attending"); %>

<h2><%=event.getPlace() %></h2>
<div class='span6'>
<% if(attending) { %>
	<button onclick='Events.unAttend(<%=event.getId() %>);' class='btn btn-info pull-right'>unAttend</button>
<% } else { %>
	<button onclick='Events.attend(<%=event.getId() %>);' class='btn btn-success pull-right'>Attend</button>
<% } %>
	<a href='/CmpeCommunityWeb/Profile/details/<%=creator.getId() %>'><strong><%=creator.getName() %></strong></a>
</div>
<div class='span6 clearfix' style='text-indent: 20px; margin-bottom: 50px;'>
	<% for(String p : event.getDescription().split("\n")) { %>
		<p><%=p %></p>
	<% } %>
	<hr />
	<h3>Attending:</h3>
	<% for(UserTable user : users){ %>
		<a href='/CmpeCommunityWeb/Profile/details/<%=user.getId() %>'><%=user.getName() %></a>
	<% } %>
</div>

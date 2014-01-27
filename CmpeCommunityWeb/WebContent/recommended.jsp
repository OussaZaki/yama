<%@ page import="Tables.UserTable" %>
<% UserTable[] users=(UserTable[])request.getAttribute("users"); %>

<% for(UserTable user : users){ %>
	<tr>
		<td>
			<a href='/CmpeCommunityWeb/Profile/details/<%=user.getId()%>'><%=user.getName()%></a>
		</td>
	</tr>
<% } %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="Tables.UserTable" %>

	<div class="nav span6 nav-tabs">
		<table class = "table table-zebra">
			<%UserTable[] users = (UserTable[])request.getAttribute("users");
			for(UserTable user: users) {%>
			<tr>
				<td>
					<a href="/CmpeCommunityWeb/Profile/details/<%=user.getId()%>"><%= user.getName()%></a>
				</td>
			</tr>	 
			<%}%>
		</table>
	</div>

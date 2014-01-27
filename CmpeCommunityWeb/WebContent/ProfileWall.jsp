
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="Tables.*" %>
<%UserTable user = (UserTable)request.getAttribute("user"); %>
<div class="row">
	<div class="input-prepend input-append" style='margin-left: 100px'>
		<span class="add-on">@<%=user.getName() %></span>
		<input class='input-xlarge' type="text" name="posts" placeholder="Enter your post"/>
		<button class="btn" type="button" onclick="Posts.createWithTaggedUserId(<%=user.getId()%>);">Post</button>
	</div>
</div>
<hr />
<%@include file='PostListView.jsp'%>
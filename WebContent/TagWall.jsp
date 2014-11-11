
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="Tables.TagsTable" %>

<% TagsTable tag = (TagsTable)request.getAttribute("tag"); %>
<% Integer tagId = (Integer)request.getAttribute("tag_id"); %>

<div class="row">
	<div class="input-prepend input-append" style='margin-left: 100px'>
		<span class="add-on">@<%=tag.getTag() %></span>
		<input class='input-xlarge' type="text" name="tag_post" placeholder="Enter your post"/>
		<button class="btn" type="button" onclick="Posts.createWithTaggedId(<%=tag.getId()%>);">Post</button>
	</div>
</div>
<hr/>
<%@include file='PostListView.jsp'%>

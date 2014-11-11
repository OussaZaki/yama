
<%@page import="Tables.TagsTable"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Set"%>
<%@page import="Tables.ReplyTable"%>
<%@ page import="Tables.PostsTable"%>
<%@ page import="Tables.UserTable"%>
<%@ page import="drivers.PostDriver"%>
<%@page import="java.util.Map"%>
<% Map<Integer, UserTable> users = (Map<Integer, UserTable>)request.getAttribute("users"); %>
<% Map<Integer, ReplyTable[]> replies = (Map<Integer, ReplyTable[]>)request.getAttribute("replies"); %>
<% Map<Integer, Object> tags = (Map<Integer, Object>)request.getAttribute("tagsInPosts"); %>

<div style="margin-left: 30px" id="postList">
	<%
			PostsTable[] posts = (PostsTable[])request.getAttribute("posts");
			for(PostsTable post: posts) {
	
	%>
	<div style="margin-left: 10px; margin-bottom: 10px;">
		<strong><a href="/YamaWeb/Profile/details/<%=((UserTable)users.get(post.getOwnerId())).getId()%>"><%=((UserTable)users.get(post.getOwnerId())).getName()%></a></strong>
		<%
			Object o = tags.get(post.getId());
			if(o!=null && o instanceof TagsTable){
		%>
			<span class="muted"> > </span><a href='/YamaWeb/Tags/details/<%=((TagsTable)o).getId() %>'><%=((TagsTable)o).getTag() %></a>
		<% }
			else if (o!=null && o instanceof UserTable){
		%>
			<span class="muted"> > </span><a href='/YamaWeb/Profile/details/<%=((UserTable)o).getId() %>'><%=((UserTable)o).getName() %></a>
		<% } %>
		 <%= post.getBody()%>
		<div class="pull-right muted">
			<%=PostDriver.niceTime(post.getPostingTime())%>
		</div>
	</div>
	<blockquote>
	<%
		ReplyTable[] postReplies = replies.get(post.getId());
		if(postReplies!=null && postReplies.length>0){
			for(ReplyTable r : postReplies){
	%>
	<p><small><a href="/YamaWeb/Profile/details/<%=((UserTable)users.get(r.getOwnerId())).getId()%>"><%=((UserTable)users.get(r.getOwnerId())).getName()%>: </a><%=r.getBody() %></small></p>
	<%
			}
		}
	%>
	</blockquote>
	<form name="reply_form" method="post" onsubmit="Reply.create(<%=post.getId()%>); return false;">
		<div class="controls">
			<input type="text" style="height: 10px; margin-left: 30px; font-size: 10px; line-height: 10px" name="reply_<%=post.getId()%>" placeholder="Reply...">
		</div>
	</form>
	<hr />
	<%} %>
</div>
<%@page import="java.util.Map"%>
<%@page import="Tables.EventTable"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="Tables.TagsTable"%>

<%@ page import="Tables.UserTable"%>

<div class="span6">
<%String type = (String)request.getAttribute("type"); %>
<%if (type.equals("myEvents")){ %>
	<div class="row span4" style='margin-bottom: 25px;'>
		<fieldset>
			<legend>Create Event</legend>
			<div class="controls">
				<input type="text" id="place" name="place" class="input-xlarge" placeholder="Place" />
				<input type="text" name="datetime" class="input-xlarge" placeholder="Date-Time" />
				<textarea id="description" name="description" class="input-xlarge" placeholder="Description"></textarea>
				<input type="button" class="btn btn-info pull-right" value="Create" onclick="Events.add()"/>
			</div>
		</fieldset>
	</div>
	<script>
		$(document).ready(function(){
			$('[name="datetime"]').datetimepicker({dateFormat: 'yy-mm-dd', timeFormat: 'hh:mm:ss'});
		});
	</script>
<%} %>
</div>

<% EventTable[] events = (EventTable[])request.getAttribute("events"); %>
<% Map<Integer, UserTable> users = (Map<Integer, UserTable>)request.getAttribute("users"); %>
<% UserTable user = (UserTable)request.getAttribute("user"); %>

<table class='table'>
<% for(EventTable event: events){ %>
	<tr>
		<td>
			<a href='/YamaWeb/Events/details/<%=event.getId() %>'><button class='btn btn-info pull-right'>Details</button></a>
			<strong>
				<a href='/YamaWeb/Profile/details/<%=event.getUserId()%>'><%=users.get(event.getUserId()).getName() %></a>
			</strong><br/>
			<strong>Place: </strong><%=event.getPlace() %><br/>
			<strong>Time: </strong><%=event.getEventTime() %>
		</td>
	</tr>
<% } %>
</table>

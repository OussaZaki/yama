
<%@page import="java.util.Set"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="Tables.TagsTable"%>
<%@ page import="Tables.UserTable"%>
<%@ page import="Tables.SurveyTable"%>
<%@ page import="Tables.ChoiceTable"%>
<%@ page import="drivers.SurveyDriver"%>
<%@ page import="java.util.ArrayList;"%>

<div class="nav span6 nav-tabs">
<%
	UserTable user = (UserTable)request.getAttribute("user");
	String type = (String)request.getAttribute("type"); 
%>
<%if (type.equals("mySurvey")){ %> 
	<div class="row" style='margin-bottom: 20px;'>
		<div>
				<fieldset>
					<input id="newquestion" type="text" placeholder="Question"
						class="span6" name="newquestion">
					<ul id="newchoices" style='list-style: none;'>
						<li>
							<div class="input-append input-prepend">
								<span class='add-on muted'>1</span>
								<input id="choice0" name="choice0" type="text"
									placeholder="Choice" class="span5">
								<button id="addchoice" class="btn btn-success" type="button">
									<i class="icon-plus icon-white"></i>
								</button>
							</div>
						</li>
					</ul>
					<div class="controls offset3">
						<input type="submit" onclick="Surveys.add(<%=user.getId()%>);" value="Create Survey" class="btn btn-info">
					</div>
				</fieldset>
		</div>
	</div>
<%} %>
	<script src="/CmpeCommunityWeb/js/bootstrap.js"></script>
	
<% SurveyTable[] surveyList = (SurveyTable[])request.getAttribute("surveyList"); %>
<% Set<Integer> joinedSurveys = (Set<Integer>)request.getAttribute("joinedSurveys"); %>

	<div class="accordion" id="accordionAll">

<% 
ChoiceTable[] choices=null;
for (int i=0;i<surveyList.length;i++) { %>

		<div class="accordion-group">
			<div class="accordion-heading">
				<a class="accordion-toggle" data-toggle="collapse"
					data-parent="#accordionAll" href="#collapse<%=i%>"> <%=surveyList[i].getQuestion() %> </a>
			</div>
			<div id="collapse<%=i%>" class="accordion-body collapse">
				<div class="accordion-inner" id="surveyContainer<%= surveyList[i].getId() %>">
				<% choices = surveyList[i].getChoiceTable();%>
				<% if(joinedSurveys.contains(surveyList[i].getId())){ %>
					<% for (int j=0;j<choices.length;j++) { %>
			            <div class="row-fluid">
							<div class="progress progress-warning span2">
								<div class="bar" style="width:<%=choices[j].getPercentageVotes()+"%" %>;">
									<p>&nbsp;<%=choices[j].getVotes() %></p>
								</div>
							</div>
							<div class="span5">
								<p><%=choices[j].getChoice() %></p>
							</div>
						</div>
		            <% } %>
	            <% } else { %>
					<% for (int j=0;j<choices.length;j++) { %>
						<label class="radio">
							<input type="radio" value="<%=choices[j].getId() %>" name="survey<%=surveyList[i].getId() %>" />
							<%=choices[j].getChoice() %>
						</label>
		            <% } %>
						<div class="controls">
							<button onclick='Surveys.submit(<%=surveyList[i].getId() %>)' class="btn btn-success" type="button">Submit</button>
						</div>
	            <% } %>
				</div>
			</div>
		</div>
		<% } %>
	</div>

	<script>
	$('#addchoice').click(function() {
		var ul = $(this).closest('ul');
		var li = $(this).closest('li');
		var clone = li.clone(true);
		var choiceNo = $('#newchoices>li').length;
		var name = 'choice' + choiceNo;
		clone.find('input').val('').attr('id', name).attr('name', name);
		clone.appendTo(ul);
		clone.find("span").html((choiceNo+1));
		$(this).remove();
	});

	$('createsurvey').click(function() {
		$(document.body).append();
	});
	</script>
</div>
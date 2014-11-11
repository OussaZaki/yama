
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="Tables.TagsTable"%>
<%@ page import="Tables.UserTable"%>


<link rel="stylesheet"
	href="/YamaWeb/css/bootstrap-tagmanager.css">
<script type="text/javascript"
	src="/YamaWeb/js/bootstrap-tagmanager.js"></script>	
<script type="text/javascript">
function addTags() {
	document.forms['form1'].submit();
}
</script>

<div class="nav span6 nav-tabs">
	<%
		UserTable user = (UserTable)request.getAttribute("user");
	%>
	<div class="row">
		<div>
			<!-- <form name="form1" method="post" class="form-horizontal" action="/YamaWeb/Tags/addTags/<%=user.getId()%>">
				-->
			<div class="form-horizontal">
				<input class="FormText" type="hidden" name="clickedButton"
					id="clickedButton" value="">
				<fieldset>
					<div class="control-group">
						<div class="controls">
							<input type="text" name="tags"
								placeholder="new tags, seperated with comma" class="tagManager" />
							<input type="submit" class="btn btn-info" value="Add"
								onclick="Tags.add(<%=user.getId()%>);" />
						</div>
					</div>
				</fieldset>
			</div>
		</div>
	</div>
	<div id="successMessage" class="alert alert-success" style="display:none">
		<button type="button" class="close" data-dismiss="alert" onclick="$(this).closest('div').hide();">&times;</button>
		<strong id>Success!</strong> Tag removed successfully.
	</div>

	<div id="errorMessage" class="alert alert-error" style="display:none">
		<button type="button" class="close" data-dismiss="alert" onclick="$(this).closest('div').hide();">&times;</button>
		<strong id>Warning!</strong> Error occured during removing.
	</div>

	<hr>
	<%
		TagsTable[] tags = (TagsTable[])request.getAttribute("tags");
		for(TagsTable tag: tags) {
	%>
	<span class="myTag" id="tags_<%=tag.getId()%>"> <span><a
			href="/YamaWeb/Tags/details/<%=tag.getId()%>"><%=tag.getTag()%></a>&nbsp;&nbsp;</span>
		<a href="#" class="myTagRemover" title="Remove"
		onclick="Tags.removeTag(this, <%=tag.getId()%>);return false;">x</a>
	</span>
	<%
		}
	%>
</div>


<script>
	  jQuery(".tagManager").tagsManager();
	</script>

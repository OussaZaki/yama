<div style="margin-left: 30px">
			
	<div id="contentBody" class="span8">
		<input type="text" class="input-xlarge search-query" placeholder="Serach for">
  		<button type="submit" class="btn">Search</button>
		
		<form class="form-inline">
		<br>
		<strong>Search In: </strong>&nbsp;
				<label class="checkbox">
		      		<input id="topics" type="checkbox"> Topics
		      	</label>
		      	&nbsp;
		    	<label class="checkbox">
		      		<input id="users" type="checkbox"> Users
		    	</label>
		    	&nbsp;
		    	<label class="checkbox">
		      		<input id="tags" type="checkbox"> Tags
		    	</label>
		</form>
		<div class="span4" id="results">
				<table class="table table-hover">
					<thead>
						<th colspan="2">Results</th>
					</thead>
					<tbody>
						<%
						String[] results = {"Erdem Orman", "Cmpe 451 Exam", "music"};
						String[] types = {"User", "Topic", "Tag"};
						for(int i = 0; i < results.length; i++){%>
						
						<tr>				
							<td><a><%= results[i] %></a></td>
							<td class="text-success"><%= types[i] %></td>
						</tr>
						<%} %>
					</tbody>
				</table>
		</div>
	</div>
</div>
package Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tables.EventTable;
import Tables.PostsTable;
import Tables.ReplyTable;
import Tables.TagsTable;
import Tables.UserTable;
import UtilityPack.HashString;
import drivers.EventDriver;
import drivers.PostDriver;
import drivers.ReplyDriver;
import drivers.TagsDriver;
import drivers.UserDriver;

public class AndroidApi extends ServletBase {

	public AndroidApi(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public void login() throws ServletException, IOException{
		System.out.println("Here?");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		response.setContentType("application/json");
		if(email==null || password==null ){
			response.getOutputStream().println("{\"success\": false, \"error\": \"All fields should be filled\"}");
		}
		else if(UserDriver.isCredentialsValid(email, password)){
			UserTable user = UserDriver.getByEmail(email);
			response.getOutputStream().println("{\"success\": true, \"name\": \""+user.getName()+"\", \"id\": "+user.getId()+" }");
		}
		else{
			request.setAttribute("loginFailed", 1);
			response.getOutputStream().println("{\"success\": false, \"error\": \"Email and password does not match\"}");
		}
	}
	
	public void eventUsers(Integer id) throws IOException{
		response.setContentType("application/json");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		if(!UserDriver.isCredentialsValid(email, password))
			return;
		
		UserTable[] users = UserDriver.getUsersByEvent(id);
		String output = "{users: [";
		for (UserTable user : users) {
			output = output+"{";
				output = output+"'id': "+user.getId()+",";
				output = output+"'name': '"+user.getName().replaceAll("'", "")+"',";
				output = output+"'profile_image': 'http://10.0.2.2:8082/YamaWeb/img/minions.jpg'";
			output = output+"},";
		}
		if(output.charAt(output.length()-1) == ',')
			output = output.substring(0, output.length()-1);
		output = output+"]}";
		response.getOutputStream().println(output);
	}
	
	public void myEvents(Integer userId) throws IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		if(!UserDriver.isCredentialsValid(email, password))
			return;
		printEventList(EventDriver.getUserEvent(userId));

	}
	
	public void attendedEvents(Integer userId) throws IOException{
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		if(!UserDriver.isCredentialsValid(email, password))
			return;
		printEventList(EventDriver.getUserJoinedEvent(userId));
	}
	
	private void printEventList(EventTable[] events) throws IOException{
		String output = "{events: [";
		for (EventTable event : events) {
			UserTable owner = UserDriver.getById(event.getUserId());
			output = output+"{";
				output = output+"'id': "+event.getId()+",";
				output = output+"'place': '"+event.getPlace().replaceAll("'", "")+"',";
				output = output+"'description': '"+event.getDescription().replaceAll("'", "")+"',";
				output = output+"'time': '"+PostDriver.niceTime(event.getEventTime())+"',";
				output = output+"'owner_id': "+owner.getId()+",";
				output = output+"'owner_name': '"+owner.getName().replaceAll("'", "")+"'";
			output = output+"},";
		}
		if(output.charAt(output.length()-1) == ',')
			output = output.substring(0, output.length()-1);
		output = output+"]}";
		response.getOutputStream().println(output);
	}

	public void postToUser(Integer id) throws IOException{
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		if(!UserDriver.isCredentialsValid(email, password))
			return;
		UserTable user = UserDriver.getByEmail(email);
		
		//get parameters from HTTP POST and convert string userId's into integer
		String body =  request.getParameter("body");
		
		int[] users = new int[0];
		if(id != user.getId()){
			users = new int[1];
			users[0] = id;
		}
		
		//if post body is not null then add the post and tag users or give an error message, which is not expected to occur.
		if(body!=null && PostDriver.addPostAndTagUsers(user.getId(), body, users)){
			response.getOutputStream().println("{\"success\": true}");
		}
		else
			response.getOutputStream().println("{\"success\": false, \"error\": \"unknown\"}");
	}

	public void postToTag(Integer id) throws IOException{
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		if(!UserDriver.isCredentialsValid(email, password))
			return;
		UserTable user = UserDriver.getByEmail(email);
		String body =  request.getParameter("body");
		if(body!=null && PostDriver.addPostWithTag(user.getId(), body, id))
			response.getOutputStream().println("{\"success\": true}");
		else
			response.getOutputStream().println("{\"success\": false, \"error\": \"unknown\"}");
	}
	
	public void reply(Integer postId) throws IOException{
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		if(!UserDriver.isCredentialsValid(email, password))
			return;
		UserTable user = UserDriver.getByEmail(email);
		String body =  request.getParameter("body");
		if(body == null)
			response.getOutputStream().println("{\"success\": false, \"error\": \"no_input\"}");
		else if(ReplyDriver.insert(postId, user.getId(), body))
			response.getOutputStream().println("{\"success\": true}");
		else
			response.getOutputStream().println("{\"success\": false, \"error\": \"unknown\"}");
	}
	
	public void register() throws ServletException, IOException{
		String email = request.getParameter("email");
		String name = request.getParameter("name")+" "+request.getParameter("last_name");
		String password = request.getParameter("password");
		String birthDate = "1970-01-01";
		String registerDate = "1970-01-01";
		response.setContentType("application/json");
		Tables.UserTable user = null;
		try {
			user = new Tables.UserTable(email, name, HashString.encrypt(password), birthDate, registerDate);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		if(UserDriver.createUser(user)){
			response.getOutputStream().println("{'success': true }");
		}
		else{
			response.getOutputStream().println("{'success': false }");
		}
	}
	
	public void newsFeed() throws IOException{
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		if(!UserDriver.isCredentialsValid(email, password))
			return;
		UserTable user = UserDriver.getByEmail(email);
		if(user == null){
			System.out.println("user is null");
			return;
		}
		newsFeed(user.getId());
	}
	
	public void newsFeed(Integer id) throws IOException{
		PostsTable[] posts = PostDriver.getNewsFeedByUserId(id);
		response.setContentType("application/json");
		String output = "{posts: [";
		for (PostsTable post : posts) {
			UserTable owner = UserDriver.getById(post.getOwnerId());
			output = output+"{";
				output = output+"'id': "+post.getId()+",";
				output = output+"'content': '"+post.getBody().replaceAll("'", "")+"',";
				output = output+"'posting_time': '"+PostDriver.niceTime(post.getPostingTime())+"',";
				output = output+"'owner_id': "+owner.getId()+",";
				output = output+"'owner_name': '"+owner.getName().replaceAll("'", "")+"',";
				output = output+"'replies': [";
				ReplyTable[] replies = ReplyDriver.getByPostId(post.getId());
				for (ReplyTable replyTable : replies) {
					UserTable user = UserDriver.getById(replyTable.getOwnerId());
					output = output+"{";
					output = output+"'owner_id': '"+user.getId()+"',";
					output = output+"'name': '"+user.getName()+"',";
					output = output+"'content': '"+replyTable.getBody()+"',";
					output = output+"'posting_time': '"+PostDriver.niceTime(replyTable.getPostingTime())+"'";
					output = output+"},";
				}
				if(output.charAt(output.length()-1) == ',')
					output = output.substring(0, output.length()-1);
				output = output+"]";
			output = output+"},";
		}
		if(output.charAt(output.length()-1) == ',')
			output = output.substring(0, output.length()-1);
		output = output+"]}";
		response.getOutputStream().println(output);
	}
	
	public void wall() throws IOException{
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		if(!UserDriver.isCredentialsValid(email, password))
			return;
		UserTable user = UserDriver.getByEmail(email);
		if(user == null){
			System.out.println("user is null");
			return;
		}
		wall(user.getId());
	}
	
	public void wall(Integer id) throws IOException{
		PostsTable[] posts = PostDriver.getWallPosts(id);
		response.setContentType("application/json");
		String output = "{posts: [";
		for (PostsTable post : posts) {
			UserTable owner = UserDriver.getById(post.getOwnerId());
			output = output+"{";
				output = output+"'id': "+post.getId()+",";
				output = output+"'content': '"+post.getBody().replaceAll("'", "")+"',";
				output = output+"'posting_time': '"+PostDriver.niceTime(post.getPostingTime())+"',";
				output = output+"'owner_id': "+owner.getId()+",";
				output = output+"'owner_name': '"+owner.getName().replaceAll("'", "")+"',";
				output = output+"'replies': [";
				ReplyTable[] replies = ReplyDriver.getByPostId(post.getId());
				for (ReplyTable replyTable : replies) {
					UserTable user = UserDriver.getById(replyTable.getOwnerId());
					output = output+"{";
					output = output+"'owner_id': '"+user.getId()+"',";
					output = output+"'name': '"+user.getName()+"',";
					output = output+"'content': '"+replyTable.getBody()+"',";
					output = output+"'posting_time': '"+PostDriver.niceTime(replyTable.getPostingTime())+"'";
					output = output+"},";
				}
				if(output.charAt(output.length()-1) == ',')
					output = output.substring(0, output.length()-1);
				output = output+"]";
			output = output+"},";
		}
		if(output.charAt(output.length()-1) == ',')
			output = output.substring(0, output.length()-1);
		output = output+"]}";
		response.getOutputStream().println(output);
	}
	
	public void tags() throws IOException{
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		if(!UserDriver.isCredentialsValid(email, password))
			return;
		UserTable user = UserDriver.getByEmail(email);
		if(user == null){
			System.out.println("user is null");
			return;
		}
		tags(user.getId());
	}
	
	public void tagWall(Integer id) throws IOException{
		PostsTable[] posts = PostDriver.getPostsByTag(id);
		response.setContentType("application/json");
		String output = "{posts: [";
		for (PostsTable post : posts) {
			UserTable owner = UserDriver.getById(post.getOwnerId());
			output = output+"{";
				output = output+"'id': "+post.getId()+",";
				output = output+"'content': '"+post.getBody().replaceAll("'", "")+"',";
				output = output+"'posting_time': '"+PostDriver.niceTime(post.getPostingTime())+"',";
				output = output+"'owner_id': "+owner.getId()+",";
				output = output+"'owner_name': '"+owner.getName().replaceAll("'", "")+"',";
				output = output+"'replies': [";
				ReplyTable[] replies = ReplyDriver.getByPostId(post.getId());
				for (ReplyTable replyTable : replies) {
					UserTable user = UserDriver.getById(replyTable.getOwnerId());
					output = output+"{";
					output = output+"'owner_id': '"+user.getId()+"',";
					output = output+"'name': '"+user.getName()+"',";
					output = output+"'content': '"+replyTable.getBody()+"',";
					output = output+"'posting_time': '"+PostDriver.niceTime(replyTable.getPostingTime())+"'";
					output = output+"},";
				}
				if(output.charAt(output.length()-1) == ',')
					output = output.substring(0, output.length()-1);
				output = output+"]";
			output = output+"},";
		}
		if(output.charAt(output.length()-1) == ',')
			output = output.substring(0, output.length()-1);
		output = output+"]}";
		response.getOutputStream().println(output);
	}
	
	public void tagUsers(Integer id) throws IOException{
		UserTable[] users = UserDriver.getUsersByTag(id);
		response.setContentType("application/json");
		String output = "{users: [";
		for (UserTable user : users) {
			output = output+"{";
				output = output+"'id': "+user.getId()+",";
				output = output+"'name': '"+user.getName().replaceAll("'", "")+"',";
				output = output+"'profile_image': 'http://10.0.2.2:8082/YamaWeb/img/minions.jpg'";
			output = output+"},";
		}
		if(output.charAt(output.length()-1) == ',')
			output = output.substring(0, output.length()-1);
		output = output+"]}";
		response.getOutputStream().println(output);
	}
	
	public void tags(Integer id) throws IOException{
		TagsTable[] tags = TagsDriver.getByUserId(id);
		response.setContentType("application/json");
		String output = "{tags: [";
		for (TagsTable tag : tags) {
			output = output+"{";
				output = output+"'id': "+tag.getId()+",";
				output = output+"'tag': '"+tag.getTag()+"'";
			output = output+"},";
		}
		if(output.charAt(output.length()-1) == ',')
			output = output.substring(0, output.length()-1);
		output = output+"]}";
		response.getOutputStream().println(output);
	}
}

package Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tables.UserTable;
import drivers.PostDriver;
import drivers.ReplyDriver;

public class Posts extends ServletBase {

	public Posts(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	/**
	 * Gets posts and users[] paramaters from HTTP POST,
	 * Creates a post in database table and tags users given in users[]
	 * in created post.
	 * Response is either 'success' or and error message written in a single line.
	 * @throws IOException
	 */
	public void addPost() throws IOException{
		//login check
		response.setContentType("application/json");
		UserTable user = getCurrentUser();
		if(user == null){
			response.getOutputStream().println("{\"success\": false, \"error\": \"need_login\"}");
			return;
		}
		
		//get parameters from HTTP POST and convert string userId's into integer
		String body =  request.getParameter("posts");
		String[] userStrings = request.getParameterValues("users[]");
		if(userStrings == null){
			response.getOutputStream().println("{\"success\": false, \"error\": \"unknown\"}");
			return;
		}
		
		int[] users = new int[userStrings.length];
		for(int i=0; i<userStrings.length; i++)
			users[i] = Integer.parseInt(userStrings[i]);
		
		//if post body is not null then add the post and tag users or give an error message, which is not expected to occur.
		if(body!=null && PostDriver.addPostAndTagUsers(user.getId(), body, users)){
			response.getOutputStream().println("{\"success\": true}");
		}
		else
			response.getOutputStream().println("{\"success\": false, \"error\": \"unknown\"}");
	}
	
	public void reply(Integer postId) throws IOException{
		//login check
		response.setContentType("application/json");
		UserTable user = getCurrentUser();
		if(user == null){
			response.getOutputStream().println("{\"success\": false, \"error\": \"need_login\"}");
			return;
		}
		
		String body =  request.getParameter("body");
		if(body == null)
			response.getOutputStream().println("{\"success\": false, \"error\": \"no_input\"}");
		else if(ReplyDriver.insert(postId, user.getId(), body))
			response.getOutputStream().println("{\"success\": true}");
		else
			response.getOutputStream().println("{\"success\": false, \"error\": \"unknown\"}");
	}
	
	/**
	 * @deprecated
	 * use addPost() method which supports multiple user tagging by sending users by POST method.
	 */
	@Deprecated
	public void addPosts(Integer user_id) throws ServletException, IOException{
		UserTable user = getCurrentUser();
		if(user == null){
			response.getOutputStream().print("An unexpected error has occured, sorry for inconvenience.");
			return;
		}
		String body =  request.getParameter("posts");
		int[] users = new int[1];
		users[0] = user_id;
		if(body!=null && PostDriver.addPostAndTagUsers(user.getId(), body, users)){
			response.getOutputStream().print("success");
		}
		else
			response.getOutputStream().print("An unexpected error has occured, sorry for inconvenience.");
	}
}

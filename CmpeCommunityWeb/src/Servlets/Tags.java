package Servlets;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import drivers.PostDriver;
import drivers.ReplyDriver;
import drivers.TagsDriver;
import drivers.UserDriver;

import Tables.PostsTable;
import Tables.TagsTable;
import Tables.UserTable;

public class Tags extends ServletBase {

	public Tags(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public void details(Integer tagId) throws ServletException, IOException{
		TagsTable tag = TagsDriver.getById(tagId);
		if(tag == null){
			//TODO show 404
			return;
		}
		UserTable user = getCurrentUser();
		if(user == null){
			request.getRequestDispatcher("/User/login").forward(request, response);
			return;
		}
		
		request.setAttribute("user", user);
		request.setAttribute("tag", tag);
		request.getRequestDispatcher("/layout/header.jsp").include(request, response);
		request.getRequestDispatcher("/TagView.jsp").include(request, response);
		request.getRequestDispatcher("/layout/footer.jsp").include(request, response);
	}
	
	public void wall(Integer tagId) throws ServletException, IOException{
		if(getCurrentUser() == null)
			return;
		PostsTable[] posts = PostDriver.getPostsByTag(tagId);
		
		Map<Integer, UserTable> users = new TreeMap<Integer, UserTable>();
		for (PostsTable post : posts)
			if(users.get(post.getOwnerId()) == null)
				users.put(post.getOwnerId(), UserDriver.getById(post.getOwnerId()));
		request.setAttribute("posts", posts);
		request.setAttribute("users", users);
		request.setAttribute("replies", ReplyDriver.getReplies(posts));
		UserTable user = getCurrentUser();
		Map<Integer, Object> tags = new TreeMap<Integer, Object>();
		for(PostsTable p : posts){
			TagsTable t = TagsDriver.getByPostId(p.getId());
			UserTable u = TagsDriver.getUserTagByPostId(p.getId());
			if(t != null)
				tags.put(p.getId(), t);
			if(u != null)
				tags.put(p.getId(), u);
		}
		request.setAttribute("tagsInPosts", tags);
		request.setAttribute("user", user);
		request.setAttribute("tag_id", tagId);
		request.setAttribute("tag", TagsDriver.getById(tagId));
		request.getRequestDispatcher("/TagWall.jsp").include(request, response);
	}
	
	public void users(Integer tagId) throws ServletException, IOException{
		if(getCurrentUser() == null)
			return;
		UserTable[] users = UserDriver.getUsersByTag(tagId);
		request.setAttribute("users", users);
		request.getRequestDispatcher("/UserListView.jsp").include(request, response);
	}
	
	public void addTags(Integer user_id) throws Exception{
		//login check
		response.setContentType("application/json");
		UserTable user = getCurrentUser();
		if(user == null){
			response.getOutputStream().println("{\"success\": false, \"error\": \"need_login\"}");
			return;
		}
		
		//get parameters from HTTP POST and convert to TagsTable[]
		String tags =  request.getParameter("tags");
		if (tags==null){
			response.getOutputStream().println("{\"success\": false, \"error\": \"unknown\"}");
			return;
		}
		TagsTable[] tagsTable = TagsDriver.createTagsArray(tags);
		
		//try to create tags and return the result
		if(TagsDriver.createTags(tagsTable, UserDriver.getById(user_id)))
			response.getOutputStream().println("{\"success\": true}");
		else
			response.getOutputStream().println("{\"success\": false, \"error\": \"unknown\"}");
	}
	
	public void addPost(Integer tagId) throws IOException{
		//login check
		response.setContentType("application/json");
		UserTable user = getCurrentUser();
		if(user == null){
			response.getOutputStream().println("{\"success\": false, \"error\": \"need_login\"}");
			return;
		}
		
		//get parameters from HTTP POST
		String body =  request.getParameter("body");
		
		//if post body is not null then add the post and tag users or give an error message, which is not expected to occur.
		if(body!=null && PostDriver.addPostWithTag(user.getId(), body, tagId)){
			response.getOutputStream().println("{\"success\": true}");
		}
		else
			response.getOutputStream().println("{\"success\": false, \"error\": \"unknown\"}");
	}
	
	/**
	 * @deprecated
	 * Use addPost() instead. There is no need for user_id parameter
	 * since the poster is the logged-in user.
	 */
	@Deprecated
	public void addPosts(Integer tag_id,Integer user_id) throws Exception {
		String post=request.getParameter("tag_post");
		PostDriver.addPosts(user_id, post);
		int post_id=PostDriver.getMaxId(user_id);
		if (post_id==0)
			return;
		TagsDriver.insertTagsInPosts(post_id,tag_id);
		
	}
	public void removeTag(Integer tagId) throws Exception {
		response.setContentType("application/json");
		UserTable user = getCurrentUser();
		if(user == null){
			response.getOutputStream().println("{\"success\": false, \"error\": \"need_login\"}");
			return;
		}
		if (TagsDriver.removeTagFromUser(user.getId(), tagId)) {
			response.getOutputStream().println("{\"success\": true}");
		} else
			response.getOutputStream().println("{\"success\": false, \"error\": \"unknown\"}");
		
	}
}

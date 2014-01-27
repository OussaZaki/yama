package Servlets;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tables.PostsTable;
import Tables.ReplyTable;
import Tables.SurveyTable;
import Tables.TagsTable;
import Tables.UserTable;
import drivers.PostDriver;
import drivers.ReplyDriver;
import drivers.SurveyDriver;
import drivers.TagsDriver;
import drivers.UserDriver;

public class Profile extends ServletBase {

	public Profile(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	public void index() throws IOException, ServletException{
		UserTable user = getCurrentUser();
		if(user == null){
			request.getRequestDispatcher("/User/login").forward(request, response);
			return;
		}
		details(user.getId());
	}

	public void wall(Integer userId) throws ServletException, IOException{
		if(getCurrentUser() == null)
			return;
		PostsTable[] posts = PostDriver.getWallPosts(userId);
		if (posts == null)
			return;
		Map<Integer, UserTable> users = new TreeMap<Integer, UserTable>();
		for (PostsTable post : posts)
			if(users.get(post.getOwnerId()) == null)
				users.put(post.getOwnerId(), UserDriver.getById(post.getOwnerId()));
		Map<Integer, ReplyTable[]> replies = ReplyDriver.getReplies(posts);
		for (ReplyTable[] replyTables : replies.values())
			for(ReplyTable r : replyTables)
				if(users.get(r.getOwnerId()) == null)
					users.put(r.getOwnerId(), UserDriver.getById(r.getOwnerId()));
		request.setAttribute("posts", posts);
		request.setAttribute("users", users);
		request.setAttribute("replies", replies);
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
		UserTable user = UserDriver.getById(userId);
		request.setAttribute("user", user);
		request.getRequestDispatcher("/ProfileWall.jsp").include(request, response);
	}

	public void news(Integer userId) throws ServletException, IOException{
		if(getCurrentUser() == null)
			return;
		PostsTable[] posts = PostDriver.getNewsFeedByUserId(userId);
		if (posts == null)
			return;
		Map<Integer, UserTable> users = new TreeMap<Integer, UserTable>();
		for (PostsTable post : posts)
			if(users.get(post.getOwnerId()) == null)
				users.put(post.getOwnerId(), UserDriver.getById(post.getOwnerId()));
		request.setAttribute("posts", posts);
		request.setAttribute("replies", ReplyDriver.getReplies(posts));
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
		request.setAttribute("users", users);
		UserTable user = UserDriver.getById(userId);
		request.setAttribute("user", user);
		request.getRequestDispatcher("/PostListView.jsp").include(request, response);
	}
	
	public void details(Integer userId) throws IOException, ServletException{
		if(getCurrentUser() == null){
			request.getRequestDispatcher("/User/login").forward(request, response);
			return;
		}
		response.setContentType("text/html");
		UserTable user = UserDriver.getById(userId);
		if(user == null){
			//TODO show 404
			return;
		}
		request.setAttribute("user", user);
		request.getRequestDispatcher("/layout/header.jsp").include(request, response);
		request.getRequestDispatcher("/ProfilePage.jsp").include(request, response);
		request.getRequestDispatcher("/layout/footer.jsp").include(request, response);
	}
	
	public void tags(Integer userId) throws ServletException, IOException{
		if(getCurrentUser() == null){
			request.getRequestDispatcher("/User/login").forward(request, response);
			return;
		}
		
		UserTable user = UserDriver.getById(userId);
		if(user == null){
			//TODO show 404
			return;
		}
		request.setAttribute("user", user);
		
		
		
		TagsTable[] tags = TagsDriver.getByUserId(userId);
		request.setAttribute("tags", tags);
		
		response.setContentType("text/html");
		request.getRequestDispatcher("/layout/header.jsp").include(request, response);
		request.getRequestDispatcher("/TagList.jsp").include(request, response);
		request.getRequestDispatcher("/layout/footer.jsp").include(request, response);
	}
	public void survey(Integer userId) throws ServletException, IOException{
		if(getCurrentUser() == null){
			request.getRequestDispatcher("/User/login").forward(request, response);
			return;
		}
		
		UserTable user = UserDriver.getById(userId);
		if(user == null){
			//TODO show 404
			return;
		}
		request.setAttribute("user", user);
		
		
		
		SurveyTable[] userSurvey = SurveyDriver.getUserSurvey(userId);
		request.setAttribute("userSurvey", userSurvey);
		
		SurveyTable[] userJoinedSurvey = SurveyDriver.getUserJoinedSurvey(userId);
		request.setAttribute("userJoinedSurvey", userJoinedSurvey);
		
		response.setContentType("text/html");
		request.getRequestDispatcher("/layout/header.jsp").include(request, response);
		request.getRequestDispatcher("/Survey.jsp").include(request, response);
		request.getRequestDispatcher("/layout/footer.jsp").include(request, response);
	}
	
	public void event() throws ServletException, IOException{
		if(getCurrentUser() == null){
			request.getRequestDispatcher("/User/login").forward(request, response);
			return;
		}
		event(getCurrentUser().getId());
	}
	
	public void event(Integer userId) throws ServletException, IOException{
		if(getCurrentUser() == null){
			request.getRequestDispatcher("/User/login").forward(request, response);
			return;
		}
		UserTable user = UserDriver.getById(userId);
		if(user == null){
			//TODO show 404
			return;
		}
		request.setAttribute("user", user);
		request.setAttribute("type", "myEvents");
		response.setContentType("text/html");
		request.getRequestDispatcher("/layout/header.jsp").include(request, response);
		request.getRequestDispatcher("/Event.jsp").include(request, response);
		request.getRequestDispatcher("/layout/footer.jsp").include(request, response);
	}
	
	public void edit() throws ServletException, IOException{
		UserTable user = getCurrentUser();
		if(user == null){
			request.getRequestDispatcher("/User/login").forward(request, response);
			return;
		}
		
		request.setAttribute("user", user);
		request.getRequestDispatcher("/layout/header.jsp").include(request, response);
		request.getRequestDispatcher("/ProfileEdit.jsp").include(request, response);
		request.getRequestDispatcher("/layout/footer.jsp").include(request, response);
	}
	public void update(Integer userId) throws ServletException, IOException{
		UserTable user = getCurrentUser();
		if(user == null){
			request.getRequestDispatcher("/User/login").forward(request, response);
			return;
		}
		
		request.setAttribute("user", user);
		String name=request.getParameter("name");
		String email=request.getParameter("email");
		String password=request.getParameter("password");
		if (UserDriver.updateUserSettings(userId, name, password, email)) {
			request.setAttribute("message", "Profile settings are updated");
			UserTable updatedUser=UserDriver.getById(userId);
			setCurrentUser(updatedUser);
			user=getCurrentUser();
		}
		request.getRequestDispatcher("/layout/header.jsp").include(request, response);
		request.getRequestDispatcher("/ProfileEdit.jsp").include(request, response);
		request.getRequestDispatcher("/layout/footer.jsp").include(request, response);
	}
	
	public void cloud()throws ServletException, IOException{
		UserTable user = getCurrentUser();
		if(user == null){
			request.getRequestDispatcher("/User/login").forward(request, response);
			return;
		}
		request.getRequestDispatcher("/Cloud.jsp").include(request, response);
	}
}

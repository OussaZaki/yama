package Servlets;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tables.ForumPostTable;
import Tables.ForumTopicTable;
import Tables.ForumsTable;
import Tables.UserTable;
import drivers.ForumsDriver;
import drivers.UserDriver;

public class Forum extends ServletBase {

	public Forum(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	public void index() throws ServletException, IOException{
		response.setContentType("text/html");
		ForumsTable[] categories = ForumsDriver.getCategories();
		Map<Integer, ForumsTable[]> subForums = new TreeMap<Integer, ForumsTable[]>();
		Map<Integer, ForumPostTable> posts = new TreeMap<Integer, ForumPostTable>();
		Map<Integer, UserTable> users = new TreeMap<Integer, UserTable>();

		for(ForumsTable category: categories){
			ForumsTable[] forums = ForumsDriver.getByParentId(category.getId());
			subForums.put(category.getId(), forums);
			for(ForumsTable f: forums){
				subForums.put(f.getId(), ForumsDriver.getByParentId(f.getId()));
				ForumPostTable post = ForumsDriver.getPostById(f.getLastPostId());
				if(post != null){
					posts.put(post.getId(), post);
					users.put(post.getUserId(), UserDriver.getById(post.getUserId()));
				}
			}
		}
		
		request.setAttribute("categories", categories);
		request.setAttribute("subForums", subForums);
		request.setAttribute("posts", posts);
		request.setAttribute("users", users);
		request.getRequestDispatcher("/categories.jsp").include(request, response);
	}
	
	public void index(Integer forumId) throws ServletException, IOException{
		response.setContentType("text/html");
		ForumsTable category = ForumsDriver.getById(forumId);
		Map<Integer, ForumsTable[]> subForums = new TreeMap<Integer, ForumsTable[]>();
		ForumsTable[] parents = ForumsDriver.getParentsById(forumId);
		Map<Integer, ForumPostTable> posts = new TreeMap<Integer, ForumPostTable>();
		Map<Integer, UserTable> users = new TreeMap<Integer, UserTable>();
		
		if(category != null){
			ForumsTable[] forums = ForumsDriver.getByParentId(category.getId());
			subForums.put(category.getId(), forums);
			for(ForumsTable f: forums){
				subForums.put(f.getId(), ForumsDriver.getByParentId(f.getId()));
				ForumPostTable post = ForumsDriver.getPostById(f.getLastPostId());
				if(post != null){
					posts.put(post.getId(), post);
					users.put(post.getUserId(), UserDriver.getById(post.getUserId()));
				}
			}
			ForumTopicTable[] topics = ForumsDriver.getTopicsByForumId(forumId);
			for(ForumTopicTable t: topics){
				users.put(t.getUserId(), UserDriver.getById(t.getUserId()));
				posts.put(t.getLastPostId(), ForumsDriver.getPostById(t.getLastPostId()));
			}
			
			request.setAttribute("topics", topics);
			request.setAttribute("category", category);
			request.setAttribute("subForums", subForums);
			request.setAttribute("parents", parents);
			request.setAttribute("posts", posts);
			request.setAttribute("users", users);
			request.getRequestDispatcher("/forums.jsp").include(request, response);
		}
	}
	
	public void newtopic(Integer forumId)throws ServletException, IOException{
		response.setContentType("text/html");
		ForumsTable forum = ForumsDriver.getById(forumId);
		if(forum != null){
			ForumsTable[] parents = ForumsDriver.getParentsById(forumId);
			request.setAttribute("forum", forum);
			request.setAttribute("parents", parents);
			request.getRequestDispatcher("/newtopic.jsp").include(request, response);
		}
	}
	
	public void createtopic(Integer forumId) throws IOException, ServletException{
		UserTable user = getCurrentUser();
		if(user == null){
			request.getRequestDispatcher("/User/login").forward(request, response);
		}else{
			String title =  request.getParameter("title");
			String content = request.getParameter("content");
			if(content == null || title == null || "".equals(content) || "".equals(title)){
				request.getRequestDispatcher("/Forum/index/" + forumId).forward(request, response);
			}else{
				int topicId = ForumsDriver.createTopic(forumId, title, content, user.getId());
				request.getRequestDispatcher("/Forum/topic/" + topicId).forward(request, response);
			}
		}
	}
	
	public void topic(Integer topicId)throws IOException, ServletException{
		UserTable user = getCurrentUser();
		if(user == null){
			request.getRequestDispatcher("/User/login").forward(request, response);
		}else{
			ForumTopicTable topic = ForumsDriver.getTopicById(topicId);
			if(topic != null){
				ForumsDriver.incrementTopicViewCount(topic.getId());
				ForumsTable forum = ForumsDriver.getById(topic.getForumId());
				ForumsTable[] parents = ForumsDriver.getParentsById(topic.getForumId());
				ForumPostTable[] posts = ForumsDriver.getPostsByTopicId(topicId);
				Map<Integer, UserTable> users = new TreeMap<Integer, UserTable>();
				for(ForumPostTable p: posts){
					users.put(p.getUserId(), UserDriver.getById(p.getUserId()));
				}
				
				request.setAttribute("topic", topic);
				request.setAttribute("forum", forum);
				request.setAttribute("parents", parents);
				request.setAttribute("posts", posts);
				request.setAttribute("users", users);
				request.setAttribute("user", user);
				request.getRequestDispatcher("/topic.jsp").include(request, response);
			}
		}
	}
	
	public void reply(Integer topicId) throws IOException, ServletException{
		UserTable user = getCurrentUser();
		if(user == null){
			request.getRequestDispatcher("/User/login").forward(request, response);
		}else{
			String content = request.getParameter("content");
			if(content != null && !"".equals(content)){
				int postId = ForumsDriver.createPost(topicId, content, user.getId());
				ForumsDriver.updateTopicLastPost(topicId, postId);
				ForumsDriver.incrementTopicRepliesCount(topicId);
			}
			request.getRequestDispatcher("/Forum/topic/" + topicId).forward(request, response);
		}
	}
}

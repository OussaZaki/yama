package drivers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import DBPack.DBStatement;
import Tables.ForumPostTable;
import Tables.ForumTopicTable;
import Tables.ForumsTable;

public class ForumsDriver {
	public static int createTopic(int forumId, String title, String content, int userId){
		try{
			String query = "INSERT INTO forum_topics (forum_id, title, user_id, creation_time) " +
					"VALUES (?, ?, ?, NOW())";
			PreparedStatement ps = (PreparedStatement)DBStatement.getMainConnection()
					.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, forumId);
			ps.setString(2, title);
			ps.setInt(3, userId);
			ps.executeUpdate();
			ResultSet key = ps.getGeneratedKeys();
			if(key.next()){
				int topicId = key.getInt(1);
				int postId = createPost(topicId, content, userId);
				updateTopicLastPost(topicId, postId);
				incrementForumTopicsCount(forumId);
				incrementForumPostsCount(forumId);
				return topicId;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	public static int createPost(int topicId, String content, int userId){
		try{
			String query = "INSERT INTO forum_posts (topic_id, content, user_id, post_time) " +
					"VALUES(?, ?, ?, NOW())";
			PreparedStatement ps = (PreparedStatement)DBStatement.getMainConnection()
					.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, topicId);
			ps.setString(2, content);
			ps.setInt(3, userId);
			ps.executeUpdate();
			
			ResultSet key = ps.getGeneratedKeys();
			if(key.next()){
				return key.getInt(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void updateTopicLastPost(int topicId, int postId){
		try{
			String query = "UPDATE forum_topics SET last_post_id = ? WHERE id = ?";
			PreparedStatement ps = (PreparedStatement)DBStatement.getMainConnection()
					.prepareStatement(query);
			ps.setInt(1, postId);
			ps.setInt(2, topicId);
			ps.executeUpdate();
			
			int forumId = getTopicById(topicId).getForumId();
			updateForumLastPosts(forumId, postId);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void updateForumLastPosts(int forumId, int postId){
		try{
			String updateQuery = "UPDATE forums SET last_post_id = ? WHERE id = ?"; 
			String parentQuery = "SELECT parent_id FROM forums WHERE id = ?";
			PreparedStatement psUpdate = (PreparedStatement)DBStatement.getMainConnection()
					.prepareStatement(updateQuery);
			PreparedStatement psParent = (PreparedStatement)DBStatement.getMainConnection()
					.prepareStatement(parentQuery);
			
			while(true){
				psUpdate.setInt(1, postId);
				psUpdate.setInt(2, forumId);
				psUpdate.executeUpdate();
				
				psParent.setInt(1, forumId);
				ResultSet parent = psParent.executeQuery();
				if(parent.next() && parent.getInt(1) > 0){
					forumId = parent.getInt(1);
				}else{
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void incrementForumTopicsCount(int forumId){
		try{
			String updateQuery = "UPDATE forums SET topics_count = topics_count + 1 WHERE id = ?"; 
			String parentQuery = "SELECT parent_id FROM forums WHERE id = ?";
			PreparedStatement psUpdate = (PreparedStatement)DBStatement.getMainConnection()
					.prepareStatement(updateQuery);
			PreparedStatement psParent = (PreparedStatement)DBStatement.getMainConnection()
					.prepareStatement(parentQuery);
			
			while(true){
				psUpdate.setInt(1, forumId);
				psUpdate.executeUpdate();
				
				psParent.setInt(1, forumId);
				ResultSet parent = psParent.executeQuery();
				if(parent.next() && parent.getInt(1) > 0){
					forumId = parent.getInt(1);
				}else{
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void incrementForumPostsCount(int forumId){
		try{
			String updateQuery = "UPDATE forums SET posts_count = posts_count + 1 WHERE id = ?"; 
			String parentQuery = "SELECT parent_id FROM forums WHERE id = ?";
			PreparedStatement psUpdate = (PreparedStatement)DBStatement.getMainConnection()
					.prepareStatement(updateQuery);
			PreparedStatement psParent = (PreparedStatement)DBStatement.getMainConnection()
					.prepareStatement(parentQuery);
			
			while(true){
				psUpdate.setInt(1, forumId);
				psUpdate.executeUpdate();
				
				psParent.setInt(1, forumId);
				ResultSet parent = psParent.executeQuery();
				if(parent.next() && parent.getInt(1) > 0){
					forumId = parent.getInt(1);
				}else{
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void incrementTopicViewCount(int topicId){
		try{
			String query = "UPDATE forum_topics SET views_count = views_count + 1 WHERE id = ?";
			PreparedStatement ps = (PreparedStatement)DBStatement.getMainConnection()
					.prepareStatement(query);
			ps.setInt(1, topicId);
			ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public static void incrementTopicRepliesCount(int topicId){
		try{
			String query = "UPDATE forum_topics SET replies_count = replies_count + 1 WHERE id = ?";
			PreparedStatement ps = (PreparedStatement)DBStatement.getMainConnection()
					.prepareStatement(query);
			ps.setInt(1, topicId);
			ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		
	public static ForumTopicTable[] getTopicsByForumId(int forumId){
		ForumTopicTable[] topics = new ForumTopicTable[0];
		try{
			String query = "SELECT * FROM forum_topics WHERE forum_id = ? ORDER BY creation_time DESC";
			PreparedStatement ps = (PreparedStatement)DBStatement.getMainConnection()
					.prepareStatement(query);
			ps.setInt(1, forumId);
			ResultSet result = ps.executeQuery();
			topics = convertToTopicsArray(result);
		}catch(Exception e){
			e.printStackTrace();
		}
		return topics;
	}
	
	public static ForumsTable getById(int id){
		ForumsTable table = null;
		try{
			String query="SELECT * FROM `forums` WHERE `id` = ?";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			if(result.next())
				table = new ForumsTable(result.getInt("id"), result.getInt("parent_id"), result.getString("name"), result.getString("description"), result.getInt("topics_count"), result.getInt("posts_count"), result.getInt("last_post_id"));
		} catch(SQLException e) {
		} catch (Exception e) {
		}
		return table;
	}
	
	public static ForumsTable[] getCategories(){
		try{
			String query="SELECT * FROM `forums` WHERE ISNULL(`parent_id`) ORDER BY `id`";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ResultSet result = ps.executeQuery();
			return convertToArray(result);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return new ForumsTable[0];
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ForumsTable[0];
		}
	}
	
	public static ForumsTable[] getByParentId(int id){
		try{
			String query="SELECT * FROM `forums` WHERE `parent_id`=? ORDER BY `id`";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			return convertToArray(result);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return new ForumsTable[0];
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ForumsTable[0];
		}
	}
	
	public static ForumsTable[] getParentsById(int id){
		ArrayList<ForumsTable> parents = new ArrayList<ForumsTable>();
		while(id > 0){
			ForumsTable parent = getById(id);
			parents.add(parent);
			id = parent.getParentId();
		}
		ForumsTable[] ret = new ForumsTable[parents.size()]; 
		return parents.toArray(ret);
	}
	
	public static ForumTopicTable getTopicById(int id){
		try{
			String query = "SELECT * FROM forum_topics WHERE id = ?";
			PreparedStatement ps = DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, id);
			ResultSet r = ps.executeQuery();
			if(r.next()){
				return new ForumTopicTable(r.getInt("id"), r.getInt("forum_id"), r.getString("title"), 
						r.getInt("replies_count"), r.getInt("views_count"), r.getTimestamp("creation_time"), 
						r.getInt("user_id"), r.getInt("last_post_id"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static ForumPostTable getPostById(int id){
		try{
			String query = "SELECT * FROM forum_posts WHERE id = ?";
			PreparedStatement ps = DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, id);
			ResultSet r = ps.executeQuery();
			if(r.next()){
				return new ForumPostTable(r.getInt("id"), r.getString("content"),
						r.getTimestamp("post_time"), r.getInt("user_id"), r.getInt("topic_id"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static ForumPostTable[] getPostsByTopicId(int topicId){
		ForumPostTable[] posts = new ForumPostTable[0];
		try{
			String query = "SELECT * FROM forum_posts WHERE topic_id = ?";
			PreparedStatement ps = DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, topicId);
			ResultSet r = ps.executeQuery();
			posts = convertToPostsArray(r);
		}catch(Exception e){
			e.printStackTrace();
		}
		return posts;
	}
	
	private static ForumsTable[] convertToArray(ResultSet result) throws SQLException{
		int N = 0;
		while(result.next()) N++;
		result.beforeFirst();
		ForumsTable[] forums = new ForumsTable[N];
		int i=0;
		while(result.next())
			forums[i++] = new ForumsTable(result.getInt("id"), result.getInt("parent_id"), result.getString("name"), result.getString("description"), result.getInt("topics_count"), result.getInt("posts_count"), result.getInt("last_post_id"));
		return forums;
	}
	
	private static ForumTopicTable[] convertToTopicsArray(ResultSet result) throws SQLException{
		int N = 0;
		while(result.next()) N++;
		result.beforeFirst();
		ForumTopicTable[] forums = new ForumTopicTable[N];
		int i=0;
		while(result.next()){
			forums[i++] = new ForumTopicTable(result.getInt("id"), result.getInt("forum_id"),
					result.getString("title"), result.getInt("replies_count"), result.getInt("views_count"),
					result.getTimestamp("creation_time"), result.getInt("user_id"), result.getInt("last_post_id"));
		}
		return forums;
	}
	
	private static ForumPostTable[] convertToPostsArray(ResultSet result) throws SQLException{
		int N = 0;
		while(result.next()) N++;
		result.beforeFirst();
		ForumPostTable[] forums = new ForumPostTable[N];
		int i=0;
		while(result.next()){
			forums[i++] = new ForumPostTable(result.getInt("id"), result.getString("content"),
					result.getTimestamp("post_time"), result.getInt("user_id"), result.getInt("topic_id"));
		}
		return forums;
	}
	
	public static String niceTime(Timestamp time){
		Calendar cal = Calendar.getInstance();

		Date now = cal.getTime();
		long difference = now.getTime() - time.getTime();
		
		long year = (((long)1000)*60*60*24*365);
		long month = (((long)1000)*60*60*24*30);
		
		if(difference > year)
			return (difference/year)+" years ago";
		if(difference > month)
			return (difference/month)+" months ago";
		if(difference > (1000*60*60*24))
			return (difference/(1000*60*60*24))+" days ago";
		if(difference > (1000*60*60))
			return (difference/(1000*60*60))+" hours ago";
		if(difference > 1000*60)
			return (difference/(1000*60))+" minutes ago";
		return (difference/1000)+" seconds ago";
	}
}

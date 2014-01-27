package drivers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.mysql.jdbc.Statement;

import DBPack.DBStatement;
import Tables.PostsTable;

public class PostDriver {

	public static PostsTable[] getWallPosts(int user_id) {
		try {
			String query="SELECT * FROM ((select * from posts where owner_id=?) union (select * from posts where id in (select post_id from users_in_posts where user_id=?))) AS `posts` ORDER BY `posting_time` DESC";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, user_id);
			ps.setInt(2, user_id);
			ResultSet result = ps.executeQuery();
			return convertToArray(result);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return new PostsTable[0];
		}  catch (Exception e) {
			System.out.println(e.getMessage());
			return new PostsTable[0];
		}
	}
	
	public static PostsTable[] getNewsFeedByUserId(int id){
		try {
			//posted to or by user
			//user's tags' posts
			//
			String postedToUser="SELECT `posts`.* FROM `users_in_posts` INNER JOIN `posts` ON `users_in_posts`.`post_id`=`posts`.`id` WHERE `users_in_posts`.`user_id`=?";
			String postedToUsersTags = "SELECT `posts`.* FROM `tags_in_posts` INNER JOIN `posts` INNER JOIN `tags_in_users` ON `tags_in_posts`.`post_id`=`posts`.`id` AND `tags_in_users`.`tag_id`=`tags_in_posts`.`tag_id` WHERE `tags_in_users`.`user_id`=? ";
			String query = "SELECT * FROM (("+postedToUser+") UNION ("+postedToUsersTags+")) AS `p` ORDER BY `p`.`posting_time` DESC";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, id);
			ps.setInt(2, id);
			ResultSet result = ps.executeQuery();
			return convertToArray(result);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return new PostsTable[0];
		}  catch (Exception e) {
			System.out.println(e.getMessage());
			return new PostsTable[0];
		}
	}
	
	public static PostsTable[] getPostsByUserId(int id){
		try {
			String query="SELECT * FROM `posts` WHERE `owner_id`=? ORDER BY posting_time desc";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			return convertToArray(result);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return new PostsTable[0];
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new PostsTable[0];
		}
	}

	public static PostsTable[] getPostsByTag(int tagId){
		try {
			String query="SELECT * FROM `posts` INNER JOIN `tags_in_posts` ON `posts`.`id`=`tags_in_posts`.`post_id` WHERE `tags_in_posts`.`tag_id`=? ORDER BY `posts`.`posting_time` DESC";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, tagId);
			ResultSet result = ps.executeQuery();
			return convertToArray(result);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return new PostsTable[0];
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new PostsTable[0];
		}
	}

	private static PostsTable[] convertToArray(ResultSet result) throws SQLException{
		int N = 0;
		while(result.next()) N++;
		result.beforeFirst();
		PostsTable[] posts = new PostsTable[N];
		int i=0;
		while(result.next())
			posts[i++] = new PostsTable(result.getInt("id"), result.getInt("owner_id"), result.getString("body"), result.getString("posting_time"));
		return posts;
	}
	public static boolean addPosts(int ownerId,String post) {
		try{
			String query="INSERT INTO posts (owner_id,body,posting_time) VALUES (?,?,NOW())" ;	
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, ownerId);
			ps.setString(2,post);
			ps.executeUpdate();
			return true; // means false
		}  catch(SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		} finally {

		}
	}
	
	public static boolean addPostWithTag(int ownerId, String body, int tagId){
		try{
			String query="INSERT INTO posts (owner_id,body,posting_time) VALUES (?,?,NOW())" ;	
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, ownerId);
			ps.setString(2, body);
			ps.executeUpdate();
			ResultSet result = ps.getGeneratedKeys();
			if(!result.next())
				return false;
			int postId = result.getInt(1);
			if(!TagsDriver.insertTagsInPosts(postId, tagId)){
				//TODO delete previously created post
				return false;
			}
			return true;
		}  catch(SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
		
	}

	public static boolean addPostAndTagUsers(int ownerId, String body, int[] users){
		try{
			String query="INSERT INTO posts (owner_id,body,posting_time) VALUES (?,?,NOW())" ;	
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, ownerId);
			ps.setString(2, body);
			ps.executeUpdate();
			ResultSet result = ps.getGeneratedKeys();
			if(!result.next())
				return false;
			int postId = result.getInt(1);
			for (int i : users)
				TagsDriver.insertUserInPost(i, postId);
			return true;
		}  catch(SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		} finally {

		}
	}

	public static int getMaxId(int user_id) {
		try {
			String query="(select max(id) from posts where owner_id=?";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, user_id);
			ResultSet result = ps.executeQuery();
			return result.getInt(1);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return 0;
		}  catch (Exception e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	public static String niceTime(String time){
		Calendar cal = Calendar.getInstance();

		time = time.substring(0, time.length()-2);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = null;
		try {
			date1 = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date date2 = cal.getTime();
		long difference = date2.getTime() - date1.getTime();
		
		long year = (((long)1000)*60*60*24*365);
		long month = (((long)1000)*60*60*24*30);
		
		if(difference > 0){
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
		else{
			SimpleDateFormat f = new SimpleDateFormat("HH:mm");
			difference = -difference;
			if(difference > year)
				return "in "+(difference/year)+" years at "+f.format(date1);
			if(difference > month)
				return "in "+(difference/month)+" months at "+f.format(date1);
			if(difference > (1000*60*60*24))
				return "in "+(difference/(1000*60*60*24))+" days at "+f.format(date1);
			if(difference > (1000*60*60))
				return "in "+(difference/(1000*60*60))+" hours";
			if(difference > 1000*60)
				return "in "+(difference/(1000*60))+" minutes";
			return "in "+(difference/1000)+" seconds";
			
		}
	}
}

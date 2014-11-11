package drivers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DBPack.DBStatement;
import Tables.TagsTable;
import Tables.UserTable;

public class TagsDriver {

	public static TagsTable[] createTagsArray(String tags) {
		TagsTable[] tagsTable = null;
		String[] tags_arr = tags.split(",");
		tagsTable = new Tables.TagsTable[tags_arr.length];
		for (int i = 0;i<tags_arr.length;i++){
			tagsTable[i] = new Tables.TagsTable(tags_arr[i]);
		}
		return tagsTable;
	}
	public static TagsTable getById(int id){
		try{
			String query="SELECT * FROM tags where `id`= ?" ;
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, id);
			ResultSet set = ps.executeQuery();
			if(set.next())
				return new TagsTable(set.getString("tag"), set.getInt("id"));
		}  catch(SQLException e) {
		} catch (Exception e) {
		}
		return null;
	}

	public static TagsTable[] getByUserId(int userId){
		try{
			String query="SELECT `tags`.* FROM `tags_in_users` INNER JOIN `tags` ON `tags_in_users`.`tag_id`=`tags`.`id` WHERE `tags_in_users`.`user_id`= ?" ;
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, userId);
			ResultSet set = ps.executeQuery();
			return convertToArray(set);
		}  catch(SQLException e) {
		} catch (Exception e) {
		}
		return new TagsTable[0];
	}

	public static boolean createTags(TagsTable[] tagsTable, Tables.UserTable userTable) throws Exception{
		if (tagsTable==null)
			return true;
		userTable = drivers.UserDriver.getByEmail(userTable.getEmail());
		try {
			for (TagsTable table : tagsTable) {
				String str = table.getTag();
				// check if the tag already exists in the table
				Integer tag_id = tagId(str);
				// if new tag, insert to tags table
				if (tag_id == -1) {
					if (!insertTags(str)) {
						return false;
					}
					tag_id = tagId(str);
				}
				// get tag id
				table.setId(tag_id);
				insertTagsInUsers(userTable, table);
				// insert tag-user row to tags_in_users_table
			}
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			return false;
		}


	}
	public static int tagId(String tag_name) {
		try{
			String query="SELECT * FROM tags where tag = ?" ;	
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setString(1, tag_name);
			ResultSet set = ps.executeQuery();
			if(!set.first()){
				return -1; // means false
			} else {
				return set.getInt("id");
			}
		}  catch(SQLException e) {
			return -1;
		} catch (Exception e) {
			return -1;
		} finally {

		}
	}
	public static boolean insertTags(String tag_name) {
		try{
			String query="INSERT INTO tags (tag) VALUES (?)";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setString(1, tag_name);
			ps.executeUpdate();
			return true; // means false
		}  catch(SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		} finally {

		}

	}
	public static boolean insertTagsInUsers(UserTable user,TagsTable tag) {
		try{
			String query="INSERT INTO tags_in_users (user_id,is_permanent,tagging_time,tag_id) VALUES (?,?,NOW(),?)" ;	
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, user.getId());
			ps.setBoolean(2, tag.getIsPermanent());
			ps.setInt(3, tag.getId());
			ps.executeUpdate();
			return true; // means false
		}  catch(SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		} finally {

		}
	}

	private static TagsTable[] convertToArray(ResultSet result) throws SQLException{
		int N = 0;
		while(result.next()) N++;
		result.beforeFirst();
		TagsTable[] tags = new TagsTable[N];
		int i=0;
		while(result.next())
			tags[i++] = new TagsTable(result.getString("tag"), result.getInt("id"));
		return tags;

	}
	public static UserTable[] getUsers(Integer tag_id) {
		try{
			String query="SELECT * FROM `tags_in_users` where tag_id=?" ;
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, tag_id);
			ResultSet set = ps.executeQuery();
			return convertToUserArray(set);
		}  catch(SQLException e) {
		} catch (Exception e) {
		}
		return new UserTable[0];
	}
	private static UserTable[] convertToUserArray(ResultSet result) throws SQLException{
		int N = 0;
		while(result.next()) N++;
		result.beforeFirst();
		UserTable[] users = new UserTable[N];
		int i=0;
		while(result.next())
			users[i++] = UserDriver.getById(result.getInt("user_id"));
		return users;

	}
	public static boolean insertTagsInPosts(int post_id,int tag_id) throws SQLException {
		try{
			String query="INSERT INTO tags_in_posts (post_id,tag_id) VALUES (?,?)" ;
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, post_id);
			ps.setInt(2, tag_id);
			ps.executeUpdate();
			return true; // means false
		}  catch(SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		} finally {

		}
	}

	public static boolean insertUserInPost(int userId, int postId){
		try{
			String query="INSERT INTO users_in_posts (user_id, post_id) VALUES (?,?)" ;	
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, userId);
			ps.setInt(2, postId);
			ps.executeUpdate();
			return true; // means false
		}  catch(SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		} finally {

		}
	}
	
	public static TagsTable getByPostId(int postId){
		try{
			String query="SELECT * FROM `tags_in_posts` INNER JOIN `tags` ON `tags`.`id`=`tags_in_posts`.`tag_id` WHERE `tags_in_posts`.`post_id`=?";	
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, postId);
			ResultSet set = ps.executeQuery();
			if(set.next())
				return new TagsTable(set.getString("tag"), set.getInt("id"));
		}  catch(SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return null;
	}
	
	public static UserTable getUserTagByPostId(int id){
		try{
			String query="SELECT * FROM `users_in_posts` INNER JOIN `users` ON `users`.`id`=`users_in_posts`.`user_id` WHERE `users_in_posts`.`post_id`=?";	
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			if(result.next())
				return new UserTable(result.getInt("id"), result.getString("email"), result.getString("name"),
							result.getString("password_hash"), result.getString("birth_date"), result.getString("register_date"));
		}  catch(SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static boolean removeTagFromUser(int userId,int tagId) {
		try{
			String query="DELETE FROM tags_in_users WHERE user_id=? and tag_id=?" ;	
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, userId);
			ps.setInt(2, tagId);
			ps.executeUpdate();
			return true; // means false
		}  catch(SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		} finally {

		}
	}
	public static TagsTable[] getPopularTags() {
		try {
			String query="SELECT id,tag,count(*) as count FROM tags_in_users INNER JOIN tags ON tag_id=id group by tag_id order by count desc limit 0,20";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ResultSet set = ps.executeQuery();
			return convertToArray(set);
		} catch(SQLException e) {
		} catch (Exception e) {
		}
		return new TagsTable[0];
		
	}
	public static TagsTable[] getRecentTags(int count) {
		try {
			String query="SELECT distinct tag_id as id,tag FROM tags_in_users INNER JOIN tags ON tag_id=id order by tagging_time desc limit 0,"+count;
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ResultSet set = ps.executeQuery();
			return convertToArray(set);
		} catch(SQLException e) {
		} catch (Exception e) {
		}
		return new TagsTable[0];
		
	}
}	

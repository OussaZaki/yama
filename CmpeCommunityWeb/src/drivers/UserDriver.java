package drivers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DBPack.DBStatement;
import Tables.UserTable;
import UtilityPack.HashString;

public abstract class UserDriver {
	public static boolean createUser(UserTable user){
		try{
			String query="INSERT INTO users(email, password_hash, name, birth_date, register_date) VALUES (?, ?, ?, NOW(), NOW())";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setString(1, user.getEmail());
			ps.setString(2, user.getPasswordHash());
			ps.setString(3, user.getName());
			ps.executeUpdate();
		} catch(SQLException e) {
			System.err.println("SQLException: "+e.getMessage());
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	public static boolean isCredentialsValid(String email, String password){
		try{
			String query="SELECT * FROM `users` WHERE `email`=? AND `password_hash`=?";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, HashString.encrypt(password));
			return ps.executeQuery().next();
		} catch(SQLException e) {
			System.err.println(e.getMessage());
			return false;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
	
	public static UserTable getById(int id){
		try{
			String query="SELECT * FROM `users` WHERE `id`=?";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			if(result.next()){
				return new UserTable(result.getInt("id"), result.getString("email"), result.getString("name"),
						result.getString("password_hash"), result.getString("birth_date"), result.getString("register_date"));
			}
			return null;
		} catch(SQLException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static UserTable getByEmail(String email){
		try{
			String query="SELECT * FROM `users` WHERE `email`=?";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setString(1, email);
			ResultSet result = ps.executeQuery();
			if(result.next()){
				return new UserTable(result.getInt("id"), result.getString("email"), result.getString("name"),
						result.getString("password_hash"), result.getString("birth_date"), result.getString("register_date"));
			}
			return null;
		} catch(SQLException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static UserTable[] getRecommendedByUserId(int userId){
		String tagRelatedQuery = "SELECT COUNT(`tiu`.`tag_id`) AS `relation`,`tiu`.`user_id` FROM (SELECT * FROM `tags_in_users` WHERE `tag_id` IN (SELECT `tag_id` FROM `tags_in_users` WHERE `user_id`=?)) AS `tiu` GROUP BY `tiu`.`user_id` ORDER BY `relation` DESC";
		String postRelatedQuery = "SELECT COUNT(`users_in_posts`.`post_id`) AS `relation`,`users_in_posts`.`user_id` FROM `users_in_posts` WHERE `users_in_posts`.`post_id` IN (SELECT `posts`.`id` FROM `posts` WHERE `owner_id`=?) GROUP BY `user_id` ORDER BY `relation` DESC";
		String finalQuery = "SELECT `users`.* FROM `users` INNER JOIN (("+tagRelatedQuery+") UNION ALL ("+postRelatedQuery+")) AS `tmp` ON `users`.`id`=`tmp`.`user_id` WHERE `users`.`id`<>? GROUP BY `tmp`.`user_id` ORDER BY SUM(`tmp`.`relation`)*RAND() DESC LIMIT 0,5";
		try{
			PreparedStatement ps = (PreparedStatement) DBStatement.getMainConnection().prepareStatement(finalQuery);
			ps.setInt(1, userId);
			ps.setInt(2, userId);
			ps.setInt(3, userId);
			ResultSet result = ps.executeQuery();
			return convertToArray(result);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return new UserTable[0];
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new UserTable[0];
		}
	}
	
	public static UserTable[] getUsersByTag(int tagId){
		try {
			String query="SELECT * FROM `users` INNER JOIN `tags_in_users` ON `users`.`id`=`tags_in_users`.`user_id` WHERE `tags_in_users`.`tag_id`=?";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, tagId);
			ResultSet result = ps.executeQuery();
			return convertToArray(result);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return new UserTable[0];
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new UserTable[0];
		}
	}
	
	public static UserTable[] getUsersByEvent(int eventId){
		try {
			String query="SELECT `users`.* FROM `users` INNER JOIN `users_in_event` ON `users`.`id`=`users_in_event`.`user_id` WHERE `users_in_event`.`event_id`=?";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, eventId);
			ResultSet result = ps.executeQuery();
			return convertToArray(result);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return new UserTable[0];
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new UserTable[0];
		}
	}
	
	private static UserTable[] convertToArray(ResultSet result) throws SQLException{
		int N = 0;
		while(result.next()) N++;
		result.beforeFirst();
		UserTable[] users = new UserTable[N];
		int i=0;
		while(result.next())
			users[i++] = new UserTable(result.getInt("id"), result.getString("email"), result.getString("name"),
					result.getString("password_hash"), result.getString("birth_date"), result.getString("register_date"));
		return users;
		
	}
	public static boolean updateUserSettings(int userId,String name,String password,String email) {
		try{
			if (password==null || password.equals("")) {
				String query="UPDATE users SET email=?,name=? where id=?";
				PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
				ps.setString(1,email);
				ps.setString(2,name);
				ps.setInt(3,userId);
				ps.executeUpdate();
			} else {
				String query="UPDATE users SET email=?,name=?,password_hash=? where id=?";
				PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
				ps.setString(1,email);
				ps.setString(2,name);
				ps.setString(3,HashString.encrypt(password));
				ps.setInt(4,userId);
				ps.executeUpdate();
			}
			return true;
		} catch(SQLException e) {
			System.err.println("SQLException: "+e.getMessage());
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			return false;
		}
	}
}

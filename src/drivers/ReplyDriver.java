package drivers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import DBPack.DBStatement;
import Tables.PostsTable;
import Tables.ReplyTable;

public class ReplyDriver {
	public static boolean insert(int postId, int ownerId, String body){
		try{
			String query="INSERT INTO `replies` (`post_id`, `owner_id`, `body`, `posting_time`) VALUES (?, ?, ?, NOW())" ;
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, postId);
			ps.setInt(2, ownerId);
			ps.setString(3, body);
			ps.executeUpdate();
			return true;
		}  catch(SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		} finally {

		}
	}
	
	public static ReplyTable[] getByPostId(int postId){
		try {
			String query="SELECT * FROM `replies` WHERE `post_id`=? ORDER BY `posting_time` ASC";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, postId);
			ResultSet result = ps.executeQuery();
			return convertToArray(result);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return new ReplyTable[0];
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ReplyTable[0];
		}
	}
	
	public static Map<Integer, ReplyTable[]> getReplies(PostsTable[] posts){
		Map<Integer, ReplyTable[]> rets = new TreeMap<Integer, ReplyTable[]>();
		for (PostsTable post : posts)
			rets.put(post.getId(), getByPostId(post.getId()));
		return rets;
	}
	
	private static ReplyTable[] convertToArray(ResultSet result) throws SQLException{
		int N = 0;
		while(result.next()) N++;
		result.beforeFirst();
		ReplyTable[] replies = new ReplyTable[N];
		int i=0;
		while(result.next())
			replies[i++] = new ReplyTable(result.getInt("id"), result.getInt("post_id"), result.getInt("owner_id"), result.getString("body"), result.getString("posting_time"));
		return replies;
	}
	/*
	private static Map<Integer, ReplyTable> convertToMap(ResultSet result) throws SQLException{
		Map<Integer, ReplyTable> replies = new TreeMap<Integer, ReplyTable>();
		while(result.next())
			replies.put(result.getInt("post_id"), new ReplyTable(result.getInt("id"), result.getInt("post_id"), result.getInt("owner_id"), result.getString("body"), result.getString("postingTime")));
		return replies;
	}*/
}

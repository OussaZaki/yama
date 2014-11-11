package drivers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DBPack.DBStatement;
import Tables.EventTable;

import com.mysql.jdbc.Statement;

public class EventDriver {
	public static EventTable[] getUserEvent(int userId) {
		try {
			String query="SELECT * FROM `events` WHERE `user_id`=? order by creation_time";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, userId);
			ResultSet result = ps.executeQuery();
			return convertEventToArray(result);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return new EventTable[0];
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new EventTable[0];
		}
	}
	public static EventTable[] getUserJoinedEvent(int userId) {
		try{
			String query="SELECT `events`.* FROM `events` INNER JOIN `users_in_event` ON `users_in_event`.`event_id`=`events`.`id` WHERE `users_in_event`.`user_id`= ? order by creation_time" ;
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, userId);
			ResultSet set = ps.executeQuery();
			return convertEventToArray(set);
		}  catch(SQLException e) {
			System.out.println(e.getMessage());
			return new EventTable[0];
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new EventTable[0];
		}
	}
	public static boolean createEvent(int userId,String description,String place,String eventTime) {
		try{
			String query="INSERT INTO events (user_id, place, event_time, description, creation_time) VALUES (?,?,?,?,NOW())" ;	
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, userId);
			ps.setString(2, place);
			ps.setString(3,eventTime);
			ps.setString(4, description);
			ps.executeUpdate();
			return true;
		}  catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}

	}
	public static EventTable getEvent(int EventId) {
		try {
			String query="SELECT * FROM `events` WHERE `id`=?";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, EventId);
			ResultSet result = ps.executeQuery();
			if(!result.next())
				return null;
			return new EventTable(result.getInt("id"),result.getInt("user_id"),result.getString("description"),result.getString("place"),result.getString("event_time"));
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	private static EventTable[] convertEventToArray(ResultSet result) throws SQLException{
		int N = 0;
		while(result.next()) N++;
		result.beforeFirst();
		EventTable[] events = new EventTable[N];
		int i=0;
		while(result.next())
			events[i++] = new EventTable(result.getInt("id"),result.getInt("user_id"),result.getString("description"),result.getString("place"),result.getString("event_time"));
		return events;
	}
	
	public static boolean insertUsersInEvent(int userId,int EventId) {
		try{
			String query="INSERT INTO `users_in_event` (`event_id`, `user_id`, `attending_time`) VALUES (?,?, NOW())" ;
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, EventId);
			ps.setInt(2, userId);
			ps.executeUpdate();
			return true;
		}  catch(SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean deleteUsersInEvent(int userId,int eventId) {
		try{
			String query = "DELETE FROM `users_in_event` WHERE `user_id`=? AND `event_id`=?";	
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, userId);
			ps.setInt(2, eventId);
			ps.executeUpdate();
			return true;
		}  catch(SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isAttending(int userId, int eventId){
		try{
			String query="SELECT * FROM `users_in_event` WHERE `user_id`=? AND `event_id`=?" ;	
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, userId);
			ps.setInt(2, eventId);
			ResultSet set = ps.executeQuery();
			if(set.next())
				return true;
			return false;
		}  catch(SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
}

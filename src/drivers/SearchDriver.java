package drivers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DBPack.DBStatement;
import Tables.TagsTable;

public class SearchDriver {

	public static String getSearchAutoComplete() {
		StringBuffer result=new StringBuffer();
		result.append(getAllTags());
		result.append(getAllForums());
		result.append(getAllUsers());
		return result.toString();
	}
	public static String getAllUsers() {
		String result="";
		String link="/YamaWeb/Profile/details/";
			try{
				String query="SELECT * FROM users" ;
				PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
				ResultSet set = ps.executeQuery();
				while (set.next()) {
					result=result+"{ value:"+"\""+link+set.getInt("id")+"\""+ "," + "label: "+"\""+set.getString("name")+"\"" +", category: \"Person\" },\n";
				}
					
			}  catch(SQLException e) {
				
			} catch (Exception e) {
			}
			return result;
	}
	public static String getAllTags() {
		String result="";
		String link="/YamaWeb/Tags/details/";
			try{
				String query="SELECT * FROM tags" ;
				PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
				ResultSet set = ps.executeQuery();
				while (set.next()) {
					result=result+"{ value:"+"\""+link+set.getInt("id")+"\""+ "," + "label: "+"\""+set.getString("tag")+"\"" +", category: \"Tag\" },\n";
				}
					
			}  catch(SQLException e) {
				
			} catch (Exception e) {
			}
			return result;
	}
	public static String getAllForums() {
		String result="";
		String link="/YamaWeb/Forum/index/";
			try{
				String query="SELECT * FROM forums" ;
				PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
				ResultSet set = ps.executeQuery();
				while (set.next()) {
					result=result+"{ value:"+"\""+link+set.getInt("id")+"\""+ "," + "label: "+"\""+set.getString("name")+"\"" +", category: \"Forum\" },\n";
				}
					
			}  catch(SQLException e) {
				
			} catch (Exception e) {
			}
			return result;
	}
		
}
